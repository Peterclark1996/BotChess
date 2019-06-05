package botChess;

public abstract class Player {
	
	protected Move lastMove = null;
	protected int team = 0;
	
	public Player(int team) {
		if(team != 1 && team != 2) {
			throw new IllegalArgumentException();
		}
		this.team = team;
	}
	
	public Move takeTurn(GameState gs) {
		return null;
	}
	
	public void tileClicked(int x, int y) {
		//For player types that need a user input
	}
}
