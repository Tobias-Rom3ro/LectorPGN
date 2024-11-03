package controller;

import model.*;
import utils.Converter;
import view.View;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private final View view; // Referencia a la vista
    private PGN pgn; // Objeto PGN que representa un archivo de juego
    private Game game; // Objeto que representa el juego actual
    private boolean isSelected; // Indica si se ha seleccionado un archivo PGN
    private boolean isMove; // Indica si se está realizando un movimiento
    private boolean isPlay; // Indica si el juego se está reproduciendo
    private int indexOfMove; // Índice del movimiento actual
    private int indexOfGame; // Índice del juego actual
    private Timer timer; // Temporizador para la reproducción automática
    private boolean notFlip; // Indica si el tablero debe ser invertido

    public Controller(View view) {
        this.view = view; // Inicializa la vista
        this.isSelected = false; // Inicializa el estado de selección
        this.notFlip = true; // Inicializa el estado de giro
        this.indexOfGame = 0; // Inicializa el índice de juego
        this.isMove = false; // Inicializa el estado de movimiento
        this.isPlay = false; // Inicializa el estado de reproducción
        this.indexOfMove = -1; // Inicializa el índice de movimiento

        initTimer(); // Inicializa el temporizador
        initButtonListeners(); // Inicializa los escuchadores de botones
    }

    private void initTimer() {
        timer = new Timer(750, this); // Temporizador que se dispara cada 750 ms
        timer.start(); // Inicia el temporizador
    }

    private void initButtonListeners() {
        // Configura los escuchadores de eventos para los botones
        view.getBtnPgn().addActionListener(e -> handleOpenPGNFile());
        view.getBtnReset().addActionListener(e -> handleReset());
        view.getBtnEnd().addActionListener(e -> handleEnd());
        view.getBtnPreMove().addActionListener(e -> handlePreviousMove());
        view.getBtnNxtMove().addActionListener(e -> handleNextMove());
        view.getBtnSearchMove().addActionListener(e -> handleSearchMove());
        view.getBtnPlay().addActionListener(e -> handlePlay());
    }

    private void handleOpenPGNFile() {
        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); // Abre el selector de archivos
        j.setAcceptAllFileFilterUsed(false);
        j.setDialogTitle("Selecciona un archivo .pgn");
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Solamente archivos .pgn", "pgn");
        j.addChoosableFileFilter(restrict);
        int r = j.showOpenDialog(null); // Muestra el diálogo de abrir archivo
        if (r == JFileChooser.APPROVE_OPTION) { // Si se selecciona un archivo
            indexOfGame = 0; // Reinicia el índice de juego
            pgn = new PGN(j.getSelectedFile().getAbsolutePath()); // Crea un objeto PGN
            isSelected = true; // Marca el archivo como seleccionado

            // Asigna el nombre del archivo a la etiqueta
            String nameOfFile = j.getSelectedFile().getName();
            view.getPgnLabel().setText(nameOfFile.substring(0, nameOfFile.length()-4));

            setGameButton(); // Configura el botón del juego
        }
    }

    private void handleReset() {
        if(isSelected) { // Verifica si un archivo ha sido seleccionado
            isMove = false; // Restablece el estado de movimiento
            isPlay = false; // Restablece el estado de reproducción
            indexOfMove = -1; // Reinicia el índice de movimiento

            // Reinicia el tablero
            game.board.resetBoard();

            // Actualiza las etiquetas
            int size = game.board.getMoves().size();
            size = (size / 2) + (size % 2);
            view.getMoveLabel().setText("Movimiento " + (indexOfMove + 1) + " de " + size);
            view.getColorLabel().setText("");

            view.getBtnPlay().setText("Reproducir");

            highlightMove(); // Resalta el movimiento actual
            view.repaint(); // Redibuja la vista
        }
    }

    private void handleEnd() {
        if(isSelected && indexOfMove < game.board.getMoves().size() - 1) {
            isMove = true; // Cambia el estado a movimiento
            isPlay = false; // Cambia el estado a no reproducción
            indexOfMove = game.board.getMoves().size() - 1; // Mueve al final de la lista de movimientos

            for(int i = 0; i <= indexOfMove; i++)
                game.board.doMove(game.board.getMoves().get(i)); // Realiza todos los movimientos

            int size = game.board.getMoves().size();
            size = (size / 2) + (size % 2);
            if(indexOfMove + 1 != 0 && (indexOfMove + 1) % 2 == 1) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                view.getColorLabel().setText("Blanca");
            }
            else if(indexOfMove + 1 != 0) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                view.getColorLabel().setText("Negra");
            }

            view.getBtnPlay().setText("Reproducir");

            highlightMove(); // Resalta el movimiento actual
            view.repaint(); // Redibuja la vista
        }
    }

    private void handlePreviousMove() {
        if(isSelected && indexOfMove >= 0) {
            isMove = true; // Cambia el estado a movimiento
            isPlay = false; // Cambia el estado a no reproducción
            indexOfMove--; // Retrocede en los movimientos

            game.board.undoMove(); // Deshace el movimiento

            int size = game.board.getMoves().size();
            size = (size / 2) + (size % 2);
            if(indexOfMove + 1 != 0 && (indexOfMove + 1) % 2 == 1) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                view.getColorLabel().setText("Blanca");
            }
            else if(indexOfMove + 1 != 0) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                view.getColorLabel().setText("Negra");
            }
            else {
                view.getMoveLabel().setText("Movimiento 0 de " + size);
                view.getColorLabel().setText("");
            }

            view.getBtnPlay().setText("Reproducir");

            highlightMove(); // Resalta el movimiento actual
            view.repaint(); // Redibuja la vista
        }
    }

    private void handleNextMove() {
        if(isSelected && indexOfMove < game.board.getMoves().size() - 1) {
            isMove = true; // Cambia el estado a movimiento
            isPlay = false; // Cambia el estado a no reproducción
            indexOfMove++; // Avanza en los movimientos

            game.board.doMove(game.board.getMoves().get(indexOfMove)); // Realiza el movimiento

            int size = game.board.getMoves().size();
            size = (size / 2) + (size % 2);
            if(indexOfMove + 1 != 0 && (indexOfMove + 1) % 2 == 1) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                view.getColorLabel().setText("Blanca");
            }
            else if(indexOfMove + 1 != 0) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                view.getColorLabel().setText("Negra");
            }

            view.getBtnPlay().setText("Reproducir");

            highlightMove(); // Resalta el movimiento actual
            view.repaint(); // Redibuja la vista
        }
    }

    private void handleSearchMove() {
        if(isSelected && view.getSearchMove().getText() != null) {
            int index = (Integer.parseInt(view.getSearchMove().getText()) - 1) * 2; // Obtiene el índice del movimiento
            if(index >= 0 && index < game.board.getMoves().size()) {
                if(view.getBlackRadio().isSelected())
                    index++; // Si está seleccionado el botón de negras, incrementa el índice
                if(index > indexOfMove) { // Si el índice es mayor que el movimiento actual
                    for (int i = indexOfMove + 1; i <= index; i++)
                        game.board.doMove(game.board.getMoves().get(i)); // Realiza los movimientos hasta el índice
                    indexOfMove = index; // Actualiza el índice del movimiento
                }
                else if(index < indexOfMove) { // Si el índice es menor que el movimiento actual
                    while (index != indexOfMove) {
                        indexOfMove--; // Retrocede en los movimientos
                        game.board.undoMove(); // Deshace el movimiento
                    }
                }
                isMove = true; // Cambia el estado a movimiento
                int size = game.board.getMoves().size();
                size = (size / 2) + (size % 2);
                if(indexOfMove + 1 != 0 && (indexOfMove + 1) % 2 == 1) {
                    view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                    view.getColorLabel().setText("Blanca");
                }
                else if(indexOfMove + 1 != 0) {
                    view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                    view.getColorLabel().setText("Negra");
                }
            }
            else if(index == -2) { // Manejo especial para el índice -2
                indexOfMove = -1;
                isMove = false;
                game.board.resetBoard(); // Reinicia el tablero
                int size = game.board.getMoves().size();
                size = (size / 2) + (size % 2);
                view.getMoveLabel().setText("Movimiento " + (indexOfMove + 1) + " de " + size);
                view.getColorLabel().setText("");
            }
            isPlay = false; // Cambia el estado a no reproducción
            view.getBtnPlay().setText("Reproducir");
            highlightMove(); // Resalta el movimiento actual
            view.repaint(); // Redibuja la vista
        }
    }

    private void handlePlay() {
        if(isSelected && indexOfMove < game.board.getMoves().size() - 1) {
            isPlay = !isPlay; // Cambia el estado de reproducción
            if (isPlay)
                view.getBtnPlay().setText("Pausa"); // Actualiza el texto del botón
            else
                view.getBtnPlay().setText("Reproducir");
        }
    }

    private void setGameButton() {
        if (pgn != null && !pgn.getGames().isEmpty()) { // Verifica si hay juegos disponibles en el PGN
            isMove = false; // Restablece el estado de movimiento
            isPlay = false; // Restablece el estado de reproducción
            indexOfMove = -1; // Reinicia el índice de movimiento

            game = pgn.getGames().get(indexOfGame); // Obtiene el juego actual
            game.board = new Board(Converter.convertMoves(game.getStringMovesArray())); // Inicializa el tablero con los movimientos
            game.board.resetBoard(); // Reinicia el tablero

            view.getTagsArea().setText(game.toString()); // Muestra las etiquetas del juego
            view.getMovesArea().setText(game.getStrMovesText()); // Muestra los movimientos del juego
            view.getBtnPlay().setText("Reproducir");

            view.repaint(); // Redibuja la vista
        }
    }

    private void play() {
        if(isSelected && indexOfMove < game.board.getMoves().size() - 1) {
            indexOfMove++; // Avanza en el índice de movimiento
            isMove = true; // Cambia el estado a movimiento
            game.board.doMove(game.board.getMoves().get(indexOfMove)); // Realiza el movimiento
            int size = game.board.getMoves().size();
            size = (size / 2) + (size % 2);
            if(indexOfMove + 1 != 0 && (indexOfMove + 1) % 2 == 1) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                view.getColorLabel().setText("Blanca");
            }
            else if(indexOfMove + 1 != 0) {
                view.getMoveLabel().setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                view.getColorLabel().setText("Negra");
            }
            highlightMove(); // Resalta el movimiento actual
        }
    }

    private void highlightMove() {
        JTextArea movesArea = view.getMovesArea(); // Obtiene el área de texto de movimientos
        Highlighter highlighter = movesArea.getHighlighter(); // Obtiene el resaltador
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(231, 231, 0, 255)); // Define el color del resaltador
        highlighter.removeAllHighlights(); // Elimina resaltados anteriores
        if(indexOfMove >= 0) {
            int indexOfMoveText;
            if((indexOfMove + 1) % 2 == 1) {
                indexOfMoveText = movesArea.getText().indexOf(String.valueOf((indexOfMove + 2) / 2)); // Encuentra el índice del texto
            }
            else {
                indexOfMoveText = movesArea.getText().indexOf(String.valueOf((indexOfMove + 1) / 2));
            }
            if(view.getColorLabel().getText().equals("Negra"))
                indexOfMoveText += game.board.getMoves().get(indexOfMove - 1).getStringMove().length(); // Ajusta el índice si es un movimiento de negras
            String strMove;
            strMove = game.board.getMoves().get(indexOfMove).getStringMove(); // Obtiene el movimiento actual
            int p0 = movesArea.getText().indexOf(strMove, indexOfMoveText); // Encuentra el inicio del movimiento
            int p1 = p0 + strMove.length(); // Encuentra el final del movimiento
            try {
                highlighter.addHighlight(p0, p1, painter); // Resalta el movimiento
            } catch (BadLocationException e) {
                e.printStackTrace(); // Maneja excepciones
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isPlay) { // Si se está reproduciendo
            play(); // Ejecuta el siguiente movimiento
            if(indexOfMove == game.board.getMoves().size() - 1) { // Si se ha llegado al final de los movimientos
                isPlay = false; // Detiene la reproducción
                view.getBtnPlay().setText("Reproducir"); // Actualiza el texto del botón
            }
            view.repaint(); // Redibuja la vista
        }
    }
}
