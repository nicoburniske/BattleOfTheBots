import common.Coord;
import piece.*;

import java.util.ArrayList;
import java.util.List;

// represents a chess board
public class ChessBoard {
    private IPiece[][] board;
    private List<IPiece> whitePieces;
    private List<IPiece> blackPieces;
    private boolean whiteTurn;
    private List<IPiece[][]> history;
    private int movesSoFar;

    /*
        ################################
            CONSTRUCTORS
        ################################
     */

    ChessBoard() {
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.board = generateChessBoard();
        this.whiteTurn = true;
        this.history = new ArrayList<>();
        this.movesSoFar = 0;
        this.recordHistory();
    }

    private ChessBoard(IPiece[][] board, boolean whiteTurn) {
        if (board.length != 8 || board[0].length != 8) {
            throw new IllegalArgumentException("Board is not regulation size");
        }
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.board = board;
        this.whiteTurn = whiteTurn;
        this.history = new ArrayList<>();
        movesSoFar = 0;
        this.initPieces();
    }

    /*
        ################################
            PUBLIC / GEN
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
    boolean playGame(int fromX, int fromY, int toX, int toY) {
        if (this.isValidMove(fromX, fromY, toX, toY)) {
            performMove(fromX, fromY, toX, toY);
            this.nextTurn();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the current turn
     * @return true if white's turn, false if black's
     */
    boolean isWhiteTurn() {
        return this.whiteTurn;
    }

    /**
     * Creates a new copy of the board
     * @return - new copy of board
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

    /*
        toString() method
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                IPiece current = board[j][i];
                if (current == null)
                    sb.append("XX");
                else {
                    sb.append(current.toString());
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /*
        ################################
            PRIVATE METHODS
        ################################
    */

    /**
     *  Executes a castle
     * @param fromX - x-coordinate of target castle
     * @param toX - x-coordinate of king
     * @param y - either 0 or 7
     */
    //TODO: remove (int y) parameter and replace with
    // int y = this.whiteTurn ? 0 : 7;
    private void executeCastle(int fromX, int toX, int y) {
        int direction = toX - fromX;
        int fromCastleX = direction > 0 ? 7 : 0;
        int toCastleX = direction > 0 ? 5 : 3;

        if (isValidMove(fromCastleX, y, toCastleX, y)) {
            IPiece castle = this.board[fromCastleX][y];
            castle.makeMove(toCastleX, y);
            this.board[fromCastleX][y] = null;
            this.board[toCastleX][y] = castle;
        }
    }

    /**
     *  Tests a move in an isolated clone ChessBoard to see if it will result in king in check.
     * @param fromX - x-coordinate of target piece
     * @param fromY - y-coordinate of target piece
     * @param toX - x-coordinate of desired location
     * @param toY - y-coordinate of desired location
     * @return True if move results in no check, false otherwise
     */
    private boolean testMove(int fromX, int fromY, int toX, int toY){
        ChessBoard temp = new ChessBoard(this.getBoard(), this.isWhiteTurn());
        temp.performMove(fromX, fromY, toX, toY);
        return !temp.isInCheck();
    }

    /**
     *  Checks if the current turn's king is in check
     * @return - True if in check, false otherwise
     */
    private boolean isInCheck(){
        for (IPiece x : (this.whiteTurn ? this.whitePieces : this.blackPieces) ){
            if ( x.toString().charAt(1) == 'K'){
                return isInDanger(x.getX(), x.getY());
            }
        }
        return true;
    }

