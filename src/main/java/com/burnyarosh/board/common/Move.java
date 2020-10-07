package com.burnyarosh.board.common;

import com.burnyarosh.board.Board;
import com.burnyarosh.board.Chess;
import com.burnyarosh.board.piece.*;
import java.util.List;

/**
 *
 */
public final class Move {

    private final IPiece p;
    private final Chess.PlayerColor c;
    private final Coord origin;
    private final Coord target;
    private final String an;
    private final Type t;
    private final boolean isCapture;
    private final boolean isCheck;
    private final boolean isMate;

    /**
     *
     */
    public enum Type{
        STANDARD,
        CASTLE,
        EN_PASSANT,
        PROMOTION
    }

    public Move(Board b, Coord origin, Coord target) {
       this(b, origin, target, 'Q');
    }

    /**
     *
     * @param b
     * @param origin
     * @param target
     * @param promotion
     */
    public Move(Board b, Coord origin, Coord target, char promotion){
        this.p = b.getBoardArray()[origin.getX()][origin.getY()];
        this.c =  (this.p.getIsBlack() ? Chess.PlayerColor.BLACK : Chess.PlayerColor.WHITE);
        this.target = target;
        this.origin = origin;
        this.t = classifyMove(b, origin, target);
        this.isCapture = (b.getBoardArray()[target.getX()][target.getY()] != null) && (this.p.getIsBlack() != b.getBoardArray()[target.getX()][target.getY()].getIsBlack());
        Board temp = Board.tryMove(b, origin, target);
        this.isCheck = Chess.isInCheck( temp, this.c.other());
        this.isMate = Chess.isMate( temp, this.c.other());
        this.an = toAlgebraicNotation(b, promotion);
    }

    public Coord getTarget() {
        return this.target;
    }

    public Coord getOrigin() {
        return this.origin;
    }
    /**
     *
     * @return
     */
    public Type getType(){
        return this.t;
    }

    /**
     *
     * @return
     */
    public IPiece getPiece(){
        return this.p;
    }

    /**
     *
     * @return
     */
    public String toString(){
        return this.an;
    }

    /**
     *
     * @param b
     * @param origin
     * @param target
     * @return
     */
    public static Type classifyMove(Board b, Coord origin, Coord target){
        if (b.getBoardArray()[origin.getX()][origin.getY()] instanceof King && Math.abs(origin.getX() - target.getX()) == 2 && !Chess.isInDangerBetween(b, (b.getBoardArray()[origin.getX()][origin.getY()].getIsBlack() ? Chess.PlayerColor.BLACK : Chess.PlayerColor.WHITE), origin, target)) {
            int direction = target.getX() - origin.getX();
            int fromCastleX = direction > 0 ? 7 : 0;
            int toCastleX = direction > 0 ? 5 : 3;
            if (Chess.isValidMoveBoolean(b, (b.getBoardArray()[origin.getX()][origin.getY()].getIsBlack() ? Chess.PlayerColor.BLACK : Chess.PlayerColor.WHITE), new Coord(fromCastleX, target.getY()), new Coord(toCastleX, target.getY()))) {
                return Type.CASTLE;
            }
        } else if (b.getBoardArray()[origin.getX()][origin.getY()] instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && b.getBoardArray()[target.getX()][target.getY()] == null){
            List<Move> temp_history = b.getMoveHistory();
            if (origin.getY() == (b.getBoardArray()[origin.getX()][origin.getY()].getIsBlack() ? 3 : 4) //  Condition #1
                    && temp_history.size() > 3  //  avoid OutOfBoundsException  (en passant impossible under 4 moves)
                    && temp_history.get(temp_history.size() - 2).getPiece() instanceof Pawn && b.getBoardArray()[target.getX()][origin.getY()].getMoveCount() == 1){ //Conditions 2, 3, and 4
                return Type.EN_PASSANT;
            }
        } else if (b.getBoardArray()[origin.getX()][origin.getY()] instanceof Pawn && target.getY() == (b.getBoardArray()[origin.getX()][origin.getY()].getIsBlack() ? 0 : 7)){
            return Type.PROMOTION;
        }
        return Type.STANDARD;
    }

    /**
     *
     * @param b
     * @param promo
     * @return
     */
    private String toAlgebraicNotation(Board b, char promo){
        //  SPECIAL CONDITION: CASTLE
        if (this.t == Type.CASTLE){
            if (target.getX() > origin.getX()){
                return "O-O";
            }
            return "O-O-O";
        }
        StringBuilder sb = new StringBuilder();
        if (!(this.p instanceof Pawn)){
            //  initial piece identifier
            sb.append(this.p.toString().charAt(1));
        }
        {   //  DISAMBIGUATING MOVES
            if (!((this.p instanceof Pawn) || (this.p instanceof King))){
                boolean requiresDisambiguation = false;
                boolean sameFile = false;
                boolean sameRank = false;
                for (IPiece t : b.getPieces(this.c)){
                    if (t.toString().charAt(1) == this.p.toString().charAt(1) && !t.getCoord().equals(this.p.getCoord())){
                        if (t.getPossibleMoves(b.getBoardArray(), b.getMoveHistory()).contains(this.target)){
                            requiresDisambiguation = true;
                            if (t.getCoord().getX() == this.origin.getX()){
                                sameFile = true;
                            }
                            if (t.getCoord().getY() == this.origin.getY()){
                                sameRank = true;
                            }
                        }
                    }
                }
                if (requiresDisambiguation){
                    if (!sameFile){
                        sb.append((char) (97 + this.origin.getX()));
                    } else if (!sameRank){
                        sb.append(this.origin.getY()+1);
                    } else {
                        sb.append(this.origin.toChessString());
                    }
                }
            }
        }   //  END OF DISAMBIGUATING MOVES
        if (this.isCapture || this.t == Type.EN_PASSANT){
            if (this.p instanceof Pawn){
                sb.append((char) (97 + this.origin.getX()));
            }
            sb.append("x");
        }
        {   //  DESTINATION COORDINATES
            sb.append(this.target.toChessString());
        }   //  END OF DESTINATION COORDINATES
        if (this.t == Type.PROMOTION){
            sb.append("=");
            sb.append(promo);
        }
        if (this.isCheck){
            if (this.isMate){
                sb.append("#");
            } else {
                sb.append("+");
            }
        }
        return sb.toString();
    }

}