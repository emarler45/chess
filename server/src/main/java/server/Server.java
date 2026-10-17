package server;

import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    record RegisterRequest(
            String username,
            String password,
            String email
    ){}

    private final Javalin javalin;
    private final ChessService service = new ChessService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser);

        // Register your endpoints and exception handlers here.

    }

    private void registerUser(Context ctx) {
        RegisterRequest r = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        String body = new Gson().toJson(Map.of("message", "hello!"));
        ctx.contentType("application/json");
        ctx.result(body);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
