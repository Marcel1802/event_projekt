package de.Marcel1802.eventbot.rest;

import de.Marcel1802.eventbot.entities.creationEntities.Event1Creation;
import de.Marcel1802.eventbot.entities.event1.Event;
import de.Marcel1802.eventbot.service.Event1Service;

import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("event1")
public class Event1Rest {

    Event1Service eservice = new Event1Service();

    @GET
    @Path("get/next")
    public Response getNextEvent(){
        return eservice.getNextEvent();
    }

    @GET
    @Path("get/all")
    public Response getAllEvents(){
        return eservice.getAllEvents();
    }

    @GET
    @Path("get/allFuture")
    public Response getAllFutureEvents(){
        return eservice.getAllFutureEvents();
    }

    @GET
    @Path("get/allPast")
    public Response getAllPastEvents(){
        return eservice.getAllPastEvents();
    }

    @GET
    @Path("get/byID/{id}")
    public Response getByID(@PathParam("id") UUID id) {
        return eservice.getByID(id);
    }

    @GET
    @Path("get/byName/{name}")
    public Response getByName(@PathParam("name") String name){
        return eservice.getByName(name);
    }

    @POST
    @Transactional
    @Path("register/{eventID}/{userID}")
    public Response registerToEvent(@PathParam("eventID") UUID eventID, @PathParam("userID") UUID userID){
        return eservice.registerToEvent(eventID, userID);
    }

    @DELETE
    @Transactional
    @Path("unregister/{eventID}/{userID}")
    public Response unregisterFromEvent(@PathParam("eventID") UUID eventID, @PathParam("userID") UUID userID){
        return eservice.unregisterFromEvent(eventID, userID);
    }

    @POST
    @Transactional
    @Path("create")
    public Response createEvent(Event1Creation dto){
        return eservice.createEvent(dto);
    }

    @DELETE
    @Transactional
    @Path("delete/{UUID}")
    public Response deleteEvent(@PathParam("UUID") UUID uuid) {
        return eservice.deleteEvent(uuid);
    }
}
