//package com.peanuttech.app;
package controller.timer;

import common.TestTimerLogger;
import db.sqlconn;
import org.apache.log4j.*;
import model.studenttimer.StudentTimerRemoveCell;
import factory.AccommodationFactory;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import javafx.fxml.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
//import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn.*;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Accommodation;
import model.ClassTime;
import model.studenttimer.StudentTimer;
import model.studenttimer.StudentTimerCellCallback;

import java.util.*;


public class TestTimerController {
    public java.util.Timer timer;
    @FXML
    private TableView<StudentTimer> StudentTimerTable;
    @FXML
    private TextField StudentName;
    @FXML
    private TextField AllottedClassTime;
    @FXML
    private javafx.scene.control.ComboBox ExtendedTime;
    @FXML
    private ComboBox StartTimeHour;
    @FXML
    private ComboBox StartTimeMinute;
    @FXML
    private ComboBox StartTimeAMPM;
    @FXML
    private CheckMenuItem SoundOn;
    @FXML
    private CheckBox StartNow;
    @FXML
    private MenuItem AboutDialog;
    @FXML
    private MenuItem soundMenu;
    @FXML
    private MenuItem StatisticsMenu;
    @FXML
    private CheckBox readerAccom;
    @FXML
    private CheckBox wordProcAccom;
    @FXML
    private CheckBox voiceRecAccom;
    @FXML
    private CheckBox scribeAccom;
    @FXML
    private CheckBox calculatorAccom;
    @FXML
    private TextField otherAccom;

    private static final int menuIconSize = 25;
    //final static Logger logger = Logger.getLogger(TestTimerController.class.getName());


    private boolean StudentNameIsValid() {
        if (StudentName.getText().isEmpty()) {
            ShowErrorDialog("Please enter a student name.");
            return false;
        }

        return true;
    }

    private boolean AllottedClassTimeIsValid() {
        try {
            int classtime = Integer.parseInt(AllottedClassTime.getText().trim());

            if (classtime < 1) {
                ShowErrorDialog("Class time must be a positive integer, in minutes.");
                return false;
            }
        } catch (Exception ex) {
            ShowErrorDialog("Class time must be a positive integer, in minutes.");
            return false;
        }

        return true;
    }

    private int GetClassTimeForAsNeededStudent() {
        try {
            int classtime = Integer.parseInt(AllottedClassTime.getText().trim());

            if (classtime < 0) {
                return 0;
            } else {
                return classtime;
            }
        } catch (Exception ex) {
            return 0;
        }
    }

    private StudentTimer addAccommodations(StudentTimer st, int studentTimerId){

        List<Accommodation> a = new ArrayList<Accommodation>();

        if(readerAccom.isSelected()){
            a.add(AccommodationFactory.createReaderAccommodation(studentTimerId));
        }

        if(wordProcAccom.isSelected()){
            a.add(AccommodationFactory.createWordProcAccommodation(studentTimerId));
        }

        if(voiceRecAccom.isSelected()){
            a.add(AccommodationFactory.createVoiceRecAccommodation(studentTimerId));
        }

        if(scribeAccom.isSelected()){
            a.add(AccommodationFactory.createScribeAccommodation(studentTimerId));
        }

        if(calculatorAccom.isSelected()){
            a.add(AccommodationFactory.createCalculatorAccommodation(studentTimerId));
        }

        if(!otherAccom.getText().equals("")){
            a.add(AccommodationFactory.createOtherAccommodation(otherAccom.getText(), studentTimerId));
        }

        st.accommodations = a;

        return st;
    }

    private StudentTimer createNewStudentTimerRow(){
        StudentTimer newRow = new StudentTimer();

        newRow.studentNameProperty().set(StudentName.getText());
        newRow.ExtendedTime = ExtendedTime.getValue().toString();

        if (newRow.ExtendedTime != "As Needed") {
            if (!AllottedClassTimeIsValid())
                return null;

            newRow.ClassTimeInMinutes = Integer.parseInt(AllottedClassTime.getText());
        } else {
            newRow.ClassTimeInMinutes = GetClassTimeForAsNeededStudent();
        }

        newRow.StartTime = GetStartTime();
        newRow.EndTime = GetEndTime(newRow.StartTime, newRow.ClassTimeInMinutes);

        return newRow;
    }

