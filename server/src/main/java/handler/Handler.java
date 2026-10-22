package handler;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.*;

public class Handler {

    private ChessUserService user = new ChessUserService();
    private ChessGameService game = new ChessGameService();
    private ChessClearService clear = new ChessClearService();

    private final Gson srlzr = new Gson();

    public Handler() {

    }

    public LoginResult login(Context ctx) throws Exception {
        LoginRequest r = srlzr.fromJson(ctx.body(), LoginRequest.class);
        return user.loginUser(r);
    }

    public void logout(Context ctx) throws Exception{
        LogoutRequest r = srlzr.fromJson(ctx.body(), LogoutRequest.class);
        user.logoutUser(r);
    }

    public RegisterResult register(Context ctx) throws Exception{
        RegisterRequest r = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        return user.registerUser(r);
    }

    public CreateGameResult createGame(Context ctx) throws Exception {
        String authToken = ctx.header("authorization");
        CreateGameRequest cgr = srlzr.fromJson(ctx.body(), CreateGameRequest.class);
        return game.createGame(authToken, cgr);
    }


}
