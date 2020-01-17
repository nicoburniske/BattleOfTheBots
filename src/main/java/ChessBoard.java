import piece.*;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private IPiece[][] board;
    private List<IPiece> whitePieces;
    private List<IPiece> blackPieces;
    private boolean whiteTurn;
    private List<IPiece[][]> history;

    public ChessBoard() {
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.board = generateChessBoard();
        this.whiteTurn = true;
        this.history = new ArrayList<>();
    }

    public ChessBoard(IPiece[][] board, boolean whiteTurn) {
        if (board.length != 8 || board[0].length != 8) {
            throw new IllegalArgumentException("Board is not regulation size");
        }
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.board = board;
        this.whiteTurn = whiteTurn;
        this.history = new ArrayList<>();
    }

    public boolean playGame(int fromX, int fromY, int toX, int toY) {
        if (this.isValidMove(fromX, fromY, toX, toY)) {
            IPiece tempFrom = this.board[fromX][fromY];
            this.board[toX][toY] = tempFrom;
        }
        return true;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
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
        System.out.println(this.board[0][5].getClass() + " " + this.board[0][5].getIsBlack());
        return sb.toString();
    }

    // Private Methods
    private IPiece[][] generateChessBoard() {
        IPiece[][] newBoard = new IPiece[8][8];
        IPiece curr;
        // starting from lower left corner (white side)
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // empty slots
                if (row > 1 && row < 6) newBoard[row][col] = null;

                // pawns
                if (row == 1) newBoard[row][col] = this.addPiece(new Pawn(true));
                else if (row == 6) newBoard[row][col] = this.addPiece(new Pawn(false));

                    // white pieces
                else if ((row == 0) && (col == 0 || col == 7)) newBoard[row][col] = this.addPiece(new Castle(true));
                else if ((row == 0) && (col == 1 || col == 6)) newBoard[row][col] = this.addPiece(new Horse(true));
                else if ((row == 0) && (col == 2 || col == 5)) newBoard[row][col] = this.addPiece(new Bishop(true));
                else if (row == 0 && col == 3) newBoard[row][col] = this.addPiece(new Queen(true));
                else if (row == 0 && col == 4) newBoard[row][col] = this.addPiece(new King(true));

                    // black pieces
                else if ((row == 7) && (col == 0 || col == 7)) newBoard[row][col] = this.addPiece(new Castle(false));
                else if ((row == 7) && (col == 1 || col == 6)) newBoard[row][col] = this.addPiece(new Horse(false));
                else if ((row == 7) && (col == 2 || col == 5)) newBoard[row][col] = this.addPiece(new Bishop(false));
                else if (row == 7 && col == 3) newBoard[row][col] = this.addPiece(new Queen(false));
                else if (row == 7 && col == 4) newBoard[row][col] = this.addPiece(new King(false));
            }
        }
        System.out.println(newBoard[0][2] + " " + newBoard[0][2].getIsBlack());
        return newBoard;
    }

    private IPiece addPiece(IPiece piece) {
        if (piece == null) throw new IllegalArgumentException("Piece cannot be null");
        (piece.getIsBlack() ? this.blackPieces : this.whitePieces).add(piece);
        return piece;
    }

    private boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        IPiece tempFrom = this.board[fromX][fromY];
        IPiece tempTo = this.board[toX][toY];
        if (!coordInsideBoard(fromX, fromY) || !coordInsideBoard(toX, toY)) {
            throw new IllegalArgumentException("Coordinate outside of board");
        } else if (tempFrom == null) {
            throw new IllegalArgumentException("Must move a piece");
        } else if (tempFrom.getIsBlack() && this.whiteTurn || !tempFrom.getIsBlack() && !this.whiteTurn) {
            throw new IllegalArgumentException("Other player's move");
        } else if (tempFrom.getIsBlack() && tempTo.getIsBlack() || !(tempFrom.getIsBlack() || tempTo.getIsBlack())) {
            throw new IllegalArgumentException("Cannot move to square occupied by piece of same color");
        } else if (!tempFrom.movePiece(this.board, fromX, fromY, toX, toY)) {
            throw new IllegalArgumentException("Invalid move");
        }
        return true;
    }

    private boolean coordInsideBoard(int x, int y) {
        return (x > -1 && x < 8) && (y > -1 && y < 8);
    }
}














