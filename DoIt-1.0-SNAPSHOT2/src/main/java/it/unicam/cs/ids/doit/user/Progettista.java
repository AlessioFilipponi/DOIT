package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.Named;

public class Progettista extends Ruolo implements Named{
  
   
	private String nomeCognome;
	private Curriculum curriculum;
     
    public Progettista(Utente u) {
    	super(u);
    
    	this.curriculum = new Curriculum(u);
    	
    }


    @Override
    public Set<String> getCompetenze() {
        return curriculum.getCompetenze();
    }

//    @Override
//    public List<Progetto> getPartecipazioni() {
//        return Bacheca.getInstance().getCatalogoProgetti().search(p-> p.getCandidati().contains(super.getUtente()));
//    }

    @Override
    public boolean isExpert() {
        return false;
    }




	@Override
	public boolean isEnte() {
		return false;
	}

	@Override
	public void setName(String nome) {
		this.nomeCognome = nome;
		
	}

	@Override
	public String getName() {
		return nomeCognome;
	}

	@Override
	public Curriculum getCurriculum() {
		return curriculum;
	}


	@Override
	public int getRoleNumber() {
		return 0;
	}


	





}
