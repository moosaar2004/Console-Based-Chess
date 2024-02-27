//Authors: Ali Rehman; Musa Mahmood

package chess;

public abstract class Piece
{
    ReturnPiece.PieceType pieceType;
    boolean pieceNotMoved = true;

    public Piece(ReturnPiece.PieceType pieceType)
    {
        this.pieceType = pieceType;
    }

    public abstract boolean canMove(ChessBoard board, int preRank, int preFile, int newRank, int newFile);
    public boolean moveAfterCheck(ChessBoard board, int preRank, int preFile, int newRank, int newFile, String prom)
    {
        if(board.checkIfWhiteTurn != board.squares[preRank][preFile].piece.checkIfWhite())
            return false;
        else if(preRank == newRank && preFile == newFile)
            return false;
        else if(doEmpassantIfAllowed(board, preRank, preFile, newRank, newFile))
            return true;
        else if (doCastleIfAllowed(board, preRank, preFile, newRank, newFile))
            return true;
        else if (canMove(board, preRank, preFile, newRank, newFile))
        {
            Piece prePiece = board.squares[preRank][preFile].piece;
            Piece newPiece = board.squares[newRank][newFile].piece;

            placePiece(board, preRank, preFile, null);
            placePiece(board, newRank, newFile, prePiece);

            if(board.checkIfKingInDanger(board.checkIfWhiteTurn))
            {
                placePiece(board, preRank, preFile, prePiece);
                placePiece(board, newRank, newFile, newPiece);
                return false;
            }
            notiTwoJumpPawn(board, preRank, preFile, newRank, newFile);
            promoteIfAllowed(board, newRank, newFile, prom);
            pieceNotMoved = false;
            return true;
        }
        else
            return false;
    }

    public boolean analyzeMoveValidity(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        if(board.checkIfWhiteTurn != board.squares[preRank][preFile].piece.checkIfWhite())
            return false;
        else if(preRank == newRank && preFile == newFile)
            return false;
        else if(canMove(board, preRank, preFile, newRank, newFile))
        {
            Piece prePiece = board.squares[preRank][preFile].piece;
            Piece newPiece = board.squares[newRank][newFile].piece;

            placePiece(board, preRank, preFile, null);
            placePiece(board, newRank, newFile, prePiece);

            boolean checks = board.checkIfKingInDanger(board.checkIfWhiteTurn);

            placePiece(board, preRank, preFile, prePiece);
            placePiece(board, newRank, newFile, newPiece);

            return !(checks);
        }
        else
            return false;
    }


    public boolean checkIfMoveValid(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        if(newRank < 0 || newRank > 7 || newFile < 0 || newFile > 7)
            return false;

        Piece piece = board.squares[newRank][newFile].piece;
        boolean checkIfSamePlace = preRank == newRank && preFile == newFile;

        boolean res = ((piece == null && !checkIfSamePlace) || (piece != null && piece.checkIfWhite() != this.checkIfWhite() && !checkIfSamePlace));
        return res;
    }

    public boolean checkIfPieceConsumAtPlace(ChessBoard board, int newRank, int newFile)
    {
        boolean res = board.squares[newRank][newFile].piece != null && board.squares[newRank][newFile].piece.checkIfWhite() != checkIfWhite();;
        return res;
    }

    public void placePiece(ChessBoard board, int rank, int file, Piece piece)
    {
        board.squares[rank][file].piece = piece;
    }
    public boolean checkIfWhite()
    {
        boolean res = pieceType == ReturnPiece.PieceType.WK || pieceType == ReturnPiece.PieceType.WR || pieceType == ReturnPiece.PieceType.WQ || pieceType == ReturnPiece.PieceType.WP || pieceType == ReturnPiece.PieceType.WN || pieceType == ReturnPiece.PieceType.WB;
        return res;
    }

    public boolean checkIfNotBlockedByPieceFront(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        int negPart;
        
        if (newRank == preRank && newFile > preFile)
            negPart = -1;
        else if (newFile == preFile && newRank > preRank)
            negPart = -1;
        else
            negPart = 1;

        int bound = Math.abs(preRank - newRank + preFile - newFile);
        for(int i = 1; i < bound; i++)
        {
            int x, y;
            if (newRank == preRank)
                x = newRank;
            else
                x = newRank + (negPart * i);

            if (newFile == preFile)
                y = newFile;
            else
                y = newFile + (negPart * i);

            if(x < 0 || x > 7 || y < 0 || y > 7)
                continue;

            if(board.squares[x][y].piece != null)
                return false;
        }
        return true;
    }

    public boolean checkIfNotDiaganalBlockFront(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        int xnegPart;
        int ynegPart;

        if (newRank > preRank)
            xnegPart = 1;
        else
            xnegPart = -1;

        if (newFile > preFile)
            ynegPart = 1;
        else
            ynegPart = -1;

        int bound = Math.abs(preRank - newRank);
        for(int i = 1; i < bound; i++)
        {
            if(board.squares[preRank + xnegPart * i][preFile + ynegPart * i].piece != null)
                return false;
        }
        return true;
    }

    public boolean checkMoveDoesNotRCheck(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        Piece prePiece = board.squares[preRank][preFile].piece;
        Piece newPiece = board.squares[newRank][newFile].piece;

        placePiece(board, preRank, preFile, null);
        placePiece(board, newRank, newFile, prePiece);

        boolean checks = board.checkIfKingInDanger(board.checkIfWhiteTurn);

        placePiece(board, preRank, preFile, prePiece);
        placePiece(board, newRank, newFile, newPiece);

        return !(checks);
    }

