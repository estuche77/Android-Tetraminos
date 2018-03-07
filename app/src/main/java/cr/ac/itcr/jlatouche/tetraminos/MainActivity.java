package cr.ac.itcr.jlatouche.tetraminos;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final int rowCount = 24;
    private final int columnCount = 12;
    private final int squareSize = 60;
    private GridLayout boardLayout;
    private Tetris game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting the board
        boardLayout = findViewById(R.id.boardLayout);
        boardLayout.setRowCount(rowCount);
        boardLayout.setColumnCount(columnCount);
        boardLayout.setBackgroundColor(Color.BLACK);

        //Initializing the game
        game = new Tetris();
        game.init();

        //This will handle when a piece drops
        final Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                game.dropDown();
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
    }

    public void onMoveLeftClick(View view) {
        game.move(-1);
    }

    public void onMoveRightClick(View view) {
        game.move(1);
    }

    public void onMoveDownClick(View view) {
        game.dropDown();
    }

    public void onRotateClick(View view) {
        game.rotate(1);
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
        private Square[][] board;

        //Creates a border around the board and initializes the dropping piece
        private void init() {

            board = new Square[rowCount][columnCount];

            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {

                    Square square = new Square(context, i, j);

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = squareSize;
                    params.height = squareSize;
                    params.setMargins(1, 1, 1, 1);

                    square.setLayoutParams(params);
                    boardLayout.addView(square);

                    if (i == 0 || i == rowCount - 1 || j == 0 || j == columnCount - 1) {
                        square.setBackgroundColor(Color.GRAY);
                    } else {
                        square.setBackgroundColor(Color.BLACK);
                    }

                    board[i][j] = square;
                }
            }
            newPiece();
        }

        //Puts a new, random piece into the dropping position
        public void newPiece() {
            pieceOrigin = new Square(context, 1, 5);
            rotation = 0;
            if (nextPieces.isEmpty()) {
                Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
                Collections.shuffle(nextPieces);
            }
            currentPiece = nextPieces.get(0);
            nextPieces.remove(0);

            drawPiece();
        }

        // Collision test for the dropping piece
        private boolean collidesAt(int i, int j, int rotation) {
            for (Square p : Tetraminos[currentPiece][rotation]) {
                if (board[p.i + i][p.j + j].getColor() != Color.BLACK) {
                    return true;
                }
            }
            return false;
        }

        // Rotate the piece clockwise or counterclockwise
        public void rotate(int r) {
            erasePiece();
            int newRotation = (rotation + r) % 4;
            if (newRotation < 0) {
                newRotation = 3;
            }
            if (!collidesAt(pieceOrigin.i, pieceOrigin.j, newRotation)) {
                rotation = newRotation;
            }
            drawPiece();
        }

        // Move the piece left or right
        public void move(int j) {
            erasePiece();
            if (!collidesAt(pieceOrigin.i, pieceOrigin.j + j, rotation)) {
                pieceOrigin.j += j;
            }
            drawPiece();
        }

        // Drops the piece one line or fixes it to the well if it can't drop
        public void dropDown() {
            erasePiece();
            if (!collidesAt(pieceOrigin.i + 1, pieceOrigin.j, rotation)) {
                pieceOrigin.i += 1;
                drawPiece();
            } else {
                fixToWell();
            }
        }

        // Make the dropping piece part of the well, so it is available for
        // collision detection.
        public void fixToWell() {
            drawPiece();
            for (Square p : Tetraminos[currentPiece][rotation]) {
                board[pieceOrigin.i + p.i][pieceOrigin.j + p.j].setBackgroundColor(tetraminoColors[currentPiece]);
            }
            clearRows();
            newPiece();
        }

        public void deleteRow(int row) {
            for (int i = row - 1; i > 0; i--) {
                for (int j = 1; j < columnCount - 1; j++) {
                    board[i + 1][j].setBackgroundColor(board[i][j].getColor());
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
                    if (board[i][j].getColor() == Color.BLACK) {
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

        public void erasePiece() {
            for (Square p : Tetraminos[currentPiece][rotation]) {
                board[p.i + pieceOrigin.i][p.j + pieceOrigin.j].setBackgroundColor(Color.BLACK);
            }
        }

        public void drawPiece() {
            for (Square p : Tetraminos[currentPiece][rotation]) {
                board[p.i + pieceOrigin.i][p.j + pieceOrigin.j].setBackgroundColor(tetraminoColors[currentPiece]);
            }
        }
    }
}