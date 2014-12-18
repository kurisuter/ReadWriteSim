package jus.poc.rw.v2;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;
import jus.poc.rw.deadlock.IDetector;

public class LResource implements IResource {

	//nombre de lecteur qui a pu lire la derniere donnée
	private int nombreDeLecture;
	//nombre minimal de lecteur qui doit lire la donée 
	private int lectureMinimal;
	//nombre de reader présent
	private int readerPresent;
	//boolean si writer présent
	private boolean writerPresent;
	
	private static int _ident=0;
	private int ident;
	private IObservator obs;
	
	public LResource(IDetector detector, IObservator observator,int lectureMinimal)
	{
		this.nombreDeLecture = 0;
		this.lectureMinimal = lectureMinimal;
		this.readerPresent = 0;
		this.writerPresent = false;
		this.ident = LResource._ident;
		LResource._ident++;
		obs = observator;
	}
	
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
	@Override
	public synchronized void endR(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		readerPresent--;
		notify();
		
	}
	@Override
	public synchronized void endW(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		writerPresent = false;
		nombreDeLecture = 0;
		notifyAll();
		
	}
	@Override
	public int ident() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void init(Object arg0) throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}
	
	public String toString()
	{
		return "n°"+ident;
	}
	
	
}
