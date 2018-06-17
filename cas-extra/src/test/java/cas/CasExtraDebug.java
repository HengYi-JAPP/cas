package cas;

import com.hengyi.japp.cas.server.MainVerticle;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.Vertx;

public class CasExtraDebug {
    private static final VertxOptions vertxOptions = new VertxOptions()
            .setMaxEventLoopExecuteTime(Long.MAX_VALUE)
            .setMaxWorkerExecuteTime(Long.MAX_VALUE);
    private static final ConfigRetrieverOptions retriever = new ConfigRetrieverOptions()
            .addStore(new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("yaml")
                    .setConfig(new JsonObject()
                            .put("path", "/home/jzb/git/com.hengyi/cas/cas-extra/src/test/resources/config.yml")
                    )
            );
    private static final Vertx vertx = Vertx.vertx(vertxOptions);

    public static void main(String[] args) {
        ConfigRetriever.create(vertx, retriever)
                .rxGetConfig()
                .subscribe(config -> {
                    final DeploymentOptions deploymentOptions = new DeploymentOptions()
                            .setConfig(config);
                    vertx.deployVerticle(MainVerticle.class.getName(), deploymentOptions);
                });
    }
}
