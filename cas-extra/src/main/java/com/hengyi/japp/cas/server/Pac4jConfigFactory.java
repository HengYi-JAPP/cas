package com.hengyi.japp.cas.server;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.vertx.core.http.HttpMethod;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.auth.AuthProvider;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.*;
import io.vertx.reactivex.ext.web.sstore.SessionStore;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.CasProxyReceptor;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.http.HttpActionAdapter;
import org.pac4j.vertx.auth.Pac4jAuthProvider;
import org.pac4j.vertx.cas.logout.VertxCasLogoutHandler;
import org.pac4j.vertx.context.session.VertxSessionStore;
import org.pac4j.vertx.core.store.VertxLocalMapStore;
import org.pac4j.vertx.handler.impl.CallbackHandler;
import org.pac4j.vertx.handler.impl.CallbackHandlerOptions;
import org.pac4j.vertx.handler.impl.SecurityHandler;
import org.pac4j.vertx.handler.impl.SecurityHandlerOptions;
import org.pac4j.vertx.http.DefaultHttpActionAdapter;

import java.util.Set;
import java.util.regex.Pattern;

@Singleton
public class Pac4jConfigFactory implements ConfigFactory {
    private final String CALLBACK_PATH = "/callback";
    private final Pattern anonymousPattern = Pattern.compile(".*\\.(css|js|ico|json)", Pattern.CASE_INSENSITIVE);
    private final Vertx vertx;
    private final Pac4jAuthProvider pac4jAuthProvider = new Pac4jAuthProvider();
    private final AuthProvider authProvider = new AuthProvider(pac4jAuthProvider);
    private final HttpActionAdapter httpActionAdapter = new DefaultHttpActionAdapter();
    private final String baseUrl;
    private final SessionStore sessionStore;
    private final VertxSessionStore vertxSessionStore;

    @Inject
    private Pac4jConfigFactory(Vertx vertx, @Named("pac4j.baseUrl") String baseUrl, SessionStore sessionStore) {
        this.vertx = vertx;
        this.baseUrl = baseUrl;
        this.sessionStore = sessionStore;
        this.vertxSessionStore = new VertxSessionStore(sessionStore.getDelegate());
    }

    public void enable(Router router) {
        final Config config = build();

        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler.create(sessionStore));
        router.route().handler(UserSessionHandler.create(authProvider));
        router.route().handler(BodyHandler.create());
        router.route().handler(ResponseContentTypeHandler.create());
        final Set<String> allowHeaders = Sets.newHashSet("x-requested-with", "Access-Control-Allow-Origin", "origin", "Content-Type", "accept");
        router.route().handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.PUT).allowedMethod(HttpMethod.OPTIONS).allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.DELETE).allowedMethod(HttpMethod.PATCH));

        final CallbackHandler callbackHandler = callbackHandler(config);
        router.getDelegate().get(CALLBACK_PATH).handler(callbackHandler);
        router.post(CALLBACK_PATH).handler(BodyHandler.create().setMergeFormAttributes(true));
        router.getDelegate().post(CALLBACK_PATH).handler(callbackHandler);

        final SecurityHandler securityHandler = securityHandler(config);
        router.getDelegate().route().handler(securityHandler);
    }

    @Override
    public Config build(Object... parameters) {
        final Config config = new Config(baseUrl + CALLBACK_PATH, casClient());
        config.setHttpActionAdapter(httpActionAdapter);
        config.setSessionStore(vertxSessionStore);
        config.addMatcher("CasClient", webContext -> {
            final String path = webContext.getPath();
            return !anonymousPattern.matcher(path).matches();
        });
        return config;
    }

    private CasClient casClient() {
        final CasConfiguration casConfiguration = new CasConfiguration("http://cas.hengyi.com:8080/login");
        final CasProxyReceptor casProxyReceptor = new CasProxyReceptor();
        casConfiguration.setProxyReceptor(casProxyReceptor);
        final VertxLocalMapStore vertxLocalMapStore = new VertxLocalMapStore(this.vertx.getDelegate());
        final VertxCasLogoutHandler vertxCasLogoutHandler = new VertxCasLogoutHandler(vertxLocalMapStore, true);
        casConfiguration.setLogoutHandler(vertxCasLogoutHandler);
        return new CasClient(casConfiguration);
    }

    private CallbackHandler callbackHandler(Config config) {
        final CallbackHandlerOptions callbackHandlerOptions = new CallbackHandlerOptions()
                .setDefaultUrl("/casChangePassword.hbs");
        return new CallbackHandler(vertx.getDelegate(), vertxSessionStore, config, callbackHandlerOptions);
    }

    private SecurityHandler securityHandler(Config config) {
        final SecurityHandlerOptions securityHandlerOptions = new SecurityHandlerOptions()
                .setClients("CasClient")
                .setMatchers("CasClient");
        return new SecurityHandler(vertx.getDelegate(), vertxSessionStore, config, pac4jAuthProvider, securityHandlerOptions);
    }

}
