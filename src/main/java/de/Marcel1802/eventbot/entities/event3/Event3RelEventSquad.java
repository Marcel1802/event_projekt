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
@Table(name = "event3RelEventSquad")
public class Event3RelEventSquad extends PanacheEntityBase {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event3event", nullable = false)
    private Event3 Event3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event3squad", nullable = false)
    private Event3Squad Event3Squad;

    public Event3RelEventSquad() {

    }

    public Event3RelEventSquad(Event3 event, Event3Squad squad) {
        this.setId(UUID.randomUUID());
        this.setEvent3(event);
        this.setEvent3squad(squad);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Event3 getEvent3() {
        return Event3;
    }

    public void setEvent3(Event3 Event3) {
        this.Event3 = Event3;
    }

    public Event3Squad getEvent3squad() {
        return Event3Squad;
    }

    public void setEvent3squad(Event3Squad Event3Squad) {
        this.Event3Squad = Event3Squad;
    }
}
