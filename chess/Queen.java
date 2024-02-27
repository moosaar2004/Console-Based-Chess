//Authors: Ali Rehman; Musa Mahmood

package chess;

public class Queen extends Piece
{
    public Queen(boolean isWhite)
    {
        super(isWhite ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ);
    }
    public boolean canMove(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        boolean checkIfMoveValid, checkIfDiagonalMove, checkIfNotDiagonalBlockFront, checkIfVerticalOrHorizontalMove, checkIfNotBlockedByPieceFront;
        
        checkIfMoveValid = checkIfMoveValid(board, preRank, preFile, newRank, newFile);
        checkIfDiagonalMove = Math.abs(newRank - preRank) == Math.abs(newFile - preFile);
        checkIfNotDiagonalBlockFront = checkIfNotDiaganalBlockFront(board, preRank, preFile, newRank, newFile);
        checkIfVerticalOrHorizontalMove = (newRank != preRank && newFile == preFile) || (newRank == preRank && newFile != preFile);
        checkIfNotBlockedByPieceFront = checkIfNotBlockedByPieceFront(board, preRank, preFile, newRank, newFile);
    
        return checkIfMoveValid && ((checkIfDiagonalMove && checkIfNotDiagonalBlockFront) || (checkIfVerticalOrHorizontalMove && checkIfNotBlockedByPieceFront));
    }
}
