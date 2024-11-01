import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class View extends JFrame implements ActionListener {
    private final GridBagLayout layout ;
    private final GridBagConstraints constraints ;
    JPanel infoPanel;
    JButton btnPgn, btnReset, btnEnd, btnPreMove, btnNxtMove, btnSearchMove, btnFlip, btnPlay;
    JTextArea tagsArea, movesArea;
    JScrollPane scrollTags, scrollMoves;
    JTextField searchMove;
    JLabel searchMoveLabel, pgnLabel, gameLabel, moveLabel, colorLabel;
    JRadioButton whiteRadio, blackRadio;
    ButtonGroup bg;
    Timer timer;
    private PGN pgn;
    private Game game;
    private boolean isSelected, isMove, notFlip, isPlay;
    private int indexOfMove, indexOfGame;


    public View(){
        super("Lector PGN");
        setResizable(false);
        layout = new GridBagLayout();
        setLayout(layout);
        constraints = new GridBagConstraints();

        notFlip = true;
        indexOfGame = 0;
        isMove = false;
        isPlay = false;
        indexOfMove = -1;
        timer = new Timer(750, this);
        timer.start();

        //create tagsArea
        tagsArea = new JTextArea(8, 20);
        scrollTags = setTextArea(tagsArea);

        //create movesArea
        movesArea = new JTextArea (7 , 20);
        scrollMoves = setTextArea(movesArea);

        //create info panel
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(100, 140));
        infoPanel.setLayout(layout);

        pgnLabel = new JLabel("");
        gameLabel = new JLabel("");
        moveLabel = new JLabel("");
        colorLabel = new JLabel("");

        constraints.insets = new Insets(0, 0, 10, 0);
        addComponentToInfoPanel(pgnLabel, 0);
        addComponentToInfoPanel(gameLabel, 1);
        constraints.insets = new Insets(0, 0, 9, 0);
        addComponentToInfoPanel(moveLabel, 2);
        constraints.insets = new Insets(0, 0, 6, 0);
        addComponentToInfoPanel(colorLabel, 3);


        //create buttons
        btnPgn = new JButton ("Abrir PGN");
        btnPgn.addActionListener(e -> {
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            j.setAcceptAllFileFilterUsed(false);
            j.setDialogTitle("Selecciona un archivo .pgn");
            FileNameExtensionFilter restrict = new FileNameExtensionFilter("Solamente archivos .pgn", "pgn");
            j.addChoosableFileFilter(restrict);
            int r = j.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                indexOfGame = 0;
                pgn = new PGN(j.getSelectedFile().getAbsolutePath());

                //set variables
                isSelected = true;

                //set labels in info panel
                String nameOfFile = j.getSelectedFile().getName();
                pgnLabel.setText(nameOfFile.substring(0, nameOfFile.length()-4));

                setGameButton();
            }
        });


        btnReset = new JButton ("Reiniciar");
        btnReset.addActionListener(e -> {
            if(isSelected){
                //set variables
                isMove = false;
                isPlay = false;
                indexOfMove = -1;

                //reset board
                game.board.resetBoard();

                //set labels of info panel
                int size = game.board.getMoves().size();
                size = (size/2)+(size%2);
                moveLabel.setText("Movimiento "+(indexOfMove + 1)+" de "+size);
                colorLabel.setText("");

                btnPlay.setText("Reproducir");

                highlightMove();
                repaint();
            }
        });

        btnEnd = new JButton ("Final");
        btnEnd.addActionListener(e -> {
            if(isSelected && indexOfMove < game.board.getMoves().size()-1){
                //set variables
                isMove = true;
                isPlay = false;
                indexOfMove = game.board.getMoves().size()-1;

                //perform every move to reach the end of game
                for(int i = 0; i <= indexOfMove; i++)
                    game.board.doMove(game.board.getMoves().get(i));

                //set labels of info panel
                int size = game.board.getMoves().size();
                size = (size/2)+(size%2);
                if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
                    moveLabel.setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                    colorLabel.setText("Blanca");
                }
                else if(indexOfMove+1 != 0) {
                    moveLabel.setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                    colorLabel.setText("Negra");
                }

                btnPlay.setText("Reproducir");

                highlightMove();
                repaint();
            }
        });


        btnPreMove = new JButton ("Movimiento anterior");
        btnPreMove.addActionListener(e -> {
            if(isSelected && indexOfMove >= 0){
                //set variables
                isMove = true;
                isPlay = false;
                indexOfMove--;

                //undo last move
                game.board.undoMove();

                //set labels of info panel
                int size = game.board.getMoves().size();
                size = (size/2)+(size%2);
                if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
                    moveLabel.setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                    colorLabel.setText("Blanca");
                }
                else if(indexOfMove+1 != 0) {
                    moveLabel.setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                    colorLabel.setText("Negra");
                }
                else {
                    moveLabel.setText("Movimiento 0 de " + size);
                    colorLabel.setText("");
                }


                btnPlay.setText("Reproducir");

                highlightMove();
                repaint();
            }
        });

        btnNxtMove = new JButton ("Siguiente movimiento");
        btnNxtMove.addActionListener(e -> {
            if(isSelected && indexOfMove < game.board.getMoves().size()-1){
                //set variables
                isMove = true;
                isPlay = false;
                indexOfMove++;

                //perform next move
                game.board.doMove(game.board.getMoves().get(indexOfMove));

                //set labels of info panel
                int size = game.board.getMoves().size();
                size = (size/2)+(size%2);
                if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
                    moveLabel.setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                    colorLabel.setText("Blanca");
                }
                else if(indexOfMove+1 != 0) {
                    moveLabel.setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                    colorLabel.setText("Negra");
                }

                btnPlay.setText("Reproducir");

                highlightMove();
                repaint();
            }
        });

        searchMove = new JTextField(5);
        searchMoveLabel = new JLabel("No. de movimiento:");
        whiteRadio = new JRadioButton("Blanca");
        blackRadio = new JRadioButton("Negra");
        bg = new ButtonGroup();
        bg.add(whiteRadio); bg.add(blackRadio);
        btnSearchMove = new JButton("Buscar");
        btnSearchMove.addActionListener(e -> {
            if(isSelected && searchMove.getText() != null){
                int index = (Integer.parseInt(searchMove.getText())-1)*2;
                if(index >= 0 && index < game.board.getMoves().size()) {
                    if(blackRadio.isSelected())
                        index++;
                    if(index > indexOfMove) {
                        for (int i = indexOfMove + 1; i <= index; i++)
                            game.board.doMove(game.board.getMoves().get(i));
                        indexOfMove = index;
                    }
                    else if(index < indexOfMove){
                        while (index != indexOfMove){
                            indexOfMove--;
                            game.board.undoMove();
                        }
                    }
                    isMove = true;
                    int size = game.board.getMoves().size();
                    size = (size/2)+(size%2);
                    if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
                        moveLabel.setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                        colorLabel.setText("Blanca");
                    }
                    else if(indexOfMove+1 != 0) {
                        moveLabel.setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                        colorLabel.setText("Negra");
                    }
                }
                else if(index == -2){
                    indexOfMove = -1;
                    isMove = false;
                    game.board.resetBoard();
                    int size = game.board.getMoves().size();
                    size = (size/2)+(size%2);
                    moveLabel.setText("Movimiento "+(indexOfMove + 1)+" de "+size);
                    colorLabel.setText("");
                }
                isPlay = false;
                btnPlay.setText("Reproducir");
                highlightMove();
                repaint();
            }
        });

        btnFlip = new JButton ("Voltear tablero");
        btnFlip.addActionListener(e -> {
            notFlip = !notFlip;
            repaint();
        });

        btnPlay = new JButton ("Reproducir");
        btnPlay.addActionListener(e -> {
            if(isSelected && indexOfMove < game.board.getMoves().size()-1) {
                isPlay = !isPlay;
                if (isPlay)
                    btnPlay.setText("Pausa");
                else
                    btnPlay.setText("Reproducir");
            }
        });

        //add chessboard
        constraints.insets = new Insets(0, 0, 0, 0);
        addComponent(new ChessBoard(), 0, 0, 8,8);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

