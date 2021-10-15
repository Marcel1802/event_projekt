package de.Marcel1802.eventbot.rest;


import de.Marcel1802.eventbot.entities.Banlist;
import de.Marcel1802.eventbot.entities.groups.GroupRank;
import de.Marcel1802.eventbot.service.AdminService;

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

    AdminService adminService = new AdminService();

    @PUT
    @Transactional
    @Path("ban/create")
    public Response banUser(Banlist banlist){
        return adminService.banUser(banlist);
    }

    @DELETE
    @Transactional
    @Path("ban/delete/{banID}")
    public Response unbanUser(@PathParam("banID") UUID banID){
        return adminService.unbanUser(banID);
    }

    @GET
    @Path("ban/get/actual")
    public Response getActualBans() {
        return adminService.getActualBans();
    }

    @GET
    @Path("ban/get/permanent")
    public Response getPermanentBans() {
        return adminService.getPermanentBans();
    }

    @GET
    @Path("ban/get/expired")
    public Response getExpiredBans() {
        return adminService.getExpiredBans();
    }

    @GET
    @Path("ban/checkperson/{ID}")
    public Response checkBan(@PathParam("ID") UUID id) {
        return adminService.checkBan(id);
    }

    @POST
    @Transactional
    @Path("group/addMember/{groupID}/{personID}")
    public Response addPersonToGroup(@PathParam("groupID") UUID groupID, @PathParam("personID") UUID personID) {
        return adminService.addPersonToGroup(groupID, personID);
    }

    @DELETE
    @Transactional
    @Path("group/removeMember/{groupID}/{personID}")
    public Response removePersonFromGroup(@PathParam("groupID") UUID groupID, @PathParam("personID") UUID personID) {
        return adminService.removePersonFromGroup(groupID, personID);
    }

    @PUT
    @Transactional
    @Path("group/changeRank/{groupID}/{personID}/{rank}")
    public Response changeGroupRank(@PathParam("groupID") UUID groupID, @PathParam("personID") UUID personID, @PathParam("rank") GroupRank rank) {
        return adminService.changeGroupRank(groupID, personID, rank);
    }

    @GET
    @Path("group/get/all")
    public Response getAllGroups() {
        return adminService.getAllGroups();
    }

    @GET
    @Path("group/get/byID/{ID}")
    public Response getGroupByID(@PathParam("ID") UUID gID) {
        return adminService.getGroupByID(gID);
    }

}
