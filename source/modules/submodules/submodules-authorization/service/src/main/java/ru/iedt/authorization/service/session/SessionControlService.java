package ru.iedt.authorization.service.session;

import static ru.iedt.authorization.crypto.SRP.getRandomString;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigInteger;
import java.util.*;
import ru.iedt.authorization.api.repository.information.AppInformationRepository;
import ru.iedt.authorization.api.repository.session.SessionControlRepository;
import ru.iedt.authorization.api.repository.users.UserAccountRepository;
import ru.iedt.authorization.crypto.EllipticDiffieHellman;
import ru.iedt.authorization.crypto.Equals;
import ru.iedt.authorization.crypto.SRP;
import ru.iedt.authorization.crypto.Streebog;
import ru.iedt.authorization.exception.AuthorizationException;
import ru.iedt.authorization.exception.SessionControlServiceException;
import ru.iedt.authorization.models.AppInformation;
import ru.iedt.authorization.models.Session;
import ru.iedt.authorization.models.UserAccount;
import ru.iedt.authorization.rest.session.ResultAppInformation;
import ru.iedt.authorization.rest.session.ResultConfirm;
import ru.iedt.authorization.rest.session.ResultInformation;

@Singleton
public class SessionControlService {

    @Inject
    SessionControlRepository sessionControlRepository;

    @Inject
    AppInformationRepository appInformationRepository;

    @Inject
    UserAccountRepository userAccountRepository;

    @Inject
    PgPool client;

    public Uni<ResultInformation> createSession(String xCord, String yCord, String accountPublicKey, UUID accountId, UUID appId, String fingerprint, String ip) {
        Uni<UserAccount> userAccountUni = getUserAccount(accountId);
        Uni<Void> isBlockUni = checkUserIsBlock(accountId, ip);
        Uni<AppInformation> appInformationUni = getAppInformation(appId);
        return Uni
            .combine()
            .all()
            .unis(userAccountUni, isBlockUni, appInformationUni)
            .asTuple()
            .onItem()
            .transformToUni(tuple -> {
                UserAccount userAccount = tuple.getItem1();
                String sessionId = getRandomString(75);
                EllipticDiffieHellman diffieHellman = new EllipticDiffieHellman();
                String key = diffieHellman.getSecret(xCord, yCord);
                BigInteger serverPrivateKey = SRP.generateServerPrivateKey();
                BigInteger serverPublicKey = SRP.generateServerPublicKey(userAccount.getAccountPasswordVerifier(), serverPrivateKey);
                BigInteger scrambler = new BigInteger(SRP.H(serverPublicKey.toString(16) + accountPublicKey), 16);
                String authorizationKey = SRP.getKeyServer(serverPublicKey, new BigInteger(accountPublicKey, 16), new BigInteger(userAccount.getAccountPasswordVerifier(), 16), serverPrivateKey);
                return sessionControlRepository
                    .addSession(sessionId, key, accountId, appId, serverPrivateKey.toString(16), serverPublicKey.toString(16), accountPublicKey, scrambler.toString(16), authorizationKey, getSignature(ip, fingerprint), ip, this.client)
                    .onItem()
                    .transform(sessionModel -> new ResultInformation(sessionId, accountId, serverPublicKey.toString(16), userAccount.getAccountSalt(), diffieHellman.getPublicKeyX(), diffieHellman.getPublicKeyY()));
            });
    }

    public Uni<ResultConfirm> confirmSession(String sessionId, String confirm, String fingerprint, String ip) {
        return sessionControlRepository
            .getSession(sessionId, client)
            .onItem()
            .transformToUni(session -> {
                Uni<UserAccount> userAccountUni = getUserAccount(session.getAccountId());
                Uni<Void> isBlockUni = checkUserIsBlock(session.getAccountId(), ip);
                return Uni.combine().all().unis(userAccountUni, isBlockUni, Uni.createFrom().item(session)).asTuple();
            })
            .onItem()
            .transformToUni(tuple -> {
                UserAccount userAccount = tuple.getItem1();
                Session session = tuple.getItem3();
                String serverConfirm = SRP.H(
                    new BigInteger(SRP.H(SRP.n.toString(16)), 16).xor(new BigInteger(SRP.H(SRP.g.toString(16)), 16)).toString(16) +
                    SRP.H(userAccount.getAccountName()) +
                    userAccount.getAccountSalt() +
                    session.getSessionAccountPublicKey().toString(16) +
                    session.getSessionServerPublicKey().toString(16) +
                    session.getSessionAuthorizationKey().toString(16)
                );
                String signature = getSignature(ip, fingerprint);
                try {
                    if (!Equals.equals(serverConfirm, confirm)) {
                        throw new SessionControlServiceException("Неверный пароль", "Invalid confirm");
                    } else if (!Equals.equals(signature, session.getSessionSignature())) {
                        throw new SessionControlServiceException("Неверный пароль", "Invalid signature");
                    }
                } catch (Exception e) {
                    authorizationLog(session, ip, signature, false);
                    throw e;
                }
                authorizationLog(session, ip, signature, true);
                String serverOutConfirm = SRP.H(session.getSessionAccountPublicKey() + confirm + session.getSessionAuthorizationKey());
                String token = getRandomString(50), refreshToken = getRandomString(70);
                return Uni.createFrom().item(new ResultConfirm(sessionId, userAccount.getAccountId(), serverOutConfirm, new ArrayList<>(), token, refreshToken));
            });
    }

    public Uni<UUID> getUserUUID(String accountName) {
        return userAccountRepository
            .getUserAccount(accountName, this.client)
            .onItem()
            .transform(userAccountModel -> {
                if (userAccountModel == null) throw new AuthorizationException("Пользователь не найден", "UserAccountModel is null");
                return userAccountModel.getAccountId();
            });
    }

    public Multi<ResultAppInformation> getAppList() {
        return appInformationRepository.getAllAppInfo(this.client).onItem().transform(appInformation -> new ResultAppInformation(appInformation.getAppId(), appInformation.getAppName(), appInformation.getRedirectUrl(), appInformation.getAppImage()));
    }

    private String getSignature(String ip, String fingerprint) {
        return Streebog.getHash(ip + fingerprint);
    }

    private void authorizationLog(Session session, String ip, String signature, boolean success) {
        sessionControlRepository.authorizationAttempt(session.getAccountId(), session.getAppId(), ip, signature, success, this.client).replaceWithVoid().subscribe().with(unused -> {});
    }

    private Uni<UserAccount> getUserAccount(UUID accountId) {
        return userAccountRepository
            .getUserAccount(accountId, this.client)
            .onItem()
            .transform(user -> {
                if (user == null) {
                    throw new SessionControlServiceException("Пользователь не найден", "User is null");
                } else if (user.isDeprecated()) {
                    throw new SessionControlServiceException("Пользователь удален из системы", "User is deprecated");
                }
                return user;
            });
    }

    private Uni<Void> checkUserIsBlock(UUID accountId, String ip) {
        return userAccountRepository
            .accountIsBlock(accountId, ip, this.client)
            .onItem()
            .transform(isBlock -> {
                if (isBlock.isBlock()) throw new SessionControlServiceException("Пользователь заблокирован на " + isBlock.getTimeToUnlock() + " мин.", "User is blocked");
                return null;
            })
            .replaceWithVoid();
    }

    private Uni<AppInformation> getAppInformation(UUID appId) {
        return appInformationRepository.getAppInfo(appId, this.client).onItem().ifNotNull().failWith(new SessionControlServiceException("Приложение не найдено", "Invalid appId"));
    }
}
