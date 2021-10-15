package de.Marcel1802.eventbot.entities.event3;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table(name = "event3squad")
@Entity
public class Event3Squad extends PanacheEntityBase {

    @Id
    private UUID id;

    private String squadname;

    private String description = "";

    public Event3Squad(String squadname, String description) {
        this.setId(UUID.randomUUID());
        this.setSquadname(squadname);
        this.setDescription(description);
    }

    public Event3Squad() {}

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<Event3Slot> getSlots() {
        List<Event3RelSquadSlot> relList = Event3RelSquadSlot.find("event3squad = ?1", this).list();

        if (relList.isEmpty()) {
            return null;
        }

        Set<Event3Slot> slotList = new HashSet<>();

        for (Event3RelSquadSlot elem : relList) {
            slotList.add(Event3Slot.findById(elem.getEvent3slot().getId()));
        }

        if (slotList.isEmpty()) {
            return null;
        }

        return slotList;
    }
}
