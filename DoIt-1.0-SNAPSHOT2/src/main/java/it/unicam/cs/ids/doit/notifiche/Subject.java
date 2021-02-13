package it.unicam.cs.ids.doit.notifiche;

import it.unicam.cs.ids.doit.ui.Named;

public interface Subject extends Named {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
    StatiRichieste getStato();
}
