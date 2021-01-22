package it.unicam.cs.DOIT.business;

import java.util.Set;

public class Partecipazione implements Subject {

	private Utente progettista;
	private Progetto progetto;
	private StatiPartecipazione stato;
	private Set<Observer> destinatari;

	/**
	 * Crea una nuova partecipazione tra un progettista e un progetto
	 * @param progettista il progettista
	 * @param progetto il progetto
	 */
	public Partecipazione(Utente progettista, Progetto progetto) {
		this.progettista=progettista;
		this.progetto=progetto;
		setStato(StatiPartecipazione.NON_CONFERMATO);
		attach(progettista);
		attach(progetto.getProponente());
		notifyObservers();
	}

	/**
	 * imposta lo stato alla partecipazione
	 * @param stato il nuovo stato
	 */
	private void setStato(StatiPartecipazione stato) {
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
	public StatiPartecipazione getStato(){
		return stato;
	}

	/**
	 * accetta
	 */
	public void accetta(){
		//Sia questo accetta che il "rifiuta" si possono eseguire solo quando la partecipazione si trova nello stato "NON CONFERMATO"
		if(stato==StatiPartecipazione.NON_CONFERMATO&&getStato()==StatiPartecipazione.NON_CONFERMATO)
		{
			setStato(StatiPartecipazione.CONFERMATO);//Modifica lo stato della partecipazione in "CONFERMATO"
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
		if(stato==StatiPartecipazione.NON_CONFERMATO)//stessa condizione iniziale di sopra
			setStato(StatiPartecipazione.RIFIUTATO); //Imposta lo stato della partecipazione a rifiutato
		else
			throw new IllegalStateException("Impossibile modificare la scelta!");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * Metodo che aggiunge un Observer alla notifica
	 * */

	@Override
	public void attach(Observer o) {
		destinatari.add(o);
		
	}

	/*
	 * Metodo che rimuove un observer alla notifica
	 * */
	@Override
	public void detach(Observer o) {
		destinatari.remove(o);
		
	}

	/* Manda una notifica in risposta
	 * */
	@Override
	public void reply() {
		notifyObservers();
		
	}

	/*
	 * Metodo che notifica il cambiamento di stato della Partecipazione a tutti i
	 * suoi Observer
	 * */
	@Override
	public void notifyObservers() {
		for (Observer observer : destinatari) {
			observer.update();
			observer.addNotifica(this);
		}
		
	}




	

}