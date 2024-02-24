package chess;

//white = 0 black = 1
public class King extends Piece
{
    public String pKind = "";
    public King(int w)
    {
        if(w == 0)
            pKind = "w";
        else
            pKind = "b";
    }

    //check if move is valid for king
    //implement check for if new spot is empty
    public boolean canMove(char preFile, int preRank, char targetFile, int targetRank, boolean newEmpty)
    {
        if(isWithinBounds(targetFile, targetRank) && isSamePlace(targetFile, targetRank, preFile, preRank) == false)
        {
            if(Math.abs(targetFile - preFile) + Math.abs(targetRank - preRank) == 1)
                return true;
            if(Math.abs(targetFile - preFile) * Math.abs(targetRank - preRank) == 1)
                return true;
        }
        return false;
    }

    public String dPiece()
    {
        return this.pKind;
    }
    public void move()
    {
        //to be implemented
    }
}