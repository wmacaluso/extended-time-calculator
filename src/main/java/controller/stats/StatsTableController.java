package controller.stats;

import factory.DateIntervalFactory;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.studenttimer.StudentTimer;
import model.studenttimerstatistic.StudentTimerStatistic;
import model.studenttimerstatistic.StudentTimerStatisticCellCallback;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by will on 11/5/14.
 */
public class StatsTableController {

    final FileChooser fileChooser = new FileChooser();

    private static String INITIAL_FILENAME = "Exam Statistics.csv";

    @FXML
    private TableView<StudentTimerStatistic> studentTimerStatsTable;

    @FXML
    private DatePicker tableStartDate;

    @FXML
    private DatePicker tableEndDate;

    @FXML
    private ComboBox<String> tableInterval;

    @FXML
    private Button excelExport;

    @FXML
    public void initialize() {

        bindTableControls();
        updateTable();
    }

    private void updateTable(){

        Date startDate = common.App.localDateToDate(tableStartDate.getValue());
        Date endDate = common.App.localDateToDate(tableEndDate.getValue());
        String interval = tableInterval.getValue();

        List<StudentTimerStatistic> stats = getTableStats(startDate, endDate, interval);

        ObservableList<StudentTimerStatistic> data = studentTimerStatsTable.getItems();

        for(StudentTimerStatistic sts : stats){data.add(sts);}
    }

    private List<StudentTimerStatistic> getTableStats(Date startDate, Date endDate, String interval){

        List<StudentTimer> studentTimers = StudentTimer.getStudentTimersInDateRange(startDate, endDate);

        for(StudentTimer st : studentTimers){
            st.accommodations = st.getAccommodations();
        }

        return StudentTimerStatistic.groupByDateInterval(studentTimers, DateIntervalFactory.getDateInterval(interval));
    }

    private void tableToCsv(PrintWriter out){

        int numColumns = studentTimerStatsTable.getColumns().size();
        List<String> columnHeaders = new ArrayList<String>();

        for (int i = 0; i < numColumns; i++) {
            columnHeaders.add(studentTimerStatsTable.getColumns().get(i).getText());
        }

        out.println(StringUtils.join(columnHeaders, ", "));

        ObservableList<StudentTimerStatistic> rows = studentTimerStatsTable.getItems();

        for(StudentTimerStatistic studentTimerStatistic : rows){
            out.println(studentTimerStatistic.getCsv());
        }
    }

    private void saveTableToCsv(){

        try {

            fileChooser.setInitialFileName(INITIAL_FILENAME);
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CSV File", "*.csv")
            );

            File file = fileChooser.showOpenDialog(new Stage());

            PrintWriter out = new PrintWriter(file);
            tableToCsv(out);
            out.close();
        }
        catch(Exception ex){}
    }

    private void bindTableControls(){

        tableEndDate.setValue(LocalDate.now());
        tableStartDate.setValue(LocalDate.now().minusDays(7));

        tableStartDate.setOnAction(new EventHandler() {
            public void handle(Event t) {
                studentTimerStatsTable.getItems().clear();
                updateTable();
            }
        });

        tableEndDate.setOnAction(new EventHandler() {
            public void handle(Event t) {
                studentTimerStatsTable.getItems().clear();
                updateTable();
            }
        });
        tableInterval.setOnAction(new EventHandler() {
            public void handle(Event t) {
                studentTimerStatsTable.getItems().clear();
                setDefaultStartTime(tableInterval.getValue());
                //updateTable();
            }
        });

        excelExport.setOnAction(new EventHandler() {
            public void handle(Event t) {
                saveTableToCsv();
            }
        });

        int numColumns = studentTimerStatsTable.getColumns().size();

        for (int i = 0; i < numColumns; i++) {

            TableColumn<StudentTimerStatistic, String> s = (TableColumn<StudentTimerStatistic, String>) studentTimerStatsTable.getColumns().get(i);
            s.setCellFactory(new StudentTimerStatisticCellCallback());
        }
    }

    private void setDefaultStartTime(String value) {

        if(value.equals("Day")){
            tableStartDate.setValue(LocalDate.now().minusDays(7));
        }
        else if(value.equals("Week")){
            tableStartDate.setValue(LocalDate.now().minusWeeks(8));
        }
        else if(value.equals("Month")){
            tableStartDate.setValue(LocalDate.now().minusMonths(12));
        }
        else if(value.equals("Year")){
            tableStartDate.setValue(LocalDate.now().minusYears(5));
        }
    }
}
