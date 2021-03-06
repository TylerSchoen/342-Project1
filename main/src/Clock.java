import javax.swing.plaf.SliderUI;

/**
 * Created by John King on 12-Oct-16.
 */

public class Clock {
    private static Clock instance = null;
    private State state = State.ARRIVAL_TIME;
    private int hour = 0;
    private int minute = 0;


    /**
     * This private method exists, just so that a developer cannot
     * create a clock object the traditional way with "new Clock();"
     * but instead is forced to use the createClock() method
     */
    private Clock(){}

    /**
     * Ensures that our clock class is a singleton. If there is no clock created
     * method will return a new clock object, otherwise it will return null
     * @return
     */
    public static Clock createClock(){
        if(instance == null){
            instance = new Clock();
        }
        else{
            System.err.println("There can only be one clock instance");
        }
        return instance;
    }


    /**
     * When the Clock object is started, the current time in milliseconds
     * is logged and used later to calculate the current time in the simulation
     */
    public void startClock(){
        new Thread( new Timer(this) ).start(); // creates and runs a Timer object
    }

    /**
     * returns a list in the format of [hour,minute].
     * @return
     */
    public int[] getTime(){
        int[] currentTime = {hour, minute};
        return currentTime;
    }

    public int getTimeInMinutes() {
        int minutes = (hour * 60) + minute;
        return minutes;
    }

    /**
     * Calcuates the total elapsed time in minutes from the current time (minutes) - input time (minutes)
     * @param startTime
     * @return elaspsedTime in minutes
     */
    public int elapsedTime(int startTime){
        int elapsedTime = getTimeInMinutes() - startTime;

        return elapsedTime;

    }

    /**
     * returns true if the given array is the current time in the system
     * @param input
     * @return
     */
    public  boolean isSameTime(int[] input){
        return input[0] == hour && input[1] == minute;
    }

    /***
     * Takes the duration of a metting and returns the end time
     * @param minute
     * @return
     */
    public int[] getEndOfMeeting(int minute){

        if(minute > 60){
            System.err.println("MINUTE CANNOT BE GREATER THAN 60");
            System.exit(1);
        }
        int nMinute = this.minute + minute;
        int nHour = hour;


        if(nMinute > 60){
            if(nHour+1 > 12){
                nHour = (nHour+1)-12;
            }
            else{
                nHour += 1;
            }

            nMinute -= 60;
        }
        System.out.println(nHour + ":" + nMinute);
        int[] result = {nHour,nMinute};

        return result;
    }

    /**
     * These two update methods are only used by the Timer class, which updates
     * our "hour" and "minute" variables in the Clock class
     * @param minutes
     */
    public void updateMinutes(int minutes){
        this.minute = minutes;
    }
    public void updateHour(int hour){
        this.hour = hour;
    }

    /**
     * This method returns one of the basic states our system can be in.
     * @return
     */
    public State getState(){


        if (hour == 8 && minute <= 30) {
            state = State.ARRIVAL_TIME;
        } else if (hour == 4) {
            if (minute >= 30)
                state = State.DEPARTURE_TIME;
            else
                state = State.MANDATORY_MEETING;
        } else if (hour == 5) {
            state = State.CLOSED;
        } else {
            state = State.WORK_TIME;  // default value is work time
        }

        return state;

    }

    /**
     * forces all threads to wait till 8:00 to do anything
     */
    public void waitForOpening(){
        while(this.getTime()[0] !=8){   // makes sure the manager enters at 8:00
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

