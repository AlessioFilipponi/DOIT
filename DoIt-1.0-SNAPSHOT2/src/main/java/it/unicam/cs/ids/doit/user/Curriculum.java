package it.unicam.cs.ids.doit.user;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.progetto.Progetto;
/**
 * Curriculum è una classe riferita ad un Utente specifico che contiene le competenze
 * una descrizione delle esperienze lavorative passate. E' possibile ottenere anche
 * la lista dei progetti a cui si partecipa
 *
 */
public class Curriculum {
	private Utente utente;
	private Set<String> competenze;
	private String descrizione;
	
	/**
	 * 
	 * @param u		Utente 
	 */
	public Curriculum(Utente u) {
		this.utente = u;
		setCompetenze(new HashSet<>());
		setDescrizione(new String());
	}

	/**
	 * Metodo che ritorna la lista dei progetti in cui l'Utente è un partecipante
	 * @return List	lista dei progetti in cui l'Utente partecipa
	 */
	public List<Progetto> getProgetti() {
		return Bacheca.getInstance().getCatalogoProgetti().search(p-> p.getPartecipanti().contains(utente));
	}

	/**
	 * Metodo che ritorna le competenze
	 * @return	competenze
	 */
	public Set<String> getCompetenze() {
		return competenze;
	}

	/**
	 * Metodo per settare le competenze 
	 * @param competenze	Set insieme delle nuove competenze
	 */
	public void setCompetenze(Set<String> competenze) {
		this.competenze = competenze;
	}

	/**
	 * Metodo che ritorna la descrizione delle esperienze lavorative dell'utente
	 * @return descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Metodo per settare la descrizione delle esperienze lavorative dell'Utente
	 * @param descrizione	nuova descrizione
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
