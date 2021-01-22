package it.unicam.cs.DOIT.business;

public interface Subject extends Named{
    //TODO RICORDA CHE QUA C'è il notify di OBJECT
    void attach(Observer o);
    void detach(Observer o);
    void reply();
    void notifyObservers();
}
