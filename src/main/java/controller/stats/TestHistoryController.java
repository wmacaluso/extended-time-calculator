package controller.stats;

import com.j256.ormlite.stmt.DeleteBuilder;
import db.sqlconn;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Accommodation;
import model.studenttimer.StudentTimer;
import model.studenttimer.StudentTimerCellCallback;
import model.studenttimer.StudentTimerRemoveCell;
import model.studenttimerhistory.StudentTimerHistoryCellCallback;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Will on 11/8/2014.
 */
public class TestHistoryController {

    final FileChooser fileChooser = new FileChooser();

    private static String INITIAL_FILENAME = "Test History.csv";

    @FXML
    private Button excelExport;

    @FXML
    private TableView<StudentTimer> StudentTimerTable;

    @FXML
    private DatePicker tableStartDate;

    @FXML
    private DatePicker tableEndDate;

    @FXML
    public void initialize() {

        bindTableControls();
        BindRemoveButtonColumn();
        updateTable();
    }

    private void updateTable(){

        Date startDate = common.App.localDateToDate(tableStartDate.getValue());
        Date endDate = common.App.localDateToDate(tableEndDate.getValue());

        List<StudentTimer> studentTimers = StudentTimer.getStudentTimersInDateRange(startDate, endDate);

        ObservableList<StudentTimer> tableData = StudentTimerTable.getItems();

        for(StudentTimer st : studentTimers){tableData.add(st);}
    }

    private void BindRemoveButtonColumn() {
        TableColumn<StudentTimer, Button> RemoveColumn = new TableColumn<StudentTimer, Button>("Remove");

        RemoveColumn.setCellFactory(new Callback<TableColumn<StudentTimer, Button>, TableCell<StudentTimer, Button>>() {
            @Override
            public TableCell<StudentTimer, Button> call(TableColumn<StudentTimer, Button> studentTimerButtonTableColumn) {
                StudentTimerRemoveCell cell = new StudentTimerRemoveCell();

                cell.setAlignment(Pos.CENTER);

                return cell;
            }
        });

        RemoveColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StudentTimer, Button>, ObservableValue<Button>>() {
            public ObservableValue<Button> call(final TableColumn.CellDataFeatures<StudentTimer, Button> p) {
                try {

                    Image imageRemove = new Image(getClass().getResource("/images/RemoveButton_small.png").toString());
                    final Button RemoveButton = new Button("", new ImageView(imageRemove));

                    RemoveButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            try {
                                StudentTimer t = p.getValue();

                                DeleteBuilder<Accommodation, String> deleteBuilder = sqlconn.getStudentTimerAccommodationDAO().deleteBuilder();
                                deleteBuilder.where().eq("studentTimerId", t.getId());
                                deleteBuilder.delete();

                                sqlconn.getStudentTimerDAO().delete(t);

                                StudentTimerTable.getItems().remove(p.getValue());
                            }catch(Exception ex){}
                        }
                    });

                    ObservableValue<Button> button = new ObservableValue<Button>() {
                        @Override
                        public void addListener(ChangeListener<? super Button> changeListener) {
                        }

                        @Override
                        public void removeListener(ChangeListener<? super Button> changeListener) {
                        }

                        @Override
                        public Button getValue() {
                            return RemoveButton;
                        }

                        @Override
                        public void addListener(InvalidationListener invalidationListener) {
                        }

                        @Override
                        public void removeListener(InvalidationListener invalidationListener) {
                        }
                    };

                    return button;

                } catch (Exception ex) {
                }

                return null;
            }
        });

        StudentTimerTable.getColumns().add(RemoveColumn);
    }

    private void bindTableControls(){

        tableEndDate.setValue(LocalDate.now());
        tableStartDate.setValue(LocalDate.now().minusDays(7));

        tableStartDate.setOnAction(new EventHandler() {
            public void handle(Event t) {
                StudentTimerTable.getItems().clear();
                updateTable();
            }
        });

        tableEndDate.setOnAction(new EventHandler() {
            public void handle(Event t) {
                StudentTimerTable.getItems().clear();
                updateTable();
            }
        });

        excelExport.setOnAction(new EventHandler() {
            public void handle(Event t) {
                saveTableToCsv(fileChooser);
            }
        });

        int numColumns = StudentTimerTable.getColumns().size();

        for (int i = 0; i < numColumns; i++) {

            TableColumn<StudentTimer, String> s = (TableColumn<StudentTimer, String>) StudentTimerTable.getColumns().get(i);
            s.setCellFactory(new StudentTimerHistoryCellCallback());
        }
    }

    private void saveTableToCsv(FileChooser fileChooser){

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

    private void tableToCsv(PrintWriter out){

        int numColumns = StudentTimerTable.getColumns().size();
        java.util.List<String> columnHeaders = new ArrayList<String>();

        for (int i = 0; i < numColumns; i++) {
            columnHeaders.add(StudentTimerTable.getColumns().get(i).getText());
        }

        out.println(StringUtils.join(columnHeaders, ", "));

        ObservableList<StudentTimer> rows = StudentTimerTable.getItems();

        for(StudentTimer studentTimer : rows){
            out.println(studentTimer.getCsv());
        }
    }
}