// Primero, organizamos el lado derecho en secciones más claras
// Sección 1: Áreas de texto
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(5, 5, 5, 5);
        addComponent(scrollTags, 0, 8, 5, 2);  // Tags área
        addComponent(scrollMoves, 2, 8, 5, 3);  // Moves área
        addComponent(infoPanel, 5, 8, 5, 2);    // Info panel

// Sección 2: Botones principales
        constraints.insets = new Insets(5, 5, 5, 5);
        addComponent(btnPgn, 7, 8, 5, 1);       // Botón PGN

// Sección 3: Controles de navegación
        JPanel navigationPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        navigationPanel.add(btnPreMove);
        navigationPanel.add(btnNxtMove);
        navigationPanel.add(btnReset);
        navigationPanel.add(btnEnd);
        constraints.insets = new Insets(5, 5, 5, 5);
        addComponent(navigationPanel, 8, 8, 5, 1);

// Sección 4: Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.add(searchMoveLabel);
        searchPanel.add(searchMove);
        searchPanel.add(whiteRadio);
        searchPanel.add(blackRadio);
        searchPanel.add(btnSearchMove);
        constraints.insets = new Insets(5, 5, 5, 5);
        addComponent(searchPanel, 9, 8, 5, 1);

// Sección 5: Controles de tablero
        JPanel boardControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        boardControlPanel.add(btnFlip);
        boardControlPanel.add(btnPlay);
        constraints.insets = new Insets(5, 5, 5, 5);
        addComponent(boardControlPanel, 10, 8, 5, 10);

        // Ajustar tamaños preferidos
        scrollTags.setPreferredSize(new Dimension(250, 120));
        scrollMoves.setPreferredSize(new Dimension(250, 150));
        infoPanel.setPreferredSize(new Dimension(250, 100));

