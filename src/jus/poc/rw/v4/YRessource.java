package jus.poc.rw.v4;

import java.util.concurrent.Semaphore;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadLock.Detector;
import jus.poc.rw.deadlock.DeadLockException;

public class YRessource implements IResource{

	Semaphore readWrite = new Semaphore(1);
	Semaphore Mutex = new Semaphore(1);
	int nbLect = 0;
	
	int r=0;
	int w=0;
	private int ident;
	private IObservator obs;
	private static int _ident;
	private Detector detector;
	
	public YRessource(Detector d, IObservator obse)
	{
		this.ident = YRessource._ident;
		YRessource._ident++;
		this.obs = obse;
		this.detector = d;
	}
	
	@Override
	public void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		try{detector.waitResource(arg0, this);}
		catch(DeadLockException e){throw e;}
		Mutex.acquire();

		if(nbLect == 0)
			readWrite.acquire();
		obs.acquireResource(arg0, this);
		detector.useResource(arg0, this);
		nbLect++;
		
		Mutex.release();
	}

	@Override
	public void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		try{detector.waitResource(arg0, this);}
		catch(DeadLockException e){throw e;}
		readWrite.acquire();
		obs.acquireResource(arg0, this);
		detector.useResource(arg0, this);
	}

	@Override
	public void endR(Actor arg0) throws InterruptedException {
		Mutex.acquire();
		obs.releaseResource(arg0, this);
		detector.freeResource(arg0, this);
		nbLect--;
		if(nbLect==0)
			readWrite.release();
		Mutex.release();
	}

	@Override
	public void endW(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		detector.freeResource(arg0, this);
		readWrite.release();
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
	
	public int identifiant()
	{
		return ident;
	}
	public String toString()
	{
		return "n°"+ident;
	}
}
