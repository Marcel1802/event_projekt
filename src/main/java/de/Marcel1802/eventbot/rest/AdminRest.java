package de.Marcel1802.eventbot.rest;


import de.Marcel1802.eventbot.entities.creationEntities.BanCreation;
import de.Marcel1802.eventbot.entities.groups.GroupRank;
import de.Marcel1802.eventbot.service.AdminService;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("admin")
public class AdminRest {

    @Inject
    AdminService adminService;

    @Inject
    SecurityIdentity securityIdentity;

    @RolesAllowed("event_usermanagement")
    @POST
    @Transactional
    @Path("ban/create")
    public Response banUser(BanCreation ban){
        return adminService.banUser(ban);
    }

    @RolesAllowed("event_usermanagement")
    @DELETE
    @Transactional
    @Path("ban/delete/{banID}")
    public Response unbanUser(@PathParam("banID") UUID banID){
        return adminService.unbanUser(banID);
    }

    @RolesAllowed("event_usermanagement")
    @GET
    @Path("ban/get/actual")
    public Response getActualBans() {
        return adminService.getActualBans();
    }

    @RolesAllowed("event_usermanagement")
    @GET
    @Path("ban/get/permanent")
    public Response getPermanentBans() {
        return adminService.getPermanentBans();
    }

    @RolesAllowed("event_usermanagement")
    @GET
    @Path("ban/get/expired")
    public Response getExpiredBans() {
        return adminService.getExpiredBans();
    }

    @Authenticated
    @GET
    @Path("ban/checkperson/{ID}")
    public Response checkBan(@PathParam("ID") UUID id) {
        return adminService.checkBan(id);
    }

    @RolesAllowed("event_usermanagement")
    @POST
    @Transactional
    @Path("group/addMember/{groupID}/{personID}")
    public Response addPersonToGroup(@PathParam("groupID") UUID groupID, @PathParam("personID") UUID personID) {
        return adminService.addPersonToGroup(groupID, personID);
    }

    @RolesAllowed("event_usermanagement")
    @DELETE
    @Transactional
    @Path("group/removeMember/{groupID}/{personID}")
    public Response removePersonFromGroup(@PathParam("groupID") UUID groupID, @PathParam("personID") UUID personID) {
        return adminService.removePersonFromGroup(groupID, personID);
    }

    @RolesAllowed("event_usermanagement")
    @PUT
    @Transactional
    @Path("group/changeRank/{groupID}/{personID}/{rank}")
    public Response changeGroupRank(@PathParam("groupID") UUID groupID, @PathParam("personID") UUID personID, @PathParam("rank") GroupRank rank) {
        return adminService.changeGroupRank(groupID, personID, rank);
    }

    @RolesAllowed("event_usermanagement")
    @GET
    @Path("group/get/all")
    public Response getAllGroups() {
        return adminService.getAllGroups();
    }

    @RolesAllowed("event_usermanagement")
    @GET
    @Path("group/get/byID/{ID}")
    public Response getGroupByID(@PathParam("ID") UUID gID) {
        return adminService.getGroupByID(gID);
    }

    @Authenticated
    @GET
    @Path("group/get/forPerson/{ID}")
    public Response getGroupsForPerson(@PathParam("ID") UUID id) { return adminService.getGroupsForUser(id); }

}
