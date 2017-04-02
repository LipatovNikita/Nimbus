package Beans;

import DAO.InformationSystemDAO;
import DAO.StatusInfoDAO;
import DAO.UsersDAO;
import Entities.InformationSystem;
import Entities.StatusInfo;
import Entities.User;
import Services.ISStatusServiceInterface;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "statusBean")
@RequestScoped
public class StatusBean {
    StatusInfo statusInfo = new StatusInfo();
    List<StatusInfo> statusInfoList = new ArrayList<StatusInfo>();
    StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
    List<Integer> ids = new ArrayList<Integer>();
    String status;
    List<StatusInfo> lastWeekStatuses = new ArrayList<StatusInfo>();
    Logger logger = Logger.getLogger(StatusBean.class);

    public InformationSystemBean getInformationSystemBean() {
        return informationSystemBean;
    }

    public void setInformationSystemBean(InformationSystemBean informationSystemBean) {
        this.informationSystemBean = informationSystemBean;
    }

    @ManagedProperty(value = "#{informationSystemBean}")
    InformationSystemBean informationSystemBean;

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public void setStatusInfo(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

    public void setStatusList(List<StatusInfo> statusInfoList) {
        this.statusInfoList = statusInfoList;
    }

    public List<StatusInfo> getStatusList() {
        return statusInfoList;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void getSystemForAllStatus() {
        InformationSystemDAO systemDAO = new InformationSystemDAO();
        for (StatusInfo statusInfo : statusInfoList) {
            statusInfo.setSystem(systemDAO.getById(new InformationSystem(statusInfo.getIdsystem())));
        }
    }

    @PostConstruct
    public void setData() {
        try {
            setStatusList(statusInfoDAO.getAllStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        getSystemForAllStatus();
    }

    /*----------------*/
    public String statusPublished(int idsystem) {
        InformationSystemDAO systemDAO = new InformationSystemDAO();
        StatusInfo currentStatus = new StatusInfo();
        try {
            InformationSystem system = systemDAO.selectInformationSystemById(idsystem);
            currentStatus = statusInfoDAO.getStatusBySystem(system);
            if (!currentStatus.getPublished()) {
                currentStatus.setPublished(true);
                statusInfoDAO.updatePublished(currentStatus);
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return "detailIS";
    }


    public String addStatus() {
        statusInfo.setIdstatus(statusInfoDAO.insert(statusInfo));
        InformationSystemDAO systemDAO = new InformationSystemDAO();
        systemDAO.updateStatus(new InformationSystem(statusInfo.getIdsystem()), statusInfo);
        return "AllStatus";
    }

    public String addStatus(int idsystem) {
        statusInfo.setIdsystem(idsystem);
        try {
            InformationSystemDAO systemDAO = new InformationSystemDAO();
            InformationSystem system = systemDAO.selectInformationSystemById(idsystem);
            this.status = statusInfoDAO.getStatusBySystem(system).getStatus();
            if (!statusInfo.getStatus().equals(this.status)) {
                statusInfo.setIdstatus(statusInfoDAO.insert(statusInfo));
                systemDAO.updateStatus(new InformationSystem(statusInfo.getIdsystem()), statusInfo);
                informationSystemBean.informationSystem = systemDAO.selectInformationSystemById(statusInfo.getIdsystem());
            }
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
        }
        return "AllStatus";
    }

    public List<StatusInfo> getAllStatusByDev(String login) {
        UsersDAO usersDAO = new UsersDAO();
        User user = new User(login);
        try {
            user = usersDAO.getByLogin(user);
            setStatusList(statusInfoDAO.getAllStatusByDeveloper(user));
        } catch (SQLException | NamingException e) {
            logger.error("Ошибка извления " + user + "из бд");
            e.printStackTrace();
        }
        getSystemForAllStatus();
        return statusInfoList;
    }

    public List<StatusInfo> getAllStatusByAdmin() {
        try {
            setStatusList(statusInfoDAO.getAllStatus());
        } catch (SQLException | NamingException e) {
            logger.error("Ошибка извления статусов из БД для админа");
        }
        getSystemForAllStatus();
        return statusInfoList;
    }

    public List<String> getExistsStatusList() {
        List<String> statusList = statusInfoDAO.getExistsStatus();
        return statusList;
    }


    /*-----------------------------*/
    /* Работа с WebService */

    public Integer[] arrayIdsToService() {
        Integer[] ids = new Integer[0];
        try {
            ids = statusInfoDAO.selectIds().toArray(new Integer[statusInfoDAO.selectIds().size()]);
            return ids;
        } catch (SQLException | NamingException exception) {
            logger.error("Ошика извлечения идентификаторов систем из БД");
        }
        return ids;
    }

    public String[] getStatusesFromServices() {
        String[] statuses = new String[0];
        try {
            URL url = new URL("http://localhost:1488/ISStatusService?wsdl");
            QName qname = new QName("http://Services/", "ISStatusServiceService");
            Service service = Service.create(url, qname);
            ISStatusServiceInterface result = service.getPort(ISStatusServiceInterface.class);
            statuses = result.statusesToBean(arrayIdsToService());
        } catch (MalformedURLException exception) {
            logger.error("Неверное URL сервиса");
        }
        return statuses;
    }

    public String insertStatus() {
        try {
            String[] statusesFromService = getStatusesFromServices();
            java.sql.Timestamp date = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            Integer[] ids = statusInfoDAO.selectIds().toArray(new Integer[statusInfoDAO.selectIds().size()]);

            for (int i = 0; i < statusInfoDAO.selectIds().size(); i++) {
                for (i = 0; i < statusesFromService.length; i++) {
                    this.status = statusInfoDAO.sqlSelectLastRowById(ids[i]);
                    if (!(this.status.equals(statusesFromService[i]))) {
                        this.statusInfo = new StatusInfo(statusesFromService[i], date, false, statusInfoDAO.selectIds().get(i));
                        addStatus();
                    }
                }
            }
        } catch (SQLException | NamingException exception) {
            logger.error("Ошибка выполнения Insert'а статуса");
        }
        return "allstatuses";
    }

    /*--------------------------*/


    /*--------------------------*/
    /* Данные для таблицы статусов за последнюю неделю*/

    public List<StatusInfo> getStatusesBySystem(int id) {
        List<StatusInfo> transferStatuses = new ArrayList<StatusInfo>();
        try {
            StatusInfoDAO statusInfoDAO = new StatusInfoDAO();
            this.lastWeekStatuses = statusInfoDAO.selectLastDaysById(id);
            List<LocalDate> week = lastWeek();
            for (LocalDate day : week) {
                for (int i = 0; i < this.lastWeekStatuses.size(); i++) {
                    if ((day.compareTo(lastWeekStatuses.get(i).getDate().toLocalDateTime().toLocalDate()) == 0) &&
                            !(lastWeekStatuses.get(i).getStatus().equals("Active"))) {
                        transferStatuses.add(lastWeekStatuses.get(i));
                    }
                }
            }
        } catch (SQLException | NamingException exception) {
            logger.error("Ошибка извлечения статусов за посленюю неделю");
        }
        return transferStatuses;
    }

    public LocalDate convertToLocalDate(Timestamp date) {
        LocalDate localDate;
        localDate = date.toLocalDateTime().toLocalDate();
        return localDate;
    }

    public List<LocalDate> lastWeek() {
        List<LocalDate> days = new ArrayList<LocalDate>();
        LocalDate now = LocalDate.now();
        days.add(now);
        for (int i = 1; i <= 7; i++) {
            LocalDate dayago = now.minusDays(i);
            days.add(dayago);
        }
        Collections.reverse(days);
        return days;
    }
    /*-------------------------------*/
}
