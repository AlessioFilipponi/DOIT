package it.unicam.cs.ids.doit.user;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.progetto.Progetto;

public class Curriculum {
	private Utente utente;
	private Set<String> competenze;
	private String descrizione;
	
	public Curriculum(Utente u) {
		this.utente = u;
		setCompetenze(new HashSet<>());
		setDescrizione(new String());
	}

	public List<Progetto> getProgetti() {
		return Bacheca.getInstance().getCatalogoProgetti().search(p-> p.getPartecipanti().contains(utente));
	}

	public Set<String> getCompetenze() {
		return competenze;
	}

	public void setCompetenze(Set<String> competenze) {
		this.competenze = competenze;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

}
