package fr.pinguet62.battleship.view;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import fr.pinguet62.battleship.view.parameters.GuestParametersView;
import fr.pinguet62.battleship.view.parameters.HostParametersView;

/** First view: choice between host and guest. */
public final class PlayerTypeView extends Frame {

    /** Serial version UID. */
    private static final long serialVersionUID = 8779709289758249345L;

    /** Constructor. */
    public PlayerTypeView() {
	super("Player type");
	setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

	// - Host
	JButton hostButton = new JButton("Host");
	hostButton.addActionListener((event) -> {
	    System.out.println("HOST");
	    new HostParametersView();
	    dispose();
	});
	add(hostButton);
	// - Guest
	JButton guestButton = new JButton("Guest");
	guestButton.addActionListener((event) -> {
	    System.out.println("GUEST");
	    new GuestParametersView();
	    dispose();
	});
	add(guestButton);

	setVisible(true);
    }

}
