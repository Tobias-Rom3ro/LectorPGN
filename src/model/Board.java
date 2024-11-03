package model;

import utils.Method;

import java.util.ArrayList;
import java.util.Stack;

// Clase que representa el tablero de ajedrez, extiende la clase Method
public class Board extends Method {
    private ArrayList<Move> moves; // Lista de movimientos disponibles
    private final Stack<Move> movesPerformed; // Pila que guarda los movimientos que se han realizado
    static char[][] finalPositions; // Representación estática de las posiciones finales de las piezas
    private final ArrayList<Piece> capturedPieces; // Lista de piezas capturadas durante la partida

    public Board(){
        moves = new ArrayList<>(); // Inicializa la lista de movimientos
        movesPerformed = new Stack<>(); // Inicializa la pila de movimientos realizados
        finalPositions = buildFinalPosition(); // Construye la posición inicial del tablero
        capturedPieces = new ArrayList<>(); // Inicializa la lista de piezas capturadas
    }

    // Constructor que permite inicializar el tablero con una lista específica de movimientos
    public Board(ArrayList<Move> moves){
        this.setMoves(moves); // Establece los movimientos iniciales
        movesPerformed = new Stack<>(); // Inicializa la pila de movimientos realizados
        finalPositions = buildFinalPosition(); // Construye la posición inicial del tablero
        capturedPieces = new ArrayList<>(); // Inicializa la lista de piezas capturadas
    }

    // Método getter para obtener la lista de movimientos
    public ArrayList<Move> getMoves() {
        return moves; // Devuelve la lista de movimientos
    }

    // Método setter para establecer la lista de movimientos
    public void setMoves(ArrayList<Move> moves) {
        this.moves = moves; // Establece la lista de movimientos
    }

    // Método para realizar un movimiento en el tablero
    public void doMove(Move move){
        // Maneja la lógica para realizar un movimiento basado en el tipo de constructor del movimiento
        if(move.getTypeOfConstructor() == 1) // Movimiento normal
            movesPerformed.push(new Move(move.getFrom(), move.getTo(), move.getMovingPiece(), move.getStringMove()));
        else if(move.getTypeOfConstructor() == 2){ // Movimiento con captura
            movesPerformed.push(new Move(move.getFrom(), move.getTo(), move.getMovingPiece(), move.getCaptured(), move.getSpecialPawn(), false, move.getStringMove()));
            if(move.getCaptured() == Piece.NONE){ // Si no se capturó ninguna pieza
                if(move.getMovingPiece() == Piece.WHITE_PAWN)
                    capturedPieces.add(Piece.BLACK_PAWN); // Agrega peón negro a las piezas capturadas
                else
                    capturedPieces.add(Piece.WHITE_PAWN); // Agrega peón blanco a las piezas capturadas
            } else { // Si se capturó una pieza
                // Verifica si la pieza capturada está en una posición intermedia y no es un peón
                if((Method.getPosition(move.getTo())[0] != 0 && Method.getPosition(move.getTo())[0] != 7)
                        || (move.getCaptured() != Piece.WHITE_PAWN && move.getCaptured() != Piece.BLACK_PAWN))
                    capturedPieces.add(move.getCaptured()); // Agrega la pieza capturada a la lista
            }
        }
        else // Movimiento especial (enroque)
            movesPerformed.push(new Move(move.isWhite(), move.isKingSideCastle(), move.isQueenSideCastle(), false, move.getStringMove()));
    }

    // Método para deshacer el último movimiento realizado
    public void undoMove() {
        if (!movesPerformed.empty()) { // Verifica si hay movimientos para deshacer
            Move lastMove = movesPerformed.pop(); // Obtiene y elimina el último movimiento de la pila
            // Maneja la lógica para revertir el movimiento
            if (lastMove.getTypeOfConstructor() == 1)
                new Move(lastMove.getTo(), lastMove.getFrom(), lastMove.getMovingPiece(), lastMove.getStringMove());
            else if (lastMove.getTypeOfConstructor() == 2) {
                new Move(lastMove.getFrom(), lastMove.getTo(), lastMove.getMovingPiece(), lastMove.getCaptured(), lastMove.getSpecialPawn(), true, lastMove.getStringMove());
                // Lógica para revertir la captura de piezas
                if (lastMove.getCaptured() == Piece.NONE) {
                    if (lastMove.getMovingPiece() == Piece.WHITE_PAWN)
                        capturedPieces.remove(Piece.BLACK_PAWN); // Remueve peón negro de las piezas capturadas
                    else
                        capturedPieces.remove(Piece.WHITE_PAWN); // Remueve peón blanco de las piezas capturadas
                } else {
                    // Verifica si la pieza capturada estaba en una posición intermedia y no era un peón
                    if((Method.getPosition(lastMove.getTo())[0] != 0 && Method.getPosition(lastMove.getTo())[0] != 7)
                            || (lastMove.getCaptured() != Piece.WHITE_PAWN && lastMove.getCaptured() != Piece.BLACK_PAWN))
                        capturedPieces.remove(lastMove.getCaptured()); // Remueve la pieza capturada de la lista
                }
            } else
                new Move(lastMove.isWhite(), lastMove.isKingSideCastle(), lastMove.isQueenSideCastle(), true, lastMove.getStringMove()); // Manejo del enroque
        }
    }

    // Método para obtener la lista de piezas capturadas
    public ArrayList<Piece> capturedPieces(){
        return capturedPieces; // Devuelve la lista de piezas capturadas
    }

    // Método para reiniciar el tablero a su estado inicial
    public void resetBoard(){
        finalPositions = buildFinalPosition(); // Reinicia la posición del tablero
        movesPerformed.removeAllElements(); // Limpia la pila de movimientos realizados
        capturedPieces.clear(); // Limpia la lista de piezas capturadas
    }

    // Método privado para construir la representación de las posiciones finales del tablero
    private char[][] buildFinalPosition(){
        Method.resetSquares(); // Reinicia las casillas
        char[][] finalPosition = new char[8][8]; // Crea la matriz para las posiciones finales
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                finalPosition[i][j] = Method.getNotationPiece(new int[]{i, j}); // Obtiene la notación de la pieza en la posición actual
            }
        }
        return finalPosition; // Devuelve la matriz de posiciones finales
    }

    // Método para obtener una representación en cadena del estado del tablero
    @Override
    public String toString(){
        StringBuilder result = new StringBuilder(); // Crea un StringBuilder para construir la representación
        for (int i = 7; i >= 0; i--) { // Recorre las filas desde la última hasta la primera
            for (int j = 0; j < 8; j++)
                result.append(finalPositions[i][j]); // Agrega la notación de la pieza a la representación
            if(i != 0)
                result.append("\n"); // Agrega un salto de línea si no es la última fila
            else
                result.append("\nSide: WHITE"); // Indica el lado que juega
        }
        return result.toString(); // Devuelve la representación en cadena del estado del tablero
    }
}
