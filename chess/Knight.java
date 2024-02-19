package chess;

//white = 0 black = 1
public class Knight extends Piece{
    public String pKind = "";
    public Knight(int color, char colFile, int rank)
    {
        super(color, colFile, rank);
        if(color == 0)
            pKind = "w";
        else
            pKind = "b";
    }
}