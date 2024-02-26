package ru.iedt.authorization.rest.authorization.session;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestHeader;
import ru.iedt.authorization.rest.BaseRestController;
import ru.iedt.authorization.rest.session.ResultConfirm;
import ru.iedt.authorization.rest.session.ResultInformation;
import ru.iedt.authorization.service.session.SessionControlService;
import ru.iedt.authorization.validation.session.Confirm;
import ru.iedt.authorization.validation.session.Initialization;
import ru.iedt.authorization.validation.session.UserUUID;

@Path("api/v1/session")
public class Session extends BaseRestController {

    @Inject
    SessionControlService sessionControlService;

    @Inject
    RoutingContext context;

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Создание сессии", description = "Генерация информации о сессии и получение данных")
    public Uni<ResultInformation> createSession(Initialization session, @RestHeader("X-Account-id") UUID accountId) {
        String ip = context.request().remoteAddress().hostAddress();
        return this.sessionControlService.createSession(session.x_cord(), session.y_cord(), session.account_public_key(), accountId, session.app_id(), this.fingerprint, ip);
    }

    @POST
    @Path("confirm")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Подтверждение сессии", description = "Подтверждение сессии ")
    public Uni<ResultConfirm> confirmSession(Confirm session) {
        String ip = context.request().remoteAddress().hostAddress();
        return this.sessionControlService.confirmSession(session.session_id(), session.confirm(), this.fingerprint, ip);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("user/{username}")
    @Operation(summary = "Получение UUID пользователя", description = "Обмен имени пользователя на eго UUID")
    public Uni<UserUUID> geUserInfo(@PathParam("username") String username) {
        return this.sessionControlService.getUserUUID(username).onItem().transform(uuid -> new UserUUID(uuid, username));
    }

    @POST
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Обновление токена сессии", description = "Обновление сессии через токен")
    public Uni<ResultConfirm> updateSession(Confirm session) {
        String ip = context.request().remoteAddress().hostAddress();
        return this.sessionControlService.confirmSession(session.session_id(), session.confirm(), this.fingerprint, ip);
    }
}
