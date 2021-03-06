package de.Marcel1802.eventbot.service;


import de.Marcel1802.eventbot.entities.Banlist;
import de.Marcel1802.eventbot.entities.Person;
import de.Marcel1802.eventbot.entities.ResponseMessage;
import de.Marcel1802.eventbot.entities.creationEntities.BanCreation;
import de.Marcel1802.eventbot.entities.groups.Group;
import de.Marcel1802.eventbot.entities.groups.GroupRank;
import de.Marcel1802.eventbot.entities.groups.RelGroupPerson;
import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class AdminService {

    @Inject
    SecurityIdentity securityIdentity;


    public Response banUser(BanCreation banlist) {

        System.out.println(banlist.toString());

        if (banlist.getBannedPerson() == null || banlist.getReason() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build(); // 400 Bad Request
        }

        if (banlist.getReason().equals("")) {
            return Response.status(400).entity(new ResponseMessage("No reason provided")).build(); // 400 Bad Request
        }

        if (!banlist.getPermanent()) {

            if (banlist.getBannedUntil() == null) {
                return Response.status(400).entity(new ResponseMessage("No end of ban provided")).build(); // 400 Bad Request
            }

            if (banlist.getBannedUntil().isBefore(LocalDateTime.now())) {
                return Response.status(400).entity(new ResponseMessage("The end of the ban has to be in the future")).build(); // 400 Bad Request
            }

        }

        Person personWhoBanned = Person.find("gamertag = ?1", securityIdentity.getPrincipal().getName()).firstResult();

        if (personWhoBanned == null) {
            return Response.status(400).entity(new ResponseMessage("Cannot find person who banned")).build();
        }

        Person personToBan = Person.findById(UUID.fromString(banlist.getBannedPerson().getId()));

        if (personToBan == null) {
            return Response.status(400).entity(new ResponseMessage("Person to ban to found")).build(); // 400 Bad Request
        }

        /*
        if (Banlist.find("(gamertag = ?1 AND ispermanent = true) OR (gamertag = ?1 AND banneduntil > ?2",personToBan, LocalDateTime.now()).firstResult() != null) {
            return Response.status(400).entity(new ResponseMessage("The person is already banned.")).build();
        }
        */

        Banlist newBan = new Banlist(personToBan, personWhoBanned, banlist.getBannedUntil(), banlist.getReason(), banlist.getPermanent());

        newBan.persist();

        if (!newBan.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot save new ban")).build(); // 500 Internal
        }

        return Response.ok().entity(newBan).build(); // 200 OK
    }

    public Response unbanUser(UUID id){
        Banlist ban = Banlist.findById(id);
        if (ban == null){
            return Response.status(400).entity(new ResponseMessage("Ban cannot be found")).build(); // 400 Bad Request
        }

        ban.delete();
        if (ban.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete ban")).build(); // 500 Internal
        }

        return Response.ok().entity(ban).build(); // 200 OK
    }

    public Response getActualBans() {
        List<Banlist> returnList = Banlist.find("ispermanent = false AND banneduntil > ?1", LocalDateTime.now()).list();
        if (returnList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no entries on the list")).build();
        }
        return Response.status(200).entity(returnList).build();
    }

    public Response getPermanentBans() {
        List<Banlist> returnList = Banlist.find("ispermanent = true").list();
        if (returnList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no entries on the list")).build();
        }
        return Response.status(200).entity(returnList).build();
    }

    public Response getExpiredBans() {
        List<Banlist> returnList = Banlist.find("ispermanent = false AND banneduntil < ?1", LocalDateTime.now()).list();
        if (returnList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("There are no entries on the list")).build();
        }
        return Response.status(200).entity(returnList).build();
    }

    public Response checkBan(UUID id) {

        if (id == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Person p = Person.findById(id);

        if (p == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person provided")).build();
        }

        if (!securityIdentity.getPrincipal().getName().equals(p.getGamertag()) && !securityIdentity.getRoles().contains("event_usermanagement")) {
            return Response.status(403).entity(new ResponseMessage("Not allowed")).build();
        }

        if (Banlist.find("ispermanent = true AND bannedperson_id = ?1",p).firstResult() != null || Banlist.find("bannedperson_id = ?1 AND banneduntil < ?2", p, LocalDateTime.now()).firstResult() != null) {
            return Response.status(200).entity(true).build();
        }
        else {
            return Response.status(200).entity(false).build();
        }
    }

    public Response addPersonToGroup(UUID groupID, UUID personID) {

        if (groupID == null || personID == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Group g = Group.findById(groupID);

        if (g == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid group prodided")).build();
        }

        Person p = Person.findById(personID);

        if (p == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person provided")).build();
        }

        if (RelGroupPerson.find("group_id = ?1 AND person_id = ?2",g,p).firstResult() != null) {
            return Response.status(400).entity("This person is already member of this group").build();
        }

        RelGroupPerson newRel = new RelGroupPerson(g,p, GroupRank.MEMBER);
        newRel.persist();

        if (!newRel.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Server cannot save new membership")).build();
        }

        return Response.status(200).entity(newRel).build();

    }

    public Response removePersonFromGroup(UUID groupID, UUID personID) {
        if (groupID == null || personID == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Group g = Group.findById(groupID);

        if (g == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid group prodided")).build();
        }

        Person p = Person.findById(personID);

        if (p == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person provided")).build();
        }

        RelGroupPerson relFromDB = RelGroupPerson.find("group_id = ?1 AND person_id = ?2",g,p).firstResult();

        if (relFromDB == null) {
            return Response.status(400).entity("This person is not a member of this group").build();
        }

        relFromDB.delete();
        if (relFromDB.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete membership")).build();
        }

        return Response.status(200).entity(relFromDB).build();
    }

    public Response changeGroupRank(UUID groupID, UUID personID, GroupRank rank) {
        if (groupID == null || personID == null || rank == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Group g = Group.findById(groupID);

        if (g == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid group prodided")).build();
        }

        Person p = Person.findById(personID);

        if (p == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid person provided")).build();
        }

        RelGroupPerson relFromDB = RelGroupPerson.find("group_id = ?1 AND person_id = ?2",g,p).firstResult();

        if (relFromDB == null) {
            return Response.status(400).entity("This person is not a member of this group").build();
        }

        relFromDB.setRank(rank);

        return Response.status(200).entity(relFromDB).build();

    }

    public Response getAllGroups() {
        List<Group> returnList = Group.listAll();

        if (returnList.isEmpty()) {
            return Response.status(204).entity(new ResponseMessage("List is empty")).build();
        }

        return Response.status(200).entity(returnList).build();

    }

    public Response getGroupByID(UUID id) {
        Group returnValue = Group.findById(id);

        if (returnValue == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid UUID")).build();
        }
        return Response.status(200).entity(returnValue).build();
    }

    public Response getGroupsForUser(UUID userID) {

        if (userID == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided.")).build();
        }

        Person personFromDB = Person.findById(userID);

        if (personFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid user ID provided")).build();
        }

        if (!securityIdentity.getPrincipal().getName().equals(personFromDB.getGamertag()) && !securityIdentity.getRoles().contains("event_usermanagement")) {
            return Response.status(403).entity(new ResponseMessage("Not allowed")).build();
        }

        List<RelGroupPerson> relGroupPeople = RelGroupPerson.find("person_id = ?1",personFromDB).list();

        if (relGroupPeople.isEmpty()) {
            return Response.status(204).build();
        }

        Set<Group> grpList = new HashSet<>();

        for (RelGroupPerson elem : relGroupPeople) {
            grpList.add(elem.getGroup());
        }

        return Response.status(200).entity(grpList).build();

    }

}
