package factory;

import model.DateInterval;

/**
 * Created by will on 11/5/14.
 */
public class DateIntervalFactory {

    public static DateInterval getDateInterval(String intervalString){

        if(intervalString.equals("Day")){
            return DateInterval.DAY;
        }
        else if(intervalString.equals("Week")){
            return DateInterval.WEEK;
        }
        else if(intervalString.equals("Month")){
            return DateInterval.MONTH;
        }
        else if(intervalString.equals("Year")){
            return DateInterval.YEAR;
        }

        return null;
    }
}
