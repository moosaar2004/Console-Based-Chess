//Authors: Ali Rehman; Musa Mahmood

package chess;

import java.util.ArrayList;

public class ChessBoard
{
    Square[][] squares = new Square[8][8];
    boolean checkIfWhiteTurn = true;
    boolean checkIfGameFinished = false;

    long turn = 0;

    public ChessBoard()
    {
        for (int r = 0; r < 8; r++)
        {
            for (int f = 0; f < 8; f++)
                squares[r][f] = new Square(r, f);
        }
    
        for (int x = 0; x < 2; x++)
        {
            if (x == 0)
            {
                squares[0][0].piece = new Rook(true);
                squares[1][0].piece = new Knight(true);
                squares[2][0].piece = new Bishop(true);
                squares[3][0].piece = new Queen(true);
                squares[4][0].piece = new King(true);
                squares[5][0].piece = new Bishop(true);
                squares[6][0].piece = new Knight(true);
                squares[7][0].piece = new Rook(true);
            }
            else
            {
                squares[0][7].piece = new Rook(false);
                squares[1][7].piece = new Knight(false);
                squares[2][7].piece = new Bishop(false);
                squares[3][7].piece = new Queen(false);
                squares[4][7].piece = new King(false);
                squares[5][7].piece = new Bishop(false);
                squares[6][7].piece = new Knight(false);
                squares[7][7].piece = new Rook(false);
            }
        }
    
        for (int c = 0; c < 8; c++)
        {
            squares[c][1].piece = new Pawn(true);
            squares[c][6].piece = new Pawn(false);
        }
    }

    public ReturnPlay processGivenMove(String move)
    {
        ReturnPlay play = new ReturnPlay();
        play.piecesOnBoard = receiveActivePieces();

        if(checkIfGameFinished)
            return play;

        if(move.equals("resign"))
        {
            if(checkIfWhiteTurn)
                play.message = ReturnPlay.Message.RESIGN_BLACK_WINS;
            else
                play.message = ReturnPlay.Message.RESIGN_WHITE_WINS;

            return play;
        }

        String[] additionalInput = move.split(" ");
        boolean checkIfDraw = false, checkIfProm = false;
        if (additionalInput.length == 3)
        {
            String lastPart = additionalInput[2];
            if (lastPart.equals("draw?"))
                checkIfDraw = true;
            else if (lastPart.equals("R") || lastPart.equals("B") || lastPart.equals("N") || lastPart.equals("Q"))
                checkIfProm = true;
        }

        if((additionalInput.length != 2 && !checkIfDraw && !checkIfProm) || (additionalInput[0].length() != 2 || additionalInput[1].length() != 2))
        {
            play.message = ReturnPlay.Message.ILLEGAL_MOVE;

            return play;
        }

        int preRank = pieceFileToX(String.valueOf(additionalInput[0].charAt(0)));
        int preFile = Character.getNumericValue(additionalInput[0].charAt(1)) - 1;

        int newRank = pieceFileToX(String.valueOf(additionalInput[1].charAt(0)));
        int newFile = Character.getNumericValue(additionalInput[1].charAt(1)) - 1;

        if(!checkIfOnBoard(preRank, preFile) || !checkIfOnBoard(newRank, newFile))
        {
            play.message = ReturnPlay.Message.ILLEGAL_MOVE;

            return play;
        }

        Piece piece = squares[preRank][preFile].piece;

        if(piece == null)
        {
            play.message = ReturnPlay.Message.ILLEGAL_MOVE;

            return play;
        }

        if(piece.moveAfterCheck(this, preRank, preFile, newRank, newFile, additionalInput.length == 3 ? additionalInput[2] : ""))
        {
            play.piecesOnBoard = receiveActivePieces();
            checkIfWhiteTurn = !checkIfWhiteTurn;

            if (checkIfDraw)
                play.message = ReturnPlay.Message.DRAW;
            else if (checkIfKingInDanger(checkIfWhiteTurn))
            {
                if (checkIfCheckmate(checkIfWhiteTurn, newRank, newFile))
                {
                    if (checkIfWhiteTurn)
                        play.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
                    else
                        play.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
                }
                else
                    play.message = ReturnPlay.Message.CHECK;
            }
            else
                play.message = null;

            checkIfGameFinished = play.message != ReturnPlay.Message.CHECK && play.message != null;

            turn++;

            return play;
        }

        play.message = ReturnPlay.Message.ILLEGAL_MOVE;

        return play;
    }

