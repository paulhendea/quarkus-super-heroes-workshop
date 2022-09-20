package io.quarkus.workshop.superheroes.fight.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api/heroes")
@RegisterRestClient(configKey = "hero-proxy")
public interface HeroProxy {
    
    @GET
    @Path("/random")
    Hero findRandomHero();

}
