package de.Marcel1802.eventbot.entities.event2;

import de.Marcel1802.eventbot.entities.Person;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "event2team")
public class Event2Team extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    private String teamname;

    private String description;

    private int maxPeople;

    public Event2Team() {}

    public Event2Team(String teamname, String description, int maxPeople) {
        this.id = UUID.randomUUID();
        this.setDescription(description);
        this.setTeamname(teamname);
        this.setMaxPeople(maxPeople);
    }

    public UUID getId() {
        return id;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public Set<Person> getMembers() {

        List<Event2RelTeamPerson> relList = Event2RelTeamPerson.find("event2team = ?1",this).list();

        Set<Person> returnList = new HashSet<>();

        for (Event2RelTeamPerson elem : relList) {
            returnList.add(elem.getPerson());
        }

        return returnList;
    }
}
