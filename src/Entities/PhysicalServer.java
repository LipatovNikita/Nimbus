package Entities;

import java.util.ArrayList;
import java.util.List;

public class PhysicalServer {
    private int id;
    private int HDD;
    private int RAM;
    private String status;
    private String processorName;
    private String IP;
    private String MAC;
    private List<VirtualServer> virtualServers = new ArrayList<VirtualServer>();

    public PhysicalServer() {
    }

    public PhysicalServer(int id, int HDD, int RAM, String status, String processorName, String IP, String MAC) {
        this.id = id;
        this.HDD = HDD;
        this.RAM = RAM;
        this.status = status;
        this.processorName = processorName;
        this.IP = IP;
        this.MAC = MAC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHDD() {
        return HDD;
    }

    public void setHDD(int HDD) {
        this.HDD = HDD;
    }

    public int getRAM() {
        return RAM;
    }

    public void setRAM(int RAM) {
        this.RAM = RAM;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public List<VirtualServer> getVirtualServers() {
        return virtualServers;
    }

    public void setVirtualServers(List<VirtualServer> virtualServers) {
        this.virtualServers = virtualServers;
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
