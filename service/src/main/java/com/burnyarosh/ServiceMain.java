package com.burnyarosh;

import com.burnyarosh.board.ChessBoard;

public class ServiceMain {
    public static void main(String[] args) {
        ApiMain api = new ApiMain();
        api.startRouter();

        ChessBoard board = new ChessBoard();
        System.out.println(board);
    }
    //TODO: finish castling logic. Only one part missing. Description in King Class.
    //TODO: create isGameOver method: () -> boolean (uses possibleMove method)
    //TODO: create possibleMove Method: () -> List<Pair<Coord>>
    //TODO: figure out enpasse logic
}
