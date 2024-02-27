//Authors: Ali Rehman; Musa Mahmood

package chess;

public class Bishop extends Piece
{
    public Bishop(boolean seeIfWhite)
    {
        super(seeIfWhite ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB);
    }

    public boolean canMove(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        boolean a, b;
        a = checkIfMoveValid(board, preRank, preFile, newRank, newFile);
        b = (Math.abs(newRank - preRank) == Math.abs(newFile - preFile) && checkIfNotDiaganalBlockFront(board, preRank, preFile, newRank, newFile));

        return a && b;
    }
}