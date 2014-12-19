package jus.poc.rw;

import jus.poc.rw.Actor;
import jus.poc.rw.Aleatory;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;

public class Reader extends Actor {

	private static int _ident = 0;
	private int ident;
	public Reader(Aleatory useLaw, Aleatory vacationLaw, Aleatory iterationLaw,
			IResource[] selection, IObservator observator) {
		super(useLaw, vacationLaw, iterationLaw, selection, observator);
		this.ident = Reader._ident;
		Reader._ident++;
	}

	@Override
	protected void acquire(IResource resource) throws InterruptedException,
			DeadLockException {
		try{resource.beginR(this);}
		catch(DeadLockException e){throw e;}
	}

	@Override
	protected void release(IResource resource) throws InterruptedException {
		resource.endR(this);
	}

	public int Identifiant()
	{
		return ident;
	}
	public String toString()
	{
		return "Reader	" + ident; 
	}
}
