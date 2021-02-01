package it.unicam.cs.ids.doit.notifiche;

public interface Observer {
    void update();
    void addNotifica(Subject s);
}
