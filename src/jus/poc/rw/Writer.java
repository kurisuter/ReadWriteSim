package jus.poc.rw;

import jus.poc.rw.Actor;
import jus.poc.rw.Aleatory;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;

public class Writer extends Actor{
	
	private static int _ident = 0;
	private int ident;
	public Writer(Aleatory useLaw, Aleatory vacationLaw, Aleatory iterationLaw,
			IResource[] selection, IObservator observator) {
		super(useLaw, vacationLaw, iterationLaw, selection, observator);
		this.ident = Writer._ident;
		Writer._ident++;
	}

	@Override
	protected void acquire(IResource resource) throws InterruptedException,
			DeadLockException {
		try{resource.beginW(this);}
		catch(DeadLockException e){throw e;}
	}

	@Override
	protected void release(IResource resource) throws InterruptedException {
		resource.endW(this);
	}

	public int identifiant()
	{
		return ident;
	}
	public String toString()
	{
		return "Writer	" + ident; 
	}
}
