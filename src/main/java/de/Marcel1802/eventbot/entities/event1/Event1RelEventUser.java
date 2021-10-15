package de.Marcel1802.eventbot.entities.event1;


import de.Marcel1802.eventbot.entities.Person;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;


@Entity
@Table(name = "event1releventuser")
public class Event1RelEventUser extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event1event", nullable = false)
    private Event event_id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event1person", nullable = false)
    private Person person_id;

    public Event1RelEventUser() {}

    public Event1RelEventUser(Event event, Person person) {
        this.id = UUID.randomUUID();
        this.setEvent_id(event);
        this.setPerson_id(person);
    }

    public UUID getId() {
        return id;
    }

    public Event getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Event event_id) {
        this.event_id = event_id;
    }

    public Person getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Person person_id) {
        this.person_id = person_id;
    }
}
