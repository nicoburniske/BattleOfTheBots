# BattleOfTheBots

This is an event driven and non-blocking Chess game server written in Java, with the help of the [Eclipse Vert.x library](https://vertx.io/). 
It utilizes WebSocket protocol in order to facilitate performant communication between the clients/players.

The server architecture breaks down as follows:
There are three main [verticles](https://vertx.io/docs/vertx-core/java/#_verticles)
1. __ServerVerticle__ : Handles the websocket connections with the players.
2. __GameLobbyVerticle__ : Keeps track of active players and games. Responsible for registration requests, forming/joining lobbies, etc. Deploys new GameVerticles when needed.
3. __GameVerticle__ : Keeps track of an indiviual game between two players. Game state, processing, etc. are all taken care of here.

The GameVerticle contains a ChessBoard object (within /board package), which is the internal representation of a chess game in play. The board is the *Model* from a MVC perspective.

The features that still need to be implemented are:
- Persistence with a database.
- A cli client.
- A computer opponent, with differing levels of difficulty.

The end goal for this project is to enable bots/programs to play each other and have them to complete as many games as possible. This could be done to train models, or to see whose bot stacks up to be the strongest.
