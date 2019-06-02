package botChess;

public class GameRunnable implements Runnable{
	
	private static final int redoTurnCooldown = 100; //Time in ms, when an invalid turn is given, the turn will be requested again after this amount of time
	
	private GameState currentGameState;
	private Player[] currentPlayers;
	private int currentTurn = 0;
	
	public GameRunnable(GameState gs, Player[] players) {
		currentGameState = gs;
		currentPlayers = players;
	}
	
	@Override
	public void run() {
		while(true) {//TODO change "true" to a actual win/draw condition
			Move nextMove = currentPlayers[currentTurn].takeTurn(currentGameState);
			if(nextMove == null) {
				//System.out.println("Given invalid move");
				try {
					Thread.sleep(redoTurnCooldown);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				currentGameState.makeMove(nextMove);
				if(currentTurn == 0) {
					currentTurn = 1;
				}else {
					currentTurn = 0;
				}
			}
			GameHandler.paintBoard();
		}
	}
	
	public int getCurrentTurn() {
		return currentTurn;
	}
	
	public Player[] getCurrentPlayers() {
		return currentPlayers;
	}
	
	public GameState getCurrentGameState() {
		return currentGameState;
	}
}
