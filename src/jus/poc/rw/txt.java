package jus.poc.rw;

public class txt {

	
	/**
	 * 
	 * 		w							r
	 * 
	 * a  while(R || W || nb != cpt)	while(W) 	
	 *  	wait							wait
	 *    lock()						unlock()
	 * 
	 * 
	 * r  cpt = 0						cpt++;
	 * 	  notifyAll()						notify()
	 * 
	 */
	
}
