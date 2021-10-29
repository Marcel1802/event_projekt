package de.Marcel1802.eventbot.entities.creationEntities;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDateTime;

public class BanCreation {

    private PersonFromFrontend bannedPerson;

    private String reason;

    private boolean permanent;

    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm")
    private LocalDateTime bannedUntil;

    public BanCreation(PersonFromFrontend bannedPerson, String reason, boolean perm, @JsonbDateFormat(value = "yyyy-MM-dd HH:mm") LocalDateTime bannedUntil) {
        this.setBannedPerson(bannedPerson);
        this.setReason(reason);
        this.setBannedUntil(bannedUntil);
        this.setPermanent(perm);
    }

    public BanCreation() {}

    public PersonFromFrontend getBannedPerson() {
        return bannedPerson;
    }

    public void setBannedPerson(PersonFromFrontend bannedPerson) {
        this.bannedPerson = bannedPerson;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(LocalDateTime bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    @Override
    public String toString() {
        return "BanCreation{" +
                "bannedPerson=" + bannedPerson +
                ", reason='" + reason + '\'' +
                ", isPermanent=" + permanent +
                ", bannedUntil=" + bannedUntil +
                '}';
    }

    public boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
}
