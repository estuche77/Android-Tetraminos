package cr.ac.itcr.jlatouche.tetraminos;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final int rowCount = 24;
    private final int columnCount = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tetris game = new Tetris();
        game.init();
    }

    private class Tetris {

        private final Context context = getApplicationContext();

        private final Square[][][] Tetraminos = {
                // I-Piece
                {
                        {new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 3, 1)},
                        {new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 1, 2), new Square(context, 1, 3)},
                        {new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 3, 1)},
                        {new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 1, 2), new Square(context, 1, 3)}
                },
                // J-Piece
                {
                        {new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 2, 0)},
                        {new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 1, 2), new Square(context, 2, 2)},
                        {new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 0, 2)},
                        {new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 1, 2), new Square(context, 0, 0)}
                },
                // L-Piece
                {
                        {new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 2, 2)},
                        {new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 1, 2), new Square(context, 0, 2)},
                        {new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 0, 0)},
                        {new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 1, 2), new Square(context, 2, 0)}
                },
                // O-Piece
                {
                        {new Square(context, 0, 0), new Square(context, 0, 1), new Square(context, 1, 0), new Square(context, 1, 1)},
                        {new Square(context, 0, 0), new Square(context, 0, 1), new Square(context, 1, 0), new Square(context, 1, 1)},
                        {new Square(context, 0, 0), new Square(context, 0, 1), new Square(context, 1, 0), new Square(context, 1, 1)},
                        {new Square(context, 0, 0), new Square(context, 0, 1), new Square(context, 1, 0), new Square(context, 1, 1)}
                },
                // S-Piece
                {
                        {new Square(context, 1, 0), new Square(context, 2, 0), new Square(context, 0, 1), new Square(context, 1, 1)},
                        {new Square(context, 0, 0), new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 1, 2)},
                        {new Square(context, 1, 0), new Square(context, 2, 0), new Square(context, 0, 1), new Square(context, 1, 1)},
                        {new Square(context, 0, 0), new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 1, 2)}
                },
                // T-Piece
                {
                        {new Square(context, 1, 0), new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1)},
                        {new Square(context, 1, 0), new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 1, 2)},
                        {new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 1, 2)},
                        {new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 2, 1), new Square(context, 1, 2)}
                },
                // Z-Piece
                {
                        {new Square(context, 0, 0), new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 2, 1)},
                        {new Square(context, 1, 0), new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 0, 2)},
                        {new Square(context, 0, 0), new Square(context, 1, 0), new Square(context, 1, 1), new Square(context, 2, 1)},
                        {new Square(context, 1, 0), new Square(context, 0, 1), new Square(context, 1, 1), new Square(context, 0, 2)}
                }
        };

        private final int[] tetraminoColors = {
                Color.CYAN, Color.BLUE, Color.WHITE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED
        };

        private Square pieceOrigin;
        private int currentPiece;
        private int rotation;
        private final ArrayList<Integer> nextPieces = new ArrayList<>();

        private long score;
        private int[][] well;

        // Creates a border around the well and initializes the dropping piece
        private void init() {
            well = new int[rowCount][columnCount];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    if (i == 0 || i == rowCount - 1 || j == 0 || j == columnCount - 1) {
                        well[i][j] = Color.GRAY;
                    } else {
                        well[i][j] = Color.BLACK;
                    }
                }
            }
            newPiece();
            repaint();
        }

        // Put a new, random piece into the dropping position
        public void newPiece() {
            pieceOrigin = new Square(context, 1, 5);
            rotation = 0;
            if (nextPieces.isEmpty()) {
                Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
                Collections.shuffle(nextPieces);
            }
            currentPiece = nextPieces.get(0);
            nextPieces.remove(0);
        }

        // Collision test for the dropping piece
        private boolean collidesAt(int x, int y, int rotation) {
            for (Square p : Tetraminos[currentPiece][rotation]) {
                if (well[p.i + x][p.j + y] != Color.BLACK) {
                    return true;
                }
            }
            return false;
        }

        // Rotate the piece clockwise or counterclockwise
        public void rotate(int i) {
            int newRotation = (rotation + i) % 4;
            if (newRotation < 0) {
                newRotation = 3;
            }
            if (!collidesAt(pieceOrigin.i, pieceOrigin.j, newRotation)) {
                rotation = newRotation;
            }
            repaint();
        }

        // Move the piece left or right
        public void move(int i) {
            if (!collidesAt(pieceOrigin.i, pieceOrigin.j  + i, rotation)) {
                pieceOrigin.j += i;
            }
            repaint();
        }

        // Drops the piece one line or fixes it to the well if it can't drop
        public void dropDown() {
            if (!collidesAt(pieceOrigin.i + 1, pieceOrigin.j, rotation)) {
                pieceOrigin.i += 1;
            } else {
                fixToWell();
            }
            repaint();
        }

        // Make the dropping piece part of the well, so it is available for
        // collision detection.
        public void fixToWell() {
            for (Square p : Tetraminos[currentPiece][rotation]) {
                well[pieceOrigin.i + p.i][pieceOrigin.j + p.j] = tetraminoColors[currentPiece];
            }
            clearRows();
            newPiece();
        }

        public void deleteRow(int row) {
            for (int i = row - 1; i > 0; i--) {
                for (int j = 1; j < columnCount - 1; j++) {
                    well[i + 1][j] = well[i][j];
                }
            }
        }

        // Clear completed rows from the field and award score according to
        // the number of simultaneously cleared rows.
        public void clearRows() {
            boolean gap;
            int numClears = 0;

            for (int i = 1; i < rowCount - 1; i++) {
                gap = false;
                for (int j = 1; j < columnCount - 1; j++) {
                    if (well[i][j] == Color.BLACK) {
                        gap = true;
                        break;
                    }
                }
                if (!gap) {
                    deleteRow(i);
                    i += 1;
                    numClears += 1;
                }
            }

            switch (numClears) {
                case 1:
                    score += 100;
                    break;
                case 2:
                    score += 300;
                    break;
                case 3:
                    score += 500;
                    break;
                case 4:
                    score += 800;
                    break;
            }
        }
        /**
        // Draw the falling piece
        private void drawPiece() {
            for (Square p : Tetraminos[currentPiece][rotation]) {
                imageGrid[p.i + pieceOrigin.i][p.j + pieceOrigin.j].setBackgroundColor(tetraminoColors[currentPiece]);
            }
        }
         **/

        public void repaint() {
            /**
            // Paint the well

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    imageGrid[i][j].setBackgroundColor(well[i][j]);
                }
            }
            // Draw the currently falling piece
            drawPiece();
             **/
        }


    }
}
