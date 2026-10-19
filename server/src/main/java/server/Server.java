package server;

import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import service.CSR.*;
import com.google.gson.Gson;

public class Server {

    private final Gson srlzr = new Gson();


    private final Javalin javalin;
    private final ChessUserService service = new ChessUserService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser);

        // Register your endpoints and exception handlers here.

    }

    private void registerUser(Context ctx) {
        RegisterRequest r = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult response = service.registerUser(r);
        ctx.contentType("application/json");
        var body = srlzr.toJson(response);
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
