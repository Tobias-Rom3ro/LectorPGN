package model;

import utils.*;

// Clase que representa un movimiento en el juego de ajedrez
public class Move extends Method {
    private Square from; // Cuadro de origen del movimiento
    private Square to; // Cuadro de destino del movimiento
    private Piece movingPiece; // Pieza que se está moviendo
    private Piece captured; // Pieza capturada en el movimiento
    private Piece specialPawn; // Pieza especial (para promociones de peón)
    private boolean white; // Indica si es el turno de las piezas blancas
    private boolean kingSideCastle; // Indica si es un enroque por el lado del rey
    private boolean queenSideCastle; // Indica si es un enroque por el lado de la reina
    private boolean reverse; // Indica si el movimiento es revertido
    private int typeOfConstructor; // Tipo de constructor utilizado para crear el movimiento
    private String stringMove; // Representación en texto del movimiento

    // Constructor para un movimiento básico
    public Move(Square s1, Square s2, Piece movingPiece, String stringMove){
        this.setFrom(s1);
        this.setTo(s2);
        this.setMovingPiece(movingPiece);
        this.setTypeOfConstructor(1);
        if(stringMove != null)
            this.setStringMove(stringMove);

        setNewPosition(s1, Piece.NONE);
        setNewPosition(s2, movingPiece);
    }

    // Constructor para un movimiento que puede incluir captura y peón especial
    public Move(Square s1, Square s2, Piece movingPiece, Piece captured, Piece specialPawn, boolean reverse, String stringMove){
        this.setFrom(s1);
        this.setTo(s2);
        this.setMovingPiece(movingPiece);
        this.setCaptured(captured);
        this.setSpecialPawn(specialPawn);
        this.setReverse(reverse);
        this.setTypeOfConstructor(2);
        this.setStringMove(stringMove);

        if(notReverse())
            new Move(s1, s2, movingPiece, null);
        else {
            if ((Method.getPosition(s1)[0] != 0 && Method.getPosition(s2)[0] != 7)
                    || (captured != Piece.WHITE_PAWN && captured != Piece.BLACK_PAWN)) {
                setNewPosition(s2, captured);
                if (specialPawn == Piece.NONE)
                    setNewPosition(s1, movingPiece);
                else
                    setNewPosition(s1, specialPawn);
            } else {
                setNewPosition(s1, captured);
                setNewPosition(s2, Piece.NONE);
            }
        }

        if (captured == Piece.NONE) {
            if (movingPiece == Piece.WHITE_PAWN) {
                int[] enPassant = new int[]{Method.getPosition(s2)[0] - 1, Method.getPosition(s2)[1]};
                if(notReverse())
                    setNewPosition(enPassant, Piece.NONE);
                else
                    setNewPosition(enPassant, Piece.BLACK_PAWN);
            }
            else if (movingPiece == Piece.BLACK_PAWN) {
                int[] enPassant = new int[]{Method.getPosition(s2)[0] + 1, Method.getPosition(s2)[1]};
                if(notReverse())
                    setNewPosition(enPassant, Piece.NONE);
                else
                    setNewPosition(enPassant, Piece.WHITE_PAWN);
            }
        }
    }


    // Constructor para movimientos de enroque
    public Move(boolean white, boolean kingSideCastle, boolean queenSideCastle, boolean reverse, String stringMove){
        this.setWhite(white);
        this.setKingSideCastle(kingSideCastle);
        this.setQueenSideCastle(queenSideCastle);
        this.setReverse(reverse);
        this.setTypeOfConstructor(3);
        this.setStringMove(stringMove);
        if (white) {
            if (kingSideCastle) {
                if(notReverse()) {
                    new Move(Square.E1, Square.G1, Piece.WHITE_KING, null);
                    new Move(Square.H1, Square.F1, Piece.WHITE_ROOK, null);
                } else {
                    new Move(Square.G1, Square.E1, Piece.WHITE_KING, null);
                    new Move(Square.F1, Square.H1, Piece.WHITE_ROOK, null);
                }
            }
            if (queenSideCastle) {
                if(notReverse()) {
                    new Move(Square.E1, Square.C1, Piece.WHITE_KING, null);
                    new Move(Square.A1, Square.D1, Piece.WHITE_ROOK, null);
                } else {
                    new Move(Square.C1, Square.E1, Piece.WHITE_KING, null);
                    new Move(Square.D1, Square.A1, Piece.WHITE_ROOK, null);
                }
            }
        } else {
            if (kingSideCastle) {
                if(notReverse()) {
                    new Move(Square.E8, Square.G8, Piece.BLACK_KING, null);
                    new Move(Square.H8, Square.F8, Piece.BLACK_ROOK, null);
                } else {
                    new Move(Square.G8, Square.E8, Piece.BLACK_KING, null);
                    new Move(Square.F8, Square.H8, Piece.BLACK_ROOK, null);
                }
            }
            if (queenSideCastle) {
                if(notReverse()) {
                    new Move(Square.E8, Square.C8, Piece.BLACK_KING, null);
                    new Move(Square.A8, Square.D8, Piece.BLACK_ROOK, null);
                } else {
                    new Move(Square.C8, Square.E8, Piece.BLACK_KING, null);
                    new Move(Square.D8, Square.A8, Piece.BLACK_ROOK, null);
                }
            }
        }

    }

