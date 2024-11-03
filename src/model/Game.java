package model;

import java.util.LinkedHashMap;

public class Game {
    public Board board; // Instancia del tablero de ajedrez
    private final LinkedHashMap<String, String> tags; // Mapa que almacena información sobre la partida
    private String[] stringMovesArray; // Array que contiene los movimientos en formato de texto
    private String strMovesText; // Texto que representa todos los movimientos de la partida

    // Constructor que inicializa todos los campos relevantes para la partida
    public Game(String event, String site, String date, String round, String whitePlayer, String blackPlayer,
                String result, String whiteTitle, String blackTitle, String whiteElo, String blackElo, String eco,
                String opening, String variation, String whiteFideId, String blackFideId, String eventDate,
                String termination, Board board, String strMovesText, String[] stringMovesArray) {
        tags = new LinkedHashMap<>(); // Inicializa el mapa de tags
        this.setEvent(event); // Establece el evento
        this.setSite(site); // Establece el sitio
        this.setDate(date); // Establece la fecha
        this.setRound(round); // Establece la ronda
        this.setWhitePlayer(whitePlayer); // Establece el jugador blanco
        this.setBlackPlayer(blackPlayer); // Establece el jugador negro
        this.setResult(result); // Establece el resultado de la partida
        this.setWhiteTitle(whiteTitle); // Establece el título del jugador blanco
        this.setBlackTitle(blackTitle); // Establece el título del jugador negro
        this.setWhiteElo(whiteElo); // Establece el Elo del jugador blanco
        this.setBlackElo(blackElo); // Establece el Elo del jugador negro
        this.setEco(eco); // Establece el código ECO
        this.setOpening(opening); // Establece la apertura
        this.setVariation(variation); // Establece la variación
        this.setWhiteFideId(whiteFideId); // Establece el ID FIDE del jugador blanco
        this.setBlackFideId(blackFideId); // Establece el ID FIDE del jugador negro
        this.setEventDate(eventDate); // Establece la fecha del evento
        this.setTermination(termination); // Establece la terminación de la partida
        this.setStringMovesArray(stringMovesArray); // Establece el array de movimientos
        this.board = board; // Asocia el tablero a la partida
        this.setStrMovesText(strMovesText); // Establece el texto de movimientos
    }

    // Métodos setter para actualizar los tags
    public void setEvent(String event) {
        tags.put("Event", event);
    }

    public void setSite(String site) {
        tags.put("Site", site);
    }

    public void setDate(String date) {
        tags.put("Date", date);
    }

    public void setRound(String round) {
        tags.put("Round", round);
    }

    public void setWhiteElo(String whiteElo) {
        tags.put("White Elo", whiteElo);
    }

    public void setBlackElo(String blackElo) {
        tags.put("Black Elo", blackElo);
    }

    public void setEco(String eco) {
        tags.put("ECO", eco);
    }

    public String getWhitePlayer() {
        return tags.get("White"); // Devuelve el jugador blanco
    }

    public void setWhitePlayer(String whitePlayer) {
        tags.put("White", whitePlayer); // Establece el jugador blanco
    }

    public String getBlackPlayer() {
        return tags.get("Black"); // Devuelve el jugador negro
    }

    public void setBlackPlayer(String blackPlayer) {
        tags.put("Black", blackPlayer); // Establece el jugador negro
    }

    public String getOpening() {
        return tags.get("Opening"); // Devuelve la apertura
    }

    public void setOpening(String opening) {
        tags.put("Opening", opening); // Establece la apertura
    }

    public String getResult() {
        return tags.get("Result"); // Devuelve el resultado de la partida
    }

    public void setResult(String result) {
        tags.put("Result", result); // Establece el resultado de la partida
    }

    public void setWhiteTitle(String whiteTitle) {
        tags.put("White Title", whiteTitle); // Establece el título del jugador blanco
    }

    public void setBlackTitle(String blackTitle) {
        tags.put("Black Title", blackTitle); // Establece el título del jugador negro
    }

    public void setWhiteFideId(String whiteFideId) {
        tags.put("White Fide Id", whiteFideId); // Establece el ID FIDE del jugador blanco
    }

    public void setBlackFideId(String blackFideId) {
        tags.put("Black Fide Id", blackFideId); // Establece el ID FIDE del jugador negro
    }

    public void setVariation(String variation) {
        tags.put("Variation", variation); // Establece la variación de la apertura
    }

    public void setEventDate(String eventDate) {
        tags.put("Event Date", eventDate); // Establece la fecha del evento
    }

    public void setTermination(String termination) {
        tags.put("Termination", termination); // Establece la terminación de la partida
    }

    public String getStrMovesText() {
        return strMovesText; // Devuelve el texto de movimientos
    }

    public void setStrMovesText(String strMovesText) {
        this.strMovesText = strMovesText; // Establece el texto de movimientos
    }

    public String[] getStringMovesArray() {
        return stringMovesArray; // Devuelve el array de movimientos
    }

    public void setStringMovesArray(String[] stringMovesArray) {
        this.stringMovesArray = stringMovesArray; // Establece el array de movimientos
    }

    // Método para representar la partida en forma de cadena
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(); // Crea un StringBuilder para construir la salida
        for (String tag : tags.keySet()) {
            if (tags.get(tag) != null)
                result.append(tag).append(":   ").append(tags.get(tag)).append("\n"); // Agrega cada tag y su valor
        }
        // Elimina el último salto de línea extra si existe
        if (result.toString().contains("\n")) {
            int lastNextLine = result.lastIndexOf("\n");
            return result.delete(lastNextLine, lastNextLine + 2).toString(); // Retorna la cadena sin el salto final
        } else {
            return result.toString(); // Retorna la cadena construida
        }
    }
}
