package de.Marcel1802.eventbot.entities.event2;

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
@Table(name = "event2releventteam")
public class Event2RelEventTeam extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event2event", nullable = false)
    private Event2 event;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event2team", nullable = false)
    private Event2Team team;

    public Event2RelEventTeam() {}

    public Event2RelEventTeam(Event2 event, Event2Team team) {
        this.id = UUID.randomUUID();
        this.setEvent(event);
        this.setTeam(team);
    }

    public UUID getId() {
        return id;
    }

    public Event2 getEvent() {
        return event;
    }

    public void setEvent(Event2 event) {
        this.event = event;
    }

    public Event2Team getTeam() {
        return team;
    }

    public void setTeam(Event2Team team) {
        this.team = team;
    }
}
