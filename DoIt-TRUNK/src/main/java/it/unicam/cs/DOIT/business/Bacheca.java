package it.unicam.cs.DOIT.business;
import java.util.Collection;
import java.util.HashSet;

public class Bacheca {
	static Catalogo<Utente> catalogoUtenti=new Catalogo<>();
	static Catalogo<Progetto> catalogoProgetti=new Catalogo<>();

	/**
	 * Restituisce il catalogo dei progetti
	 * @return catalogo progetti
	 */
	public static Catalogo<Progetto> getCatalogoProgetti()
	{
		return catalogoProgetti;
	}

	/**
	 * Restituisce il catalog dei progettisti
	 * @return catalogo progettisti
	 */
	public static Collection<Utente> getCatalogoUtenti()
	{
		return catalogoUtenti;
	}

	/**
	 * Restituisce il catalogo degli esperti
	 * @return catalogo Esperti
	 */
	public static Collection<Utente> getCatalogoEsperti(){
		Collection<Utente> esperti = new HashSet<>();
		for(Utente u:getCatalogoUtenti())
			if(u.getRole()== Ruoli.ESPERTO)
				esperti.add(u);
		return esperti;
	}

	/**
	 * Restituisce la lista dei progetti proposti da un utente
	 * @param userID l'id dell'utente di cui si vogliono ottenere i progetti
	 * @return lista dei progetti dell'utente
	 */
	public static Collection<Progetto> getListaMieiProgetti(String userID) {
		Collection<Progetto> myProjs=new HashSet<>();
		for(Progetto p : getCatalogoProgetti())
			if(p.getCreatorID().equals(userID))
				myProjs.add(p);
			return myProjs;
	}


}