    @FXML
    protected void AddStudent(ActionEvent event) {
        ObservableList<StudentTimer> data = StudentTimerTable.getItems();
        int studentTimerId = -1;

        StudentTimer newRow = createNewStudentTimerRow();

        ValidateNewRow(newRow);

        newRow.UpdateTimeLeft();

        data.add(newRow);

        StudentName.setText("");
        AllottedClassTime.setText("");

        try {
            studentTimerId = sqlconn.getStudentTimerDAO().create(newRow);
            //TestTimerLogger.log("Successfully inserted student timer " + studentTimerId);
            //logger.log(Level.TRACE, "Successfully inserted student timer " + studentTimerId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            //TestTimerLogger.log("Error inserting student timer " + ex.getMessage());
            //TestTimerLogger.logException(ex);
            //logger.log(Level.ERROR, ex.getMessage());
        }

        addAccommodations(newRow, newRow.getId());
    }

    private void ValidateNewRow(StudentTimer newRow){

        if (!StudentNameIsValid())
            return;

        if (newRow.StartTime == null) {
            ShowErrorDialog("Please enter a valid start time.");
            return;
        }

        if (newRow.EndTime != null) {
            if (newRow.EndTime.compareTo(new Date()) < 0) {
                ShowErrorDialog("The end time for this test is in the past!");
                return;
            }
        }

        if (newRow.ExtendedTime == "As Needed") {
            newRow.TimeRemainingProperty().set("No End Time");
        } else if (newRow.EndTime == null) {
            return;
        }
    }

    private Date GetStartTime() {
        try {
            int startHour = Integer.parseInt(StartTimeHour.getValue().toString());
            int startMinute = Integer.parseInt(StartTimeMinute.getValue().toString());
            String AMPM = StartTimeAMPM.getValue().toString();

            Calendar StartTimeCal = Calendar.getInstance();

            StartTimeCal.set(Calendar.MINUTE, startMinute);
            StartTimeCal.set(Calendar.SECOND, 0);
            StartTimeCal.set(Calendar.MILLISECOND, 0);

            if (AMPM == "AM") {
                StartTimeCal.set(Calendar.HOUR_OF_DAY, startHour);
            } else {
                StartTimeCal.set(Calendar.HOUR_OF_DAY, startHour + 12);
            }

            Date ParsedStartTime = StartTimeCal.getTime();

            return ParsedStartTime;
        } catch (Exception ex) {
            return null;
        }
    }

    private Date GetEndTime(Date StartTime, int ClassTimeInMinutes) {
        try {
            String ExtendedTimeString = ExtendedTime.getValue().toString();
            Date EndTime = null;

            if (ExtendedTimeString.equals(ClassTime.NONE)) {
                EndTime = new Date(StartTime.getTime() + ClassTimeInMinutes * 60 * 1000);
            } else if (ExtendedTimeString.equals(ClassTime.TIME_AND_A_HALF)) {
                EndTime = new Date(StartTime.getTime() + (int) (1.5 * (ClassTimeInMinutes * 60 * 1000)));
            } else if (ExtendedTimeString.equals(ClassTime.DOUBLE_TIME)) {
                EndTime = new Date(StartTime.getTime() + 2 * (ClassTimeInMinutes * 60 * 1000));
            } else if (ExtendedTimeString.equals(ClassTime.AS_NEEDED)) {
                EndTime = null;
            }

            return EndTime;
        } catch (Exception ex) {
            return null;
        }
    }

    private void ShowErrorDialog(String error) {
        Dialogs.showErrorDialog((Stage) StudentName.getScene().getWindow(), error, "Invalid Input", "Error");
    }

