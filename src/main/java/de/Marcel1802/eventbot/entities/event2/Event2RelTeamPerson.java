package de.Marcel1802.eventbot.entities.event2;

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
@Table(name = "event2relteamperson")
public class Event2RelTeamPerson extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event2team", nullable = false)
    private Event2Team team;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event2person", nullable = false)
    private Person person;

    public Event2RelTeamPerson() {}

    public Event2RelTeamPerson(Event2Team team, Person person) {
        this.id = UUID.randomUUID();
        this.setPerson(person);
        this.setTeam(team);
    }

    public UUID getId() {
        return id;
    }

    public Event2Team getTeam() {
        return team;
    }

    public void setTeam(Event2Team team) {
        this.team = team;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
