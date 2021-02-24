package it.unicam.cs.ids.doit.notifiche;

import it.unicam.cs.ids.doit.ui.Named;

public interface Subject<E> extends Named {
	public static String ERROR_STATE = "Non è possibile tornare indietro dalla decisione presa";
   /**
    * Metodo per aggiungere un osservatore alla Subject
    * @param o Observer da aggiungere come osservatore per la Subject
    */
	
	public void attach(Observer<E> o);
	
	/**
	 * Metodo per rimuovere un osservatore dalla Subject
	 * @param o		Observer da rimuovere
	 */
    public void detach(Observer<E> o);
    
    /**
     * Metodo che chiama l'update nei vari observer
     */
    public void notifyObservers();
   
    /**
     * Metodo che ritorna lo stato della Subject
     * @return StatiRichieste 	stato della Subject
     */
    public StatiRichieste getStato();
    
    /**
     * Metodo per impostare uno stato della Subject
     * @param s		nuovo stato della Subject
     */
    public void setStato(StatiRichieste s);
}
