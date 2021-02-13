package it.unicam.cs.ids.doit.notifiche;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.user.Utente;

public class RichiestaValutazione implements  Subject{
    private String motivazione;
    private Set<Observer> destinatari;
    private Progetto progetto;
    private StatiRichieste stato;
	private Utente esperto;

    public RichiestaValutazione(Utente esperto, Progetto progetto) {
        this.destinatari = new HashSet<>();
        this.progetto = progetto;
        this.esperto = esperto;
        attach(esperto);
        attach(progetto.getProponente());
        esperto.addNotifica(this);
        stato= StatiRichieste.IN_VALUTAZIONE;

    }
    private void setStato(StatiRichieste inValutazione) {
        stato = inValutazione;
        notifyObservers();

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
    public String getName() {
        return "Richiesta di valutazione del progetto "+progetto.getTitolo();
    }

    public Progetto getProgetto() {
        return progetto;
    }

    public void conferma() {
        setStato(StatiRichieste.CONFERMATO);
    }

    public void rifiuta(String motivazione){
        setStato(StatiRichieste.RIFIUTATO);
        this.motivazione = motivazione;
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : destinatari) {
            observer.update();
        }

    }
	@Override
	public StatiRichieste getStato() {
		// TODO Auto-generated method stub
		return stato;
	}
	
	public Utente getEsperto() {
		return esperto;
	}
}
