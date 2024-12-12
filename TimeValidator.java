import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeValidator {
    private Scanner scanner = new Scanner(System.in); // Scanner for user input
    public String dateValidator (String promptUser) {
        boolean dateValid = false;
        String tempDate = "";
        while (!dateValid) {
            System.out.print(promptUser);
            tempDate = scanner.nextLine();

            if (!dateFormatValidator(tempDate)) {
                System.out.print("The date format inputted is wrong. Please try again \n");
            } else if (!dateExistsValidator(tempDate)) {
                System.out.print("The date inputted does not exist. Please try again \n");
            } else {
                dateValid = true;
            }

        }
        String date = tempDate;
        return date;

    }
    public double durationValidator (String promptUser) {
        boolean durationValid = false;
        String tempDuration = "";
        double duration = 0;
        while (!durationValid) {
            System.out.println(promptUser);
            tempDuration = scanner.nextLine();
            if (!durationFormatValidator(tempDuration)) {
                System.out.print("The duration format is incorrect. Please try again \n");
            } else if (!hourExistsValidator(tempDuration)) {
                System.out.print("The duration inputted does not exist. Please try again \n");
            } else {
                durationValid = true;
            }
        }

        try { //try catch prevents program from crashing if double is instantiated wrong
            duration = Double.parseDouble(hourIncrementer(tempDuration, '.')); //
        } catch (NumberFormatException num){
            System.out.print ("Error: Duration Formatting failed");
        }
        return duration;
    }
    public String hourValidator (String promptUser) {
        boolean hourValid = false;
        String tempHour = "";
        while (!hourValid) {
            System.out.print(promptUser);
            tempHour = scanner.nextLine();
            if (!hourFormatValidator(tempHour)) {
                System.out.print("The hour format is incorrect. Please try again \n");
            } else if (!hourExistsValidator(tempHour)) {
                System.out.print("The hour inputted does not exist. Please try again \n");
            } else {
                hourValid = true;
            }
        }

        String hour = hourIncrementer(tempHour, ':');
        return hour;
    }
    private boolean dateFormatValidator (String date) { //checks for date format
        String dateRegex = "\\d{4}-\\d{2}-\\d{2}"; //regex code for date
        Pattern datePattern = Pattern.compile(dateRegex);
        Matcher dateMatcher = datePattern.matcher(date);

        return dateMatcher.matches();
    }
    private boolean durationFormatValidator (String date) { //checks for date format
        String dateRegex = "\\d{2}\\.\\d{2}"; //regex code for date
        Pattern datePattern = Pattern.compile(dateRegex);
        Matcher dateMatcher = datePattern.matcher(date);

        return dateMatcher.matches();
    }
    private boolean hourFormatValidator (String hour) { //checks for hour format
        String timeRegex = "\\d{2}:\\d{2}"; //regex code for hour
        Pattern datePattern = Pattern.compile(timeRegex);
        Matcher dateMatcher = datePattern.matcher(hour);

        return dateMatcher.matches();
    }

    public int dateOverlap (String beforeDate, String afterDate) {
        int year1 = intErrHandler (beforeDate.substring(0,4), "Year"); //turns the string into an int value
        int month1 = intErrHandler (beforeDate.substring(5,7), "Month");
        int day1 = intErrHandler (beforeDate.substring(8,10), "Day");
        double tempDate1 = ((double)year1 * 10000) + ((double)month1 * 100) + (double)day1;//Double.parseDouble(String.valueOf(year1) + String.valueOf(month1) + String.valueOf(day1)); //creates a concatenation of the date

        int year2 = intErrHandler (afterDate.substring(0,4), "Year");
        int month2 = intErrHandler (afterDate.substring(5,7), "Month");
        int day2 = intErrHandler (afterDate.substring(8,10), "Day");
        double tempDate2 = ((double)year2 * 10000) + ((double)month2 * 100) + (double)day2; //creates a concatenation of the date
        System.out.println(year1 + " " + tempDate1 + " " + tempDate2 + " " + year2);

        if (tempDate2 < tempDate1) {
            return 1;
        }
        if (tempDate2 == tempDate1) {
            return 2;
        }
        return 0;
    }

    private boolean dateExistsValidator (String date) {//checks if date is valid
        boolean dateExists = false;
        //turns first strings into integers
        int year = intErrHandler (date.substring(0,3), "Year");
        int month = intErrHandler (date.substring(5,7), "Month");
        int day = intErrHandler (date.substring(8,10), "Day");

        /*
        Month Information:
        31 days: (1)January (3)March (5)May (7)July (8)August (10)October (12)December
        30 days: (4)April (6)June (9)September (11)November
        29 or 28 days: (2)February [28 days in a common year, 29 in leap year]
         */

        switch (month) {
            case 1,3,5,7,8,10,12: //Months with 31 days
                if (day > 0 && day <= 31) { dateExists = true; }
                break;

            case 4,6,9,11: //Months with 30 days
                if (day > 0 && day <= 30) { dateExists = true; }
                break;

            case 2: //February
                if ((year & 3) == 0 && ((year %25) != 0 || (year & 15)==0)) { //leap year checker
                    if (day > 0 && day <= 29) { dateExists = true; }
                }
                else {
                    if (day > 0 && day <= 28) { dateExists = true; } //non leap year checker
                }
                break;
            default:
                dateExists = false;
        }
        return dateExists;
    }

    private boolean hourExistsValidator (String hourString) {
        //turns first strings into integers
        int hour = intErrHandler (hourString.substring(0,2), "Hour");
        int minute = intErrHandler (hourString.substring(3,5), "Minute");

        //if hour is between 0 and 23 (using military time) and minute is between 0 and 59
        return ((hour >= 0 && hour <= 23) && (minute >= 0 && minute <= 59));
    }
    private String hourIncrementer (String hourString, char separator) { //rounds up code to nearest 15 minutes
        //turns first strings into integers
        int hour = intErrHandler (hourString.substring(0,2), "Hour");
        int minute = intErrHandler (hourString.substring(3,5), "Minute");
        int tempMinute = minute;
        int tempHour = hour;

        if (minute > 0 && minute <= 15 ) {
            tempMinute = 25;
        } else if (minute > 15 && minute <= 30 ) {
            tempMinute = 50;
        } else if (minute > 30 && minute <=45) {
            tempMinute = 75;
        } else if (minute > 45 && minute <= 59) {
            tempMinute = 0;
            if (hour == 23) { tempHour = 0; }
            else { tempHour++; }
        }

        return (String.valueOf(tempHour) + separator + String.valueOf(tempMinute)); //final string value;
    }

    private int intErrHandler(String intCheck, String type) {
        int charValid = Integer.MAX_VALUE;
        try { //the try catch block checks if the value of the integer pulled from the string is a valid integer (secondary error handling)
            charValid = Integer.parseInt(String.valueOf(intCheck));
        } catch (NumberFormatException e) {
            System.out.printf ("%s is incorrectly formatted", type);
        }
        return charValid;
    }

}