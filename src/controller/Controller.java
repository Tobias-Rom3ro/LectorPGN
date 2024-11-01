//package controller;
//
//import model.*;
//import utils.*;
//import view.*;
//
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.filechooser.FileSystemView;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.DefaultHighlighter;
//import javax.swing.text.Highlighter;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class Controller implements ActionListener {
//    private View view;
//    private PGN pgn;
//    private Game game;
//    private boolean isSelected, isMove, notFlip, isPlay;
//    private int indexOfMove, indexOfGame;
//    private Timer timer;
//
//    public Controller(View view) {
//        this.view = view;
//        notFlip = true;
//        indexOfGame = 0;
//        isMove = false;
//        isPlay = false;
//        indexOfMove = -1;
//        timer = new Timer(750, this);
//        timer.start();
//    }
//
//    public void loadPGN() {
//        JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
//        j.setAcceptAllFileFilterUsed(false);
//        j.setDialogTitle("Selecciona un archivo .pgn");
//        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Solamente archivos .pgn", "pgn");
//        j.addChoosableFileFilter(restrict);
//        int r = j.showOpenDialog(null);
//        if (r == JFileChooser.APPROVE_OPTION) {
//            indexOfGame = 0;
//            pgn = new PGN(j.getSelectedFile().getAbsolutePath());
//
//            isSelected = true;
//
//            String nameOfFile = j.getSelectedFile().getName();
//            view.setPgnLabel(nameOfFile.substring(0, nameOfFile.length()-4));
//
//            setGameButton();
//        }
//    }
//
//    public void resetGame() {
//        if(isSelected){
//            isMove = false;
//            isPlay = false;
//            indexOfMove = -1;
//
//            game.board.resetBoard();
//
//            int size = game.board.getMoves().size();
//            size = (size/2)+(size%2);
//            view.setMoveLabel("Movimiento "+(indexOfMove + 1)+" de "+size);
//            view.setColorLabel("");
//
//            view.setBtnPlayText("Reproducir");
//
//            highlightMove();
//            view.repaint();
//        }
//    }
//
//    public void goToEnd() {
//        if(isSelected && indexOfMove < game.board.getMoves().size()-1){
//            isMove = true;
//            isPlay = false;
//            indexOfMove = game.board.getMoves().size()-1;
//
//            for(int i = 0; i <= indexOfMove; i++)
//                game.board.doMove(game.board.getMoves().get(i));
//
//            int size = game.board.getMoves().size();
//            size = (size/2)+(size%2);
//            if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
//                view.setMoveLabel("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
//                view.setColorLabel("Blanca");
//            }
//            else if(indexOfMove+1 != 0) {
//                view.setMoveLabel("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
//                view.setColorLabel("Negra");
//            }
//
//            view.setBtnPlayText("Reproducir");
//
//            highlightMove();
//            view.repaint();
//        }
//    }
//
//    public void previousMove() {
//        if(isSelected && indexOfMove >= 0){
//            isMove = true;
//            isPlay = false;
//            indexOfMove--;
//
//            game.board.undoMove();
//
//            int size = game.board.getMoves().size();
//            size = (size/2)+(size%2);
//            if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
//                view.setMoveLabel("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
//                view.setColorLabel("Blanca");
//            }
//            else if(indexOfMove+1 != 0) {
//                view.setMoveLabel("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
//                view.setColorLabel("Negra");
//            }
//            else {
//                view.setMoveLabel("Movimiento 0 de " + size);
//                view.setColorLabel("");
//            }
//
//            view.setBtnPlayText("Reproducir");
//
//            highlightMove();
//            view.repaint();
//        }
//    }
//
//    public void nextMove() {
//        if(isSelected && indexOfMove < game.board.getMoves().size()-1){
//            isMove = true;
//            isPlay = false;
//            indexOfMove++;
//
//            game.board.doMove(game.board.getMoves().get(indexOfMove));
//
//            int size = game.board.getMoves().size();
//            size = (size/2)+(size%2);
//            if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
//                view.setMoveLabel("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
//                view.setColorLabel("Blanca");
//            }
//            else if(indexOfMove+1 != 0) {
//                view.setMoveLabel("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
//                view.setColorLabel("Negra");
//            }
//
//            view.setBtnPlayText("Reproducir");
//
//            highlightMove();
//            view.repaint();
//        }
//    }
//
//    public void searchMove(String moveText, boolean isBlack) {
//        if(isSelected && moveText != null){
//            int index = (Integer.parseInt(moveText)-1)*2;
//            if(index >= 0 && index < game.board.getMoves().size()) {
//                if(isBlack)
//                    index++;
//                if(index > indexOfMove) {
//                    for (int i = indexOfMove + 1; i <= index; i++)
//                        game.board.doMove(game.board.getMoves().get(i));
//                    indexOfMove = index;
//                }
//                else if(index < indexOfMove){
//                    while (index != indexOfMove){
//                        indexOfMove--;
//                        game.board.undoMove();
//                    }
//                }
//                isMove = true;
//                int size = game.board.getMoves().size();
//                size = (size/2)+(size%2);
//                if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
//                    view.setMoveLabel("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
//                    view.setColorLabel("Blanca");
//                }
//                else if(indexOfMove+1 != 0) {
//                    view.setMoveLabel("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
//                    view.setColorLabel("Negra");
//                }
//            }
//            else if(index == -2){
//                indexOfMove = -1;
//                isMove = false;
//                game.board.resetBoard();
//                int size = game.board.getMoves().size();
//                size = (size/2)+(size%2);
//                view.setMoveLabel("Movimiento "+(indexOfMove + 1)+" de "+size);
//                view.setColorLabel("");
//            }
//            isPlay = false;
//            view.setBtnPlayText("Reproducir");
//            highlightMove();
//            view.repaint();
//        }
//    }
//
//    public void flipBoard() {
//        notFlip = !notFlip;
//        view.repaint();
//    }
//
//    public void togglePlay() {
//        if(isSelected && indexOfMove < game.board.getMoves().size()-1) {
//            isPlay = !isPlay;
//            if (isPlay)
//                view.setBtnPlayText("Pausa");
//            else
//                view.setBtnPlayText("Reproducir");
//        }
//    }
//
//    private void playMove() {
//        if(isPlay) {
//            if(isSelected && indexOfMove < game.board.getMoves().size()-1){
//                indexOfMove++;
//                isMove = true;
//                game.board.doMove(game.board.getMoves().get(indexOfMove));
//                int size = game.board.getMoves().size();
//                size = (size/2)+(size%2);
//                if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
//                    view.setMoveLabel("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
//                    view.setColorLabel("Blanca");
//                }
//                else if(indexOfMove+1 != 0) {
//                    view.setMoveLabel("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
//                    view.setColorLabel("Negra");
//                }
//                highlightMove();
//            }
//            if(indexOfMove == game.board.getMoves().size()-1){
//                isPlay = false;
//                view.setBtnPlayText("Reproducir");
//            }
//            view.repaint();
//        }
//    }
//
//    private void highlightMove() {
//        Highlighter highlighter = view.getMovesArea().getHighlighter();
//        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(231, 231, 0, 255));
//        highlighter.removeAllHighlights();
//        if(indexOfMove >= 0) {
//            int indexOfMoveText;
//            if((indexOfMove+1) % 2 == 1) {
//                indexOfMoveText = view.getMovesArea().getText().indexOf(String.valueOf((indexOfMove + 2) / 2));
//            }
//            else {
//                indexOfMoveText = view.getMovesArea().getText().indexOf(String.valueOf((indexOfMove + 1) / 2));
//            }
//            if(view.getColorLabel().getText().equals("Negra"))
//                indexOfMoveText += game.board.getMoves().get(indexOfMove-1).getStringMove().length();
//            String strMove;
//            strMove = game.board.getMoves().get(indexOfMove).getStringMove();
//            int p0 = view.getMovesArea().getText().indexOf(strMove, indexOfMoveText);
//            int p1 = p0 + strMove.length();
//            try {
//                highlighter.addHighlight(p0, p1, painter);
//            } catch (BadLocationException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void setGameButton() {
//        if (pgn != null && !pgn.getGames().isEmpty()) {
//            isMove = false;
//            isPlay = false;
//            indexOfMove = -1;
//
//            game = pgn.getGames().get(indexOfGame);
//            game.board = new Board(Converter.convertMoves(game.getStringMovesArray()));
//            game.board.resetBoard();
//
//            view.setTagsAreaText(game.toString());
//            view.setMovesAreaText(game.getStrMovesText());
//
//            view.setBtnPlayText("Reproducir");
//
//            view.repaint();
//        }
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        playMove();
//    }
//}
