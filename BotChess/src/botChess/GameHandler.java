package botChess;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameHandler {

	private static JFrame frame;
	private static JFrame frameGame;
	
	private static JPanel panelSetupTop;
	private static JPanel panelSetupMid;
	private static JPanel panelSetupBot;
	
	private static JPanel panelLeftPlayer;
	private static JPanel panelRightPlayer;
	private static JPanel panelBoard;
	
	private static JCheckBox inputShowGames;
	private static JTextField inputTurnTime;
	private static JTextField inputGamesPerMatchup;
	
	private static String[] playerNames;
	private static JButton startButton;
	
	private static GameRunnable currentGameRunnable = null;
	private static Thread currentGameThread = null;
	
	private static int selectedX = -10;
	private static int selectedY = -10;
	
	static ActionListener playerChange = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//Add black player at the end if the play player isnt "No player"
			if(((JComboBox<String>) panelSetupMid.getComponent(panelSetupMid.getComponentCount() - 1)).getSelectedIndex() != 0) {
				JComboBox<String> temp = new JComboBox<String>(playerNames);
				temp.addActionListener(playerChange);
				panelSetupMid.add(temp);
				frame.revalidate();
				frame.repaint();
				frame.pack();
			}
			
			//Remove all "No player" except the one at the end
			if(((JComboBox<String>) panelSetupMid.getComponent(panelSetupMid.getComponentCount() - 1)).getSelectedIndex() == 0) {
				for(int i = panelSetupMid.getComponentCount() - 2; i >= 0; i--) {
					if(((JComboBox<String>) panelSetupMid.getComponent(i)).getSelectedIndex() == 0) {
						panelSetupMid.remove(panelSetupMid.getComponent(i));
					}
				}
				frame.revalidate();
				frame.repaint();
				frame.pack();
			}
		}
	};
	
	static ActionListener startSimulation = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			loadInstance();
		}
	};
	
	static ActionListener boardClicked = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Board clicked");
		}
	};
	
	public static void main(String[] args) {
		//Build the options
		panelSetupTop = new JPanel();
		panelSetupTop.setLayout(new GridLayout(4, 2));
		panelSetupTop.add(new JLabel("Show Games:"));
		inputShowGames = new JCheckBox();
		panelSetupTop.add(inputShowGames);
		panelSetupTop.add(new JLabel("Turn Time (ms):"));
		inputTurnTime = new JTextField();
		panelSetupTop.add(inputTurnTime);
		panelSetupTop.add(new JLabel("Games Per Matchup:"));
		inputGamesPerMatchup = new JTextField();
		panelSetupTop.add(inputGamesPerMatchup);
		
		//Build the player list
		panelSetupMid = new JPanel();
		panelSetupMid.setLayout(new BoxLayout(panelSetupMid, BoxLayout.PAGE_AXIS));
		panelSetupMid.setLayout(new GridLayout(2, 1));
		playerNames = new String[] {"No Player", "Human", "Random"};
		JComboBox<String> temp = new JComboBox<String>(playerNames);
		temp.addActionListener(playerChange);
		panelSetupMid.add(temp);
		
		//Build the start button
		panelSetupBot = new JPanel();
		startButton = new JButton("Start Simulation");
		startButton.addActionListener(startSimulation);
		panelSetupBot.add(startButton);
		
		//Setup the frame and add the start options
		frame = new JFrame("Bot Chess Setup");
		frame.setLayout(new GridLayout(3, 1));
		frame.add(panelSetupTop);
		frame.add(panelSetupMid);
		frame.add(panelSetupBot);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        //Build the game view panels
        panelLeftPlayer = new JPanel();
        panelBoard = new GameBoard();
        panelBoard.addMouseListener(new GameMouseListener());
        panelRightPlayer = new JPanel();
        
        panelLeftPlayer.setPreferredSize(new Dimension(256, 512));
        panelBoard.setPreferredSize(new Dimension(512, 512));
        panelRightPlayer.setPreferredSize(new Dimension(256, 512));
        
        //Create the game frame and add the new panels
        frameGame = new JFrame("Bot Chess");
        frameGame.setLayout(new GridLayout(1, 3));
        frameGame.setSize(1024, 512);
        frameGame.add(panelLeftPlayer);
        frameGame.add(panelBoard);
        frameGame.add(panelRightPlayer);
        frameGame.setLocationRelativeTo(null);
        frameGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void loadInstance() {
        frame.setVisible(false);
        frameGame.setVisible(true);
        frameGame.pack();
        
        //TODO Load all settings and players properly
        currentGameRunnable = new GameRunnable(new GameState(), new Player[] {new PlayerHuman(1), new PlayerHuman(2)});
        currentGameThread = new Thread(currentGameRunnable);
        currentGameThread.start();
	}
	
	public static void tileClicked(int x, int y) {
		if(selectedX >= 0 && selectedX <= 7 && selectedY >= 0 && selectedY <= 7) {
			currentGameRunnable.getCurrentPlayers()[currentGameRunnable.getCurrentTurn()].tileClicked(x, y);
			selectedX = -10;
			selectedY = -10;
		}else {
			selectedX = x;
			selectedY = y;
		}
		paintBoard();
	}
	
	public static void paintBoard() {
		panelBoard.paintComponents(panelBoard.getGraphics());
	}
	
	public static int getSelectedX() {
		return selectedX;
	}
	
	public static int getSelectedY() {
		return selectedY;
	}
	
	public static GameRunnable getCurrentGameThread() {
		return currentGameRunnable;
	}
}