    public Square getFrom() {
        return from;
    }

    public void setFrom(Square from) {
        this.from = from;
    }

    public Square getTo() {
        return to;
    }

    public void setTo(Square to) {
        this.to = to;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public void setMovingPiece(Piece movingPiece) {
        this.movingPiece = movingPiece;
    }

    public Piece getCaptured() {
        return captured;
    }

    public void setCaptured(Piece captured) {
        this.captured = captured;
    }

    public Piece getSpecialPawn() {
        return specialPawn;
    }

    public void setSpecialPawn(Piece specialPawn) {
        this.specialPawn = specialPawn;
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean isKingSideCastle() {
        return kingSideCastle;
    }

    public void setKingSideCastle(boolean kingSideCastle) {
        this.kingSideCastle = kingSideCastle;
    }

    public boolean isQueenSideCastle() {
        return queenSideCastle;
    }

    public void setQueenSideCastle(boolean queenSideCastle) {
        this.queenSideCastle = queenSideCastle;
    }

    public int getTypeOfConstructor() {
        return typeOfConstructor;
    }

    public void setTypeOfConstructor(int typeOfConstructor) {
        this.typeOfConstructor = typeOfConstructor;
    }

    public boolean notReverse() {
        return !reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public String getStringMove() {
        return stringMove;
    }

    public void setStringMove(String stringMove) {
        this.stringMove = stringMove;
    }

    // Establece una nueva posición de una pieza en el cuadrado dado
    private void setNewPosition(Square square, Piece piece){
        int[] position = Method.getPosition(square);
        Board.finalPositions[position[0]][position[1]] = Method.getNotationPiece(piece);
        Square.piece.put(square, piece);
    }

    // Devuelve el nombre de la pieza dada
    private void setNewPosition(int[] position, Piece piece){
        Board.finalPositions[position[0]][position[1]] = Method.getNotationPiece(piece);
        Square.piece.put(Method.getSquare(position), piece);
    }

    private String stringPiece(Piece piece){
        switch (piece) {
            case WHITE_ROOK -> {
                return "White Rook";
            }
            case WHITE_KING -> {
                return "White King";
            }
            case WHITE_PAWN -> {
                return "White Pawn";
            }
            case WHITE_QUEEN -> {
                return "White Queen";
            }
            case WHITE_BISHOP -> {
                return "White Bishop";
            }
            case WHITE_KNIGHT -> {
                return "White Knight";
            }
            case BLACK_KING -> {
                return "Black King";
            }
            case BLACK_PAWN -> {
                return "Black Pawn";
            }
            case BLACK_ROOK -> {
                return "Black Rook";
            }
            case BLACK_QUEEN -> {
                return "Black Queen";
            }
            case BLACK_BISHOP -> {
                return "Black Bishop";
            }
            default -> {
                return "Black Knight";
            }
        }
    }

    @Override
    public String toString() {
        return getStringMove();
    }


    @Override
    public boolean equals(Object o){
        if(o.getClass() == this.getClass()) {
            Move m = (Move) o;
            if (this.getTypeOfConstructor() == m.getTypeOfConstructor()) {
                if (this.getTypeOfConstructor() == 1 && this.getFrom() == m.getFrom() && this.getTo() == m.getTo()
                        && this.getMovingPiece() == m.getMovingPiece())
                    return true;
                else if (this.getTypeOfConstructor() == 2 && this.getFrom() == m.getFrom() && this.getTo() == m.getTo()
                    && this.getMovingPiece() == m.getMovingPiece()) {
                    if(this.getSpecialPawn() == m.getSpecialPawn() && this.getSpecialPawn() == Piece.NONE) {
                        if ((Method.getPosition(getTo())[0] != 0 && Method.getPosition(getTo())[0] != 7)
                                || (getCaptured() != Piece.WHITE_PAWN && getCaptured() != Piece.BLACK_PAWN)) {
                            return true;
                        } else {
                            return this.getCaptured() == m.getCaptured();
                        }
                    } else return this.getSpecialPawn() == m.getSpecialPawn() && this.getCaptured() == m.getCaptured();
                }
                else return this.getTypeOfConstructor() == 3 && this.isWhite() == m.isWhite()
                            && this.isKingSideCastle() == m.isKingSideCastle()
                            && this.isQueenSideCastle() == m.isQueenSideCastle();
            } else
                return false;
        } else
            return false;
    }
}
