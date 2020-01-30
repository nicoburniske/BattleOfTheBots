package com.burnyarosh.api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class ApiMain extends AbstractVerticle {

    @Override
    public void start(Promise<Void> promise) throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
    }
}
