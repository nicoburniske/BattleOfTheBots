package com.burnyarosh.board;

import com.burnyarosh.board.common.Coord;
import com.burnyarosh.board.common.Move;
import com.burnyarosh.board.piece.IPiece;

import java.util.List;

public class Board {
    private IPiece[][] board;
    private List<IPiece> whitePieces;
    private List<IPiece> blackPieces;
    private List<Move> history;

    public enum MoveType
    {
        STANDARD, CASTLE, PROMOTION, ENPASSANT;
    }

    //  TODO: UNFINISHED METHOD
    public Board(){

    }

    public IPiece[][] getBoardArray(){
        IPiece[][] newBoard = new IPiece[8][8];
        for (IPiece p : this.whitePieces) {
            newBoard[p.getCoord().getX()][p.getCoord().getY()] = p.copy();
        }
        for (IPiece p : this.blackPieces) {
            newBoard[p.getCoord().getX()][p.getCoord().getY()] = p.copy();
        }
        return newBoard;
    }

    //  TODO: UNFINISHED METHOD
    public List<IPiece> getPieces(Chess.Color c){
        if (c == Chess.Color.BLACK){
            // TODO: MAKE A COPY OF this.blackPieces;
        } else if (c == Chess.Color.WHITE){
            // TODO: MAKE A COPY OF this.blackPieces;
        }
        return null;
    }

    //  TODO: UNFINISHED METHOD
    public List<Move> getMoveHistory(){
        //  TODO: MAKE A COPY OF THE MOVE HISTORY
        return null;
    }

    // in Game aka ChessBoard, have validate or classifyMove()
    // check for CASTLE
    // check for enPassant

    //  TODO: UNFINISHED METHOD
    public void executeMove(MoveType t, Coord origin, Coord target, char promotion){
        Move temp = new Move(this.getBoardArray(), this.getMoveHistory(), origin, target);

        IPiece p = this.board[origin.getX()][origin.getY()];
        if (t == MoveType.CASTLE){
            //executeCastle code here
        } else if (t == MoveType.ENPASSANT){
            //en passant code here (from perform move)
        }
        // final make move things here
        if (t == MoveType.PROMOTION){
            // executePromotion code here
        }
        // TODO: CLASSIFY temp FOR EACH SPECIALTY PERFORMED
        this.history.add(temp);
    }

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


    //  addPiece
    //  removePiece

}
