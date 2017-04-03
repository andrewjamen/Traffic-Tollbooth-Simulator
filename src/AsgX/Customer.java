package AsgX;

import myUtil.ExpDistribution;
import myUtil.NormalDistribution;

public class Customer {
	static protected ExpDistribution customerDistr;
	// This one determines how customers arrive
	static protected NormalDistribution serviceDistr;
	// This one determines how customers request
	static protected double currentTime, totalWaitingTime, minimumService;
	static protected int totalCount = 0;

	static public void setDistribution(double meanWaitingForCustomerTime, double meanServiceTime, double var,
			double min) {
		customerDistr = new ExpDistribution(meanWaitingForCustomerTime);
		serviceDistr = new NormalDistribution(meanServiceTime, var);
		totalCount = 0;
		currentTime = totalWaitingTime = 0;
		minimumService = min;
	}

	/**
	 * 
	 * @return a customer with arriving time and requested servic e
	 */
	static public Customer next() {
		currentTime = currentTime + customerDistr.next();
		double service = serviceDistr.sample();
		if (service < minimumService)
			service = minimumService;
		totalCount++;
		return new Customer(currentTime, service);
	}

	/**
	 * 
	 * @return totalWaitingTime;
	 */
	public static double totalWaitingTime() {
		return totalWaitingTime;
	}

	public static int totalCount() {
		return totalCount;
	}

	/****
	 * The following are instance variables and meth od
	 ****/
	protected double TimeToArrive, timeBeginsToServe, serviceNeeded;

	/**
	 * This is a rare case that the constructor is not public
	 * 
	 * @param arriveTime
	 * 
	 * @param timeNeeded
	 */
	protected Customer(double TimeToArrive, double serviceNeeded) {
		this.TimeToArrive = TimeToArrive;
		this.serviceNeeded = serviceNeeded;
		timeBeginsToServe = -1;
	}

	/**
	 * 
	 * @return the time this customer will arrive
	 */
	public double whenToArrive() {
		return TimeToArrive;
	}

	/**
	 * 
	 * @param time
	 *            is the time this customer begins to be served
	 */
	public void setBeginToServe(double time) {
		timeBeginsToServe = time;
		;
		totalWaitingTime += (this.timeBeginsToServe - this.TimeToArrive);
		// Total waiting time will be accumulated.
	}

	/**
	 * 
	 * @return compute when to leave after the service starts
	 * 
	 * @throws Exception
	 */
	public double whenToLeave() {
		if (timeBeginsToServe < 0) {
			System.out.println("Error");
			int a = 0, b = 2 / a;
			return 1E10 / a;
			// Still waiting
		}
		return timeBeginsToServe + serviceNeeded;
	}
}