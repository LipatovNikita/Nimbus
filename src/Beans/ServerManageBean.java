package Beans;

import DAO.PhysicalServerDAO;
import DAO.VirtualServerDAO;
import Entities.PhysicalServer;
import Entities.VirtualServer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionScoped
@ManagedBean(name = "ServerManage")
public class ServerManageBean {
    private PhysicalServer physicalServer = new PhysicalServer();
    private VirtualServer virtualServer = new VirtualServer();

    private List<PhysicalServer> physicalServers = new ArrayList<>();
    private List<VirtualServer> virtualServers = new ArrayList<>();
    private Map<Integer, String> selectMapPhysServ = new HashMap<>();


    public PhysicalServer getPhysicalServer() {
        return physicalServer;
    }
    public void setPhysicalServer(PhysicalServer physicalServer) {
        this.physicalServer = physicalServer;
    }

    public VirtualServer getVirtualServer() {
        return virtualServer;
    }
    public void setVirtualServer(VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
    }

    public List<PhysicalServer> getPhysicalServers() {
        PhysicalServerDAO physServerDAO = new PhysicalServerDAO();
        try {
            physicalServers = physServerDAO.selectPhysicalServers();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return physicalServers;
    }

    public List<VirtualServer> getVirtualServers() {
        VirtualServerDAO virtualServerDAO = new VirtualServerDAO();
        try {
            virtualServers = virtualServerDAO.selectVirtualServers();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return virtualServers;
    }

    //Создаем список физ. серверов для Виртуального сервера!!!
    public Map<Integer, String> getSelectMapPhysServ() {
        PhysicalServerDAO physServerDAO = new PhysicalServerDAO();
        try {
            physicalServers = physServerDAO.selectPhysicalServers();
            if(physicalServers.isEmpty()){
                selectMapPhysServ.put(null,"Необходимо сначала создать физический сервер");
            }
            else{
                for (PhysicalServer ps: physicalServers) {
                    selectMapPhysServ.put(ps.getId(),"ID - "+ps.getId()+",IP - "+ps.getIP()+", MAC - "+ps.getMAC());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return selectMapPhysServ;
    }


    public String pServers(){
        return "allPS";
    }
    public String vServers(){
        return "allVS";
    }

    public String goToAddPhysicalServer(){
        physicalServer = new PhysicalServer();
        return "addPS";
    }
    public String goToAddVirtualServer(){
        virtualServer = new VirtualServer();
        return "addVS";
    }

    public String detailPhysicalServer(int idPhysicalServer){
        for (PhysicalServer p : physicalServers ) {
            if(p.getId() == idPhysicalServer){
                physicalServer = p;
                return "detailPS";
            }
        }

        PhysicalServerDAO physServDAO = new PhysicalServerDAO();
        try {
            physicalServer = physServDAO.selectPhysicalServerById(idPhysicalServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "detailPS";
    }

    public String detailVirtualServer(int idVirtualServer){
        for (VirtualServer v : virtualServers ) {
            if(v.getId() == idVirtualServer){
                virtualServer = v;
                return "detailVS";
            }
        }

        VirtualServerDAO virtServDAO = new VirtualServerDAO();
        try {
            virtualServer = virtServDAO.selectVirtualServerById(idVirtualServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "detailVS";
    }


    public List<VirtualServer> selectVirtServForPS(int idPhysicalServer) {
        VirtualServerDAO virtServDAO = new VirtualServerDAO();
        try {
            virtualServers = virtServDAO.selectVirtualServersForPhysServer(idPhysicalServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return virtualServers;
    }



    //Нужно обработать список ФИЗИЧЕСКИХ СЕРВЕРОВ
    public String addVirtualServer(){
        VirtualServerDAO virtualServerDAO = new VirtualServerDAO();
        try {
            virtualServerDAO.insertVirtualServer(virtualServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "allVS";
    }

    public String deleteVirtualServer(int idVirtualServer) {
        VirtualServerDAO virtualServerDAO = new VirtualServerDAO();
        try {
            virtualServerDAO.deleteVirtualServer(idVirtualServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "allVS";
    }

    public String updateVirtualServer() {
        VirtualServerDAO virtualServerDAO = new VirtualServerDAO();
        try {
            virtualServerDAO.updateVirtualServer(virtualServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "allVS";
    }

    public String goToUpdateVS(int idVirtualServer) {
        for (VirtualServer vs: virtualServers) {
            if(vs.getId() == idVirtualServer){
                virtualServer = vs;
                return "updateVS";
            }
        }

        VirtualServerDAO virtServDAO = new VirtualServerDAO();
        try {
            virtualServer = virtServDAO.selectVirtualServerById(idVirtualServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "updateVS";
    }




    public String addPhysicalServer(){
        PhysicalServerDAO physServerDAO = new PhysicalServerDAO();
        try {
            physServerDAO.insertPhysicalServer(physicalServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "allPS";
    }

    public String deletePhysicalServer(int idPhysicalServer) {
        System.out.println("Invoking delete " + this + "  " + idPhysicalServer);
        PhysicalServerDAO physServerDAO = new PhysicalServerDAO();
        try {
            physServerDAO.deletePhysicalServer(idPhysicalServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "allPS";
    }

    public String updatePhysicalServer() {
        PhysicalServerDAO physServerDAO = new PhysicalServerDAO();
        try {
            physServerDAO.updatePhysicalServer(physicalServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "allPS";
    }

    public String goToUpdatePS(int idPhysicalServer) {
        System.out.println("Invoking update " + this + "  " + idPhysicalServer);
        for (PhysicalServer ps: physicalServers) {
            if(ps.getId()==idPhysicalServer){
                physicalServer = ps;
                return "updatePS";
            }
        }

        PhysicalServerDAO physServDAO = new PhysicalServerDAO();
        try {
            physicalServer = physServDAO.selectPhysicalServerById(idPhysicalServer);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "updatePS";
    }

}
