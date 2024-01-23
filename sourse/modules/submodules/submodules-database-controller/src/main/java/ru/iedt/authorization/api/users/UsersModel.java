package ru.iedt.authorization.api.users;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.UUID;
import ru.iedt.authorization.api.users.dto.UserAccount;
import ru.iedt.authorization.api.users.dto.UserInfo;
import ru.iedt.database.request.controller.DatabaseController;
import ru.iedt.database.request.controller.parameter.ParameterInput;

@ApplicationScoped
public class UsersModel {
    @Inject
    DatabaseController databaseController;

    public Uni<UserInfo> getUserInfo(UUID userId, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("USER_UUID", new ParameterInput("USER_UUID", userId.toString()));
        return databaseController
                .runningQuerySet("users_model", "GET_USER_INFO_FROM_ID", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserInfo.from(iterator.next()) : null);
    }

    public Multi<UserInfo> getAllUserInfo(PgPool client) {
        return databaseController
                .runningQuerySet("users_model", "GET_ALL_USER_INFO", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserInfo::from);
    }

    public Multi<UserAccount> getAllUserAccount(PgPool client) {
        return databaseController
                .runningQuerySet("users_model", "GET_USER_ACCOUNT", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserAccount::from);
    }

    public Uni<UserAccount> getUserAccount(String username, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("USER_NAME", new ParameterInput("USER_NAME", username));
        return databaseController
                .runningQuerySet("users_model", "GET_USER_ACCOUNT", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }

    public Uni<UserAccount> getUserAccount(UUID userId, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("USER_UUID", new ParameterInput("USER_UUID", userId.toString()));
        return databaseController
                .runningQuerySet("users_model", "GET_USER_ACCOUNT", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }

    public Uni<UserAccount> addUserAccount(
            String username, String userMail, String passwordVerifier, String salt, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("USER_NAME", new ParameterInput("USER_NAME", username));
        parameters.put("USER_MAIL", new ParameterInput("USER_MAIL", userMail));
        parameters.put("PASSWORD_VERIFIER", new ParameterInput("PASSWORD_VERIFIER", passwordVerifier));
        parameters.put("SALT", new ParameterInput("SALT", salt));
        return databaseController
                .runningQuerySet("users_model", "ADD_USER_ACCOUNT", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }
}
