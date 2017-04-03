package myUtil;

/**
 * Class of Exponential distributions
 * @author Chung-Chih Li
 *
 */
public class ExpDistribution {
	private double theta;
	
	/**
	 * @param theta is the mean of this distribution
	 */
	public ExpDistribution(double theta) {
		this.theta = theta;
	}
	
	/**
	 *  This is the density function
	 * @param x
	 * @return density at y
	 */
	public double pdf(double x) {
		return (1/Math.exp(x/theta))/theta;
	}
	
	/**
	 * @param x
	 * @return the probability that there is no event before time x 
	 */
	public double no(double x) {
		return 1/Math.exp(x/theta);
	}
	
	/**
	 * Time to next random event  
	 * @return a random waiting time
	 */
	public double next() {	
		double p = Math.random();
		return Math.log(1-p)*(-theta);
	}
}