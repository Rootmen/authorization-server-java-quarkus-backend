package ru.iedt.authorization.api.users;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.UUID;
import ru.iedt.authorization.models.UserAccount;
import ru.iedt.database.request.controller.DatabaseController;
import ru.iedt.database.request.controller.parameter.ParameterInput;

@Singleton
public class UserAccountRepository {

    @Inject
    DatabaseController databaseController;

    public Multi<UserAccount> getAllUserAccount(PgPool client) {
        return databaseController
            .runningQuerySet("USERS", "GET_USER_ACCOUNT", new HashMap<>(), client)
            .onItem()
            .transform(element -> element.get(0).get("main"))
            .onItem()
            .transformToMulti(set -> Multi.createFrom().iterable(set))
            .onItem()
            .transform(UserAccount::from);
    }

    public Uni<UserAccount> getUserAccount(String accountName, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("ACCOUNT_NAME", new ParameterInput("ACCOUNT_NAME", accountName));
        return databaseController
            .runningQuerySet("USERS", "GET_USER_ACCOUNT", parameters, client)
            .onItem()
            .transform(element -> element.get(0).get("main"))
            .onItem()
            .transform(RowSet::iterator)
            .onItem()
            .transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }

    public Uni<UserAccount> getUserAccount(UUID userId, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("ACCOUNT_UUID", new ParameterInput("ACCOUNT_UUID", userId.toString()));
        return databaseController
            .runningQuerySet("USERS", "GET_USER_ACCOUNT", parameters, client)
            .onItem()
            .transform(element -> element.get(0).get("main"))
            .onItem()
            .transform(RowSet::iterator)
            .onItem()
            .transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }

    public Uni<UserAccount> addUserAccount(String accountName, String userMail, String passwordVerifier, String salt, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("ACCOUNT_NAME", new ParameterInput("ACCOUNT_NAME", accountName));
        parameters.put("ACCOUNT_MAIL", new ParameterInput("ACCOUNT_MAIL", userMail));
        parameters.put("ACCOUNT_PASSWORD_VERIFIER", new ParameterInput("ACCOUNT_PASSWORD_VERIFIER", passwordVerifier));
        parameters.put("ACCOUNT_SALT", new ParameterInput("ACCOUNT_SALT", salt));
        return databaseController
            .runningQuerySet("USERS", "ADD_USER_ACCOUNT", parameters, client)
            .onItem()
            .transform(element -> element.get(0).get("main"))
            .onItem()
            .transform(RowSet::iterator)
            .onItem()
            .transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }

    public Uni<UserAccount> updateUserAccount(UserAccount userAccount, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("ACCOUNT_UUID", new ParameterInput("ACCOUNT_NAME", userAccount.getAccountId().toString()));
        parameters.put("ACCOUNT_NAME", new ParameterInput("ACCOUNT_NAME", userAccount.getAccountName()));
        parameters.put("ACCOUNT_MAIL", new ParameterInput("ACCOUNT_MAIL", userAccount.getAccountMail()));
        parameters.put("ACCOUNT_PASSWORD_VERIFIER", new ParameterInput("ACCOUNT_PASSWORD_VERIFIER", userAccount.getAccountPasswordVerifier()));
        parameters.put("ACCOUNT_SALT", new ParameterInput("ACCOUNT_SALT", userAccount.getAccountSalt()));
        parameters.put("ACCOUNT_PASSWORD_RESET_INTERVAL", new ParameterInput("ACCOUNT_PASSWORD_RESET_INTERVAL", Integer.toString(userAccount.getAccountPasswordResetInterval())));
        return databaseController
            .runningQuerySet("USERS", "UPDATE_USER_ACCOUNT", parameters, client)
            .onItem()
            .transform(element -> element.get(0).get("main"))
            .onItem()
            .transform(RowSet::iterator)
            .onItem()
            .transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }
}
