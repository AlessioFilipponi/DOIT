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

public class IProgettista  implements UserInterface{

	Utente utente;

	public IProgettista(Utente u)
	{
		utente=u;
	}
	/**
	 *
	 * @param p
	 */
	public void richiediPartecipazione(Progetto p) {
//		if(p.getStato()== StatiProgetto.PENDING)//Se il progetto è in stato PENDING
//		{
//			Partecipazione part = new Partecipazione(getUtente(),p); //Creo una nuova partecipazione correlata all'utente e al progetto
//			p.getPartecipazioni().add(part); //La aggiungo al progetto
//			
//			UserCommunicator.print("Richiesta di partecipazione inviata"); //Poi notifico l'utente
//		}
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