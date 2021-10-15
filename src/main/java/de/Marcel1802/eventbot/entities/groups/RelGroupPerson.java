package de.Marcel1802.eventbot.entities.groups;

import de.Marcel1802.eventbot.entities.Person;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "relgroupperson")
public class RelGroupPerson extends PanacheEntityBase {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GroupRank rank;

    public RelGroupPerson() {}

    public RelGroupPerson(Group group, Person person, GroupRank rank) {
        this.id = UUID.randomUUID();
        this.setGroup(group);
        this.setPerson(person);
        this.setRank(rank);
    }


    public UUID getId() {
        return id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public GroupRank getRank() {
        return rank;
    }

    public void setRank(GroupRank rank) {
        this.rank = rank;
    }
}
