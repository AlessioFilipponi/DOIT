package it.unicam.cs.ids.doit.view;

import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Ruolo;
import it.unicam.cs.ids.doit.user.Utente;
/**
 * IMenu è la classe che gestisce i menu che visualizza l'utente in base al suo ruolo,
 * e se è stato effettuato o meno il login
 * */
public class IMenu {
	/**
	 * Menu visualizzato dal Guest, permette 
	 * @return user quando viene effettuato il login
	 */
	public static Utente menu() {
		int selezione = -1;
		boolean flag = true;
		Utente user = null;
		while (flag) {
			do{UserCommunicator.print("\n****> MENU PRINCIPALE <****\n" +
				"1) Visualizza progetti\n" +
				"2) Registrazione\n"+
				"3) Login\n" +
				"4) Esci");
//				"5) Logout");
			try{selezione = UserCommunicator.insertInteger("La tua scelta");}
			catch (Exception e) {
				UserCommunicator.print("Puoi solo inserire un numero da 1 a 6");
			}
			}while(selezione<0|| selezione>8 );
			switch(selezione)
			{
				case 1 : IGuest.VisualizzaProgetti();break;
				case 2 : IGuest.registrazione();
				case 3 : user = IGuest.logIn();if(user!=null) return user;break;
				case 4: flag = false;break;
//				case 5: main(args);flag=false;break;
			}
		}
			return user;
		
	}
	/* Menu dell'ente
	 * 
	 * 
	 */
	public static boolean myMenu(Ente user, boolean flag) {
		int selezione = -1;
		do{String updateMessage = "";
		if (user.getUtente().getMessage() && !user.getUtente().getNotifiche().isEmpty()) updateMessage = " **new**";
		UserCommunicator.print(
				"****> MENU PRINCIPALE <****\n" +
			"1) Visualizza progetti\n" +
			"2) Proponi un progetto\n" +
			"3) Valuta proposte di partecipazione\n" +
			"4) Invita Collaboratore\n" +
			"5) Richiedi Valutazione di una Proposta Progetto\n"+				
			"6) Visualizza Notifiche"+ updateMessage+ 
			"\n7) Esci\n"+
			"8) Logout");
		try{selezione = UserCommunicator.insertInteger("La tua scelta");}
		catch (Exception e) {
			UserCommunicator.print("Puoi solo inserire un numero da 1 a 6");
		}
		}while(selezione<0|| selezione>8 );
		switch(selezione)
		{
			case 1 : new IUtente(user.getUtente()).visualizzaProgetti();break;
			case 2: new IProponenteProgetto(user.getUtente()).createProject();break;
			case 3: new IUtente(user.getUtente()).valutaPartecipazioni();break;
			case 4: new IEnte(user.getUtente()).InvitaCollaboratore();
			case 5: new IProponenteProgetto(user.getUtente()).richiediValutazionePropostaProgetto(); break;
//			case 5: main(args);flag=false;break;
			case 6: new IUtente(user.getUtente()).visualizzaNotifiche();break;
			case 7: flag = false;break;
			case 8: user = null; IMenu.menu();
		}
		return flag;
		
	}
	/*
	 * Menu del progettista 
	 */
	public static boolean myMenu(Progettista user, boolean flag) {
		int selezione = -1;
		do{ String updateMessage = "";
		if (user.getUtente().getMessage() && !user.getUtente().getNotifiche().isEmpty()) updateMessage = " **new**";
			UserCommunicator.print(
				"\n ****> MENU PRINCIPALE <****\n" +
			"1) Visualizza progetti\n" +
			"2) Proponi un progetto\n" +
			"3) Valuta proposte di partecipazione\n" +
			"4) Richiedi Valutazione di una Proposta Progetto\n"+				
			"5) Visualizza Notifiche"+ updateMessage+
			"\n6) Esci\n"+
			"7) Logout");
		try{selezione = UserCommunicator.insertInteger("La tua scelta");
}
		catch (Exception e) {
			UserCommunicator.print("Puoi solo inserire un numero da 1 a 6");
			
		}
		}while(selezione<0|| selezione>7 );
		switch(selezione)
		{
			case 1: new IUtente(user.getUtente()).visualizzaProgetti();break;
			case 2: new IProponenteProgetto(user.getUtente()).createProject();break;
			case 3: new IUtente(user.getUtente()).valutaPartecipazioni();break;
			
			case 4: new IProponenteProgetto(user.getUtente()).richiediValutazionePropostaProgetto(); break;
//			case 5: main(args);flag=false;break;
			case 5: new IUtente(user.getUtente()).visualizzaNotifiche();break;
			case 6:flag = false;break;
			case 7: IMenu.menu();
		}
		return flag;
	}
	
	/*
	 * Metodo che in base al ruolo invoca il rispettivo menu 
	 */
	public static boolean myMenu(Ruolo r, boolean flag) {
		if (r.isEnte()) return myMenu((Ente)r, flag);
		else
			return myMenu((Progettista)r, flag);
		
	}

}
