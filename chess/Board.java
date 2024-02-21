package chess;

import java.util.ArrayList;

//white = 0 black = 1
public class Board
{
    public static ArrayList<Piece> black = new ArrayList<Piece>();
    public static ArrayList<Piece> white = new ArrayList<Piece>();

    static Piece board[][] = new Piece[8][8];

    static void printBoard()
    {
        int n = 8;
        for(int r = 0; r < 8; r++)
        {
            for(int f = 0; f < 8; f++)
            {
                if(board[r][f] == null)
                    System.out.print("## ");
                else
                    System.out.print(board[r][f]);
            }
            System.out.print(n);
            n--;
            System.out.println();
        }
        System.out.println(" a  b  c  d  e  f  g  h");
        System.out.println();
    }
}
