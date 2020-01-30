package com.burnyarosh;

import com.burnyarosh.router.ApiRouter;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class ApiMain {
    private final ApiRouter apiRouter;

    public ApiMain() {
        this.apiRouter  = new ApiRouter();
    }

    public void startRouter() {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.mountSubRouter("api/v1", apiRouter.initialize(vertx));
        server.requestHandler(router).listen(8081); //TODO: decide on port #
    }
}
