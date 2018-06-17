package com.hengyi.japp.cas.server;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServerRequest;
import io.vertx.reactivex.ext.auth.User;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.TemplateHandler;
import io.vertx.reactivex.ext.web.templ.HandlebarsTemplateEngine;
import io.vertx.reactivex.ext.web.templ.TemplateEngine;

import java.util.Optional;

import static org.pac4j.core.context.HttpConstants.LOCATION_HEADER;

/**
 * @author jzb 2018-04-13
 */
public class MainVerticle extends AbstractVerticle {
    public static Injector GUICE;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        GUICE = GuiceCasModule.injector(vertx);

        final Router router = Router.router(vertx);
        final Pac4jConfigFactory configFactory = GUICE.getInstance(Pac4jConfigFactory.class);
        configFactory.enable(router);

        // http://localhost:9999/casChangePassword.hbs
        router.post("/api/casChangePassword").blockingHandler(this::casChangePassword);
        router.route().handler(StaticHandler.create());
        final TemplateEngine engine = HandlebarsTemplateEngine.create();
        router.route().handler(TemplateHandler.create(engine));

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(GUICE.getInstance(Key.get(Integer.class, Names.named("http.port"))))
                .toCompletable()
                .subscribe(startFuture::complete, startFuture::fail);
    }

    private void casChangePassword(RoutingContext rc) {
        final AdService adService = GUICE.getInstance(AdService.class);
        final HttpServerRequest request = rc.request();
        final String oldpassword = request.getFormAttribute("oldpassword");
        final String newpassword = request.getFormAttribute("newpassword");
        final String uid = Optional.ofNullable(rc.user())
                .map(User::principal)
                .map(it -> it.getJsonObject("CasClient"))
                .map(it -> it.getString("uid"))
                .orElse(null);
        adService.change(uid, oldpassword, newpassword).subscribe(
                () -> rc.response().putHeader(LOCATION_HEADER, "http://info.hengyi.com").setStatusCode(303).end(),
                ex -> {
                    rc.put("error", true);
                    rc.reroute("/casChangePassword.hbs");
                }
        );
    }

}
