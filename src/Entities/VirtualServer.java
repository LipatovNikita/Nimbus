package Entities;

import java.util.ArrayList;
import java.util.List;

public class VirtualServer {
    private int id;
    private int idPhysicalServer;
    private String nameVM;
    private String IP;
    private String Mask;
    private int port;

    private List<Component> components = new ArrayList<>();
    private PhysicalServer physicalServer = new PhysicalServer();


    public VirtualServer() {}

    public VirtualServer(int id, int idPhysicalServer, String nameVM, String IP, String mask, int port) {
        this.id = id;
        this.nameVM = nameVM;
        this.IP = IP;
        Mask = mask;
        this.port = port;
        this.idPhysicalServer = idPhysicalServer;
    }

    public int getIdPhysicalServer() {
        return idPhysicalServer;
    }

    public void setIdPhysicalServer(int idPhysicalServer) {
        this.idPhysicalServer = idPhysicalServer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameVM() {
        return nameVM;
    }

    public void setNameVM(String nameVM) {
        this.nameVM = nameVM;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMask() {
        return Mask;
    }

    public void setMask(String mask) {
        Mask = mask;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PhysicalServer getPhysicalServer() {
        return physicalServer;
    }

    public void setPhysicalServer(PhysicalServer physicalServer) {
        this.physicalServer = physicalServer;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
