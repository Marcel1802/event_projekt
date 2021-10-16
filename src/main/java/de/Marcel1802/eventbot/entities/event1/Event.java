package de.Marcel1802.eventbot.entities.event1;

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

@Table(name = "event1")
@Entity
public class Event extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    @Length(max = 64)
    private String eventName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @NotNull
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @NotNull
    @Length(max = 256)
    private String description;

    @NotNull
    private int minPeople;

    @NotNull
    private int maxPeople;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "createdby_id")
    private Person createdBy;

    @NotNull
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    public Event(String eventName, Game game, LocalDateTime dateTime, String description, int minParticipants, int maxParticipants, Group group){
        this.setEventName(eventName);
        this.id = UUID.randomUUID();
        this.setGame(game);
        this.setDate(dateTime);
        this.setDescription(description);
        this.setMaxPeople(maxParticipants);
        this.setMinPeople(minParticipants);
        this.setCreatedTime(LocalDateTime.now());
        // FIXME: get logged in user by access token
        this.setCreatedBy(Person.findById(UUID.fromString("4d742fee-ba68-41f1-9aa0-618b9ef6086d")));
        this.setGroup(group);
    }

    public Event(){}

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

    public int getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(int minPeople) {
        this.minPeople = minPeople;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public UUID getId() {
        return id;
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


    @Override
    public String toString() {
        return "ID: " + this.id + "\n" +
                "Name: " + this.eventName;
    }

    public Set<Person> getParticipants() {
        List<Event1RelEventUser> relList =  Event1RelEventUser.find("event1event = ?1",this).list();
        Set<Person> returnList = new HashSet<>();
        if (relList.isEmpty()) {
            for (Event1RelEventUser elem : relList) {
                returnList.add(elem.getPerson_id());
            }
        }

        return returnList;
    }

    public void resetParticipants() {

        List<Event1RelEventUser> forList = Event1RelEventUser.find("event1event = ?1",this).list();

        for (Event1RelEventUser elem : forList) {
            elem.delete();
        }
    }

    public void removeParticipant(Person p) {
       Event1RelEventUser.find("event1event = ?1 AND event1person = ?1", this, p).firstResult().delete();
    }

    public void addParticipant(Person p) {
        Event1RelEventUser newElem = new Event1RelEventUser(this, p);
        newElem.persist();
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
