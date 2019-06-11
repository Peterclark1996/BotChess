package botChess;

import java.util.ArrayList;

public class PlayerMiniMaxRecursive extends Player{
	
	private IGameStateEvaluation evaluator;
	private int maxDepth = 5;
	
	public PlayerMiniMaxRecursive(int team, IGameStateEvaluation gse, int depth) {
		super(team);
		evaluator = gse;
		this.maxDepth = depth;
	}
	
	public Move takeTurn(GameState gs) {
		ArrayList<Move> bestMoves = new ArrayList<Move>();
		int bestValue = Integer.MIN_VALUE;
		for(Move m : gs.getAllPossibleMoves(team, false)) {
			GameState nextState = new GameState(gs);
			nextState.makeMove(m);
			int nextValue = minimax(nextState, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
			if(nextValue > bestValue) {
				bestValue = nextValue;
				bestMoves = new ArrayList<Move>();
				bestMoves.add(m);
			}else if(nextValue == bestValue) {
				bestMoves.add(m);
			}
		}
		
		if(bestMoves.size() == 0) {
			return null;
		}else {
			return bestMoves.get((int) (Math.random() * (bestMoves.size() - 1)));
		}
	}
	
	private int minimax(GameState currentState, int depth, int alpha, int beta, boolean maximisingPlayer) {
		if(depth == 0 || currentState.getWinner() != 0) {
			return evaluator.evaluateState(team, currentState);
		}
		
		if(maximisingPlayer) {
			int maxEval = Integer.MIN_VALUE;
			for(Move m : currentState.getAllPossibleMoves(team, false)) {
				GameState nextState = new GameState(currentState);
				nextState.makeMove(m);
				int eval = minimax(nextState, depth - 1, alpha, beta, false);
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if(beta <= alpha) {
					break;
				}
			}
			return maxEval;
		}else {
			int minEval = Integer.MAX_VALUE;
			for(Move m : currentState.getAllPossibleMoves(team, false)) {
				GameState nextState = new GameState(currentState);
				nextState.makeMove(m);
				int eval = minimax(nextState, depth - 1, alpha, beta, true);
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if(beta <= alpha) {
					break;
				}
			}
			return minEval;
		}
	}

}
