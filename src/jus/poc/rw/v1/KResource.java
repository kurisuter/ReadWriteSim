package jus.poc.rw.v1;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;
import jus.poc.rw.deadlock.IDetector;

public class KResource implements IResource{
	private ReentrantReadWriteLock lock;
	/**
	 * pour la numerotation de a ressource
	 */
	private static int _ident=0;
	private int ident;
	
	//observateur
	private IObservator obs;
	
	public KResource(IDetector detector, IObservator observator) {
		//on instancie notre reentrant read write lock avec une politique fair
 		lock = new ReentrantReadWriteLock(true);
		this.ident = KResource._ident;
		KResource._ident++;
		obs = observator;
	}
	
	/**
	 * On fait juste le lock du readLock qui respect les contrainte demande
	 */
	@Override
	public void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		this.lock.readLock().lock();
		obs.acquireResource(arg0, this);
		
	}

	/**
	 * On fait juste le lock du writeLock qui respect les contrainte demande
	 */
	@Override
	public void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		this.lock.writeLock().lock();
		obs.acquireResource(arg0, this);
		
	}

	/**
	 * On fait juste le unlock du readLock qui respect les contrainte demande
	 */
	@Override
	public void endR(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		this.lock.readLock().unlock();
		
	}
	
	/**
	 * On fait juste le unlock du writeLock qui respect les contrainte demande
	 */
	@Override
	public void endW(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		this.lock.writeLock().unlock();
		
	}

	@Override
	public int ident() {
		return this.ident;
	}

	@Override
	public void init(Object arg0) throws UnsupportedOperationException {
	}
	
	//pour l'affichage
	public String toString()
	{
		return "n°"+ident;
	}
	

}
