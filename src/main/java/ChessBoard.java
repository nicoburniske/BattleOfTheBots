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
        for (int i = 7; i > -1; i--) {
            for (int j = 7; j > -1; j--) {
                IPiece current = board[i][j];
                if (current == null)
                    sb.append("X ");
                else {
                    sb.append(current.getIsBlack() ? "B" : "W");
                }
                if (current instanceof King) {
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

    // Private Methods
    private IPiece[][] generateChessBoard() {
        IPiece[][] newBoard = {
                {new Castle(false), new Knight(false), new Bishop(false), new Queen(false), new King(false), new Bishop(false), new Knight(false), new Castle(false)},
                {new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false)},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true)},
                {new Castle(true), new Knight(true), new Bishop(true), new Queen(true), new King(true), new Bishop(true), new Knight(true), new Castle(true)},
        };
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
        } else if (fromX == toX && fromY == toY) {
            throw new IllegalArgumentException("Cannot move to same space");
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














