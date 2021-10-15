package de.Marcel1802.eventbot.service;

import de.Marcel1802.eventbot.entities.Game;
import de.Marcel1802.eventbot.entities.ResponseMessage;
import de.Marcel1802.eventbot.entities.event1.Event;
import de.Marcel1802.eventbot.entities.event2.Event2;
import de.Marcel1802.eventbot.entities.event3.Event3;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class GameService {

    public Response getGameByName(String gameName) {

        List<Game> gList = Game.find("title LIKE ?1 OR description LIKE ?1", gameName).list();

        if (gList.isEmpty()){
            return Response.noContent().build(); // 204 No Content
        }
        else {
            return Response.ok(gList).build(); // 200 OK + Liste
        }
    }

    public Response getGameByID(UUID id){
        Game g = Game.findById(id);

        if (g == null){
            return Response.status(400).entity(new ResponseMessage("Game cannot be found")).build();
        }

        return Response.ok().entity(g).build();
    }

    public Response deleteGame(UUID id) {
        if (id == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        Game gameFromDB = Game.findById(id);

        if (gameFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid game provided")).build();
        }

        if (!Event.find("game_id = ?1",gameFromDB).list().isEmpty() || !Event2.find("game_id = ?1",gameFromDB).list().isEmpty() || !Event3.find("game_id = ?1",gameFromDB).list().isEmpty()) {
            return Response.status(400).entity(new ResponseMessage("There are events which are using this game")).build();
        }

        gameFromDB.delete();
        if (gameFromDB.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot delete game")).build();
        }

        return Response.status(200).entity(gameFromDB).build();
    }

    public Response editGame(Game gameParam) {
        if (gameParam == null || gameParam.getId() == null | gameParam.getTitle() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        if (gameParam.getTitle().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid event name")).build();
        }

        if (gameParam.getDescription() == null) {
            gameParam.setDescription("");
        }

        Game gameFromDB = Game.findById(gameParam.getId());

        if (gameFromDB == null) {
            return Response.status(400).entity(new ResponseMessage("Invalid game provided")).build();
        }

        gameFromDB.setDescription(gameParam.getDescription());
        gameFromDB.setTitle(gameParam.getTitle());

        return Response.status(200).entity(gameFromDB).build();
    }

    public Response addGame(Game game) {
        if (game == null || game.getTitle() == null) {
            return Response.status(400).entity(new ResponseMessage("Null value provided")).build();
        }

        if (game.getTitle().replaceAll("\\s+","").equals("")){
            return Response.status(400).entity(new ResponseMessage("Invalid event name")).build();
        }

        if (game.getDescription() == null) {
            game.setDescription("");
        }

        Game newGame = new Game(game.getTitle(), game.getDescription());
        newGame.persist();

        if (!newGame.isPersistent()) {
            return Response.status(500).entity(new ResponseMessage("Cannot persist new game.")).build();
        }

        return Response.status(201).entity(newGame).build();
    }











}

