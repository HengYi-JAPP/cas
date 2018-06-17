package io.vertx.config.yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.vertx.config.spi.ConfigProcessor;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

import static java.nio.charset.StandardCharsets.UTF_8;

public class YamlProcessor implements ConfigProcessor {

    public static ObjectMapper YAML_MAPPER = new YAMLMapper();

    @Override
    public String name() {
        return "yaml";
    }

    @Override
    public void process(Vertx vertx, JsonObject configuration, Buffer input, Handler<AsyncResult<JsonObject>> handler) {
        if (input.length() == 0) {
            handler.handle(Future.succeededFuture(new JsonObject()));
            return;
        }

        vertx.executeBlocking(future -> {
            try {
                JsonNode root = YAML_MAPPER.readTree(input.toString(UTF_8));
                JsonObject json = new JsonObject(root.toString());
                future.complete(json);
            } catch (Exception e) {
                future.fail(e);
            }
        }, handler);
    }
}

