package io.quarkus.workshop.superheroes.hero;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.*;

@Path("/api/heroes")
@Tag(name = "heroes")
public class HeroResource {

    @Inject Logger logger;

    @GET
    @Path("/random")
    @Operation(summary = "Returns a random hero")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class, required = true)))
    public Uni<Hero> getRandomHero() {
        return Hero.findRandom()
            .invoke(h -> logger.debugf("Found random hero: %s", h));
    }

    @GET
    @Operation(summary = "Returns all the heroes from the database")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No heroes")
    public Uni<List<Hero>> getAllHeroes() {
        return Hero.listAll();
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
    @APIResponse(responseCode = "204", description = "The hero is not found for the fiven indentifier")
    public Uni<Response> getHero(@RestPath Long id) {
        return Hero.<Hero>findById(id)
            .map(hero -> {
                if (hero != null) return Response.ok(hero).build();
                logger.debugf("No hero found with id %s", id);
                return Response.noContent().build();
            });
    }
    
    @POST
    @Operation(summary = "Creates a valid hero")
    @APIResponse(responseCode = "201", description = "The URI of the created hero", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @ReactiveTransactional
    public Uni<Response> createHero(@Valid Hero hero, @Context UriInfo uriInfo) {
        return hero.<Hero>persist()
            .map(h -> {
                UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(h.id));
                logger.debug("New hero created with URI " + builder.build().toString());
                return Response.created(builder.build()).build();
            });
    }

    @PUT
    @Operation(summary = "Updates an existing hero")
    @APIResponse(responseCode = "200", description = "The updated hero", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Hero.class)))
    @ReactiveTransactional
    public Uni<Response> updateHero(@Valid Hero hero) {
        return Hero.<Hero>findById(hero.id)
            .map(retrieved -> {
                retrieved.name = hero.name;
                retrieved.otherName = hero.otherName;
                retrieved.level = hero.level;
                retrieved.picture = hero.picture;
                retrieved.powers = hero.powers;
                return retrieved;
            })
            .map(h -> {
                logger.debugf("Hero updated with new values %s", h);
                return Response.ok(h).build();
            });
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletes an existing hero")
    @APIResponse(responseCode = "204")
    @ReactiveTransactional
    public Uni<Response> deleteHero(@RestPath Long id) {
        return Hero.deleteById(id)
            .invoke(() -> logger.debugf("Hero deleted with id %d", id))
            .replaceWith(Response.noContent().build());
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    @Tag(name = "hello")
    public String hello() {
        return "Hello super hero :)";
    }

}