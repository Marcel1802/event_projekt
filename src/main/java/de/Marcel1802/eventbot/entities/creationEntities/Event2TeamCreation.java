package de.Marcel1802.eventbot.entities.creationEntities;

public class Event2TeamCreation {
    public String teamname;
    public String teamdescription;
    public int maxPeople;

    public Event2TeamCreation() {}

    public Event2TeamCreation(String t, String d, int m) {
        this.teamname = t;
        this.teamdescription = d;
        this.maxPeople = m;
    }
}