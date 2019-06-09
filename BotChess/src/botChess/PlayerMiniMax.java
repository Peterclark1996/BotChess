package botChess;

import java.util.ArrayList;

public class PlayerMiniMax extends Player{
	//TODO Iterative deepening
	//TODO Alpha beta pruning
	
	private int searchDepth = 1;
	
	private GameStateLeaf head;
	private ArrayList<GameStateLeaf> bestScoreLeaves;
	
	private ArrayList<GameStateLeaf> frontier;
	
	public PlayerMiniMax(int team, int searchDepth) {
		super(team);
		this.searchDepth = searchDepth;
	}
	
	public Move takeTurn(GameState gs) {//TODO
		bestScoreLeaves = new ArrayList<GameStateLeaf>();
		frontier = new ArrayList<GameStateLeaf>();
		
		boolean searching = true;
		int currentDepth = 1;
		while(searching) {
			ArrayList<GameStateLeaf> nextFrontier = new ArrayList<GameStateLeaf>();
			
			//Explore the frontier
			for(GameStateLeaf l : frontier) {
				
			}
			
			//Copy across the new frontier
			frontier = nextFrontier;
			
			//Stop searching once an exit condition is met
			if(currentDepth == searchDepth) {//TODO Stop searching after a given time as well as max depth to limit think times
				searching = false;
			}else {
				currentDepth++;
			}
		}
		
		//Return a random move from the best options found
		if(bestScoreLeaves.size() == 0) {
			return null;
		}else {
			return bestScoreLeaves.get((int) (Math.random() * (bestScoreLeaves.size() - 1))).getMove();
		}
	}
}
