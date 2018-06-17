package com.hengyi.japp.cas.server;

import com.google.inject.*;
import com.google.inject.name.Named;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.sstore.LocalSessionStore;
import io.vertx.reactivex.ext.web.sstore.SessionStore;

import java.util.Optional;

/**
 * 描述：
 *
 * @author jzb 2018-03-21
 */
public class GuiceCasModule extends AbstractModule {
    private static Injector injector;
    private final Vertx vertx;

    private GuiceCasModule(Vertx vertx) {
        this.vertx = vertx;
    }

    public synchronized static Injector injector(Vertx vertx) {
        return Optional.ofNullable(injector)
                .orElseGet(() -> {
                    final GuiceCasModule module = new GuiceCasModule(vertx);
                    injector = Guice.createInjector(module);
                    return injector;
                });
    }

    @Override
    protected void configure() {
        bind(AdService.class);
        bind(Pac4jConfigFactory.class);
    }

    @Provides
    @Named("config")
    private JsonObject config() {
        return vertx.getOrCreateContext().config();
    }

    @Provides
    private Vertx vertx() {
        return vertx;
    }

    @Provides
    @Singleton
    private SessionStore SessionStore() {
        return LocalSessionStore.create(vertx);
    }

    @Provides
    @Named("http")
    private JsonObject http(@Named("config") JsonObject config) {
        return config.getJsonObject("http", new JsonObject());
    }

    @Provides
    @Named("http.port")
    private int httpPort(@Named("http") JsonObject http) {
        return http.getInteger("port", 9999);
    }

    @Provides
    @Named("ad")
    private JsonObject ad(@Named("config") JsonObject config) {
        return config.getJsonObject("ad", new JsonObject());
    }

    @Provides
    @Named("ad.address")
    private String adAddress(@Named("ad") JsonObject ad) {
        return ad.getString("address", "192.168.0.133");
    }

    @Provides
    @Named("ad.port")
    private int adPort(@Named("ad") JsonObject ad) {
        return ad.getInteger("port", 8722);
    }

    //========== pac4j =====================
    @Provides
    @Named("pac4j")
    private JsonObject pac4j(@Named("config") JsonObject config) {
        return config.getJsonObject("pac4j", new JsonObject());
    }

    @Provides
    @Named("pac4j.baseUrl")
    private String pac4jBaseUrl(@Named("pac4j") JsonObject pac4j, @Named("http.port") int port) {
        final String baseUrl = pac4j.getString("baseUrl", "http://cas.hengyi.com");
        return baseUrl + ":" + port;
    }

}
