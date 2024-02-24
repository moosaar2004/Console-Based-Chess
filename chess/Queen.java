package chess;

//white = 0 black = 1
public class Queen extends Piece
{
    public String pKind = "";

    public Queen(int w)
    {
        if(w == 0)
            pKind = "w";
        else
            pKind = "b";
    }

    public boolean canMove(char preFile, int preRank, char targetFile, int targetRank, boolean newEmpty)
    {
        if(isWithinBounds(targetFile, targetRank) && isSamePlace(targetFile, targetRank, preFile, preRank) == false)
        {
            if(targetFile == preFile || targetRank == preRank)
                return true;
            if(Math.abs(targetFile - preFile) == Math.abs(targetRank - preRank))
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