//Authors: Ali Rehman & Musa Mahmood

package chess;

abstract class Piece
{
    ReturnPiece.PieceType pieceType;
    boolean pieceMoved = true;

    public Piece(ReturnPiece.PieceType pieceType)
    {
        this.pieceType = pieceType;
    }

    public abstract boolean canMove(Board board, int preRank, int preFile, int newRank, int newFile);
    public boolean moveAfterCheck(Board board, int preRank, int preFile, int newRank, int newFile, String inProm)
    {
        if(board.checkIfWhiteTurn != board.spaces[preRank][preFile].piece.checkIfWhite())
            return false;
        else if(preRank == newRank && preFile == newFile)
            return false;
        else if(doEmpassantIfAllowed(board, preRank, preFile, newRank, newFile))
            return true;
        else if (doCastleIfAllowed(board, preRank, preFile, newRank, newFile))
            return true;
        else if (canMove(board, preRank, preFile, newRank, newFile))
        {
            Piece prePiece = board.spaces[preRank][preFile].piece;
            Piece newPiece = board.spaces[newRank][newFile].piece;

            placePiece(board, preRank, preFile, null);
            placePiece(board, newRank, newFile, prePiece);

            if(board.checkIfKingInDanger(board.checkIfWhiteTurn))
            {
                placePiece(board, preRank, preFile, prePiece);
                placePiece(board, newRank, newFile, newPiece);
                return false;
            }
            notiTwoJumpPawn(board, preRank, preFile, newRank, newFile);
            promoteIfAllowed(board, newRank, newFile, inProm);
            pieceMoved = false;
            return true;
        }
        else
            return false;
    }

    public boolean analyzeMoveValidity(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        if(board.checkIfWhiteTurn != board.spaces[preRank][preFile].piece.checkIfWhite())
            return false;
        else if(preRank == newRank && preFile == newFile)
            return false;
        else if(canMove(board, preRank, preFile, newRank, newFile))
        {
            Piece prePiece = board.spaces[preRank][preFile].piece;
            Piece newPiece = board.spaces[newRank][newFile].piece;

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


    public boolean checkIfMoveValid(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        if(newRank < 0 || newRank > 7 || newFile < 0 || newFile > 7)
            return false;

        Piece piece = board.spaces[newRank][newFile].piece;
        boolean isLocTheSame = preRank == newRank && preFile == newFile;

        boolean res = ((piece == null && !checkIfSamePlace) || (piece != null && piece.checkIfWhite() != this.checkIfWhite() && !checkIfSamePlace));
        return res;
    }

    public boolean checkIfPieceConsumAtPlace(Board board, int newRank, int newFile)
    {
        boolean res = board.spaces[newRank][newFile].piece != null && board.spaces[newRank][newFile].piece.checkIfWhite() != checkIfWhite();;
        return res;
    }

    public void placePiece(Board board, int rank, int file, Piece piece)
    {
        board.spaces[rank][file].piece = piece;
    }
    public boolean checkIfWhite()
    {
        boolean res = pieceType == ReturnPiece.PieceType.WK || pieceType == ReturnPiece.PieceType.WR || pieceType == ReturnPiece.PieceType.WQ || pieceType == ReturnPiece.PieceType.WP || pieceType == ReturnPiece.PieceType.WN || pieceType == ReturnPiece.PieceType.WB;
        return res;
    }
    public boolean checkIfNotBlockedByPieceFront(Board board, int preRank, int preFile, int newRank, int newFile)
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

            if(board.spaces[x][y].piece != null)
                return false;
        }
        return true;
    }

