package de.Marcel1802.eventbot.service;

import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.entities.ResponseMessage;
import de.Marcel1802.eventbot.entities.event3.Event3;
import de.Marcel1802.eventbot.entities.event3.Event3RelEventSquad;
import de.Marcel1802.eventbot.entities.event3.Event3RelSquadSlot;
import de.Marcel1802.eventbot.entities.event3.Event3Slot;
import de.Marcel1802.eventbot.entities.event3.Event3Squad;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class Event3Service {

    public Response getNextA3Event() {

         Event3 returnElem = Event3.find("date > ?1 ORDER BY date ASC", LocalDateTime.now()).firstResult();

        if (returnElem == null){
            return Response.noContent().build();  // 204 No Content
        }
        else{
            return Response.ok(returnElem).build(); // 200 OK
        }
    }

    public Response getAllA3Events() {

        List<Event3> a3list = Event3.findAll().list();

        if (a3list.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no events in the database.")).build();
        }

       return Response.ok(Event3.findAll().list()).build();
    }

    public Response getAllFutureA3Events() {

        LocalDateTime today = LocalDateTime.now();

        List<Event3> a3list = Event3.find("date > ?1", today).list();

        if (a3list.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no events in the database.")).build();
        }

        return Response.ok(Event3.findAll().list()).build();
    }

    public Response getAllPastA3Events() {

        LocalDateTime today = LocalDateTime.now();

        List<Event3> a3list = Event3.find("date < ?1", today).list();

        if (a3list.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no events in the database.")).build();
        }

        return Response.ok(Event3.findAll().list()).build();
    }

    public Response getA3EventByID(UUID id) {
        Event3 e = Event3.findById(id);

        if (e == null){
            return Response.status(400).entity(new ResponseMessage("Event cannot be found")).build();
        }

        return Response.ok().entity(e).build();
    }

    public Response getA3EventByName(String name) {
        List<Event3> eList = Event3.find("eventname LIKE ?1", ("%"+name+"%")).list();

        if (eList.isEmpty()) {
            return Response.noContent().entity(new ResponseMessage("No events with that name were found")).build();
        }

        return Response.ok().entity(eList).build();
    }

    public Response getA3Slots(UUID squadID) {

        Event3Squad squad = Event3Squad.findById(squadID);

        if (squad == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid squad")).build();
        }

        List<Event3RelSquadSlot> relationList = Event3RelSquadSlot.find("arma3squad = ?1", squad).list();

        if (relationList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no slots available for the squad")).build();
        }

        Set<Event3Slot> event3SlotList = new HashSet<>();

        for (Event3RelSquadSlot elem : relationList) {
            event3SlotList.add(Event3Slot.findById(elem.getEvent3slot().getId()));
        }

        if (event3SlotList.isEmpty()) {
            return Response.status(500).entity(new ResponseMessage("Slots cannot be listed.")).build();
        }

        return Response.status(200).entity(event3SlotList).build();
    }

    public Response getA3Squads(UUID eventID) {

        Event3 event = Event3.findById(eventID);

        if (event == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid event")).build();
        }

        List<Event3RelEventSquad> relationList = Event3RelEventSquad.find("arma3event = ?1", event).list();

        if (relationList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no squads available for the event")).build();
        }

        Set<Event3Squad> event3SquadList = new HashSet<>();

        for (Event3RelEventSquad elem : relationList) {
            event3SquadList.add(Event3Squad.findById(elem.getEvent3squad().getId()));
        }

        if (event3SquadList.isEmpty()) {
            return Response.status(500).entity(new ResponseMessage("Squads cannot be listed.")).build();
        }

        return Response.status(200).entity(event3SquadList).build();
    }

    public Response createA3Event(Event3 event) {

        if (event == null || event.getEventName() == null || event.getDate() == null) {
            return Response.status(400).entity(new ResponseMessage("Null values are not allowed")).build();
        }

        if (event.getEventName().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid event name")).build();
        }

        if (event.getDescription() == null) {
            event.setDescription("");
        }

        //TODO: createdby by keycloak

        if (event.getCreatedBy().isBanned()){
            return Response.status(403).entity(new ResponseMessage("Sorry, but you are currently banned.")).build(); // 403 Forbidden
        }

        if (event.getDate().isBefore(LocalDateTime.now().plusDays(3))) {
            return Response.status(400).entity(new ResponseMessage("Events must be planned 72 hours before they take part.")).build();
        }

        Event3 newEvent = new Event3
        (
            event.getEventName(),
            event.getDate(),
            event.getDescription(),
            event.getGame(),
            event.getGroup()
        );

        newEvent.persist();

        if (!newEvent.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot save the new event")).build();
        }

        return Response.status(201).entity(newEvent).build();

    }

    public Response createA3Squad(UUID eventID, Event3Squad squad) {

        if (eventID == null || squad == null || squad.getSquadname() == null) {
            return Response.status(400).entity(new ResponseMessage("Null values are not permitted.")).build();
        }

        Event3 eventFromDB = Event3.findById(eventID);

        if (eventFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid event provided")).build();
        }

        if (squad.getDescription() == null) {
            squad.setDescription("");
        }

        Event3Squad newSquad = new Event3Squad
        (
                squad.getSquadname(),
                squad.getDescription()
        );

        newSquad.persist();

        if (!newSquad.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Could not persist the new Squad")).build();
        }

        Event3RelEventSquad newRel = new Event3RelEventSquad(eventFromDB, newSquad);
        newRel.persist();

        if (!newRel.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Could not persist squad event relation")).build();
        }

        return Response.status(201).entity(newSquad).build();
    }

    public Response createA3Slot(UUID squadID, Event3Slot slot) {

        if (squadID == null || slot == null || slot.getRole() == null) {
            return Response.status(400).entity(new ResponseMessage("Null values are not permitted")).build();
        }

        Event3Squad squadFromDB = Event3Squad.findById(squadID);

        if (squadFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid squad provided")).build();
        }

        Event3Slot newSlot = new Event3Slot(slot.getRole());

        newSlot.persist();

        if (!newSlot.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Could not perstist the new slot")).build();
        }

        Event3RelSquadSlot newRel = new Event3RelSquadSlot(squadFromDB, newSlot);
        newRel.persist();

        if (!newRel.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Could not perstist slot squad relation")).build();
        }

        return Response.status(201).entity(newSlot).build();
    }

    public Response deleteA3Event(UUID uuid) {

        Event3 e = Event3.findById(uuid);

        if (e == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid event")).build();
        }

        for (Event3Squad squadElem : e.getSquads()) {

            for (Event3Slot slotElem : squadElem.getSlots()) {

                Event3RelSquadSlot tmpRelSqSl = Event3RelSquadSlot.find("arma3slot = ?1", slotElem).firstResult();
                if (tmpRelSqSl != null) {
                    tmpRelSqSl.delete();
                    if (tmpRelSqSl.isPersistent()) {
                        return Response.status(500).entity(new ResponseMessage("Cannot delete related arma3RelSquadSlot object.")).build();
                    }
                }

                slotElem.delete();
                if (slotElem.isPersistent()) {
                    return Response.status(500).entity(new ResponseMessage("Cannot delete related arma3slot object.")).build();
                }
            }

            Event3RelEventSquad tmpRelEvSq = Event3RelEventSquad.find("arma3squad = ?1", squadElem).firstResult();
            if (tmpRelEvSq != null) {
                tmpRelEvSq.delete();
                if (tmpRelEvSq.isPersistent()) {
                    return Response.status(500).entity(new ResponseMessage("Cannot delete related arma3RelEventSquad object.")).build();
                }
            }

            squadElem.delete();
            if (squadElem.isPersistent()) {
                return Response.status(500).entity(new ResponseMessage("Cannot delete related arma3Squad object.")).build();
            }
        }

        e.delete();

        if (e.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete event.")).build();
        }

        return Response.status(204).build();
    }

    public Response deleteA3Squad(UUID uuid) {

        Event3Squad s = Event3Squad.findById(uuid);

        if (s == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid squad.")).build();
        }

       for (Event3Slot slotElem : s.getSlots()) {

           Event3RelSquadSlot tmpRelSqSl = Event3RelSquadSlot.find("arma3slot = ?1", slotElem).firstResult();
           if (tmpRelSqSl != null) {
               tmpRelSqSl.delete();
               if (tmpRelSqSl.isPersistent()) {
                   return Response.status(500).entity(new ResponseMessage("Cannot delete slot squad relation object.")).build();
               }
           }

           slotElem.delete();
           if (slotElem.isPersistent()) {
               return Response.status(500).entity(new ResponseMessage("Cannot delete related slot object.")).build();
           }
       }

       Event3RelEventSquad relEvSq = Event3RelEventSquad.find("arma3squad = ?1",s).firstResult();
       if (relEvSq != null) {
           relEvSq.delete();
           if (relEvSq.isPersistent()) {
               return Response.status(500).entity(new ResponseMessage("Cannot delete related arma3RelEventSquad object.")).build();
           }
        }


        s.delete();

        if (s.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete squad.")).build();
        }

        return Response.status(204).build();

    }

    public Response deleteA3Slot(UUID uuid) {

        Event3Slot s = Event3Slot.findById(uuid);

        if (s == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid slot.")).build();
        }


        Event3RelSquadSlot relObj = Event3RelSquadSlot.find("arma3slot = ?1", uuid).firstResult();

        if (relObj != null) {

            relObj.delete();

            if (relObj.isPersistent()) {
                return Response.status(500).entity(new ResponseMessage("Cannot delete relationship between slot and squad")).build();
            }
        }

        s.delete();

        if (s.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete slot.")).build();
        }

        return Response.status(204).build();
    }


    public Response editA3Event(Event3 event) {

        if (event == null || event.getId() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Event3 e = Event3.findById(event.getId());
        if (e == null) {
            return Response.status(400).entity(new ResponseMessage("Event not found")).build();
        }

        e.setEventName(event.getEventName());
        e.setDate(event.getDate());
        e.setDescription(event.getDescription());

        return Response.status(200).entity(e).build();
    }

    public Response editA3Squad(Event3Squad squad) {

        if (squad == null || squad.getId() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Event3Squad s = Event3Squad.findById(squad.getId());
        if (s == null) {
            return Response.status(400).entity(new ResponseMessage("Squad not found")).build();
        }

        s.setDescription(squad.getDescription());
        s.setSquadname(squad.getSquadname());

        return Response.status(200).entity(s).build();
    }

    public Response editA3Slot(Event3Slot slot) {

        if (slot == null || slot.getId() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Event3Slot s = Event3Slot.findById(slot.getId());
        if (s == null) {
            return Response.status(400).entity(new ResponseMessage("Slot not found")).build();
        }

        s.setSlotUsedBy(slot.getSlotUsedBy());
        s.setRole(slot.getRole());

        return Response.status(200).entity(s).build();
    }

    public Response leaveSlot(UUID eventSlotID, UUID personID) {

        if (eventSlotID == null || personID == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Event3Slot slotFromDB = Event3Slot.findById(eventSlotID);

        if (slotFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid slot ID provided")).build();
        }

        Person personFromDB = Person.findById(personID);

        if (personFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person ID provided")).build();
        }

        if (slotFromDB.getSlotUsedBy() != personFromDB) {
            return Response.status(400).entity(new ResponseMessage("Cannot unregister from slot: You are not using this slot.")).build();
        }

        slotFromDB.setSlotUsedBy(null);

        return Response.status(200).entity(slotFromDB).build();
    }

    public Response joinSlot (UUID eventSlotID, UUID personID) {
        if (eventSlotID == null || personID == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Event3Slot slotFromDB = Event3Slot.findById(eventSlotID);

        if (slotFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid slot ID provided")).build();
        }

        Person personFromDB = Person.findById(personID);

        if (personFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person ID provided")).build();
        }

        if (slotFromDB.getSlotUsedBy() != null) {
            return Response.status(400).entity(new ResponseMessage("Slot already in use.")).build();
        }

        Event3RelSquadSlot relSquadSlot = Event3RelSquadSlot.find("event3slot = ?1", slotFromDB).firstResult();
        if (relSquadSlot == null)  {
            return Response.status(500).entity(new ResponseMessage("The slot is not linked to a squad.")).build();
        }
        Event3RelEventSquad relEventSquad = Event3RelEventSquad.find("event3squad = ?1", relSquadSlot.getEvent3squad()).firstResult();
        if (relEventSquad == null) {
            return Response.status(500).entity(new ResponseMessage("Squad of slot is not linked to an event.")).build();
        }
        for (Event3Squad squadElem : relEventSquad.getEvent3().getSquads()) {
            for (Event3Slot slotElem : squadElem.getSlots()) {
                if (slotElem.getSlotUsedBy() == personFromDB) {
                    return Response.status(400).entity(new ResponseMessage("You cannot register for multiple slots.")).build();
                }
            }
        }

        slotFromDB.setSlotUsedBy(personFromDB);

        return Response.status(200).entity(slotFromDB).build();

    }

}
