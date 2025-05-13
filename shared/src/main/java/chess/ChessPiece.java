package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, teamColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceType=" + pieceType +
                ", teamColor=" + teamColor +
                '}';
    }

    private final ChessPiece.PieceType pieceType;
    private final ChessGame.TeamColor teamColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> movePossibilities = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (pieceType == PieceType.PAWN){
            int [][] pawnMoves;
            int [][] pawnMoves_capt;
            if (teamColor == ChessGame.TeamColor.WHITE){
                pawnMoves_capt = new int[][]{{+1,+1}, {+1,-1}};
                if(row == 2){
                    pawnMoves = new int[][] {{+1,0}, {+2,0}};
                }else{
                    pawnMoves = new int[][] {{+1,0}};
                }
            }else{
                pawnMoves_capt = new int[][] {{-1,-1}, {-1,+1}};
                if (row == 7){
                    pawnMoves = new int[][] {{-1,0}, {-2,0}};
                }else{
                    pawnMoves = new int[][] {{-1,0}};
                }
            }

            for (int[] possibility : pawnMoves){
                int newRow = row;
                newRow += possibility[0];
                int newCol = col;
                newCol += possibility[1];
                if(inBounds(newRow, newCol)){
                    ChessPosition newSpot = new ChessPosition(newRow,newCol);
                    if(row == 1 && teamColor == ChessGame.TeamColor.WHITE){
                        if(board.getPiece(newSpot) == null){
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.QUEEN));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.ROOK));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.KNIGHT));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.BISHOP));
                        }
                    }else if(row == 8 && teamColor == ChessGame.TeamColor.BLACK) {
                        if(board.getPiece(newSpot)== null){
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.QUEEN));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.ROOK));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.KNIGHT));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.BISHOP));
                        }
                    }else{
                        movePossibilities.add(new ChessMove(myPosition, newSpot, null));
                    }
                }
            }
            for (int[] possibility : pawnMoves_capt){
                int newRow = row + possibility[0];
                int newCol = col + possibility[1];
                if(inBounds(newRow, newCol)){
                    ChessPosition newSpot = new ChessPosition(newRow, newCol);
                    if(row == 1 && teamColor == ChessGame.TeamColor.WHITE){
                        if(board.getPiece(newSpot).teamColor != board.getPiece(myPosition).teamColor){
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.QUEEN));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.ROOK));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.KNIGHT));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.BISHOP));
                        }
                    }else if(row == 8 && teamColor == ChessGame.TeamColor.BLACK) {
                        if (board.getPiece(newSpot).teamColor != board.getPiece(myPosition).teamColor) {
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.QUEEN));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.ROOK));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.KNIGHT));
                            movePossibilities.add(new ChessMove(myPosition, newSpot, PieceType.BISHOP));
                        }
                    }
                }
            }
        }else if(pieceType == PieceType.ROOK){
            int [][] rookMoves = {
                    {+1,0}, {-1,0}, {0,+1}, {0,-1}
            };
            for (int[] possibility : rookMoves){
                int newRow = row;
                int newCol = col;
                while(true){
                    if(!inBounds(newRow, newCol)){
                        break;
                    }
                    newRow += possibility[0];
                    newCol += possibility[1];
                    if (inBounds(newRow, newCol)) {
                        ChessPosition newSpot = new ChessPosition(newRow, newCol);
                        if (canMove(newSpot, board)) {
                            movePossibilities.add(new ChessMove(myPosition, newSpot, null));
                        }else{
                            break;
                        }
                    }else{
                        break;
                    }
                }
            }
        }else if(pieceType == PieceType.KNIGHT){
            int [][] knightMoves = {
                    {+2, +1}, {+2,-1}, {-2, +1}, {-2, -1}, {+1, +2}, {+1, -2}, {-1, +2}, {-1, -2}
            };
            for (int[] possibility : knightMoves){
                int newRow = row + possibility[0];
                int newCol = col + possibility[1];
                if(inBounds(newRow, newCol)){
                    ChessPosition newSpot = new ChessPosition(newRow, newCol);
                    if (canMove(newSpot, board)){
                        movePossibilities.add(new ChessMove(myPosition, newSpot, null));
                    }
                }
            }
        }else if(pieceType == PieceType.BISHOP){
            int[][] bishopMoves = {
                    {+1,+1}, {+1, -1}, {-1, +1}, {-1,-1}
            };
            for(int[] possibility : bishopMoves){
                int newRow = row;
                int newCol = col;
                while(true){
                    if (!inBounds(newRow, newCol)){
                        break;
                    }
                    newRow += possibility[0];
                    newCol += possibility[1];
                    if (!inBounds(newRow, newCol)){
                        break;
                    }else{
                        ChessPosition newSpot = new ChessPosition(newRow, newCol);
                        if(canMove(newSpot, board)){
                            movePossibilities.add(new ChessMove(myPosition, newSpot, null));
                        }else{
                            break;
                        }
                    }

                }
            }
        }else if(pieceType == PieceType.QUEEN){
            int[][] queenMoves = {
                    {+1, +1}, {+1, 0}, {+1, -1}, {-1, -1}, {-1, 0}, {-1, +1}, {0, +1}, {0, -1}
            };
            for(int[] possibility : queenMoves){
                int newRow = row;
                int newCol = col;
                while(true){
                    if (!inBounds(newRow, newCol)){
                        break;
                    }
                    newRow += possibility[0];
                    newCol += possibility[1];
                    if (!inBounds(newRow, newCol)){
                        break;
                    }else{
                        ChessPosition newSpot = new ChessPosition(newRow, newCol);
                        if(canMove(newSpot, board)){
                            movePossibilities.add(new ChessMove(myPosition, newSpot, null));
                        }else{
                            break;
                        }
                    }

                }
            }
        }else if(pieceType == PieceType.KING){
            int [][] kingMoves = {
                    {+1, +1}, {+1, 0}, {+1, -1}, {-1, -1}, {-1, 0}, {-1, +1}, {0, +1}, {0, -1}
            };
            for (int[] possibility : kingMoves){
                int newRow = row + possibility[0];
                int newCol = col + possibility[1];
                if(inBounds(newRow, newCol)){
                    ChessPosition newSpot = new ChessPosition(newRow, newCol);
                    if (canMove(newSpot, board)){
                        movePossibilities.add(new ChessMove(myPosition, newSpot, null));
                    }
                }
            }
        }
        return movePossibilities;
    }
    public boolean inBounds(int row, int col){
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
    public boolean canMove(ChessPosition newSpot, ChessBoard board){
        if(!inBounds(newSpot.getRow(), newSpot.getColumn())) {
           return false;
        }
        ChessPiece spotCheck = board.getPiece(newSpot);
        return spotCheck == null || board.getPiece(newSpot).teamColor != this.teamColor;
    }
}
