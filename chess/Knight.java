//Authors: Ali Rehman; Musa Mahmood

package chess;

public class Knight extends Piece
{
    public Knight(boolean seeIfWhite)
    {
        super(seeIfWhite ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN);
    }

    public boolean canMove(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        int diffX = Math.abs(newRank - preRank);
        int diffY = Math.abs(newFile - preFile);
    
        boolean isMoveValid = checkIfMoveValid(board, preRank, preFile, newRank, newFile);
        boolean checkIfDiffXValid = (diffX == 1 && diffY == 2);
        boolean checkIfDiffYValid = (diffX == 2 && diffY == 1);
    
        return isMoveValid && (checkIfDiffXValid || checkIfDiffYValid);
    }
}