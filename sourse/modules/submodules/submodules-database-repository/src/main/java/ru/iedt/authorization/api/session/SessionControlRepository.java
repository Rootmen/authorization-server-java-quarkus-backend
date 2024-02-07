package ru.iedt.authorization.api.session;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import ru.iedt.authorization.models.session.SessionModel;
import ru.iedt.database.request.controller.DatabaseController;
import ru.iedt.database.request.controller.parameter.ParameterInput;

import java.util.HashMap;
import java.util.UUID;

@Singleton
public class SessionControlRepository {
    @Inject
    DatabaseController databaseController;

    public Multi<SessionModel> getAllSession(PgPool client) {
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "GET_ACTIVE_SESSION", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(SessionModel::from);
    }

    public Uni<SessionModel> getSession(String session, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("SESSION_ID", new ParameterInput("SESSION_ID", session));
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "GET_ACTIVE_SESSION", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? SessionModel.from(iterator.next()) : null);
    }

    public Uni<SessionModel> addSession(String sessionId,
                                        String sessionKey,
                                        UUID accountId,
                                        String serverPrivateKey,
                                        String serverPublicKey,
                                        String accountPublicKey,
                                        String scrambler,
                                        String authorizationKey,
                                        String signature,
                                        String ip,
                                        PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("SESSION_ID", new ParameterInput("SESSION_ID", sessionId));
        parameters.put("SESSION_KEY", new ParameterInput("SESSION_KEY", sessionKey));
        parameters.put("SESSION_ACCOUNT_ID", new ParameterInput("SESSION_ACCOUNT_ID", accountId.toString()));
        parameters.put("SESSION_SERVER_PRIVATE_KEY", new ParameterInput("SESSION_SERVER_PRIVATE_KEY", serverPrivateKey));
        parameters.put("SESSION_SERVER_PUBLIC_KEY", new ParameterInput("SESSION_SERVER_PUBLIC_KEY", serverPublicKey));
        parameters.put("SESSION_ACCOUNT_PUBLIC_KEY", new ParameterInput("SESSION_ACCOUNT_PUBLIC_KEY", accountPublicKey));
        parameters.put("SESSION_SCRAMBLER", new ParameterInput("SESSION_SCRAMBLER", scrambler));
        parameters.put("SESSION_AUTHORIZATION_KEY", new ParameterInput("SESSION_AUTHORIZATION_KEY", authorizationKey));
        parameters.put("SESSION_SIGNATURE", new ParameterInput("SESSION_SIGNATURE", signature));
        parameters.put("SESSION_IP_ADDRESS", new ParameterInput("SESSION_IP_ADDRESS", ip));
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "ADD_ACTIVE_SESSION", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? SessionModel.from(iterator.next()) : null);
    }

    public Uni<SessionModel> removeOutdatedSession(PgPool client) {
        return databaseController
                .runningQuerySet("SESSION_CONTROL", "REMOVE_OLD_ACTIVE_SESSION", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? SessionModel.from(iterator.next()) : null);
    }

}
