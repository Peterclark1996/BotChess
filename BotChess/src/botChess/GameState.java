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
	
	private boolean whiteCheck = false;
	private boolean blackCheck = false;
	
	private int winner = 0; //0 = no winner, 1 = white, 2 = black, 3 = draw
	private int turnsTaken = 0;
	
	public GameState() {//Default Constructor
		//Initialise the board
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
	
	public GameState(GameState gs) {//Copy another GameState
		board = new int[8][8];
		
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				board[x][y] = gs.getObjectAtTile(x, y);
			}
		}
		
		whiteCheck = gs.isWhiteInCheck();
		blackCheck = gs.isBlackInCheck();
		
		winner = gs.getWinner();
		
		turnsTaken = gs.getTurnsTaken();
	}
	
	public GameState(GameState gs, Move m) {//Copy another GameState and force a semi unchecked move
		board = new int[8][8];
		
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				board[x][y] = gs.getObjectAtTile(x, y);
			}
		}
		
		whiteCheck = gs.isWhiteInCheck();
		blackCheck = gs.isBlackInCheck();
		
		winner = gs.getWinner();
		
		turnsTaken = gs.getTurnsTaken();
		
		
		//Make the move
		board[m.getDestX()][m.getDestY()] = board[m.getSourceX()][m.getSourceY()];
		board[m.getSourceX()][m.getSourceY()] = 0;
		
		//Check for check
		if(getTeam(m.getDestX(), m.getDestY()) == 1) {
			blackCheck = false;
			for(Move m2 : getPossibleMoves(1, m.getDestX(), m.getDestY(), true)) {
				if(getObjectAtTile(m2.getDestX(), m2.getDestY()) == 12) {
					blackCheck = true;
				}
			}
		}else {
			whiteCheck = false;
			for(Move m2 : getPossibleMoves(2, m.getDestX(), m.getDestY(), true)) {
				if(getObjectAtTile(m2.getDestX(), m2.getDestY()) == 6) {
					whiteCheck = true;
				}
			}
		}
				
		turnsTaken++;
	}
	
	public void makeMove(Move move) {
		makeMove(move, false);
	}
	
	public void makeMove(Move move, boolean skipValidation) {
		//Make sure the game hasnt ended
		if(winner != 0) {
			return;
		}
		
		//Validate the move
		if(!skipValidation) {
			if(!isValidMove(move)) {
				return;
			}
		}
		
		//Make the move
		board[move.getDestX()][move.getDestY()] = board[move.getSourceX()][move.getSourceY()];
		board[move.getSourceX()][move.getSourceY()] = 0;
		
		//Check for check
		if(getTeam(move.getDestX(), move.getDestY()) == 1) {
			blackCheck = false;
			for(Move m : getPossibleMoves(1, move.getDestX(), move.getDestY(), true)) {
				if(getObjectAtTile(m.getDestX(), m.getDestY()) == 12) {
					blackCheck = true;
				}
			}
		}else {
			whiteCheck = false;
			for(Move m : getPossibleMoves(2, move.getDestX(), move.getDestY(), true)) {
				if(getObjectAtTile(m.getDestX(), m.getDestY()) == 6) {
					whiteCheck = true;
				}
			}
		}
		
		
		//Check for check mate
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(getObjectAtTile(x, y) == 6 && whiteCheck) {
					if(getPossibleMoves(1, x, y, false).length == 0) {
						winner = 2;
					}
				}
				if(getObjectAtTile(x, y) == 12 && blackCheck){
					if(getPossibleMoves(2, x, y, false).length == 0) {
						winner = 1;
					}
				}
			}
		}
		
		turnsTaken++;
	}
	
	public boolean isMoveSafe(Move move) {
		//Return false if the move is out of bounds
		if(!move.isInBounds()) {
			return false;
		}
		
		//Get the team id
		int team = getTeam(move.getSourceX(), move.getSourceY());
		if(team != 1 && team != 2) {
			return false;
		}
		
		//Get the enemy team id
		int enemyTeam = 0;
		if(team == 1) {
			enemyTeam = 2;
		}else {
			enemyTeam = 1;
		}
		
		//Create a new GameState where this move would have been done
		GameState newState = new GameState(this, move);
		
		//Return false if any enemy can move to this new position
		for(Move m : newState.getAllPossibleMoves(enemyTeam, true)) {
			if(m.getDestX() == move.getDestX() && m.getDestY() == move.getDestY()) {
				return false;
			}
		}
		
		return true;
	}
	
	public Move[] getAllPossibleMoves(int team, boolean isAttackingMove) {
		ArrayList<Move> moveArrayList = new ArrayList<Move>();
		
		//Get all the moves for each tile
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(!(isAttackingMove && (board[x][y] == 6 || board[x][y] == 12))) {
					Move[] moves = getPossibleMoves(team, x, y, isAttackingMove);
					if(moves != null) {
						for(int i = 0; i < moves.length; i++) {
							moveArrayList.add(moves[i]);
						}
					}
				}
			}
		}
		
		//Convert the ArrayList to an array and return it
		Move[] output = new Move[moveArrayList.size()];
		return moveArrayList.toArray(output);
	}
	
	public Move[] getPossibleMoves(int team, int x, int y, boolean isAttackingMove) {
		//isAttackingMove is for pawn movement, this will only be true when using the isTileSafe function

		//0 = either team
		//1 = white
		//2 = black
		
		//If the team of the piece on the tile isnt equal to the team given
		//If the team isnt 1 or 2 return
		//If there is no piece on the tile return
		if((getTeam(x, y) != team && team != 0) || board[x][y] == 0) {
			return new Move[0];
		}

		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		switch(board[x][y]) {
		case 1:
			//White Pawn
			if(y != 7) {
				if(isAttackingMove) {
					possibleMoves.add(new Move(x, y, x - 1, y + 1));//Add north-west
					possibleMoves.add(new Move(x, y, x + 1, y + 1));//Add north-west
				}else {
					if(getTeam(x - 1, y + 1) == 2) {//Check for a move north-west if there is an enemy
						possibleMoves.add(new Move(x, y, x - 1, y + 1));
					}
					if(getTeam(x + 1, y + 1) == 2) {//Check for a move north-east if there is an enemy
						possibleMoves.add(new Move(x, y, x + 1, y + 1));
					}
					if(getTeam(x, y + 1) == 0) {//Check for a move north
						possibleMoves.add(new Move(x, y, x, y + 1));
					}
					if(getTeam(x, y + 1) == 0 && getTeam(x, y + 2) == 0 && y == 1) {//Check for a double move north if in starting position
						possibleMoves.add(new Move(x, y, x, y + 2));
					}
				}
			}
			break;
		case 7:
			//Black Pawn
			if(y != 0) {
				if(isAttackingMove) {
					possibleMoves.add(new Move(x, y, x - 1, y - 1));//Add south-west
					possibleMoves.add(new Move(x, y, x + 1, y - 1));//Add south-west
				}else {
					if(getTeam(x - 1, y - 1) == 1) {//Check for a move south-west if there is an enemy
						possibleMoves.add(new Move(x, y, x - 1, y - 1));
					}
					if(getTeam(x + 1, y - 1) == 1) {//Check for a move south-east if there is an enemy
						possibleMoves.add(new Move(x, y, x + 1, y - 1));
					}
					if(getTeam(x, y - 1) == 0) {//Check for a move south
						possibleMoves.add(new Move(x, y, x, y - 1));
					}
					if(getTeam(x, y - 1) == 0 && getTeam(x, y - 2) == 0 && y == 6) {//Check for a double move south if in starting position
						possibleMoves.add(new Move(x, y, x, y - 2));
					}
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
					if(getTeam(x, y + i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y + i));
						if(getTeam(x, y + i) != 0) {
							north = false;
						}
					}else {
						north = false;
					}
				}else {
					north = false;
				}
				if(y - i >= 0 && south) {//Check for a move south
					if(getTeam(x, y - i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y - i));
						if(getTeam(x, y - i) != 0) {
							south = false;
						}
					}else {
						south = false;
					}
				}else {
					south = false;
				}
				if(x + i < 8 && east) {//Check for a move east
					if(getTeam(x + i, y) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y));
						if(getTeam(x + i, y) != 0) {
							east = false;
						}
					}else {
						east = false;
					}
				}else {
					east = false;
				}
				if(x - i >= 0 && west) {//Check for a move west
					if(getTeam(x - i, y) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y));
						if(getTeam(x - i, y) != 0) {
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
			if(getTeam(x + 1, y + 2) != getTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 1, y + 2));
			}
			if(getTeam(x - 1, y + 2) != getTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 1, y + 2));
			}
			if(getTeam(x - 2, y + 1) != getTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 2, y + 1));
			}
			if(getTeam(x + 2, y + 1) != getTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 2, y + 1));
			}
			if(getTeam(x - 2, y - 1) != getTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 2, y - 1));
			}
			if(getTeam(x - 1, y - 2) != getTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x - 1, y - 2));
			}
			if(getTeam(x + 1, y - 2) != getTeam(x, y)) {
				possibleMoves.add(new Move(x, y, x + 1, y - 2));
			}
			if(getTeam(x + 2, y - 1) != getTeam(x, y)) {
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
					if(getTeam(x + i, y + i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y + i));
						if(getTeam(x + i, y + i) != 0) {
							northeast = false;
						}
					}else {
						northeast = false;
					}
				}else {
					northeast = false;
				}
				if((x + i < 8 && y - i >= 0) && southeast) {//Check for a move southeast
					if(getTeam(x + i, y - i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y - i));
						if(getTeam(x + i, y - i) != 0) {
							southeast = false;
						}
					}else {
						southeast = false;
					}
				}else {
					southeast = false;
				}
				if((x - i >= 0 && y + i < 8) && northwest) {//Check for a move east
					if(getTeam(x - i, y + i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y + i));
						if(getTeam(x - i, y + i) != 0) {
							northwest = false;
						}
					}else {
						northwest = false;
					}
				}else {
					northwest = false;
				}
				if((x - i >= 0 && y - i >= 0) && southwest) {//Check for a move west
					if(getTeam(x - i, y - i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y - i));
						if(getTeam(x - i, y - i) != 0) {
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
					if(getTeam(x + i, y + i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y + i));
						if(getTeam(x + i, y + i) != 0) {
							northeastQueen = false;
						}
					}else {
						northeastQueen = false;
					}
				}else {
					northeastQueen = false;
				}
				if((x + i < 8 && y - i >= 0) && southeastQueen) {//Check for a move southeast
					if(getTeam(x + i, y - i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y - i));
						if(getTeam(x + i, y - i) != 0) {
							southeastQueen = false;
						}
					}else {
						southeastQueen = false;
					}
				}else {
					southeastQueen = false;
				}
				if((x - i >= 0 && y + i < 8) && northwestQueen) {//Check for a move east
					if(getTeam(x - i, y + i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y + i));
						if(getTeam(x - i, y + i) != 0) {
							northwestQueen = false;
						}
					}else {
						northwestQueen = false;
					}
				}else {
					northwestQueen = false;
				}
				if((x - i >= 0 && y - i >= 0) && southwestQueen) {//Check for a move west
					if(getTeam(x - i, y - i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y - i));
						if(getTeam(x - i, y - i) != 0) {
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
					if(getTeam(x, y + i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y + i));
						if(getTeam(x, y + i) != 0) {
							northQueen = false;
						}
					}else {
						northQueen = false;
					}
				}else {
					northQueen = false;
				}
				if(y - i >= 0 && southQueen) {//Check for a move south
					if(getTeam(x, y - i) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x, y - i));
						if(getTeam(x, y - i) != 0) {
							southQueen = false;
						}
					}else {
						southQueen = false;
					}
				}else {
					southQueen = false;
				}
				if(x + i < 8 && eastQueen) {//Check for a move east
					if(getTeam(x + i, y) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x + i, y));
						if(getTeam(x + i, y) != 0) {
							eastQueen = false;
						}
					}else {
						eastQueen = false;
					}
				}else {
					eastQueen = false;
				}
				if(x - i >= 0 && westQueen) {//Check for a move west
					if(getTeam(x - i, y) != getTeam(x, y)) {
						possibleMoves.add(new Move(x, y, x - i, y));
						if(getTeam(x - i, y) != 0) {
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
			if(!isAttackingMove) {
				if(getTeam(x, y + 1) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x, y + 1))) {
						possibleMoves.add(new Move(x, y, x, y + 1));
					}
				}
				if(getTeam(x + 1, y + 1) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x + 1, y + 1))) {
						possibleMoves.add(new Move(x, y, x + 1, y + 1));
					}
				}
				if(getTeam(x - 1, y + 1) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x - 1, y + 1))) {
						possibleMoves.add(new Move(x, y, x - 1, y + 1));
					}
				}
				if(getTeam(x + 1, y) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x + 1, y))) {
						possibleMoves.add(new Move(x, y, x + 1, y));
					}
				}
				if(getTeam(x - 1, y) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x - 1, y))) {
						possibleMoves.add(new Move(x, y, x - 1, y));
					}
				}
				if(getTeam(x, y - 1) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x, y - 1))) {
						possibleMoves.add(new Move(x, y, x, y - 1));
					}
				}
				if(getTeam(x + 1, y - 1) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x + 1, y - 1))) {
						possibleMoves.add(new Move(x, y, x + 1, y - 1));
					}
				}
				if(getTeam(x - 1, y - 1) != getTeam(x, y)) {
					if(isMoveSafe(new Move(x, y, x - 1, y - 1))) {
						possibleMoves.add(new Move(x, y, x - 1, y - 1));
					}
				}
			}
			
			break;
		}

		//If in check and the tile dosent contain the king, return
		if((whiteCheck && board[x][y] != 6 && team == 1) ||(blackCheck && board[x][y] != 12 && team == 2)) {
			return new Move[0];
		}
		
		//Remove all "Out of bounds" moves
		for(int i = possibleMoves.size() - 1; i >= 0; i--) {
			if(!possibleMoves.get(i).isInBounds()) {
				possibleMoves.remove(i);
			}
			if(team == 1 && whiteCheck) {
				//If whites in check, does this get them out of check?
				GameState newState = new GameState(this, possibleMoves.get(i));
				if(newState.isWhiteInCheck()) {
					possibleMoves.remove(i);
				}
			}else if(team == 2 && blackCheck){
				//If blacks in check, does this get them out of check?
				GameState newState = new GameState(this, possibleMoves.get(i));
				if(newState.isBlackInCheck()) {
					possibleMoves.remove(i);
				}
			}
		}

		//Convert the ArrayList to an array and return it
		Move[] output = new Move[possibleMoves.size()];
		return possibleMoves.toArray(output);
	}
	
	public boolean isValidMove(Move move) {
		if(getTeam(move.getSourceX(), move.getSourceY()) != 0) {
			for(Move m : getPossibleMoves(getTeam(move.getSourceX(), move.getSourceY()), move.getSourceX(), move.getSourceY(), false)) {
				if(move.isSameMove(m)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getObjectAtTile(int x, int y) {
		if(x < 0 || x > 7 || y < 0 || y > 7) {
			return 0;
		}
		return board[x][y];
	}
	
	public int getTeam(int x, int y) {
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
	
	public boolean isWhiteInCheck() {
		return whiteCheck;
	}
	
	public boolean isBlackInCheck() {
		return blackCheck;
	}
	
	public int getWinner() {
		return winner;
	}
	
	public int getTurnsTaken() {
		return turnsTaken;
	}
}
