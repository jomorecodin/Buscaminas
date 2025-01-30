/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package morenotauiledd1;

/**
 *
 * @author moren
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Random;

/**
 * Clase principal que implementa el juego de Buscaminas.
 * Utiliza una interfaz gráfica basada en Swing y permite guardar/cargar el estado del juego.
 */
public class Buscaminas extends JFrame {
    private int filas;
    private int columnas;
    private int minas;
    private JButton[][] botones;
    private boolean[][] minasTablero;
    private boolean[][] revelado;
    private boolean[][] banderas;
    private int banderasColocadas;
    private JFileChooser fileChooser;
    private JComboBox<String> metodoBusqueda;
    private JLabel estadoJuego;

    /**
     * Constructor de la clase Buscaminas.
     *
     * @param filas    Número de filas del tablero.
     * @param columnas Número de columnas del tablero.
     * @param minas    Número de minas en el tablero.
     */
    public Buscaminas(int filas, int columnas, int minas) {
        this.filas = filas;
        this.columnas = columnas;
        this.minas = minas;
        this.botones = new JButton[filas][columnas];
        this.minasTablero = new boolean[filas][columnas];
        this.revelado = new boolean[filas][columnas];
        this.banderas = new boolean[filas][columnas];
        this.banderasColocadas = 0;
        this.fileChooser = new JFileChooser();
        this.estadoJuego = new JLabel("Juego en progreso", SwingConstants.CENTER);

        inicializarTablero();
        colocarMinas();
        crearInterfaz();
    }

