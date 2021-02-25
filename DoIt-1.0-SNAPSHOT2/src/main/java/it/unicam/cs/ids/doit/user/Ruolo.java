package it.unicam.cs.ids.doit.user;

import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
/**
 * Classe che usa il Pattern State per definire i vari comportamenti dell'Utente
 * a seconda del ruolo che ha.
 * 
 *
 */
public abstract class Ruolo{
	private Utente utente;

	/**
	 * 
	 * @param u Utente a cui associare il ruolo
	 */
	public Ruolo(Utente u) {
		this.utente = u;
	}
	/**
	 * Metodo che restituisce le competenze dell'Utente
	 * @return competenze dell'Utente
	 */
	abstract public Set<String> getCompetenze();
	
	/**
	 * Metodo che ritorna la lista dei progetti a cui l'utente si è candidato
	 * @return List		lista dei progetti 
	 */
	public List<Progetto> getPartecipazioni(){
		return Bacheca.getInstance().getCatalogoProgetti().search(p->p.getCandidati().contains(getUtente()));
	}
	
	/**
	 * Metodo che verifica se un utente è un esperto
	 * @return true se l'utente è un esperto, false altrimenti
	 */
	abstract public boolean isExpert();

	/**
	 * Metodo che verifica se un utente è un ente
	 * @return	true se l'utente è un ente, false altrimenti
	 */
	abstract public boolean isEnte();
	/**
	 * Metodo che ritorna il curriculum dell'utente
	 * @return Curriculum
	 */
	abstract public Curriculum getCurriculum();
	
	/**
	 * Metodo che ritorna l'utente a cui è associato il ruolo
	 * @return utente
	 */
	public Utente getUtente() {
		return utente;
	}
	/**
	 * Metodo per settare il nome dell'Utente
	 * @param nome nuovo nome
	 */
	public abstract void setName(String nome);
	
	/**
	 * Metodo che restituisce il nome dell'Utente
	 * @return nome dell'Utente
	 */
	public abstract String getName();
	
	/**
	 * Metodo che ritorna il numero intero associato al ruolo
	 * @return int valore intero associato al ruolo
	 */
	public abstract int getRoleNumber();
	
	/**
	 * Metodo che ritorna l'username dell'Utente a cui è associato il ruolo
	 * @return username dell'utente a cui è associato il ruolo
	 */
    public String getUsername() {
    	return utente.getUsername();
    }

}