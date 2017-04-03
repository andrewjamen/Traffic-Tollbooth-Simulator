package AsgX;


import AsgX.Car;
import AsgX.ListQueue;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * 
 * @author Andrew Amen
 */

public class Sim
{
	
	public static void main(String[] args)
	{
		//Setting variables equal to the sample arguments
		double mtime = Double.parseDouble(args[0]);
		double vtime = Double.parseDouble(args[1]);
		double flow = Double.parseDouble(args[2]);
		int booths = Integer.parseInt(args[3]);
		
		//Setting up the output
		System.out.println("Simulation -- 3 hours,  Booth No:" + booths +", "
				+ "Without EZ-Pass Modification: m=" + mtime + ", v=" + vtime );
		System.out.println("** Flow: " + flow + " cars per second");
		simulate((1/flow),mtime,vtime, booths);
	}
	
	/**
	 * simulates the toll booth process
	 * 
	 * @param meanWaitingForCarTime, average time for cars to arrive (Exponential Distribution)
	 * @param meanServiceTime, average time each car spends at a booth (Normal Distribution)
	 * @param var, variance from Normal Distribution
	 * @param lineNum, number of booths
	 */
	public static void simulate(double meanWaitingCarTime, double meanServiceTime,
			double var,  int lineNum)
	{
		double min = 1;
		Car.setDistribution(meanWaitingCarTime, meanServiceTime, var, min);
			
		ListQueue<Car> carPool = new ListQueue<Car>(); //queue for all new cars, the front one is the next car to arrive 
		ArrayList<ListQueue<Car>> tollBooths = new ArrayList<ListQueue<Car>>(); //ArrayList to hold all the toll booths
		for(int i = 0; i < lineNum; i++){
			ListQueue<Car> booth = new ListQueue<Car>(20);
			tollBooths.add(booth);
		}
		ListQueue<Car> openRoad = new ListQueue<Car>(); //queue for cars to go if all booths are full
		
		int totalCount = 0; //number of cars to be served
		int maxLength = 0; //max number of cars waiting on the open road at once
		double timer; //time timer
		
		//determines if the cars arrival time is less than the total simulation time
		Car car = Car.next();
		while(car.whenToArrive() <= 10800){
			carPool.offer(car);
			car = Car.next();
			totalCount++;
		}
		
		//gets all cars to be served into shortest line
		ListQueue<Car> currentBooth;
		while(!carPool.empty())
		{
			currentBooth = checkSize(tollBooths); //booth with the smallest line
			if (currentBooth != null) //If the booth is not full
			{
				if(!openRoad.empty())
					car = openRoad.poll(); //if there is a car on the open road put it in a line
				else
					car = carPool.poll(); //next car comes from the car pool
				timer = car.whenToArrive(); //set time to when next car arrives
				currentBooth.offer(car); //next car goes into the booth with the smallest line
				car.setBeginToServe(timer); //service right away
				continue;
			}
			else{ //If the booth is full				
				car = carPool.poll(); //car comes from the car pool queue
				openRoad.offer(car); //car goes straight into the open road queue
				if(openRoad.size() > maxLength){
					maxLength = openRoad.size(); //determines the max number of cars on the open road at once
				}
				timer = car.whenToArrive(); //set time to when next car arrives
			}
			currentBooth = nextToLeave(tollBooths); //booth with the next car to leave
			
			//if another car arrives first, then go here
			if(carPool.peek() != null && currentBooth.peek() != null && carPool.peek().whenToArrive() < currentBooth.peek().whenToLeave()){
				car = carPool.poll(); //comes out from car pool
				timer = car.whenToArrive(); //set timer to when a car arrives
				currentBooth = checkSize(tollBooths); //booth with the shortest line
				if(currentBooth != null) {
					currentBooth.offer(car); //if the booth is not full, car goes into the booth
					if(currentBooth.size() == 1){
						car.setBeginToServe(timer); //if the car is the only car in the booth, get served immediately
					}
				}
				else{
					openRoad.offer(car); //if the booth is full, the car goes to the open road queue
					if(openRoad.size() > maxLength)
						maxLength = openRoad.size(); //determines the max number of cars on the open road at once
				}
				continue;
			}
			car = currentBooth.poll(); //car is the next car to leave
			timer = car.whenToLeave(); //set timer to when car leaves
			if(!currentBooth.empty())
				currentBooth.peek().setBeginToServe(timer); //if the same booth is open, get served immediately 
		}
		//after all cars have arrived, empty out the rest of the toll booths
		while(!areBoothsEmpty(tollBooths))
		{
			currentBooth = nextToLeave(tollBooths); //this booth has the next car to leave
			car = currentBooth.poll(); //car is the next car to leave
			timer = car.whenToLeave(); //set timer to when car leaves
			if(!currentBooth.empty())
				currentBooth.peek().setBeginToServe(timer); //If the same booth is open, get served immediately
		}
		//average wait time for cars is the total waiting time divided by the number of cars
		double averageWaitTime = (Car.totalWaitingTime())/(Car.totalCount());
		
		DecimalFormat df = new DecimalFormat("#.###");
		System.out.println("** Total Cars: " + totalCount);
		System.out.println("** EZ-Pass: " + Car.totalCountEasyPass());
		System.out.println("** Pass window without EZ-Pass: " + df.format(Car.totalPassWindowEasyPass(totalCount)) + " secs");
		System.out.println("** Max number of cars waiting on road: " + maxLength);
		System.out.println("** Average waiting time: " + formatTime(averageWaitTime));
	}
	
