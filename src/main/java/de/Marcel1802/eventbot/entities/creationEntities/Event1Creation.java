package de.Marcel1802.eventbot.entities.creationEntities;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event1Creation {

    private String eventName;
    private UUID gameID;
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;
    private String description;
    private int maxPeople;
    private int minPeople;
    private UUID groupID;

    public Event1Creation(String name, UUID gameID,@JsonbDateFormat(value = "yyyy-MM-dd HH:mm") LocalDateTime date, String description, int maxPeople, int minPeople, UUID groupID) {
        this.setEventName(name);
        this.setGameID(gameID);
        this.setDate(date);
        this.setDescription(description);
        this.setMaxPeople(maxPeople);
        this.setMinPeople(minPeople);
        this.setGroupID(groupID);
    }

    public Event1Creation() {}

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
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

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(int minPeople) {
        this.minPeople = minPeople;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }
}
