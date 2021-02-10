package it.unicam.cs.ids.doit.view;

import java.sql.SQLException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
import it.unicam.cs.ids.doit.utilities.SystemUtilities;

public class IGuest {
	
	public static void VisualizzaProgetti() {
        Collection<Progetto> catalogo = Bacheca.getInstance().getCatalogoProgetti();
        if(catalogo.isEmpty()) {
        	UserCommunicator.print("Non ci sono progetti");
        	return;
        } else {
		Progetto p = selezionaProgetto(catalogo);//Chiedo all'utente di selezionare un progetto tra tutti quelli presenti nel sistema
		if(p==null) //Se non ne ha selezionato nessuno o il progetto selezionato non esiste
			return; //Annullo la procedura
		visualizzaDettagliProgetto(p); //Altrimenti visualizzo i dettagli del progetto
        }
	}

	public static Progetto selezionaProgetto(Collection<Progetto> projects){
		return UserCommunicator.selectElement(projects,"Seleziona un progetto");
	}
	/**
	 * 
	 * @param p
	 */
	public static void visualizzaDettagliProgetto(Progetto p) {
		UserCommunicator.print(p.toString());//stampa i dettagli del progetto
		
	}
	

	public static Utente logIn() {
		UserCommunicator.print("***LOGIN***\n");
		String username = UserCommunicator.insertString("Inserisci il tuo username");
		String password = UserCommunicator.insertString("Inserisci la tua password");
		if (SystemUtilities.getInstance().exist(username)) {
//			String password = UserCommunicator.insertString("Inserisci la tua password");
			if (SystemUtilities.getInstance().getPassword().get(username).equals(password.hashCode())) {
				UserCommunicator.print("**Benvenuto " + username + "**");
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
		try {c =UserCommunicator.insertInteger("Vuoi creare un profilo Ente?(Un Ente è una società/ente pubblico)\n"
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
		else
			try {
				DBManager.getInstance().insertUtente(u, pass);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//		UserCommunicator.insertString("Il tuo ID è: "+ut.getID()+" Premere [INVIO] per continuare... ");
//		start();
		return u;
	}

	private static Utente creaPriloUtente(String ut) {
		Utente u = new Utente(ut);
		u.setRuolo(new Progettista(u));
		u.insertName(UserCommunicator.insertString("Inserisci Nome e Cognome"));
		String email;
		do{
			email = UserCommunicator.insertString("Inserisci Email");
			if (emailValidator(email)) u.insertEmail(email);
		}while(!emailValidator(email));
		u.getCurriculum().getCompetenze().addAll(UserCommunicator.selectMultipleElementsS(SystemUtilities.getInstance().getCompetenze(), "Seleziona Competenze"));
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
		String email;
		do{
			email = UserCommunicator.insertString("Inserisci Email");
			if (emailValidator(email)) u.insertEmail(email);
		}while(!emailValidator(email));
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
	private static boolean emailValidator(String email) {
		String regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";

		Pattern pattern = Pattern.compile(regex);

		Matcher matcher = pattern.matcher(email);

		if(matcher.matches()) {
			return true;
		}
		
		return false;
	}
	
}
