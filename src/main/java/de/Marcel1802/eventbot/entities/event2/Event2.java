package de.Marcel1802.eventbot.entities.event2;

import de.Marcel1802.eventbot.entities.Game;
import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.entities.groups.Group;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import org.hibernate.validator.constraints.Length;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "event2")
public class Event2 extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    @Length(max = 64)
    private String eventName;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @NotNull
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @NotNull
    @Length(max = 256)
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "createdby_id")
    private Person createdBy;

    @NotNull
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    public Event2() {}

    public Event2(String eventName, Game game, LocalDateTime date, String description, Group group, Person createdBy) {

        this.id = UUID.randomUUID();
        this.setEventName(eventName);
        this.setGame(game);
        this.setDate(date);
        this.setDescription(description);
        this.setCreatedTime(LocalDateTime.now());
        this.setCreatedBy(createdBy);
        this.setGroup(group);

    }



    public UUID getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Set<Event2Team> getTeams() {

        List<Event2RelEventTeam> relList = Event2RelEventTeam.find("event2event = ?1",this).list();

        Set<Event2Team> returnList = new HashSet<>();

        for (Event2RelEventTeam team : relList) {
            returnList.add(team.getTeam());
        }

        return returnList;

    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
