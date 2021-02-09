package it.unicam.cs.DOIT.business;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RichiestaValutazione implements  Subject{
    private String motivazione;
    private Set<Observer> destinatari; //devono essere sempre set
    private Progetto progetto;
    private statiRichiesta stato;

    public RichiestaValutazione(Utente esperto, Progetto progetto) {
        this.destinatari = new HashSet<>();
        this.progetto = progetto;
        esperto.getNotifiche().add(this);
        attach(esperto);
        attach(progetto.getProponente());
        stato = statiRichiesta.IN_VALUTAZIONE;
        
    }
   private void setStato(statiRichiesta inValutazione) {
		stato = inValutazione;
		reply();
		
	}
public  enum statiRichiesta  { RIFIUTATA, CONFERMATA, IN_VALUTAZIONE}

    @Override
    public void attach(Observer o) {
        destinatari.add(o);
    }

    @Override
    public void detach(Observer o) {
        destinatari.remove(o);
    }

    @Override
    public void reply() {
    	progetto.getProponente().addNotifica(this);
    	notifyObservers();
    }

    @Override
    public String getName() {
        return "Richiesta di valutazione del progetto "+progetto.getTitolo();
    }

    public Progetto getProgetto() {
        return progetto;
    }

    public void conferma() {
       setStato(statiRichiesta.CONFERMATA);
    }

    public void rifiuta(String motivazione){
        setStato(statiRichiesta.RIFIUTATA);
	this.motivazione = motivazione;
    }

	@Override
	public void notifyObservers() {
		for (Observer observer : destinatari) {
			observer.update();
		}
		
	}
}