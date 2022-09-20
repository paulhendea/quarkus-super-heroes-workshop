package io.quarkus.workshop.superheroes.fight;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.List;

import org.eclipse.microprofile.faulttolerance.Timeout;

@Path("api/fights")
public class FightResource {
    
    @Inject Logger logger;
    @Inject FightService service;

    @GET
    public Response getAllFights() {
        List<Fight> fights = service.findAllFights();
        logger.debug("Total number of fights " + fights.size());
        return Response.ok(fights).build();
    }

    @GET
    @Path("/{id}")
    public Response getFight(Long id) {
        Fight fight = service.findFightById(id);
        if(fight != null) {
            logger.debug("found fight " + fight);
            return Response.ok(fight).build();
        } else {
            logger.debug("No fight found with id " + id);
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/randomfighters")
    @Timeout(1000)
    public Response getRandomfighters() {
        Fighters fighters = service.findRandomFighters();
        logger.debug("Get random fighters " + fighters);
        return Response.ok(fighters).build();
    }

    @POST
    public Fight fight(@Valid Fighters fighters, UriInfo uriinfo) {
        return service.persistFight(fighters);
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello Fight Resource :)";
    }

}
