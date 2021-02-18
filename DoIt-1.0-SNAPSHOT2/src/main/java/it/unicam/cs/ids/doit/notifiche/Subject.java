package it.unicam.cs.ids.doit.notifiche;

import it.unicam.cs.ids.doit.ui.Named;

public interface Subject<E> extends Named {
    public void attach(Observer<E> o);
    public void detach(Observer<E> o);
    public void notifyObservers();
    public StatiRichieste getStato();
    public void setStato(StatiRichieste s);
}
