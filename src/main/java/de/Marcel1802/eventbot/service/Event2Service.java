package de.Marcel1802.eventbot.service;

import de.Marcel1802.eventbot.entities.Game;
import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.entities.ResponseMessage;
import de.Marcel1802.eventbot.entities.event2.Event2;
import de.Marcel1802.eventbot.entities.event2.Event2RelEventTeam;
import de.Marcel1802.eventbot.entities.event2.Event2RelTeamPerson;
import de.Marcel1802.eventbot.entities.event2.Event2Team;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class Event2Service {

    public Response getAllEvents() {
        List<Event2> returnList = Event2.listAll();

        if (returnList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("The list is empty")).build();
        }

        return Response.status(200).entity(returnList).build();
    }

    public Response getAllFutureEvents() {

        LocalDateTime today = LocalDateTime.now();

        List<Event2> eventList = Event2.find("date > ?1", today).list();

        if (eventList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are not events in the database")).build();
        }

        return Response.status(200).entity(eventList).build();
    }

    public Response getAllPastEvents() {

        LocalDateTime today = LocalDateTime.now();

        List<Event2> eventList = Event2.find("date < ?1", today).list();

        if (eventList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are not events in the database")).build();
        }

        return Response.status(200).entity(eventList).build();
    }

    public Response getByID(UUID id) {
        Event2 e = Event2.findById(id);

        if (e == null){
            return Response.status(404).entity(new ResponseMessage("Event cannot be found")).build();
        }

        return Response.ok().entity(e).build();
    }

    public Response createEvent(Event2 eventParam) {

        if (eventParam == null || eventParam.getEventName() == null || eventParam.getDate() == null || eventParam.getGame() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        if (eventParam.getEventName().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid event name")).build();
        }

        if (eventParam.getDescription() == null) {
            eventParam.setDescription("");
        }

        //TODO: createdby by keycloak

        if (eventParam.getCreatedBy().isBanned()){
            return Response.status(403).entity(new ResponseMessage("Sorry, but you are currently banned.")).build(); // 403 Forbidden
        }

        if (eventParam.getDate().isBefore(LocalDateTime.now().plusDays(3))) {
            return Response.status(400).entity(new ResponseMessage("Events must be planned 72 hours before they take part.")).build();
        }

        if (Game.findById(eventParam.getGame().getId()) == null || !Game.findById(eventParam.getGame().getId()).equals(eventParam.getGame())) {
            return Response.status(400).entity(new ResponseMessage("The provided game is invalid")).build();
        }

        Event2 newEvent = new Event2
        (
                eventParam.getEventName(),
                eventParam.getGame(),
                eventParam.getDate(),
                eventParam.getDescription(),
                eventParam.getGroup()
        );

        newEvent.persist();

        if (!newEvent.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot save the new event")).build();
        }

        return Response.status(201).entity(newEvent).build();

    }

    public Response createTeam(UUID paramTeamID, Event2Team teamParam) {

        if (paramTeamID == null || teamParam == null | teamParam.getTeamname() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        if (teamParam.getTeamname().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid team name")).build();
        }

        Event2 eventFromDB = Event2.findById(paramTeamID);

        if (eventFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid team provided")).build();
        }

        if (teamParam.getDescription() == null) {
            teamParam.setDescription("");
        }

        Event2Team newTeam = new Event2Team(
                teamParam.getTeamname(),
                teamParam.getDescription(),
                teamParam.getMaxPeople()
        );

        newTeam.persist();

        if (!newTeam.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot persist new team")).build();
        }

        Event2RelEventTeam newRel = new Event2RelEventTeam(eventFromDB, newTeam);
        newRel.persist();

        if (!newRel.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot persist new relation")).build();
        }

        return Response.status(201).entity(newTeam).build();

    }

    public Response deleteTeam(UUID id) {
        if (id == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Event2Team teamFromDB = Event2Team.findById(id);

        if (teamFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Cannot find team")).build();
        }


        List<Event2RelTeamPerson> relTeamPersonList = Event2RelTeamPerson.find("event2team = ?1",teamFromDB).list();
        for (Event2RelTeamPerson rel : relTeamPersonList) {
            rel.delete();
            if (rel.isPersistent()) {
                return Response.status(500).entity(new ResponseMessage("Cannot delete relation")).build();
            }
        }


        List<Event2RelEventTeam> relEventTeamList = Event2RelEventTeam.find("event2team = ?1",teamFromDB).list();
        for (Event2RelEventTeam rel : relEventTeamList) {
            rel.delete();
            if (rel.isPersistent()) {
                return Response.status(500).entity(new ResponseMessage("Cannot delete relation")).build();
            }
        }

        teamFromDB.delete();

        if (teamFromDB.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete team")).build();
        }

        return Response.status(200).entity(teamFromDB).build();
    }

    public Response deleteEvent(UUID id) {
        if (id == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Event2 eventFromDB = Event2.findById(id);

        if (eventFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Cannot find Event")).build();
        }

        List<Event2RelEventTeam> relEventTeamList = Event2RelEventTeam.find("event2event = ?1",eventFromDB).list();
        for (Event2RelEventTeam eventTeamRel : relEventTeamList) {

            Event2Team eventTeam = eventTeamRel.getTeam();

            List<Event2RelTeamPerson> relTeamPersonList = Event2RelTeamPerson.find("event2team = ?1",eventTeam).list();
            for (Event2RelTeamPerson relTeamPerson : relTeamPersonList) {
                relTeamPerson.delete();
                if (relTeamPerson.isPersistent()) {
                    return Response.status(500).entity(new ResponseMessage("Cannot delete team person relation")).build();
                }
            }

            eventTeamRel.delete();
            if (eventTeamRel.isPersistent()) {
                return Response.status(500).entity(new ResponseMessage("Cannot delete event team relation")).build();
            }

            eventTeam.delete();
            if (eventTeam.isPersistent()) {
                return Response.status(500).entity(new ResponseMessage("Cannot delete event")).build();
            }

        }

        eventFromDB.delete();
        if (eventFromDB.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete event")).build();
        }

        return Response.status(200).entity(eventFromDB).build();

    }

    public Response editTeam(Event2Team teamParam) {

        if (teamParam == null | teamParam.getTeamname() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        if (teamParam.getTeamname().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid team name")).build();
        }

        if (teamParam.getDescription() == null) {
            teamParam.setDescription("");
        }

        Event2Team teamFromDB = Event2Team.findById(teamParam.getId());

        if (teamFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid team provided")).build();
        }

        teamFromDB.setTeamname(teamParam.getTeamname());
        teamFromDB.setDescription(teamParam.getDescription());
        teamFromDB.setMaxPeople(teamParam.getMaxPeople());

        return Response.status(200).entity(teamFromDB).build();

    }

    public Response editEvent(Event2 eventParam) {
        if (eventParam == null || eventParam.getEventName() == null || eventParam.getDate() == null || eventParam.getGame() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        if (eventParam.getEventName().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid event name")).build();
        }

        if (eventParam.getDescription() == null) {
            eventParam.setDescription("");
        }

        Event2 eventFromDB = Event2.findById(eventParam.getId());

        if (eventFromDB ==  null) {
            return Response.status(400).entity("Invalid event provided").build();
        }

        eventFromDB.setEventName(eventParam.getEventName());
        eventFromDB.setDescription(eventParam.getDescription());
        eventFromDB.setGroup(eventParam.getGroup());
        eventFromDB.setDate(eventParam.getDate());
        eventFromDB.setGame(eventParam.getGame());

        return Response.status(200).entity(eventFromDB).build();
    }

    public Response joinTeam(UUID teamID, UUID personID) {
        if (teamID == null || personID == null) {
            return Response.status(400).entity("Null value provided").build();
        }

        Event2Team teamFromDB = Event2Team.findById(teamID);

        if (teamFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid team provided")).build();
        }

        Person personFromDB = Person.findById(personID);

        if (personFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person provided")).build();
        }

        if (Event2RelTeamPerson.find("event2person = ?1 AND event2team = ?2", personFromDB, teamFromDB) != null) {
            return Response.status(400).entity(new ResponseMessage("You are already member of that team")).build();
        }

        //TODO cant register in multiple teams

        Event2RelTeamPerson newRel = new Event2RelTeamPerson(teamFromDB, personFromDB);
        newRel.persist();

        if (!newRel.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot persist relation")).build();
        }

        return Response.status(200).entity(newRel).build();

    }

    public Response leaveTeam(UUID teamID, UUID personID) {
        if (teamID == null || personID == null) {
            return Response.status(400).entity("Null value provided").build();
        }

        Event2Team teamFromDB = Event2Team.findById(teamID);

        if (teamFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid team provided")).build();
        }

        Person personFromDB = Person.findById(personID);

        if (personFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person provided")).build();
        }

        Event2RelTeamPerson relFromDB = Event2RelTeamPerson.find("event2person = ?1 AND event2team = ?2", personFromDB, teamFromDB).firstResult();

        if (relFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("You are not a member of that team")).build();
        }

        relFromDB.delete();

        if (relFromDB.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete relation")).build();
        }

        return Response.status(200).entity(relFromDB).build();

    }




























}



























