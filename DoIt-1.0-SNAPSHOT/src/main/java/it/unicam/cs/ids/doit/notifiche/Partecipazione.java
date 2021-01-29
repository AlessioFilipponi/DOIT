package it.unicam.cs.ids.doit.notifiche;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.user.Utente;

public class Partecipazione {

	private Utente progettista;
	private Progetto progetto;
	private StatiRichieste stato;

	/**
	 * Crea una nuova partecipazione tra un progettista e un progetto
	 * @param progettista il progettista
	 * @param progetto il progetto
	 */
	public Partecipazione(Utente progettista, Progetto progetto) {
		this.progettista=progettista;
		this.progetto=progetto;
		setStato(StatiRichieste.NON_CONFERMATO);
	}

	/**
	 * imposta lo stato alla partecipazione
	 * @param stato il nuovo stato
	 */
	private void setStato(StatiRichieste stato) {
		this.stato = stato;
	}

	/**
	 * Restituisce il progettista
	 * @return progettista
	 */
	public Utente getProgettista() {
		return progettista;
	}

	/**
	 * restituisce il progetto
	 * @return progetto
	 */
	public Progetto getProgetto()
	{
		return progetto;
	}

	/**
	 * Restituisce lo stato
	 * @return stato
	 */
	public StatiRichieste getStato(){
		return stato;
	}

	/**
	 * accetta
	 */
	public void accetta(){
		//Sia questo accetta che il "rifiuta" si possono eseguire solo quando la partecipazione si trova nello stato "NON CONFERMATO"
		if(stato== StatiRichieste.NON_CONFERMATO&&getStato()== StatiRichieste.NON_CONFERMATO)
		{
			setStato(StatiRichieste.CONFERMATO);//Modifica lo stato della partecipazione in "CONFERMATO"
			if(progetto.getPartecipanti().size()>=progetto.getNumPartecipanti()) //Poi controlla tutte le partecipazioni confermate nel progetto
				progetto.setStato(StatiProgetto.CONFERMATO);/*Se ce ne sono tante quante i posti disponibili, il progetto passa nello stato "CONFERMATO"
				e da questo momento sarà impossibile rihiedere una partecipazione o inviare proposte*/

		}
		else
			throw new IllegalStateException("Impossibile modificare la scelta!");

	}

	/**
	 * rifiuta
	 */
	public void rifiuta() {
		if(stato== StatiRichieste.NON_CONFERMATO)//stessa condizione iniziale di sopra
			setStato(StatiRichieste.RIFIUTATO); //Imposta lo stato della partecipazione a rifiutato
		else
			throw new IllegalStateException("Impossibile modificare la scelta!");
	}

}