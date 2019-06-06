package botChess;

public class GameRunnable implements Runnable{
	
	private static final int redoTurnCooldown = 100; //Time in ms, when an invalid turn is given, the turn will be requested again after this amount of time
	
	private GameState currentGameState;
	private Player[] currentPlayers;
	private int currentTurn = 0;
	
	private int turnTime;
	
	public GameRunnable(GameState gs, Player[] players, int turnTime) {
		currentGameState = gs;
		currentPlayers = players;
		this.turnTime = turnTime;
	}
	
	@Override
	public void run() {
		while(true) {//TODO change "true" to a actual win/draw condition
			//Check if the current player is in check
			//TODO Check if the current play is actually in check mate
			//TODO Force the player to move then king when in check
			boolean inCheck = false;
			for(int y = 0; y < 8; y++) {
				for(int x = 0; x < 8; x++) {
					if((currentGameState.getObjectAtTile(x, y) == 6 || currentGameState.getObjectAtTile(x, y) == 12) && currentGameState.getTeam(x, y) == getCurrentTeamTurn()) {
						if(!currentGameState.isTileSafe(getCurrentTeamTurn(), x, y)) {
							inCheck = true;
						}
					}
				}
			}
			
			//Get the players move
			Move nextMove = currentPlayers[currentTurn].takeTurn(currentGameState, inCheck);

			//Check for a given move
			if(nextMove == null) {
				try {
					Thread.sleep(redoTurnCooldown);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				//Validate the move
				Move[] possibleMoves = currentGameState.getPossibleMoves(getCurrentTeamTurn(), nextMove.getSourceX(), nextMove.getSourceY());
				boolean foundMove = false;
				for(int i = 0; i < possibleMoves.length; i++) {
					if(nextMove.isSameMove(possibleMoves[i])) {
						foundMove = true;
					}
				}
				
				if(!foundMove) {
					nextMove = null;
				}else {
					//Perform the move
					currentGameState.makeMove(nextMove);
					nextMove = null;
					if(currentTurn == 0) {
						currentTurn = 1;
					}else {
						currentTurn = 0;
					}
					
					//Wait for the given "turnTime"
					try {
						Thread.sleep(turnTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			GameHandler.paintBoard();
		}
	}
	
	public int getCurrentTeamTurn() {
		//Return the team id of the current player (Current turn = 0 || 1, while current team = 1 || 2)
		return currentTurn + 1;
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
