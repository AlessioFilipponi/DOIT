package it.unicam.cs.ids.doit.notifiche;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Utente;
/**
 * Invito è la classe che rappresenta l'invito inviato da
 * un Utente con il Ruolo di Ente ad un Utente per iniziare
 * una collaborazione.
 * Se l'Invito viene accettato l'Utente viene aggiunto tra
 * i collaboratori dell'Ente
 * 
 * */


public class Invito implements Subject<Utente>
{
    public StatiRichieste stato;
    private Observer<Utente> ente;
    private Set<Observer<Utente>> destinatari;
    private Observer<Utente> progettista;




	public Invito(Observer<Utente> ente, Observer<Utente> progettista) {
        if (!ente.getObserver().getRole().isEnte()) throw new IllegalArgumentException("Non sei un Ente!");
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
    public void detach(Observer<Utente> o) {
        destinatari.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer<Utente> observer : destinatari) {
       
            observer.update();
        }
    }

    @Override
    public String getName() {
        return "Invito";
    }

    /**
     * @return l'Ente che ha inviato l'invito
     */
    public Utente getEnte() {
        return ente.getObserver();
    }
    /**
     * metodo che cambia lo stato della richiesta in CONFERMAto
     * e aggiunge l'Utente ai collaboratori dell'Ente e lo notifica
     * 
     */
    public void accetta(){
        if(stato== StatiRichieste.IN_VALUTAZIONE)
        {
            stato= StatiRichieste.CONFERMATO;
            ente.getObserver().getNotifiche().add(this);
            notifyObservers();
            ((Ente)(ente.getObserver().getRole())).addCollaboratore(progettista.getObserver());
        }
        else throw new IllegalStateException(ERROR_STATE);

    }
    
    public void rifiuta(){
        if(stato== StatiRichieste.IN_VALUTAZIONE)
        {
            stato= StatiRichieste.RIFIUTATO;
            ente.getObserver().getNotifiche().add(this);
        }
        else throw new IllegalStateException(ERROR_STATE);

    }
    public Utente getProgettista() {
		return progettista.getObserver();
	}

	@Override
	public StatiRichieste getStato() {
		return stato;
	}

	@Override
	public void setStato(StatiRichieste s) {
		stato = s;
		
	}

}