    public boolean checkIfNotDiaganalBlockFront(Board board, int preRank, int preFile, int newRank, int newFile)
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
            if(board.spaces[preRank + xnegPart * i][preFile + ynegPart * i].piece != null)
                return false;
        }
        return true;
    }

    public boolean checkMoveDoesNotRCheck(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        Piece prePiece = board.spaces[preRank][preFile].piece;
        Piece newPiece = board.spaces[newRank][newFile].piece;

        placePiece(board, preRank, preFile, null);
        placePiece(board, newRank, newFile, prePiece);

        boolean checks = board.checkIfKingInDanger(board.checkIfWhiteTurn);

        placePiece(board, preRank, preFile, prePiece);
        placePiece(board, newRank, newFile, newPiece);

        return !(checks);
    }

    public boolean checkIfCastingAllowed(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        Piece leftRook;
        Piece rightRook;

        if (board.checkIfWhiteTurn)
        {
            leftRook = board.spaces[0][0].piece;
            rightRook = board.spaces[7][0].piece;
        }
        else
        {
            leftRook = board.spaces[0][7].piece;
            rightRook = board.spaces[7][7].piece;
        }

        boolean a, b, c, d, e, f, g, h, k;

        a = !board.checkIfKingInDanger(board.checkIfWhiteTurn);
        b = analyzeMoveValidity(board, preRank, preFile, 5, newFile);
        c = checkMoveDoesNotRCheck(board, preRank, preFile, 6, newFile);
        d = (newRank == 6 && leftRook instanceof Rook && leftRook.pieceMoved && a && b && c && board.spaces[6][preFile].piece == null);
        e = !board.checkIfKingInDanger(board.checkIfWhiteTurn);
        f = analyzeMoveValidity(board, preRank, preFile, 3, newFile);
        g = checkMoveDoesNotRCheck(board, preRank, preFile, 2, newFile);

        h = (this instanceof King) && pieceMoved;
        k = (d || (newRank == 2 && rightRook instanceof Rook && rightRook.pieceMoved && e && f && g && board.spaces[2][preFile].piece == null));

        return h && k;
    }

    public boolean doCastleIfAllowed(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        if(checkIfCastingAllowed(board, preRank, preFile, newRank, newFile))
        {
            if(newRank == 2)
            {
                board.spaces[3][preFile].piece = board.spaces[0][preFile].piece;
                board.spaces[0][preFile].piece = null;

                board.spaces[2][preFile].piece = board.spaces[4][preFile].piece;
                board.spaces[4][preFile].piece = null;

                board.spaces[3][preFile].piece.pieceMoved = false;
                board.spaces[2][preFile].piece.pieceMoved = false;
            }
            else
            {
                board.spaces[5][preFile].piece = board.spaces[7][preFile].piece;
                board.spaces[7][preFile].piece = null;

                board.spaces[6][preFile].piece = board.spaces[4][preFile].piece;
                board.spaces[4][preFile].piece = null;

                board.spaces[5][preFile].piece.pieceMoved = false;
                board.spaces[6][preFile].piece.pieceMoved = false;
            }
            return true;
        }
        else
            return false;
    }

    public boolean doEmpassantIfAllowed(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        if(checkIfEmpassantAllowed(board, preRank, preFile, newRank, newFile))
        {
            Piece pawn = board.spaces[preRank][preFile].piece;
            Piece destroyed = board.spaces[newRank][newFile - (checkIfWhite() ? 1 : -1)].piece;

            board.spaces[preRank][preFile].piece = null;
            board.spaces[newRank][newFile - (checkIfWhite() ? 1 : -1)].piece = null;
            board.spaces[newRank][newFile].piece = pawn;

            if(board.checkIfKingInDanger(checkIfWhite()))
            {
                board.spaces[preRank][preFile].piece = pawn;
                board.spaces[newRank][newFile - (checkIfWhite() ? 1 : -1)].piece = destroyed;
                board.spaces[newRank][newFile].piece = null;
                return false;
            }
            return true;
        }
        else
            return false;
    }

    public boolean checkIfEmpassantAllowed(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        int negPart;
        if(checkIfWhite())
            negPart = 1;
        else
            negPart = -1;

        if(newFile - negPart < 0 || newFile - negPart > 7)
            return false;

        Piece maybePawn = board.spaces[preRank][preFile].piece;
        Piece maybeCapturedPawn = board.spaces[newRank][newFile - negPart].piece;

        boolean a, b, c, d, e, f, g, h, i;

        a = newFile - preFile == negPart;
        b = Math.abs(newRank - preRank) == 1;
        c = board.spaces[newRank][newFile].piece == null;
        d = maybePawn instanceof Pawn;
        e = maybeCapturedPawn instanceof Pawn;
        f = ((Pawn) maybeCapturedPawn).last2JumpTurn == board.turn - 1;
        g = board.spaces[newRank][newFile - negPart].piece != null;
        h = board.spaces[newRank][newFile - negPart].piece.checkIfWhite() != checkIfWhite();
        i = newFile == (checkIfWhite() ? 5 : 2);

        return a && b && c && d && e && f && g && h && i;
    }

    public void notiTwoJumpPawn(Board board, int preRank, int preFile, int newRank, int newFile)
    {
        if(preRank == newRank && Math.abs(newFile - preFile) == 2 && board.spaces[newRank][newFile].piece instanceof Pawn)
            ((Pawn) board.spaces[newRank][newFile].piece).last2JumpTurn = board.turn;
    }

    public void promoteIfAllowed(Board board, int newRank, int newFile, String inProm)
    {
        if((newFile == 7 && checkIfWhite() || newFile == 0 && !checkIfWhite()) && board.spaces[newRank][newFile].piece instanceof Pawn)
        {
            if (inProm.equals("") || inProm.equals("Q"))
                board.spaces[newRank][newFile].piece = new Queen(checkIfWhite());
            else if (inProm.equals("R"))
                board.spaces[newRank][newFile].piece = new Rook(checkIfWhite());
            else if (inProm.equals("N"))
                board.spaces[newRank][newFile].piece = new Knight(checkIfWhite());
            else if (inProm.equals("B"))
                board.spaces[newRank][newFile].piece = new Bishop(checkIfWhite());
            else
                System.out.println("ERROR");
        }
    }
}