package it.unicam.cs.ids.doit.view;

import java.sql.SQLException;

import javax.security.auth.login.LoginContext;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
import it.unicam.cs.ids.doit.utilities.SystemUtilities;

public class InteractionManager {
	
	public static void start() {
		UserCommunicator.print("" +
				"* * * * * * * * * * * * * * * * * * * *\n" +
				"*  BENVENUTO nella community di DOIT  *\n" +
				"* * * * * * * * * * * * * * * * * * * *\n" +
				"\n"
		);
		
		int c = -1;
		do{
			try {c =UserCommunicator.insertInteger("Vuoi effettuare il Login?\n"
				+ "[1] yes "
				+ "[0] no "
				+ "[2] registrazione");}
			catch (Exception e) {
				UserCommunicator.print("Puoi solo inserire [1] [0] [2]");
			}
		}while(c<0 || c>2 );
		Utente user;
		switch (c){
		case 0: IGuest.Menu();break;
		case 2: user= registrazione() ;{if (user == null) IGuest.Menu() ;} 
		case 1:
			user =logIn();
			if (user == null) IGuest.Menu();
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
//			start();
////			return;
		
		boolean flag = true;
		while (flag) {
			int selezione = -1;
			do{UserCommunicator.print("\n ****> MENU PRINCIPALE <****\n" +
				"1) Visualizza progetti\n" +
				"2) Proponi un progetto\n" +
				"3) Valuta proposte di partecipazione\n" +
				"4) Invita Collaboratore\n" +
				"5) Richiedi Valutazione di una Proposta Progetto\n"+
				"6) Visualizza Notifiche\n"+ 
				"7) Esci\n");
//				"5) Logout");
			if (user.getStato()) UserCommunicator.print("Notifiche(" + user.getNotifiche().size()+")");
			try{selezione = UserCommunicator.insertInteger("La tua scelta");}
			catch (Exception e) {
				UserCommunicator.print("Puoi solo inserire un numero da 1 a 6");
			}
			}while(selezione<0|| selezione>7 );
			switch(selezione)
			{
				case 1 : new IUtente(user).visualizzaProgetti();break;
				case 2: new IProponenteProgetto(user).createProject();break;
				case 3: new IUtente(user).valutaPartecipazioni();break;
				case 4: if(user.getRole().isEnte()) new IEnte(user).InvitaCollaboratore(); break;
				case 5: new IProponenteProgetto(user).richiediValutazionePropostaProgetto(); break;
//				case 5: main(args);flag=false;break;
				case 6: new IUtente(user).visualizzaNotifiche();break;
				case 7:flag = false;break;
			}
		}
		if (!flag) System.out.println("**BYE BYE**");
		}
}
	
	public static Utente logIn() {
		String username = UserCommunicator.insertString("Inserisci il tuo username");
		if (SystemUtilities.getInstance().exist(username)) {
			String password = UserCommunicator.insertString("Inserisci il tuo password");
			if (SystemUtilities.getInstance().getPassword().get(username).equals(password.hashCode())) {
				return SystemUtilities.getInstance().getUtente(username);
			}
			else {
				UserCommunicator.print("Password Errata");
				return null;
			}
		}
		
//		Bacheca.getInstance();
//		for(Utente u: Bacheca.getInstance().getCatalogoUtenti())
//			if(u.getID().equals(IDutente))
//				return u;
		else {
			UserCommunicator.print("Username Inesistente");
			int c =-1;
			do{
			try {c =UserCommunicator.insertInteger("Vuoi effettuare la registrazione?\n"
					+ "[1] yes "
					+ "[0] no");}
			catch (Exception e) {
				UserCommunicator.print("Puoi solo inserire [1] o [0]");
			}}while(c<0 || c>1 );
			switch(c){
			case 1: return registrazione();
			
			}
			
		}
		return null;
	}
	
	public static Utente registrazione() {
		UserCommunicator.print("\n\nFORM DI REGISTRAZIONE \n");
		String ut=UserCommunicator.insertString("Inserire username");
		while ( SystemUtilities.getInstance().exist(ut)) {
			UserCommunicator.print("Username esistente");
			ut=UserCommunicator.insertString("Inserire username");
		}
		String pass = UserCommunicator.insertString("Inserire password");
		SystemUtilities.getInstance().getPassword().put(ut, pass.hashCode());
	
		int c =-1;
		Utente u = null;
		do{
		try {c =UserCommunicator.insertInteger("Vuoi creare un profilo Ente?\n"
				+ "[1] yes "
				+ "[0] no");}
		catch (Exception e) {
			UserCommunicator.print("Puoi solo inserire [1] o [0]");
		}}while(c<0 || c>1 );
		switch(c){
		case 1: u = creaProfiloEnte(ut);break;
		case 0: u =  creaPriloUtente(ut);break;
		
		}
		if (u==null) SystemUtilities.getInstance().getPassword().remove(ut);
//		UserCommunicator.insertString("Il tuo ID è: "+ut.getID()+" Premere [INVIO] per continuare... ");
//		start();
		else
			try {
				DBManager.getInstance().insertUtente(u, pass);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.getMessage();
			}
		return u;
	}

	private static Utente creaPriloUtente(String ut) {
		Utente u = new Utente(ut);
		u.insertName(UserCommunicator.insertString("Inserisci Nome e Cognome"));
		u.insertEmail(UserCommunicator.insertString("Inserisci Email"));
		u.getCurriculum().getCompetenze().addAll(UserCommunicator.selectMultipleElements(SystemUtilities.getInstance().getCompetenze(), "Seleziona Competenze"));
		u.getCurriculum().setDescrizione(UserCommunicator.insertString("Inserisci una descrizione delle tue esperienze lavorative"));
		int c =-1;
		try {c =UserCommunicator.insertInteger("Vuoi confermare?\n"
				+ "[1] yes "
				+ "[0] no");}
		catch (Exception e) {
			UserCommunicator.print("Puoi solo inserire [1] o [0]");
		}switch(c){
		case 1: {SystemUtilities.getInstance().getUtenti().put(ut, u);
		Bacheca.getInstance().getCatalogoUtenti().add(u);
		u.getRole().addToCatalogo();
		
		break;}
		case 0: u = null;
		
		}
		return u;
	}

	private static Utente creaProfiloEnte(String ut) {
		Utente u = new Utente(ut);
		u.setRuolo(new Ente(u));
		Ente ente = (Ente) u.getRole();
		u.insertName(UserCommunicator.insertString("Inserisci nome dell'Ente"));
		u.insertEmail(UserCommunicator.insertString("Inserisci Email"));
		ente.setDescrizione(UserCommunicator.insertString("Inserisci una descrizione delle tue esperienze lavorative"));
		int c =-1;
		try {c =UserCommunicator.insertInteger("Vuoi confermare?\n"
				+ "[1] yes "
				+ "[0] no");}
		catch (Exception e) {
			UserCommunicator.print("Puoi solo inserire [1] o [0]");
		}switch(c){
		case 1: {SystemUtilities.getInstance().getUtenti().put(ut, u);
		Bacheca.getInstance().getCatalogoUtenti().add(u);
		u.getRole().addToCatalogo();
		int d =-1;
		try {d =UserCommunicator.insertInteger("Vuoi invitare dei collaboratori?\n"
				+ "[1] yes "
				+ "[0] no");}
		catch (Exception e) {
			UserCommunicator.print("Puoi solo inserire [1] o [0]");
		}switch(d){
		case 1: {new IEnte(u).InvitaCollaboratore(); break;
		}
		case 0: break;}
		}
		case 0: u = null;
		
		}
		return u;
		
	}
}



