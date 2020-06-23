package sioegve.tictactoe.model;

public class TicTacToe {


    private final Player[][] board;
    private int moves_played;
    private Player winner;

    enum Player { X,O }
    private Player currentPlayer;

    public Player getWinner()
    {
        return winner;
    }

    public TicTacToe()
    {
        currentPlayer = Player.X;
        board = new Player[3][3];
        moves_played = 0;
        winner = null;
    }


    private void switchPlayer()
    {
        currentPlayer =  (currentPlayer==Player.X) ? Player.O : Player.X;
    }

    public void next_move(int row_index, int column_index)
    {
        moves_played ++;
        board[row_index][column_index] = currentPlayer;


        if(this.check_if_winner())
        {
            this.winner = currentPlayer;
        }
        else {
            this.switchPlayer();
        }

    }

    private boolean check_if_winner() {

        for (int i = 0; i < 3; i++) {
            if(board[i][0]!=null && board[i][0]==board[i][1] && board[i][1]== board[i][2])
                return true;
        }

        for (int i = 0; i < 3; i++) {
            if(board[0][i]!=null && board[0][i]==board[1][i] && board[1][i]== board[2][i])
                return true;
        }


        if(board[0][0]!=null && board[0][0]==board[1][1] && board[1][1]== board[2][2])
                return true;

        if(board[2][0]!=null && board[2][0]==board[1][1] && board[1][1]== board[0][2])
            return true;


        return false;
    }

    public boolean game_over()
    {
        return this.winner!=null  || this.moves_played == 9;
    }

}
