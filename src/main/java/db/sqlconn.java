package db;

/**
 * Created by will on 10/13/14.
 */

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import javafx.beans.property.SimpleStringProperty;
import model.Accommodation;
import model.ClassTime;
import model.studenttimer.StudentTimer;

import java.sql.*;
import java.util.*;

public class sqlconn {

    private static Connection conn;
    //private static String DATABASE_NAME = "/home/will/Code/testtimer/extended-time-calculator/testtimer.db";
    private static String DATABASE_NAME = "testtimer.db";
    private static String CONN_STRING = "jdbc:sqlite:" + DATABASE_NAME;
    private static Dao<StudentTimer,String> studentTimerDao;
    private static Dao<Accommodation,String> accommodationTimerDao;
    private static ConnectionSource connectionSource;

    private static void CreateDatabase(){

        try {

            //TableUtils.dropTable(connectionSource, StudentTimer.class, true);
            TableUtils.createTable(connectionSource, StudentTimer.class);

        }
        catch (Exception ex){System.out.println(ex.getMessage());}

        try {

            //TableUtils.dropTable(connectionSource, Accommodation.class, true);
            TableUtils.createTable(connectionSource, Accommodation.class);
        }
        catch (Exception ex){System.out.println(ex.getMessage());}
    }

    private static void CreateRandomStudent(){

        StudentTimer newRow = new StudentTimer();

        int month = (int)(Math.random()*12);
        int day = (int)(Math.random()*30);
        int year = 2012 + (int)(Math.random()*3);
        int hour =  (int)(Math.random()*23);
        int min = (int)(Math.random()*59);

        String[] extendedTime = {"None", "Time and a Half", "Double Time", "As Needed"};

        newRow.ExtendedTime = extendedTime[new Random().nextInt(extendedTime.length)];

        newRow.ClassTimeInMinutes = (int)(Math.random()*180);
        String[] names = {"John", "Mark", "Sally", "Annie", "Kelly", "Robert"};
        newRow.studentName = new SimpleStringProperty(names[new Random().nextInt(names.length)]);

        String[] classTimes = {ClassTime.TIME_AND_A_HALF, ClassTime.AS_NEEDED, ClassTime.DOUBLE_TIME, ClassTime.NONE};
        newRow.ExtendedTime = classTimes[new Random().nextInt(classTimes.length)];

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        startCal.set(year, month, day, hour, min);
        endCal.set(year, month, day, hour, min);

        endCal.add(Calendar.MINUTE, (int)Math.random()*200);

        newRow.StartTime = startCal.getTime();
        newRow.EndTime = endCal.getTime();

        try {
            studentTimerDao.create(newRow);
        }
        catch(Exception ex){}


        createRandomAccommodations(newRow.getId());
    }

    private static void createRandomAccommodations(int id){
        if(Math.random() < .3){
            new Accommodation(Accommodation.READER, "", id);
        }

        if(Math.random() < .3){
            new Accommodation(Accommodation.WORD_PROCESSOR, "", id);
        }

        if(Math.random() < .3){
            new Accommodation(Accommodation.VOICE_RECOGNITION, "", id);
        }

        if(Math.random() < .2){
            new Accommodation(Accommodation.SCRIBE, "", id);
        }

        if(Math.random() < .2){
            new Accommodation(Accommodation.OTHER, "other", id);
        }
    }

    public static void CreateDummyTestData(){

        int MAX_TESTS = 1000;

        for(int i = 0; i < MAX_TESTS; i++){

            CreateRandomStudent();
        }
    }

    public static Dao<StudentTimer,String> getStudentTimerDAO(){
        return studentTimerDao;
    }

    public static Dao<Accommodation,String> getStudentTimerAccommodationDAO(){
        return accommodationTimerDao;
    }

    public static void closeConn(){

        try {
            connectionSource.close();
        }
        catch(Exception ex){}
    }

    public static Connection getConn() {
        if (conn != null) {
            return conn;
        } else {

            try {
                Class.forName("org.sqlite.JDBC");

                conn = DriverManager.getConnection(CONN_STRING);
                connectionSource = new JdbcConnectionSource(CONN_STRING);

                CreateDatabase();

                studentTimerDao = DaoManager.createDao(connectionSource, StudentTimer.class);
                accommodationTimerDao = DaoManager.createDao(connectionSource, Accommodation.class);

            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }

            //CreateDummyTestData();
            return conn;
        }
    }
}
