package com.burnyarosh.board;
import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.piece.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Board {
    private IPiece[][] board;
    private List<IPiece> whitePieces;
    private List<IPiece> blackPieces;
    private List<Move> history;

    /**
     *
     */
    public Board(){
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        this.history = new ArrayList<>();
        this.board =  this.generateNewBoard();
    }

    /**
     *
     * @param whitePieces
     * @param blackPieces
     * @param history
     */
    private Board(List<IPiece> whitePieces, List<IPiece> blackPieces, List<Move> history){
        this.whitePieces = whitePieces;
        this.blackPieces = blackPieces;
        this.board = initializeBoardArray();
        this.history = history;
    }

    /**
     *
     * @param c
     * @return
     */
    public List<IPiece> getPieces(Chess.Color c){
        return (c == Chess.Color.WHITE ? this.whitePieces : this.blackPieces);
    }

    /**
     *
     * @return
     */
    public IPiece[][] getBoardArray(){
        return this.board;
    }

    /**
     *
     * @return
     */
    public List<Move> getMoveHistory(){
        return this.history;
    }

    /**
     *
     * @param origin
     * @param target
     * @param promotion
     */
    public void executeMove(Coord origin, Coord target, char promotion){
        Move temp = new Move(this.copy(), origin, target, promotion);
        IPiece p = this.board[origin.getX()][origin.getY()];
        boolean isPromotion = false;
        switch (temp.getType()){
            case CASTLE:
                int direction = target.getX() - origin.getX();
                IPiece castle = this.board[direction > 0 ? 7 : 0][target.getY()];
                castle.makeMove(new Coord(direction > 0 ? 5 : 3, target.getY()));
                this.board[direction > 0 ? 7 : 0][target.getY()] = null;
                this.board[direction > 0 ? 5 : 3][target.getY()] = castle;
                break;
            case EN_PASSANT:
                this.removePiece(this.board[target.getX()][origin.getY()]);
                this.board[target.getX()][origin.getY()] = null;
                break;
            case PROMOTION:
                isPromotion = true;
                break;
        }
        p.makeMove(target);
        this.removePiece(this.board[target.getX()][target.getY()]);
        this.board[target.getX()][target.getY()] = p;
        this.board[origin.getX()][origin.getY()] = null;
        if (isPromotion){
            if (promotion == 'Q'){
                IPiece tmp = this.board[target.getX()][target.getY()].promote(false);
                this.removePiece(this.board[target.getX()][target.getY()]);
                this.addPiece(tmp);
                this.board[target.getX()][target.getY()] = tmp;
            } else if (promotion == 'N'){
                IPiece tmp = this.board[target.getX()][target.getY()].promote(true);
                this.removePiece(this.board[target.getX()][target.getY()]);
                this.addPiece(tmp);
                this.board[target.getX()][target.getY()] = tmp;
            }
        }
        this.history.add(temp);
    }

    /**
     *
     * @param b
     * @param origin
     * @param target
     * @return
     */
    public static Board tryMove(Board b, Coord origin, Coord target){
        Board temp = b.copy();
        IPiece p = temp.getBoardArray()[origin.getX()][origin.getY()];
        switch (Move.classifyMove(temp, origin, target)){
            case CASTLE:
                int direction = target.getX() - origin.getX();
                IPiece castle = temp.getBoardArray()[direction > 0 ? 7 : 0][target.getY()];
                castle.makeMove(new Coord(direction > 0 ? 5 : 3, target.getY()));
                temp.getBoardArray()[direction > 0 ? 7 : 0][target.getY()] = null;
                temp.getBoardArray()[direction > 0 ? 5 : 3][target.getY()] = castle;
                break;
            case EN_PASSANT:
                temp.removePiece(temp.getBoardArray()[target.getX()][origin.getY()]);
                temp.getBoardArray()[target.getX()][origin.getY()] = null;
                break;
        }
        p.makeMove(target);
        temp.removePiece(temp.getBoardArray()[target.getX()][target.getY()]);
        temp.getBoardArray()[target.getX()][target.getY()] = p;
        temp.getBoardArray()[origin.getX()][origin.getY()] = null;
        return temp;
    }

    /**
     *
     * @return
     */
    public String toString(){
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

    /**
     *
     * @return
     */
    public Board copy(){
        return new Board(this.getPiecesCopy(Chess.Color.WHITE), this.getPiecesCopy(Chess.Color.BLACK), this.history);
    }

    /**
     *
     * @param c
     * @return
     */
    public List<IPiece> getPiecesCopy(Chess.Color c){
        List<IPiece> temp = new ArrayList<>();
        if (c == Chess.Color.BLACK){
            for (IPiece p : this.blackPieces){
                temp.add(p.copy());
            }
        } else if (c == Chess.Color.WHITE){
            for (IPiece p : this.whitePieces){
                temp.add(p.copy());
            }
        }
        return temp;
    }

    /**
     *
     * @return
     */
    public IPiece[][] getBoardArrayCopy(){
        IPiece[][] newBoard = new IPiece[8][8];
        for (IPiece p : this.whitePieces) {
            newBoard[p.getCoord().getX()][p.getCoord().getY()] = p.copy();
        }
        for (IPiece p : this.blackPieces) {
            newBoard[p.getCoord().getX()][p.getCoord().getY()] = p.copy();
        }
        return newBoard;
    }

    /**
     *
     * @return
     */
    private IPiece[][] generateNewBoard() {
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
     *
     * @param p
     * @return
     */
    private IPiece addPiece(IPiece p) {
        if (p == null) throw new IllegalArgumentException("Piece cannot be null");
        (p.getIsBlack() ? this.blackPieces : this.whitePieces).add(p);
        return p;
    }

    /**
     *
     * @param p
     */
    private void removePiece(IPiece p) {
        if (p != null) {
            (p.getIsBlack() ? this.blackPieces : this.whitePieces).remove(p);
        }
    }

    private IPiece[][] initializeBoardArray(){
        IPiece[][] newBoard = new IPiece[8][8];
        for (IPiece w : this.whitePieces){
            newBoard[w.getCoord().getX()][w.getCoord().getY()] = w;
        }
        for (IPiece b : this.blackPieces){
            newBoard[b.getCoord().getX()][b.getCoord().getY()] = b;
        }
        return newBoard;
    }

}
