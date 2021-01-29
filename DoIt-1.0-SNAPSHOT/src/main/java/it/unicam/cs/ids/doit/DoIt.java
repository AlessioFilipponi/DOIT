package it.unicam.cs.ids.doit;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.view.IProponenteProgetto;
import it.unicam.cs.ids.doit.view.IUtente;

public class DoIt {

	public static void main(String [] args) {
		UserCommunicator.print("" +
				"* * * * * * * * * * * * * * * * * * * *\n" +
				"*  BENVENUTO nella community di DOIT  *\n" +
				"* * * * * * * * * * * * * * * * * * * *\n" +
				"\n"
		);
		String IDutente = UserCommunicator.insertString("Inserisci il tuo ID utente");
		Utente user = null;
		Bacheca.getInstance();
		for(Utente u: Bacheca.getInstance().getCatalogoUtenti())
			if(u.getID().equals(IDutente))
				user=u;
		if(user==null)
		{
			UserCommunicator.print("\n\n\n\nUtente non trovato!! \n\nFORM DI REGISTRAZIONE \n");
			Utente ut=new Utente(UserCommunicator.insertString("Inserire Nome"), UserCommunicator.insertString("Inserire Cognome"));
			Bacheca.getInstance().getCatalogoUtenti().add(ut);
			UserCommunicator.insertString("Il tuo ID è: "+ut.getID()+" Premere [INVIO] per continuare... ");
			main(new String[]{});
			return;
		}
		boolean flag = true;
		while (flag) {UserCommunicator.print("\n\n\n ****> MENU PRINCIPALE <****\n" +
				"1) Visualizza progetti\n" +
				"2) Proponi un progetto\n" +
				"3) Valuta inviti a partecipare\n" +
				"4) Esci\n"+
				"5) Logout");
			int selezione = UserCommunicator.insertInteger("La tua scelta");
			switch(selezione)
			{
				case 1 : new IUtente(user).visualizzaProgetti();break;
				case 2: new IProponenteProgetto(user).createProject();break;
				case 3: new IUtente(user).valutaPartecipazioni();break;
				case 4: flag = false;break;
				case 5: main(args);flag=false;break;
			}
		}
		if (!flag) System.out.println("**BYE BYE**");
	}

}