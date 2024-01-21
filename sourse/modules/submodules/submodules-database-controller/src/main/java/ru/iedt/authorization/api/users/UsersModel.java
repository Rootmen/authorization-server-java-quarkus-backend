package ru.iedt.authorization.api.users;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.iedt.authorization.api.users.dto.UserAccount;
import ru.iedt.database.request.controller.DatabaseController;

import java.util.HashMap;

@ApplicationScoped
public class UsersModel {
    @Inject
    DatabaseController databaseController;


    public Multi<UserAccount> getAllUserAccount(PgPool client) {
        return databaseController
                .runningQuerySet("users_model", "GET_ALL_USER_ACCOUNT", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(UserAccount::from);
    }
}
