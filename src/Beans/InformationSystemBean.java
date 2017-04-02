package Beans;

import DAO.InformationSystemDAO;
import DAO.StatusInfoDAO;
import DAO.UsersDAO;
import Entities.InformationSystem;
import Entities.StatusInfo;
import Entities.User;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "informationSystemBean")
@SessionScoped
public class InformationSystemBean {
    private InformationSystem system = new InformationSystem(); // система с передачей объекта
    InformationSystem informationSystem = new InformationSystem(); // система по Id
    InformationSystemDAO systemDAO = new InformationSystemDAO();
    private StatusInfo currentStatusInfo = new StatusInfo();

    List<InformationSystem> systems = new ArrayList<InformationSystem>(); // список подсистем
    List<InformationSystem> systemList = new ArrayList<InformationSystem>();
    List<InformationSystem> superSystems = new ArrayList<InformationSystem>();
    List<InformationSystem> infoSystemList = new ArrayList<InformationSystem>();
    Logger logger = Logger.getLogger(InformationSystem.class);

    @PostConstruct
    public void setData() {
        try {
            setSystems(systemDAO.getAll());
            this.superSystems = systemDAO.selectSuperSystem();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @ManagedProperty(value = "#{uploadBean}")
    private UploadBean uploadBean;


    public UploadBean getUploadBean() {
        return uploadBean;
    }

    public void setUploadBean(UploadBean uploadBean) {
        this.uploadBean = uploadBean;
    }

    public String getDescription() {
        return system.getDiscription();
    }

    public InformationSystem getInformationSystem() {
        return informationSystem;
    }

    public void setInformationSystem(InformationSystem informationSystem) {
        this.informationSystem = informationSystem;
    }

    public StatusInfo getCurrentStatusInfo() {
        return currentStatusInfo;
    }

    public InformationSystem getSystem() {
        return system;
    }

    public List<InformationSystem> getSystems() {
        return systemList;
    }

    public void setSystems(List<InformationSystem> systemList) {
        this.systemList = systemList;
    }

    public InformationSystem getSystemById(InformationSystem system) {
        return systemDAO.getById(system);
    }

    public List<InformationSystem> getSuperSystems() {
        return superSystems;
    }

    public List<InformationSystem> getSystemsWithStatus(int id) {
        try {
            this.systems = systemDAO.selectSystemForTable(id);
        } catch (SQLException | NamingException exception) {
            logger.error("Ошибка извлечения систем для истории статусов");
        }
        return systems;
    }


    public String addSystem() {
        try {
            //если надсистема не выбрана, то idsystem=idsupersystem
            system.setIdsystem(systemDAO.insert(system));
            //заносим в текущий статус системы её id
            this.currentStatusInfo.setIdsystem(system.getIdsystem());
            StatusInfoDAO statusDAO = new StatusInfoDAO();
            //вставляем текуший статус в бд, заносим автоинкрементный id status в текуший статус currentStatusInfo
            this.currentStatusInfo.setIdstatus(statusDAO.insert(this.currentStatusInfo));
            //заносим id статуса в систему
            system.setIdstatus(this.currentStatusInfo.getIdstatus());
            if (system.getIdSuperSystem() == 0) {
                system.setIdSuperSystem(system.getIdsystem());
                systemDAO.update(system);
            }
            //обновляем данные о системе и её текущем статусе
            systemDAO.updateStatus(system, this.currentStatusInfo);
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.setAttribute("InfSystem", system);
        }
        catch (SQLException | NamingException exception) {
            logger.error("Ошибочка БД");
        }
        finally {
            return "AddSystem";
        }
    }

    public String addUnderSystem(int idsystem){
        system.setIdSuperSystem(idsystem);
        return "AddUnderSystem";
    }

    //Прописать на кнопку админу!
    public String addSystemAdmin(){
        this.system.setIdSuperSystem(0);
        return "AddUnderSystem";
    }

    public void insertPicture(FileUploadEvent event) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        InformationSystem informationSystem = (InformationSystem) session.getAttribute("InfSystem");
        informationSystem.setPicture(uploadBean.upload(event));
        systemDAO.addPicture(informationSystem);
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            context.redirect(context.getRequestContextPath() + "allfordev.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String deleteSystem(int idSystem) {
        try {
            systemDAO.delete(idSystem);
        } catch (SQLException | NamingException exception) {
            logger.error("Ошибка удаление ИС с ID: " + idSystem + " из БД");
            exception.printStackTrace();
        }
        return "AllSystems";
    }

    //взять суперсистему системы. не учитывает факт, что система мб самодостаточной
    public InformationSystem getSuperSystem(InformationSystem system) {
        InformationSystem supersystem = new InformationSystem(system.getIdSuperSystem());
        supersystem = systemDAO.getById(supersystem);
        return supersystem;
    }

    //взять текущий статус какой-то конкретной системы
    public StatusInfo getCurrentStatusInfoBySystem(InformationSystem system) throws SQLException {
        StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
        this.currentStatusInfo = statusInfoDAO.getStatusBySystem(system);
        return currentStatusInfo;
    }

    public InformationSystem getSystemById(StatusInfo statusInfo) {
        InformationSystem system = new InformationSystem(statusInfo.getIdsystem());
        system = systemDAO.getById(system);
        return system;
    }

    public List<InformationSystem> getInfoSystemList() {
        try {
            if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Admin")) {
                this.infoSystemList = systemDAO.getAll();
            } else if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("Developer")) {
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                User user = new User(request.getRemoteUser());
                UsersDAO usersDAO = new UsersDAO();
                user = usersDAO.getByLogin(user);
                this.infoSystemList = systemDAO.selectSystem(user.getId_supersystem());


            } else {
                this.infoSystemList = systemDAO.selectAllInfSyst();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoSystemList;
    }

    public String selectInfoSystem(int idInfoSystem) {
        if (idInfoSystem != 0) {
            for (InformationSystem is : infoSystemList) {
                if (idInfoSystem == is.getIdsystem()) {
                    informationSystem = is;
                    return "detailIS";
                }
            }
            try {
                informationSystem = systemDAO.selectInformationSystemById(idInfoSystem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "detailIS";
    }


    public List<InformationSystem> getAllSystemByDev(String login){
        UsersDAO usersDAO = new UsersDAO();
        User user = new User(login);
        user = usersDAO.getByLogin(user);
        this.system = new InformationSystem(user.getId_supersystem());
        try {
            this.systemList = systemDAO.getListSuperSystem(system);
        } catch (SQLException | NamingException e) {
           logger.error("Ошибка извлечения данных из бд");
        }
        return systemList;
    }
}
