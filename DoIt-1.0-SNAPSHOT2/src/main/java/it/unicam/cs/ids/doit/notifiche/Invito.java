package it.unicam.cs.ids.doit.notifiche;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Utente;

public class Invito implements Subject<Utente>
{
    public StatiRichieste stato;
    private Observer<Utente> ente;
    private Set<Observer<Utente>> destinatari;
    private Observer<Utente> progettista;




	public Invito(Observer<Utente> ente,Observer<Utente> progettista) {
        this.destinatari = new HashSet<Observer<Utente>>();
        this.ente=ente;
        stato= StatiRichieste.IN_VALUTAZIONE;
        this.progettista=progettista;
        attach(progettista);
        attach(ente);
        notifyObservers();
        progettista.getObserver().getNotifiche().add(this);
    }

    @Override
    public void attach(Observer<Utente> o) {
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

    @Override
    public String getName() {
        return "Invito";
    }

    public Utente getEnte() {
        return ente.getObserver();
    }

    public void accetta(){
        if(stato== StatiRichieste.IN_VALUTAZIONE)
        {
            stato= StatiRichieste.CONFERMATO;
            ente.getObserver().getNotifiche().add(this);
            notifyObservers();
            ((Ente)(ente.getObserver().getRole())).addCollaboratore(progettista.getObserver());
        }

    }
    public void rifiuta(){
        if(stato== StatiRichieste.IN_VALUTAZIONE)
        {
            stato= StatiRichieste.RIFIUTATO;
            ente.getObserver().getNotifiche().add(this);
        }

    }
    public Utente getProgettista() {
		return progettista.getObserver();
	}

	@Override
	public StatiRichieste getStato() {
		// TODO Auto-generated method stub
		return stato;
	}

	@Override
	public void setStato(StatiRichieste s) {
		stato = s;
		
	}

}
