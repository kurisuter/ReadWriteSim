/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.poc.rw;

import java.util.LinkedList;
import java.util.Random;

import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;

/**
 * Define the gobal behavior of an actor in Reader/writer protocole.
 * @author morat 
 */
public abstract class Actor extends Thread{
	
	private static int identGenerator=0;
	/** the identificator of the actor */
	protected int ident;
	/** le pool of resources to be used */
	protected IResource[] resources;
	/** the ramdomly service for use delay*/
	protected Aleatory useLaw;
	/** the ramdomly service for vacation delay*/
	protected Aleatory vacationLaw;
	/** the observator */
	protected IObservator observator;
	/** the number of iteration to do, -1 means infinity */
	protected int nbIteration;
	/** the rank of the last access done or under execution */
	protected int accessRank;
	/** the ramdomly quantity of ressource to acces */
	protected Random accessLaw;
	/** nombre de ressource pris sur l'instance */
	protected int nbRessource;
	
	protected IResource resourcePlantante;
	
	public void setRessourcePlantante(IResource r)
	{
		resourcePlantante = r;
	}
	/**
	 * Constructor
	 * @param useLaw the gaussian law for using delay
	 * @param vacationLaw the gaussian law for the vacation delay
	 * @param iterationLaw the gaussian law for the number of iteration do do
	 * @param selection the resources to used
	 * @param observator th observator of the comportment
	 */
	public Actor(Aleatory useLaw, Aleatory vacationLaw, Aleatory iterationLaw, IResource[] selection, IObservator observator){
		this.ident = identGenerator++;
		resources = selection;
		this.useLaw = useLaw;
		this.vacationLaw = vacationLaw;
		nbIteration=iterationLaw.next();
		setName(getClass().getSimpleName()+"-"+ident());
		this.observator=observator;
		this.accessLaw = new Random();
	}
	/**
	 * the behavior of an actor accessing to a resource.
	 */
	public void run(){
		// to be completed
		for(accessRank=1; accessRank!=nbIteration; accessRank++) {
			temporizationVacation(vacationLaw.next());
			try {acquire();} 
				catch (InterruptedException e) {e.printStackTrace();} 
				catch (DeadLockException e) {accessRank=1; this.restart(resourcePlantante);}
			try {Thread.sleep(30);}
				catch (InterruptedException e1) {e1.printStackTrace();}
			temporizationUse(useLaw.next());
			try {release();} 
				catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	/**
	 * the temporization for using the ressources.
	 */
	private void temporizationUse(int delai) {
		try{Thread.sleep(delai+2000);}catch(InterruptedException e1){e1.printStackTrace();}		
	}
	/**
	 * the temporization between access to the resources.
	 */
	private void temporizationVacation(int delai) {
		try{Thread.sleep(delai);}catch(InterruptedException e1){e1.printStackTrace();}		
	}
	/**
	 * the acquisition stage of the resources.
	 * @throws DeadLockException 
	 * @throws InterruptedException 
	 */
	private void acquire() throws InterruptedException, DeadLockException{
		Simulator.mixe(resources);
		nbRessource = accessLaw.nextInt(resources.length+1);
		for(int i=0; i< nbRessource;i++)
		{
			try {
				acquire(resources[i]);				
			} catch (DeadLockException e) { 
				throw e;
			}

			//try{Thread.sleep(2000);}catch(InterruptedException e1){System.out.println("\nFATALITY\n");e1.printStackTrace();}
		}
	}
	/**
	 * the release stage of the resources prevously acquired
	 * @throws InterruptedException 
	 */
	private void release() throws InterruptedException{
		for(int i =0; i< nbRessource; i++)
		{
			release(resources[i]);
		}
	}
	/**
	 * Restart the actor at the start of his execution, having returned all the resources acquired.
	 * @param resource the resource at the origin of the deadlock.
	 */
	protected void restart(IResource resource) {
		System.out.println("Restart !!");
		int i =-1;
		do
		{
			i++;
			try {this.release(resources[i]);} catch (InterruptedException e) {e.printStackTrace();}
		}while(this.resources[i] != resource);
	}
	/**
	 * acquisition proceeding specific to the type of actor.
	 * @param resource the required resource
	 * @throws InterruptedException
	 * @throws DeadLockException
	 */
	protected abstract void acquire(IResource resource) throws InterruptedException, DeadLockException;
	/**
	 * restitution proceeding specific to the type of actor.
	 * @param resource
	 * @throws InterruptedException
	 */
	protected abstract void release(IResource resource) throws InterruptedException;
	/**
	 * return the identification of the actor
	 * return the identification of the actor
	 */
	public final int ident(){return ident;}
	/**
	 * the rank of the last access done or under execution.
	 * @return the rank of the last access done or under execution
	 */
	public final int accessRank(){return accessRank;}
}