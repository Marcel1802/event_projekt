package de.Marcel1802.eventbot.service;

import de.Marcel1802.eventbot.entities.Game;
import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.entities.ResponseMessage;
import de.Marcel1802.eventbot.entities.creationEntities.Event1Creation;
import de.Marcel1802.eventbot.entities.event1.Event;
import de.Marcel1802.eventbot.entities.groups.Group;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class Event1Service {

    public Response getNextEvent(){

        Event returnElem = Event.find("date > ?1 ORDER BY date ASC", LocalDateTime.now()).firstResult();

        if (returnElem == null){
            return Response.noContent().build();  // 204 No Content
        }
        else{
            return Response.ok(returnElem).build(); // 200 OK
        }
    }

    public Response registerToEvent(UUID eventID, UUID userID){

        Event event = Event.find("ID = ?1", eventID).firstResult();
        if (event == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid event ID")).build(); // 400 Bad Request
        }

        Person person = Person.find("ID = ?1", userID).firstResult();
        if (person == null){
            return Response.status(400).entity(new ResponseMessage("Invalid person ID")).build(); // 400 Bad Request
        }


        if (person.isBanned()){
            return Response.status(403).entity(new ResponseMessage("Sorry, but you are currently banned.")).build(); // 403 Forbidden
        }

        if (event.getParticipants().size() >= event.getMaxPeople()){
            return Response.status(400).entity(new ResponseMessage("Too many people already registered for the event")).build(); // 400 Bad Request
        }
        else if (event.getParticipants().contains(person)){
            return Response.status(400).entity(new ResponseMessage("You are already registered")).build(); // 400 Bad Request
        }
        else{
            event.addParticipant(person);
            return Response.ok().entity(new ResponseMessage("Successfully registered")).build(); // 200 OK
        }
    }

    public Response unregisterFromEvent(UUID eventID, UUID userID){
        Event event = Event.findById(eventID);
        if (event == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid event ID")).build(); // 400 Bad Request
        }

        Person person = Person.find("ID = ?1", userID).firstResult();
        if (person == null){
            return Response.status(400).entity(new ResponseMessage("Invalid person ID")).build();
        }

        if (!event.getParticipants().contains(person)){
            return Response.status(400).entity(new ResponseMessage("You are not registered")).build();
        }
        else{
            event.removeParticipant(person);
            return Response.ok().entity(new ResponseMessage("Successfully unregistered")).build();
        }

    }

    public Response getAllEvents() {
        List<Event> eventList = Event.findAll().list();

        if (eventList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are not events in the database")).build();
        }

        return Response.status(200).entity(eventList).build();
    }

    public Response getAllFutureEvents() {

        LocalDateTime today = LocalDateTime.now();

        List<Event> eventList = Event.find("date > ?1", today).list();

        if (eventList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are not events in the database")).build();
        }

        return Response.status(200).entity(eventList).build();
    }

    public Response getAllPastEvents() {

        LocalDateTime today = LocalDateTime.now();

        List<Event> eventList = Event.find("date < ?1", today).list();

        if (eventList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are not events in the database")).build();
        }

        return Response.status(200).entity(eventList).build();
    }

    public Response getByID(UUID id){
        Event e = Event.findById(id);

        if (e == null){
            return Response.status(404).entity(new ResponseMessage("Event cannot be found")).build();
        }

        return Response.ok().entity(e).build();
    }

    public Response getByName(String name){
        List<Event> eList = Event.find("eventname LIKE ?1",  ("%"+name+"%")).list();

        if (eList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("No events with that name were found")).build();
        }

        return Response.ok().entity(eList).build();
    }

    public Response createEvent(Event1Creation event) {

        // int cannot be null, description can be null
        if (event == null || event.getEventName() == null || event.getGameID() == null || event.getDate() == null){
            return Response.status(400).entity(new ResponseMessage("Null values are not allowed")).build();
        }

        if (event.getEventName().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid event name")).build();
        }



        if (event.getDescription() == null) {
            event.setDescription("");
        }



        if (event.getMinPeople() < 4) {
            return Response.status(400).entity(new ResponseMessage("At least 4 people should be allowed to take part in an event.")).build();
        }



        if (event.getMinPeople() > event.getMaxPeople()) {
            return Response.status(400).entity(new ResponseMessage("The maximum amount of users cannot be less that the minimum amount.")).build();
        }



        if (event.getDate().isBefore(LocalDateTime.now().plusDays(3))) {
            return Response.status(400).entity(new ResponseMessage("Events must be planned 72 hours before they take part.")).build();
        }



        Group groupFromDB = null;

        if (event.getGroupID() != null) {
            groupFromDB = Group.findById(event.getGroupID());
            if (groupFromDB == null) {
                return Response.status(400).entity(new ResponseMessage("Invalid group provided.")).build();
            }
        }


        Game gameFromDB = null;

        if (event.getGameID() != null) {
            gameFromDB = Game.findById(event.getGameID());
            if (gameFromDB == null) {
                return Response.status(400).entity(new ResponseMessage("Invalid game provided.")).build();
            }
        }



        Event newObj = new Event
        (
            event.getEventName(),
            gameFromDB,
            event.getDate(),
            event.getDescription(),
            event.getMinPeople(),
            event.getMaxPeople(),
            groupFromDB
        );



        newObj.persist();

        if (!newObj.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot save the new event")).build();
        }

        return Response.status(201).entity(newObj).build();

    }

    public Response deleteEvent(UUID uuid) {

        Event e = Event.findById(uuid);

        if (e == null) {
            return Response.status(400).entity(new ResponseMessage("Event cannot be found")).build();
        }

        // removing Users first
        e.resetParticipants();
        e.persist();
        if (e.getParticipants() != null) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete event")).build();
        }

        e.delete();

        if (e.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete event")).build();
        }

        return  Response.status(200).entity(new ResponseMessage("Event successfully deleted")).build();

    }


}