    /**
     * Returns true is the square at the specified coordinates is at risk.
     * @param x - Target square's x coordinate
     * @param y - Target square's y coordinate
     * @return - True if target square is at risk, false otherwise
     */
    private boolean isInDanger(int x, int y){
        for (IPiece p : (this.whiteTurn ? this.blackPieces : this.whitePieces) ){
            if ( this.isValidMovePiece(p.getX(), p.getY(), x, y) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of coordinates of all possible valid moves for the specified piece
     * @param p - IPiece being evaluated
     * @return Array of coordinates of all valid moves for specified IPiece
     */
    private List<Coord> possibleMoves(IPiece p){
        //TODO: Move method to AbstractPiece and introduce specific move seeking algorithms per piece
        //  Currently checks 64 moves on grid, can be significantly reduced.
        //TODO: STILL SLOPPY, testMove() should be in Piece's getPossibleMoves()
        List<Coord> pos_moves = new ArrayList<>(p.getPossibleMoves(this.getBoard()));
        for (int i = 0; i < pos_moves.size(); i++){
            if ( !testMove(p.getX(), p.getY(), pos_moves.get(i).getX(), pos_moves.get(i).getY()) ){
                pos_moves.remove(i);
            }
        }
       return pos_moves;
    }

    /**
     * Checks if the current player can make a move that won't result with their king in check
     * @return True if moves available, false otherwise
     */
    private boolean hasRemainingMoves(){
        for (IPiece p : (this.whiteTurn ? this.whitePieces : this.blackPieces) ){
            if ( !this.possibleMoves(p).isEmpty() ){
                return true;
            }
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
     *
     * @param fromX - x-coordinate of target piece
     * @param fromY - y-coordinate of target piece
     * @param toX - x-coordinate of desired location
     * @param toY - y-coordinate of desired location
     * @return will return true if the given move is valid, and false otherwise.
     */
    private boolean isValidMoveBoolean(int fromX, int fromY, int toX, int toY) {
        try {
            return this.isValidMove(fromX, fromY, toX, toY);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the given piece is allowed to move to the given coordinate on the board.
     * @param fromX - x-coordinate of target piece
     * @param fromY - y-coordinate of target piece
     * @param toX - x-coordinate of desired location
     * @param toY - y-coordinate of desired location
     * @return
     */
    private boolean isValidMovePiece(int fromX, int fromY, int toX, int toY){
        IPiece from = this.board[fromX][fromY];
        return from.isValidMove(this.board, fromX, fromY, toX, toY);
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
     * @param fromX - x-coordinate of target piece
     * @param fromY - y-coordinate of target piece
     * @param toX - x-coordinate of desired location
     * @param toY - y-coordinate of desired location
     * @return - True if the given move is valid, and will throw an exception otherwise.
     */
    //TODO: if isInCheck and the resulting move does not remove the player from being in check, then it is an invalid move
    // use constructor that takes in a 2d board to make a new ChessBoard (feed it this.getBoard(), and this.isWhiteTurn()) to have a copy of the board.
    // perform the move on this board and if the king is still in check (for the player that moved, than the move is invalid.
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
            } else if (!from.isValidMove(this.board, fromX, fromY, toX, toY)) {
                throw new IllegalArgumentException("Invalid move");
            } else if (this.isInCheck() && !this.testMove(fromX, fromY, toX, toY)) {
                throw new IllegalArgumentException("Move results with King in check");
            }
            return true;
        }
    }

    /**
     * Performs a given move. Should be used after making check that it is a valid move.
     * @param fromX - x-coordinate of target piece
     * @param fromY - y-coordinate of target piece
     * @param toX - x-coordinate of desired location
     * @param toY - y-coordinate of desired location
     */
    private void performMove(int fromX, int fromY, int toX, int toY){
        IPiece movedPiece = this.board[fromX][fromY];
        if (movedPiece instanceof King && Math.abs(fromX - toX) == 2) {
            this.executeCastle(fromX, toX, toY);
        }
        movedPiece.makeMove(toX, toY);
        this.removePiece(this.board[toX][toY]);
        this.board[toX][toY] = movedPiece;
        this.board[fromX][fromY] = null;
    }

    /**
     * Populates the Black and White IPiece[]'s with piece from IPiece[][] board
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

    /**
     *  Generates a new fully populated chessboard
     * @return - new populated chessboard (IPiece[][])
     */
    private IPiece[][] generateChessBoard() {
        return new IPiece[][]{
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
     * Adds a piece to its respective IPiece[] array
     * @param p - The piece being added to the chessboard
     * @return - The added piece
     */
    private IPiece addPiece(IPiece p) {
        if (p == null) throw new IllegalArgumentException("Piece cannot be null");
        (p.getIsBlack() ? this.blackPieces : this.whitePieces).add(p);
        return p;
    }

    /**
     * Removes a piece to its respective IPiece[] array
     * @param p - The piece being removed to the chessboard
     */
    private void removePiece(IPiece p) {
        if (p != null) {
            (p.getIsBlack() ? this.blackPieces : this.whitePieces).remove(p);
        }
    }

    /**
     * Checks if the specified coordinate is inside board bounds
     * @param x - target x-coordinate
     * @param y - target y-coordinate
     * @return True if in bounds, false otherwise
     */
    private boolean coordInsideBoard(int x, int y) {
        return (x > -1 && x < 8) && (y > -1 && y < 8);
    }

    /**
     * Advances game-state turn
     */
    private void nextTurn() {
        this.whiteTurn = !this.whiteTurn;
        this.movesSoFar++;
        this.recordHistory();
    }

    /**
     * Adds the board-state to history
     */
    private void recordHistory() {
        this.history.add(this.getBoard());
    }

}
