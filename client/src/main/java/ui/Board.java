package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class Board {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String[] boarder_letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static final String[] boarder_spacing = {"   ", "    ", "    ", "    ", "   ", "    ", "   ", "    "};
    private static Random rand = new Random();
    private static ChessGame chessGame = new ChessGame();


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        setupBoard();

        out.print(ERASE_SCREEN);

        drawHorizontalLineBackwards(out);
        drawBoardBackwards(out);
        drawHorizontalLineBackwards(out);

        drawHorizontalLine(out);
        drawBoard(out);
        drawHorizontalLine(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setupBoard(){
        ChessBoard board = chessGame.getBoard();
        board.resetBoard();
        chessGame.setBoard(board);
    }

    private static void drawHorizontalLineBackwards(PrintStream out) {

        setBlack(out);

        int spacingCol = 0;
        for (int boardCol = BOARD_SIZE_IN_SQUARES-1; boardCol >= 0; --boardCol) {
            if (spacingCol < BOARD_SIZE_IN_SQUARES) {
                out.print(boarder_spacing[spacingCol]);
            }
            printHeaderText(out, boarder_letters[boardCol]);
            spacingCol++;
        }

        out.println();
    }

    private static void drawBoardBackwards(PrintStream out) {

        int row = 1;
        String number = "" + row;
        for (int boardRow = BOARD_SIZE_IN_SQUARES-1; boardRow >= 0; --boardRow) {
            drawVerticalLine(out, number);
            drawRowBackwards(out, boardRow);
            drawVerticalLine(out, number);
            out.println();
            row++;
            number = "" + row;
        }
    }

    private static void drawRowBackwards(PrintStream out, int boardRow) {

        ChessBoard board = chessGame.getBoard();

        for (int boardCol = BOARD_SIZE_IN_SQUARES-1; boardCol >= 0; --boardCol) {
            ChessPosition position = new ChessPosition(boardRow+1, boardCol+1);
            ChessPiece piece = board.getPiece(position);
            if (piece == null) {
                drawBoardSpace(out, EMPTY);
            } else {
                drawBoardSpace(out, getChessPieceCharacter(piece));
            }
        }
    }

    private static void drawHorizontalLine(PrintStream out) {


        setBlack(out);

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            if (boardCol < BOARD_SIZE_IN_SQUARES) {
                out.print(boarder_spacing[boardCol]);
            }
            printHeaderText(out, boarder_letters[boardCol]);
        }

        out.println();
    }

    private static void drawBoard(PrintStream out) {

        int row = BOARD_SIZE_IN_SQUARES;
        String number = "" + row;
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawVerticalLine(out, number);
            drawRow(out, boardRow);
            drawVerticalLine(out, number);
            out.println();
            row--;
            number = "" + row;
        }
    }

    private static void drawRow(PrintStream out, int boardRow) {

        ChessBoard board = chessGame.getBoard();

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            ChessPosition position = new ChessPosition(boardRow+1, boardCol+1);
            ChessPiece piece = board.getPiece(position);
            if (piece == null) {
                drawBoardSpace(out, EMPTY);
            } else {
                drawBoardSpace(out, getChessPieceCharacter(piece));
            }
        }
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(player);
        setBlack(out);
    }
    private static void printPieceText(PrintStream out, String piece) {
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(piece);
        setBlack(out);
    }

    private static void drawBoardSpace(PrintStream out, String piece) {
        setGrey(out);
        printPieceText(out, piece);
        out.print(" ");
    }

    private static void drawVerticalLine(PrintStream out, String number) {
        setBlack(out);
        printPieceText(out, number);
        out.print(" ");
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static String getChessPieceCharacter(ChessPiece piece) {
        ChessPiece.PieceType type = piece.getPieceType();
        ChessGame.TeamColor color = piece.getTeamColor();
        if (type == ChessPiece.PieceType.KING) {
            if (color == ChessGame.TeamColor.WHITE) {
                return WHITE_KING;
            } else {
                return BLACK_KING;
            }
        } else if (type == ChessPiece.PieceType.QUEEN) {
            if (color == ChessGame.TeamColor.WHITE) {
                return WHITE_QUEEN;
            } else {
                return BLACK_QUEEN;
            }
        } else if (type == ChessPiece.PieceType.KNIGHT) {
            if (color == ChessGame.TeamColor.WHITE) {
                return WHITE_KNIGHT;
            } else {
                return BLACK_KNIGHT;
            }
        } else if (type == ChessPiece.PieceType.BISHOP) {
            if (color == ChessGame.TeamColor.WHITE) {
                return WHITE_BISHOP;
            } else {
                return BLACK_BISHOP;
            }
        } else if (type == ChessPiece.PieceType.ROOK) {
            if (color == ChessGame.TeamColor.WHITE) {
                return WHITE_ROOK;
            } else {
                return BLACK_ROOK;
            }
        } else if (type == ChessPiece.PieceType.PAWN) {
            if (color == ChessGame.TeamColor.WHITE) {
                return WHITE_PAWN;
            } else {
                return BLACK_PAWN;
            }
        } else {
            return EMPTY;
        }
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }
}
