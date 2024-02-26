package ru.iedt.authorization.rest.authorization.app;

import io.smallrye.mutiny.Multi;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import ru.iedt.authorization.rest.BaseRestController;
import ru.iedt.authorization.rest.session.ResultAppInformation;
import ru.iedt.authorization.service.session.SessionControlService;

@Path("api/v1/app/info")
public class AppInformation extends BaseRestController {

    @Inject
    SessionControlService sessionControlService;

    @Inject
    RoutingContext context;

    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Получение списка приложений", description = "Список приложений доступных для авторизации")
    public Multi<ResultAppInformation> getAppList() {
        return this.sessionControlService.getAppList();
    }
}
