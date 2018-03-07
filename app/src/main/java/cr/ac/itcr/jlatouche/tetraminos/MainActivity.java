package cr.ac.itcr.jlatouche.tetraminos;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private final int rowCount = 24;
    private final int columnCount = 12;
    private final int squareSize = 60;
    private GridLayout boardLayout;
    private TextView scoreTextView;
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

        //Setting the score textView
        scoreTextView = findViewById(R.id.scoreTextView);

        //Initializing the game
        game = new Tetris();
        game.init();

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

        //All the pieces, with all their positions and all their squares
        private final Index[][][] Tetraminos = {
                // I-Piece
                {
                        {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(3, 1)},
                        {new Index(1, 0), new Index(1, 1), new Index(1, 2), new Index(1, 3)},
                        {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(3, 1)},
                        {new Index(1, 0), new Index(1, 1), new Index(1, 2), new Index(1, 3)}
                },
                // J-Piece
                {
                        {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(2, 0)},
                        {new Index(1, 0), new Index(1, 1), new Index(1, 2), new Index(2, 2)},
                        {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(0, 2)},
                        {new Index(1, 0), new Index(1, 1), new Index(1, 2), new Index(0, 0)}
                },
                // L-Piece
                {
                        {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(2, 2)},
                        {new Index(1, 0), new Index(1, 1), new Index(1, 2), new Index(0, 2)},
                        {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(0, 0)},
                        {new Index(1, 0), new Index(1, 1), new Index(1, 2), new Index(2, 0)}
                },
                // O-Piece
                {
                        {new Index(0, 0), new Index(0, 1), new Index(1, 0), new Index(1, 1)},
                        {new Index(0, 0), new Index(0, 1), new Index(1, 0), new Index(1, 1)},
                        {new Index(0, 0), new Index(0, 1), new Index(1, 0), new Index(1, 1)},
                        {new Index(0, 0), new Index(0, 1), new Index(1, 0), new Index(1, 1)}
                },
                // S-Piece
                {
                        {new Index(1, 0), new Index(2, 0), new Index(0, 1), new Index(1, 1)},
                        {new Index(0, 0), new Index(0, 1), new Index(1, 1), new Index(1, 2)},
                        {new Index(1, 0), new Index(2, 0), new Index(0, 1), new Index(1, 1)},
                        {new Index(0, 0), new Index(0, 1), new Index(1, 1), new Index(1, 2)}
                },
                // T-Piece
                {
                        {new Index(1, 0), new Index(0, 1), new Index(1, 1), new Index(2, 1)},
                        {new Index(1, 0), new Index(0, 1), new Index(1, 1), new Index(1, 2)},
                        {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(1, 2)},
                        {new Index(1, 0), new Index(1, 1), new Index(2, 1), new Index(1, 2)}
                },
                // Z-Piece
                {
                        {new Index(0, 0), new Index(1, 0), new Index(1, 1), new Index(2, 1)},
                        {new Index(1, 0), new Index(0, 1), new Index(1, 1), new Index(0, 2)},
                        {new Index(0, 0), new Index(1, 0), new Index(1, 1), new Index(2, 1)},
                        {new Index(1, 0), new Index(0, 1), new Index(1, 1), new Index(0, 2)}
                }
        };

        //All the color by piece
        private final int[] tetraminosColors = {
                Color.CYAN, Color.BLUE, Color.WHITE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.RED
        };

        //The piece state
        private Index pieceOrigin;
        private int currentPiece;
        private int rotation;

        //Game state variables
        private final ArrayList<Integer> nextPieces = new ArrayList<>();
        private Runnable autoDropDown;
        private final Handler handler = new Handler();
        private boolean gameOver;

        //Visuals
        private long score = 0;
        private SquareView[][] board;

        //Creates a border around the board and initializes the dropping piece
        private void init() {
            gameOver = false;

            board = new SquareView[rowCount][columnCount];

            //This will initialize the layout
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {

                    SquareView squareView = new SquareView(getApplicationContext());

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = squareSize;
                    params.height = squareSize;
                    params.setMargins(1, 1, 1, 1);

                    squareView.setLayoutParams(params);
                    boardLayout.addView(squareView);

                    if (i == 0 || i == rowCount - 1 || j == 0 || j == columnCount - 1) {
                        squareView.setBackgroundColor(Color.GRAY);
                    } else {
                        squareView.setBackgroundColor(Color.BLACK);
                    }

                    board[i][j] = squareView;
                }
            }
            newPiece();
            automaticDropDown();
        }

        //This will handle when a piece drops
        private void automaticDropDown() {
            autoDropDown = new Runnable() {
                public void run() {
                    if (!gameOver){
                        game.dropDown();
                        handler.postDelayed(this, 1000);
                    }
                }
            };
            handler.postDelayed(autoDropDown, 1000);
        }

        //Puts a new, random piece into the dropping position
        private void newPiece() {

            pieceOrigin = new Index(1, (columnCount / 2) - 1 );
            rotation = 0;

            if (nextPieces.isEmpty() || nextPieces.size() == 1) {
                Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
                Collections.shuffle(nextPieces);
            }
            currentPiece = nextPieces.get(0);
            nextPieces.remove(0);

            if (collidesAt(pieceOrigin.i + 1, pieceOrigin.j, rotation)) {
                gameEnded();
            }
            else {
                drawPiece();
            }
        }

        //Ends the game by stopping the auto drop down runnable and showing the score
        private void gameEnded() {
            gameOver = true;
            handler.removeCallbacksAndMessages(autoDropDown);
            handler.postDelayed(autoDropDown, 5000);
            Log.d("Terminado", "gameEnded: ");
        }

        //Collision test for the dropping piece
        private boolean collidesAt(int i, int j, int rotation) {
            erasePiece();
            for (Index p : Tetraminos[currentPiece][rotation]) {
                if (board[p.i + i][p.j + j].getColor() != Color.BLACK) {
                    return true;
                }
            }
            return false;
        }

        //Rotate the piece clockwise by adding 1
        public void rotate(int r) {
            int newRotation = (rotation + r) % 4;
            if (newRotation < 0) {
                newRotation = 3;
            }
            if (!collidesAt(pieceOrigin.i, pieceOrigin.j, newRotation)) {
                rotation = newRotation;
            }
            drawPiece();
        }

        //Move the piece left or right
        public void move(int j) {
            if (!collidesAt(pieceOrigin.i, pieceOrigin.j + j, rotation)) {
                pieceOrigin.j += j;
            }
            drawPiece();
        }

        //Drops the piece one line or fixes it to the board if it can't drop
        public void dropDown() {
            if (!collidesAt(pieceOrigin.i + 1, pieceOrigin.j, rotation)) {
                pieceOrigin.i += 1;
                drawPiece();
            } else {
                fixToWell();
                newPiece();
            }
        }

        //Make the dropping piece part of the board, so it is available for collision detection
        private void fixToWell() {
            drawPiece();
            for (Index p : Tetraminos[currentPiece][rotation]) {
                board[pieceOrigin.i + p.i][pieceOrigin.j + p.j].setBackgroundColor(tetraminosColors[currentPiece]);
            }
            clearRows();
        }

        //Delete the selected row
        private void deleteRow(int row) {
            for (int i = row - 1; i > 0; i--) {
                for (int j = 1; j < columnCount - 1; j++) {
                    board[i + 1][j].setBackgroundColor(board[i][j].getColor());
                }
            }
        }

        // Clear completed rows from the field and award score according to
        // the number of simultaneously cleared rows.
        private void clearRows() {
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
                    numClears++;
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

        //Erases the piece before checking for collisions
        private void erasePiece() {
            for (Index p : Tetraminos[currentPiece][rotation]) {
                board[p.i + pieceOrigin.i][p.j + pieceOrigin.j].setBackgroundColor(Color.BLACK);
            }
        }

        //Draw the piece again
        private void drawPiece() {
            for (Index p : Tetraminos[currentPiece][rotation]) {
                board[p.i + pieceOrigin.i][p.j + pieceOrigin.j].setBackgroundColor(tetraminosColors[currentPiece]);
            }
            scoreTextView.setText(String.valueOf(score));
        }
    }
}