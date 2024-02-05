package ru.iedt.authorization.api.session;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import ru.iedt.authorization.api.session.dto.Session;
import ru.iedt.authorization.api.users.dto.UserAccountModel;
import ru.iedt.database.request.controller.DatabaseController;
import ru.iedt.database.request.controller.parameter.ParameterInput;

import java.util.HashMap;
import java.util.UUID;

@Singleton
public class SessionControlRepository {
    @Inject
    DatabaseController databaseController;

    public Multi<Session> getAllSession(PgPool client) {
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "GET_ACTIVE_SESSION", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Session::from);
    }

    public Uni<Session> getSession(UUID session, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("SESSION_ID", new ParameterInput("SESSION_ID", session.toString()));
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "GET_ACTIVE_SESSION", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? Session.from(iterator.next()) : null);
    }

    public Uni<Session> addSession(UUID session, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("SESSION_ID", new ParameterInput("SESSION_ID", session.toString()));
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "GET_ACTIVE_SESSION", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? Session.from(iterator.next()) : null);
    }

    public Uni<Session> removeOutdatedSession(PgPool client) {
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "REMOVE_OLD_ACTIVE_SESSION", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? Session.from(iterator.next()) : null);
    }

}
