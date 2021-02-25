package it.unicam.cs.ids.doit.progetto;

import it.unicam.cs.ids.doit.user.Utente;

/**
 * Classe che rappresenta la Valutazione effettuata da un Esperto su un Progetto
 * 
 *
 */
public class Valutazione {
	private Utente esperto;
	private int voto;
	private String commento;

	/**
	 * 
	 * @param esperto	Utente esperto che effettua la valutazione
	 * @param voto		Voto attribuito al Progetto
	 * @param commento	Commento sul progetto
	 */
	public Valutazione(Utente esperto, int voto, String commento) {
		if(esperto==null || commento==null)
			throw new NullPointerException("Non è possibile aggiungere campi null");
		this.esperto = esperto;
		if(voto>=1&&voto<=5)
			this.voto = voto;
		else throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 5");
		this.commento = commento;
	}

	/**
	 * Metodo che ritorna l'esperto che effettua la valutazione
	 * @return esperto
	 */
	public Utente getEsperto() {
		return esperto;
	}

	/**
	 * Metodo che restuisce il commento
	 * @return	commento
	 */
	public String getCommento() {
		return commento;
	}

	/**
	 * Restituisce il voto
	 * @return voto
	 */
	public int getVoto() {
		return voto;
	}
}