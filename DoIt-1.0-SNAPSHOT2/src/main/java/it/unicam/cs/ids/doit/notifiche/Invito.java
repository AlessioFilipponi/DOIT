package it.unicam.cs.ids.doit.notifiche;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Utente;

public class Invito implements Subject
{
    StatiRichieste stato;
    private Utente ente;
    private Set<Observer> destinatari;
    private Utente progettista;

    public Invito(Utente ente,Utente progettista) {
        this.destinatari = new HashSet<Observer>();
        this.ente=ente;
        stato= StatiRichieste.NON_CONFERMATO;
        this.progettista=progettista;
        attach(progettista);
        attach(ente);
        notifyObservers();
        progettista.getNotifiche().add(this);
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

    @Override
    public String getName() {
        return "Invito a: " + ente.getName();
    }

    public Utente getEnte() {
        return ente;
    }

    public void accetta(){
        if(stato== StatiRichieste.NON_CONFERMATO)
        {
            stato= StatiRichieste.CONFERMATO;
            ente.getNotifiche().add(this);
            progettista.getNotifiche().remove(this);
            notifyObservers();
            ((Ente)(ente.getRole())).addCollaboratore(progettista);
        }

    }
    public void rifiuta(){
        if(stato== StatiRichieste.NON_CONFERMATO)
        {
        	progettista.getNotifiche().remove(this);
            stato= StatiRichieste.RIFIUTATO;
            ente.getNotifiche().add(this);
        }

    }
}