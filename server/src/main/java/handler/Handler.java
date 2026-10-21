package handler;

import service.*;

public class Handler {

    private ChessUserService user = new ChessUserService();
    private ChessGameService game = new ChessGameService();
    private ChessClearService clear = new ChessClearService();

    public Handler() {

    }


}
