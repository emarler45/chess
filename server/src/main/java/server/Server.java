package server;

import dataaccess.DataNotFoundException;
import service.CreateGameResult;
import handler.Handler;
import service.LoginResult;
import service.RegisterResult;
import io.javalin.*;
import io.javalin.http.Context;
import service.*;
import com.google.gson.Gson;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final Handler handler = new Handler();
    private final ChessUserService user = new ChessUserService();
    private final ChessClearService clear = new ChessClearService();
    private final ChessGameService game = new ChessGameService();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser)
                .delete("/db", this::clearDB)
                .post("/session", this::loginUser)
                .delete("/session", this::logoutUser)
                .post("/game", this::createGame)
                .get("/game", this::listGames)
                .put("/game", this::joinGame);

        // Register your endpoints and exception handlers here.

    }

    private void logoutUser(Context ctx){
        try {
            handler.logout(ctx);
        } catch (UnauthorizedException e) {
            handleError(ctx, 401, e.getMessage());
        } catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }
    }

    private void loginUser(Context ctx) {
        try {
            LoginResult response = handler.login(ctx);
            handleSuccess(ctx, response);
        } catch (IllegalArgumentException | DataNotFoundException e){
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
            RegisterResult response = handler.register(ctx);
            handleSuccess(ctx, response);
        } catch (IllegalArgumentException e) {
            handleError(ctx, 400, e.getMessage());
        } catch (AlreadyTakenException e) {
            handleError(ctx, 403, e.getMessage());
        } catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }

    }

    private void createGame(Context ctx) {
        try {
            CreateGameResult response = handler.createGame(ctx);
            handleSuccess(ctx, response);
        } catch (IllegalArgumentException e) {
            handleError(ctx, 400, e.getMessage());
        } catch (UnauthorizedException e) {
            handleError(ctx, 401, e.getMessage());
        } catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }
    }

    private void listGames(Context ctx) {
        try{
            ListGamesResult response = handler.listGames(ctx);
            handleSuccess(ctx, response);
        } catch (UnauthorizedException e) {
            handleError(ctx, 401, e.getMessage());
        }
        catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }
    }

    private void joinGame(Context ctx) {
        try {
            handler.joinGame(ctx);
        } catch (IllegalArgumentException e) {
            handleError(ctx, 400, e.getMessage());
        } catch (UnauthorizedException e) {
            handleError(ctx, 401, e.getMessage());
        } catch (AlreadyTakenException e) {
            handleError(ctx, 403, e.getMessage());
        } catch (Exception e) {
            handleError(ctx, 500, e.getMessage());
        }
    }

    private void clearDB(Context ctx) {
        try {
            handler.clearAll();
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
        ctx.result(new Gson().toJson(Map.of("message", "Error: %s".formatted(message))));
    }

    private void handleSuccess(Context ctx, Record response){
        ctx.contentType("application/json");
        var body = new Gson().toJson(response);
        ctx.result(body);
    }

    public void stop() {
        javalin.stop();
    }
}
