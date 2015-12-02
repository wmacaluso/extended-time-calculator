//package com.peanuttech.app;
package model.studenttimer;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;
import common.App;
import db.SimpleStringPropertyPersister;
import db.sqlconn;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import model.Accommodation;
import model.DateInterval;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

///ORMLite.com is used for Object relational mapping to SQlite
@DatabaseTable(tableName = "studenttimer")
public class StudentTimer implements Comparable<StudentTimer> {

    @DatabaseField(columnName = "studentName", persisterClass = SimpleStringPropertyPersister.class)
    public SimpleStringProperty studentName = new SimpleStringProperty("");

    public double minutesLeft = 0;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    public String ExtendedTime;

    public SimpleStringProperty TimeRemaining = new SimpleStringProperty("");

    @DatabaseField
    public int ClassTimeInMinutes;

    @DatabaseField
    public Date StartTime;

    @DatabaseField
    public Date EndTime;

    public List<Accommodation> accommodations;
    private InvalidationListener StudentNameInvalidationListener;

    SimpleDateFormat shortTimeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
    SimpleDateFormat historyTimeFormat = new SimpleDateFormat("MMM dd',' yyyy h:mm a", Locale.ENGLISH);

    public int getId() {
        return id;
    }

    public int compareTo(StudentTimer otherStudent) {

        if (StartTime.before(otherStudent.StartTime)) {
            return -1;
        } else if (StartTime.equals(otherStudent.StartTime)) {
            return 0;
        } else if (StartTime.after(otherStudent.StartTime)) {
            return 1;
        }

        return 0;
    }

    public StudentTimer() {
        StudentNameInvalidationListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
            }
        };

        studentName.addListener(StudentNameInvalidationListener);
    }

    public void setStudentName(String name) {
        studentNameProperty().set(name);
    }

    public String getStudentName() {
        return studentNameProperty().get();
    }

    public SimpleStringProperty studentNameProperty() {
        return studentName;
    }

    public String getExtendedTime() {
        return ExtendedTime;
    }

    public String getClassTime() {
        if (ClassTimeInMinutes == 0) {
            return "As Needed";
        } else {
            return Integer.toString(ClassTimeInMinutes) + " minutes";
        }
    }

    public String getStartTime() {
        return shortTimeFormat.format(StartTime);
    }

    public String getHistoryStartTime() {
        return historyTimeFormat.format(StartTime);
    }

    public String getEndTime() {
        if (EndTime == null) {
            return "No End Time";
        } else {
            return shortTimeFormat.format(EndTime);
        }
    }

    public String getHistoryEndTime() {

        if (EndTime == null) {
            return "No End Time";
        } else {
            return historyTimeFormat.format(EndTime);
        }
    }

    public void UpdateTimeLeft() {
        if (EndTime == null) {
            return;
        }

        Date now = new Date();

        int diffInSeconds = 0;

        if (now.getTime() > StartTime.getTime()) {
            diffInSeconds = (int) Math.round(((EndTime.getTime() - now.getTime()) / (1000.0)));

            if (diffInSeconds <= 0) {
                setTimeRemaining("00:00:00");
                return;
            }

        } else {
            diffInSeconds = (int) Math.round(((EndTime.getTime() - StartTime.getTime()) / (1000.0)));
        }

        int seconds = diffInSeconds % 60;
        int hours = (int) diffInSeconds / 3600;
        int minutes = (int) (diffInSeconds - hours * 3600) / 60;

        minutesLeft = diffInSeconds / 60;

        String minuteString = Integer.toString(minutes);
        String secondString = Integer.toString(seconds);
        String hourString = Integer.toString(hours);

        if (minutes < 10) {
            minuteString = "0" + minutes;
        }

        if (seconds < 10) {
            secondString = "0" + seconds;
        }

        if (hours > 0) {
            setTimeRemaining(hourString + ":" + minuteString + ":" + secondString);
        } else {
            setTimeRemaining(minuteString + ":" + secondString);
        }
    }

    public final String getTimeRemaining() {
        if (EndTime == null) {
            return "No End Time";
        } else {
            return TimeRemaining.get();
        }
    }

    public final void setTimeRemaining(String s) {
        TimeRemaining.set(s);
    }

    public SimpleStringProperty TimeRemainingProperty() {
        return TimeRemaining;
    }

    public Date getDatePartIntervalString(DateInterval interval) {

        switch (interval) {
            case DAY:
                return App.getStartOfDay(StartTime);
            case WEEK:
                return App.getStartOfWeek(StartTime);
            case MONTH:
                return App.getStartOfMonth(StartTime);
            case YEAR:
                return App.getStartOfYear(StartTime);
        }

        return new Date();
    }

    public static Map<Date, Integer> groupStudentTimers(List<StudentTimer> st, DateInterval interval) {

        Map<Date, Integer> studentTimerCounts = new TreeMap<Date, Integer>();

        for (StudentTimer s : st) {

            Date datePart = s.getDatePartIntervalString(interval);

            int count = studentTimerCounts.getOrDefault(datePart, 0);

            if (count == 0) {
                studentTimerCounts.put(datePart, 1);
            } else {
                studentTimerCounts.replace(datePart, count + 1);
            }
        }

        return studentTimerCounts;
    }

    public static List<StudentTimer> getStudentTimersInDateRange(Date startDate, Date endDate) {
        PreparedQuery<StudentTimer> preparedQuery;
        List<StudentTimer> studentTimerList;

        Dao<StudentTimer, String> studentTimerDao = sqlconn.getStudentTimerDAO();
        QueryBuilder<StudentTimer, String> queryBuilder = studentTimerDao.queryBuilder();

        try {

            queryBuilder.where().between("StartTime", startDate, endDate);
            preparedQuery = queryBuilder.prepare();
            studentTimerList = studentTimerDao.query(preparedQuery);
            return studentTimerList;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public List<Accommodation> getAccommodations() {

        PreparedQuery<Accommodation> preparedQuery;
        Dao<Accommodation, String> accommDao = sqlconn.getStudentTimerAccommodationDAO();
        QueryBuilder<Accommodation, String> queryBuilder = accommDao.queryBuilder();

        try {

            queryBuilder.where().eq("studentTimerId", id);
            preparedQuery = queryBuilder.prepare();
            List<Accommodation> accommodations = accommDao.query(preparedQuery);
            return accommodations;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public String getCsv() {

        List<String> values = new ArrayList<String>();

        values.add(studentName.get());
        values.add(Integer.toString(ClassTimeInMinutes));
        values.add(ExtendedTime);
        values.add(StartTime.toString());
        values.add(EndTime.toString());

        return StringUtils.join(values, ", ");

    }
}