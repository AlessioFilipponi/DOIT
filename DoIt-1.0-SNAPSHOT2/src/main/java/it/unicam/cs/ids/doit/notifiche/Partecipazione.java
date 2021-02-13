package it.unicam.cs.ids.doit.notifiche;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.user.Utente;

public class Partecipazione implements Subject{

	private Utente progettista;
	private Progetto progetto;
	private StatiRichieste stato;
	private Set<Observer> destinatari;

	/**
	 * Crea una nuova partecipazione tra un progettista e un progetto
	 * @param progettista il progettista
	 * @param progetto il progetto
	 */
	public Partecipazione(Utente progettista, Progetto progetto) {
		if (progetto.getStato() != StatiProgetto.PENDING) throw new IllegalArgumentException("Non puoi partecipare a questo progetto");
		this.progettista=progettista;
		this.progetto=progetto;
		destinatari = new HashSet<Observer>();
		progetto.getPartecipazioni().add(this);
		attach(progettista);
		attach(progetto.getProponente());
//		setStato(StatiRichieste.IN_VALUTAZIONE);
	}

	/**
	 * imposta lo stato alla partecipazione
	 * @param stato il nuovo stato
	 */
	public void setStato(StatiRichieste stato) {
		this.stato = stato;
		notifyObservers();
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
		if(stato== StatiRichieste.IN_VALUTAZIONE&&getStato()== StatiRichieste.IN_VALUTAZIONE)
		{
			setStato(StatiRichieste.CONFERMATO);//Modifica lo stato della partecipazione in "CONFERMATO"
			if(progetto.getPartecipanti().size()>=progetto.getNumPartecipanti()) { //Poi controlla tutte le partecipazioni confermate nel progetto
				progetto.setStato(StatiProgetto.PUBBLICATO);/*Se ce ne sono tante quante i posti disponibili, il progetto passa nello stato "CONFERMATO"
				e da questo momento sarà impossibile rihiedere una partecipazione o inviare proposte*/
				progettista.getCurriculum().getProgetti().add(progetto);
				progetto.getPartecipanti().add(progettista);
			}
		}
		else
			throw new IllegalStateException("Impossibile modificare la scelta!");

	}

	/**
	 * rifiuta
	 */
	public void rifiuta() {
		if(stato== StatiRichieste.IN_VALUTAZIONE)//stessa condizione iniziale di sopra
			setStato(StatiRichieste.RIFIUTATO); //Imposta lo stato della partecipazione a rifiutato
		else
			throw new IllegalStateException("Impossibile modificare la scelta!");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "partecipazione";
	}

	@Override
	public void attach(Observer o) {
		destinatari.add(o);
		
	}

	@Override
	public void detach(Observer o) {
		destinatari.remove(o);
		
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : destinatari) {
			observer.update();
			}
		}
		
	

}