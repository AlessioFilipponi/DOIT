package it.unicam.cs.ids.doit.user;

import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;

public abstract class Ruolo {
	private Utente utente;

	public Ruolo(Utente u) {
		this.utente = u;
	}
	abstract public Set<String> getcompetenze();
	public List<Progetto> getPartecipazioni(){
		return Bacheca.getInstance().getCatalogoProgetti().search(p->p.getCandidati().contains(getUtente()));
	}
	abstract public boolean isExpert();
	abstract public void addToCatalogo();
	abstract public boolean isEnte();
	abstract public Curriculum getCurriculum();
	
	public Utente getUtente() {
		return utente;
	}
	protected abstract void setName(String nome);
	protected abstract String getName();
	public abstract int getRuolo();


}