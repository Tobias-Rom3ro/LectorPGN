package model;

import java.util.HashMap;

public enum Piece {
    // Enum para representar las piezas del ajedrez, tanto blancas como negras
    // Piezas blancas
    WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP,
    WHITE_QUEEN, WHITE_KING, WHITE_PAWN,

    // Piezas negras
    BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP,
    BLACK_QUEEN, BLACK_KING, BLACK_PAWN,

    // Representa la ausencia de una pieza
    NONE;

    // Mapa que asocia cada tipo de pieza con su notación en notación algebraica
    public static HashMap<Piece, Character> notation = new HashMap<>();

    static {
        // Inicializa el mapa de notaciones para piezas blancas
        notation.put(Piece.WHITE_ROOK, 'R');
        notation.put(Piece.WHITE_KNIGHT, 'N');
        notation.put(Piece.WHITE_BISHOP, 'B');
        notation.put(Piece.WHITE_QUEEN, 'Q');
        notation.put(Piece.WHITE_KING, 'K');
        notation.put(Piece.WHITE_PAWN, 'P');

        // Inicializa el mapa de notaciones para piezas negras
        notation.put(Piece.BLACK_ROOK, 'r');
        notation.put(Piece.BLACK_KNIGHT, 'n');
        notation.put(Piece.BLACK_BISHOP, 'b');
        notation.put(Piece.BLACK_QUEEN, 'q');
        notation.put(Piece.BLACK_KING, 'k');
        notation.put(Piece.BLACK_PAWN, 'p');

        // Asocia la ausencia de una pieza con un espacio en blanco
        notation.put(Piece.NONE, ' ');
    }
}
