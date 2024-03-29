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
	
	private static boolean forceTwoPlayers = true;
	
	static ActionListener playerChange = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(forceTwoPlayers) {
				return;
			}
			//Add blank player at the end if the play player isnt "No player"
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
		panelSetupTop.add(new JLabel("Minimum Turn Time (ms):"));
		inputTurnTime = new JTextField("1000");
		panelSetupTop.add(inputTurnTime);
		panelSetupTop.add(new JLabel("Games Per Matchup:"));
		inputGamesPerMatchup = new JTextField("1");
		panelSetupTop.add(inputGamesPerMatchup);
		
		//Build the player list
		panelSetupMid = new JPanel();
		panelSetupMid.setLayout(new BoxLayout(panelSetupMid, BoxLayout.PAGE_AXIS));
		panelSetupMid.setLayout(new GridLayout(2, 1));
		playerNames = new String[] {"No Player", "Human", "Random", "MiniMax Recursive (Depth = 5)"};
		JComboBox<String> temp = new JComboBox<String>(playerNames);
		temp.addActionListener(playerChange);
		panelSetupMid.add(temp);
		if(forceTwoPlayers) {
			JComboBox<String> temp2 = new JComboBox<String>(playerNames);
			temp2.addActionListener(playerChange);
			panelSetupMid.add(temp2);
		}
		
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
        panelLeftPlayer.setLayout(new GridLayout(2, 1));
        panelBoard = new GameBoard();
        panelBoard.addMouseListener(new GameMouseListener());
        panelRightPlayer = new JPanel();
        panelRightPlayer.setLayout(new GridLayout(2, 1));
        
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
		//Validate input settings
		if(Integer.valueOf(inputTurnTime.getText()) == null) {
			return;
		}
		if(Integer.valueOf(inputGamesPerMatchup.getText()) == null) {//TODO Use this setting
			return;
		}
		if(panelSetupMid.getComponentCount() != 3 && !forceTwoPlayers) {//TODO make this accept more that two players (Value is 3 because the last combo box will always be "No player")
			return;
		}
		
		//Build the player list
		int maxPlayerBoxes = panelSetupMid.getComponentCount();
		if(!forceTwoPlayers) {
			maxPlayerBoxes--;
		}
		Player[] players = new Player[panelSetupMid.getComponentCount()];
		for(int i = 0; i < maxPlayerBoxes; i++) {
			switch(((JComboBox<String>)panelSetupMid.getComponent(i)).getSelectedIndex()) {
			case 0:
				return;
			case 1:
				players[i] = new PlayerHuman(i + 1);
				break;
			case 2:
				players[i] = new PlayerRandom(i + 1);
				break;
			case 3:
				players[i] = new PlayerMiniMaxRecursive(i + 1, new EvaluationStrengthCount(), 5);
				break;
			}
		}

		//Setup the frame
        frame.setVisible(false);
        frameGame.setVisible(true);
        frameGame.pack();
        
        //Setup the game thread
        String[] playerNames = new String[2];
        for(int i = 0; i < panelSetupMid.getComponentCount() - 1; i++) {
        	playerNames[i] = (String)((JComboBox<String>)panelSetupMid.getComponent(i)).getSelectedItem();
        }
        currentGameRunnable = new GameRunnable(new GameState(), players, Integer.valueOf(inputTurnTime.getText()), playerNames);
        currentGameThread = new Thread(currentGameRunnable);
        
        //Setup the info frames
        panelLeftPlayer.add(new JLabel("White Player"));
        panelLeftPlayer.add(new JLabel(currentGameRunnable.getPlayerNames()[0]));
        panelRightPlayer.add(new JLabel("Black Player"));
        panelRightPlayer.add(new JLabel(currentGameRunnable.getPlayerNames()[1]));
        
        //Start the thread
        currentGameThread.start();
	}
	
	public static void gameFinished(GameRunnable thread) {
		String winner = "";
		if(thread.getCurrentGameState().getWinner() == 1) {
			winner = "White";
		}else {
			winner = "Black";
		}
		System.out.println("Game Finished : Winner = " + winner + " : Turns Taken = " + thread.getCurrentGameState().getTurnsTaken() + "(" + Math.ceil((thread.getCurrentGameState().getTurnsTaken()/2)) + ")");
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
