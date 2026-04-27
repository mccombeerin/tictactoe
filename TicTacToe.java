import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game.
 * 
 * @author Lynn Marshall
 * @version November 8, 2012
 * 
 * @author Erin McCombe
 * @version April 4 2026, Assignment 3 part B
 */

public class TicTacToe implements ActionListener
{
   // game state  
   private String player; // current player
   private String winner; // winner 
   private int numFreeSquares; // free squares
   public static final String PLAYER_X = "X"; // player using "X"
   public static final String PLAYER_O = "O"; // player using "O"
   public static final String TIE = "T"; // game ended in a tie
   
   // button board
   private JButton[][] board = new JButton[3][3]; 
   
   // menu attributes
   private JMenuItem quitItem;
   private JMenuItem newItem;
   
   // label for game status
   private JLabel statusLabel;
   
   // gif for winner/tie
   private static ImageIcon druski = new ImageIcon("druskinew.gif");
   private static ImageIcon tie = new ImageIcon("tie.gif");
   
   
   /** 
    * Constructs a new Tic-Tac-Toe board.
    */
   public TicTacToe()
   {
        // make the JFrame
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.setLayout(new BorderLayout()); 
      
        // menu bar
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
      
        // menu creation
        JMenu fileMenu = new JMenu("Game");
        menubar.add(fileMenu);
      
        // menu items
        newItem = new JMenuItem("New"); // new item
        fileMenu.add(newItem); 
        quitItem = new JMenuItem("Quit"); // quit item
        fileMenu.add(quitItem);
      
        // this allows us to use shortcuts (e.g. Ctrl-R and Ctrl-Q)
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx(); // to save typing
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
        quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));

        // listen for menu selections
        newItem.addActionListener(this); 
        quitItem.addActionListener(this);
    
        // make grid of buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3)); // 3x3 layout 
        buttonPanel.setPreferredSize(new Dimension(450, 450)); // make it bit to start
      
        // init all buttons
        for (int r = 0; r < 3; r ++)
        {
            for (int c = 0; c < 3; c ++)
            {
                board[r][c] = new JButton("");
                board[r][c].setOpaque(true); // for colours
                board[r][c].setFont(new Font("Arial", Font.BOLD, 50));
                board[r][c].addActionListener(this);
                buttonPanel.add(board[r][c]);
            }
        }
        frame.add(buttonPanel, BorderLayout.NORTH);
        
        // make status label
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER); // make centered
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        frame.add(statusLabel, BorderLayout.SOUTH);
        
        // finish setting up the frame and game 
        player = PLAYER_X;
        winner = null; 
        numFreeSquares = 9;
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // exit when we hit the "X"
        frame.pack(); // pack everthing into our frame
        frame.setVisible(true); // it's visible
        clearBoard(); // start game
   }
   
   /** 
    * This action listener is called when the user clicks on 
    * any of the GUI's buttons.
    * 
    * @param e the actionevent
    */
   public void actionPerformed(ActionEvent e)
   {
        Object source = e.getSource(); // get the action 
        
        if (source == newItem)
        {
            clearBoard(); // reset game
            return;
        }
        if (source == quitItem)
        {
            System.exit(0); // quit
        }
        
        JButton clicked = (JButton) source;
        for (int r = 0; r < 3; r ++)
        {
            for (int c = 0; c < 3; c ++)
            {
                // see if its the button we clicked, if it hasnt been clicked yet and if no winner yet
                if (clicked == board[r][c] && clicked.getText().equals("") && winner == null)
                {
                    // Set color based on which player just moved
                    if (player.equals(PLAYER_X)) {
                        clicked.setBackground(Color.CYAN);
                    } else {
                        clicked.setBackground(Color.PINK);
                    }
                    // change button to display X or O
                    clicked.setText(player);
                    clicked.setEnabled(false); // disable button
                    clicked.setOpaque(true);
                    numFreeSquares --;
                    
                    // if this makes us have a winner
                    if(haveWinner(r,c)) {
                        // if a winner print out and declare
                        winner = player;
                        statusLabel.setText("game over, " + winner + " wins!");
                        // colour the label
                        statusLabel.setBackground(Color.GREEN);
                        statusLabel.setOpaque(true);
                        // set GIF
                        setGIF();
                    } else if (numFreeSquares == 0) {
                        // if out of squares its a tie
                        winner = TIE;
                        statusLabel.setText("game over, it's a tie!");
                        // colour the label
                        statusLabel.setBackground(Color.RED);
                        statusLabel.setOpaque(true);
                        // set GIF
                        setGIF();
                    } else {
                        // get next round player and print out its their turn
                        if (player.equals(PLAYER_X)) {
                            player = PLAYER_O;
                        } else {
                            player = PLAYER_X;
                        }
                        statusLabel.setText(player + "'s Turn");
                    }
                }
            }
        }
   }

   /**
    * Sets everything up for a new game.  Marks all squares in the Tic Tac Toe board as empty,
    * and indicates no winner yet, 9 free squares and the current player is player X.
    */
   private void clearBoard()
   {
        for (int r = 0; r < 3; r ++)
        {
            for (int c = 0; c < 3; c ++)
            {
                board[r][c].setText(""); // clear all buttons
                board[r][c].setIcon(null); // clear gif
                board[r][c].setEnabled(true); //reenable buttons
                board[r][c].setBackground(null); // clear colour
            }
        }
        
        player = "X"; // player X always starts
        numFreeSquares = 9; // reset counter
        winner = null; // winner is null
        statusLabel.setText(PLAYER_X + "'s turn"); // update label
        statusLabel.setOpaque(false); // clear background colour 
   }
   
   /**
    * Sets all buttons as the GIF when we get a winner.
    */
   private void setGIF()
   {
        // if its a tie set it as the tie gif
        if (winner == TIE) {
                for (int r = 0; r < 3; r ++)
            {
                for (int c = 0; c < 3; c ++)
                {
                    board[r][c].setText(""); // clear all buttons
                    board[r][c].setIcon(tie); // set all as image
                    board[r][c].setDisabledIcon(tie); // for colour
                    board[r][c].setBackground(null); // clear old colours
                }
            }
        } 
        // otherwise set as the winner gif
        else {
            for (int r = 0; r < 3; r ++)
            {
                for (int c = 0; c < 3; c ++)
                {
                    board[r][c].setText(""); // clear all buttons
                    board[r][c].setIcon(druski); // set all as image
                    board[r][c].setDisabledIcon(druski); // for colour
                    board[r][c].setBackground(null); // clear old colours
                }
            }
        }
   }


   /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise.
    *
    * @param row The row of square just set
    * @param col The column of square just set
    * 
    * @return true if we have a winner, false otherwise
    */
   private boolean haveWinner(int row, int col) 
   {
        // can only win if have at most 4 free squares left
        if (numFreeSquares > 4) return false;
        // check row
        if (board[row][0].getText().equals(player) &&
            board[row][1].getText().equals(player) &&
            board[row][2].getText().equals(player)) return true;

        // check column
        if (board[0][col].getText().equals(player) &&
            board[1][col].getText().equals(player) &&
            board[2][col].getText().equals(player)) return true;

        // check diagonal
        if (board[0][0].getText().equals(player) && board[1][1].getText().equals(player) && board[2][2].getText().equals(player)) return true;
        if (board[0][2].getText().equals(player) && board[1][1].getText().equals(player) && board[2][0].getText().equals(player)) return true;

        return false;
   }
} 