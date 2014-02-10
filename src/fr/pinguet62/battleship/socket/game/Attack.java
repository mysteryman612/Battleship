package fr.pinguet62.battleship.socket.game;

import java.io.Serializable;

import fr.pinguet62.battleship.model.grid.Coordinates;

/**
 * Store informations about an attack.<br />
 * Sent to opponent player.
 */
public class Attack implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 4311698328134751924L;

    /** The {@link Coordinates} of the attack. */
    private final Coordinates coordinates;

    /**
     * Constructor.
     * 
     * @param coordinates
     *            The {@link Coordinates} of the attack.
     */
    public Attack(final Coordinates coordinates) {
	this.coordinates = coordinates;
    }

    /**
     * Gets the {@link Coordinates} of the attack.
     * 
     * @return The {@link Coordinates} of the attack.
     */
    public Coordinates getCoordinates() {
	return coordinates;
    }

}