import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class gameBoard implements Cloneable {
    // class fields
    private int[][] playBoard;
    private int pieceCount;
    private int currentTurn;
    private maxconnect4.PLAYER_TYPE first_turn;
    private maxconnect4.MODE game_mode;

    public static final int NOF_COLS = 7;
    public static final int NOF_ROWS = 6;
    public static final int MAX_PIECE_COUNT = 42;

    public gameBoard(String inputFile) {
        this.playBoard = new int[NOF_ROWS][NOF_COLS];
        this.pieceCount = 0;
        int counter = 0;
        BufferedReader input = null;
        String gameData = null;

        // open the input file
        try {
            input = new BufferedReader(new FileReader(inputFile));
        } catch (IOException e) {
            System.out.println("\nProblem opening the input file!\nTry again." + "\n");
            e.printStackTrace();
        }

        // read the game data from the input file
        for (int i = 0; i < NOF_ROWS; i++) {
            try {
                gameData = input.readLine();

                // read each piece from the input file
                for (int j = 0; j < NOF_COLS; j++) {

                    this.playBoard[i][j] = gameData.charAt(counter++) - 48;

                    // sanity check
                    if (!((this.playBoard[i][j] == 0) || (this.playBoard[i][j] == maxconnect4.ONE)
                            || (this.playBoard[i][j] == maxconnect4.TWO))) {
                        System.out.println(
                                "\nProblems!\n--The piece read " + "from the input file was not a 1, a 2 or a 0");
                        this.exit_function(0);
                    }

                    if (this.playBoard[i][j] > 0) {
                        this.pieceCount++;
                    }
                }
            } catch (Exception e) {
                System.out.println("\nProblem reading the input file!\n" + "Try again.\n");
                e.printStackTrace();
                this.exit_function(0);
            }

            // reset the counter
            counter = 0;

        } // end for loop

        // read one more line to get the next players turn
        try {
            gameData = input.readLine();
        } catch (Exception e) {
            System.out.println("\nProblem reading the next turn!\n" + "--Try again.\n");
            e.printStackTrace();
        }

        this.currentTurn = gameData.charAt(0) - 48;

        // make sure the turn corresponds to the number of pcs played already
        if (!((this.currentTurn == maxconnect4.ONE) || (this.currentTurn == maxconnect4.TWO))) {
            System.out.println("Problems!\n The current turn read is not a " + "1 or a 2!");
            this.exit_function(0);
        } else if (this.getCurrentTurn() != this.currentTurn) {
            System.out.println(
                    "Problems!\n the current turn read does not " + "correspond to the number of pieces played!");
            this.exit_function(0);
        }
    } // end gameBoard( String )

    /**
     * This method set piece value for both human & computer(1 or 2).
     * 
     */
    public void setPieceValue() {
        if ((this.currentTurn == maxconnect4.ONE && first_turn == maxconnect4.PLAYER_TYPE.COMPUTER)
                || (this.currentTurn == maxconnect4.TWO && first_turn == maxconnect4.PLAYER_TYPE.HUMAN)) {
            maxconnect4.COMPUTER_PIECE = maxconnect4.ONE;
            maxconnect4.HUMAN_PIECE = maxconnect4.TWO;
        } else {
            maxconnect4.HUMAN_PIECE = maxconnect4.ONE;
            maxconnect4.COMPUTER_PIECE = maxconnect4.TWO;
        }

        System.out.println(
                "Human plays as : " + maxconnect4.HUMAN_PIECE + " , Computer plays as : " + maxconnect4.COMPUTER_PIECE);

    }

    /**
     * This mathod creats clone (Shallow copy) for object of gameBoard class. (It's
     * recommended to use clone() method instead of copy constructor as it's process
     * faster)
     * 
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public gameBoard(int masterGame[][]) {

        this.playBoard = new int[NOF_ROWS][NOF_COLS];
        this.pieceCount = 0;

        for (int i = 0; i < NOF_ROWS; i++) {
            for (int j = 0; j < NOF_COLS; j++) {
                this.playBoard[i][j] = masterGame[i][j];

                if (this.playBoard[i][j] > 0) {
                    this.pieceCount++;
                }
            }
        }
    } // end gameBoard( int[][] )

    public int getScore(int player) {
        // reset the scores
        int playerScore = 0;

        // check horizontally
        for (int i = 0; i < NOF_ROWS; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i][j + 1] == player)
                        && (this.playBoard[i][j + 2] == player) && (this.playBoard[i][j + 3] == player)) {
                    playerScore++;
                }
            }
        } // end horizontal

        // check vertically
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < NOF_COLS; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j] == player)
                        && (this.playBoard[i + 2][j] == player) && (this.playBoard[i + 3][j] == player)) {
                    playerScore++;
                }
            }
        } // end verticle

        // check diagonally - backs lash -> \
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j + 1] == player)
                        && (this.playBoard[i + 2][j + 2] == player) && (this.playBoard[i + 3][j + 3] == player)) {
                    playerScore++;
                }
            }
        }

        // check diagonally - forward slash -> /
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i + 3][j] == player) && (this.playBoard[i + 2][j + 1] == player)
                        && (this.playBoard[i + 1][j + 2] == player) && (this.playBoard[i][j + 3] == player)) {
                    playerScore++;
                }
            }
        } // end player score check

        return playerScore;
    } // end getScore

    public int getUnBlockedThrees(int player) {
        // reset the scores
        int playerScore = 0;

        // check horizontally
        for (int i = 0; i < NOF_ROWS; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i][j + 1] == player)
                        && (this.playBoard[i][j + 2] == player)
                        && (this.playBoard[i][j + 3] == player || this.playBoard[i][j + 3] == 0)) {
                    playerScore++;
                }
            }
        } // end horizontal

        // check vertically
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < NOF_COLS; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j] == player)
                        && (this.playBoard[i + 2][j] == player)
                        && (this.playBoard[i + 3][j] == player || this.playBoard[i + 3][j] == 0)) {
                    playerScore++;
                }
            }
        } // end verticle

        // check diagonally - backs lash -> \
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j + 1] == player)
                        && (this.playBoard[i + 2][j + 2] == player)
                        && (this.playBoard[i + 3][j + 3] == player || this.playBoard[i + 3][j + 3] == 0)) {
                    playerScore++;
                }
            }
        }

        // check diagonally - forward slash -> /
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i + 3][j] == player) && (this.playBoard[i + 2][j + 1] == player)
                        && (this.playBoard[i + 1][j + 2] == player)
                        && (this.playBoard[i][j + 3] == player || this.playBoard[i][j + 3] == 0)) {
                    playerScore++;
                }
            }
        } // end player score check

        return playerScore;
    }

    public int getCurrentTurn() {
        return (this.pieceCount % 2) + 1;
    } // end getCurrentTurn

    public int getPieceCount() {
        return this.pieceCount;
    }

    public int[][] getgameBoard() {
        return this.playBoard;
    }

    public boolean isValidPlay(int column) {

        if (!(column >= 0 && column < 7)) {
            // check the column bounds
            return false;
        } else if (this.playBoard[0][column] > 0) {
            // check if column is full
            return false;
        } else {
            // column is NOT full and the column is within bounds
            return true;
        }
    }

    boolean isBoardFull() {
        return (this.getPieceCount() >= gameBoard.MAX_PIECE_COUNT);
    }

    public boolean playPiece(int column) {

        // check if the column choice is a valid play
        if (!this.isValidPlay(column)) {
            return false;
        } else {

            // starting at the bottom of the board,
            // place the piece into the first empty spot
            for (int i = 5; i >= 0; i--) {
                if (this.playBoard[i][column] == 0) {
                    this.playBoard[i][column] = getCurrentTurn();
                    this.pieceCount++;
                    return true;
                }
            }
            // the pgm shouldn't get here
            System.out.println("Something went wrong with playPiece()");

            return false;
        }
    } // end playPiece

    /*************************** solution methods **************************/

    public void removePiece(int column) {

        // starting looking at the top of the game board,
        // and remove the top piece
        for (int i = 0; i < NOF_ROWS; i++) {
            if (this.playBoard[i][column] > 0) {
                this.playBoard[i][column] = 0;
                this.pieceCount--;

                break;
            }
        }
    } // end remove piece

    /************************ end solution methods **************************/

    public void printgameBoard() {
        System.out.println(" -----------------");

        for (int i = 0; i < NOF_ROWS; i++) {
            System.out.print(" | ");
            for (int j = 0; j < NOF_COLS; j++) {
                System.out.print(this.playBoard[i][j] + " ");
            }

            System.out.println("| ");
        }

        System.out.println(" -----------------");
    } // end printgameBoard

    public void printgameBoardToFile(String outputFile) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    output.write(this.playBoard[i][j] + 48);
                }
                output.write("\r\n");
            }

            // write the current turn
            output.write(this.getCurrentTurn() + "\r\n");
            output.close();

        } catch (IOException e) {
            System.out.println("\nProblem writing to the output file!\n" + "Try again.");
            e.printStackTrace();
        }
    } // end printgameBoardToFile()

    private void exit_function(int value) {
        System.out.println("exiting from gameBoard.java!\n\n");
        System.exit(value);
    }

    // set first turn
    public void setFirstTurn(maxconnect4.PLAYER_TYPE turn) {

        first_turn = turn;
        setPieceValue();
    }

    public maxconnect4.PLAYER_TYPE getFirstTurn() {

        return first_turn;
    }

    // set game mode (interactive or one-move)
    public void setGameMode(maxconnect4.MODE mode) {

        game_mode = mode;
    }

    public maxconnect4.MODE getGameMode() {

        return game_mode;
    }

}