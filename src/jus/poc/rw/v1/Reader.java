package jus.poc.rw.v1;

import jus.poc.rw.Aleatory;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;

public class Reader extends Actor {

	
	public Reader(Aleatory useLaw, Aleatory vacationLaw, Aleatory iterationLaw,
			IResource[] selection, IObservator observator) {
		super(useLaw, vacationLaw, iterationLaw, selection, observator);
	}

	@Override
	protected void acquire(IResource resource) throws InterruptedException,
			DeadLockException {
		Actor.lock.readLock().lock();
	}

	@Override
	protected void release(IResource resource) throws InterruptedException {
		Actor.lock.readLock().unlock();
	}

}
