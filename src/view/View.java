package view;

import utils.Method;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import model.*;

/**
 * La clase View representa la interfaz gráfica del lector de archivos PGN de ajedrez.
 * Contiene elementos de la interfaz, como botones, áreas de texto y un tablero de ajedrez.
 */
public class View extends JFrame {
    // Layout y restricciones para la disposición de componentes
    private final GridBagLayout layout;
    private final GridBagConstraints constraints;

    // Panel de información y componentes de la interfaz
    private JPanel infoPanel;
    public JButton btnPgn, btnReset, btnEnd, btnPreMove, btnNxtMove, btnSearchMove, btnPlay;
    public JTextArea tagsArea, movesArea;
    private JScrollPane scrollTags, scrollMoves;
    public JTextField searchMove;
    private JLabel searchMoveLabel, pgnLabel, gameLabel, moveLabel, colorLabel;
    public JRadioButton whiteRadio, blackRadio;
    private ButtonGroup bg;
    private boolean notFlip = true; // Controla si el tablero está invertido

    /**
     * Constructor de la clase View.
     * Inicializa el marco y los componentes de la interfaz gráfica.
     */
    public View() {
        super("Lector PGN"); // Título de la ventana
        setResizable(false); // La ventana no es redimensionable
        layout = new GridBagLayout();
        setLayout(layout);
        constraints = new GridBagConstraints();

        initializeComponents(); // Inicializa los componentes de la interfaz
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        pack(); // Ajusta el tamaño de la ventana para que se ajusten los componentes
        setVisible(true); // Hace visible la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void initializeComponents() {
        // Inicializa las áreas de texto para los tags y movimientos
        tagsArea = new JTextArea(8, 20);
        scrollTags = setTextArea(tagsArea);
        movesArea = new JTextArea(7, 20);
        scrollMoves = setTextArea(movesArea);

        // Configuración del panel de información
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(100, 140));
        infoPanel.setLayout(layout);

        // Inicializa las etiquetas para mostrar información del juego
        pgnLabel = new JLabel("");
        gameLabel = new JLabel("");
        moveLabel = new JLabel("");
        colorLabel = new JLabel("");

        // Configura el espaciado y añade las etiquetas al panel de información
        constraints.insets = new Insets(0, 0, 10, 0);
        addComponentToInfoPanel(pgnLabel, 0);
        addComponentToInfoPanel(gameLabel, 1);
        constraints.insets = new Insets(0, 0, 9, 0);
        addComponentToInfoPanel(moveLabel, 2);
        constraints.insets = new Insets(0, 0, 6, 0);
        addComponentToInfoPanel(colorLabel, 3);

        // Inicializa los botones de la interfaz
        btnPgn = new JButton("Abrir archivo PGN");
        btnReset = new JButton("Reiniciar");
        btnEnd = new JButton("Final");
        btnPreMove = new JButton("Movimiento anterior");
        btnNxtMove = new JButton("Siguiente movimiento");
        btnSearchMove = new JButton("Buscar");
        btnPlay = new JButton("Reproducir");

        // Inicializa los campos de búsqueda y opciones de color
        searchMove = new JTextField(5);
        searchMoveLabel = new JLabel("No. de movimiento:");
        whiteRadio = new JRadioButton("Blanca");
        blackRadio = new JRadioButton("Negra");
        bg = new ButtonGroup();
        bg.add(whiteRadio);
        bg.add(blackRadio);

