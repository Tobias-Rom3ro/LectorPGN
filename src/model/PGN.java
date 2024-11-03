package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

// Clase que lee los archivos PGN.
public class PGN {
    private ArrayList<Game> games; // Lista para almacenar los juegos leídos desde el archivo PGN
    private String path; // Ruta del archivo PGN

    // Constructor que inicializa la ruta del archivo y carga los juegos
    public PGN(String path){
        this.setPath(path);
        games = new ArrayList<>();
        addAdvanced(); // Llama a la función que carga los juegos desde el archivo
    }

    // Getters y setters para la ruta del archivo y la lista de juegos
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Game> getGames() {
        return games; // Retorna la lista de juegos
    }

    /**
     * Lee el archivo PGN y devuelve una lista de cadenas con las etiquetas y movimientos de los juegos.
     */
    private ArrayList<String> read(String path){
        File f = new File(path); // Crea un objeto File a partir de la ruta
        ArrayList<String> lines = new ArrayList<>(); // Lista para almacenar las líneas leídas
        try {
            BufferedReader br = new BufferedReader(new FileReader(f)); // Lee el archivo línea por línea
            String line;
            long countEmpty = 0L; // Contador para líneas vacías
            StringBuilder gameLine = new StringBuilder(); // Almacena líneas de movimientos del juego

            while((line = br.readLine()) != null){
                if(line.equals("")) {
                    countEmpty++; // Incrementa el contador si la línea está vacía
                    continue; // Salta a la siguiente iteración si la línea está vacía
                }
                if (line.contains("["))
                    lines.add(line); // Agrega etiquetas a la lista

                // Maneja las líneas de movimientos y separación entre juegos
                if(countEmpty % 2 == 1) {
                    gameLine = (gameLine == null ? new StringBuilder() : gameLine).append(line);
                    gameLine.append(" ");
                }
                else if(countEmpty > 1) {
                    if (gameLine != null) {
                        lines.add(gameLine.toString());
                        gameLine = null;
                        lines.add("endOfGame"); // Marca el final de un juego
                    }
                }
            }
            // Agrega cualquier línea de movimiento restante al final
            if (gameLine != null) {
                lines.add(gameLine.toString());
                lines.add("endOfGame");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo básico de excepciones
        }
        return lines; // Retorna la lista de líneas leídas
    }

    /**
     * Prepara un objeto Game y lo agrega a la lista de juegos.
     */
    void addAdvanced(){
        ArrayList<String> lines = read(getPath()); // Lee las líneas del archivo
        // Inicializa variables para almacenar la información del juego
        String event = null, site = null, date = null, round = null, whitePlayer = null,
                blackPlayer = null, result = null, whiteTitle = null, blackTitle = null, whiteElo = null,
                blackElo = null, eco = null, opening = null, variation = null, whiteFideId = null,
                blackFideId = null, eventDate = null, termination = null, strMovesText = null;
        String[] movesArray = new String[0];

        // Procesa cada línea para extraer información del juego
        for(String line: lines){
            // Manejo de las diferentes etiquetas del PGN
            if(line.contains("[EventDate ")) {
                eventDate = line.replaceAll("\\[EventDate \"", "").replaceAll("\"]", "");
            }
                else if(line.contains("[Event ")) {
                    event = line.replaceAll("\\[Event \"", "");
                    event = event.replaceAll("\"]", "");
                }
                else if(line.contains("[Site ")) {
                    site = line.replaceAll("\\[Site \"", "");
                    site = site.replaceAll("\"]", "");
                }
                else if(line.contains("[Date ")) {
                    date = line.replaceAll("\\[Date \"", "");
                    date = date.replaceAll("\"]", "");
                }
                else if(line.contains("[Round ")) {
                    round = line.replaceAll("\\[Round \"", "");
                    round = round.replaceAll("\"]", "");
                }
                else if(line.contains("[WhiteElo ")) {
                    whiteElo = line.replaceAll("\\[WhiteElo \"", "");
                    whiteElo = whiteElo.replaceAll("\"]", "");
                }
                else if(line.contains("[BlackElo ")) {
                    blackElo = line.replaceAll("\\[BlackElo \"", "");
                    blackElo = blackElo.replaceAll("\"]", "");
                }
                else if(line.contains("[WhiteTitle ")) {
                    whiteTitle = line.replaceAll("\\[WhiteTitle \"", "");
                    whiteTitle = whiteTitle.replaceAll("\"]", "");
                }
                else if(line.contains("[BlackTitle ")) {
                    blackTitle = line.replaceAll("\\[BlackTitle \"", "");
                    blackTitle = blackTitle.replaceAll("\"]", "");
                }
                else if(line.contains("[WhiteFideId ")) {
                    whiteFideId = line.replaceAll("\\[WhiteFideId \"", "");
                    whiteFideId = whiteFideId.replaceAll("\"]", "");
                }
                else if(line.contains("[BlackFideId ")) {
                    blackFideId = line.replaceAll("\\[BlackFideId \"", "");
                    blackFideId = blackFideId.replaceAll("\"]", "");
                }
                else if(line.contains("[White ")) {
                    whitePlayer = line.replaceAll("\\[White \"", "");
                    whitePlayer = whitePlayer.replaceAll("\"]", "");
                }
                else if(line.contains("[Black ")) {
                    blackPlayer = line.replaceAll("\\[Black \"", "");
                    blackPlayer = blackPlayer.replaceAll("\"]", "");
                }
                else if(line.contains("[Opening ")) {
                    opening = line.replaceAll("\\[Opening \"", "");
                    opening = opening.replaceAll("\"]", "");
                }
                else if(line.contains("[Result ")) {
                    result = line.replaceAll("\\[Result \"", "");
                    result = result.replaceAll("\"]", "");
                }
                else if(line.contains("[ECO ")) {
                    eco = line.replaceAll("\\[ECO \"", "");
                    eco = eco.replaceAll("\"]", "");
                }
                else if(line.contains("[Variation ")) {
                    variation = line.replaceAll("\\[Variation \"", "");
                    variation = variation.replaceAll("\"]", "");
                }
                else if(line.contains("[Termination ")) {
                    termination = line.replaceAll("\\[Termination \"", "");
                    termination = termination.replaceAll("\"]", "");
                }

            else if(line.contains("endOfGame")){ // Al llegar al final de un juego, crea un objeto Game
                add(new Game(event, site, date, round, whitePlayer, blackPlayer, result,
                        whiteTitle, blackTitle, whiteElo, blackElo, eco, opening, variation,
                        whiteFideId, blackFideId, eventDate, termination, new Board(), strMovesText, movesArray));
            }
            else {
                // Maneja las líneas de movimientos
                strMovesText = line;
                strMovesText = strMovesText.replaceAll(" e.p.", "");
                movesArray = preparingMoves(line); // Llama a la función que prepara los movimientos
            }
        }
    }

    /**
     * Agrega un objeto Game a la lista de juegos.
     */
    void add(Game g){
        games.add(g); // Agrega el juego a la lista
    }

    /**
     * Elimina un juego de la lista de juegos.
     * @param g El juego a eliminar
     */
    void remove(Game g) {
        games.remove(g); // Elimina el juego especificado
    }

    /**
     * Elimina notaciones innecesarias en las cadenas de movimientos y separa cada movimiento en una cadena.
     */
    String[] preparingMoves(String movesLine){
        // Limpieza de notaciones específicas de los movimientos
        movesLine = movesLine.replaceAll("\\?", "").replaceAll("!", "").replaceAll("#", "")
                .replaceAll("\\+", "").replaceAll("1-0", "").replaceAll("0-1", "")
                .replaceAll("1/2-1/2", "").replaceAll("\\*", "").replaceAll(" e.p.", "");

        String[] movesArray = movesLine.split(" "); // Divide la línea de movimientos en un arreglo
        // Filtra y limpia los movimientos en el arreglo
        for (int i = 0; i < movesArray.length; i++) {
            if (movesLine.charAt(2) == ' ') {
                if (i % 3 == 0)
                    movesArray[i] = null; // Elimina el movimiento correspondiente si está vacío
            } else {
                if (i % 2 == 0)
                    movesArray[i] = movesArray[i].substring(movesArray[i].indexOf('.') + 1); // Ajusta el formato
            }
        }
        // Filtra los elementos nulos del arreglo
        movesArray = Arrays.stream(movesArray).filter(Objects::nonNull).toArray(String[]::new);

        return movesArray; // Retorna el arreglo de movimientos preparado
    }
}
