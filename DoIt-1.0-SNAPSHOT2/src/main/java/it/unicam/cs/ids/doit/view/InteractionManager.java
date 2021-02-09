package it.unicam.cs.ids.doit.view;

import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Utente;

public class InteractionManager {
	
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
	