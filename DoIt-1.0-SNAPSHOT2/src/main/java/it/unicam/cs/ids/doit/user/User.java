package it.unicam.cs.ids.doit.user;

import java.util.Set;


public interface User {
	public String getName();
	public String getUsername();
	public Set<String> getCompetenze();
	public Curriculum getCurriculum();

}
