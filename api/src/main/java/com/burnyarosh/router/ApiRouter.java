package com.burnyarosh.router;

import com.burnyarosh.router.sub.ChessRouter;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ApiRouter implements IRouter {
    private final ChessRouter chessRouter;
    public ApiRouter(ChessProcessor chessProcessor) {
        chessRouter = new ChessRouter(chessProcessor);
    }
    public Router initialize(Vertx vertx) {
        Router router = Router.router(vertx);
        router.mountSubRouter("/game", chessRouter.initialize(vertx));
        return router;
    }
}
