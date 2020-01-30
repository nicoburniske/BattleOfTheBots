package com.burnyarosh.router.sub;

import com.burnyarosh.router.IRouter;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class ChessRouter implements IRouter {

    public ChessRouter(ChessProcessor chessProcessor) {

    }

    public Router initialize(Vertx vertx) {
        return null;
    }
}
