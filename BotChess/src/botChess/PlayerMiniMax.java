package botChess;

public class PlayerMiniMax extends Player{
	//TODO Iterative deepening
	//TODO Alpha beta pruning
	
	private GameStateLeaf head;
	private Move currentBestMove;
	
	public PlayerMiniMax(int team) {
		super(team);
	}
	
	public Move takeTurn(GameState gs) {//TODO
		return null;
	}
}
