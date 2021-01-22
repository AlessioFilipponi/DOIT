package it.unicam.cs.DOIT.view;

import it.unicam.cs.DOIT.business.Partecipazione;
import it.unicam.cs.DOIT.business.Progetto;
import it.unicam.cs.DOIT.business.StatiProgetto;
import it.unicam.cs.DOIT.business.Utente;
import it.unicam.cs.DOIT.UI.UserCommunicator;

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
			getUtente().getPartecipazioni().add(part); //E all'utente
			UserCommunicator.print("Richiesta di partecipazione inviata"); //Poi notifico l'utente
		}

	}

	@Override
	public Utente getUtente() {
		return utente;
	}
}