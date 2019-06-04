package botChess;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class GameBoard extends JPanel{
	
	private static final String[] spriteList = new String[] {
			"gfx/board.png",
			"gfx/white_pawn.png",
			"gfx/white_rook.png",
			"gfx/white_knight.png",
			"gfx/white_bishop.png",
			"gfx/white_queen.png",
			"gfx/white_king.png",
			"gfx/black_pawn.png",
			"gfx/black_rook.png",
			"gfx/black_knight.png",
			"gfx/black_bishop.png",
			"gfx/black_queen.png",
			"gfx/black_king.png",
			"gfx/selector_blue.png",
			"gfx/selector_cyan.png"
	};
	
	public void paintComponent(Graphics g){
		//Draw the board
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1024, 1024);
		g.drawImage(SpriteLoader.getImage("gfx/board.png"), 0, 0, this);
		
		//Draw the pieces
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				if(GameHandler.getCurrentGameThread().getCurrentGameState().getObjectAtTile(x, y) != 0) {
					g.drawImage(SpriteLoader.getImage(spriteList[GameHandler.getCurrentGameThread().getCurrentGameState().getObjectAtTile(x, y)]), x * 64, (-y * 64) + 448, this);
				}
			}
		}
		
		if(GameHandler.getSelectedX() >= 0 && GameHandler.getSelectedX() <= 7 && GameHandler.getSelectedY() >= 0 && GameHandler.getSelectedY() <= 7) {
			//Draw the selected tile
			g.drawImage(SpriteLoader.getImage(spriteList[13]), GameHandler.getSelectedX() * 64, (-GameHandler.getSelectedY() * 64) + 448, this);
			
			//Draw the possible moves
			Move[] possibleMoves = GameHandler.getCurrentGameThread().getCurrentGameState().getPossibleMoves(GameHandler.getCurrentGameThread().getCurrentTeamTurn(), GameHandler.getSelectedX(), GameHandler.getSelectedY());
			for(int i = 0; i < possibleMoves.length; i++) {
				g.drawImage(SpriteLoader.getImage(spriteList[14]), possibleMoves[i].getDestX() * 64, (-possibleMoves[i].getDestY() * 64) + 448, this);
			}
		}

		repaint();
	}
}
