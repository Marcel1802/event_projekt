package de.Marcel1802.eventbot.rest;

import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.service.PersonService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("person")
public class PersonRest {

    @Inject
    PersonService pservice;

    @Inject
    SecurityIdentity identity;

    @Authenticated
    @GET
    @Path("get/all")
    public Response getAllPersons() {
        return Response.ok().entity(Person.findAll().list()).build();
    }

    @Authenticated
    @GET
    @Path("get/byName/{pname}")
    public Response getPersonByName(@PathParam("pname") String pname){
        return pservice.getPersonByName(pname);
    }

    @Authenticated
    @GET
    @Path("get/byID/{id}")
    public Response getPersonByID(@PathParam("id") UUID id){
        return pservice.getPersonByID(id);
    }

    @Authenticated
    @POST
    @Transactional
    @Path("create/{id}/{name}")
    public Response createPerson(@PathParam("id") UUID id, @PathParam("name") String name) {
        return pservice.createPerson(id, name);
    }

}
