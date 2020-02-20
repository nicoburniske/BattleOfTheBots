package com.burnyarosh.board.piece;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends AbstractPiece {

    public King(int x, int y, boolean isBlack) {
        super(x, y, isBlack);
    }

    private King(int x, int y, boolean isBlack, boolean isFirstMove, int moveCount) {
        super(x, y, isBlack, isFirstMove, moveCount);
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */

    /**
     * Checks if the move is a valid King move
     * @param board - current IPiece[][] board
     * @param origin - origin coordinates
     * @param target - target coordinates
     * @return - true if valid move, false otherwise
     */
    public boolean isValidMove(IPiece[][] board, Coord origin, Coord target) {
        if (!super.getIsFirstMove()) {
            return isValidKingMove(board, origin, target);
        } else {
            return isCastlingValid(board, target)
                    || this.isValidKingMove(board, origin, target);
        }
    }

    /**
     * Returns a list of all possible moves for this instance of King (including moves that danger King)
     * @param board - current IPiece[][] board
     * @param move_history - previous moves during current game
     * @return - List<Coord> where Coord is target
     */
    @Override
    public List<Coord> getPossibleMoves(IPiece[][] board, List<Move> move_history) {
        Coord[] skeleton = {
                new Coord(0, 1), new Coord(1, 1),
                new Coord(1, 0), new Coord(1, -1),
                new Coord(0, -1), new Coord(-1, -1),
                new Coord(-1, 0), new Coord(-1, 1)
        };
        List<Coord> moves = new ArrayList<>();
        for (Coord c : skeleton){
            addValidMove(board, c.addCoords(super.getCoord()), moves);
        }
        return moves;
    }

    /**
     * Creates a copy of this piece
     * @return - copy of current instance of King
     */
    public IPiece copy() {
        return new King(super.getCoord().getX(), super.getCoord().getY(), super.getIsBlack(), super.getIsFirstMove(), super.getMoveCount());
    }

    /**
     * King's toString method
     * @return - "WK" if King is white, "BK" if King is black
     */
    public String toString() {
        return super.toString() + "K";
    }

    /**
     * Piece cannot promote --> return copy of itself
     * @return - copy of self
     */
    @Override
    public IPiece promote(boolean toKnight) {
        return this.copy();
    }

    /*
        ################################
            PRIVATE METHODS
        ################################
     */

    /**
     * Checks if the move from the given coordinates is a valid King move
     * @param board - current IPiece[][] board
     * @param origin - origin coordinate
     * @param target - target coordinate
     * @return - true if valid king move, false otherwise
     */
    private boolean isValidKingMove(IPiece[][] board, Coord origin, Coord target) {
        return (super.validDiagonalMove(origin, target)
                || super.validInlineMove(origin, target))
                && super.validLineMove(board, origin, target, 1);
    }

    /**
     * Checks if the given move is a valid Castle move.
     * @param board - current IPiece[][] board
     * @param target - target coordinate
     * @return - true if valid castle, false otherwise
     */
    private boolean isCastlingValid(IPiece[][] board, Coord target) {
        int direction = target.getX() - super.getCoord().getX();
        IPiece castle;
        if ((Math.abs(direction) == 2) && (super.getCoord().getY() == target.getY())) { //  checks if king is attempting to move 2 places to either side.
            castle = board[direction < 0 ? super.getCoord().getX() - 4 : super.getCoord().getX() + 3][target.getY()];
            if (castle == null) {
                return false;
            } else if (castle instanceof Rook && castle.getIsFirstMove()) { //  checks if chosen rook has previously moved
                return super.notObstructed(board, super.getCoord(), castle.getCoord()); //  checks if there are no pieces between the king and the chosen rook
            }
        }
        return false;
    }

}
