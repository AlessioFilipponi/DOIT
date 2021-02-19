package it.unicam.cs.ids.doit.notifiche;

public interface Observer<E>{
	public E getObserver();
    public boolean update();
    public void addNotifica(Subject<E> s);


}
