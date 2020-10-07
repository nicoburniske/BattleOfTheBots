package com.burnyarosh.board;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.piece.*;
import io.vertx.core.json.JsonObject;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  Object representing a Chess game
 * @author - Peter Yarosh
 * @author - Nick Burniske
 * @version - incomplete
 */
public class Chess {
    private Board board;
    private PlayerColor turn;
    private Map<Class, Double> defaultValues;

    public enum PlayerColor {
        WHITE, BLACK;

        public PlayerColor other(){
            if (this.equals(WHITE)){
                return BLACK;
            }
            return WHITE;
        }
    }

    /**
     *
     */
    public Chess(){
        this.board = new Board();
        this.turn = PlayerColor.WHITE;
        this.defaultValues = new HashMap<>() {{
            put(King.class, 1000.0);
            put(Queen.class, 9.0);
            put(Rook.class, 5.0);
            put(Bishop.class, 3.0);
            put(Knight.class, 3.0);
            put(Pawn.class, 1.0);
        }};
    }

    /**
     *
     * @param b
     * @param turn
     */
    public Chess(Board b, PlayerColor turn){
        this.board = b.copy();
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

    public boolean play(Move move) {
        return this.play(move.getOrigin(), move.getTarget(), 'Q');
    }

    public boolean play(Move move, char promotion) {
        return this.play(move.getOrigin(), move.getTarget(), promotion);
    }
    /**
     * TODO: UNFINISHED
     * @param originX
     * @param originY
     * @param targetX
     * @param targetY
     * @param promotion
     * @return
     */
    public boolean play(int originX, int originY, int targetX, int targetY, char promotion){
        Coord origin = new Coord(originX, originY);
        Coord target = new Coord(targetX, targetY);
        return this.play(origin, target, promotion);
    }

    /**
     *
     * @param originX
     * @param originY
     * @param targetX
     * @param targetY
     * @return
     */
    public boolean play(int originX, int originY, int targetX, int targetY){
        return this.play(originX, originY, targetX, targetY, 'Q');
    }

    public boolean play(Coord origin, Coord target, char promotion) {
        if (this.isGameOver()){
            return false;
        } else {
            if (isValidMove(this.board, this.turn, origin, target)){
                this.board.executeMove(origin, target, promotion);
                this.nextTurn();
                return true;
            } else {
                return false;
            }
        }
    }

    public Chess testMove(Move move) {
        Chess newGame = new Chess(Board.tryMove(this.board, move.getOrigin(), move.getTarget()), this.turn);
        // newGame.play(move);
        return newGame;
    }

    /**
     *
     * @return
     */
    public Board getBoard(){
        return this.board;
    }

    /**
     *
     * @return
     */
    public PlayerColor getTurn(){
        return this.turn;
    }

    /**
     *
     * @return
     */
    public double getScore(){
        return this.getScore(this.defaultValues);
    }

    /**
     * Calculates material difference between players
     * @param values
     * @return
     */
    public double getScore(Map<Class, Double> values){
        if (!this.isValidPieceValueMap(values)) throw new IllegalArgumentException("Invalid values map");
        double score = 0;
        for (IPiece b : this.board.getPieces(PlayerColor.BLACK)) {
            score -= values.get(b.getClass());
        }
        for (IPiece w : this.board.getPieces(PlayerColor.WHITE)) {
            score += values.get(w.getClass());
        }
        // limits return to only contain one decimal point.
        return (double) Math.round(score * 100) / 100;
    }

    /**
     * TODO: UNFINISHED
     * @return
     */
    public String toString(){
        //  TODO: FIGURE OUT WHAT TO PUT HERE
        return null;
    }

    /**
     * TODO: UNFINISHED
     * @return
     */
    public JsonObject toJson(){
        //  TODO: FIGURE OUT THIS
        return null;
    }

    /**
     *
     * @param values
     * @return
     */
    private boolean isValidPieceValueMap(Map<Class, Double> values){
        if (values == null) return false;
        Set<Class> s = new HashSet<>((Arrays.asList(King.class, Queen.class, Rook.class, Bishop.class, Knight.class, Pawn.class)));
        return values.keySet().containsAll(s);
    }

    /**
     *
     * @return
     */
    public boolean isGameOver(){
        return this.getAllPossibleMoves().size() == 0;
    }

    /**
     *
     * @param b
     * @param turn
     * @return
     */
    public static boolean isMate(Board b, PlayerColor turn){
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

    /**
     * TODO: PENDING REVIEW - (HAS CURRENT LOGIC, COULD BE IMPROVED)
     * @param b
     * @param origin
     * @param target
     * @return
     */
    private static boolean isValidEnPassant(Board b, Coord origin, Coord target){
        if (b.getBoardArray()[origin.getX()][origin.getY()] instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && b.getBoardArray()[target.getX()][target.getY()] == null){
            if (origin.getY() == (b.getBoardArray()[origin.getX()][origin.getY()].getIsBlack() ? 3 : 4)){
                if (b.getMoveHistory().size() > 3){        //  TODO: VERRIFY THIS
                    return (b.getMoveHistory().get(b.getMoveHistory().size() - 2).getPiece() instanceof Pawn) && (b.getBoardArray()[target.getX()][origin.getY()].getMoveCount() == 1);
                }
            }
        }
        return false;
    }

    /**
     *
     * @param b
     * @param turn
     * @return
     */
    public static boolean isInCheck(Board b, PlayerColor turn){
        for (IPiece p : b.getPieces(turn)){
            if (p.toString().charAt(1) == 'K'){
                return isInDanger(b, turn, p.getCoord());
            }
        }
        return true;
    }

    public List<Move> getAllPossibleMoves() {
       return this.getAllPossibleMoves(this.turn);
    }
    /**
     * Obtains a list of all the possible moves that can be made for the given player.
     * @return
     */
    public List<Move> getAllPossibleMoves(PlayerColor color) {
         List<IPiece> pieces = (color == PlayerColor.WHITE) ? this.board.getPieces(PlayerColor.WHITE) :  this.board.getPieces(PlayerColor.BLACK);
         List<Move> result = new ArrayList<>();
         pieces.forEach(p -> {
             Coord currentCoord = p.getCoord();
             List<Coord> possibleCoords = p.getPossibleMoves(this.board.getBoardArray(), this.board.getMoveHistory());
             // TODO: take all possible promotions into account?
             result.addAll(possibleCoords.stream().map(c -> new Move(this.board, currentCoord, c, 'Q')).collect(Collectors.toList()));
         });
         return result;
    }

    /**
     *
     * @param b
     * @param turn
     * @param start
     * @param end
     * @return
     */
    public static boolean isInDangerBetween(Board b, PlayerColor turn, Coord start, Coord end){
        for ( Coord c : start.calculatePointsBetweenInclusiveEnd((end))) {
            if (isInDanger(b, turn, c)) return true;
        }
        return false;
    }

    /**
     *
     * @param b
     * @param turn
     * @param target
     * @return
     */
    private static boolean isInDanger(Board b, PlayerColor turn, Coord target){
        for (IPiece p : b.getPieces(turn.other())){
            if (isValidMovePiece(b, p.getCoord(), target)) return true;
        }
        return false;
    }

    /**
     *
     * @param b
     * @param origin
     * @param target
     * @return
     */
    private static boolean isValidMovePiece(Board b, Coord origin, Coord target){
        return b.getBoardArray()[origin.getX()][origin.getY()].isValidMove(b.getBoardArray(), origin, target);
    }

    /**
     *
     * @param b
     * @param turn
     * @param origin
     * @param target
     * @return
     */
    public static boolean isValidMoveBoolean(Board b, PlayerColor turn, Coord origin, Coord target){
        try {
            return isValidMove(b, turn, origin, target);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @param b
     * @param turn
     * @param origin
     * @param target
     * @return
     */
    private static boolean isValidMove(Board b, PlayerColor turn, Coord origin, Coord target){
        if (!origin.isInsideBoard() || !target.isInsideBoard()) {
            throw new IllegalArgumentException("Coordinate outside of board");
        } else {
            IPiece from = b.getBoardArray()[origin.getX()][origin.getY()];
            IPiece to = b.getBoardArray()[target.getX()][target.getY()];
            if (from == null) {
                throw new IllegalArgumentException("Must move a piece");
            } else if (origin.equals(target)) {
                throw new IllegalArgumentException("Cannot move to same space");
            } else if (from.getIsBlack() && turn == PlayerColor.WHITE || !from.getIsBlack() && turn == PlayerColor.BLACK) {
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

    /**
     *
     */
    private void nextTurn(){
        if (this.turn == PlayerColor.WHITE){
            this.turn = PlayerColor.BLACK;
        } else {
            this.turn = PlayerColor.WHITE;
        }
    }

}
