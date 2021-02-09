package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.ui.Named;

public class Esperto extends Ruolo implements Named{
    private Set<String> competenze;
    private Set<Partecipazione> partecipazioni;
    private Utente utente;
	private String nome;

    public Esperto(Utente u) {
        super(u);
        this.competenze = new HashSet<>();
        this.partecipazioni = new HashSet<>();
    }



    @Override
    public Set<String> getcompetenze() {
        return competenze;
    }


    @Override
    public boolean isExpert() {
        return true;
    }

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return utente.getName();
	}
	
	public Utente getUtente() {
		return utente;
	}

	@Override
	public void addToCatalogo() {
		Bacheca.getInstance().getCatalogoEsperti().add(this);
		
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
	public Curriculum getCurriculum() {
		// TODO Auto-generated method stub
		return null;
	}
}
