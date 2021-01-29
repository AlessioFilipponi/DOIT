package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.notifiche.Partecipazione;

public class Progettista extends Ruolo{
    private Set<String> competenze;
    private Set<Partecipazione> partecipazioni;
     
    public Progettista(Utente u) {
    	super(u);
    	this.competenze = new HashSet<>();
    	this.partecipazioni = new HashSet<>();
    }
    //va tolto

    public Ruoli getRole() {
        return Ruoli.PROGETTISTA;
    }

    @Override
    public Set<String> getcompetenze() {
        return competenze;
    }

    @Override
    public Set<Partecipazione> getPartecipazioni() {
        return partecipazioni;
    }

    @Override
    public boolean isExpert() {
        return false;
    }

	@Override
	public void addToCatalogo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Partecipazione> getListaPartecipazioni() {
		// TODO Auto-generated method stub
		return null;
	}
}
