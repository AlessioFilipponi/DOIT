package it.unicam.cs.ids.doit.user;

import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.notifiche.Partecipazione;

public abstract class Ruolo {
	private Utente utente;

	public Ruolo(Utente u) {
		this.utente = u;
	}
	abstract public Set<String> getcompetenze();
	abstract public Set<Partecipazione> getPartecipazioni();
	abstract public boolean isExpert();
	abstract public void addToCatalogo();
	abstract public Set<Partecipazione> getListaPartecipazioni();
	
	Utente getUtente() {
		return utente;
	}


}