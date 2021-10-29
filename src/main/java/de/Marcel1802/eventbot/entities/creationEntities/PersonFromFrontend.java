package de.Marcel1802.eventbot.entities.creationEntities;

public class PersonFromFrontend {
    // ID is a String
    private String id;
    private String gamertag;

    public PersonFromFrontend() {}

    public PersonFromFrontend(String id, String gamertag) {
        setId(id);
        setGamertag(gamertag);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    @Override
    public String toString() {
        return "PersonFromFrontend{" +
                "id='" + id + '\'' +
                ", gamertag='" + gamertag + '\'' +
                '}';
    }
}
