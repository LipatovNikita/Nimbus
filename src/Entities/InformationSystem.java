package Entities;


public class InformationSystem {
    private int idsystem;
    private int idSuperSystem;
    private String name;
    private int idstatus;
    private String picture;
    private String discription;
    private StatusInfo statusInfo;

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

    public InformationSystem(){}

    public InformationSystem(int idsystem){
        this.idsystem = idsystem;
    }
    public InformationSystem(int idSuperSystem, String name, int idstatus) {
        this.idSuperSystem = idSuperSystem;
        this.name = name;
        this.idstatus = idstatus;
    }
    public InformationSystem(int idsystem, int idSuperSystem, String name, int idstatus) {
        this.idsystem = idsystem;
        this.idSuperSystem = idSuperSystem;
        this.name = name;
        this.idstatus = idstatus;
    }
    public void setIdsystem(int idsystem) {
        this.idsystem = idsystem;
    }

    public void setIdSuperSystem(int idSuperSystem) {
        this.idSuperSystem = idSuperSystem;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIdstatus(int idstatus) {
        this.idstatus = idstatus;
    }

    public int getIdsystem() {
        return idsystem;
    }

    public int getIdSuperSystem() {
        return idSuperSystem;
    }

    public String getName() {
        return name;
    }

    public int getIdstatus() {
        return idstatus;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
