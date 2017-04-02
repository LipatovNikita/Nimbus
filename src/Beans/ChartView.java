package Beans;

import DAO.InformationSystemDAO;
import DAO.StatusInfoDAO;
import Entities.InformationSystem;
import Entities.StatusInfo;
import org.primefaces.model.chart.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@ManagedBean
@RequestScoped
public class ChartView {

    private LineChartModel dateModel;
    private List<InformationSystem> systemList;
    private StatusInfoDAO statusInfoDAO = new StatusInfoDAO();


    @PostConstruct
    public void init() {
        InformationSystemDAO systemDAO = new InformationSystemDAO();
        try {
            this.systemList = systemDAO.getAll();
        }catch(SQLException|NamingException ex){
            ex.printStackTrace();
        }
        createDateModel();
    }

    public LineChartModel getDateModel() {
        return dateModel;
    }

    public int getNumberStatus(String status) {
        switch (status) {
            case "Active":
                return 2;
            case "TechnicalWork":
                return 1;
            case "Down":
                return 0;
                default: return -1;
        }
    }

    private void createDateModel() {

        dateModel = new LineChartModel();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        for (InformationSystem system : systemList) {
            LineChartSeries systemTrend = new LineChartSeries();
            systemTrend.setLabel(system.getName());
            List<StatusInfo> statusInfoList = statusInfoDAO.getAllStatusBySystemWeek(system);

            for (int i = 0; i < statusInfoList.size(); i++) {
                systemTrend.set(statusInfoList.get(i).getDate().toString(), getNumberStatus(statusInfoList.get(i).getStatus().toString()));
                if (i + 1 != statusInfoList.size()) {
                    Timestamp later = new Timestamp(statusInfoList.get(i + 1).getDate().getTime() - 10);
                    systemTrend.set(later.toString(), getNumberStatus(statusInfoList.get(i).getStatus().toString()));
                } else {
                    systemTrend.set(timestamp.toString(), getNumberStatus(statusInfoList.get(i).getStatus().toString()));
                }
            }

            dateModel.addSeries(systemTrend);
        }
        dateModel.setTitle("График ИС");
        dateModel.getAxis(AxisType.Y).setLabel("Состояние");
        DateAxis xAxis = new DateAxis("Дата");
        Axis yAxis = dateModel.getAxis(AxisType.Y);
        yAxis.setTickAngle(-5);
        timestamp.setTime(timestamp.getTime()+ 1000*60*60*12);
        dateModel.setLegendPosition("e");
        dateModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        xAxis.setMax(timestamp.toString());
        //%H:%#M:%S
        xAxis.setTickFormat("%b %#d %y %H:%#M:%S");
        dateModel.setZoom(true);
        xAxis.setTickAngle(-50);
        dateModel.getAxes().put(AxisType.X, xAxis);
    }

}
