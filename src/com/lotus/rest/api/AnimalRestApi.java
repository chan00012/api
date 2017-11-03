package com.lotus.rest.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.lotus.rest.dao.AnimalDAO;
import com.lotus.rest.dao.AnimalOJDBDAO;
import com.lotus.rest.domain.Animal;

@Path("/animal")
public class AnimalRestApi {
	private AnimalDAO animalDAO = null;
	
	@Path("displayfields")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response displayFields(Animal a) {
		return Response.status(200).entity(a).build();
	}
	
	@GET
	@Produces("application/json")
	public Response list() throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		List<Animal> animals = new ArrayList<>();
		Animal a = new Animal();
		a.setAlive(true);
		a.setEnergy(10);
		a.setId(1);
		a.setName("tina");
		a.setSpecies("dinosaur");
		animals.add(a);
		ObjectMapper mapper = new ObjectMapper();
		String response = "{}"; 
		if(!animals.isEmpty()) {
			response = mapper.writeValueAsString(animals);
		}
		return Response.status(200).entity(response).build();
	}

	@Path("{name}")
	@GET
	@Produces("application/json")
	public Response show(@PathParam("name") String name) throws JSONException, JsonGenerationException, JsonMappingException, IOException {
		Animal animal = animalDAO.getByName(name);
		ObjectMapper mapper = new ObjectMapper();
		String response = "{}";
		
		if(animal != null) {
			response = mapper.writeValueAsString(animal);
		}
		return Response.status(200).entity(response).build();
	}
	
	@Path("{name}")
	@DELETE
	@Produces("application/json")
	public Response delete(@PathParam("name") String name) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		try {
			Animal animal = animalDAO.getByName(name);
			animalDAO.delete(animal);
			jsonObject.put("success", true);
		} catch (Exception e) {
			jsonObject.put("success", false);
			jsonObject.put("errorMessage", "cannot delete animal");
		}

		return Response.status(200).entity(jsonObject.toString()).build();
	}
	
	@POST
	@Produces("application/json")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response save(@FormParam("name") String name, @FormParam("alive") boolean alive,
			@FormParam("energy") int energy, @FormParam("species") String species) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		
		if(name == null) {
			jsonObject.put("error", "bad request, name parameter is required");
			return Response.status(400).entity(jsonObject.toString()).build();
		}
		
		try {
			Animal animal = new Animal(name, species, energy, alive);
			animal.persist();
			
			jsonObject.put("success", true);
		} catch (Exception e) {
			jsonObject.put("success", false);
			jsonObject.put("errorMessage", "Cannot save animal.");
		}

		return Response.status(200).entity(jsonObject.toString()).build();
	}
	
	
	
}
