import java.util.*;

public class aiPlayer {

    public int depth_level;
    public gameBoard gameBoardShallow;

    public aiPlayer(int depth, gameBoard currentGame) {
        this.depth_level = depth;
        this.gameBoardShallow = currentGame;
    }

    public int findBestPlay(gameBoard currentGame) throws CloneNotSupportedException {
        int playChoice = maxconnect4.INVALID;
        if (currentGame.getCurrentTurn() == maxconnect4.ONE) {
            int v = Integer.MAX_VALUE;
            for (int i = 0; i < gameBoard.NOF_COLS; i++) {
                if (currentGame.isValidPlay(i)) {
                    gameBoard nextMoveBoard = new gameBoard(currentGame.getgameBoard());
                    nextMoveBoard.playPiece(i);
                    int value = Calculate_Max_Utility(nextMoveBoard, depth_level, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (v > value) {
                        playChoice = i;
                        v = value;
                    }
                }
            }
        } else {
            int v = Integer.MIN_VALUE;
            for (int i = 0; i < gameBoard.NOF_COLS; i++) {
                if (currentGame.isValidPlay(i)) {
                    gameBoard nextMoveBoard = new gameBoard(currentGame.getgameBoard());
                    nextMoveBoard.playPiece(i);
                    int value = Calculate_Min_Utility(nextMoveBoard, depth_level, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (v < value) {
                        playChoice = i;
                        v = value;
                    }
                }
            }
        }
        return playChoice;
    }

    private int Calculate_Min_Utility(gameBoard gameBoard, int depth_level, int alpha_value, int beta_value)
            throws CloneNotSupportedException {

        if (!gameBoard.isBoardFull() && depth_level > 0) {
            int v = Integer.MAX_VALUE;
            for (int i = 0; i < gameBoard.NOF_COLS; i++) {
                if (gameBoard.isValidPlay(i)) {
                    gameBoard board4NextMove = new gameBoard(gameBoard.getgameBoard());
                    board4NextMove.playPiece(i);
                    int value = Calculate_Max_Utility(board4NextMove, depth_level - 1, alpha_value, beta_value);
                    if (v > value) {
                        v = value;
                    }
                    if (v <= alpha_value) {
                        return v;
                    }
                    if (beta_value > v) {
                        beta_value = v;
                    }
                }
            }
            return v;
        } else {
            // this is a terminal state
            return gameBoard.getScore(maxconnect4.TWO) - gameBoard.getScore(maxconnect4.ONE);
        }
    }

    private int Calculate_Max_Utility(gameBoard gameBoard, int depth_level, int alpha_value, int beta_value)
            throws CloneNotSupportedException {

        if (!gameBoard.isBoardFull() && depth_level > 0) {
            int v = Integer.MIN_VALUE;
            for (int i = 0; i < gameBoard.NOF_COLS; i++) {
                if (gameBoard.isValidPlay(i)) {
                    gameBoard board4NextMove = new gameBoard(gameBoard.getgameBoard());
                    board4NextMove.playPiece(i);
                    int value = Calculate_Min_Utility(board4NextMove, depth_level - 1, alpha_value, beta_value);
                    if (v < value) {
                        v = value;
                    }
                    if (v >= beta_value) {
                        return v;
                    }
                    if (alpha_value < v) {
                        alpha_value = v;
                    }
                }
            }
            return v;
        } else {
            // this is a terminal state
            return gameBoard.getScore(maxconnect4.TWO) - gameBoard.getScore(maxconnect4.ONE);
        }
    }
}