package de.Marcel1802.eventbot.entities.event3;

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

@Table(name = "event3")
@Entity
public class Event3 extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    @Length(max = 64)
    private String eventName;

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
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    public Event3(String eventName, LocalDateTime dateTime, String description, Game game, Group group, Person createdBy){
        this.setEventName(eventName);
        this.setId(UUID.randomUUID());
        this.setDate(dateTime);
        this.setDescription(description);
        this.setCreatedTime(LocalDateTime.now());
        this.setCreatedBy(createdBy);
        this.setGame(game);
        this.setGroup(group);
    }

    public Event3(){}

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Set<Event3Squad> getSquads() {
        List<Event3RelEventSquad> relList = Event3RelEventSquad.find("event3event = ?1", this).list();

        if (relList.isEmpty()) {
            return null;
        }

        Set<Event3Squad> squadList = new HashSet<>();

        for (Event3RelEventSquad elem : relList) {
            squadList.add(Event3Squad.findById(elem.getEvent3squad().getId()));
        }

        if (squadList.isEmpty()) {
            return null;
        }

        return squadList;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
