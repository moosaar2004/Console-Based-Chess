//Authors: Ali Rehman; Musa Mahmood

package chess;

public class Rook extends Piece
{

    public Rook(boolean isWhite)
    {
        super(isWhite ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR);
    }

    public boolean canMove(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        boolean checkIfMoveValid, checkIfHorizontalMove, checkIfVerticalMove, checkIfNotBlockedByPiece;

        checkIfMoveValid = checkIfMoveValid(board, preRank, preFile, newRank, newFile);
        checkIfHorizontalMove = newRank != preRank && newFile == preFile;
        checkIfVerticalMove = newRank == preRank && newFile != preFile;
        checkIfNotBlockedByPiece = checkIfNotBlockedByPieceFront(board, preRank, preFile, newRank, newFile);
        
        return checkIfMoveValid && (checkIfHorizontalMove || checkIfVerticalMove) && checkIfNotBlockedByPiece;
    }
}