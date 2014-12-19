package jus.poc.rw.v2;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;
import jus.poc.rw.deadlock.IDetector;

public class LResource implements IResource {

	//nombre de lecteur qui a pu lire la derniere donnée
	private int nombreDeLecture=0;
	//nombre minimal de lecteur qui doit lire la donée 
	private int lectureMinimal;
	//nombre de reader présent
	private int readerPresent=0;
	//boolean si writer présent
	private boolean writerPresent;
	/**
	 * pour la numerotation des ressource
	 */
	private static int _ident=0;
	private int ident;
	private IObservator obs;
	
	public LResource(IDetector detector, IObservator observator,int lectureMinimal)
	{
		//initialisation des variables
		this.lectureMinimal = lectureMinimal;
		this.writerPresent = false;
		this.ident = LResource._ident;
		LResource._ident++;
		obs = observator;
	}
	
	/**
	 * Tant que des writer sont present on attend
	 * Si il n'y a pas de writer, alors on incrémente le nombre de lecture ainsi que le nombre de reader present
	 * on garde un while car lorsque le processus est reveiller, c'est par un notifyAll, donc un writer a aussi pu etre reveiller
	 */
	@Override
	public synchronized void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		while(writerPresent)
		{
			wait();
		}
		nombreDeLecture++;
		readerPresent++;
		obs.acquireResource(arg0, this);
		
	}
	/**
	 * tant que un writer ou des readers sont present ou bien que le nombre de lecture de la ressource est inferieur a la lecture minimal
	 * on attend.
	 * Sinon, on indique qu'un writer est présent
	 * On garde le while, car un writer peut etre reveiller mais un reader peut entrer en zone critique avant le writer
	 */
	@Override
	public synchronized void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		while(readerPresent>0 || writerPresent || nombreDeLecture < lectureMinimal)
		{
			wait();
		}
		writerPresent = true;
		obs.acquireResource(arg0, this);
		
	}
	/**
	 * lorsqu'un reader lache la ressource, on reveille un thread, qui sera un writer, que si le nombre de reader present est nul
	 */
	@Override
	public synchronized void endR(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		readerPresent--;
		if(readerPresent ==0)
			notify();
		
	}
	/**
	 * lorsqu'un writer lache la ressource, on reveille tout les thread, il peut y avoir plusieurs reader qui attendent
	 * et endR ne fait qu'un notify, ca pourrai marché car il serai tjs le seul, mais ca n'aurai alors pas vraiment d'interet de faire des thread...
	 */
	@Override
	public synchronized void endW(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		writerPresent = false;
		nombreDeLecture = 0;
		notifyAll();
		
	}
	@Override
	public int ident() {
		return 0;
	}
	@Override
	public void init(Object arg0) throws UnsupportedOperationException {
		
	}
	
	public String toString()
	{
		return "n°"+ident;
	}
	
	
}
