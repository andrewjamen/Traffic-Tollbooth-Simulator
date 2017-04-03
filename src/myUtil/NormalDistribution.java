package myUtil;

/**
 * Class of Normal Distributions
 * @author Chung-Chih Li
 *
 */
public class NormalDistribution {
	private double mu, roh;
	private double xs; // x less than xs is too small to be considered for sampling
	private double xe; // x greater than xe is too big to be considered for sampling	
	private double dx; 
	private double[] dist = new double[50]; 
	private double iI; // the initial segment of the internal 
	
	/**
	 * Normal Distribution 
	 * @param mu is the mean
	 * @param roh is the variance 
	 */
	public NormalDistribution(double mu, double roh) {
		this.mu = mu;
		this.roh = roh; // roh will be the reference to determine the precision. 
						// roh/100,000 will be considered too small for sampling. 
		dx = roh/10000.0;
		
		double x,y = 1/100000.0;

		xs = mu-5*Math.abs(mu); //xs = mu - rpdf(y);  // rpdf is the reverse pdf;
		xe = mu+5*Math.abs(mu); //xe = mu + rpdf(y);
				
		double I=dx*y;
		x = xs;
		iI = 0;
		while (I-iI > 1.0E-25) {
			iI = I;
			x = x-dx;
			I += dx*pdf(x);
		}
		x=xs;
		int i = 1;
		dist[0] = 0;
		while (x<xe) {
			if (I > (double)i/(double)dist.length) 	{
				dist[i] = x;
				i++;
				if (i == dist.length) break;
				
			}
			x += dx;
			I += dx*pdf(x);		
		}
	}
	
	/**
	 * This is the density function
	 * @param x double
	 * @return density at x
	 */
	public double pdf(double x) {
		return 1/roh
		        /Math.pow(2*Math.PI, 0.5)
		        /Math.exp((Math.pow((x-mu)/roh,2))/2);
	}
	
	
	/**
	 * The reverse function of the density function (pdf)
	 * @param y is a double
	 * @return x is a double which is the input of the density function.
	 */
	public double rpdf(double y) {
		return mu + roh*Math.pow(-2*Math.log(y*roh*Math.pow(2*Math.PI,0.5)),0.5);	
	}
	
	
	/**  
	 * A numerical approximation is used
	 * @return  A random sample in this distribution;
	 */
	
	public double sample() {	
		double I, x, p=Math.random();
		// found x that the integral from - infinity to x is bigger than p
		
		int i = (int)(p*50);			
		if (i==0) {
			x = xs;
			I =iI;
		}
		else {				
			x = dist[i];
			I = (double)i/50.0;
		}
		while (I < p && x < xe) {
			x += dx;
			I += dx*pdf(x);
		}
		return x;
	}
	
}