        // Organiza los componentes en el marco
        organizeComponents();
    }

    /**
     * Organiza los componentes en la ventana utilizando GridBagLayout.
     */
    private void organizeComponents() {
        // Añade el tablero de ajedrez
        constraints.insets = new Insets(0, 0, 0, 0);
        addComponent(new ChessBoard(), 0, 0, 8, 8);

        // Configura las restricciones de los componentes de la interfaz
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = new Insets(5, 5, 5, 5);

        // Añade áreas de texto y botones al marco
        addComponent(scrollTags, 0, 8, 5, 2);
        addComponent(scrollMoves, 2, 8, 5, 3);
        addComponent(infoPanel, 5, 8, 5, 2);
        addComponent(btnPgn, 7, 8, 5, 1);

        // Panel de navegación para los botones de movimiento
        JPanel navigationPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        navigationPanel.add(btnPreMove);
        navigationPanel.add(btnNxtMove);
        navigationPanel.add(btnReset);
        navigationPanel.add(btnEnd);
        addComponent(navigationPanel, 8, 8, 5, 1);

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchPanel.add(searchMoveLabel);
        searchPanel.add(searchMove);
        searchPanel.add(whiteRadio);
        searchPanel.add(blackRadio);
        searchPanel.add(btnSearchMove);
        addComponent(searchPanel, 9, 8, 5, 1);

        // Panel de control del tablero
        JPanel boardControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        boardControlPanel.add(btnPlay);
        addComponent(boardControlPanel, 10, 8, 5, 10);

        // Ajusta el tamaño de los componentes
        scrollTags.setPreferredSize(new Dimension(250, 120));
        scrollMoves.setPreferredSize(new Dimension(250, 150));
        infoPanel.setPreferredSize(new Dimension(250, 100));
        searchMove.setPreferredSize(new Dimension(50, 25));

        Dimension buttonSize = new Dimension(110, 25);
        btnPreMove.setPreferredSize(buttonSize);
        btnNxtMove.setPreferredSize(buttonSize);
        btnReset.setPreferredSize(buttonSize);
        btnEnd.setPreferredSize(buttonSize);
        btnPlay.setPreferredSize(new Dimension(115, 25));
        btnPgn.setPreferredSize(new Dimension(115, 25));
        btnSearchMove.setPreferredSize(new Dimension(80, 25));
    }

    /**
     * Configura un JTextArea y lo encapsula en un JScrollPane.
     * @param textArea El JTextArea a configurar.
     * @return JScrollPane que contiene el JTextArea configurado.
     */
    private JScrollPane setTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Sans-Serif", Font.BOLD, 15)); // Establece la fuente
        textArea.setLineWrap(true); // Permite el ajuste de línea
        textArea.setWrapStyleWord(true); // Ajusta el texto por palabra
        textArea.setEditable(false); // No es editable
        return new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Añade un componente a la interfaz gráfica con restricciones específicas.
     * @param component El componente a añadir.
     * @param row La fila donde se colocará el componente.
     * @param column La columna donde se colocará el componente.
     * @param width Ancho del componente.
     * @param height Alto del componente.
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
     * Añade un componente al panel de información.
     * @param component El componente a añadir.
     * @param row La fila donde se colocará el componente en el panel de información.
     */
    private void addComponentToInfoPanel(Component component, int row) {
        constraints.gridx = 0;
        constraints.gridy = row;
        layout.setConstraints(component, constraints);
        infoPanel.add(component);
    }

    /**
     * Clase interna que representa el tablero de ajedrez.
     */
    public class ChessBoard extends JPanel {
        final int UNIT_SIZE = 60; // Tamaño de cada unidad del tablero
        final int SCREEN_SIZE = 8 * UNIT_SIZE; // Tamaño total del tablero

        /**
         * Constructor de la clase ChessBoard.
         * Establece el tamaño y el color de fondo del tablero.
         */
        public ChessBoard() {
            setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
            setBackground(new Color(238, 238, 210)); // Color claro para el tablero
            setFocusable(true); // Permite que el tablero sea enfocado
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // Llama al método de la superclase
            drawBoard(g); // Dibuja el tablero
            drawPieces(g); // Dibuja las piezas
        }

        /**
         * Dibuja el tablero de ajedrez.
         * @param g El objeto Graphics utilizado para dibujar.
         */
        private void drawBoard(Graphics g) {
            g.setColor(new Color(96, 0, 0)); // Color oscuro para las casillas
            // Dibuja el tablero
            for (int i = 0; i <= SCREEN_SIZE; i += UNIT_SIZE) {
                for (int j = 0; j <= SCREEN_SIZE; j += UNIT_SIZE) {
                    // Alterna el color de las casillas
                    if (((i / UNIT_SIZE) % 2 == 0 && (j / UNIT_SIZE) % 2 != 0)
                            || ((i / UNIT_SIZE) % 2 != 0 && (j / UNIT_SIZE) % 2 == 0)) {
                        g.fillRect(i, j, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }

            // Dibuja las etiquetas del tablero
            g.setFont(new Font("Calibri", Font.BOLD, 15));
            for (int i = 8; i > 0; i--) {
                // Dibuja números y letras en los bordes del tablero
                if (i % 2 == 1) g.setColor(new Color(238, 238, 210));
                else g.setColor(new Color(96, 0, 0));
                g.drawString(String.valueOf(i), 2, 13 + UNIT_SIZE * (8 - i)); // Números de filas
                g.drawString(String.valueOf((char) (i + 96)), i * UNIT_SIZE - 10, 8 * UNIT_SIZE - 5); // Letras de columnas
            }
        }

        /**
         * Dibuja las piezas en el tablero.
         * @param g El objeto Graphics utilizado para dibujar.
         */
        public void drawPieces(Graphics g) {
            BufferedImage image = null; // Imagen de la pieza
            try {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        Piece piece = Method.getPiece(new int[]{i, j}); // Obtiene la pieza en la posición (i, j)
                        if (piece != Piece.NONE) { // Si hay una pieza
                            // Carga la imagen correspondiente según el tipo de pieza
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
                            // Dibuja la pieza en el tablero
                            if (notFlip)
                                g.drawImage(image, j * UNIT_SIZE, (7 - i) * UNIT_SIZE, null);
                            else
                                g.drawImage(image, (7 - j) * UNIT_SIZE, i * UNIT_SIZE, null);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Maneja excepciones al cargar imágenes
            }
        }
    }

    // Métodos para obtener componentes de la interfaz

    public JLabel getPgnLabel() {
        return pgnLabel;
    }

    public JLabel getMoveLabel() {
        return moveLabel;
    }

    public JLabel getColorLabel() {
        return colorLabel;
    }

    public JButton getBtnPgn() {
        return btnPgn;
    }

    public JButton getBtnReset() {
        return btnReset;
    }

    public JButton getBtnEnd() {
        return btnEnd;
    }

    public JButton getBtnPreMove() {
        return btnPreMove;
    }

    public JButton getBtnNxtMove() {
        return btnNxtMove;
    }

    public JButton getBtnSearchMove() {
        return btnSearchMove;
    }

    public JButton getBtnPlay() {
        return btnPlay;
    }

    public JTextArea getTagsArea() {
        return tagsArea;
    }

    public JTextArea getMovesArea() {
        return movesArea;
    }

    public JTextField getSearchMove() {
        return searchMove;
    }

    public JRadioButton getBlackRadio() {
        return blackRadio;
    }
}
