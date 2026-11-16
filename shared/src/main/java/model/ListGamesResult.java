package model;

import java.util.ArrayList;

public record ListGamesResult(
        ArrayList<GameInfo> games
) {
}
