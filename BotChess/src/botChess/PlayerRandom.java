package botChess;

public class PlayerRandom extends Player{

	public PlayerRandom(int team) {
		super(team);
	}

	@Override
	public Move takeTurn(GameState gs) {
		//Get all possible moves
		Move[] moves = gs.getAllPossibleMoves(team, false);
		
		if(moves.length > 0) {
			
			//Pick a move at random
			return moves[(int) (Math.random() * (moves.length - 1))];
			
		}
		return null;
	}
	
}
