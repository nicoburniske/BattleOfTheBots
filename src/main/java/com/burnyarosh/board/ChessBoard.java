package com.burnyarosh.board;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.piece.*;

import java.util.ArrayList;
import java.util.List;

// represents a chess board
public class ChessBoard {
    private IPiece[][] board;
    private List<IPiece> whitePieces;
    private List<IPiece> blackPieces;
    private boolean whiteTurn;
    private List<Move> moves;

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
    }

    /*
        ################################
            PUBLIC METHODS
        ################################
     */

    /**
     * Master method for a turn in the game
     * @param fromX - x-coordinate of target piece
     * @param fromY - y-coordinate of target piece
     * @param toX - x-coordinate of desired location
     * @param toY - y-coordinate of desired location
     * @return - true if valid move*, false otherwise
     */
    public boolean playGame(int fromX, int fromY, int toX, int toY) {
        if (isGameOver()){
            //GAME IS OVER HERE BECAUSE CHECKMATE DETECTED
            return false;
        } else {
            Coord origin = new Coord(fromX, fromY);
            Coord target = new Coord(toX, toY);
            if (this.isValidMove(origin, target)) {
                this.performMove(origin, target);
                this.updateMoveList(origin, board[toX][toY]);
                this.nextTurn();
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Returns the current turn
     * @return true if white's turn, false if black's
     */
    public boolean isWhiteTurn() {
        return this.whiteTurn;
    }

    /**
     * Gets a copy of Chessboards IPiece[][] board
     * @return - copy of IPiece[][] board
     */
    public IPiece[][] getBoard() {
        IPiece[][] newBoard = new IPiece[8][8];
        for (IPiece p : this.whitePieces) {
            newBoard[p.getX()][p.getY()] = p.copy();
        }
        for (IPiece p : this.blackPieces) {
            newBoard[p.getX()][p.getY()] = p.copy();
        }
        return newBoard;
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
            castle.makeMove(toCastleX, y);
            this.board[fromCastleX][y] = null;
            this.board[toCastleX][y] = castle;
        }
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
            this.board[target.getX()][origin.getY()] = null;
        }
        movedPiece.makeMove(target.getX(), target.getY());
        this.removePiece(this.board[target.getX()][target.getY()]);
        this.board[target.getX()][target.getY()] = movedPiece;
        this.board[origin.getX()][origin.getY()] = null;
    }

    /**
     *  Tests a move in an isolated clone ChessBoard to see if it will result in king in check.
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return True if move results in no check, false otherwise
     */
    private boolean testMove(Coord origin, Coord target){
        ChessBoard temp = new ChessBoard(this.getBoard(), this.isWhiteTurn());
        temp.performMove(origin, target);
        return !temp.isInCheck();
    }

    private boolean isCheckMate(){
        if (isInCheck()){
            for (IPiece p : this.isWhiteTurn() ? this.whitePieces : this.blackPieces){
                for (Coord c : p.getPossibleMoves(this.board, moves)){
                    if (testMove(new Coord(p.getX(), p.getY()), c)){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean isGameOver(){
        return isCheckMate();
    }

    /**
     *  Checks if the current turn's king is in check
     * @return - True if in check, false otherwise
     */
    private boolean isInCheck(){
        for (IPiece p : (this.whiteTurn ? this.whitePieces : this.blackPieces) ){
            if (p.toString().charAt(1) == 'K'){
                return isInDanger(new Coord(p.getX(), p.getY()));
            }
        }
        return true;
    }

    /**
     * Returns true is the square at the specified coordinates is at risk.
     * @param target - target Coord obj
     * @return - True if target square is at risk, false otherwise
     */
    private boolean isInDanger(Coord target){
        for (IPiece p : (this.whiteTurn ? this.blackPieces : this.whitePieces) ){
            if (this.isValidMovePiece(new Coord(p.getX(), p.getY()), target)) return true;
        }
        return false;
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
            } else if (!from.isValidMove(this.board, origin.getX(), origin.getY(), target.getX(), target.getY()) && !this.isValidEnPassant(origin, target)) {   //TODO: update IPiece.isValidMove method to take Coord as params
                throw new IllegalArgumentException("Invalid move");
            } else if (this.isInCheck() && !this.testMove(origin, target)) {
                throw new IllegalStateException("Move results with King in check");
            }
            return true;
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
     * Checks if the given piece is allowed to move to the given coordinate on the board.
     * @param origin - origin Coord obj
     * @param target - target Coord obj
     * @return - true if given move can be made, false otherwise
     */
    private boolean isValidMovePiece(Coord origin, Coord target){
        return this.board[origin.getX()][origin.getY()].isValidMove(this.board, origin.getX(), origin.getY(), target.getX(), target.getY()); //TODO: update this method to use Coords
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
    private void updateMoveList(Coord origin, IPiece p) {
        this.moves.add(new Move(origin, p));
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
}
