package it.unicam.cs.ids.doit.ui;

import java.util.*;

/**
 * Questa classe si occupa della comunicazione tra il programma e l'utente
 */
public class UserCommunicator {
	public static String ERROR_INT_MESSAGE = "Puoi solo inserire un numero";
	public static String EXIT_MESSAGE = "Inserire -1 per terminare l'inserimento";
	public static String ERROR_INSERT = "Errore: inserimento non riuscito";
    /**
     * Stampa un meszaggio per l'utente
     * @param message messaggio da stampare
     */
    public static void print(String message)
    {
        System.out.println(message);
    }

    /**
     * Chiede all'utente di inserire una stringa
     * @param message messaggio usato per richiedere l'input all'utente
     * @return input
     */
    public static String insertString(String message){
        System.out.print(message+"> ");
        return new Scanner(System.in).nextLine();
    }

    /**
     * Chiede all'utente di inserire un intero
     * @param message messaggio usato per richiedere l'input all'utente
     * @return input
     */
    public static int insertInteger(String message)
    {
        System.out.print(message+"> ");
        return new Scanner(System.in).nextInt();
    }


    /**
     * Chiede all'utente di inserire un decimale
     * @param message messaggio usato per richiedere l'input all'utente
     * @return input
     */
    public static double insertDecimal(String message)
    {
        System.out.print(message+"> ");
        return new Scanner(System.in).nextDouble();
    }


    /**
     * Chiede all'utente di selezionare un'opzione
     * @param message messaggio usato per richiedere l'input all'utente
     * @return input
     */
    public static boolean select(String message)
    {
        int selezione=10;
        while (selezione!=0&&selezione!=1) {
        	try {
            selezione=UserCommunicator.insertInteger(message+"\n[1] Sì     [0] No\n La tua scelta");}
        	catch (Exception e) {
				UserCommunicator.print(ERROR_INT_MESSAGE);
			}
        }
        if(selezione==0)
            return false;
        return true;
    }

    /**
     * Chiede all'utente di inserire una lista di stringhe
     * @param message messaggio usato per richiedere l'input all'utente
     * @param promptRepeat promptRepeat
     * @return input
     */
    public static Collection<String> insertStringList(String message, String promptRepeat){
        Collection<String> strings = new HashSet<>();
        String string="";
        UserCommunicator.print("\n**"+message+"**\nPremere [INVIO] per terminare l'inserimento.");
        do{
            string=UserCommunicator.insertString(promptRepeat);
            if(!"".equals(string))
                strings.add(string);
        }while (!"".equals(string));
        return strings;
    }

    /**
     * Chiede all'utente di inserire una lista di interi
     * @param message messaggio usato per richiedere l'input all'utente
     * @param promptRepeat prompt repeat
     * @return input
     */
    public static Collection<Integer> insertIntegerList(String message, String promptRepeat){
        Collection<Integer> Ints = new HashSet<>();
        int Int=0;
        UserCommunicator.print("\n**"+message+"**\nInserire [-1] per terminare l'inserimento.");
        do{
            try{Int=UserCommunicator.insertInteger(promptRepeat);}
            catch (Exception e) {
				UserCommunicator.print(ERROR_INT_MESSAGE);
			}
            if(Int!=-1)
                Ints.add(Int);
        }while (Int>-1);
        return Ints;
    }

    /**
     * Metodo per selezionare un elemento da una Lista
     * @param <T> tipo
     * @param elements 	Collection collezione di elementi da cui selezionarne una
     * @param message	Messaggio stampato 
     * @return elemento selezionato
     */
    public static <T extends Named> T selectElement(Collection<T> elements, String message)
    {
        Map<Integer,T> els=new HashMap<>();
        int i=1;
        for(T el:elements)
        {
            els.put(i,el);
            print(i+") "+el.getName());
            i++;
        }
        T rtrnv=null;
        while(rtrnv==null) {
            try {rtrnv=els.get(insertInteger(message));}
            catch (Exception e) {
				UserCommunicator.print(ERROR_INT_MESSAGE);
			}}
        return rtrnv;
    }

    /**
     * Metodo per selezionare un gruppo di elementi da una Collection
     * @param <T> tipo
     * @param elements	Collection di elementi da cui selezionare degli elementi
     * @param message	Messaggio stampato all'utente
     * @return	Collection 	Collezione di elementi selezionati
     */
    public static <T extends Named> Collection<T>selectMultipleElements(Collection<T> elements, String message){
        Map<Integer,T> els=new HashMap<>();
        int i=0;
        for(T el:elements)
        {
            els.put(i,el);
            print(i+") "+el.getName());
            i++;
        }
        Collection<T> rtrnv=new HashSet<>();
        int insert=0;
        UserCommunicator.print(EXIT_MESSAGE);
        while(insert>-1)
        {
            try{insert= UserCommunicator.insertInteger(message);}
            catch (Exception e) {
            	if (insert==-1)break;
            	else UserCommunicator.print(ERROR_INT_MESSAGE);
			}
            if (insert!=-1 || insert>i) rtrnv.add(els.get(insert));
        }
        return rtrnv;
    }
    
    /**
     * Metodo per selezionare un gruppo di elementi da una Collection
     * @param <String> string
     * @param elements	Collection di elementi da cui selezionare degli elementi
     * @param message	Messaggio stampato all'utente
     * @return	Collection 	Collezione di elementi selezionati
     */
    
    public static <String> Set<String>selectMultipleElementsS(Collection<String> elements, String message){
        Map<Integer,String> els=new HashMap<>();
        int i=0;
        for(String el:elements)
        {
            els.put(i,el);
            print(i+") "+ el);
            i++;
        }
        Set<String> rtrnv=new HashSet<>();
        int insert=0;
        UserCommunicator.print(EXIT_MESSAGE);
        while(insert>-1)  {   
        	try {
        
            insert= UserCommunicator.insertInteger((java.lang.String) message);
        	}catch (Exception e) {
				UserCommunicator.print(ERROR_INT_MESSAGE);
			}
            if (insert!=-1 || insert>i) rtrnv.add(els.get(insert));
        }
        return rtrnv;
    }


	
}
