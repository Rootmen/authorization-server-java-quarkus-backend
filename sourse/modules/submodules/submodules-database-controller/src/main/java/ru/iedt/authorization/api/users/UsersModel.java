package ru.iedt.authorization.api.users;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.iedt.authorization.api.users.dto.UserAccount;
import ru.iedt.database.request.controller.DatabaseController;
import ru.iedt.database.request.controller.parameter.ParameterInput;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class UsersModel {
    @Inject
    DatabaseController databaseController;


    public Uni<UserAccount> getUserAccount(UUID userId, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("USER_UUID", new ParameterInput( "USER_UUID", userId.toString()));
        return databaseController
                .runningQuerySet("users_model", "GET_USER_ACCOUNT_FROM_ID", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? UserAccount.from(iterator.next()) : null);
    }
    public Multi<UserAccount> getAllUserAccount(PgPool client) {
        return databaseController
                .runningQuerySet("users_model", "GET_ALL_USER_ACCOUNT", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(UserAccount::from);
    }
}
