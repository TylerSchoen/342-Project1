import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * The Employee represents a team lead or a basic developer.
 * This is determined by the isLead boolean.
 * The employee arrives at a random time between 8-8:30, attends various
 * meetings, and asks questions throughout the day.
 *
 * Created by John King on 12-Oct-16.
 */
public class Employee extends Thread {

	/**
	 *  Clock, to reference time of the system
	 */
    private Clock clock;

    /**
	 * If an employee is a team lead, he will have subordinate employees
	 */
	private ArrayList<Employee> subordinateList = new ArrayList<Employee>();

	/**
	 * True if the employee is a team lead
	 */
	private boolean isLead = false;

	/**
	 * The employee's team number
	 */
	private int teamNumber;

	/**
	 * The employee's number
	 */
	private int employeeNumber;

	/**
	 * A reference to the team lead
	 */
	private Employee lead;

	/**
	 * A reference to the manager
	 */
	private Manager manager;

	/**
	 * Time spent for lunch
	 */
	private int lunchTime = 0;

	/**
	 * Time spent in meetings and waiting for meetings
	 */
	private int meetingTime = 0;

	/**
	 * Time spent working
	 */
	private int workingTime = 0;

	/**
	 * Time spent waiting for answers to questions
	 */
	private int waitingTime = 0;

	/**
	 * The object for mutually exclusive access to the conference room
	 */
	private ConferenceRoom conferenceRoom;

	/**
	 * @return the Employee's current minute
	 */
	public int getMinute()
	{
		return minute;
	}

	/**
	 * The employee's current minute
	 */
	private int minute;

	/**
	 * Constructor for a new employee
	 * 
	 * @param _teamNumber
	 *        the number of the team
	 * @param _employeeNumber
	 *        the number of the employee
	 * @param _manager
	 *        a reference to the manager
	 * @param _conferenceRoom
	 *        a reference to the ConferenceRoom
	 */
	public Employee(int _teamNumber, int _employeeNumber, boolean lead, Manager _manager,
		ConferenceRoom _conferenceRoom, Clock _clock)
	{
		teamNumber = _teamNumber;
		employeeNumber = _employeeNumber;
		manager = _manager;
		conferenceRoom = _conferenceRoom;
		clock = _clock;

		if (lead) {
			isLead = true;
		} else {
			isLead = false;
		}
	}

