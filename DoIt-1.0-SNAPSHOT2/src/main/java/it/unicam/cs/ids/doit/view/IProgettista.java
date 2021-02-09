package it.unicam.cs.ids.doit.view;

import it.unicam.cs.ids.doit.notifiche.Invito;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Utente;

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
		if(p.getStato()== StatiProgetto.PENDING)//Se il progetto è in stato PENDING
		{
			Partecipazione part = new Partecipazione(getUtente(),p); //Creo una nuova partecipazione correlata all'utente e al progetto
			p.getPartecipazioni().add(part); //La aggiungo al progetto
			
			UserCommunicator.print("Richiesta di partecipazione inviata"); //Poi notifico l'utente
		}
	}

	public void valutaInvitoAProgetto(Partecipazione p){
		UserCommunicator.print("Progetto: "+p.getProgetto().getName());
		UserCommunicator.print("Specifiche: "+ p.getProgetto().getSpecifiche());
		if(UserCommunicator.select("Vuoi accettare questo invito?"))
			p.accetta();
		else
			p.rifiuta();
	}

	public void valutaInvitoAdEnte(Invito i){
		UserCommunicator.print(i.getEnte().getUsername());
		UserCommunicator.print(((Ente)(i.getEnte().getRole())).getDescrizione());
		if(UserCommunicator.select("Vuoi accettare questo invito?"))
			i.accetta();
		else
			i.rifiuta();
	}

	@Override
	public Utente getUtente() {
		return utente;
	}
}