package server;

import dataaccess.DataNotFoundException;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    private final Gson srlzr = new Gson();


    private final Javalin javalin;
    private final ChessUserService user = new ChessUserService();
    private final ChessClearService clear = new ChessClearService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser)
                .delete("/db", this::clearDB)
                .post("/session", this::loginUser);

        // Register your endpoints and exception handlers here.

    }

    private void loginUser(Context ctx) {
        try {
            LoginRequest logr = srlzr.fromJson(ctx.body(), LoginRequest.class);
            LoginResult response = user.loginUser(logr);
        } catch (DataNotFoundException e){
            handleError(ctx, 400, e.getMessage());
        }
        catch (UnauthorizedException e) {
            handleError(ctx, 401, e.getMessage());
        }
        catch (Exception e){
            handleError(ctx, 500, e.getMessage());
        }
    }

    private void registerUser(Context ctx) {
        try {
            RegisterRequest r = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult response = user.registerUser(r);
            ctx.contentType("application/json");
            var body = srlzr.toJson(response);
            ctx.result(body);
        } catch (AlreadyTakenException e) {
            handleError(ctx, 403, e.getMessage());
        } catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }

    }

    private void clearDB(Context ctx) {
        try {
            clear.clearAll();
        } catch (Exception e){
            handleError(ctx, 500, e.getMessage());
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    private void handleError(Context ctx, int code, String message){
        ctx.status(code);
        ctx.result(srlzr.toJson(Map.of("message", "Error: %s".formatted(message))));
    }


    public void stop() {
        javalin.stop();
    }
}
