package ru.iedt.authorization.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import ru.iedt.authorization.exception.AuthorizationException;

@ApplicationScoped
public class BaseRestController {

    // ключ капчи в запросе
    @RestHeader("X-Captcha-Handshake")
    protected String captchaHandshake;

    @RestHeader("X-Fingerprint")
    protected String fingerprint;

    @ServerExceptionMapper
    public RestResponse<String> mapException(AuthorizationException x) {
        return RestResponse.status(Response.Status.BAD_REQUEST, String.format("{ \"error_message\": true, \"error_message\": \"%s\"}", x.getMessage()));
    }

    @ServerRequestFilter
    public Optional<Response> getFilter(ContainerRequestContext ctx) {
        String fingerprint = ctx.getHeaderString("X-Fingerprint");
        if (fingerprint == null || fingerprint.trim().isEmpty()) {
            return Optional.ofNullable(RestResponse.ResponseBuilder.ok("No fingerprint in request", MediaType.TEXT_PLAIN_TYPE).status(Response.Status.BAD_REQUEST).build().toResponse());
        }
        String captcha = ctx.getHeaderString("X-Captcha-Handshake");
        if (captcha == null || captcha.trim().isEmpty()) {
            return Optional.ofNullable(RestResponse.ResponseBuilder.ok("No captcha in request", MediaType.TEXT_PLAIN_TYPE).status(Response.Status.BAD_REQUEST).build().toResponse());
        }
        return Optional.empty();
    }

    public BaseRestController() {}
}