    private void BindStartNowCheckbox() {
        StartNow.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) {
                CheckStartNow(new_val);
            }
        });
    }

    private void CheckStartNow(Boolean checked) {
        StartTimeHour.setDisable(checked);
        StartTimeMinute.setDisable(checked);
        StartTimeAMPM.setDisable(checked);
    }

    private void ShowStatisticsWindow() throws Exception {
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.UTILITY);

        String fxmlFile = "/fxml/TestTimerStats.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene scene = new Scene(root);
        //Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Extended Time Calculator Statistics");
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/ETC_Icon_White.png").toString()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void BindSoundMenuItem(){

        ImageView soundMenuIcon = new ImageView("/images/icons/Gnome-Audio-Volume-Medium-64.png");
        soundMenuIcon.setFitHeight(menuIconSize);
        soundMenuIcon.setFitWidth(menuIconSize);
        soundMenu.setGraphic(soundMenuIcon);
    }

    private void BindStatisticsWindow() {
        StatisticsMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    ShowStatisticsWindow();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        ImageView statsDialogIcon = new ImageView("/images/icons/Gnome-Logviewer-64.png");
        statsDialogIcon.setFitHeight(menuIconSize);
        statsDialogIcon.setFitWidth(menuIconSize);
        StatisticsMenu.setGraphic(statsDialogIcon);
    }

    private void ShowAboutDialog() throws Exception {
        Stage primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.UTILITY);

        String fxmlFile = "/fxml/AboutExtendedTestTimer.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene scene = new Scene(root, 432, 224);
        //primaryStage.setMinHeight(500);

        primaryStage.setTitle("About Extended Time Calculator");
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/ETC_Icon_White.png").toString()));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void BindAboutDialog() {

        AboutDialog.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    ShowAboutDialog();
                } catch (Exception ex) {
                }

            }
        });

        ImageView aboutDialogIcon = new ImageView("/images/icons/Gnome-Dialog-Question-64.png");
        aboutDialogIcon.setFitHeight(menuIconSize);
        aboutDialogIcon.setFitWidth(menuIconSize);
        AboutDialog.setGraphic(aboutDialogIcon);
    }

    @FXML
    public void initialize() {

        for (int i = 0; i < 6; i++) {
            TableColumn<StudentTimer, String> s = (TableColumn<StudentTimer, String>) StudentTimerTable.getColumns().get(i);
            s.setCellFactory(new StudentTimerCellCallback());
        }

        BindRemoveButtonColumn();
        BindStartNowCheckbox();
        BindAboutDialog();
        BindStatisticsWindow();
        BindSoundMenuItem();

        String[] hours = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        ExtendedTime.getItems().setAll(new String[]{"None", "Time and a Half", "Double Time", "As Needed"});
        StartTimeAMPM.getItems().setAll(new String[]{"AM", "PM"});
        StartTimeMinute.getItems().setAll(GetMinutes());
        StartTimeHour.getItems().setAll(hours);

        SetInitialStartTime();

        timer = new java.util.Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                UpdateTable();

            }
        }, 0, 1000);
    }

    private void SetInitialStartTime() {
        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        if (hour > 12) {
            StartTimeAMPM.setValue("PM");
            hour = hour - 12;
        } else {
            StartTimeAMPM.setValue("AM");
        }

        String hourString = Integer.toString(hour);
        String minuteString = Integer.toString(minute);

        if (hour < 10) {
            hourString = "0" + hourString;
        }

        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        StartTimeMinute.setValue(minuteString);
        StartTimeHour.setValue(hourString);
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

        RemoveColumn.setCellValueFactory(new Callback<CellDataFeatures<StudentTimer, Button>, ObservableValue<Button>>() {
            public ObservableValue<Button> call(final CellDataFeatures<StudentTimer, Button> p) {
                try {

                    Image imageRemove = new Image(getClass().getResource("/images/RemoveButton_small.png").toString());
                    final Button RemoveButton = new Button("", new ImageView(imageRemove));

                    RemoveButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {

                            StudentTimerTable.getItems().remove(p.getValue());
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

    public void UpdateTable() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (StartNow.isSelected()) {
                    SetInitialStartTime();
                }

                for (StudentTimer s : StudentTimerTable.getItems()) {
                    s.UpdateTimeLeft();

                    if (s.getTimeRemaining() == "00:00:00" && SoundOn.isSelected()) {
                        try {
                            AudioClip ac = new AudioClip(getClass().getResource("/sound/202029__hykenfreak__notification-chime.wav").toString());
                            ac.play();
                        } catch (Exception ex) {
                        }
                    }
                }

                //RedrawTable();
            }
        });
    }

    private String[] GetMinutes() {
        String[] minutes = new String[60];

        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                minutes[i] = "0" + Integer.toString(i);
            } else {
                minutes[i] = Integer.toString(i);
            }
        }

        return minutes;
    }
}
