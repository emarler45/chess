package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;

public class Handler {

    private final ChessService service = new ChessService();

    private final Gson srlzr = new Gson();

    public Handler() {

    }

    public LoginResult login(Context ctx) throws Exception {
        LoginRequest r = srlzr.fromJson(ctx.body(), LoginRequest.class);
        return service.loginUser(r);
    }

    public void logout(Context ctx) throws Exception{
        LogoutRequest r = srlzr.fromJson(ctx.body(), LogoutRequest.class);
        service.logoutUser(r);
    }

    public RegisterResult register(Context ctx) throws Exception{
        RegisterRequest r = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        return service.registerUser(r);
    }

    public CreateGameResult createGame(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        CreateGameRequest cgr = srlzr.fromJson(ctx.body(), CreateGameRequest.class);
        return service.createGame(authToken, cgr);
    }

    public void joinGame(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        JoinGameRequest r = srlzr.fromJson(ctx.body(), JoinGameRequest.class);
        service.joinGame(authToken, r);
    }

    public ListGamesResult listGames(Context ctx) throws Exception {

    }

    public void clearAll(){
        service.clearAll();
    }
}
