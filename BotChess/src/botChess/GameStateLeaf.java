package botChess;

public class GameStateLeaf {
	
	private GameStateLeaf upperBranch;
	private Move move;
	private GameState gameState;
	
	public GameStateLeaf(GameStateLeaf gsl, Move m, GameState gs) {
		this.upperBranch = gsl;
		this.move = m;
		this.gameState = gs;
	}
	
}
