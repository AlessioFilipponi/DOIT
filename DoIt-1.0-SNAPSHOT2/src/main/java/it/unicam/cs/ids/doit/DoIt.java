package it.unicam.cs.ids.doit;

import java.sql.SQLException;
import java.util.List;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Invito;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
import it.unicam.cs.ids.doit.utilities.SystemUtilities;
import it.unicam.cs.ids.doit.view.IProponenteProgetto;
import it.unicam.cs.ids.doit.view.IUtente;
import it.unicam.cs.ids.doit.view.InteractionManager;

public class DoIt {

	public static void main(String [] args) {
		//popolo il sistema
  	    Utente u1 = new Utente("ciccio");
  	    SystemUtilities.getInstance().getPassword().put("ciccio", "pass".hashCode());
  	    SystemUtilities.getInstance().getUtenti().put("ciccio", u1);
  	    u1.insertName("Ciccio");
//		u1.setRuolo(new Progettista(u1));
//		Utente u2 = new Utente("Tiziano","Rossi");
//		u2.setRuolo(new Progettista(u2));
//		Utente u3 = new Utente("Francesco","Mari");
//		u3.setRuolo(new Progettista(u3));
//		Utente u4 = new Utente("Ottavio","Bianchi");
//		u4.setRuolo(new Progettista(u4));
//		Utente u5 = new Utente("Sara","Polli");
//		u5.setRuolo(new Progettista(u5));
//		Utente u6 = new Utente("Daniele","Assi");
//		u6.setRuolo(new Progettista(u6));
		Utente u8 = new Utente("Universitàdi Camerino");
		u8.setRuolo(new Ente(u8));
		u8.insertName("Uni");
		((Ente) u8.getRole()).setDescrizione("nwef");
		Progetto p = new Progetto(u1);
		p.setTitolo("Progetto IDS");
		p.setSpecifiche("Progetto corso di Ingegneria del Software");
		Bacheca.getInstance().getCatalogoProgetti().add(p);
		Progetto p1 = new Progetto(u1);
		p1.setTitolo("Progetto ASD");
		p1.setSpecifiche("Progetto corso di Algoritmi e Strutture Dati");
		Bacheca.getInstance().getCatalogoProgetti().add(p1);
		Progetto p2 = new Progetto(u1);
		p2.setTitolo("Progetto IRS");
		p2.setSpecifiche("Progetto corso di Internet Reti e Sicurezza");
		Bacheca.getInstance().getCatalogoProgetti().add(p2);
		Progetto p3 = new Progetto(u1);
		p3.setTitolo("Progetto FCC");
		p3.setSpecifiche("Progetto corso di Fondamenti di Cloud Computing");
//		Bacheca.getInstance().getCatalogoProgetti().add(p3);
//		Utente u7 = new Utente("Angela", "Forni");
//		u7.setRuolo(new Esperto(u7));
//		Utente u9 = new Utente("Olga", "Bernini");
//		u9.setRuolo(new Esperto(u9));
		Partecipazione par = new Partecipazione(u1, p);
		Partecipazione par1 = new Partecipazione(u1, p1);
		Partecipazione par2 = new Partecipazione(u1, p2);
		Invito i = new Invito(u8, u1);
//
//		UserCommunicator.print("" +
//				"* * * * * * * * * * * * * * * * * * * *\n" +
//				"*  BENVENUTO nella community di DOIT  *\n" +
//				"* * * * * * * * * * * * * * * * * * * *\n" +
//				"\n"
//		);
//		String IDutente = UserCommunicator.insertString("Inserisci il tuo ID utente");
//		Utente user = null;
//		Bacheca.getInstance();
//		for(Utente u: Bacheca.getInstance().getCatalogoUtenti())
//			if(u.getID().equals(IDutente))
//				user=u;
//		if(user==null)
//		{
//			UserCommunicator.print("\n\n\n\nUtente non trovato!! \n\nFORM DI REGISTRAZIONE \n");
//			Utente ut=new Utente(UserCommunicator.insertString("Inserire Nome"), UserCommunicator.insertString("Inserire Cognome"));
//			Bacheca.getInstance().getCatalogoUtenti().add(ut);
//			UserCommunicator.insertString("Il tuo ID è: "+ut.getID()+" Premere [INVIO] per continuare... ");
//			main(new String[]{});
//			return;
//		}
//		boolean flag = true;
//		while (flag) {UserCommunicator.print("\n\n\n ****> MENU PRINCIPALE <****\n" +
//				"1) Visualizza progetti\n" +
//				"2) Proponi un progetto\n" +
//				"3) Valuta inviti a partecipare\n" +
//				"4) Esci\n"+
//				"5) Logout");
//			int selezione = UserCommunicator.insertInteger("La tua scelta");
//			switch(selezione)
//			{
//				case 1 : new IUtente(user).visualizzaProgetti();break;
//				case 2: new IProponenteProgetto(user).createProject();break;
//				case 3: new IUtente(user).valutaPartecipazioni();break;
//				case 4: flag = false;break;
//				case 5: main(args);flag=false;break;
//			}
//		}
//		if (!flag) System.out.println("**BYE BYE**");
//		System.out.println(u7.getID());
		try {

			DBManager.getInstance().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		try {
			List<Progetto> l = DBManager.getInstance().listaProgetti();
			for (Progetto progetto : l) {
				System.out.println(progetto.toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InteractionManager.start();
	}

}