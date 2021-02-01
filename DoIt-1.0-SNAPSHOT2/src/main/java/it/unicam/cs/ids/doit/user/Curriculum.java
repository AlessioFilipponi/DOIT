package it.unicam.cs.ids.doit.user;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.ids.doit.progetto.Progetto;

public class Curriculum {
	private Utente utente;
	private List<Progetto> progetti;
	private List<String> competenze;
	private String descrizione;
	
	public Curriculum(Utente u) {
		this.utente = u;
		progetti = new ArrayList<>();
		setCompetenze(new ArrayList<>());
		setDescrizione(new String());
	}

	public List<Progetto> getProgetti() {
		return progetti;
	}

	public List<String> getCompetenze() {
		return competenze;
	}

	public void setCompetenze(List<String> competenze) {
		this.competenze = competenze;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
