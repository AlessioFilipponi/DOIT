package it.unicam.cs.ids.doit.notifiche;

public interface Observer<E>{
	E getObserver();
    boolean update();
    void addNotifica(Subject s);
}
