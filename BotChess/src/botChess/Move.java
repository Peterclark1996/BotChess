package botChess;

public class Move {
	
	private int sx;
	private int sy;
	private int dx;
	private int dy;
	
	public Move(int sx, int sy, int dx, int dy) throws IllegalArgumentException {
		/*
		if(sx < 0 || sx > 7 || sy < 0 || sy > 7 || dx < 0 || dx > 7 || dy < 0 || dy > 7) {
			throw new IllegalArgumentException();
		}
		*/
		this.sx = sx;
		this.sy = sy;
		this.dx = dx;
		this.dy = dy;
	}
	
	public boolean isPossible() {
		if(sx < 0 || sx > 7 || sy < 0 || sy > 7 || dx < 0 || dx > 7 || dy < 0 || dy > 7) {
			return false;
		}
		return true;
	}
	
	public boolean isSameMove(Move m) {
		if(sx == m.getSourceX() && sy == m.getSourceY() && dx == m.getDestX() && dy == m.getDestY()) {
			return true;
		}
		return false;
	}
	
	public String getMoveString() {
		return "(" + sx + "," + sy + ") to (" + dx + "," + dy + ")";
	}
	
	public int getSourceX() {
		return sx;
	}
	
	public int getSourceY() {
		return sy;
	}
	
	public int getDestX() {
		return dx;
	}
	
	public int getDestY() {
		return dy;
	}
	
}
