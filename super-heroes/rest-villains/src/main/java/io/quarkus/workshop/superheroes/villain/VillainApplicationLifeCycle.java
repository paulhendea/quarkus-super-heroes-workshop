package io.quarkus.workshop.superheroes.villain;

import javax.enterprise.event.Observes;

import org.jboss.logging.Logger;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class VillainApplicationLifeCycle {
    
    private static final Logger LOGGER = Logger.getLogger(VillainApplicationLifeCycle.class);

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("\n __  __       ___    ___                             ______  ____    ______     \n/\\ \\/\\ \\  __ /\\_ \\  /\\_ \\             __            /\\  _  \\/\\  _`\\ /\\__  _\\    \n\\ \\ \\ \\ \\/\\_\\\\//\\ \\ \\//\\ \\      __   /\\_\\    ___    \\ \\ \\L\\ \\ \\ \\L\\ \\/_/\\ \\/    \n \\ \\ \\ \\ \\/\\ \\ \\ \\ \\  \\ \\ \\   /'__`\\ \\/\\ \\ /' _ `\\   \\ \\  __ \\ \\ ,__/  \\ \\ \\    \n  \\ \\ \\_/ \\ \\ \\ \\_\\ \\_ \\_\\ \\_/\\ \\L\\.\\_\\ \\ \\/\\ \\/\\ \\   \\ \\ \\/\\ \\ \\ \\/    \\_\\ \\__ \n   \\ `\\___/\\ \\_\\/\\____\\/\\____\\ \\__/.\\_\\\\ \\_\\ \\_\\ \\_\\   \\ \\_\\ \\_\\ \\_\\    /\\_____\\\n    `\\/__/  \\/_/\\/____/\\/____/\\/__/\\/_/ \\/_/\\/_/\\/_/    \\/_/\\/_/\\/_/    \\/_____/");
        LOGGER.info("The application VILLAIN is starting with profile " + ProfileManager.getActiveProfile());
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application VILLAIN is stopping...");
    }

}