// Ajustar tamaños de los campos de texto
        searchMove.setPreferredSize(new Dimension(50, 25));

// Ajustar tamaños de los botones
        Dimension buttonSize = new Dimension(110, 25);
        btnPreMove.setPreferredSize(buttonSize);
        btnNxtMove.setPreferredSize(buttonSize);
        btnReset.setPreferredSize(buttonSize);
        btnEnd.setPreferredSize(buttonSize);
        btnFlip.setPreferredSize(new Dimension(115,25));
        btnPlay.setPreferredSize(new Dimension(115,25));
        btnPgn.setPreferredSize(new Dimension(115, 25));
        btnSearchMove.setPreferredSize(new Dimension(80, 25));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    /**
     * create a panel for chess board
     */
    private class ChessBoard extends JPanel{
        final int UNIT_SIZE = 60;
        final int SCREEN_SIZE = 8*UNIT_SIZE;

        ChessBoard() {
            setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
            setBackground(new Color(238, 238, 210));
            setFocusable(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        public void draw(Graphics g) {
            // paint the board
            g.setColor(new Color(96, 0, 0));
            for(int i = 0; i <= SCREEN_SIZE; i += UNIT_SIZE) {
                for(int j = 0; j <= SCREEN_SIZE; j += UNIT_SIZE) {
                    if (((i / UNIT_SIZE) % 2 == 0 && (j / UNIT_SIZE) % 2 != 0)
                    || ((i / UNIT_SIZE) % 2 != 0 && (j / UNIT_SIZE) % 2 == 0))
                        g.fillRect(i, j, UNIT_SIZE, UNIT_SIZE);
                }
            }

            //paint the move (show initial and final square of move)
            if(isMove){
                Move move;
                if(indexOfMove >= 0)
                    move = game.board.getMoves().get(indexOfMove);
                else
                    move = game.board.getMoves().get(0);
                g.setColor(new Color(231, 231, 0));
                if(move.getTypeOfConstructor() != 3) {
                    int[] s1 = Method.getPosition(move.getFrom());
                    int[] s2 = Method.getPosition(move.getTo());
                    if(notFlip) {
                        g.fillRect(s1[1] * UNIT_SIZE, (7 - s1[0]) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                        g.fillRect(s2[1] * UNIT_SIZE, (7 - s2[0]) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.fillRect((7-s1[1]) * UNIT_SIZE, (s1[0]) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                        g.fillRect((7-s2[1]) * UNIT_SIZE, (s2[0]) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                } else {
                    if(move.isWhite()){
                        if(move.isKingSideCastle()){
                            if(notFlip) {
                                g.fillRect((6) * UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((4) * UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                            } else {
                                g.fillRect( UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((3) * UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                            }
                        }
                        else if(move.isQueenSideCastle()){
                            if(notFlip) {
                                g.fillRect((4) * UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((2) * UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                            } else {
                                g.fillRect((3) * UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((5) * UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                            }
                        }
                    } else{
                        if(move.isKingSideCastle()){
                            if(notFlip) {
                                g.fillRect((6) * UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((4) * UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                            } else {
                                g.fillRect( UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((3) * UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                            }
                        }
                        else if(move.isQueenSideCastle()){
                            if(notFlip) {
                                g.fillRect((4) * UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((2) * UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE);
                            } else {
                                g.fillRect((3) * UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                                g.fillRect((5) * UNIT_SIZE, (7) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                            }
                        }
                    }
                }
            }

            // draw coordinates of squares
            g.setFont(new Font("Calibri", Font.BOLD, 15));
            for(int i = 8; i > 0; i--){
                if(i % 2 == 1)
                    g.setColor(new Color(238, 238, 210));
                else
                    g.setColor(new Color(96, 0, 0));
                g.drawString(String.valueOf(i),2,13+UNIT_SIZE*(8-i));
                g.drawString(String.valueOf((char)(i+96)),(i)*UNIT_SIZE-10,8*UNIT_SIZE-5);
            }

            //draw image of pieces
            BufferedImage image = null;
            try {
                for(int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        Piece piece = Method.getPiece(new int[]{i, j});
                        if(piece != Piece.NONE) {
                            switch (piece) {
                                case WHITE_PAWN -> image = ImageIO.read(new File("src/resources/wp.png"));
                                case BLACK_PAWN -> image = ImageIO.read(new File("src/resources/bp.png"));
                                case WHITE_ROOK -> image = ImageIO.read(new File("src/resources/wr.png"));
                                case BLACK_ROOK -> image = ImageIO.read(new File("src/resources/br.png"));
                                case WHITE_KNIGHT -> image = ImageIO.read(new File("src/resources/wn.png"));
                                case BLACK_KNIGHT -> image = ImageIO.read(new File("src/resources/bn.png"));
                                case WHITE_BISHOP -> image = ImageIO.read(new File("src/resources/wb.png"));
                                case BLACK_BISHOP -> image = ImageIO.read(new File("src/resources/bb.png"));
                                case WHITE_QUEEN -> image = ImageIO.read(new File("src/resources/wq.png"));
                                case BLACK_QUEEN -> image = ImageIO.read(new File("src/resources/bq.png"));
                                case WHITE_KING -> image = ImageIO.read(new File("src/resources/wk.png"));
                                case BLACK_KING -> image = ImageIO.read(new File("src/resources/bk.png"));
                            }
                            if(notFlip)
                                g.drawImage(image, j * UNIT_SIZE, (7-i) * UNIT_SIZE, null);
                            else
                                g.drawImage(image, (7-j) * UNIT_SIZE, i * UNIT_SIZE, null);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * add component to frame
     */
    private void addComponent(Component component, int row, int column, int width, int height) {
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        layout.setConstraints(component, constraints);
        add(component);
    }

    /**
     * add component to info panel
     */
    private void addComponentToInfoPanel(Component component, int row){
        constraints.gridx = 0;
        constraints.gridy = row;
        layout.setConstraints(component, constraints);
        infoPanel.add(component);
    }

    /**
     * set text areas
     */
    private JScrollPane setTextArea(JTextArea textArea){
        textArea.setFont(new Font("Sans-Serif", Font.BOLD, 15));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        return new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }


    /**
     * add common lines to pgn and game buttons
     */
    private void setGameButton() {
        if (pgn != null && !pgn.getGames().isEmpty()) {
            //set variables
            isMove = false;
            isPlay = false;
            indexOfMove = -1;

            //set the game
            game = pgn.getGames().get(indexOfGame);
            game.board = new Board(Converter.convertMoves(game.getStringMovesArray()));
            game.board.resetBoard();

            //set textAreas based on the game
            tagsArea.setText(game.toString());
            movesArea.setText(game.getStrMovesText());

            //set play button's text
            btnPlay.setText("Reproducir");

            repaint();
        }
    }

    /**
     * perform moves in order
     */
    private void play(){
        if(isSelected && indexOfMove < game.board.getMoves().size()-1){
            indexOfMove++;
            isMove = true;
            game.board.doMove(game.board.getMoves().get(indexOfMove));
            int size = game.board.getMoves().size();
            size = (size/2)+(size%2);
            if(indexOfMove+1 != 0 && (indexOfMove+1) % 2 == 1) {
                moveLabel.setText("Movimiento " + ((indexOfMove + 2) / 2) + " de " + size);
                colorLabel.setText("Blanca");
            }
            else if(indexOfMove+1 != 0) {
                moveLabel.setText("Movimiento " + ((indexOfMove + 1) / 2) + " de " + size);
                colorLabel.setText("Negra");
            }
            highlightMove();
        }
    }

    /**
     * highlight move's string in movesArea
     */
    private void highlightMove(){
        Highlighter highlighter = movesArea.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(231, 231, 0, 255));
        highlighter.removeAllHighlights();
        if(indexOfMove >= 0) {
            int indexOfMoveText;
            if((indexOfMove+1) % 2 == 1) {
                indexOfMoveText = movesArea.getText().indexOf(String.valueOf((indexOfMove + 2) / 2));
            }
            else {
                indexOfMoveText = movesArea.getText().indexOf(String.valueOf((indexOfMove + 1) / 2));
            }
            if(colorLabel.getText().equals("Negra"))
                indexOfMoveText += game.board.getMoves().get(indexOfMove-1).getStringMove().length();
            String strMove;
            strMove = game.board.getMoves().get(indexOfMove).getStringMove();
            int p0 = movesArea.getText().indexOf(strMove, indexOfMoveText);
            int p1 = p0 + strMove.length();
            try {
                highlighter.addHighlight(p0, p1, painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(isPlay) {
            play();
            if(indexOfMove == game.board.getMoves().size()-1){
                isPlay = false;
                btnPlay.setText("Reproducir");
            }
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(View::new);
    }
}
