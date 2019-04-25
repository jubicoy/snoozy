package fi.jubic.snoozy.undertow;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("")
@Produces(MediaType.TEXT_PLAIN)
public class TestResource {
    @GET
    @PermitAll
    public Response test() {
        return Response.ok("TEST")
                .build();
    }
}
