package Entities;


import java.sql.Timestamp;

public class StatusInfo {
    private int idstatus;
    private String status;
    private Timestamp date;
    private boolean published;
    private int idsystem;
    InformationSystem system = new InformationSystem();

    public InformationSystem getSystem() {
        return system;
    }

    public void setSystem(InformationSystem system) {
        this.system = system;
    }

    public StatusInfo(String status, Timestamp date, boolean published, int idsystem) {
        this.status = status;
        this.date = date;
        this.published = published;
        this.idsystem = idsystem;
    }
    public StatusInfo(){}
    public void setIdstatus(int idstatus) {
        this.idstatus = idstatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public void setIdsystem(int idsystem) {
        this.idsystem = idsystem;
    }

    public int getIdstatus() {
        return idstatus;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getDate() {
        return date;
    }

    public boolean getPublished() {
        return published;
    }

    public int getIdsystem() {
        return idsystem;
    }
}