    /**
     * Inicializa el tablero con botones y configura sus eventos.
     */
    private void inicializarTablero() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j] = new JButton();
                botones[i][j].addActionListener(new BotonClickListener(i, j));
                revelado[i][j] = false;
                banderas[i][j] = false;
            }
        }
    }

    /**
     * Coloca las minas en el tablero de manera aleatoria.
     */
    private void colocarMinas() {
        Random random = new Random();
        int minasColocadas = 0;
        while (minasColocadas < minas) {
            int fila = random.nextInt(filas);
            int columna = random.nextInt(columnas);
            if (!minasTablero[fila][columna]) {
                minasTablero[fila][columna] = true;
                minasColocadas++;
            }
        }
    }

    /**
     * Crea la interfaz gráfica del juego.
     */
    private void crearInterfaz() {
        setLayout(new BorderLayout());

        // Panel superior para el estado del juego y el método de búsqueda
        JPanel panelSuperior = new JPanel();
        metodoBusqueda = new JComboBox<>(new String[]{"BFS", "DFS"});
        panelSuperior.add(new JLabel("Método de búsqueda:"));
        panelSuperior.add(metodoBusqueda);
        panelSuperior.add(estadoJuego);
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central para el tablero
        JPanel panelTablero = new JPanel(new GridLayout(filas, columnas));
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                panelTablero.add(botones[i][j]);
            }
        }
        add(panelTablero, BorderLayout.CENTER);

        // Panel inferior para los botones de guardar y cargar
        JPanel panelInferior = new JPanel();
        JButton guardarButton = new JButton("Guardar Juego");
        guardarButton.addActionListener(e -> guardarJuego());
        JButton cargarButton = new JButton("Cargar Juego");
        cargarButton.addActionListener(e -> cargarJuego());
        panelInferior.add(guardarButton);
        panelInferior.add(cargarButton);
        add(panelInferior, BorderLayout.SOUTH);

        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Clase interna para manejar los eventos de clic en los botones.
     */
    private class BotonClickListener implements ActionListener {
        private int fila;
        private int columna;

        public BotonClickListener(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (banderas[fila][columna]) return; // No hacer nada si está marcada

            if (minasTablero[fila][columna]) {
                JOptionPane.showMessageDialog(null, "¡Has perdido!");
                reiniciarJuego();
            } else {
                if (metodoBusqueda.getSelectedItem().equals("BFS")) {
                    revelarCasillasBFS(fila, columna);
                } else {
                    revelarCasillasDFS(fila, columna);
                }
                if (verificarVictoria()) {
                    estadoJuego.setText("¡Has ganado!");
                }
            }
        }
    }

    /**
     * Revela las casillas usando BFS.
     *
     * @param fila    Fila de la casilla seleccionada.
     * @param columna Columna de la casilla seleccionada.
     */
    private void revelarCasillasBFS(int fila, int columna) {
        MiCola<int[]> cola = new MiCola<>();
        cola.encolar(new int[]{fila, columna});

        while (!cola.estaVacia()) {
            int[] actual = cola.desencolar();
            int i = actual[0];
            int j = actual[1];

            if (revelado[i][j]) continue;
            revelado[i][j] = true;
            botones[i][j].setText(String.valueOf(contarMinasAdyacentes(i, j)));

            if (contarMinasAdyacentes(i, j) == 0) {
                for (int x = Math.max(0, i - 1); x <= Math.min(filas - 1, i + 1); x++) {
                    for (int y = Math.max(0, j - 1); y <= Math.min(columnas - 1, j + 1); y++) {
                        if (!revelado[x][y] && !banderas[x][y]) {
                            cola.encolar(new int[]{x, y});
                        }
                    }
                }
            }
        }
    }

    /**
     * Revela las casillas usando DFS.
     *
     * @param fila    Fila de la casilla seleccionada.
     * @param columna Columna de la casilla seleccionada.
     */
    private void revelarCasillasDFS(int fila, int columna) {
        if (revelado[fila][columna]) return;
        revelado[fila][columna] = true;
        botones[fila][columna].setText(String.valueOf(contarMinasAdyacentes(fila, columna)));

        if (contarMinasAdyacentes(fila, columna) == 0) {
            for (int i = Math.max(0, fila - 1); i <= Math.min(filas - 1, fila + 1); i++) {
                for (int j = Math.max(0, columna - 1); j <= Math.min(columnas - 1, columna + 1); j++) {
                    if (!revelado[i][j] && !banderas[i][j]) {
                        revelarCasillasDFS(i, j);
                    }
                }
            }
        }
    }

    /**
     * Cuenta las minas adyacentes a una casilla.
     *
     * @param fila    Fila de la casilla.
     * @param columna Columna de la casilla.
     * @return Número de minas adyacentes.
     */
    private int contarMinasAdyacentes(int fila, int columna) {
        int count = 0;
        for (int i = Math.max(0, fila - 1); i <= Math.min(filas - 1, fila + 1); i++) {
            for (int j = Math.max(0, columna - 1); j <= Math.min(columnas - 1, columna + 1); j++) {
                if (minasTablero[i][j]) count++;
            }
        }
        return count;
    }

    /**
     * Verifica si el jugador ha ganado.
     *
     * @return true si el jugador ha ganado, false en caso contrario.
     */
    private boolean verificarVictoria() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (!minasTablero[i][j] && !revelado[i][j]) return false;
            }
        }
        return true;
    }

    /**
     * Reinicia el juego.
     */
    private void reiniciarJuego() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j].setText("");
                revelado[i][j] = false;
                banderas[i][j] = false;
                minasTablero[i][j] = false;
            }
        }
        colocarMinas();
        estadoJuego.setText("Juego en progreso");
    }

    /**
     * Guarda el estado del juego en un archivo CSV.
     */
    private void guardarJuego() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        writer.println(i + "," + j + "," + minasTablero[i][j] + "," + revelado[i][j] + "," + banderas[i][j]);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga el estado del juego desde un archivo CSV.
     */
    private void cargarJuego() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String[] datos = scanner.nextLine().split(",");
                    int i = Integer.parseInt(datos[0]);
                    int j = Integer.parseInt(datos[1]);
                    minasTablero[i][j] = Boolean.parseBoolean(datos[2]);
                    revelado[i][j] = Boolean.parseBoolean(datos[3]);
                    banderas[i][j] = Boolean.parseBoolean(datos[4]);
                    if (revelado[i][j]) {
                        botones[i][j].setText(String.valueOf(contarMinasAdyacentes(i, j)));
                    }
                    if (banderas[i][j]) {
                        botones[i][j].setText("F");
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Buscaminas(8, 8, 10);
    }
}