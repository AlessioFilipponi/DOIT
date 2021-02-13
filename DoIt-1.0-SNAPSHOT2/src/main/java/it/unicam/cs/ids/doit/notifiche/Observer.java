package it.unicam.cs.ids.doit.notifiche;

public interface Observer {
    boolean update();
    void addNotifica(Subject s);
}
