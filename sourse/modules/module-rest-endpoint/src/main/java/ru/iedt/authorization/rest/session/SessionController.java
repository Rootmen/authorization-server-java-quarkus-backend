package ru.iedt.authorization.rest.session;

import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import ru.iedt.authorization.models.session.output.SessionAuthorizationInfoModel;
import ru.iedt.authorization.rest.BaseRestController;
import ru.iedt.authorization.service.session.SessionControlService;

import java.util.UUID;

@Path("api/v1/session")
public class SessionController extends BaseRestController {

    @Inject
    SessionControlService sessionControlService;
    @Inject
    RoutingContext context;

    UUID accountId;

    @POST
    @Path("create")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<SessionAuthorizationInfoModel> createSession(
            String xCord,
            String yCord,
            String accountPublicKey
    ) {
        String ip = context.request().remoteAddress().hostAddress();
        return this.sessionControlService.createSession(xCord, yCord, accountPublicKey, this.accountId, this.fingerprint, ip);
    }
}
