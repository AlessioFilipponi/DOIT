package it.unicam.cs.ids.doit.utilities;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unicam.cs.ids.doit.user.Utente;

public class SystemUtilities {
	private Map<String,Utente> utenti;
	private Map<String,Integer> password;
	private static SystemUtilities instance;
	private Set<String> competenze;
	
	public void insertUtente(String username, String u) {
		password.put(username, u.hashCode());
	}
	
	public Utente getUtente(String username) {
		return utenti.get(username);
	}
	public boolean exist(String u) {
		return password.containsKey(u);
	}
	
	private SystemUtilities(){
		utenti = new HashMap<String, Utente>();
		password = new HashMap<String, Integer>();
		competenze = new HashSet<String>();
		competenze.add("Ingegnere");
		competenze.add("Informatico");
		competenze.add("Architetto");
		competenze.add("Sarta");
		
	}
	
	public static SystemUtilities getInstance(){
		if (instance==null)
			instance = new SystemUtilities();
		return instance;
	}

	public Map<String, Utente> getUtenti() {
		return utenti;
	}

	public Map<String, Integer> getPassword() {
		return password;
	}

	public Collection<String> getCompetenze() {
		return competenze;
	}
	
	

}
