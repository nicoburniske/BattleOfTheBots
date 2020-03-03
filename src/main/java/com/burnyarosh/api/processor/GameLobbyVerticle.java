package com.burnyarosh.api.processor;


import com.burnyarosh.api.dto.entity.Entity;
import com.burnyarosh.api.dto.entity.Game;
import com.burnyarosh.api.dto.entity.Player;
import com.burnyarosh.api.dto.in.NewLobbyDTO;
import com.burnyarosh.api.dto.in.NewPlayerDTO;
import com.burnyarosh.api.dto.in.PlayerTurnDTO;
import com.burnyarosh.api.dto.internal.RemovePlayerDTO;
import com.burnyarosh.api.dto.out.*;
import com.burnyarosh.api.dto.out.lobby.*;
import com.burnyarosh.api.dto.in.JoinLobbyDTO;
import com.burnyarosh.api.exception.HandledMessageException;
import com.burnyarosh.api.exception.handler.MessageFailureHandler;
import com.burnyarosh.api.exception.message.*;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.burnyarosh.api.processor.utils.EventBusAddress.*;

public class GameLobbyVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLobbyVerticle.class);
    private final MessageFailureHandler failureHandler = new MessageFailureHandler();
    // TODO: remove useless constants
    private static final String ERROR_CONFLICT_USERNAME_ALREADY_TAKEN = "Conflict: Username already taken";
    private static final String ERROR_NO_PLAYER_EXISTS = "Error: Player does not exist";
    private static final String ERROR_MALFORMED_REQUEST = "Malformed request";
    private static final String ERROR_MALFORMED_REQUEST_TEMPLATE = "Malformed request: %s";
    private static final String ERROR_CANNOT_ADD_PLAYER_TO_GAME = "Error: cannot add player to game";

    List<Player> activePlayers = new ArrayList<>();
    List<Game> activeGames = new ArrayList<>();
    private Map<String, Game> activeGamesById = new HashMap<>();
    private Map<String, Player> activePlayersById = new HashMap<>();
    private Map<String, Player> activePlayersByName = new HashMap<>();
    private Map<String, String> verticleDeploymentByGUID = new HashMap<>();

    @Override
    public void start(Promise<Void> promise) {
        MessageConsumer<NewPlayerDTO> newPlayer = vertx.eventBus().consumer(NEW_PLAYER_ADDRESS.getAddressString());
        newPlayer.handler(this::newPlayer);

        MessageConsumer<NewLobbyDTO> newLobby = vertx.eventBus().consumer(NEW_LOBBY_ADDRESS.getAddressString());
        newLobby.handler(this::newLobby);

        MessageConsumer<JoinLobbyDTO> joinLobby = vertx.eventBus().consumer(JOIN_LOBBY_ADDRESS.getAddressString());
        joinLobby.handler(this::joinLobby);

        MessageConsumer<Void> listLobbies = vertx.eventBus().consumer(LIST_LOBBY_ADDRESS.getAddressString());
        listLobbies.handler(this::listLobbies);

        MessageConsumer<Void> listPlayers = vertx.eventBus().consumer(LIST_PLAYER_ADDRESS.getAddressString());
        listPlayers.handler(this::listPlayers);

        MessageConsumer<PlayerTurnDTO> newMove = vertx.eventBus().consumer(NEW_MOVE_ADDRESS.getAddressString());
        newMove.handler(this::newMove);

        MessageConsumer<RemovePlayerDTO> removePlayer = vertx.eventBus().consumer(REMOVE_PLAYER_ADDRESS.getAddressString());
        removePlayer.handler(this::removePlayer);

        super.vertx.exceptionHandler(this::handleException);

        LOGGER.info("Successfully deployed GameLobbyVerticle");
        promise.complete();
    }


    /**
     * Verticle Exception handler
     */
    private void handleException(Throwable throwable) {
        if (throwable instanceof HandledMessageException) {
            ((HandledMessageException) throwable).callLobbyFailureHandler(this.failureHandler);
        } else {
            throwable.printStackTrace();
        }
    }

    /**
     * @param message
     */
    private void newMove(Message<PlayerTurnDTO> message) {
        PlayerTurnDTO body = message.body();
        String lobbyGUID = body.getGameGUID();
        String playerGUID = body.getPlayerGUID();
        Game currGame = activeGamesById.get(lobbyGUID);
        Player currPlayer = activePlayersById.get(playerGUID);
        if (currGame != null && currPlayer != null) {
            String lobbyAddress = String.format(GAME_MOVE_ADDRESS.getAddressString(), verticleDeploymentByGUID.get(lobbyGUID));
            super.vertx.eventBus().publish(lobbyAddress, body);
            message.reply(new SuccessDTO().toJson());
        } else {
            throw new UnauthorizedPlayerException(message);
        }
    }

    /**
     * This DTO should have been verified previously.
     * Will create a new active Player. Replies to the message with the players GUID and username if there is no conflict.
     */
    private void newPlayer(Message<NewPlayerDTO> message) {
        NewPlayerDTO dto = message.body();
        for (Player p : activePlayers) {
            if (dto.getUsername().equals(p.getName())) throw new NewPlayerConflictException(message);
        }
        Player temp = new Player(dto.getUsername(), Player.generateGUID());
        this.activePlayers.add(temp);
        this.activePlayersById.put(temp.getId(), temp);
        this.activePlayersByName.put(temp.getName(), temp);
        LOGGER.info(String.format("Registered player %s with id %s", temp.getName(), temp.getId()));
        message.reply(new PlayerCreatedDTO(temp.getId(), temp.getName()).toJson());
    }

    /**
     * Responds with a list of all currently active players.
     */
    private void listPlayers(Message<Void> message) {
        List<String> players = new ArrayList<>();
        this.activePlayers.forEach(player -> {
            players.add(player.getName());
        });
        message.reply(new ListPlayersDTO(players).toJson());
    }

    /**
     * Responds to the message with a list of all currently available (non-full) games.
     */
    private void listLobbies(Message<Void> message) {
        List<LobbyDTO> availableLobbies = new ArrayList<>();
        this.activeGames.stream().filter(game -> !game.gameIsReady()).forEach(game -> {
            availableLobbies.add(new LobbyDTO(game.getId(), game.getPlayer(1).getName()));
        });
        message.reply(new ListLobbiesDTO(availableLobbies).toJson());
    }

    /**
     * Attempts to have the given player in the JoinLobbyDTO join the given game.
     */
    private void joinLobby(Message<JoinLobbyDTO> message) {
        JoinLobbyDTO dto = message.body();
        String gameID = dto.getGameGUID();
        String playerID = dto.getPlayerGUID();
        Game currGame = activeGamesById.get(gameID);
        Player currPlayer = activePlayersById.get(playerID);

        if (currGame != null && currPlayer != null && !currGame.gameIsReady() && !currPlayer.getIsBusy()) {
            currPlayer.setBusy(true);
            currGame.addSecondPlayer(activePlayersById.get(playerID));
            JsonObject json = activeGamesById.get(gameID).toJson();
            json.put("type", "setup");
            String address = String.format(GAME_BASE_ADDRESS.getAddressString(), verticleDeploymentByGUID.get(gameID));
            super.vertx.eventBus().publish(address, json);
            message.reply(new SuccessDTO().toJson());
        } else {
            throw new GameFullException(message);
        }
    }

    /**
     * Message should have param guid
     */
    private void newLobby(Message<NewLobbyDTO> request) {
        NewLobbyDTO dto = request.body();
        String guid = dto.getPlayerGUID();
        Player player = this.activePlayersById.get(guid);
        if (player == null) throw new UnauthorizedPlayerException(request);
        if (player.getIsBusy()) throw new InvalidGameCreationException(request);

        String lobbyguid = Entity.generateGUID();
        Game game = new Game(lobbyguid, lobbyguid, player);
        this.activeGamesById.put(lobbyguid, game);
        this.activeGames.add(game);
        player.setBusy(true);

        super.vertx.deployVerticle(GameVerticle.class.getName(), ar -> {
            if (ar.succeeded()) {
                verticleDeploymentByGUID.put(lobbyguid, ar.result());
                LOGGER.info(String.format("Deployed verticle %s for game %s", ar.result(), guid));
                request.reply(new LobbyCreatedDTO(lobbyguid).toJson());
            } else {
                // should never happen
                LOGGER.error("Game Verticle Deployment failed");
                request.fail(400, ERROR_MALFORMED_REQUEST);
            }
        });
    }

    private void removePlayer(Message<RemovePlayerDTO> removePlayerDTOMessage) {
        RemovePlayerDTO dto = removePlayerDTOMessage.body();
        // remove player from list of active players
        Player p = this.activePlayersById.get(dto.getPlayerGUID());
        if (p != null) this.activePlayers.remove(p);
        // remove player from hashmaps
        this.activePlayersById.remove(p.getId());
        this.activePlayersByName.remove(p.getName());
        // then remove player from the game
        for (Game g : this.activeGames) {
            if (g.includesPlayer(p.getId())) {
                g.removePlayer(p);
                String verticle = this.verticleDeploymentByGUID.get(g.getId());
                if (verticle != null) {
                    vertx.undeploy(verticle);
                }
                // successful removal of player
                removePlayerDTOMessage.reply(new SuccessDTO().toJson());
            }
        }
        // TODO: review this implementation
        removePlayerDTOMessage.fail(400, "Player not in any active games");
    }

    /**
     * @param currPlayer
     * @return
     */
    private boolean playerIsAvailable(Player currPlayer) {
        for (Game g : this.activeGames) {
            if (g.includesPlayer(currPlayer.getId())) return false;
        }
        return true;
    }
}
