package it.unicam.cs.ids.doit.ui;

import java.util.*;

/**
 * Questa classe si occupa della comunicazione tra il programma e l'utente
 */
public class UserCommunicator {
    /**
     * Stampa un meszaggio per l'utente
     * @param message
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
        while (selezione!=0&&selezione!=1)
            selezione=UserCommunicator.insertInteger(message+"\n[1] Sì     [0] No\n La tua scelta");
        if(selezione==0)
            return false;
        return true;
    }

    /**
     * Chiede all'utente di inserire una lista di stringhe
     * @param message messaggio usato per richiedere l'input all'utente
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
     * @return input
     */
    public static Collection<Integer> insertIntegerList(String message, String promptRepeat){
        Collection<Integer> Ints = new HashSet<>();
        int Int=0;
        UserCommunicator.print("\n**"+message+"**\nInserire [-1] per terminare l'inserimento.");
        do{
            Int=UserCommunicator.insertInteger(promptRepeat);
            if(Int!=-1)
                Ints.add(Int);
        }while (Int>-1);
        return Ints;
    }

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
        while(rtrnv==null)
            rtrnv=els.get(insertInteger(message));
        return rtrnv;
    }

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
        UserCommunicator.print("Inserire -1 per terminare l'inserimento");
        while(insert>-1)
        {
            insert= UserCommunicator.insertInteger(message);
            if (insert!=-1 || insert>i) rtrnv.add(els.get(insert));
        }
        return rtrnv;
    }
    public static <String> Collection<String>selectMultipleElements(Collection<String> elements, String message){
        Map<Integer,String> els=new HashMap<>();
        int i=0;
        for(String el:elements)
        {
            els.put(i,el);
            print(i+") "+ el);
            i++;
        }
        Collection<String> rtrnv=new HashSet<>();
        int insert=0;
        UserCommunicator.print("Inserire -1 per terminare l'inserimento");
        while(insert>-1)
        {
            insert= UserCommunicator.insertInteger((java.lang.String) message);
            if (insert!=-1 || insert>i) rtrnv.add(els.get(insert));
        }
        return rtrnv;
    }
}
