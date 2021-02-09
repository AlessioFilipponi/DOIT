package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.Named;

public class Progettista extends Ruolo implements Named{
  
   
	private String nome;
	private String cognome;
	private Curriculum curriculum;
     
    public Progettista(Utente u) {
    	super(u);
    
    	this.curriculum = new Curriculum(u);
    	addToCatalogo();
    }


    @Override
    public Set<String> getcompetenze() {
        return curriculum.getCompetenze();
    }

    @Override
    public List<Progetto> getPartecipazioni() {
        return Bacheca.getInstance().getCatalogoProgetti().search(p-> p.getCandidati().contains(super.getUtente()));
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
	public boolean isEnte() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setName(String nome) {
		String[] n = null;
		try {n = nome.split(" ");}
		catch (Exception e) {
			this.nome = n[0];
			return;
		}
		this.nome = n[0];
		this.cognome = n[1];
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return nome + " " + cognome;
	}

	@Override
	public Curriculum getCurriculum() {
		// TODO Auto-generated method stub
		return curriculum;
	}


	@Override
	public int getRuolo() {
		return 0;
	}





}
