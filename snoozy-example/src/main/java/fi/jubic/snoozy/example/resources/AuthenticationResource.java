package fi.jubic.snoozy.example.resources;

import fi.jubic.snoozy.auth.implementation.LoginRequest;
import fi.jubic.snoozy.auth.implementation.StatefulAuthenticator;
import fi.jubic.snoozy.auth.implementation.Token;
import fi.jubic.snoozy.example.User;
import fi.jubic.snoozy.example.auth.HashUtil;
import fi.jubic.snoozy.example.repo.UserRepo;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {
    private final StatefulAuthenticator<User> authenticator;
    private final UserRepo userRepo;

    @Inject
    public AuthenticationResource(
            StatefulAuthenticator<User> authenticator,
            UserRepo userRepo
    ) {
        this.authenticator = authenticator;
        this.userRepo = userRepo;
    }

    @Path("/login")
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Token<User> login(LoginRequest loginRequest) {
        User user = userRepo.getUser(loginRequest.username())
                .orElseThrow(BadRequestException::new);

        if (!HashUtil.test(loginRequest.password(), user.hash(), user.salt())) {
            throw new BadRequestException();
        }

        Token<User> token = new Token<>(user);
        authenticator.addToken(token);

        return token;
    }

    @Path("/logout")
    @POST
    public Response logout(
            @Context User user,
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization
    ) {
        authenticator.revokeToken(
                new Token<>(user, authorization)
        );

        return Response.ok()
                .build();
    }
}
