package ru.iedt.authorization.service.session;

import static ru.iedt.authorization.crypto.SRP.getRandomString;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.math.BigInteger;
import java.util.UUID;
import ru.iedt.authorization.api.session.SessionControlRepository;
import ru.iedt.authorization.api.users.UserAccountRepository;
import ru.iedt.authorization.crypto.EllipticDiffieHellman;
import ru.iedt.authorization.crypto.Equals;
import ru.iedt.authorization.crypto.SRP;
import ru.iedt.authorization.exception.SessionControlServiceException;
import ru.iedt.authorization.models.session.output.SessionAuthorizationConfirmModel;
import ru.iedt.authorization.models.session.output.SessionAuthorizationInfoModel;
import ru.iedt.authorization.models.user.UserAccountModel;

@Singleton
public class SessionControlService {

    @Inject
    SessionControlRepository sessionControlRepository;

    @Inject
    UserAccountRepository userAccountRepository;

    @Inject
    PgPool client;

    public Uni<SessionAuthorizationInfoModel> createSession(String xCord, String yCord, String accountPublicKey, UUID accountId, String signature, String ip) {
        return userAccountRepository
            .getUserAccount(accountId, this.client)
            .onItem()
            .transformToUni(user -> {
                if (user.isDeprecated()) {
                    throw new RuntimeException(new SessionControlServiceException("User delete", "User is deprecated"));
                }
                String sessionId = getRandomString(75);
                EllipticDiffieHellman diffieHellman = new EllipticDiffieHellman();
                String key = diffieHellman.getSecret(xCord, yCord);
                BigInteger serverPrivateKey = SRP.generateServerPrivateKey();
                BigInteger serverPublicKey = SRP.generateServerPublicKey(user.getAccountPasswordVerifier(), serverPrivateKey);
                BigInteger scrambler = new BigInteger(SRP.H(serverPublicKey.toString(16) + accountPublicKey), 16);
                String authorizationKey = SRP.getKeyServer(serverPublicKey, new BigInteger(accountPublicKey, 16), new BigInteger(user.getAccountPasswordVerifier(), 16), serverPrivateKey);
                return sessionControlRepository
                    .addSession(sessionId, key, accountId, serverPrivateKey.toString(), serverPublicKey.toString(), accountPublicKey, scrambler.toString(), authorizationKey, signature, ip, this.client)
                    .onItem()
                    .transform(sessionModel -> new SessionAuthorizationInfoModel(sessionId, accountId, serverPublicKey.toString(16), user.getAccountSalt(), diffieHellman.getPublicKeyX(), diffieHellman.getPublicKeyY()));
            });
    }

    public Uni<SessionAuthorizationConfirmModel> confirmSession(String sessionId, String confirm, String signature) {
        return sessionControlRepository
            .getSession(sessionId, client)
            .onItem()
            .transformToUni(sessionModel ->
                userAccountRepository
                    .getUserAccount(sessionModel.getSessionAccountId(), this.client)
                    .onItem()
                    .transform(userAccountModel -> {
                        String serverConfirm = SRP.H(
                            new BigInteger(SRP.H(SRP.n.toString(16)), 16).xor(new BigInteger(SRP.H(SRP.g.toString(16)), 16)).toString(16) +
                            SRP.H(userAccountModel.getAccountName()) +
                            userAccountModel.getAccountSalt() +
                            sessionModel.getSessionAccountPublicKey() +
                            sessionModel.getSessionServerPublicKey() +
                            sessionModel.getSessionAuthorizationKey()
                        );
                        if (!Equals.equals(serverConfirm, confirm)) {
                            throw new RuntimeException(new SessionControlServiceException("Confirm is not true", "Invalid confirm"));
                        }
                        if (!Equals.equals(signature, sessionModel.getSessionSignature())) {
                            throw new RuntimeException(new SessionControlServiceException("Signature is not true", "Invalid signature"));
                        }
                        String serverOutConfirm = SRP.H(sessionModel.getSessionAccountPublicKey() + confirm + sessionModel.getSessionAuthorizationKey());
                        String token = getRandomString(50), updateToken = getRandomString(70);
                        return new SessionAuthorizationConfirmModel(sessionId, userAccountModel.getAccountId(), serverOutConfirm, null, token, updateToken);
                    })
            );
    }

    public Uni<UUID> getUserUUID(String accountName) {
        return userAccountRepository.getUserAccount(accountName, this.client).onItem().transform(UserAccountModel::getAccountId);
    }
}
