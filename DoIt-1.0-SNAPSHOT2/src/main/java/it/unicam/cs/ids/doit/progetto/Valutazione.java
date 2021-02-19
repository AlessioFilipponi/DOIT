package it.unicam.cs.ids.doit.progetto;

import it.unicam.cs.ids.doit.user.Utente;

public class Valutazione {

	private Utente esperto;
	private int voto;
	private String commento;

	public Valutazione(Utente esperto, int voto, String commento) {
		if(esperto==null || commento==null)
			throw new NullPointerException("Non è possibile aggiungere campi null");
		this.esperto = esperto;
		if(voto>=1&&voto<=5)
			this.voto = voto;
		else throw new IllegalArgumentException("Il voto deve essere compreso tra 1 e 5");
		this.commento = commento;
	}

	public Utente getEsperto() {
		return esperto;
	}

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