package utils;

import model.Piece;
import model.Square;

/**
 * La clase Method tiene metodos/utilidades para interactuar con el tablero de ajedrez.
 * Incluye métodos para convertir posiciones a casillas, obtener piezas en posiciones específicas,
 * y restablecer el tablero a su configuración inicial.
 */
public class Method {

    // Convierte una posición (fila, columna) en una casilla del tablero de ajedrez
    public static Square getSquare(int[] position){
        String name = null;

        // Determina el nombre de la columna basado en el índice de columna
        switch (position[1]){
            case 0 -> name = "A";
            case 1 -> name = "B";
            case 2 -> name = "C";
            case 3 -> name = "D";
            case 4 -> name = "E";
            case 5 -> name = "F";
            case 6 -> name = "G";
            case 7 -> name = "H";
        }
        name += position[0]+1; // Agrega el número de fila
        return Square.valueOf(name); // Convierte el nombre en un valor de enum Square
    }

    // Obtiene la posición (fila, columna) de una casilla específica
    public static int[] getPosition(Square square){
        return Square.position.get(square);
    }

    // Sobrecarga para obtener la posición de una casilla basada en su nombre como string
    public static int[] getPosition(String s){
        return Square.position.get(Square.valueOf(s.toUpperCase()));
    }

    // Obtiene la pieza en una posición dada (fila, columna)
    public static Piece getPiece(int[] position){
        return Square.piece.get(getSquare(position)); // Llama a getSquare para convertir la posición a una casilla
    }

    // Determina la pieza basada en el índice de fila y el carácter de notación
    public static Piece getPiece(int i, char c){
        String s = String.valueOf(c);
        // Determina el color de la pieza basado en la fila
        if(i % 2 == 0)
            s = s.toUpperCase(); // Fila par (blancas)
        else
            s = s.toLowerCase(); // Fila impar (negras)

        // Asigna la pieza correspondiente a partir de la notación
        switch (s){
            case "P" -> {
                return Piece.WHITE_PAWN;
            }
            case "R" -> {
                return Piece.WHITE_ROOK;
            }
            case "N" -> {
                return Piece.WHITE_KNIGHT;
            }
            case "B" -> {
                return Piece.WHITE_BISHOP;
            }
            case "Q" -> {
                return Piece.WHITE_QUEEN;
            }
            case "K" -> {
                return Piece.WHITE_KING;
            }
            case "p" -> {
                return Piece.BLACK_PAWN;
            }
            case "r" -> {
                return Piece.BLACK_ROOK;
            }
            case "n" -> {
                return Piece.BLACK_KNIGHT;
            }
            case "b" -> {
                return Piece.BLACK_BISHOP;
            }
            case "q" -> {
                return Piece.BLACK_QUEEN;
            }
            case "k" -> {
                return Piece.BLACK_KING;
            }
        }
        return null; // Devuelve null si no se encuentra la pieza
    }

    // Obtiene la notación de una pieza específica
    public static char getNotationPiece(Piece piece){
        return Piece.notation.get(piece);
    }

    // Obtiene la notación de la pieza en una posición dada (fila, columna)
    public static char getNotationPiece(int[] position){
        return Piece.notation.get(Square.piece.get(getSquare(position)));
    }

    // Reinicia las posiciones de las piezas en el tablero a la configuración inicial
    public static void resetSquares(){
        Square[] square = Square.values(); // Obtiene todas las casillas
        for (Square s : square) {
            String[] arr = s.toString().split(""); // Divide el nombre de la casilla en letra y número
            switch (arr[1]) { // Determina las piezas iniciales según la fila
                case "1": // Primera fila
                    switch (arr[0]) {
                        case "A", "H" -> Square.piece.put(s, Piece.WHITE_ROOK);
                        case "B", "G" -> Square.piece.put(s, Piece.WHITE_KNIGHT);
                        case "C", "F" -> Square.piece.put(s, Piece.WHITE_BISHOP);
                        case "D" -> Square.piece.put(s, Piece.WHITE_QUEEN);
                        case "E" -> Square.piece.put(s, Piece.WHITE_KING);
                    }
                    break;
                case "8": // Octava fila
                    switch (arr[0]) {
                        case "A", "H" -> Square.piece.put(s, Piece.BLACK_ROOK);
                        case "B", "G" -> Square.piece.put(s, Piece.BLACK_KNIGHT);
                        case "C", "F" -> Square.piece.put(s, Piece.BLACK_BISHOP);
                        case "D" -> Square.piece.put(s, Piece.BLACK_QUEEN);
                        case "E" -> Square.piece.put(s, Piece.BLACK_KING);
                    }
                    break;
                case "2": // Segunda fila, donde están los peones blancos
                    Square.piece.put(s, Piece.WHITE_PAWN);
                    break;
                case "7": // Séptima fila, donde están los peones negros
                    Square.piece.put(s, Piece.BLACK_PAWN);
                    break;
                default: // Otras casillas no tienen piezas
                    Square.piece.put(s, Piece.NONE);
                    break;
            }
        }
    }
}
