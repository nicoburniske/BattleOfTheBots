package com.burnyarosh.board;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.piece.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;

// represents a chess board
public class ChessBoard {
    private IPiece[][] board;
    private List<IPiece> whitePieces;
    private List<IPiece> blackPieces;
    private boolean whiteTurn;
    private List<Move> moves;
    private Map<Class, Double> defaultValues;

    /*
        ################################
            CONSTRUCTORS
        ################################
     */

    public ChessBoard() {
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.board = generateChessBoard();
        this.whiteTurn = true;
        this.moves = new ArrayList<>();
        this.defaultValues = new HashMap<>() {{
            put(King.class, 1000.0);
            put(Queen.class, 9.0);
            put(Rook.class, 5.0);
            put(Bishop.class, 3.0);
            put(Knight.class, 3.0);
            put(Pawn.class, 1.0);
        }};
    }

    public ChessBoard(IPiece[][] board, boolean whiteTurn) {
        if (board.length != 8 || board[0].length != 8) {
            throw new IllegalArgumentException("Board is not regulation size");
        }
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.board = board;
        this.whiteTurn = whiteTurn;
        this.moves = new ArrayList<>();
        this.initPieces();
        this.defaultValues = new HashMap<>() {{
            put(King.class, 1000.0);
            put(Queen.class, 9.0);
            put(Rook.class, 5.0);
            put(Bishop.class, 3.0);
            put(Knight.class, 3.0);
            put(Pawn.class, 1.0);
        }};
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */
    public boolean playGame(int originX, int originY, int targetX, int targetY, char promotion){
        if (isGameOver()){
            //GAME IS OVER HERE BECAUSE CHECKMATE DETECTED
            return false;
        } else {
            Coord origin = new Coord(originX, originY);
            Coord target = new Coord(targetX, targetY);
            if (this.isValidMove(origin, target)) {
                this.updateMoveList(board, origin, target);
                this.performMove(origin, target);
                this.executePromotion(target, promotion);
                this.nextTurn();
                this.updatePreviousMove();
                return true;
            } else {
                return false;
            }
        }
    }
    /**
     * Master method for a turn in the game
     * @param fromX - x-coordinate of target piece
     * @param fromY - y-coordinate of target piece
     * @param toX - x-coordinate of desired location
     * @param toY - y-coordinate of desired location
     * @return - true if valid move*, false otherwise
     */
    public boolean playGame(int fromX, int fromY, int toX, int toY) {
        return this.playGame(fromX, fromY, toX, toY, 'q');
    }

    /**
     * Returns the current turn
     * @return true if white's turn, false if black's
     */
    public boolean isWhiteTurn() {
        return this.whiteTurn;
    }

    public double getScore() {
        return this.getScore(this.defaultValues);
    }

    /**
     * Computes the score representing material differential between the two players.
     * Positive values mean the white in up by x points, and negative values mean that black is up by x points.
     */
    public double getScore(Map<Class, Double> values) {
        if (!this.isValidPieceValueMap(values)) throw new IllegalArgumentException("Invalid values map");
        double score = 0;
        for (IPiece b : this.blackPieces) {
            score -= values.get(b.getClass());
        }
        for (IPiece w : this.whitePieces) {
            score += values.get(w.getClass());
        }
        // limits return to only contain one decimal point.
        return (double) Math.round(score * 100) / 100;
    }

    private boolean isValidPieceValueMap(Map<Class, Double> values) {
        if (values == null) return false;
        Set<Class> set = new HashSet<>();
        set.addAll(Arrays.asList(King.class, Queen.class, Rook.class, Bishop.class, Knight.class, Pawn.class));
        return values.keySet().containsAll(set);
    }

    /**
     * Gets a copy of Chessboards IPiece[][] board
     * @return - copy of IPiece[][] board
     */
    public IPiece[][] getBoard() {
        IPiece[][] newBoard = new IPiece[8][8];
        for (IPiece p : this.whitePieces) {
            newBoard[p.getCoord().getX()][p.getCoord().getY()] = p.copy();
        }
        for (IPiece p : this.blackPieces) {
            newBoard[p.getCoord().getX()][p.getCoord().getY()] = p.copy();
        }
        return newBoard;
    }

    public List<Move> getHistory(){
        return this.moves;
    }

    /**
     * Provides a String representation of the game-state
     * @return - String of Chessboard
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        {   //  HEADER
            sb.append("\n╔");
            for (int s = 0; s < 7; s++) {
                sb.append("════╦");
            }
            sb.append("════╗\n");
        }
        {   //  BODY
            for (int i = 7; i > -1; i--) {
                sb.append("║ ");
                for (int j = 0; j < 8; j++) {
                    IPiece current = board[j][i];
                    if (current == null)
                        sb.append("  ");
                    else {
                        sb.append(current.toString());
                    }
                    sb.append(" ║ ");
                }
                if (i != 0) {
                    sb.append("\n║");
                    for (int s = 0; s < 7; s++) {
                        sb.append("════╬");
                    }
                    sb.append("════╣");
                    sb.append("\n");
                }
            }
        }
        {   //  FOOTER
            sb.append("\n╚");
            for (int s = 0; s < 7; s++) {
                sb.append("════╩");
            }
            sb.append("════╝");
        }
        return sb.toString();
    }

        /*
        ################################
            PRIVATE METHODS
        ################################
     */

    private void executePromotion(Coord target, char p){
       if ((this.board[target.getX()][target.getY()] instanceof Pawn) && (target.getY() == (this.board[target.getX()][target.getY()].getIsBlack() ? 0 : 7))){
           //System.out.println("PROMO");
            if (p == 'q'){
                IPiece tmp = this.board[target.getX()][target.getY()].promote(false);
                this.removePiece(this.board[target.getX()][target.getY()]);
                this.addPiece(tmp);
                this.board[target.getX()][target.getY()] = tmp;
            } else if (p == 'n'){
                IPiece tmp = this.board[target.getX()][target.getY()].promote(true);
                this.removePiece(this.board[target.getX()][target.getY()]);
                this.addPiece(tmp);
                this.board[target.getX()][target.getY()] = tmp;
            }
        }
    }

    private boolean isGameOver(){
        return isCheckmate();
    }

    private boolean isCheckmate(){
        if (isInCheck()){
            for (IPiece p : this.isWhiteTurn() ? this.whitePieces : this.blackPieces){
                for (Coord c : p.getPossibleMoves(this.board, moves)){
                    if (tryMove(p.getCoord(), c)){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     *  Tests a move in an isolated clone ChessBoard to see if it will result in king in check.
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return True if move results in no check, false otherwise
     */
    private boolean tryMove(Coord origin, Coord target){
        ChessBoard temp = new ChessBoard(this.getBoard(), this.isWhiteTurn());
        temp.performMove(origin, target);
        return !temp.isInCheck();
    }

    /**
     * Performs a given move. Should be used after making check that it is a valid move.
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     */
    private void performMove(Coord origin, Coord target) {
        IPiece movedPiece = this.board[origin.getX()][origin.getY()];
        if (movedPiece instanceof King
                && Math.abs(origin.getX() - target.getX()) == 2
                && !isInDangerBetween(origin, target)) {
            this.executeCastle(origin, target);
        } else if ( this.isValidEnPassant(origin, target)){
            this.removePiece(this.board[target.getX()][origin.getY()]);
            this.board[target.getX()][origin.getY()] = null;
            this.moves.get(this.moves.size()-1).setEnPassant();
        }
        movedPiece.makeMove(target);
        this.removePiece(this.board[target.getX()][target.getY()]);
        this.board[target.getX()][target.getY()] = movedPiece;
        this.board[origin.getX()][origin.getY()] = null;
    }

    /**
     * Executes a castle move
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     */
    private void executeCastle(Coord origin, Coord target) {
        int direction = target.getX() - origin.getX();
        int y = target.getY();
        int fromCastleX = direction > 0 ? 7 : 0;
        int toCastleX = direction > 0 ? 5 : 3;
        if (isValidMoveBoolean(new Coord(fromCastleX, y), new Coord(toCastleX, y))) {
            IPiece castle = this.board[fromCastleX][y];
            castle.makeMove(new Coord(toCastleX, y));
            this.board[fromCastleX][y] = null;
            this.board[toCastleX][y] = castle;
        }
    }

    /**
     * Checks to see if the move was a valid En passant move.
     * 1) captor must be on its 5th rank
     * 2) capturée must be on an adjacent file
     * 3) capturée must have just moved two squares in a single move (double-step move)
     * 4) the capture can only be made on the move immediately after the enemy pawn makes the double-step move
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return true if valid en passant move, false otherwise
     */
    private boolean isValidEnPassant(Coord origin, Coord target){
        if ( this.board[origin.getX()][origin.getY()] instanceof Pawn && Math.abs(origin.getY() - target.getY()) == 1 && Math.abs(origin.getX() - target.getX()) == 1 && this.board[target.getX()][target.getY()] == null){ // std conditiona
            if ( (origin.getY() == (this.isWhiteTurn() ? 4 : 3)) ) {    //  Condition #1
                if (this.moves.size() > 3) { //  avoid OutOfBoundsException  (en passant impossible under 4 moves)
                    return (this.moves.get(this.moves.size() - 2).getPiece() instanceof Pawn) && (this.board[target.getX()][origin.getY()].getMoveCount() == 1); //Conditions 2, 3, and 4
                }
            }
        }
        return false;
    }

    /**
     *  Checks if the current turn's king is in check
     * @return - True if in check, false otherwise
     */
    private boolean isInCheck(){
        for (IPiece p : (this.whiteTurn ? this.whitePieces : this.blackPieces) ){
            if (p.toString().charAt(1) == 'K'){
                return isInDanger(p.getCoord());
            }
        }
        return true;
    }

    /**
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return - true if is in danger, false otherwise
     */
    private boolean isInDangerBetween(Coord origin, Coord target) {
        for ( Coord coord : origin.calculatePointsBetweenInclusiveEnd((target))) {
            if (this.isInDanger(coord)) return true;
        }
        return false;
    }

    /**
     * Returns true is the square at the specified coordinates is at risk.
     * @param target - target Coord obj
     * @return - True if target square is at risk, false otherwise
     */
    private boolean isInDanger(Coord target){
        for (IPiece p : (this.whiteTurn ? this.blackPieces : this.whitePieces) ){
            if (this.isValidMovePiece(p.getCoord(), target)) return true;
        }
        return false;
    }

    /**
     * Checks if the given piece is allowed to move to the given coordinate on the board.
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return - true if given move can be made, false otherwise
     */
    private boolean isValidMovePiece(Coord origin, Coord target){
        return this.board[origin.getX()][origin.getY()].isValidMove(this.board, origin, target);
    }

    /**
     * Makes sure:
     * - Both coordinates are inside board.
     * - From coordinate contains a piece.
     * - Coordinates are not the same.
     * - It is the correct player's turn.
     * - Not moving to a space occupied by space of same color.
     * - Delegates to individual piece logic.
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return will return true if the given move is valid, and false otherwise.
     */
    private boolean isValidMoveBoolean(Coord origin, Coord target) {
        try {
            return this.isValidMove(origin, target);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Makes sure:
     * - Both coordinates are inside board.
     * - From coordinate contains a piece.
     * - Coordinates are not the same.
     * - It is the correct player's turn.
     * - Not moving to a space occupied by space of same color.
     * - Delegates to individual piece logic.
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return will return true if the given move is valid, and will throw an exception otherwise.
     */
    //TODO: if isInCheck and the resulting move does not remove the player from being in check, then it is an invalid move
    // use constructor that takes in a 2d board to make a new chessBoard (feed it this.getBoard(), and this.isWhiteTurn()) to have a copy of the board.
    // perform the move on this board and if the king is still in check (for the player that moved, than the move is invalid.
    private boolean isValidMove(Coord origin, Coord target) {
        if (!origin.isInsideBoard() || !target.isInsideBoard()) {
            throw new IllegalArgumentException("Coordinate outside of board");
        } else {
            IPiece from = this.board[origin.getX()][origin.getY()];
            IPiece to = this.board[target.getX()][target.getY()];
            if (from == null) {
                throw new IllegalArgumentException("Must move a piece");
            } else if (origin.equals(target)) {
                throw new IllegalArgumentException("Cannot move to same space");
            } else if (from.getIsBlack() && this.whiteTurn || !from.getIsBlack() && !this.whiteTurn) {
                throw new IllegalArgumentException("Other player's move");
            } else if (to != null && (from.getIsBlack() && to.getIsBlack() || !(from.getIsBlack() || to.getIsBlack()))) {
                throw new IllegalArgumentException("Cannot move to square occupied by piece of same color");
            } else if (!from.isValidMove(this.board, origin, target) && !this.isValidEnPassant(origin, target)) {
                throw new IllegalArgumentException("Invalid move");
            } else if (this.isInCheck() && !this.tryMove(origin, target)) {
                throw new IllegalStateException("Move results with King in check");
            }
            return true;
        }
    }

    /**
     *  Generates a new default IPiece[][] board with all pieces in their starting positions
     * @return - new default IPiece[][] board
     */
    private IPiece[][] generateChessBoard() {
        return new IPiece[][] {
                {this.addPiece(new Rook(0, 0, false)), this.addPiece(new Pawn(0, 1, false)), null, null, null, null, this.addPiece(new Pawn(0, 6, true)), this.addPiece(new Rook(0, 7, true))},
                {this.addPiece(new Knight(1, 0, false)), this.addPiece(new Pawn(1, 1, false)), null, null, null, null, this.addPiece(new Pawn(1, 6, true)), this.addPiece(new Knight(1, 7, true))},
                {this.addPiece(new Bishop(2, 0, false)), this.addPiece(new Pawn(2, 1, false)), null, null, null, null, this.addPiece(new Pawn(2, 6, true)), this.addPiece(new Bishop(2, 7, true))},
                {this.addPiece(new Queen(3, 0, false)), this.addPiece(new Pawn(3, 1, false)), null, null, null, null, this.addPiece(new Pawn(3, 6, true)), this.addPiece(new Queen(3, 7, true))},
                {this.addPiece(new King(4, 0, false)), this.addPiece(new Pawn(4, 1, false)), null, null, null, null, this.addPiece(new Pawn(4, 6, true)), this.addPiece(new King(4, 7, true))},
                {this.addPiece(new Bishop(5, 0, false)), this.addPiece(new Pawn(5, 1, false)), null, null, null, null, this.addPiece(new Pawn(5, 6, true)), this.addPiece(new Bishop(5, 7, true))},
                {this.addPiece(new Knight(6, 0, false)), this.addPiece(new Pawn(6, 1, false)), null, null, null, null, this.addPiece(new Pawn(6, 6, true)), this.addPiece(new Knight(6, 7, true))},
                {this.addPiece(new Rook(7, 0, false)), this.addPiece(new Pawn(7, 1, false)), null, null, null, null, this.addPiece(new Pawn(7, 6, true)), this.addPiece(new Rook(7, 7, true))}
        };
    }

    /**
     * Adds an IPiece to list of pieces
     * @param p - IPiece to be added
     * @return - added IPiece
     */
    private IPiece addPiece(IPiece p) {
        if (p == null) throw new IllegalArgumentException("Piece cannot be null");
        (p.getIsBlack() ? this.blackPieces : this.whitePieces).add(p);
        return p;
    }

    /**
     * Will switch the turn, increment the movesSoFar counter, and record the current state of the game.
     */
    private void nextTurn() {
        this.whiteTurn = !this.whiteTurn;
    }

    /**
     * Updates the Move List with the most recent move
     */
    private void updateMoveList(IPiece[][] board, Coord origin, Coord target) {
        this.moves.add(new Move(board, origin, target));
    }

    private void updatePreviousMove(){
        if (isInCheck()){
            this.moves.get(this.moves.size()-1).setIsCheck();
            if (isCheckmate()){
                this.moves.get(this.moves.size()-1).setIsCheckmate();
            }
        }
    }

    /**
     * Will remove the given piece from the appropriate list of pieces. Depends on the color/team of the IPiece parameter.
     *  If p is null, nothing will happen.
     * @param p the IPiece to be removed from the appropriate list of pieces.
     */
    private void removePiece(IPiece p) {
        if (p != null) {
            (p.getIsBlack() ? this.blackPieces : this.whitePieces).remove(p);
        }
    }

    /**
     * Will initialize fields whitePieces and blackPieces to contain the same pieces that the field board contains in the 2d grid.
     */
    private void initPieces() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                IPiece curr = this.board[i][j];
                if(curr != null) {
                    (curr.getIsBlack() ? this.blackPieces : this.whitePieces).add(curr);
                }
            }
        }
    }

    public JsonObject toJson() {
        JsonObject ret = new JsonObject();
        JsonArray whitePieces = new JsonArray();
        for (IPiece w : this.whitePieces) {
            whitePieces.add(w.toJson());
        }
        JsonArray blackPieces = new JsonArray();
        for (IPiece b : this.blackPieces) {
            blackPieces.add(b.toJson());
        }
        ret.put("white", whitePieces);
        ret.put("black", blackPieces);
        ret.put("score", this.getScore());
        return ret;
    }
}
