package de.Marcel1802.eventbot.rest;

import de.Marcel1802.eventbot.entities.Game;
import de.Marcel1802.eventbot.service.GameService;

import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("game")
public class GameRest {

    GameService gameService = new GameService();

    @GET
    @Path("get/all")
    public Response getAllGames(){
        return Response.ok(Game.findAll().list()).build();
    }

    @GET
    @Path("get/byName/{gamename}")
    public Response getGameByName(@PathParam("gamename") String gameName){
        return gameService.getGameByName(gameName);
    }

    @GET
    @Path("get/byID/{id}")
    public Response getGameByID(@PathParam("id") UUID id){
        return gameService.getGameByID(id);
    }

    @DELETE
    @Transactional
    @Path("delete/{id}")
    public Response deleteGame(@PathParam("id") UUID id) {
        return gameService.deleteGame(id);
    }

    @PUT
    @Transactional
    @Path("edit")
    public Response editGame(Game game) {
        return gameService.editGame(game);
    }

    @POST
    @Transactional
    @Path("create")
    public Response addGame(Game game) {return gameService.addGame(game);}


}