    public boolean checkIfCastingAllowed(ChessBoard board, int preRank, int preFile, int newRank, int newFile) {
        Piece lRook = board.checkIfWhiteTurn ? board.squares[0][0].piece : board.squares[0][7].piece;
        Piece rRook = board.checkIfWhiteTurn ? board.squares[7][0].piece : board.squares[7][7].piece;

        boolean res = (this instanceof King) && pieceNotMoved &&
        ((newRank == 6 &&
                lRook instanceof Rook &&
                lRook.pieceNotMoved &&
                !board.checkIfKingInDanger(board.checkIfWhiteTurn) &&
                analyzeMoveValidity(board, preRank, preFile, 5, newFile) &&
                checkMoveDoesNotRCheck(board, preRank, preFile, 6, newFile) &&
                board.squares[6][preFile].piece == null)
                || (newRank == 2 &&
                rRook instanceof Rook &&
                rRook.pieceNotMoved &&
                !board.checkIfKingInDanger(board.checkIfWhiteTurn) &&
                analyzeMoveValidity(board, preRank, preFile, 3, newFile) &&
                checkMoveDoesNotRCheck(board, preRank, preFile, 2, newFile) &&
                board.squares[2][preFile].piece == null));

        return res;
    }

    public boolean doCastleIfAllowed(ChessBoard board, int preRank, int preFile, int newRank, int newFile) {

        if(checkIfCastingAllowed(board, preRank, preFile, newRank, newFile)) {
            if(newRank == 2) {
                board.squares[3][preFile].piece = board.squares[0][preFile].piece;
                board.squares[0][preFile].piece = null;

                board.squares[2][preFile].piece = board.squares[4][preFile].piece;
                board.squares[4][preFile].piece = null;

                board.squares[3][preFile].piece.pieceNotMoved = false;
                board.squares[2][preFile].piece.pieceNotMoved = false;
            } else {
                board.squares[5][preFile].piece = board.squares[7][preFile].piece;
                board.squares[7][preFile].piece = null;

                board.squares[6][preFile].piece = board.squares[4][preFile].piece;
                board.squares[4][preFile].piece = null;

                board.squares[5][preFile].piece.pieceNotMoved = false;
                board.squares[6][preFile].piece.pieceNotMoved = false;
            }

            return true;
        }

        return false;
    }

    public boolean doEmpassantIfAllowed(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        if(checkIfEmpassantAllowed(board, preRank, preFile, newRank, newFile))
        {
            Piece pawn = board.squares[preRank][preFile].piece;
            Piece destroyed = board.squares[newRank][newFile - (checkIfWhite() ? 1 : -1)].piece;

            board.squares[preRank][preFile].piece = null;
            board.squares[newRank][newFile - (checkIfWhite() ? 1 : -1)].piece = null;
            board.squares[newRank][newFile].piece = pawn;

            if(board.checkIfKingInDanger(checkIfWhite()))
            {
                board.squares[preRank][preFile].piece = pawn;
                board.squares[newRank][newFile - (checkIfWhite() ? 1 : -1)].piece = destroyed;
                board.squares[newRank][newFile].piece = null;
                return false;
            }
            return true;
        }
        else
            return false;
    }


    public boolean checkIfEmpassantAllowed(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        int negPart;
        if(checkIfWhite())
            negPart = 1;
        else
            negPart = -1;

        int capFile = newFile - negPart;
    
        if (capFile < 0 || capFile > 7)
            return false;
    
        Piece maybePawn = board.squares[preRank][preFile].piece;
        Piece maybeCapturedPawn = board.squares[newRank][capFile].piece;
    
        if (newFile - preFile != negPart)
            return false;
    
        else if (Math.abs(newRank - preRank) != 1)
            return false;
    
        else if (board.squares[newRank][newFile].piece != null)
            return false;
    
        else if (!(maybePawn instanceof Pawn) || !(maybeCapturedPawn instanceof Pawn))
            return false;
    
        else if (((Pawn) maybeCapturedPawn).last2JumpTurn != board.turn - 1)
            return false;
    
        else if (board.squares[newRank][capFile].piece == null)
            return false;
    
        else if (board.squares[newRank][capFile].piece.checkIfWhite() == checkIfWhite())
            return false;
    
        else if (newFile != (checkIfWhite() ? 5 : 2))
            return false;
        else
            return true;
    }

    public void notiTwoJumpPawn(ChessBoard board, int preRank, int preFile, int newRank, int newFile)
    {
        if(preRank == newRank && Math.abs(newFile - preFile) == 2 && board.squares[newRank][newFile].piece instanceof Pawn)
            ((Pawn) board.squares[newRank][newFile].piece).last2JumpTurn = board.turn;
    }

    public void promoteIfAllowed(ChessBoard board, int newRank, int newFile, String prom)
    {
        if((newFile == 7 && checkIfWhite() || newFile == 0 && !checkIfWhite()) && board.squares[newRank][newFile].piece instanceof Pawn)
        {
            if (prom.equals("") || prom.equals("Q"))
                board.squares[newRank][newFile].piece = new Queen(checkIfWhite());
            else if (prom.equals("R"))
                board.squares[newRank][newFile].piece = new Rook(checkIfWhite());
            else if (prom.equals("N"))
                board.squares[newRank][newFile].piece = new Knight(checkIfWhite());
            else if (prom.equals("B"))
                board.squares[newRank][newFile].piece = new Bishop(checkIfWhite());
            else
                System.out.println("ERROR");
        }
    }
}