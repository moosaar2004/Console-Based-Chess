//Authors: Ali Rehman; Musa Mahmood

package chess;

public class Pawn extends Piece
{

    long last2JumpTurn = -1;

    public Pawn(boolean isWhite)
    {
        super(isWhite ? ReturnPiece.PieceType.WP : ReturnPiece.PieceType.BP);
    }


    public boolean canMove(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        int mvComp;
        if (checkIfWhite())
            mvComp = 1;
        else
            mvComp = -1;

        boolean checkIfMoveValid, checkIfSameRank, checkIfDiffXValid, checkIfDiffXValidWithPieceMoved, checkIfDiffYValid;

        checkIfMoveValid = checkIfMoveValid(board, preRank, preFile, newRank, newFile);
        checkIfSameRank = (preRank == newRank);
        checkIfDiffXValid = (newFile - preFile == mvComp && board.squares[newRank][newFile].piece == null);
        checkIfDiffXValidWithPieceMoved = (this.pieceMoved && newFile - preFile == 2 * mvComp && newRank == preRank && board.squares[preRank][preFile + mvComp].piece == null && board.squares[newRank][newFile].piece == null);
        checkIfDiffYValid = (Math.abs(newRank - preRank) == 1 && newFile - preFile == mvComp && checkIfPieceConsumAtPlace(board, newRank, newFile));
    
        return checkIfMoveValid && (checkIfSameRank && checkIfDiffXValid || checkIfDiffXValidWithPieceMoved || checkIfDiffYValid);
    }
}