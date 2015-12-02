package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import db.sqlconn;

/**
 * Created by will on 10/22/14.
 */

@DatabaseTable(tableName = "StudentTimerAccommodation")
public class Accommodation {

    public static String READER = "Reader";
    public static String WORD_PROCESSOR = "Word Processor";
    public static String VOICE_RECOGNITION = "Voice Recognition";
    public static String SCRIBE = "Scribe";
    public static String CALCULATOR = "Calculator";
    public static String OTHER = "Other";

    @DatabaseField
    private String accommodationDescription;

    @DatabaseField
    private String accommodationType;

    @DatabaseField
    private int studentTimerId;

    public Accommodation(){}

    public String getAccommodationDescription(){return accommodationDescription;}

    public String getAccommodationType(){return accommodationType;}

    public int getStudentTimerId(){return studentTimerId;}

    public Accommodation(String type, int studentTimerId){
        this.accommodationType = type;
        this.studentTimerId = studentTimerId;

        try {
            sqlconn.getStudentTimerAccommodationDAO().create(this);
        }
        catch(Exception ex){}
    }

    public Accommodation(String otherType, String otherDesc, int studentTimerId){
        this.accommodationType = otherType;
        this.accommodationDescription = otherDesc;
        this.studentTimerId = studentTimerId;

        try {
            sqlconn.getStudentTimerAccommodationDAO().create(this);
        }
        catch(Exception ex){}
    }
}
