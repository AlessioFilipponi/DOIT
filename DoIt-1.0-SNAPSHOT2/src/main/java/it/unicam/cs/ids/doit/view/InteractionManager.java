package it.unicam.cs.ids.doit.view;

import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Utente;

/**
 * Classe per la gestione dell'interazione con l'utente
 *
 */
public class InteractionManager {
	
	/**
	 * Metodo che avvia il sistema con un messaggio di bevenuto 
	 */
	public static void start() {
		UserCommunicator.print("" +
				"* * * * * * * * * * * * * * * * * * * *\n" +
				"*  BENVENUTO nella community di DOIT  *\n" +
				"* * * * * * * * * * * * * * * * * * * *" +
				""
		);
		Utente user = IMenu.menu();
		boolean flag = true;
		while (flag && user!=null) {
			flag = IMenu.myMenu(user.getRole(), flag);

		}
		System.out.println("**BYE BYE**");
	}
}
	