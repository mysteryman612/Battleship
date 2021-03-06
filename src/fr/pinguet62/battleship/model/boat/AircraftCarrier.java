package fr.pinguet62.battleship.model.boat;

import fr.pinguet62.battleship.model.Alignment;
import fr.pinguet62.battleship.model.grid.Coordinates;

/** A aircraft carrier. */
public final class AircraftCarrier extends Boat {

    /** Default constructor. */
    public AircraftCarrier() {
    }

    /**
     * Constructor.
     * 
     * @param coordinates
     *            The {@link Coordinates} at top-left.
     * @param alignment
     *            The {@link Alignment}.
     */
    public AircraftCarrier(final Coordinates coordinates,
	    final Alignment alignment) {
	super(coordinates, alignment);
    }

    @Override
    public String getName() {
	return "Aircraft carrier";
    }

    @Override
    public int getSize() {
	return 4;
    }

}
