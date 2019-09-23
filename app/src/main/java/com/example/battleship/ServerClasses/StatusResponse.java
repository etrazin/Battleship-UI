package com.example.battleship.ServerClasses;

public class StatusResponse {

    private String[][] userBoard, opponentBoard;
    private BattleshipExport[] userBattleship, opponentBattleship;
    private String turn;
    private GameState state;
    private String winner;
    private int seq;

    public StatusResponse(Game game, String user, String opponent) {
        this.userBoard = game.getBoard(user, false);
        this.opponentBoard = game.getBoard(opponent, true);
        this.userBattleship = game.getBattleship(user);
        this.opponentBattleship = game.getBattleship(opponent);
        this.turn = game.getTurn();
        this.state = game.getState();
        this.winner = game.getWinner();
        this.seq = game.getSeq();
    }

    public String[][] getUserBoard() {
        return this.userBoard;
    }

    public String[][] getOpponentBoard() {
        return this.opponentBoard;
    }

    public BattleshipExport[] getUserBattleship() {
        return this.userBattleship;
    }

    public BattleshipExport[] getOpponentBattleship() {
        return this.opponentBattleship;
    }

    public String getTurn() {
        return this.turn;
    }

    public GameState getState() {
        return this.state;
    }

    public String getWinner() {
        return this.winner;
    }

    public int getSeq() {
        return this.seq;
    }

}
