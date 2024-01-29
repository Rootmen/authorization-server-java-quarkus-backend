package ru.iedt.authorization.api.session;

import io.vertx.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import ru.iedt.database.request.controller.DatabaseController;

@ApplicationScoped
public class SessionControlRepository  {
    @Inject
    DatabaseController databaseController;

    @Inject
    PgPool pgPool;




}
