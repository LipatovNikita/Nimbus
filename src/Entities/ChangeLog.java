package Entities;


import java.sql.Timestamp;

public class ChangeLog {
    private int idlog;
    private int idauthor;
    private int idsystem;
    private String changes;
    private Timestamp datetime;
    private User user;

    public void setIdlog(int idlog) {
        this.idlog = idlog;
    }

    public void setIdauthor(int idauthor) {
        this.idauthor = idauthor;
    }

    public void setIdsystem(int idsystem) {
        this.idsystem = idsystem;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public int getIdlog() {
        return idlog;
    }

    public int getIdauthor() {
        return idauthor;
    }

    public int getIdsystem() {
        return idsystem;
    }

    public String getChanges() {
        return changes;
    }

    public Timestamp getDatetime() {
        return datetime;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
