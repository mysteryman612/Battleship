package fr.pinguet62.battleship.view.positioning;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.pinguet62.battleship.model.Game;
import fr.pinguet62.battleship.model.boat.Boat;
import fr.pinguet62.battleship.model.grid.Coordinates;
import fr.pinguet62.battleship.view.positioning.SelectCase.State;

/** View used to place {@link Boat}s in grid. */
public final class FleetPositioningView extends JFrame implements
	ActionListener {

    private enum Direction {

	/** Bottom. */
	BOTTOM(0, +1), /** Left. */
	LEFT(-1, 0), /** Right. */
	RIGHT(+1, 0), /** Top. */
	TOP(0, -1);

	/** X direction. */
	final int sensX;

	/** Y direction. */
	final int sensY;

	/**
	 * Constructor.
	 * 
	 * @param sensX
	 *            The X direction.
	 * @param sensY
	 *            The Y direction.
	 */
	Direction(final int sensX, final int sensY) {
	    this.sensX = sensX;
	    this.sensY = sensY;
	}

	/**
	 * Gets the X direction.
	 * 
	 * @return The X direction.
	 */
	public int getSensX() {
	    return sensX;
	}

	/**
	 * The Y direction.
	 * 
	 * @return The Y direction.
	 */
	public int getSensY() {
	    return sensY;
	}

	/**
	 * Tests if is horizontal.
	 * 
	 * @return Result.
	 */
	public boolean isHorizontal() {
	    return (sensX == 0) && (sensY != 0);
	}

    }

    /** Serial version UID. */
    private static final long serialVersionUID = 8676896327347418615L;

    /** The {@link JPanel} of {@link BoatView}s. */
    final JPanel boatsPanel;

    /** The {@link Boat}s to place in grid. */
    private final Collection<BoatView> boatViews = new ArrayList<BoatView>();

    /** The grid of {@link SelectCase}s. */
    private final SelectCase[][] casess;

    /** The {@link Game}. */
    private final Game game;

    /** The {@link JPanel}. */
    private final JPanel gridFleetPanel;

    /** The selected {@link Boat}. */
    private Boat selectedBoat;

    /** The selected {@link BoatView}. */
    private BoatView selectedBoatView;

    /**
     * Constructor.
     * 
     * @param game
     *            The {@link Game}.
     * @param boats
     *            The {@link Boat}s to place.
     */
    public FleetPositioningView(final Game game, final Collection<Boat> boats) {
	super("Fleet Positioning");

	this.game = game;

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Layout
	Container mainContainer = getContentPane();
	mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.X_AXIS));
	// - Boats
	boatsPanel = new JPanel();
	boatsPanel.setLayout(new BoxLayout(boatsPanel, BoxLayout.Y_AXIS));
	mainContainer.add(boatsPanel);
	for (Boat boat : boats) {
	    final BoatView boatView = new BoatView(boat);
	    boatView.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(final ActionEvent e) {
		    // Save selection
		    selectedBoat = boatView.getBoat();
		    selectedBoatView = boatView;
		    // Disable buttons
		    for (BoatView boatView : boatViews)
			boatView.setEnabled(false);
		}
	    });
	    boatViews.add(boatView);
	    boatsPanel.add(boatView);
	}
	// - Grid
	gridFleetPanel = new JPanel();
	gridFleetPanel.setLayout(new GridLayout(game.getHeight(), game
		.getWidth()));
	mainContainer.add(gridFleetPanel);
	// -- Buttons
	casess = new SelectCase[game.getHeight()][game.getWidth()];
	for (int y = 0; y < game.getHeight(); y++)
	    for (int x = 0; x < game.getWidth(); x++) {
		SelectCase button = new SelectCase(new Coordinates(x, y));
		button.addActionListener(this);
		gridFleetPanel.add(button);
		casess[y][x] = button;
	    }

	pack();
	setVisible(true);
    }

    /**
     * Click on a {@link SelectCase}.
     * 
     * @param e
     *            The {@link ActionEvent}.
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
	if (selectedBoat == null)
	    return;

	SelectCase clickCase = (SelectCase) e.getSource();

	clickCase
		.setState(clickCase.getState().equals(State.CHOOSED) ? State.SELECTABLE
			: State.CHOOSED);

	refresh();

	// Count ok
	List<SelectCase> choosedCases = getSelectedCases(State.CHOOSED);
	if (choosedCases.size() == selectedBoat.getSize()) {
	    // TODO insert boat
	    for (SelectCase choosedCase : choosedCases)
		choosedCase.setState(State.BOAT);
	    for (SelectCase unselectableCase : getSelectedCases(State.UNSELECTABLE))
		unselectableCase.setState(State.SELECTABLE);
	    selectedBoat = null;

	    selectedBoatView.setPlaced(true);
	    // Refresh buttons
	    for (BoatView boatView : boatViews)
		if (!boatView.isPlaced())
		    boatView.setEnabled(true);
	}
    }

    /**
     * Get the index of first (for {@link Direction#TOP} or
     * {@link Direction#LEFT}) or last (for {@link Direction#BOTTOM} and
     * {@link Direction#RIGHT}) {@link SelectCase} available (without
     * {@link Boat}).
     * 
     * @param coordinates
     *            The start {@link Coordinates}.
     * @param dir
     *            The {@link Direction}.
     * @param size
     *            The number of {@link SelectCase} between coordinates and
     *            first/last.
     * @return The index.
     */
    private int getLastAvailable(final Coordinates coordinates,
	    final Direction dir, final int size) {
	int x = coordinates.getX();
	int y = coordinates.getY();
	while (((0 <= x) && (x < game.getWidth()))
		&& ((0 <= y) && (y < game.getHeight()))
		&& !casess[y][x].getState().equals(State.BOAT)
		&& ((Math.abs(x - coordinates.getX()) + Math.abs(y
			- coordinates.getY())) < size)) {
	    x += dir.getSensX();
	    y += dir.getSensY();
	}
	if (dir.isHorizontal())
	    return y;
	else
	    return x;
    }

    /**
     * Gets {@link SelectCase}s by {@link State}.
     * 
     * @param state
     *            The {@link State}.
     * @return The {@link SelectCase}s.
     */
    private List<SelectCase> getSelectedCases(final State state) {
	List<SelectCase> selectedCases = new ArrayList<SelectCase>();
	for (int j = 0; j < game.getHeight(); j++)
	    for (int i = 0; i < game.getWidth(); i++)
		if (casess[j][i].getState().equals(state))
		    selectedCases.add(casess[j][i]);
	return selectedCases;
    }

    /** Refresh the grid. */
    private void refresh() {
	// Reset selection
	for (int j = 0; j < game.getHeight(); j++)
	    for (int i = 0; i < game.getWidth(); i++)
		if (!Arrays.asList(State.BOAT, State.CHOOSED).contains(
			casess[j][i].getState()))
		    casess[j][i].setState(State.SELECTABLE);

	// Choosed cases
	List<SelectCase> selectedCases = new ArrayList<SelectCase>();
	for (int j = 0; j < game.getHeight(); j++)
	    for (int i = 0; i < game.getWidth(); i++)
		if (casess[j][i].getState().equals(State.CHOOSED))
		    selectedCases.add(casess[j][i]);

	// 0 selection
	if (selectedCases.size() == 0) {
	    for (int j = 0; j < game.getHeight(); j++)
		for (int i = 0; i < game.getWidth(); i++)
		    if (!casess[j][i].getState().equals(State.BOAT))
			casess[j][i].setState(State.SELECTABLE);
	} else // 1 selection : cross
	if (selectedCases.size() == 1) {
	    Coordinates selected = selectedCases.get(0).getCoordinates();
	    final int x = selected.getX();
	    final int y = selected.getY();
	    // Complementary to the cross
	    for (int j = 0; j < game.getHeight(); j++)
		for (int i = 0; i < game.getWidth(); i++)
		    if ((i != x) && (j != y))
			if (casess[j][i].getState().equals(State.SELECTABLE))
			    casess[j][i].setState(State.UNSELECTABLE);
	    // Cross
	    // - horizontal
	    int left = getLastAvailable(selected, Direction.LEFT,
		    selectedBoat.getSize() - 1);
	    int right = getLastAvailable(selected, Direction.RIGHT,
		    selectedBoat.getSize() - 1);
	    for (int i = 0; i < left; i++)
		if (!casess[selected.getY()][i].getState().equals(State.BOAT))
		    casess[selected.getY()][i].setState(State.UNSELECTABLE);
	    for (int i = left; i <= right; i++) {
	    }
	    for (int i = right + 1; i < game.getWidth(); i++)
		if (!casess[selected.getY()][i].getState().equals(State.BOAT))
		    casess[selected.getY()][i].setState(State.UNSELECTABLE);
	    // - vertical
	    int top = getLastAvailable(selected, Direction.TOP,
		    selectedBoat.getSize() - 1);
	    int bottom = getLastAvailable(selected, Direction.BOTTOM,
		    selectedBoat.getSize() - 1);
	    for (int j = 0; j < top; j++)
		if (!casess[j][selected.getX()].getState().equals(State.BOAT))
		    casess[j][selected.getX()].setState(State.UNSELECTABLE);
	    for (int j = top; j <= bottom; j++) {
	    }
	    for (int j = bottom + 1; j < game.getHeight(); j++)
		if (!casess[j][selected.getX()].getState().equals(State.BOAT))
		    casess[j][selected.getX()].setState(State.UNSELECTABLE);
	}
	// Many selection : horizontal or vertical
	else {
	    Coordinates first = selectedCases.get(0).getCoordinates();
	    Coordinates last = selectedCases.get(selectedCases.size() - 1)
		    .getCoordinates();
	    // Horizontal
	    if (first.getY() == last.getY()) {
		final int y = first.getY();
		// Out of line
		for (int j = 0; j < game.getHeight(); j++)
		    if (j != y)
			for (int i = 0; i < game.getWidth(); i++)
			    if (!casess[j][i].getState().equals(State.BOAT))
				casess[j][i].setState(State.UNSELECTABLE);
		// Join
		for (int i = first.getX(); i <= last.getX(); i++)
		    casess[y][i].setState(State.CHOOSED);
		// Available space
		int size = selectedBoat.getSize()
			- ((last.getX() - first.getX()) + 1);
		int left = getLastAvailable(first, Direction.LEFT, size);
		int right = getLastAvailable(last, Direction.RIGHT, size);
		for (int i = 0; i < left; i++)
		    if (!casess[y][i].getState().equals(State.BOAT))
			casess[y][i].setState(State.UNSELECTABLE);
		for (int i = left; i <= right; i++) {
		}
		for (int i = right + 1; i < game.getWidth(); i++)
		    if (!casess[y][i].getState().equals(State.BOAT))
			casess[y][i].setState(State.UNSELECTABLE);
	    }
	    // Vertical
	    else {
		final int x = first.getX();
		// Out of column
		for (int i = 0; i < game.getWidth(); i++)
		    if (i != x)
			for (int j = 0; j < game.getHeight(); j++)
			    if (!casess[j][i].getState().equals(State.BOAT))
				casess[j][i].setState(State.UNSELECTABLE);
		// Join
		for (int j = first.getY(); j <= last.getY(); j++)
		    casess[j][x].setState(State.CHOOSED);
		// Available space
		int size = selectedBoat.getSize()
			- ((last.getY() - first.getY()) + 1);
		int top = getLastAvailable(first, Direction.TOP, size);
		int bottom = getLastAvailable(last, Direction.BOTTOM, size);
		for (int j = 0; j < top; j++)
		    if (!casess[j][x].getState().equals(State.BOAT))
			casess[j][x].setState(State.UNSELECTABLE);
		for (int j = top; j <= bottom; j++) {
		}
		for (int j = bottom + 1; j < game.getHeight(); j++)
		    if (!casess[j][x].getState().equals(State.BOAT))
			casess[j][x].setState(State.UNSELECTABLE);
	    }
	}
    }

}