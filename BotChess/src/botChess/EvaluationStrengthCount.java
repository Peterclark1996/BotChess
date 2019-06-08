package botChess;

public class EvaluationStrengthCount implements IGameStateEvaluation{
	
	//Kings are rated here as "10" allthough their score shouldent affect anything due to it being impossible to take them
	private static final int[] whiteStrength = new int[]{0, 1, 3, 3, 3, 6, 10, -1, -3, -3, -3, -6, -10};
	private static final int[] blackStrength = new int[]{0, -1, -3, -3, -3, -6, -10, 1, 3, 3, 3, 6, 10};
	
	public int evaluateState(int team, GameState gs) {
		int result = 0;

		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				if(team == 1) {
					result += whiteStrength[gs.getObjectAtTile(x, y)];
				}else {
					result += blackStrength[gs.getObjectAtTile(x, y)];
				}
			}
		}
		
		return result;
	}
	
}
