package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;

public class Progettista extends Ruolo{
    private Set<String> competenze;
    private Set<Partecipazione> partecipazioni;
	private String nome;
	private Curriculum curriculum;
     
    public Progettista(Utente u) {
    	super(u);
    	this.competenze = new HashSet<>();
    	this.partecipazioni = new HashSet<>();
    	this.curriculum = new Curriculum(u);
//    	addToCatalogo();
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
		Bacheca.getInstance().getCatalogoProgettisti().add(this);
		
	}

	@Override
	public Set<Partecipazione> getListaPartecipazioni() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnte() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setName(String nome) {
		this.nome = nome;
		
	}

	@Override
	protected String getNome() {
		// TODO Auto-generated method stub
		return nome;
	}

	@Override
	public Curriculum getCurriculum() {
		// TODO Auto-generated method stub
		return curriculum;
	}
}
