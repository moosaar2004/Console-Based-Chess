package chess;

//white = 0 black = 1
public abstract class Piece
{
    private int white = 0, black = 1;
    public boolean fMove = true;

    public void setWhite(int white)
    {
        this.white = white;
    }

    public boolean isWhite(int white)
    {
        if(white == 0)
        {
            return true;
        }
        return false;
    }
    //checks if desired move is on the board
    public boolean isWithinBounds(char targetFile, int targetRank)
    {
        if(targetFile >= 'a' && targetFile <= 'h' && targetRank >= 1 && targetRank <= 8)
            return true;
        return false;
    }
    public boolean isSamePlace(int targetFile, int targetRank, int preFile, int preRank)
    {
        if(targetFile == preFile && targetRank == preRank)
            return true;
        return false;
    }

    public abstract boolean canMove(char preFile, int preRank, char targetFile, int targetRank, boolean newEmpty);
    public abstract void move();
    public abstract String dPiece();  
}

/**still in progress
*/