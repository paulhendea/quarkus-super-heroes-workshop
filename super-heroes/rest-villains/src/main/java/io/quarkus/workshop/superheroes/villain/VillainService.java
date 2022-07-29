package io.quarkus.workshop.superheroes.villain;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
@Transactional(REQUIRED)
public class VillainService {
	
	@Transactional(SUPPORTS)
	public List<Villain> findAllVillains() {
		return Villain.listAll();
	}
	
	@Transactional(SUPPORTS)
	public Villain findVillainById(Long id) {
		return Villain.findById(id);
	}
	
	@Transactional(SUPPORTS)
	public Villain findRandomVillain() {
		Villain randomVillain = null;
		do {
			randomVillain = Villain.findRandom();
		} while (randomVillain == null);
		return randomVillain;
	}
	
	public Villain persistVillain(@Valid Villain villain) {
		villain.persist();
		return villain;
	}
	
	public Villain updateVillain(@Valid Villain villain) {
		Villain entity = Villain.findById(villain.id);
		entity.name = villain.name;
		entity.otherName = villain.otherName;
		entity.level = villain.level;
		entity.picture = villain.picture;
		entity.powers = villain.powers;
		return entity;
	}
	
	public void deleteVillain(Long id) {
		Villain villain = Villain.findById(id);
		villain.delete();
	}

}
