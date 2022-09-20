package io.quarkus.workshop.superheroes.fight.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api/villains")
@RegisterRestClient(configKey = "villain-proxy")
public interface VillainProxy {
    
    @GET
    @Path("/random")
    Villain findRandomVillain();

}
