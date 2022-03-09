package de.Marcel1802.eventbot.entities.creationEntities;

import java.util.HashSet;

public class Event3SquadCreation {

    public String squadname;
    public String description;
    public HashSet<Event3SlotCreation> slots;

    public Event3SquadCreation(String squadname, String description, HashSet<Event3SlotCreation> slots) {
        this.squadname = squadname;
        this.description = description;
        this.slots = slots;
    }

    public Event3SquadCreation() {
    }

    public String getSquadname() {
        return squadname;
    }

    public void setSquadname(String squadname) {
        this.squadname = squadname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashSet<Event3SlotCreation> getSlots() {
        return slots;
    }

    public void setSlots(HashSet<Event3SlotCreation> slots) {
        this.slots = slots;
    }
}
