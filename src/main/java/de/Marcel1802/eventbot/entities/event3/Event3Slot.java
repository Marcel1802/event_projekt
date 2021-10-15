
package de.Marcel1802.eventbot.entities.event3;

import de.Marcel1802.eventbot.entities.Person;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Table(name = "event3slot")
@Entity
public class Event3Slot extends PanacheEntityBase {

    @Id
    private UUID id;

    @NotNull
    private String role;

    @ManyToOne
    private Person slotUsedBy;

    public Event3Slot(String role) {
        this.setId(UUID.randomUUID());
        this.setRole(role);
        this.setSlotUsedBy(null);
    }

    public Event3Slot() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Person getSlotUsedBy() {
        return slotUsedBy;
    }

    public void setSlotUsedBy(Person slotUsedBy) {
        this.slotUsedBy = slotUsedBy;
    }
}

