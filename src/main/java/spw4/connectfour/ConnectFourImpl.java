package spw4.connectfour;

public class ConnectFourImpl implements ConnectFour {

    private Player[][] board;
    private Player playerOnTurn;
    private Player winner;
    private int freeFields = 42;

    public ConnectFourImpl(Player playerOnTurn) {
        this.playerOnTurn = playerOnTurn;
        this.winner = Player.none;
        this.board = new Player[6][7]; // 7 columns, 6 rows
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                this.board[row][col] = Player.none;
            }
        }
    }

    public Player getPlayerAt(int row, int col) {
        return this.board[row][col];
    }

    public Player getPlayerOnTurn() {
        return this.playerOnTurn;
    }

    public boolean isGameOver() {
        return freeFields == 0 || winner != Player.none;
    }

    public Player getWinner() {
        return winner;
    }

    private void checkWinner() {
        // Check rows
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row][col + 1] &&
                        board[row][col] == board[row][col + 2] &&
                        board[row][col] == board[row][col + 3]) {
                    winner = board[row][col];
                    return;
                }
            }
        }

        // Check columns
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row + 1][col] &&
                        board[row][col] == board[row + 2][col] &&
                        board[row][col] == board[row + 3][col]) {
                    winner = board[row][col];
                    return;
                }
            }
        }

        // Check diagonals (bottom-left to top-right)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row + 1][col + 1] &&
                        board[row][col] == board[row + 2][col + 2] &&
                        board[row][col] == board[row + 3][col + 3]) {
                    winner = board[row][col];
                    return;
                }
            }
        }

        // Check diagonals (top-left to bottom-right)
        for (int row = 3; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                if (board[row][col] != Player.none &&
                        board[row][col] == board[row - 1][col + 1] &&
                        board[row][col] == board[row - 2][col + 2] &&
                        board[row][col] == board[row - 3][col + 3]) {
                    winner = board[row][col];
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player: ").append(playerOnTurn.toString().toUpperCase()).append("\n");
        for (int row = 5; row >= 0; row--) {
            sb.append("|");
            for (int col = 0; col < 7; col++) {
                sb.append(" ");
                sb.append(board[row][col] == Player.none ? "." : board[row][col].toString().toUpperCase().charAt(0));
                sb.append(" ");
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public void reset(Player playerOnTurn) {
        this.playerOnTurn = playerOnTurn;
        this.winner = Player.none;
        this.freeFields = 42;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                this.board[row][col] = Player.none;
            }
        }
    }

    public void drop(int col) {
        if (col < 0 || col > 6) {
            System.out.println("Invalid column; Try again");
            return;
        }

        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == Player.none) {
                board[row][col] = playerOnTurn;
                playerOnTurn = (playerOnTurn == Player.red) ? Player.yellow : Player.red;
                freeFields--;
                checkWinner();
                return;
            }
        }

        System.out.println("Column is full; Try again");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ConnectFourImpl other)) {
            return false;
        }
        if (this.playerOnTurn != other.playerOnTurn) {
            return false;
        }
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (this.board[row][col] != other.board[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }
}
