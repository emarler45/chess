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
    private final ChessGameService game = new ChessGameService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser)
                .delete("/db", this::clearDB)
                .post("/session", this::loginUser)
                .delete("/session", this::logoutUser)
                .post("/game", this::createGame);

        // Register your endpoints and exception handlers here.

    }

    private void logoutUser(Context ctx){
        try {
            LogoutRequest r = srlzr.fromJson(ctx.body(), LogoutRequest.class);
            user.logoutUser(r);
        } catch (UnauthorizedException e) {
            handleError(ctx, 401, e.getMessage());
        } catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }
    }

    private void loginUser(Context ctx) {
        try {
            LoginRequest r = srlzr.fromJson(ctx.body(), LoginRequest.class);
            LoginResult response = user.loginUser(r);
            handleSuccess(ctx, response);
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
            handleSuccess(ctx, response);
        } catch (AlreadyTakenException e) {
            handleError(ctx, 403, e.getMessage());
        } catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }

    }

    private void createGame(Context ctx) {
        try {
            CreateGameRequest cgr = srlzr.fromJson(ctx.body(), CreateGameRequest.class);
            CreateGameResult response = game.createGame(cgr);
            handleSuccess(ctx, response);
        } catch (Exception e) {

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

    private void handleSuccess(Context ctx, Record response){
        ctx.contentType("application/json");
        var body = srlzr.toJson(response);
        ctx.result(body);
    }

    public void stop() {
        javalin.stop();
    }
}
