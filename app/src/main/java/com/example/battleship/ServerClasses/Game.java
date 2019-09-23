package com.example.battleship.ServerClasses;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private static final String DEFUALT_PLAYER = "default";
    private GameState state;
    private int turn;
    private String[] players;
    private String winner;
    private Map<String, PlayerData> data;
    private int seq;

    public Game(String player) {
        // Init game state
        this.state = GameState.WAITING;
        this.turn = 0;
        this.seq = 0;
        this.winner = null;

        // Init 1st player
        this.players = new String[]{player, DEFUALT_PLAYER};

        // Init statistics map
        this.data = new HashMap<String, PlayerData>();
        this.data.put(player, new PlayerData());

    }

    public String[][] getBoard(String user, boolean isOpponent) {
        PlayerData userData = this.data.get(user);
        if (userData == null) {
            return null;
        }

        return userData.getBoard(isOpponent);
    }

    public BattleshipExport[] getBattleship(String user) {
        PlayerData userData = this.data.get(user);
        if (userData == null) {
            return null;
        }

        return userData.getBattleship();
    }

    public GameState getState() {
        return this.state;
    }

    public boolean isEnded() {
        return this.state == GameState.END;
    }

    public String getTurn() {
        return this.players[this.turn];
    }

    public String getWinner() {
        return this.winner;
    }

    public String[] getPlayers() {
        return this.players;
    }

    public int getSeq() {
        return this.seq;
    }

    public Statistics getStats(String user) {
        PlayerData data = this.data.get(user);
        if (data == null) {
            return null;
        }

        return data.getStats();
    }

    public String getOpponent(String user) {
        for (int i = 0; i < this.players.length; i++) {
            if (!this.players[i].equals(user)) {
                return this.players[i];
            }
        }

        // Shouldn't get here
        return null;
    }

    public String join(String user) {
        if (this.state != GameState.WAITING) {
            return "Game Is Already Full";
        }

        if (this.isPlayer(user)) {
            return "User is already a part of the game";
        }

        // Init 2nd player
        this.players[1] = user;
        this.data.put(user, new PlayerData());
        this.state = GameState.PLACEMENT;

        return null;
    }

    public String place(String user, ShipPlacement[] placements) {
        if (this.state != GameState.PLACEMENT) {
            return "Game is not in placement mode";
        }

        PlayerData data = this.data.get(user);
        if (data == null) {
            return String.format("User %s doesn't participate in this game", user);
        }

        String err = data.place(placements);
        if (err != null) {
            return err;
        }

        PlayerData oppData = this.data.get(this.getOpponent(user));
        if (oppData.isPlaced()) {
            this.state = GameState.PLAY;
        }

        return null;
    }

    // Returns True for successful hit and false for miss
    public PlayResult play(String user, int row, int col) {
        if (this.state != GameState.PLAY) {
            return PlayResult.NO_PLAY;
        }

        if (!this.players[this.turn].equals(user)) {
            return PlayResult.NO_TURN;
        }

        String opponent = this.getOpponent(user);
        PlayerData opponentData = this.data.get(opponent);
        PlayResult res = opponentData.play(row, col);
        if (res == PlayResult.MISS) {
            this.turn = Math.abs(this.turn - 1); // 1 -> 0, 0 -> 1
        }

        if (res == PlayResult.SINK) {
            if (checkWin(opponent)) {
                this.state = GameState.END;
                this.winner = user;
                // TODO(Orr) - Handle stats updates
            }
        }

        this.seq++;
        return res;
    }

    public boolean isPlayer(String user) {
        for (int i = 0; i < this.players.length; i++) {
            if (user.equals(this.players[i])) {
                return true;
            }
        }

        return false;
    }

    private boolean checkWin(String opponent) {
        PlayerData oppData = this.data.get(opponent);
        return oppData.isAllSunk();
    }
}

