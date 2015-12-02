package controller.stats;

import factory.DateIntervalFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import model.DateInterval;
import model.studenttimer.StudentTimer;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by will on 11/5/14.
 */
public class ChartController {

    @FXML
    private ComboBox<String> lineChartInterval;

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private DatePicker lineChartStartDate;

    @FXML
    private DatePicker lineChartEndDate;

    @FXML
    public void initialize() {
        bindLineChartControls();
        updateLineChart();
    }

    private void updateLineChart() {

        LocalDate s = lineChartStartDate.getValue();
        LocalDate e = lineChartEndDate.getValue();

        Date startDate = common.App.localDateToDate(s);
        Date endDate = common.App.localDateToDate(e);

        List<StudentTimer> st = StudentTimer.getStudentTimersInDateRange(startDate, endDate);

        updateLineChartData(st);
    }

    private void bindLineChartControls() {

        lineChartEndDate.setValue(LocalDate.now());
        lineChartStartDate.setValue(LocalDate.now().minusYears(1));

        lineChartStartDate.setOnAction(new EventHandler() {
            public void handle(Event t) {
                updateLineChart();
            }
        });
        lineChartEndDate.setOnAction(new EventHandler() {
            public void handle(Event t) {
                updateLineChart();
            }
        });

        lineChartInterval.setOnAction(new EventHandler() {
            public void handle(Event t) {
                updateLineChart();
            }
        });
    }

    private void updateLineChartData(List<StudentTimer> st) {

        ObservableList<XYChart.Series<String, Number>> lineChartData = FXCollections.observableArrayList();

        LineChart.Series<String, Number> timerSeries = new LineChart.Series<String, Number>();
        timerSeries.setName("Number of Tests");


        DateInterval dateInterval = DateIntervalFactory.getDateInterval(lineChartInterval.getValue());

        Map<Date, Integer> studentTimerCounts = StudentTimer.groupStudentTimers(st, dateInterval);

        try {

            for (Date key : studentTimerCounts.keySet()) {
                int value = studentTimerCounts.getOrDefault(key, 0);

                timerSeries.getData().add(new XYChart.Data<String, Number>(key.toString(), value));
            }

            lineChartData.add(timerSeries);
            lineChart.setData(lineChartData);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
