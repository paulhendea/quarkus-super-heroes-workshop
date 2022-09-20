package io.quarkus.workshop.superheroes.fight;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.quarkus.workshop.superheroes.fight.client.Hero;
import io.quarkus.workshop.superheroes.fight.client.HeroProxy;
import io.quarkus.workshop.superheroes.fight.client.Villain;
import io.quarkus.workshop.superheroes.fight.client.VillainProxy;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(SUPPORTS)
public class FightService {
    
    @RestClient HeroProxy heroProxy;
    @RestClient VillainProxy villainProxy;
    
    @Inject Logger logger;

    private final Random random = new Random();

    public List<Fight> findAllFights() {
        return Fight.listAll();
    }

    public Fight findFightById(Long id) {
        return Fight.findById(id);
    }

    public Fighters findRandomFighters() {
        Hero hero = findRandomHero();
        Villain villain = findRandomVillain();
        Fighters fighters = new Fighters();
        fighters.hero = hero;
        fighters.villain = villain;
        return fighters;
    }

    @Fallback(fallbackMethod = "fallbackRandomHero")
    Hero findRandomHero() {
        return heroProxy.findRandomHero();
    }

    @Fallback(fallbackMethod = "fallbackRandomVillain")
    Villain findRandomVillain() {
        return villainProxy.findRandomVillain();
    }

    public Hero fallbackRandomHero() {
        logger.warn("Falling back on Hero");
        Hero hero = new Hero();
        hero.name = "Fallback hero";
        hero.picture = "https://dummyimage.com/280x380/1e8fff/ffffff&text=Fallback+Hero";
        hero.powers = "Fallback hero powers";
        hero.level = 1;
        return hero;
    }

    public Villain fallbackRandomVillain() {
        logger.warn("Falling back on Villain");
        Villain villain = new Villain();
        villain.name = "Fallback villain";
        villain.picture = "https://dummyimage.com/280x380/b22222/ffffff&text=Fallback+Villain";
        villain.powers = "Fallback villain powers";
        villain.level = 42;
        return villain;
    }

    @Transactional(REQUIRED)
    public Fight persistFight(Fighters fighters) {
        // Amazingly fancy logic to determine the winner... xd
        Fight fight;

        // The level will have a multiplier between 0.5 and 1.5
        double heroMultiplier = random.nextDouble() + 0.5;
        double villainMultiplier = random.nextDouble() + 0.5;

        double heroLevel = fighters.hero.level * heroMultiplier;
        double villainLevel = fighters.villain.level * villainMultiplier; 

        if(heroLevel > villainLevel) {
            fight = heroWon(fighters);
        } else if (villainLevel > heroLevel) {
            fight = villainWon(fighters);
        } else  {
            fight = random.nextBoolean() ? heroWon(fighters) : villainWon(fighters);
        }

        fight.fightDate = Instant.now();
        fight.persist();

        return fight;
    }

    private Fight heroWon(Fighters fighters) {
        logger.info("Yes, Hero won :D");
        Fight fight = new Fight();
        fight.winnerName = fighters.hero.name;
        fight.winnerPicture = fighters.hero.picture;
        fight.winnerLevel = fighters.hero.level;
        fight.winnerTeam = "heroes";
        fight.loserName = fighters.villain.name;
        fight.loserPicture = fighters.villain.picture;
        fight.loserLevel = fighters.villain.level;
        fight.loserTeam = "villains";
        return fight;
    }

    private Fight villainWon(Fighters fighters) {
        logger.info("Oh no, Villain won :C");
        Fight fight = new Fight();
        fight.winnerName = fighters.villain.name;
        fight.winnerPicture = fighters.villain.picture;
        fight.winnerLevel = fighters.villain.level;
        fight.winnerTeam = "villains";
        fight.loserName = fighters.hero.name;
        fight.loserPicture = fighters.hero.picture;
        fight.loserLevel = fighters.hero.level;
        fight.loserTeam = "heroes";
        return fight;
    }

}
