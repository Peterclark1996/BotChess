package botChess;

import java.util.ArrayList;

public class GameState {

	private int[][] board;
	//0 = nothing
	//1 = white pawn
	//2 = white rook
	//3 = white knight
	//4 = white bishop
	//5 = white queen
	//6 = white king
	//7 = black pawn
	//8 = black rook
	//9 = black knight
	//10 = black bishop
	//11 = black queen
	//12 = black king
	
	public GameState() {
		//Initialize the board
		board = new int[8][8];
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				board[x][y] = 0;
			}
		}
		
		//Add the pawns
		for(int i = 0; i < 8; i++) {
			board[i][1] = 1;
			board[i][6] = 7;
		}
		
		//Add the other pieces
		board[0][0] = 2;
		board[1][0] = 3;
		board[2][0] = 4;
		board[3][0] = 5;
		board[4][0] = 6;
		board[5][0] = 4;
		board[6][0] = 3;
		board[7][0] = 2;
		board[0][7] = 8;
		board[1][7] = 9;
		board[2][7] = 10;
		board[3][7] = 11;
		board[4][7] = 12;
		board[5][7] = 10;
		board[6][7] = 9;
		board[7][7] = 8;
	}
	
	public int getObjectAtTile(int x, int y) {
		return board[x][y];
	}
	
	public void makeMove(Move move) {
		if(isPossibleMove(move)) {
			board[move.getDestX()][move.getDestY()] = board[move.getSourceX()][move.getSourceY()];
			board[move.getSourceX()][move.getSourceY()] = 0;
		}
	}
	
	public Move[] getPossibleMoves(int team, int x, int y) {//TODO test this (Also disallow king moving to check)
		//0 = either team
		//1 = white
		//2 = black
		if((checkTeam(x, y) != team && team != 0) || board[x][y] == 0) {
			return new Move[0];
		}

		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		switch(board[x][y]) {
		case 1:
			//White Pawn
			if(y != 7) {
				if(checkTeam(x - 1, y + 1) == 2) {//Check for a move north-west if there is an enemy
					possibleMoves.add(new Move(x, y, x - 1, y + 1));
				}
				if(checkTeam(x + 1, y + 1) == 2) {//Check for a move north-east if there is an enemy
					possibleMoves.add(new Move(x, y, x + 1, y + 1));
				}
				if(checkTeam(x, y + 1) == 0) {//Check for a move north
					possibleMoves.add(new Move(x, y, x, y + 1));
				}
				if(checkTeam(x, y + 2) == 0 && y == 1) {//Check for a double move north if in starting position
					possibleMoves.add(new Move(x, y, x, y + 2));
				}
			}
			break;
		case 7:
			//Black Pawn
			if(y != 0) {
				if(checkTeam(x - 1, y - 1) == 1) {//Check for a move south-west if there is an enemy
					possibleMoves.add(new Move(x, y, x - 1, y - 1));
				}
				if(checkTeam(x + 1, y - 1) == 1) {//Check for a move south-east if there is an enemy
					possibleMoves.add(new Move(x, y, x + 1, y - 1));
				}
				if(checkTeam(x, y - 1) == 0) {//Check for a move south
					possibleMoves.add(new Move(x, y, x, y - 1));
				}
				if(checkTeam(x, y - 2) == 0 && y == 6) {//Check for a double move south if in starting position
					possibleMoves.add(new Move(x, y, x, y - 2));
				}
			}
			break;
		case 2:
		case 8:
			//Rook
			boolean north = true;
			boolean south = true;
			boolean east = true;
			boolean west = true;
			for(int i = 1; i < 8; i++) {
				if(y + i < 8 && north) {//Check for a move north
					if(checkTeam(x, y + i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y + i));
						if(checkTeam(x, y + i) != 0) {
							north = false;
						}
					}else {
						north = false;
					}
				}else {
					north = false;
				}
				if(y - i >= 0 && south) {//Check for a move south
					if(checkTeam(x, y - i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y - i));
						if(checkTeam(x, y - i) != 0) {
							south = false;
						}
					}else {
						south = false;
					}
				}else {
					south = false;
				}
				if(x + i < 8 && east) {//Check for a move east
					if(checkTeam(x + i, y) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y));
						if(checkTeam(x + i, y) != 0) {
							east = false;
						}
					}else {
						east = false;
					}
				}else {
					east = false;
				}
				if(x - i >= 0 && west) {//Check for a move west
					if(checkTeam(x - i, y) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y));
						if(checkTeam(x - i, y) != 0) {
							west = false;
						}
					}else {
						west = false;
					}
				}else {
					west = false;
				}
			}
			break;
		case 3:
		case 9:
			//Knight
			if(checkTeam(x + 1, y + 2) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 1, y + 2));
			}
			if(checkTeam(x - 1, y + 2) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 1, y + 2));
			}
			if(checkTeam(x - 2, y + 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 2, y + 1));
			}
			if(checkTeam(x + 2, y + 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 2, y + 1));
			}
			if(checkTeam(x - 2, y - 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 2, y - 1));
			}
			if(checkTeam(x - 1, y - 2) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 1, y - 2));
			}
			if(checkTeam(x + 1, y - 2) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 1, y - 2));
			}
			if(checkTeam(x + 2, y - 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 2, y - 1));
			}
			break;
		case 4:
		case 10:
			//Bishop
			boolean northeast = true;
			boolean southeast = true;
			boolean northwest = true;
			boolean southwest = true;
			for(int i = 1; i < 8; i++) {
				if((y + i < 8 && x + i < 8) && northeast) {//Check for a move northeast
					if(checkTeam(x + i, y + i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y + i));
						if(checkTeam(x + i, y + i) != 0) {
							northeast = false;
						}
					}else {
						northeast = false;
					}
				}else {
					northeast = false;
				}
				if((x + i < 8 && y - i >= 0) && southeast) {//Check for a move southeast
					if(checkTeam(x + i, y - i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y - i));
						if(checkTeam(x + i, y - i) != 0) {
							southeast = false;
						}
					}else {
						southeast = false;
					}
				}else {
					southeast = false;
				}
				if((x - i >= 0 && y + i < 8) && northwest) {//Check for a move east
					if(checkTeam(x - i, y + i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y + i));
						if(checkTeam(x - i, y + i) != 0) {
							northwest = false;
						}
					}else {
						northwest = false;
					}
				}else {
					northwest = false;
				}
				if((x - i >= 0 && y - i >= 0) && southwest) {//Check for a move west
					if(checkTeam(x - i, y - i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y - i));
						if(checkTeam(x - i, y - i) != 0) {
							southwest = false;
						}
					}else {
						southwest = false;
					}
				}else {
					southwest = false;
				}
			}
			break;
		case 5:
		case 11:
			//Queen
			boolean northeastQueen = true;
			boolean southeastQueen = true;
			boolean northwestQueen = true;
			boolean southwestQueen = true;
			for(int i = 1; i < 8; i++) {
				if((y + i < 8 && x + i < 8) && northeastQueen) {//Check for a move northeast
					if(checkTeam(x + i, y + i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y + i));
						if(checkTeam(x + i, y + i) != 0) {
							northeastQueen = false;
						}
					}else {
						northeastQueen = false;
					}
				}else {
					northeastQueen = false;
				}
				if((x + i < 8 && y - i >= 0) && southeastQueen) {//Check for a move southeast
					if(checkTeam(x + i, y - i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y - i));
						if(checkTeam(x + i, y - i) != 0) {
							southeastQueen = false;
						}
					}else {
						southeastQueen = false;
					}
				}else {
					southeastQueen = false;
				}
				if((x - i >= 0 && y + i < 8) && northwestQueen) {//Check for a move east
					if(checkTeam(x - i, y + i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y + i));
						if(checkTeam(x - i, y + i) != 0) {
							northwestQueen = false;
						}
					}else {
						northwestQueen = false;
					}
				}else {
					northwestQueen = false;
				}
				if((x - i >= 0 && y - i >= 0) && southwestQueen) {//Check for a move west
					if(checkTeam(x - i, y - i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y - i));
						if(checkTeam(x - i, y - i) != 0) {
							southwestQueen = false;
						}
					}else {
						southwestQueen = false;
					}
				}else {
					southwestQueen = false;
				}
			}
			boolean northQueen = true;
			boolean southQueen = true;
			boolean eastQueen = true;
			boolean westQueen = true;
			for(int i = 1; i < 8; i++) {
				if(y + i < 8 && northQueen) {//Check for a move north
					if(checkTeam(x, y + i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y + i));
						if(checkTeam(x, y + i) != 0) {
							northQueen = false;
						}
					}else {
						northQueen = false;
					}
				}else {
					northQueen = false;
				}
				if(y - i >= 0 && southQueen) {//Check for a move south
					if(checkTeam(x, y - i) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y - i));
						if(checkTeam(x, y - i) != 0) {
							southQueen = false;
						}
					}else {
						southQueen = false;
					}
				}else {
					southQueen = false;
				}
				if(x + i < 8 && eastQueen) {//Check for a move east
					if(checkTeam(x + i, y) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y));
						if(checkTeam(x + i, y) != 0) {
							eastQueen = false;
						}
					}else {
						eastQueen = false;
					}
				}else {
					eastQueen = false;
				}
				if(x - i >= 0 && westQueen) {//Check for a move west
					if(checkTeam(x - i, y) != checkTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y));
						if(checkTeam(x - i, y) != 0) {
							westQueen = false;
						}
					}else {
						westQueen = false;
					}
				}else {
					westQueen = false;
				}
			}
			break;
		case 6:
		case 12:
			//King
			if(checkTeam(x, y + 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x, y + 1));
			}
			if(checkTeam(x + 1, y + 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 1, y + 1));
			}
			if(checkTeam(x - 1, y + 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 1, y + 1));
			}
			if(checkTeam(x + 1, y) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 1, y));
			}
			if(checkTeam(x - 1, y) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 1, y));
			}
			if(checkTeam(x, y - 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x, y - 1));
			}
			if(checkTeam(x + 1, y - 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 1, y - 1));
			}
			if(checkTeam(x - 1, y - 1) != checkTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 1, y - 1));
			}
			break;
		}

		//Remove all "Out of bounds" moves
		for(int i = possibleMoves.size() - 1; i >= 0; i--) {
			if(!possibleMoves.get(i).isPossible()) {
				possibleMoves.remove(i);
			}
		}

		//Convert the ArrayList to an array and return it
		Move[] output = new Move[possibleMoves.size()];
		return possibleMoves.toArray(output);
	}
	
	public boolean isPossibleMove(Move move) {
		if(checkTeam(move.getSourceX(), move.getSourceY()) != 0) {
			for(Move m : getPossibleMoves(checkTeam(move.getSourceX(), move.getSourceY()), move.getSourceX(), move.getSourceY())) {
				if(move.isSameMove(m)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int checkTeam(int x, int y) {
		//0 = nothing
		//1 = white
		//2 = black
		
		if(x < 0 || x > 7 || y < 0 || y > 7) {
			return 0;
		}
		
		switch(board[x][y]) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			return 1;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
			return 2;
		}
		return 0;
	}
	
}
