package server;

import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    private final Gson srlzr = new Gson();


    private final Javalin javalin;
    private final ChessUserService service = new ChessUserService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser)
                .delete("/db", this::clearDB);

        // Register your endpoints and exception handlers here.

    }

    private void registerUser(Context ctx) {
        try {
            RegisterRequest r = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult response = service.registerUser(r);
            ctx.contentType("application/json");
            var body = srlzr.toJson(response);
            ctx.result(body);
        } catch (AlreadyTakenException e) {
            ctx.status(403);
            ctx.result(srlzr.toJson(Map.of("message", "Error: %s".formatted(e.getMessage()))));
        } catch (Exception e) {
            ctx.status(500);
            ctx.result(srlzr.toJson(Map.of("message", "Error: %s".formatted(e.getMessage()))));
        }

    }

    private void clearDB(Context ctx) {

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
