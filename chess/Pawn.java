package chess;

//white = 0 black = 1
public class Pawn extends Piece
{
    public String pKind = "";
    public Pawn(int color, char colFile, int rank)
    {
        super(color, colFile, rank);
        if(color == 0)
            pKind = "w";
        else
            pKind = "b";
    }
}