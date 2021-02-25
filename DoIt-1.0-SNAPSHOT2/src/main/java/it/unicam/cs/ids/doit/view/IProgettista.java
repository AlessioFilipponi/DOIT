package it.unicam.cs.ids.doit.view;

import java.sql.SQLException;

import it.unicam.cs.ids.doit.notifiche.Invito;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.FacadeProgetto;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;

/**
 * Classe per la gestione dell'interfaccia con l'utente con il ruolo di Progettista 
 *
 */
public class IProgettista  implements UserInterface{
	private Utente utente;

	/**
	 * Costruttore dell'IProgettista
	 * @param u utente
	 */
	public IProgettista(Utente u){
		utente=u;
	}
	/**
	 * Metodo per richiedere la partecipazione ad un progetto
	 * @param p	progetto selezionato
	 */
	public void richiediPartecipazione(Progetto p) {
		Partecipazione par = new FacadeProgetto(p).richiediPartecipazione(getUtente());
		if(par!=null) {
			UserCommunicator.print("Richiesta di partecipazione inviata");
			try {
				DBManager.getInstance().insertPartecipazione(par);
			} catch (SQLException e) {
				UserCommunicator.print(UserCommunicator.ERROR_INSERT);
			}
		}
		else
			UserCommunicator.print("Non puoi richiedere la partecipazione a questo progetto");
	}

	/**
	 * Metodo per valutare una proposta di partecipazione ad un progetto
	 * @param p	partecipazione
	 */
	public void valutaInvitoAProgetto(Partecipazione p){
		UserCommunicator.print("Progetto: "+p.getProgetto().getName());
		UserCommunicator.print("Specifiche: "+ p.getProgetto().getSpecifiche());
		if(UserCommunicator.select("Vuoi accettare questo invito?"))
			p.accetta();
		else
			p.rifiuta();
		getUtente().getNotifiche().remove(p);
		try {
			DBManager.getInstance().updatePartecipazione(p);
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}
	}
	/**
	 * Metodo per valutare un invito a collaborare con un Ente
	 * @param i	invito
	 */
	public void valutaInvitoAdEnte(Invito i){
		UserCommunicator.print(i.getEnte().getUsername());
		UserCommunicator.print(((Ente)(i.getEnte().getRole())).getDescrizione());
		if(UserCommunicator.select("Vuoi accettare questo invito?")) {
			i.accetta();
			Ente e =(Ente) i.getEnte().getRole();
			e.addCollaboratore(i.getProgettista());
		}
		else
			i.rifiuta();
		i.getProgettista().getNotifiche().remove(i);
		try {
			DBManager.getInstance().updateInvito(i);
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}
	}

	@Override
	public Utente getUtente() {
		return utente;
	}
}