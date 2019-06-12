package botChess;

import java.util.Calendar;

public class GameRunnable implements Runnable{
	
	private static final int redoTurnCooldown = 100; //Time in ms, when an invalid turn is given, the turn will be requested again after this amount of time
	
	private String[] playerNames;
	
	private GameState currentGameState;
	private Player[] currentPlayers;
	private int currentTurn = 0;
	
	private int turnTime;
	
	public GameRunnable(GameState gs, Player[] players, int turnTime, String[] playerNames) {
		currentGameState = gs;
		currentPlayers = players;
		this.turnTime = turnTime;
		this.playerNames = playerNames;
	}
	
	@Override
	public void run() {
		while(true) {//TODO Check for stalemates
			//Get the players move
			Move nextMove = currentPlayers[currentTurn].takeTurn(currentGameState);

			//Check for a given move
			if(nextMove == null) {
				try {
					Thread.sleep(redoTurnCooldown);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				//Validate the move
				Move[] possibleMoves = currentGameState.getPossibleMoves(getCurrentTeamTurn(), nextMove.getSourceX(), nextMove.getSourceY(), false);
				boolean foundMove = false;
				for(int i = 0; i < possibleMoves.length; i++) {
					if(nextMove.isSameMove(possibleMoves[i])) {
						foundMove = true;
					}
				}
				
				if(foundMove){
					long startTime = Calendar.getInstance().getTimeInMillis();
					
					//Perform the move
					currentGameState.makeMove(nextMove);
					if(currentTurn == 0) {
						currentTurn = 1;
					}else {
						currentTurn = 0;
					}
					
					//Check if the game is over
					if(currentGameState.getWinner() != 0) {
						GameHandler.gameFinished(this);
					}
					
					//Wait for the given "turnTime" minus the time taken for the move
					long timeTaken = Calendar.getInstance().getTimeInMillis() - startTime;
					if(timeTaken < turnTime) {
						try {
							Thread.sleep(turnTime - timeTaken);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			nextMove = null;
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
	
	public String[] getPlayerNames() {
		return playerNames;
	}
}
