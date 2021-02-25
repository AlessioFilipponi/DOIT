package it.unicam.cs.ids.doit.notifiche;

public interface Observer<E>{
	/**
	 * Metodo per ottenere il tipo di Observer
	 * @return E
	 */
	public E getObserver();
	
	/**
	 * Metodo che viene chiamato dalla Subject per notificare il cambio di stato
	 * @return true se lo stato è cambiato, false altrimenti
	 */
    public boolean update();
    
    /**
     * Metodo per aggiungere una Subject 
     * @param s		notifica da aggiungere
     */
    public void addNotifica(Subject<E> s);


}
