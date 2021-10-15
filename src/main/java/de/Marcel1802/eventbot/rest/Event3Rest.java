package de.Marcel1802.eventbot.rest;

import de.Marcel1802.eventbot.entities.event3.Event3;
import de.Marcel1802.eventbot.entities.event3.Event3Slot;
import de.Marcel1802.eventbot.entities.event3.Event3Squad;
import de.Marcel1802.eventbot.service.Event3Service;

import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("event3")
public class Event3Rest {

    Event3Service a3service = new Event3Service();

    @GET
    @Path("get/next")
    public Response getNextA3Event() {
        return a3service.getNextA3Event();
    }

    @GET
    @Path("get/all")
    public Response getAllA3Events() {
        return a3service.getAllA3Events();
    }

    @GET
    @Path("get/allFuture")
    public Response getAllFutureA3Events() {
        return a3service.getAllFutureA3Events();
    }

    @GET
    @Path("get/allPast")
    public Response getAllPastA3Events() {
        return a3service.getAllPastA3Events();
    }

    @GET
    @Path("get/byID/{id}")
    public Response getA3EventByID(@PathParam("id") UUID id)
    {
        return a3service.getA3EventByID(id);
    }

    @GET
    @Path("get/byName/{name}")
    public Response getA3EventByName(@PathParam("name") String name)
    {
        return a3service.getA3EventByName(name);
    }

    @GET
    @Path("getSlots/{squadID}")
    public Response getA3Slots(@PathParam("squadID") UUID squadID) {
        return a3service.getA3Slots(squadID);
    }

    @GET
    @Path("getSquads/{eventID}")
    public Response getA3Squads(@PathParam("eventID") UUID eventID) {
        return a3service.getA3Squads(eventID);
    }

    @POST
    @Transactional
    @Path("create/event")
    public Response createA3Event(Event3 event) {
        return a3service.createA3Event(event);
    }

    @POST
    @Transactional
    @Path("create/squad/{eventID}")
    public Response createA3Squad(@PathParam("eventID") UUID eventID, Event3Squad squad) {
        return a3service.createA3Squad(eventID, squad);
    }

    @POST
    @Transactional
    @Path("create/slot/{squadID}")
    public Response createA3Slot(@PathParam("squadID") UUID squadID, Event3Slot slot) {
        return a3service.createA3Slot(squadID, slot);
    }

    @DELETE
    @Transactional
    @Path("delete/event/{uuid}")
    public Response deleteA3Event(@PathParam("uuid") UUID uuid) {
        return a3service.deleteA3Event(uuid);
    }

    @DELETE
    @Transactional
    @Path("delete/squad/{uuid}")
    public Response deleteA3Squad(@PathParam("uuid") UUID uuid) {
        return a3service.deleteA3Squad(uuid);
    }

    @DELETE
    @Transactional
    @Path("delete/slot/{uuid}")
    public Response deleteA3Slot(@PathParam("uuid") UUID uuid) {
        return a3service.deleteA3Slot(uuid);
    }

    @PUT
    @Transactional
    @Path("edit/event")
    public Response editA3Event(Event3 event) {
        return a3service.editA3Event(event);
    }

    @PUT
    @Transactional
    @Path("edit/squad")
    public Response editA3Squad(Event3Squad squad) {
        return a3service.editA3Squad(squad);
    }

    @PUT
    @Transactional
    @Path("edit/slot")
    public Response editA3Slot(Event3Slot slot) {
        return a3service.editA3Slot(slot);
    }

}
