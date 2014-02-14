package fr.pinguet62.battleship.view.game;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import fr.pinguet62.battleship.model.Game;
import fr.pinguet62.battleship.model.grid.Coordinates;

/** Duel view. */
public final class GameView extends JFrame implements ActionListener {

    /** Serial version UID. */
    private static final long serialVersionUID = -2190498449403789762L;

    /** The {@link Game}. */
    private final Game game;

    /**
     * Constructor.
     * 
     * @param game
     *            The {@link Game}.
     */
    public GameView(final Game game) {
	super("Battleship");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	this.game = game;

	// Layout
	Container mainContainer = getContentPane();
	mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.X_AXIS));

	// - Fleet
	JPanel fleetPanel = new JPanel();
	fleetPanel.setLayout(new BoxLayout(fleetPanel, BoxLayout.Y_AXIS));
	mainContainer.add(fleetPanel);
	// -- Title
	JLabel fleetLabel = new JLabel("Adversaire");
	fleetPanel.add(fleetLabel);
	// -- Grid
	JPanel gridFleetPanel = new JPanel();
	gridFleetPanel.setLayout(new GridLayout(game.getHeight(), game
		.getWidth()));
	fleetPanel.add(gridFleetPanel);
	// --- Buttons
	for (int y = 0; y < game.getHeight(); y++)
	    for (int x = 0; x < game.getWidth(); x++) {
		BoxView button = new BoxView(new Coordinates(x, y));
		button.addActionListener(this);
		gridFleetPanel.add(button);
	    }

	JSeparator seperator = new JSeparator(SwingConstants.VERTICAL);
	mainContainer.add(seperator);

	// - Opponent
	JPanel opponentPanel = new JPanel();
	opponentPanel.setLayout(new BoxLayout(opponentPanel, BoxLayout.Y_AXIS));
	mainContainer.add(opponentPanel);
	// -- Title
	JLabel opponentLabel = new JLabel("Ma flotte");
	opponentPanel.add(opponentLabel);
	// -- Grid
	JPanel gridOpponenPanel = new JPanel();
	gridOpponenPanel.setLayout(new GridLayout(game.getHeight(), game
		.getWidth()));
	opponentPanel.add(gridOpponenPanel);
	// --- Buttons
	for (int y = 0; y < game.getHeight(); y++)
	    for (int x = 0; x < game.getWidth(); x++) {
		BoxView button = new BoxView(new Coordinates(x, y));
		button.setEnabled(false);
		gridOpponenPanel.add(button);
	    }

	pack();
	setVisible(true);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
	// BoxView boxView = (BoxView) event.getSource();
	// Coordinates coordinates = boxView.getCoordinates();
	// boolean touched = game.getFleet().getBox(coordinates).attack();
	// boxView.setState(touched ? State.TOUCHED : State.FAILED);
    }

    // public void refreshFleet(final Coordinates coordinates) {
    // for (BoxView[] boxViews : fleet)
    // for (BoxView boxView : boxViews)
    // if (boxView.getCoordinates().equals(coordinates)) {
    // Box target = game.getFleet().getBox(coordinates);
    // Color color = BoxView.getState().getColor(target);
    // boxView.setBackground(color);
    // return;
    // }
    // throw new IllegalArgumentException("Coordinates not found.");
    // }

}
