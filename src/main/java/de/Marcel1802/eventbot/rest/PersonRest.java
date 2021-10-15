package de.Marcel1802.eventbot.rest;

import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.service.PersonService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("person")
public class PersonRest {

    PersonService pservice = new PersonService();

    @GET
    @Path("get/all")
    public Response getAllPersons() {
        return Response.ok().entity(Person.findAll().list()).build();
    }

    @GET
    @Path("get/byName/{pname}")
    public Response getPersonByName(@PathParam("pname") String pname){
        return pservice.getPersonByName(pname);
    }

    @GET
    @Path("get/byID/{id}")
    public Response getPersonByID(@PathParam("id") UUID id){
        return pservice.getPersonByID(id);
    }

}
