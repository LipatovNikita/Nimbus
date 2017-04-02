package Beans;

import DAO.ComponentDAO;
import DAO.InformationSystemDAO;
import DAO.VirtualServerDAO;
import Entities.Component;
import Entities.InformationSystem;
import Entities.VirtualServer;
import exception.LoadComponentException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionScoped
@ManagedBean(name = "ComponentManage")
public class ComponentBean {
    private List<Component> components = new ArrayList<>();
    private Component component = new Component();
    private Map<Integer, String> selectMapInfoSyst = new HashMap<>();
    private Map<Integer, String> selectMapVirtServ = new HashMap<>();

    private boolean veryLoad = false;

    //Getter and Setter
    public Component getComponent() {
        return component;
    }
    public void setComponent(Component component) {
        this.component = component;
    }

    public boolean getVeryLoad() {
        return veryLoad;
    }
    public void setVeryLoad(boolean veryLoad) {
        this.veryLoad = veryLoad;
    }

    public List<Component> getComponents() {
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            components = componentDAO.selectComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return components;
    }

    public Map<Integer, String> getSelectMapVirtServ() {
        VirtualServerDAO virtServDAO = new VirtualServerDAO();
        ComponentDAO componentDAO = new ComponentDAO();
        List<VirtualServer> virtServList = null;
        try {
            virtServList = virtServDAO.selectVirtualServers();
            if(virtServList.isEmpty()){
                selectMapVirtServ.put(null,"Необходимо сначала создать виртуальный сервер");
            }
            else{
                for (VirtualServer vs: virtServList) {
                    int load = componentDAO.selectLoadComponents(vs.getId());
                    selectMapVirtServ.put(vs.getId(),"ID-"+vs.getId()+", "+vs.getNameVM()+", "+load+"%-загрузженности");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return selectMapVirtServ;
    }

    public Map<Integer, String> getSelectMapInfoSyst() {
        InformationSystemDAO infoSystDAO = new InformationSystemDAO();
        try {
            List<InformationSystem> infoSystList = infoSystDAO.getAll();
            if(infoSystList.isEmpty()){
                selectMapInfoSyst.put(null,"Необходимо сначала создать информационную систему");
            }
            else{
                for (InformationSystem is: infoSystList) {
                    selectMapInfoSyst.put(is.getIdsystem(),"ID-"+is.getIdsystem()+", "+is.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return selectMapInfoSyst;
    }



    //Component Controller Methods
    public String allComponents(){
        return "allComponents";
    }

    //НАПИСАТЬ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public String detail(int idComponent){
        for(Component c: components){
            if(c.getId()==idComponent){
                component = c;
                break;
            }
        }
        return "detailС";
    }

    public boolean isVeryLoad(int idVirtualServer) {
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            int load = componentDAO.selectLoadComponents(idVirtualServer);
            if(load == 100){
                veryLoad = true;
            }
            else{
                veryLoad = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return veryLoad;
    }

    public String goToAddComponent(){
        component = new Component();
        return "addComponentVS";
    }

    public String goToAddComponentToVirtServer(int idVirtualServer){
        component = new Component();
        component.setIdVirtualServer(idVirtualServer);
        return "addComponentVS";
    }

    public String goToAddComponentToInfoSystem(int idInfoSystem){
        component = new Component();
        component.setIdInformSystem(idInfoSystem);
        return "addComponentIS";
    }

    public String addComponentToVirtServer(){
        addComp();
        return new ServerManageBean().detailVirtualServer(component.getIdVirtualServer());
//        return "allComponents";
    }

    public String addComponentToInfoSystem(){
        addComp();
        return new InformationSystemBean().selectInfoSystem(component.getIdInformSystem());
//        return "allComponents";
    }

    private void addComp(){
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            int load = componentDAO.selectLoadComponents(component.getIdVirtualServer());
            if((load + component.getLoad()) <= 100){
                componentDAO.insertComponent(component);
            }
            else{
                throw new LoadComponentException(load);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Component> selectComponentsForVS(int idVirtualServer) {
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            components = componentDAO.selectComponentsForVirServ(idVirtualServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return components;
    }

    public List<Component> selectComponentsForIS(int idInfoSystem) {
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            components = componentDAO.selectComponentsForInfoSystem(idInfoSystem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return components;
    }


    public String deleteComponent(int idComponent) {
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            componentDAO.deleteComponent(idComponent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "allComponents";
    }

    public String goToUpdateComponent(int idComponent) {
        for (Component comp: components) {
            if(comp.getId()==idComponent){
                component = comp;
                break;
            }
        }
        return "updateComponent";
    }

    public String updateComponent() {
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            int load = componentDAO.selectLoadComponents(component.getIdVirtualServer());
            if((load + component.getLoad()) <= 100){
                componentDAO.updateComponent(component);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Developer")) {
            return "detailIS";
        }
        return "allComponents";
    }

    public String updateStatus(int idComponent){
        ComponentDAO componentDAO = new ComponentDAO();
        try {
            componentDAO.updateStatusComponent(idComponent, component.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Developer")) {
            return "detailIS";
        }
        return "allComponents";
    }
}

