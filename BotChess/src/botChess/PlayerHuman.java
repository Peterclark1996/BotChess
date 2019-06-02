package botChess;

public class PlayerHuman extends Player{
	
	public PlayerHuman(int team) {
		super(team);
	}

	private Move queuedMove = null;
	private boolean queuedMoveSent = false;
	
	public Move takeTurn(GameState gs) {
		if(queuedMoveSent) {
			queuedMove = null;
		}
		if(queuedMove != null) {
			queuedMoveSent = true;
		}
		return queuedMove;
	}
	
	public void tileClicked(int x, int y) {
		queuedMove = new Move(GameHandler.getSelectedX(), GameHandler.getSelectedY(), x, y);
		queuedMoveSent = false;
	}
}
