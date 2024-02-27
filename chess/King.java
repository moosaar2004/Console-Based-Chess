//Authors: Ali Rehman; Musa Mahmood

package chess;

public class King extends Piece
{

    public King(boolean seeIfWhite)
    {
        super(seeIfWhite ? ReturnPiece.PieceType.WK : ReturnPiece.PieceType.BK);
    }

    public boolean canMove(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        int dX = Math.abs(newRank - preRank);
        int dY = Math.abs(newFile - preFile);

        boolean moveRegular = ((dX == 1 && (dY == 1 || dY == 0) || dY == 1 && dX == 0));
        boolean res = checkIfMoveValid(board, preRank, preFile, newRank, newFile) && moveRegular;

        return res;
    }
}