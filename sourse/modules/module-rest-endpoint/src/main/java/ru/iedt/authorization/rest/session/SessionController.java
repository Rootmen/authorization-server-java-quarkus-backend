package ru.iedt.authorization.rest.session;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestHeader;
import ru.iedt.authorization.models.session.output.SessionAuthorizationConfirmModel;
import ru.iedt.authorization.models.session.output.SessionAuthorizationInfoModel;
import ru.iedt.authorization.rest.BaseRestController;
import ru.iedt.authorization.service.session.SessionControlService;

@Path("api/v1/session")
public class SessionController extends BaseRestController {

    @Inject
    SessionControlService sessionControlService;

    @Inject
    RoutingContext context;

    @Schema(name = "InitSession", description = "Данные для создания сессии")
    public record InitSession(
        @NotBlank @Schema(title = "x координата для эллиптической кривой", required = true, pattern = "[1-9a-f][0-9a-f]+") String x_cord,
        @NotBlank @Schema(title = "у координата для эллиптической кривой", required = true, pattern = "[1-9a-f][0-9a-f]+") String y_cord,
        @NotBlank @Schema(title = "Публичный ключ для протокола SRP-6A", required = true, pattern = "[1-9a-f][0-9a-f]+") String account_public_key
    ) {}

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Создание сессии", description = "Генерация информации о сессии и получение данных")
    public Uni<SessionAuthorizationInfoModel> createSession(InitSession session, @RestHeader("X-Account-id") UUID accountId) {
        String ip = context.request().remoteAddress().hostAddress();
        return this.sessionControlService.createSession(session.x_cord, session.y_cord, session.account_public_key, accountId, this.fingerprint + ip, ip);
    }

    @Schema(name = "ConfirmSession", description = "Данные для подтверждения сессии")
    public record ConfirmSession(
        @NotBlank @Schema(title = "id сессии", required = true, pattern = "[1-9a-f][0-9a-f]+") String session_id,
        @NotBlank @Schema(title = "Подтверждение сессии от клиента", required = true, pattern = "[1-9a-f][0-9a-f]+") String confirm
    ) {}

    @POST
    @Path("confirm")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Подтверждение сессии", description = "Подтверждение сессии ")
    public Uni<SessionAuthorizationConfirmModel> confirmSession(ConfirmSession session) {
        String ip = context.request().remoteAddress().hostAddress();
        return this.sessionControlService.confirmSession(session.session_id, session.confirm, this.fingerprint + ip);
    }

    @Schema(name = "UserUUID", description = "Данные о соответствии UUID пользователя и имени")
    public record UserUUID(UUID uuid, String username) {}

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
    public Uni<SessionAuthorizationConfirmModel> updateSession(ConfirmSession session) {
        String ip = context.request().remoteAddress().hostAddress();
        return this.sessionControlService.confirmSession(session.session_id, session.confirm, this.fingerprint + ip);
    }
}
