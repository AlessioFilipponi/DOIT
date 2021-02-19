package it.unicam.cs.ids.doit.user;

import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;

public abstract class Ruolo{
	private Utente utente;

	public Ruolo(Utente u) {
		this.utente = u;
	}
	abstract public Set<String> getCompetenze();
	public List<Progetto> getPartecipazioni(){
		return Bacheca.getInstance().getCatalogoProgetti().search(p->p.getCandidati().contains(getUtente()));
	}
	abstract public boolean isExpert();

	abstract public boolean isEnte();
	abstract public Curriculum getCurriculum();
	
	public Utente getUtente() {
		return utente;
	}
	public abstract void setName(String nome);
	public abstract String getName();
	public abstract int getRoleNumber();
    public String getUsername() {
    	return utente.getUsername();
    }

}