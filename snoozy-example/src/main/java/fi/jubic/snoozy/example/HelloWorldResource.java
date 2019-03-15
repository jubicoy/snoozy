package fi.jubic.snoozy.example;

import fi.jubic.snoozy.auth.implementation.StatefulAuthenticator;
import fi.jubic.snoozy.auth.implementation.Token;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloWorldResource {
    private StatefulAuthenticator<User> authenticator;

    @Inject
    public HelloWorldResource(StatefulAuthenticator<User> authenticator) {
        this.authenticator = authenticator;
    }

    @GET
    @PermitAll
    public Response hello() {
        return Response.ok("Hello world").build();
    }

    @Path("/auth")
    @GET
    public Response authed() {
        return Response.ok("Hello restricted world")
                .build();
    }

    @Path("/login")
    @POST
    @PermitAll
    public String login() {
        User user = User.builder()
                .setName("First Last")
                .setRole("Admin")
                .build();

        Token<User> token = new Token<>(user);

        authenticator.addToken(token);

        return token.getToken();
    }
}
