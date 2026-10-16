package server;

import io.javalin.*;
import io.javalin.http.Context;
import service.ChessService;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final ChessService service = new ChessService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::getReturnTest);

        // Register your endpoints and exception handlers here.

    }

    private void getReturnTest(Context context) {
        String body = new Gson().toJson(Map.of("message", "hello!"));
        context.contentType("application/json");
        context.result(body);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }



    public void stop() {
        javalin.stop();
    }
}
