package ru.iedt.authorization.api.app.info;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.RowSet;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import ru.iedt.authorization.models.app.AppInfoModel;
import ru.iedt.authorization.models.session.SessionModel;
import ru.iedt.database.request.controller.DatabaseController;
import ru.iedt.database.request.controller.parameter.ParameterInput;

import java.util.HashMap;
import java.util.UUID;

@Singleton
public class AppInfoRepository {
    @Inject
    DatabaseController databaseController;

    public Multi<AppInfoModel> getAllAppInfo(PgPool client) {
        return databaseController
                .runningQuerySet("APP_LIST", "GET_APP_LIST", new HashMap<>(), client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(AppInfoModel::from);
    }

    public Uni<AppInfoModel> getAppInfo(String name, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("APP_NAME", new ParameterInput("APP_NAME", name));
        return databaseController
                .runningQuerySet("APP_LIST", "GET_APP_LIST", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? AppInfoModel.from(iterator.next()) : null);
    }

    public Uni<AppInfoModel> getAppInfo(UUID appId, PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("APP_ID", new ParameterInput("APP_ID", appId.toString()));
        return databaseController
                .runningQuerySet("APP_LIST", "GET_APP_LIST", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? AppInfoModel.from(iterator.next()) : null);
    }

    public Uni<AppInfoModel> addAppInfo(UUID appId,
                                        String appName,
                                        String appSecret,
                                        String appTokenSecret,
                                        String redirectUrl,
                                        PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("APP_ID", new ParameterInput("APP_ID", appId.toString()));
        parameters.put("APP_NAME", new ParameterInput("APP_NAME", appName));
        parameters.put("APP_SECRET", new ParameterInput("APP_SECRET", appSecret));
        parameters.put("APP_TOKEN_SECRET", new ParameterInput("APP_TOKEN_SECRET", appTokenSecret));
        parameters.put("REDIRECT_URL", new ParameterInput("REDIRECT_URL", redirectUrl));
        return databaseController
                .runningQuerySet("APP_LIST", "ADD_APP_LIST", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? AppInfoModel.from(iterator.next()) : null);
    }

    public Uni<AppInfoModel> updateAppInfo(UUID appId,
                                           String appName,
                                           String appSecret,
                                           String appTokenSecret,
                                           String redirectUrl,
                                           PgPool client) {
        HashMap<String, ParameterInput> parameters = new HashMap<>();
        parameters.put("APP_ID", new ParameterInput("APP_ID", appId.toString()));
        parameters.put("APP_NAME", new ParameterInput("APP_NAME", appName));
        parameters.put("APP_SECRET", new ParameterInput("APP_SECRET", appSecret));
        parameters.put("APP_TOKEN_SECRET", new ParameterInput("APP_TOKEN_SECRET", appTokenSecret));
        parameters.put("REDIRECT_URL", new ParameterInput("REDIRECT_URL", redirectUrl));
        return databaseController
                .runningQuerySet("APP_LIST", "UPDATE_APP_LIST", parameters, client)
                .onItem()
                .transform(element -> element.get(0).get("main"))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? AppInfoModel.from(iterator.next()) : null);
    }
}
