package io.quarkus.workshop.superheroes.fight;

import org.jboss.logging.Logger;

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
    
    @Inject Logger logger;

    private final Random random = new Random();

    public List<Fight> findAllFights() {
        return Fight.listAll();
    }

    public Fight findFightById(Long id) {
        return Fight.findById(id);
    }

    public Fighters findRandomFighters() {
        // TODO implement later
        return null;
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
