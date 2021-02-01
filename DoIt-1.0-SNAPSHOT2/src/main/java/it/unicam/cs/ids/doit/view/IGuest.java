package it.unicam.cs.ids.doit.view;

import java.util.Collection;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;

public class IGuest {
	
	public static void VisualizzaProgetti() {

		Progetto p = selezionaProgetto(Bacheca.getInstance().getCatalogoProgetti());//Chiedo all'utente di selezionare un progetto tra tutti quelli presenti nel sistema
		if(p==null) //Se non ne ha selezionato nessuno o il progetto selezionato non esiste
			return; //Annullo la procedura
		visualizzaDettagliProgetto(p); //Altrimenti visualizzo i dettagli del progetto
	}

	public static Progetto selezionaProgetto(Collection<Progetto> projects){
		return UserCommunicator.selectElement(projects,"Seleziona un progetto");
	}
	/**
	 * 
	 * @param p
	 */
	public static void visualizzaDettagliProgetto(Progetto p) {
		UserCommunicator.print(p.toString());//stampa i dettagli del progetto
		
	}
	
	public static void Menu() {
		boolean flag = true;
		while (flag) {UserCommunicator.print("\n\n\n ****> MENU PRINCIPALE <****\n" +
				"1) Visualizza progetti\n" +
				"2) Effettua il Login\n"+
				"3) Registrazione\n" +
				"4) Esci");
//				"5) Logout");
			int selezione = UserCommunicator.insertInteger("La tua scelta");
			switch(selezione)
			{
				case 1 : VisualizzaProgetti();break;
				case 2 : InteractionManager.logIn();
				case 3 : InteractionManager.registrazione();
				case 4: flag = false;break;
//				case 5: main(args);flag=false;break;
			}
		}
		if (!flag) System.out.println("**BYE BYE**");
	}
	
}