    @Override
	public synchronized void run() {
        clock.waitForOpening();
		int timeStamp;
		Random random = new Random();

		boolean lunchTaken = false;
		boolean didStatus = false;

		int arrivalDelay = random.nextInt(31);
		try {
			sleep(arrivalDelay*8);
		} catch (InterruptedException e) {
			System.out.println("Interrupt at " + e);
		}

		// TODO: Make sure the offset is correct
		int arrivalTime = 480 + arrivalDelay;


		// Set what minute the employee is currently on and log the arrival event
		minute = arrivalTime;
		String employeeArrived = String.format("Employee %d:%d has arrived at work at %d:%d.", getTeamNumber(), getEmployeeNumber(), clock.getTime()[0], clock.getTime()[1]);
		System.out.println(employeeArrived);

		// Determine when the employee will take for lunch the day
		int whenToEat = minute + decideLunchTime();

		// Based on the amount of time taken for lunch, determine when they should leave
		// TODO: Make sure the offset is correct
		long departTime = arrivalTime + 480;

		// If the employee is a lead, go to the Lead Standup
		if (isLead) {
			//try {
				// Log what time it is and that the lead for team X is waiting
				// outside the Manager's office
				timeStamp = clock.getTimeInMinutes();
				String leadArrived = String.format("Lead %d has arrived at the PM's office at %d:%d.", getTeamNumber(),clock.getTime()[0], clock.getTime()[1]);
				manager.office.addMorningQueue(this); // adds themselves to the queue
			System.out.println(leadArrived);

//				// TODO: Wait for all the leads to arrive
//				// Mark the current time and measure elapsed time until the meeting starts
//				if (manager.office.addMorningQueue(this) != 3) {
//					manager.office.waitForTeamLeads(); //TODO: employee class should not be calling this method
//				}
//
//				int waitTime = clock.elapsedTime(timeStamp);
//				minute += waitTime;
//				manager.office.runMorningMeeting(); //TODO: employee class should not be calling this method
//
//				// Start the meeting
//				sleep(10*15);
//				minute += 15;

				// TODO: Implement wait for team members to arrive for morning standup

//			} catch (InterruptedException e) {
//			}

		} else {
			// You're a developer, wait for the stand-up
			timeStamp = clock.getTimeInMinutes();
			// TODO: Implement devs waiting for other devs
			//try {
				// Employee waits for other employees
			//} catch (InterruptedException e) {
			//}
			minute += clock.elapsedTime(timeStamp);

		}

		// Once the whole team has arrived, attempt to acquire the conference room
		timeStamp = clock.getTimeInMinutes();
		if (isLead) {
			// TODO: Aquire conference room

		}

		// TODO: Once the conference room is acquired, have the meeting
		//try {

		//} catch (InterruptedException e1) {
		//}

		// Adjust emplyee's minute
		minute += clock.elapsedTime(timeStamp);

		// Add the time that the meeting took
		// Wait the amount of time the meeting took
		try {
			String teamStandup = String.format("Employee %d%d contributes to the morning standup meeting.", getTeamNumber(), getEmployeeNumber());
			sleep(15*10);
			minute += 15;
		} catch (InterruptedException e) {
			System.out.println("Interrupt at " + e);
		}


		// Free the conference room for other teams
		if (isLead) {
			// Leave conference room
		}

		while (clock.getTimeInMinutes() <= departTime) {
			// Status meeting
			// If it's 4 and we haven't done the status meeting, do it
			// TODO: Finish what you're doing and queue for status meeting

			// TODO: Complete status meeting

			// Otherwise if we've worked 8 hours or more, leave
			// TODO: Implement leave logic

			// Otherwise we're working
			// Sometimes a dev will ask a question
			if (random.nextInt(1000) == 1) {

				// 50% of the time a lead can answer
				if ((!isLead)
						&& (random.nextInt(2) == 0)) {

					// Otherwise go ask the Manager		
				} else {
					// TODO: Queue question for manager
				}
			}

			// Devs need to eat
			if (!lunchTaken && (minute >= whenToEat)) {
				lunchTaken = true;

				// Decide how long to take lunch for
				int extraLunchTime = random.nextInt(31);
				int lunchTime = 30 + extraLunchTime;

				departTime += extraLunchTime;

				//Simulate lunch time
				try {
					sleep(lunchTime*10);
				} catch (InterruptedException e) {
					System.out.println("Interrupt at " + e);
				}
			}

			// Increment the working minute and minute
			minute++;
			workingTime++;
		}
	}

	/**
	 * @return the list of this employee's subordinates (if any)
	 */
	public List<Employee> getSubordinates()
	{
		return subordinateList;
	}

	/**
	 * @param e
	 * the employee to assign to this lead
	 */
	public synchronized void addSubordinate(Employee e)
	{
		subordinateList.add(e);
	}

	/**
	 * @return The team number
	 */
	public int getTeamNumber()
	{
		return teamNumber;
	}

	/**
	 * Sets the reference to the team members lead
	 */
	public void setLead(Employee e) {
		this.lead = e;
	}

	/**
	 * @return The employee number
	 */
	public int getEmployeeNumber()
	{
		return employeeNumber;
	}

	/**
	 * @return The employee's lunch time for the day
	 */
	private int decideLunchTime()
	{
		Random r = new Random();

		// Number of minutes to wait after arrival to take lunch. Not
		// within first hour or after working for 6 hours
		return 60 + r.nextInt(300);
	}

	public String id()
	{
		return "Developer " + Integer.toString(teamNumber) + " "
			+ Integer.toString(employeeNumber);
	}

	/**
	 * String that represents the report of this thread
	 */
	private String reportStr;

	/**
	 * @return this thread's report
	 */
	public String getReportStr()
	{
		return reportStr;
	}
}