	/**
	 * This method determines if there any more cars in any of the booths
	 * 
	 * @param booths, number of toll booths
	 * @return True, if all booths are empty
	 * @return False, if there is not an empty booth
	 */
	public static boolean areBoothsEmpty(ArrayList<ListQueue<Car>> booths)
	{
		for(int i = 0; i < booths.size(); i++){
			if(!booths.get(i).empty())
				return false;
		}
		return true;
	}
	
	/**
	 * determines if there is any room for cars to go into any booth.
	 * 
	 * @param booths, number of toll booths
	 * @return - booth with the least amount of cars, null if all booths are full
	 */
	public static ListQueue<Car> checkSize(ArrayList<ListQueue<Car>> booths)
	{
		int min = 20;
		ListQueue<Car> smallest = null;
		
		for(int i = 0; i<booths.size(); i++){
			if(booths.get(i).empty())
				return booths.get(i);
		}
		for(int i = 0; i<booths.size(); i++){
			if(booths.get(i).size() < min)
				smallest = booths.get(i);
		}
		return smallest;
	}
	
	/**
	 * determines the booth with the next car to leave,
	 * and closes a booth if there are no cars in its line
	 * 
	 * @param booths, number of toll booths
	 * @return booth with the next car to leave
	 */
	public static ListQueue<Car> nextToLeave(ArrayList<ListQueue<Car>> booths){
		for(int i = 0; i < booths.size(); i++){
			if(booths.get(i).empty())
				booths.remove(booths.get(i));
		}
		double nextTime = booths.get(0).peek().whenToLeave();
		ListQueue<Car> next = booths.get(0);
		for(int i=0; i<booths.size();i++){
			if(!booths.get(i).empty()){
				if(booths.get(i).peek().whenToLeave() < nextTime){
					next = booths.get(i);
					nextTime = booths.get(i).peek().whenToLeave();
				}
			}
		}
		return next;
	}
	
	/**
	 * prints formatted time
	 * @param time
	 * @return formatted time
	 */
	public static String formatTime(double time)
	{
		String string="";
		long s,m,t=Math.round(time);
		s = t%60;
		string = s+string;
		if (s<10){
			string = "0"+string;
		}
		t = t/60; m =(t%60);
		string = m+":"+string;
		if (m<10){
			string = "0"+string;
		}
		t = t/60;
		return t+":"+string;
	}
}			
