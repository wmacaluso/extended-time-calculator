package common;

import common.TestTimerLogger;
import controller.timer.TestTimerController;
import db.sqlconn;
import javafx.application.Application;
import javafx.event.EventHandler;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang.time.DateUtils;
import org.controlsfx.dialog.Dialogs;

import javax.swing.*;

public class App extends Application {

    private static String CONFIG_FILE_PATH = "config.properties";
    private static boolean IS_TRIAL_BOOL = false;
    private static String TRIAL_START_DATE = "trialStartDate";
    private static String IS_TRIAL = "isTrial";
    private static int TRIAL_DAY_LENGTH = 30;
    private static String EXPIRATION_MESSAGE = "Thank you for trying Extended Test Timer. To continue using " +
            "Extended Test Timer, please visit us at \n\n http://www.peanuttechnology.com \n\n" +
            "and purchase the full version.";

    public static void main(String[] args) {        
        launch(args);
    }

    private void ShowExpirationMessage(Stage primaryStage) {
        Dialogs.create()
                .owner(primaryStage)
                .title("Trial Period Has Expired")
                .message(EXPIRATION_MESSAGE)
                .showInformation();
    }

    private void ShowRegistryErrorMessage(Stage primaryStage) throws Exception {
        String message = "We can't find some important configuration files. If you've purchased this " +
                "product, please contact us at \n\n http://www.peanuttechnology.com\n\n and we will " +
                "provide you with a working copy of this application.";
    }

    private void CheckInstallDate(Stage primaryStage) throws Exception {
        String installDateReg = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER,
                "SOFTWARE\\Peanut Technology\\Extended Time Calculator",
                "Install Date");

        String bought = WinRegistry.readString(WinRegistry.HKEY_CURRENT_USER,
                "SOFTWARE\\Peanut Technology\\Extended Time Calculator",
                "Bought");

        if (bought == "True") {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        if (installDateReg == null) {
            ShowRegistryErrorMessage(primaryStage);
            System.exit(0);
        }

        Date installDate = sdf.parse(installDateReg);
        Calendar currentDate = Calendar.getInstance();

        Calendar ExpireDate = Calendar.getInstance();
        ExpireDate.setTime(installDate);
        ExpireDate.add(Calendar.MONTH, 1);

        if (currentDate.after(ExpireDate)) {
            ShowExpirationMessage(primaryStage);
            System.exit(0);
        }
    }

    private JFrame ShowLoading() {
        //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        //3. Create components and put them in the frame.
        //...create emptyLabel...
        frame.getContentPane().add(new JLabel("Loading Extended Test Timer..."), BorderLayout.CENTER);

        //4. Size the frame.
        frame.pack();

        //5. Show it.
        frame.setVisible(true);

        return frame;
    }

    public static LocalDate dateToLocalDate(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date localDateToDate(LocalDate d) {
        Instant instant = d.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    @Override
    public void init() throws Exception {
        //ShowLoading();
    }

    public static Date getStartOfDay(Date startTime) {
        return DateUtils.truncate(startTime, Calendar.DATE);
    }

    public static Date getEndOfDay(Date startTime) {
        return DateUtils.ceiling(startTime, Calendar.DATE);
    }

    public static Date getStartOfYear(Date startTime) {
        return DateUtils.truncate(startTime, Calendar.YEAR);
    }

    public static Date getStartOfMonth(Date startTime) {
        return DateUtils.truncate(startTime, Calendar.MONTH);
    }

    public static Date getStartOfWeek(Date startTime) {

        //This is broken...
        //return DateUtils.truncate(startTime, Calendar.WEEK_OF_YEAR);

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

        cal.add(Calendar.DATE, -dayOfWeek);

        return DateUtils.truncate(cal.getTime(), Calendar.DATE);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        sqlconn.getConn();

        String fxmlFile = "/fxml/TestTimerFXML.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene scene = new Scene(root);
        primaryStage.setMinHeight(500);

        final TestTimerController ttc = loader.getController();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ttc.timer.cancel();
                sqlconn.closeConn();
            }
        });

        primaryStage.setTitle("Extended Time Calculator");
        primaryStage.getIcons().add(new Image("/images/ETC_Icon_White.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        Properties properties = getProperties();
        IS_TRIAL_BOOL = Boolean.parseBoolean(properties.getProperty("isTrial"));

        if(IS_TRIAL_BOOL) {
            checkTrialExpiration(primaryStage);
        }
    }

    private void setProperties(Properties props) {

        try {
            FileOutputStream out = new FileOutputStream(CONFIG_FILE_PATH);
            props.store(out, null);
            out.close();
        } catch (Exception ex) {
        }
    }

    private Properties getProperties() {

        File configFile = new File(CONFIG_FILE_PATH);
        Properties props = new Properties();

        if(!configFile.exists()){

            try {
                configFile.createNewFile();
            }
            catch(Exception ex){}
        }

        try {
            FileInputStream in = new FileInputStream(CONFIG_FILE_PATH);
            props.load(in);
            in.close();
        } catch (Exception ex) {
        }

        return props;
    }

    private void checkTrialExpiration(Stage primaryStage) {

        Properties props = getProperties();
        Date now = Calendar.getInstance().getTime();

        enforceTrialLogic(props, now, primaryStage);
    }

    private void enforceTrialLogic(Properties props, Date now, Stage primaryStage){

        if (props.containsKey(TRIAL_START_DATE)) {
            long trialStartDateMillis = Long.parseLong(props.getProperty(TRIAL_START_DATE));
            long nowMillis = now.getTime();

            long elapsedMillis = nowMillis - trialStartDateMillis;

            if (millisToDays(elapsedMillis) > TRIAL_DAY_LENGTH) {

                ShowExpirationMessage(primaryStage);
                System.exit(0);
            }

        } else {
            props.setProperty(TRIAL_START_DATE, Long.toString(now.getTime()));
            setProperties(props);
        }
    }

    private long millisToDays(long elapsedMillis) {
        return (((elapsedMillis / 1000) / 60) / 60) / 24;
        //return elapsedMillis;
    }
}