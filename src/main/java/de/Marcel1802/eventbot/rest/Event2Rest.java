package de.Marcel1802.eventbot.rest;

import de.Marcel1802.eventbot.entities.event2.Event2;
import de.Marcel1802.eventbot.entities.event2.Event2Team;
import de.Marcel1802.eventbot.service.Event2Service;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("event2")
public class Event2Rest {

    @Inject
    Event2Service eservice;

    @Inject
    SecurityIdentity identity;

    @Authenticated
    @GET
    @Path("/get/all")
    public Response getAllEvents() {
        return eservice.getAllEvents();
    }

    @Authenticated
    @GET
    @Path("get/allFuture")
    public Response getAllFutureEvents() {
        return eservice.getAllFutureEvents();
    }

    @Authenticated
    @GET
    @Path("get/allPast")
    public Response getAllPastEvents() {
        return eservice.getAllPastEvents();
    }

    @Authenticated
    @GET
    @Path("get/byID/{id}")
    public Response getByID(@PathParam("id") UUID id) {
        return eservice.getByID(id);
    }

    @POST
    @Transactional
    @Path("create/event")
    public Response createEvent(Event2 newEvent) { return  eservice.createEvent(newEvent); }

    @POST
    @Transactional
    @Path("create/team/{eventID}")
    public Response createTeam(@PathParam("eventID") UUID eventID, Event2Team newTeam) { return eservice.createTeam(eventID, newTeam); }

    @DELETE
    @Transactional
    @Path("delete/event/{ID}")
    public Response deleteEvent(@PathParam("ID") UUID id) {
        return eservice.deleteEvent(id);
    }

    @DELETE
    @Transactional
    @Path("delete/team/{ID}")
    public Response deleteTeam(@PathParam("ID") UUID id) {
        return eservice.deleteTeam(id);
    }

    @PUT
    @Transactional
    @Path("edit/event")
    public Response editEvent(Event2 event2) {
        return eservice.editEvent(event2);
    }

    @PUT
    @Transactional
    @Path("edit/team")
    public Response editTeam(Event2Team team) {
        return eservice.editTeam(team);
    }

    @POST
    @Transactional
    @Path("joinTeam/{teamID}/{personID}")
    public Response joinTeam(@PathParam("teamID") UUID teamID, @PathParam("personID") UUID personID) {
        return eservice.joinTeam(teamID,personID);
    }

    @DELETE
    @Transactional
    @Path("leaveTeam/{teamID}/{personID}")
    public Response leaveTeam(@PathParam("teamID") UUID teamID, @PathParam("personID") UUID personID) {
        return eservice.leaveTeam(teamID,personID);
    }

}


















