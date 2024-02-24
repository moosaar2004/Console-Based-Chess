package chess;

//white = 0 black = 1
public class Knight extends Piece{
    public String pKind = "";

    public Knight(int w)
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
            if(Math.abs(targetFile - preFile) * Math.abs(targetRank - preRank) == 2)
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