import piece.*;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private IPiece[][] board;
    private List<IPiece> whitePieces;
    private List<IPiece> blackPieces;
    private boolean whiteTurn;
    private List<IPiece[][]> history;
    private int movesSoFar;

    public ChessBoard() {
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.board = generateChessBoard();
        this.whiteTurn = true;
        this.history = new ArrayList<>();
        movesSoFar = 0;
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
        movesSoFar = 0;
    }

    public boolean playGame(int fromX, int fromY, int toX, int toY) {
        if (this.isValidMove(fromX, fromY, toX, toY)) {
            IPiece tempFrom = this.board[fromX][fromY];
            this.board[toX][toY] = tempFrom;
            this.board[fromX][fromY] = null;
            this.nextTurn();
            return true;
        } else {
            return false;
        }
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                IPiece current = board[j][i];
                if (current == null)
                    sb.append("X ");
                else {
                    sb.append(current.toString());
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Private Methods
    private IPiece[][] generateChessBoard() {
        IPiece[][] newBoard =
                {
                        {new Castle(false), new Pawn(false), null, null, null, null, new Pawn(true), new Castle(true)},
                        {new Knight(false), new Pawn(false), null, null, null, null, new Pawn(true), new Knight(true)},
                        {new Bishop(false), new Pawn(false), null, null, null, null, new Pawn(true), new Bishop(true)},
                        {new Queen(false), new Pawn(false), null, null, null, null, new Pawn(true), new Queen(true)},
                        {new King(false), new Pawn(false), null, null, null, null, new Pawn(true), new King(true)},
                        {new Bishop(false), new Pawn(false), null, null, null, null, new Pawn(true), new Bishop(true)},
                        {new Knight(false), new Pawn(false), null, null, null, null, new Pawn(true), new Knight(true)},
                        {new Castle(false), new Pawn(false), null, null, null, null, new Pawn(true), new Castle(true)}
                };
        return newBoard;
    }

    private IPiece addPiece(IPiece piece) {
        if (piece == null) throw new IllegalArgumentException("Piece cannot be null");
        (piece.getIsBlack() ? this.blackPieces : this.whitePieces).add(piece);
        return piece;
    }

    /**
     * Makes sure:
     * - Both coordinates are inside board.
     * - From coordinate contains a piece.
     * - Coordinates are not the same.
     * - It is the correct player's turn.
     * - Not moving to a space occupied by space of same color.
     * - Delegates to individual piece logic.
     *
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */
    private boolean isValidMove(int fromX, int fromY, int toX, int toY) {
        if (!coordInsideBoard(fromX, fromY) || !coordInsideBoard(toX, toY)) {
            throw new IllegalArgumentException("Coordinate outside of board");
        } else {
            IPiece from = this.board[fromX][fromY];
            IPiece to = this.board[toX][toY];
            if (from == null) {
                throw new IllegalArgumentException("Must move a piece");
            } else if (fromX == toX && fromY == toY) {
                throw new IllegalArgumentException("Cannot move to same space");
            } else if (from.getIsBlack() && this.whiteTurn || !from.getIsBlack() && !this.whiteTurn) {
                throw new IllegalArgumentException("Other player's move");
            } else if (to != null && (from.getIsBlack() && to.getIsBlack() || !(from.getIsBlack() || to.getIsBlack()))) {
                throw new IllegalArgumentException("Cannot move to square occupied by piece of same color");
            } else if (!from.movePiece(this.board, fromX, fromY, toX, toY)) {
                throw new IllegalArgumentException("Invalid move");
            }
            return true;
        }
    }

    private boolean coordInsideBoard(int x, int y) {
        return (x > -1 && x < 8) && (y > -1 && y < 8);
    }

    private void nextTurn() {
        this.whiteTurn = !this.whiteTurn;
        this.movesSoFar++;
    }
}














