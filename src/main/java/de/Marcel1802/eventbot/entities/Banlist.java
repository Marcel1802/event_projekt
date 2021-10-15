package de.Marcel1802.eventbot.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="banlist")
public class Banlist extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "bannedPerson_id")
    private Person bannedPerson;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "bannedFrom_id")
    private Person bannedFrom;

    @NotNull
    private LocalDateTime banTime;

    // can bu null if isPermanent == true
    private LocalDateTime bannedUntil;

    @NotNull
    private String reason;

    @NotNull
    private boolean isPermanent;

    public Banlist() {

    }

    public Banlist(Person bannedPerson, Person bannedFrom, LocalDateTime bannedUntil, String reason, boolean isPermanent) {
        this.id = UUID.randomUUID();
        this.setBannedFrom(bannedFrom);
        this.setBannedPerson(bannedPerson);
        this.setReason(reason);
        this.setBannedUntil(bannedUntil);
        this.setBanTime(LocalDateTime.now());
        this.setIsPermanent(isPermanent);
    }



    public UUID getId() {
        return id;
    }

    public Person getBannedPerson() {
        return bannedPerson;
    }

    public void setBannedPerson(Person bannedPerson) {
        this.bannedPerson = bannedPerson;
    }

    public Person getBannedFrom() {
        return bannedFrom;
    }

    public void setBannedFrom(Person bannedFrom) {
        this.bannedFrom = bannedFrom;
    }

    public LocalDateTime getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(LocalDateTime bannedUntil) {
        this.bannedUntil = bannedUntil;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getBanTime() {
        return banTime;
    }

    public void setBanTime(LocalDateTime banTime) {
        this.banTime = banTime;
    }

    public boolean getIsPermanent() {
        return isPermanent;
    }

    public void setIsPermanent(boolean isPermanent) {
        this.isPermanent = isPermanent;
    }
}
