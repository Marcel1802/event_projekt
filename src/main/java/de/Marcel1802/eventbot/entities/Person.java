package de.Marcel1802.eventbot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.Marcel1802.eventbot.entities.groups.Group;
import de.Marcel1802.eventbot.entities.groups.RelGroupPerson;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import org.hibernate.validator.constraints.Length;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Table(name = "person")
@Entity
public class Person extends PanacheEntityBase {

    @Id
    private UUID id;

    @JsonIgnore
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm")
    private LocalDateTime accountCreated;

    @JsonIgnore
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastLogin;

    @NotNull
    @Length(max = 24)
    private String gamertag;

    public Person(UUID id, String gamertag){
        this.id = id;
        this.setGamertag(gamertag);
        this.accountCreated = LocalDateTime.now();
        this.setLastLogin(LocalDateTime.now());
    }

    public Person() {}

    public UUID getId() {
        return id;
    }

    public String getGamertag() {
        return gamertag;
    }

    public void setGamertag(String gamertag) {
        this.gamertag = gamertag;
    }

    @JsonIgnore
    public boolean isBanned() {
        if (Banlist.find("permanent = true AND bannedperson_id == ?1",this) != null || Banlist.find("permanent = false AND bannedperson_id = ?1 AND banneduntil > ?2", this, LocalDateTime.now()) != null) {
            return true;
        }

        return false;
    }

    public LocalDateTime getAccountCreated() {
        return accountCreated;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @JsonIgnore
    public Set<Group> getGroups() {

        List<RelGroupPerson> relList = RelGroupPerson.find("person_id = ?1",this).list();

        Set<Group> returnList = new HashSet<>();

        for (RelGroupPerson elem : relList) {
            returnList.add(elem.getGroup());
        }

        return returnList;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", accountCreated=" + accountCreated +
                ", lastLogin=" + lastLogin +
                ", gamertag='" + gamertag + '\'' +
                '}';
    }
}
