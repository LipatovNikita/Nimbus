package Beans;

import DAO.ChangeLogDAO;
import Entities.ChangeLog;
import Entities.InformationSystem;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@RequestScoped
public class ChangeLogBean implements Serializable {

    Logger logger = Logger.getLogger(ChangeLog.class);

    public UserBean getuBean() {
        return uBean;
    }

    public void setuBean(UserBean uBean) {
        this.uBean = uBean;
    }

    @ManagedProperty(value = "#{userBean}")
    private UserBean uBean;

    ChangeLog changeLog = new ChangeLog();
    List<ChangeLog> changeLogList = new ArrayList<ChangeLog>();

    @PostConstruct
    public void setData() {
        ChangeLogDAO changeLogDAO = new ChangeLogDAO();
        setChangeLogList(changeLogDAO.getAll());
    }

    public List<ChangeLog> getChangeLogList() {
        return changeLogList;
    }

    public void setChangeLogList(List<ChangeLog> changeLogList) {
        this.changeLogList = changeLogList;
    }

    public ChangeLog getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(ChangeLog changeLog) {
        this.changeLog = changeLog;
    }

    public String addChange(String currentUser, InformationSystem system) {
        try {
            ChangeLogDAO changeLogDAO = new ChangeLogDAO();
            uBean.user.setLogin(currentUser);
            uBean.searchUserByLogin();
            changeLog.setIdauthor(uBean.user.getId());
            changeLog.setIdsystem(system.getIdsystem());
            changeLogDAO.addChangeLog(changeLog);
        }
        catch (SQLException | NamingException exception) {
            logger.error("Не удалось добавить запись в таблицу ChangeLog о системе: " + system.toString());
        }
        return "AllChanges";
    }

    public String addChange() {
        return "AddChange";
    }
}
