package com.burnyarosh.board.common;

import com.burnyarosh.board.Board;
import com.burnyarosh.board.Chess;
import com.burnyarosh.board.piece.*;

import java.util.List;

public final class NewMove {

    private final IPiece p;
    private final Coord origin;
    private final Coord target;
    private final String an;
    private final Type t;

    //	flags
    private final boolean isCapture;
    private final boolean isCheck;
    private final boolean isMate;

    enum Type{
        STANDARD,
        CASTLE,
        EN_PASSANT,
        PROMOTION
    }

    public NewMove(Board b, Coord origin, Coord target){

        this.p = b.getPieceAtCoord(origin);
        this.target = target;
        this.origin = origin;
        this.classifyMove(b);
        // toAlgebraic notation
    }

    private void classifyMove(Board b){
        this.isCapture = b.getPieceAtCoord(target) != null && this.p.getIsBlack() != b.getPieceAtCoord(target).getIsBlack();

        //	MOVE TYPE CLASSIFIER
        if (b.getPieceAtCoord(origin) instanceof King && Math.abs(origin.getX() - target.getX()) == 2 && !Chess.isInDangerBetween(b, (b.getPieceAtCoord(origin).getIsBlack() ? Chess.Color.BLACK : Chess.Color.WHITE), origin, target)) {
            int direction = target.getX() - origin.getX();
            int fromCastleX = direction > 0 ? 7 : 0;
            int toCastleX = direction > 0 ? 5 : 3;
            if (Chess.isValidMoveBoolean(b, (b.getPieceAtCoord(origin).getIsBlack() ? Chess.Color.BLACK : Chess.Color.WHITE), new Coord(fromCastleX, target.getY()), new Coord(toCastleX, target.getY()))) {
                this.t = Type.CASTLE;
            }
        } else if (b.getPieceAtCoord(origin) instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && b.getPieceAtCoord(origin) == null){
            List<Move> temp_history = b.getMoveHistory();
            if (origin.getY() == (b.getPieceAtCoord(origin).getIsBlack() ? 3 : 4) //  Condition #1
                    && temp_history.size() > 3  //  avoid OutOfBoundsException  (en passant impossible under 4 moves)
                    && temp_history.get(temp_history.size() - 2).getPiece() instanceof Pawn && (b.getPieceAtCoord(new Coord(target.getX(), origin.getY()))).getMoveCount() == 1){ //Conditions 2, 3, and 4
                this.t = Type.EN_PASSANT;
            }
        } else if (b.getPieceAtCoord(origin) instanceof Pawn && target.getY() == (b.getPieceAtCoord(origin).getIsBlack() ? 0 : 7)){
            this.t = Type.PROMOTION;
        } else {
            this.t = Type.STANDARD;
        }
    }
}
