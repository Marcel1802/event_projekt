package de.Marcel1802.eventbot.entities.groups;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "grouptbl")
public class Group extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    private String name;

    private String description;

    public Group() {}

    public Group(String name, String description) {
        this.id = UUID.randomUUID();
        this.setDescription(description);
        this.setName(name);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
