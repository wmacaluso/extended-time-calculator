package controller.stats;

import common.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.studenttimer.StudentTimer;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by will on 11/5/14.
 */
public class BasicStatsController {

    @FXML
    private Label dayTests;

    @FXML
    private Label weekTests;

    @FXML
    private Label monthTests;

    @FXML
    private Label yearTests;

    private void updateBasicStats() {

        Date now = Calendar.getInstance().getTime();

        int dayTestsString = getNumTests(App.getStartOfDay(now), App.getEndOfDay(now));
        int weekTestsString = getNumTests(App.getStartOfWeek(now), App.getEndOfDay(now));
        int monthTestsString = getNumTests(App.getStartOfMonth(now), App.getEndOfDay(now));
        int yearTestsString = getNumTests(App.getStartOfYear(now), App.getEndOfDay(now));

        dayTests.setText(Integer.toString(dayTestsString));
        weekTests.setText(Integer.toString(weekTestsString));
        monthTests.setText(Integer.toString(monthTestsString));
        yearTests.setText(Integer.toString(yearTestsString));

    }


    private int getNumTests(Date startDate, Date endDate) {
        return StudentTimer.getStudentTimersInDateRange(startDate, endDate).size();
    }

    @FXML
    public void initialize() {
        updateBasicStats();
    }
}
