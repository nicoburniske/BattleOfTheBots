import piece.*;

public class ChessBoard {
    private IPiece[][] board;

    public ChessBoard() { this.board = generateChessBoard();}

    public ChessBoard(IPiece[][] board) {
        if (board.length != 8 || board[0].length != 8) {
            throw new IllegalArgumentException("Board is not regulation size");
        }
        this.board = board;
    }

    IPiece[][] generateChessBoard() {
        IPiece[][] newBoard = new IPiece[8][8];
        // starting from lower left corner (white side)
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // empty slots
                if (row > 1 && row < 6) newBoard[row][col] = null;

                // pawns
                if (row == 1) newBoard[row][col] = new Pawn(false);
                else if (row == 6) newBoard[row][col] = new Pawn(true);

                    // white pieces
                else if ((row == 0) && (col == 0 || col == 7)) newBoard[row][col] = new Castle(false);
                else if ((row == 0) && (col == 1 || col == 6)) newBoard[row][col] = new Horse(false);
                else if ((row == 0) && (col == 2 || col == 5)) newBoard[row][col] = new Bishop(false);
                else if (row == 0 && col == 3) newBoard[row][col] = new Queen(false);
                else if (row == 0 && col == 4) newBoard[row][col] = new King(false);

                    // black pieces
                else if ((row == 7) && (col == 0 || col == 7)) newBoard[row][col] = new Castle(true);
                else if ((row == 7) && (col == 1 || col == 6)) newBoard[row][col] = new Horse(true);
                else if ((row == 7) && (col == 2 || col == 5)) newBoard[row][col] = new Bishop(true);
                else if (row == 7 && col == 3) newBoard[row][col] = new Queen(true);
                else if (row == 7 && col == 4) newBoard[row][col] = new King(true);
            }
        }
        return newBoard;
    }

    boolean playGame(int fromX, int fromY, int toX, int toY) {

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                IPiece current = board[i][j];
                if (current == null)
                    sb.append("X ");
                else {
                    sb.append(current.getIsBlack() ? "B" : "W");
                }
                if (current instanceof Horse) {
                    sb.append("H");
                } else if (current instanceof King) {
                    sb.append("K");
                } else if (current instanceof Knight) {
                    sb.append("N");
                } else if (current instanceof Pawn) {
                    sb.append("P");
                } else if (current instanceof Castle) {
                    sb.append("C");
                } else if (current instanceof Bishop) {
                    sb.append("B");
                } else if (current instanceof Queen) {
                    sb.append("Q");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}














