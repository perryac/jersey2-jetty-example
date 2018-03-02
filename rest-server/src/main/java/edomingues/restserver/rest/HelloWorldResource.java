package edomingues.restserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.NameBinding;

import edomingues.restserver.model.Saying;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




@Path("/hello")
public class HelloWorldResource {
	
	private static final String TEMPLATE = "Hello there, %s!";

    @Context
    private ContainerRequestContext rc;

    public HelloWorldResource(@Context ContainerRequestContext _rc) {
        rc = _rc;
        rc.setProperty("REQUEST_ID", RequestTracking.getRequestTracker().nextID());
    }
	
	@GET
	@Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Saying sayHello(@PathParam("name") String name) {
		return new Saying(String.format(TEMPLATE, name));
    }
}

