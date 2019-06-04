package fi.jubic.snoozy.example.resources;

import fi.jubic.snoozy.example.User;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloWorldResource {
    @Inject
    public HelloWorldResource() {

    }

    @GET
    @PermitAll
    public Response hello() {
        return Response.ok("Hello public world!")
                .build();
    }

    @GET
    @Path("/{name}")
    @PermitAll
    public Response param(@PathParam("name") String name) {
        return Response.ok(String.format("Hello %s", name))
                .build();
    }

    @Path("/auth")
    @GET
    public Response authed() {
        return Response.ok("Hello private world!")
                .build();
    }

    @Path("/user")
    @GET
    public Response user(@Context User user) {
        return Response.ok("Hello " + user.name())
                .build();
    }
}
