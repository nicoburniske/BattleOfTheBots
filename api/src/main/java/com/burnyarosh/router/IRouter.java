package com.burnyarosh.router;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public interface IRouter {
    Router initialize(Vertx vertx);
}
