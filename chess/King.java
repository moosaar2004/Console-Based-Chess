package chess;

//white = 0 black = 1
public class King extends Piece
{
    public String pKind = "";
    public King(int color, char colFile, int rank)
    {
        super(color, colFile, rank);
        if(color == 0)
            pKind = "w";
        else
            pKind = "b";
    }

    //check if move is valid for king
    public boolean canMove(char targetFile, int targetRank)
    {
        if(isWithinBounds(targetFile, targetRank))
        {
            //for moving one position up/down/right/left
            if(Math.abs(targetFile - preFile) + Math.abs(targetRank - preRank) == 1)
                return true;
            //for moving one position diagonally
            if(Math.abs(targetFile - preFile) * Math.abs(targetRank - preRank) == 1)
                return true;
        }
        return false;
    }
}

//in progress