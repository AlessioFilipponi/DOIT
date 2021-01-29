package it.unicam.cs.ids.doit.cataloghi;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Ruoli;
import it.unicam.cs.ids.doit.user.Utente;

public class Bacheca {
	private static Catalogo<Utente> catalogoUtenti;
	private static Catalogo<Progetto> catalogoProgetti;
	private static Bacheca instance;
	private static Catalogo<Esperto> catalogoEsperti;
	private static Catalogo<Progettista> catalogoProgettisti;
	private static Catalogo<Ente> catalogoEnti;
	
	private Bacheca() {
		Bacheca.catalogoProgetti = new Catalogo<Progetto>();
		Bacheca.catalogoUtenti = new Catalogo<Utente>();
		Bacheca.catalogoEsperti = new Catalogo<Esperto>();
		Bacheca.catalogoProgettisti = new Catalogo<Progettista>();
		Bacheca.catalogoEnti = new Catalogo<Ente>();
		
	}
	public static Bacheca getInstance() {
		if(instance ==null)
			instance = new Bacheca();
		return instance;
	}

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
	public static Collection<Esperto> getCatalogoEsperti(){
		return catalogoEsperti;
	}
	/**
	 *  @return catalogo dei Progettisti
	 * 
	 */

	public static Collection<Progettista> getCatalogoProgettisti(){
		return catalogoProgettisti;
	}
	/**
	 * Restituisce la lista dei progetti proposti da un utente
	 * @param userID l'id dell'utente di cui si vogliono ottenere i progetti
	 * @return lista dei progetti dell'utente
	 */
	public static Collection<Progetto> getListaMieiProgetti(String userID) {
		return catalogoProgetti.search(p->p.getCreatorID().equals(userID));
	}
	/** 
	 * @param l'oggetto utente
	 * @return la lista dei progetti dell'utente
	 */
	public static Collection<Progetto> getListaMieiProgetti(Utente u) {
		return catalogoProgetti.search(p->p.getProponente().equals(u));
	}
	
	/**
	 * @param collection cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public static Collection<Esperto> getEspertiCompetenti(String competenza){
		return Bacheca.catalogoEsperti.search(p-> p.getcompetenze().contains(competenza));
	}
	/**
	 * @param competenze cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public static Collection<Esperto> getEspertiCompetenti(Collection<String> competenze){
		return Bacheca.catalogoEsperti.search(p-> p.getcompetenze().containsAll(competenze));
	}
	/**
	 * @param competenze cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public static Collection<Progettista> getProgettistiCompetenti(String competenze){
		return Bacheca.catalogoProgettisti.search(p-> p.getcompetenze().contains(competenze));
	}
	public static Catalogo<Ente> getCatalogoEnti() {
		return catalogoEnti;
	}


}