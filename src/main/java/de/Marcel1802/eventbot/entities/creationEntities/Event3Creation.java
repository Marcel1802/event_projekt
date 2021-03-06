package de.Marcel1802.eventbot.entities.creationEntities;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

public class Event3Creation {

    public UUID gameID;
    public String eventName;
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm")
    public LocalDateTime date;
    public UUID groupID;
    public String description;
    public HashSet<Event3SquadCreation> teams;

    public Event3Creation(UUID game, String eventName,@JsonbDateFormat(value = "yyyy-MM-dd HH:mm") LocalDateTime date, UUID groupID, String description, HashSet<Event3SquadCreation> teams) {
        this.gameID = game;
        this.eventName = eventName;
        this.date = date;
        this.groupID = groupID;
        this.description = description;
        this.teams = teams;
    }

    public Event3Creation() {
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UUID getGroupID() {
        return groupID;
    }

    public void setGroupID(UUID groupID) {
        this.groupID = groupID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HashSet<Event3SquadCreation> getTeams() {
        return teams;
    }

    public void setTeams(HashSet<Event3SquadCreation> teams) {
        this.teams = teams;
    }
}

