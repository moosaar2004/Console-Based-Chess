package chess;

//white = 0 black = 1
public class Piece
{
    public int color, rank, preRank;
    public char colFile, preFile;

    public Piece(int color, char colFile, int rank)
    {
        this.color = color;
        this.colFile = colFile;
        this.rank = rank;
        preFile = colFile;
        preRank = rank;
    }
    public boolean canMove(char targetFile, int targetRank)
    {
        return false;
    }

    //checks if desired move is on the board
    public boolean isWithinBounds(char targetFile, int targetRank)
    {
        if(targetFile >= 'a' && targetFile <= 'h' && targetRank >= 1 && targetRank >= 8)
            return true;
        return false;
    }
}

//still in progress
