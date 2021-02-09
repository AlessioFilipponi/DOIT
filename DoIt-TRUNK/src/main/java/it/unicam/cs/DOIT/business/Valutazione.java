package it.unicam.cs.DOIT.business;

public class Valutazione {

	private Utente esperto;
	private int voto;
	private String commento;

	public Valutazione(Utente esperto, int voto, String commento) {
		this.esperto = esperto;
		this.voto = voto;
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