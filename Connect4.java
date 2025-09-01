import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Connect4 {

    /**
     *       Program:        Connect4.java
     *       Purpose:        Stacking disk game for 2 players
     *       Creator:        Chris Clarke
     *       Created:        19.08.2007
     *       Modified:       29.11.2012 (JFrame)
     */

    public static void main(String[] args) {
        Connect4JFrame frame = new Connect4JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

class Connect4JFrame extends JFrame implements ActionListener {

    public static final int MAXROW = 6;     // 6 rows
    public static final int MAXCOL = 7;     // 7 columns

    private Button[] btn = new Button[MAXCOL];
    private Label lblSpacer;
    MenuItem newMI, exitMI, redMI, yellowMI;
    MenuItem randomPlayer;
    Color[][] theArray;
    boolean end = false;
    boolean gameStart;

    int redMoves = 0;
    int yellowMoves = 0;
    int totalMoves = 0;

    private Label redMovesLabel;
    private Label yellowMovesLabel;
    private Label totalMovesLabel;

    public static final String SPACE = "                  "; // 18 spaces
    Color activeColour = Color.RED;

    public Connect4JFrame() {
        setTitle("Connect4 by Chris Clarke");
        MenuBar mbar = new MenuBar();
        Menu fileMenu = new Menu("File");
        newMI = new MenuItem("New");
        newMI.addActionListener(this);
        fileMenu.add(newMI);
        exitMI = new MenuItem("Exit");
        exitMI.addActionListener(this);
        fileMenu.add(exitMI);
        mbar.add(fileMenu);

        Menu optMenu = new Menu("Options");
        redMI = new MenuItem("Red starts");
        redMI.addActionListener(this);
        optMenu.add(redMI);
        yellowMI = new MenuItem("Yellow starts");
        yellowMI.addActionListener(this);
        optMenu.add(yellowMI);

        randomPlayer = new MenuItem("Random player");
        randomPlayer.addActionListener(this);
        optMenu.add(randomPlayer);

        mbar.add(optMenu);
        setMenuBar(mbar);

        Panel statsPanel = new Panel();
        redMovesLabel = new Label("Red Moves: 0");
        yellowMovesLabel = new Label("Yellow Moves: 0");
        totalMovesLabel = new Label("Total Moves: 0");

        statsPanel.add(redMovesLabel);
        statsPanel.add(yellowMovesLabel);
        statsPanel.add(totalMovesLabel);

        add(statsPanel, BorderLayout.SOUTH);

        // Build control panel.
        Panel panel = new Panel();
        for (int i = 0; i < btn.length; i++) {
            btn[i] = new Button("" + i);
            btn[i].addActionListener(this);
            panel.add(btn[i]);
            lblSpacer = new Label(SPACE);
            panel.add(lblSpacer);
        }

        add(panel, BorderLayout.NORTH);
        initialize();
        setSize(1024, 768);
    }

    public void initialize() {
        theArray = new Color[MAXROW][MAXCOL];
        for (int row = 0; row < MAXROW; row++)
            for (int col = 0; col < MAXCOL; col++)
                theArray[row][col] = Color.WHITE;
        gameStart = false;
        end = false;

        redMoves = 0;
        yellowMoves = 0;
        totalMoves = 0;

        redMovesLabel.setText("Red Moves: 0");
        yellowMovesLabel.setText("Yellow Moves: 0");
        totalMovesLabel.setText("Total Moves: 0");
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(110, 50, 100 + 100 * MAXCOL, 100 + 100 * MAXROW);
        for (int row = 0; row < MAXROW; row++)
            for (int col = 0; col < MAXCOL; col++) {
                g.setColor(theArray[row][col]);
                g.fillOval(160 + 100 * col, 100 + 100 * row, 100, 100);
            }
        check4(g);
    }

    public void putDisk(int n) {
        if (end) return;
        gameStart = true;
        int row;
        for (row = 0; row < MAXROW; row++)
            if (theArray[row][n] != Color.WHITE) break;
        if (row > 0) {
            theArray[--row][n] = activeColour;
            if (activeColour == Color.RED) {
                activeColour = Color.YELLOW;
                yellowMoves++;
            } else {
                activeColour = Color.RED;
                redMoves++;
            }
            totalMoves++;

            redMovesLabel.setText("Red Moves: " + redMoves);
            yellowMovesLabel.setText("Yellow Moves: " + yellowMoves);
            totalMovesLabel.setText("Total Moves: " + totalMoves);

            repaint();
        }
    }

    public void displayWinner(Graphics g, Color n) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Courier", Font.BOLD, 100));
        if (n == Color.RED)
            g.drawString("Red wins!", 100, 400);
        else
            g.drawString("Yellow wins!", 100, 400);
        end = true;
    }

    public void check4(Graphics g) {
        // horizontal
        for (int row = 0; row < MAXROW; row++) {
            for (int col = 0; col < MAXCOL - 3; col++) {
                Color curr = theArray[row][col];
                if (curr == Color.WHITE) continue;
                if (curr == theArray[row][col + 1] &&
                    curr == theArray[row][col + 2] &&
                    curr == theArray[row][col + 3]) {
                    displayWinner(g, curr);
                }
            }
        }
        // vertical
        for (int col = 0; col < MAXCOL; col++) {
            for (int row = 0; row < MAXROW - 3; row++) {
                Color curr = theArray[row][col];
                if (curr == Color.WHITE) continue;
                if (curr == theArray[row + 1][col] &&
                    curr == theArray[row + 2][col] &&
                    curr == theArray[row + 3][col]) {
                    displayWinner(g, curr);
                }
            }
        }
        // diag down-right
        for (int row = 0; row < MAXROW - 3; row++) {
            for (int col = 0; col < MAXCOL - 3; col++) {
                Color curr = theArray[row][col];
                if (curr == Color.WHITE) continue;
                if (curr == theArray[row + 1][col + 1] &&
                    curr == theArray[row + 2][col + 2] &&
                    curr == theArray[row + 3][col + 3]) {
                    displayWinner(g, curr);
                }
            }
        }
        // diag up-right
        for (int row = MAXROW - 1; row >= 3; row--) {
            for (int col = 0; col < MAXCOL - 3; col++) {
                Color curr = theArray[row][col];
                if (curr == Color.WHITE) continue;
                if (curr == theArray[row - 1][col + 1] &&
                    curr == theArray[row - 2][col + 2] &&
                    curr == theArray[row - 3][col + 3]) {
                    displayWinner(g, curr);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < btn.length; i++) {

            if (e.getSource() == btn[i]) {
                putDisk(i);
                return;
            }
        }

        if (e.getSource() == newMI) {
            initialize();
            repaint();
        } else if (e.getSource() == exitMI) {
            System.exit(0);
        } else if (e.getSource() == redMI) {
            if (!gameStart) activeColour = Color.RED;
        } else if (e.getSource() == yellowMI) {
            if (!gameStart) activeColour = Color.YELLOW;
        } else if (e.getSource() == randomPlayer) {

            Connect4JFrame correctClass = this;

            Random rnd = new Random(69);

            Thread th = new Thread() {
                @Override
                public void run() {
                    while (!end) {

                        Color yourColor = activeColour;

                        SwingUtilities.invokeLater(() -> correctClass.putDisk(rnd.nextInt(0, 7)));

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            };

            th.start();

        }
    }

}
