package com.burnyarosh;

public class Main {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        System.out.println(board);
    }
    //TODO: finish castling logic. Only one part missing. Description in King Class.
    //TODO: create isGameOver method: () -> boolean (uses possibleMove method)
    //TODO: create possibleMove Method: () -> List<Pair<Coord>>
    //TODO: figure out enpasse logic
}
