package jus.poc.rw.v1;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.ObservateurMadeInRICM;
import jus.poc.rw.deadlock.DeadLockException;

public class KResource implements IResource{

	private ReentrantReadWriteLock lock;
	private static int _ident=0;
	private int ident;
	public KResource()
	{
		lock = new ReentrantReadWriteLock(true);
		this.ident = KResource._ident;
		KResource._ident++;
	}
	
	@Override
	public synchronized void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		ObservateurMadeInRICM.pereVert.requireResource(arg0, this);
		this.lock.readLock().lock();
		ObservateurMadeInRICM.pereVert.acquireResource(arg0, this);
		
	}

	@Override
	public synchronized void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		ObservateurMadeInRICM.pereVert.requireResource(arg0, this);
		this.lock.writeLock().lock();
		ObservateurMadeInRICM.pereVert.acquireResource(arg0, this);
		
	}

	@Override
	public synchronized void endR(Actor arg0) throws InterruptedException {
		ObservateurMadeInRICM.pereVert.releaseResource(arg0, this);
		this.lock.readLock().unlock();
		
	}

	@Override
	public synchronized void endW(Actor arg0) throws InterruptedException {
		ObservateurMadeInRICM.pereVert.releaseResource(arg0, this);
		this.lock.writeLock().unlock();
		
	}

	@Override
	public int ident() {
		return this.ident;
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
