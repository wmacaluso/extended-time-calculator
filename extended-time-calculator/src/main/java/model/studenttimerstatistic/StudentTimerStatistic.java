package model.studenttimerstatistic;

import model.Accommodation;
import model.ClassTime;
import model.DateInterval;
import model.studenttimer.StudentTimer;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by will on 10/20/14.
 */
public class StudentTimerStatistic {

    public Date date;
    public DateInterval interval;
    //public String dateString;
    public int numTests;
    public int numReaders;
    public int numWordProcessors;
    public int numVoiceRecog;
    public int numScribes;
    public int numCalculators;
    public int numOther;
    public int numTimeAndAHalf;
    public int numDoubleTime;

    SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

    public static List<StudentTimerStatistic> groupByDateInterval(List<StudentTimer> studentTimers,
                                                                  DateInterval interval){

        List<StudentTimerStatistic> stats = new ArrayList<StudentTimerStatistic>();
        Map<Date, StudentTimerStatistic> statMap = new TreeMap<Date, StudentTimerStatistic>();

        for(StudentTimer studentTimer : studentTimers){
            Date datePart = studentTimer.getDatePartIntervalString(interval);
            statMap = addStudentTimerToStatMap(statMap, studentTimer, datePart, interval);
        }

        return new ArrayList(statMap.values());
    }

    private static Map<Date, StudentTimerStatistic> addStudentTimerToStatMap(Map<Date, StudentTimerStatistic> statMap,
                                                                             StudentTimer studentTimer,
                                                                             Date datePart,
                                                                             DateInterval interval) {

        StudentTimerStatistic studentTimerStatistic = statMap.getOrDefault(datePart, null);

        if (studentTimerStatistic == null) {
            studentTimerStatistic = new StudentTimerStatistic(datePart, interval, studentTimer);
            statMap.put(datePart, studentTimerStatistic);

        } else {
            studentTimerStatistic.addStudentTimerToStats(studentTimer);
            statMap.replace(datePart, studentTimerStatistic);
        }

        return statMap;
    }

    public StudentTimerStatistic(Date date, DateInterval interval, StudentTimer studentTimer){
        this.date = date;
        this.interval = interval;
        addStudentTimerToStats(studentTimer);
    }

    public String getNumTimeAndAHalf(){return Integer.toString(numTimeAndAHalf);}

    public String getNumDoubleTime(){return Integer.toString(numDoubleTime);}

    public String getDate(){

        SimpleDateFormat day = new SimpleDateFormat("E '('dd/MM/yyyy')'");
        SimpleDateFormat week = new SimpleDateFormat("'Week' W 'of' MMMM");
        SimpleDateFormat month = new SimpleDateFormat("MMMM, yyyy");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");

        switch(interval){
            case DAY: return day.format(date);
            case WEEK: return week.format(date);
            case MONTH: return month.format(date);
            case YEAR: return year.format(date);
            default: return null;
        }
    }

    public String getNumTests(){
        return Integer.toString(numTests);
    }

    public String getNumReaders(){
        return Integer.toString(numReaders);
    }

    public String getNumWordProcessors(){
        return Integer.toString(numWordProcessors);
    }

    public String getNumVoiceRecog(){
        return Integer.toString(numVoiceRecog);
    }

    public String getNumScribes(){
        return Integer.toString(numScribes);
    }

    public String getNumCalculators(){
        return Integer.toString(numCalculators);
    }

    public String getNumOther(){
        return Integer.toString(numOther);
    }

    public void addStudentTimerToStats(StudentTimer studentTimer){
        numTests++;

        if(studentTimer.getExtendedTime().equals(ClassTime.TIME_AND_A_HALF)){
            numTimeAndAHalf++;
        }

        if(studentTimer.getExtendedTime().equals(ClassTime.DOUBLE_TIME)){
            numDoubleTime++;
        }

        List<Accommodation> accommodations = studentTimer.accommodations;

        for(Accommodation accommodation : accommodations){
            if(accommodation.getAccommodationType().equals(Accommodation.READER)){
                numReaders++;
            }

            if(accommodation.getAccommodationType().equals(Accommodation.WORD_PROCESSOR)){
                numWordProcessors++;
            }

            if(accommodation.getAccommodationType().equals(Accommodation.VOICE_RECOGNITION)){
                numVoiceRecog++;
            }

            if(accommodation.getAccommodationType().equals(Accommodation.SCRIBE)){
                numScribes++;
            }

            if(accommodation.getAccommodationType().equals(Accommodation.CALCULATOR)){
                numCalculators++;
            }

            if(accommodation.getAccommodationType().equals(Accommodation.OTHER)){
                numOther++;
            }
        }
    }

    public String getCsv() {
        List<String> values = new ArrayList<String>();

        values.add(date.toString());
        values.add(Integer.toString(numTests));
        values.add(Integer.toString(numTimeAndAHalf));
        values.add(Integer.toString(numDoubleTime));
        values.add(Integer.toString(numReaders));
        values.add(Integer.toString(numWordProcessors));
        values.add(Integer.toString(numVoiceRecog));
        values.add(Integer.toString(numScribes));
        values.add(Integer.toString(numOther));

        return StringUtils.join(values, ", ");
    }
}