    public boolean checkIfKingInDanger(boolean seeIfWhite)
    {
        Square king = null;

        for(int r = 0; r < 8; r++)
        {
            for(int f = 0; f < 8; f++)
            {
                if(squares[r][f].piece != null && squares[r][f].piece instanceof King && squares[r][f].piece.checkIfWhite() == seeIfWhite)
                {
                    king = squares[r][f];
                }
            }
        }

        if(king == null)
            return false;

        for(int x = 0; x < 8; x++)
        {
            for(int y = 0; y < 8; y++)
            {
                if(squares[x][y].piece != null && squares[x][y].piece.canMove(this, x, y, king.x, king.y))
                    return true;
            }
        }
        
        return false;
    }

    public boolean checkIfCheckmate(boolean seeIfWhite, int dRank, int dFile) {
        Square king = null;

        for(int r = 0; r < 8; r++)
        {
            for(int f = 0; f < 8; f++)
            {
                if(squares[r][f].piece != null && squares[r][f].piece instanceof King && squares[r][f].piece.checkIfWhite() == seeIfWhite)
                    king = squares[r][f];
            }
        }

        if(king == null)
            return false;

        for(int x = -1; x < 2; x++)
        {
            for(int y = -1; y < 2; y++)
            {
                if(king.piece.analyzeMoveValidity(this, king.x, king.y, king.x + x, king.y + y))
                    return false;
            }
        }

        for(int r = 0; r < 8; r++)
        {
            for(int f = 0; f < 8; f++)
            {
                if (squares[r][f] == king)
                    continue;
                if(squares[r][f].piece != null && squares[r][f].piece.checkIfWhite() == seeIfWhite)
                {
                    if(squares[r][f].piece.canMove(this, r, f, dRank, dFile))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public ArrayList<ReturnPiece> receiveActivePieces()
    {
        ArrayList<ReturnPiece> activePieces = new ArrayList<ReturnPiece>();

        for(int r = 0; r < 8; r++)
        {
            for(int f = 0; f < 8; f++)
            {
                if(squares[r][f].piece != null)
                {
                    ReturnPiece returnPiece = new ReturnPiece();
                    returnPiece.pieceRank = f + 1;
                    returnPiece.pieceFile = xToPieceFile(r);
                    returnPiece.pieceType = squares[r][f].piece.pieceType;

                    activePieces.add(returnPiece);
                }
            }
        }

        return activePieces;
    }


    public ReturnPiece.PieceFile xToPieceFile(int x)
    {
        if (x == 0)
            return ReturnPiece.PieceFile.a;
        else if (x == 1)
            return ReturnPiece.PieceFile.b;
        else if (x == 2)
            return ReturnPiece.PieceFile.c;
        else if (x == 3)
            return ReturnPiece.PieceFile.d;
        else if (x == 4)
            return ReturnPiece.PieceFile.e;
        else if (x == 5)
            return ReturnPiece.PieceFile.f;
        else if (x == 6)
            return ReturnPiece.PieceFile.g;
        else if (x == 7)
            return ReturnPiece.PieceFile.h;
        else
            return null;
    }




    public int pieceFileToX(String pieceFile)
    {
        String[] files = {"a", "b", "c", "d", "e", "f", "g", "h"};
    
        for (int i = 0; i < files.length; i++)
        {
            if (files[i].equals(pieceFile))
                return i;
        }
    
        return -1;
    }

    public boolean checkIfOnBoard(int r, int f)
    {
        boolean res = (r >= 0 && r <= 7) && (f >= 0 && f <= 7);
        return res;
    }
}