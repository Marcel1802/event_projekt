package de.Marcel1802.eventbot.entities.event3;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "event3RelSquadSlot")
public class Event3RelSquadSlot extends PanacheEntityBase {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event3squad", nullable = false)
    private Event3Squad Event3Squad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event3slot", nullable = false)
    private Event3Slot Event3Slot;

    public Event3RelSquadSlot() {}

    public Event3RelSquadSlot(Event3Squad squad, Event3Slot slot) {
        this.setId(UUID.randomUUID());
        this.setEvent3slot(slot);
        this.setEvent3squad(squad);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Event3Squad getEvent3squad() {
        return Event3Squad;
    }

    public void setEvent3squad(Event3Squad Event3Squad) {
        this.Event3Squad = Event3Squad;
    }

    public Event3Slot getEvent3slot() {
        return Event3Slot;
    }

    public void setEvent3slot(Event3Slot Event3Slot) {
        this.Event3Slot = Event3Slot;
    }
}
