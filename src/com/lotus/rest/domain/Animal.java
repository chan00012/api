package com.lotus.rest.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.lotus.rest.dao.AnimalDAO;
import com.lotus.rest.dao.AnimalOJDBDAO;

public class Animal {
	private long id;
	private String name;
	private String species;
	private int energy;
	private boolean alive=true;
	
	public Animal(String name, String species, int energy, boolean alive) {
		super();
		this.name = name;
		this.species = species;
		this.energy = energy;
		this.alive = alive;
	}
	public Animal() {
		
	}
	public Animal(long id, String name, String species, int energy, boolean alive) {
		super();
		this.id = id;
		this.name = name;
		this.species = species;
		this.energy = energy;
		this.alive = alive;
	}

	@Override
	public String toString() {
		return "Animal [id=" + id + ", name=" + name + ", species=" + species + ", energy=" + energy + ", alive="
				+ alive + "]";
	}

	@JsonIgnore
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSpecies() {
		return species;
	}

	public int getEnergy() {
		return energy;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public void persist() {
		AnimalDAO animalDao = AnimalOJDBDAO.getInstance();
		
		Animal existingAnimal = animalDao.getByName(name);
		
		if(existingAnimal == null) {
			animalDao.create(this);
			System.out.println("Created Animal "+this);
		} else {
			//Get the id of the existing animal
			this.setId(existingAnimal.getId());
			
			boolean hasChanged = existingAnimal.isAlive() !=this.alive || ! existingAnimal.getSpecies().equals(this.species) || existingAnimal.energy!=this.energy;
			if(hasChanged) {
				animalDao.update(this);
				System.out.println("Updated Animal "+this);
			} else {
				System.out.println("Ignored persistence, nothing changed for animal "+this);
			}
		}
	}
}
