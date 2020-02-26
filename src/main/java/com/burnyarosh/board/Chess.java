package com.burnyarosh.board;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.piece.*;
import io.vertx.core.json.JsonObject;

import java.util.*;

public class Chess {
    private Board b;
    private Color turn;
    private Map<Class, Double> defaultValues;

    public enum Color{
        WHITE, BLACK
    }

    public Chess(){
        this.b = new Board();
        this.turn = Color.WHITE;
        this.defaultValues = new HashMap<>() {{
            put(King.class, 1000.0);
            put(Queen.class, 9.0);
            put(Rook.class, 5.0);
            put(Bishop.class, 3.0);
            put(Knight.class, 3.0);
            put(Pawn.class, 1.0);
        }};
    }

    public Chess(Board b, Color turn){
        this.b = b.copy();
        this.turn = turn;
        this.defaultValues = new HashMap<>() {{
            put(King.class, 1000.0);
            put(Queen.class, 9.0);
            put(Rook.class, 5.0);
            put(Bishop.class, 3.0);
            put(Knight.class, 3.0);
            put(Pawn.class, 1.0);
        }};
    }

    //  TODO: UNFINISHED
    public boolean play(int originX, int originY, int targetX, int targetY, char promotion){
        if (this.isGameOver()){
            return false;
        } else {
            Coord origin = new Coord(originX, originY);
            Coord target = new Coord(targetX, targetY);
            if (isValidMove(this.b, this.turn, origin, target)){
                this.b.executeMove(origin, target, promotion);
                this.nextTurn();
                //this.updatePreviousMove();
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean play(int originX, int originY, int targetX, int targetY){
        return this.play(originX, originY, targetX, targetY, 'Q');
    }

    public Color getTurn(){
        return this.turn;
    }

    public double getScore(){
        return this.getScore(this.defaultValues);
    }

    public double getScore(Map<Class, Double> values){
        if (!this.isValidPieceValueMap(values)) throw new IllegalArgumentException("Invalid values map");
        double score = 0;
        for (IPiece b : this.b.getPieces(Color.BLACK)) {
            score -= values.get(b.getClass());
        }
        for (IPiece w : this.b.getPieces(Color.WHITE)) {
            score += values.get(w.getClass());
        }
        // limits return to only contain one decimal point.
        return (double) Math.round(score * 100) / 100;
    }

    //  TODO: UNFINISHED
    public String toString(){
        //  TODO: FIGURE OUT WHAT TO PUT HERE
        return null;
    }

    //  TODO: UNFINISHED
    public JsonObject toJson(){
        //  TODO: FIGURE OUT THIS
        return null;
    }

    private boolean isValidPieceValueMap(Map<Class, Double> values){
        if (values == null) return false;
        Set<Class> s = new HashSet<>((Arrays.asList(King.class, Queen.class, Rook.class, Bishop.class, Knight.class, Pawn.class)));
        return values.keySet().containsAll(s);
    }

    private boolean isGameOver(){
        return isMate(this.b, this.turn);
    }

    public static boolean isMate(Board b, Color turn){
        if (isInCheck(b, turn)){
            for (IPiece p : b.getPieces(turn)){
                for (Coord c : p.getPossibleMoves(b.getBoardArray(), b.getMoveHistory())){
                    if (!isInCheck(Board.tryMove(b, p.getCoord(), c), turn)){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /*
    private static boolean tryMove(Board b, Color turn, Coord origin, Coord target){
        Board temp = b.copy();
        temp.executeMove(origin, target, 'Q');  //  TODO: SHOULD I ADD METHOD PARAM FOR PROMOTION KEY
        return !isInCheck(temp, turn);
    }
    */
    //  TODO: PENDING REVIEW - (HAS CURRENT LOGIC, COULD BE IMPROVED)
    private static boolean isValidEnPassant(Board b, Coord origin, Coord target){
        if (b.getPieceAtCoord(origin) instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && b.getPieceAtCoord(target) == null){
            if (origin.getY() == (b.getPieceAtCoord(origin).getIsBlack() ? 3 : 4)){
                if (b.getMoveHistory().size() > 3){        //  TODO: VERRIFY THIS
                    return (b.getMoveHistory().get(b.getMoveHistory().size() - 2).getPiece() instanceof Pawn) && (b.getPieceAtCoord(new Coord(target.getX(), origin.getY())).getMoveCount() == 1);
                }
            }
        }
        return false;
    }

    public static boolean isInCheck(Board b, Color turn){
        for (IPiece p : b.getPieces(turn)){
            if (p.toString().charAt(1) == 'K'){
                return isInDanger(b, turn, p.getCoord());
            }
        }
        return true;
    }

    public static boolean isInDangerBetween(Board b, Color turn, Coord start, Coord end){
        for ( Coord c : start.calculatePointsBetweenInclusiveEnd((end))) {
            if (isInDanger(b, turn, c)) return true;
        }
        return false;
    }

    private static boolean isInDanger(Board b, Color turn, Coord target){
        for (IPiece p : b.getPieces(turn)){
            if (isValidMovePiece(b, p.getCoord(), target)) return true;
        }
        return false;
    }

    private static boolean isValidMovePiece(Board b, Coord origin, Coord target){
        return b.getPieceAtCoord(origin).isValidMove(b.getBoardArray(), origin, target);
    }

    public static boolean isValidMoveBoolean(Board b, Color turn, Coord origin, Coord target){
        try {
            return isValidMove(b, turn, origin, target);
        } catch (Exception e) {
            return false;
        }
    }


    //TODO: if isInCheck and the resulting move does not remove the player from being in check, then it is an invalid move
    // use constructor that takes in a 2d board to make a new chessBoard (feed it this.getBoard(), and this.isWhiteTurn()) to have a copy of the board.
    // perform the move on this board and if the king is still in check (for the player that moved, than the move is invalid.
    private static boolean isValidMove(Board b, Color turn, Coord origin, Coord target){
        if (!origin.isInsideBoard() || !target.isInsideBoard()) {
            throw new IllegalArgumentException("Coordinate outside of board");
        } else {
            IPiece from = b.getPieceAtCoord(origin);
            IPiece to = b.getPieceAtCoord(target);
            if (from == null) {
                throw new IllegalArgumentException("Must move a piece");
            } else if (origin.equals(target)) {
                throw new IllegalArgumentException("Cannot move to same space");
            } else if (from.getIsBlack() && turn == Color.WHITE || !from.getIsBlack() && turn == Color.BLACK) {
                throw new IllegalArgumentException("Other player's move");
            } else if (to != null && (from.getIsBlack() && to.getIsBlack() || !(from.getIsBlack() || to.getIsBlack()))) {
                throw new IllegalArgumentException("Cannot move to square occupied by piece of same color");
            } else if (!from.isValidMove(b.getBoardArray(), origin, target) && !isValidEnPassant(b, origin, target)) {
                throw new IllegalArgumentException("Invalid move");
            } else if (isInCheck(b, turn) && isInCheck(Board.tryMove(b, origin, target), turn)) {
                throw new IllegalStateException("Move results with King in check");
            }
            return true;
        }
    }

    private void nextTurn(){
        if (this.turn == Color.WHITE){
            this.turn = Color.BLACK;
        } else {
            this.turn = Color.WHITE;
        }
    }

}
