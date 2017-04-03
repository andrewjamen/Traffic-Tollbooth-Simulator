package AsgX;
import myUtil.ExpDistribution;
import myUtil.NormalDistribution;

public class Car
{
	static protected ExpDistribution customerDistr; //Determines how cars arrive
	static protected NormalDistribution serviceDistr; //Determines how cars request
	static protected double currentTime, totalWaitingTime, minService;
	static double serviceTime;
	static double service;
	static double meanService;
	static protected int totalCount = 0;
	static protected int easyPassCount = 0;
	
	protected double timeToArrive, timeBeginToServe, serviceNeeded;
	
	static public void setDistribution(double meanWaitingForCustomerTime, double
	meanServiceTime, double var, double min){
		customerDistr = new ExpDistribution(meanWaitingForCustomerTime);
		serviceDistr = new NormalDistribution(meanServiceTime, var);
		totalCount = 0;
		currentTime = totalWaitingTime = 0;
		minService = min;
		meanService = meanServiceTime;
	}
	
	/**
	 * 
	 * @return - The next customer with its arriving time, and service time
	 */
	static public Car next()
	{
		currentTime = currentTime + customerDistr.next();
		service = serviceDistr.sample();
		if(service < meanService)
		{
			service = 1.0;
			easyPassCount++; //If the customer's service time is 1, they have an easy pass
		}
		else
			serviceTime += service;
		totalCount++;
		return new Car(currentTime, service);
	}
	
	/**
	 * 
	 * @return - The total waiting time for customers
	 */
	public static double totalWaitingTime()
	{
		return totalWaitingTime;
	}
	
	/**
	 * 
	 * @return - The total number of customers
	 */
	public static int totalCount()
	{
		return totalCount;
	}
	
	/**
	 * 
	 * @return - The total number of customers with the easy pass
	 */
	public static int totalCountEasyPass()
	{
		return easyPassCount;
	}
	
	/**
	 * 
	 * @param totalCount - Total number of customers
	 * @return - The pass window for customers without the easy pass
	 */
	public static double totalPassWindowEasyPass(int totalCount)
	{
		return (serviceTime / (totalCount - easyPassCount));
	}
	
	/**
	 * Constructor for the next Customer
	 * 
	 * @param timeToArrive - The time the customer is arriving according to Exponential Distribution
	 * @param serviceNeeded - The amount of time the customer needs for service according to Normal Distribution
	 */
	protected Car(double timeToArrive, double serviceNeeded)
	{
		this.timeToArrive = timeToArrive;
		this.serviceNeeded = serviceNeeded;
		timeBeginToServe = -1;
	}
	
	/**
	 * 
	 * @return - The time the customer is arriving
	 */
	public double whenToArrive()
	{
		return timeToArrive;
	}
	
	/**
	 * 
	 * @param time - Sets the time the customer got their service
	 */
	public void setBeginToServe(double time)
	{
		timeBeginToServe = time;
		totalWaitingTime += (this.timeBeginToServe - this.timeToArrive);
	}
	
	/**
	 * 
	 * @return - When the customer is leaving
	 */
	public double whenToLeave()
	{
		if(timeBeginToServe < 0)
		{
			System.out.println("error");
			int a = 0;
			int b = 2/a;
			return 1E10/a;
		}
		return timeBeginToServe + serviceNeeded;
	}
	
	/**
	 * 
	 * @return - The service time for the customer
	 */
	public double getServiceNeeded()
	{
		return service;
	}
}
	