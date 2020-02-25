package com.burnyarosh.board;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.piece.*;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //  TODO: UNFINISHED
    public Chess(Board b, Color turn){
        // TODO: "Board is not regulation size exception"
        this.b = b;     //  TODO: Board.getBoard() or Board.copy()
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
            if (this.isValidMove(origin, target)){
                //  TODO: CLASSIFY MOVE type
                //this.b.executeMove();
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

    //  TODO: UNFINISHED
    public double getScore(Map<Class, Double> values){
        if (!this.isValidPieceValueMap(values)) throw new IllegalArgumentException("Invalid values map");
        double score = 0;
        // TODO: FIX THE PIECES IN THIS METHOD

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

    //  TODO: UNFINISHED
    private boolean isValidPieceValueMap(Map<Class, Double> values){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isGameOver(){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isCheckmate(){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean tryMove(Coord origin, Coord target){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isValidEnPassant(Coord origin, Coord target){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isInCheck(){
        return false;
    }

    //  TODO: UNFINISHED
    // TODO: change param names
    protected boolean isInDangerBetween(Coord target, Coord origin){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isInDanger(Coord target){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isValidMovePiece(Coord origin, Coord target){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isValidMoveBoolean(Coord origin, Coord target){
        return false;
    }

    //  TODO: UNFINISHED
    private boolean isValidMove(Coord origin, Coord target){
        return false;
    }

    //  TODO: UNFINISHED
    private void nextTurn(){}


}
