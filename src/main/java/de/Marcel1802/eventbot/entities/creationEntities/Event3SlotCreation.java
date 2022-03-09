package de.Marcel1802.eventbot.entities.creationEntities;

import de.Marcel1802.eventbot.entities.Person;

public class Event3SlotCreation {

    public String role;
    public Person personUsedBy;

    public Event3SlotCreation(String role, Person personUsedBy) {
        this.role = role;
        this.personUsedBy = personUsedBy;
    }

    public Event3SlotCreation() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Person getPersonUsedBy() {
        return personUsedBy;
    }

    public void setPersonUsedBy(Person personUsedBy) {
        this.personUsedBy = personUsedBy;
    }
}
