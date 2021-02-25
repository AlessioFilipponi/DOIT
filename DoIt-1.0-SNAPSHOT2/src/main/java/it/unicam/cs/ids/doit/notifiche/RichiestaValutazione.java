package it.unicam.cs.ids.doit.notifiche;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.user.Utente;

/**
 * La Richiesta Valutazione è una classe che rappresenta la richiesta da parte
 * di un Proponente Progetto ad un Esperto di valutare la fattibilità
 * di un determinato progetto.
 * 
 */
public class RichiestaValutazione implements  Subject<Utente>{
    private String motivazione;
    private Set<Observer<Utente>> destinatari;
    private Progetto progetto;
    private StatiRichieste stato;
	private Observer<Utente> esperto;
	private StatiProgetto oldState;
	
	/**
	 * 
	 * @param esperto 	selezionato 
	 * @param progetto 	su cui l'esperto effettua la valutazione
	 */
    public RichiestaValutazione(Observer<Utente> esperto, Progetto progetto) {
    	if(progetto.getStato()==StatiProgetto.IN_VALUTAZIONE)
            throw new IllegalStateException("Una valutazione per questo progetto è stata già richiesta!");
    	this.destinatari = new HashSet<>();
        this.progetto = progetto;
        this.esperto = esperto;
        attach(esperto);
        attach(progetto.getProponente());
        esperto.addNotifica(this);
        stato= StatiRichieste.IN_VALUTAZIONE;
        this.oldState=progetto.getStato();
        progetto.setStato(StatiProgetto.IN_VALUTAZIONE);
    }
    
    
    public void setStato(StatiRichieste inValutazione) {
        stato = inValutazione;
        notifyObservers();

    }

    @Override
    public void attach(Observer<Utente> o) {
        destinatari.add(o);
    }

    @Override
    public void detach(Observer<Utente> o) {
        destinatari.remove(o);
    }

    @Override
    public String getName() {
        return "Richiesta di valutazione del progetto "+progetto.getTitolo();
    }

    /**
     * 
     * @return 	progetto 
     */
    public Progetto getProgetto() {
        return progetto;
    }
    /**
     * Metodo per confermare che il progetto sia fattibile
     */

    public void conferma() {
        setStato(StatiRichieste.CONFERMATO);
        progetto.setStato(StatiProgetto.PENDING);
    }

    /**
     * Metodo che permette di rifiutare un progetto scrivendo la motivazione e cambiando
     * lo stato in archiviato
     * @param motivazione	string che rappresenta la motivazione del diniego
     */
    public void rifiuta(String motivazione){
        setStato(StatiRichieste.RIFIUTATO);
        this.motivazione = motivazione;
        progetto.setStato(StatiProgetto.ARCHIVIATO);
    }

    @Override
    public void notifyObservers() {
        for (Observer<Utente> observer : destinatari) {
            observer.update();
        }

    }
	@Override
	public StatiRichieste getStato() {
		return stato;
	}
	
	/**
	 * 
	 * @return esperto	che effettua la valutazione
	 */
	public Utente getEsperto() {
		return esperto.getObserver();
	}
	
	
}
