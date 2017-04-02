package Entities;

public class Component {
    private int id;
    private int idInformSystem;
    private int idVirtualServer;
    private String name;
    private String status;
    private String version;
    private int load;
    private int port;
    private InformationSystem informationSystems = new InformationSystem();
    private VirtualServer virtualServers = new VirtualServer();

    public Component() {}

    public Component(int id, String name, String status, String version, int idInformSystem, int idVirtualServer, int load, int port) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.version = version;
        this.load = load;
        this.port = port;
        this.idInformSystem = idInformSystem;
        this.idVirtualServer = idVirtualServer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InformationSystem getInformationSystems() {
        return informationSystems;
    }

    public void setInformationSystems(InformationSystem informationSystems) {
        this.informationSystems = informationSystems;
    }

    public VirtualServer getVirtualServers() {
        return virtualServers;
    }

    public void setVirtualServers(VirtualServer virtualServers) {
        this.virtualServers = virtualServers;
    }

    public int getIdInformSystem() {
        return idInformSystem;
    }

    public void setIdInformSystem(int idInformSystem) {
        this.idInformSystem = idInformSystem;
    }

    public int getIdVirtualServer() {
        return idVirtualServer;
    }

    public void setIdVirtualServer(int idVirtualServer) {
        this.idVirtualServer = idVirtualServer;
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
