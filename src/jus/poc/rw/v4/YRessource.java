package jus.poc.rw.v4;

import java.util.concurrent.Semaphore;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadLock.Detector;
import jus.poc.rw.deadlock.DeadLockException;

public class YRessource implements IResource{

	Semaphore bloqueWriter = new Semaphore(1);
	Semaphore bloqueReader = new Semaphore(1);
	Semaphore MutexR = new Semaphore(1);
	Semaphore MutexW = new Semaphore(1);
	int nbLect = 0;
	
	int r=0;
	int w=0;
	private Object ident;
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
		MutexR.acquire();
		
		obs.requireResource(arg0, this);
		//detector.waitResource(arg0, this);
		bloqueReader.acquire();
		obs.acquireResource(arg0, this);
		//detector.useResource(arg0, this);
		
		if(nbLect == 0)
			bloqueWriter.acquire();
		
		nbLect++;
		
		bloqueReader.release();
		MutexR.release();
	}

	@Override
	public void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		MutexW.acquire();
		obs.requireResource(arg0, this);
		//detector.waitResource(arg0, this);
		bloqueWriter.acquire();
		//detector.useResource(arg0, this);
		obs.acquireResource(arg0, this);
		
		bloqueReader.acquire();
	}

	@Override
	public void endR(Actor arg0) throws InterruptedException {
		MutexR.acquire();
		
		//detector.freeResource(arg0, this);
		obs.releaseResource(arg0, this);
		nbLect--;
		if(nbLect==0)
			bloqueWriter.release();
		
		MutexR.release();
	}

	@Override
	public void endW(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		//detector.freeResource(arg0, this);
		bloqueWriter.release();
		bloqueReader.release();
		MutexW.release();
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
