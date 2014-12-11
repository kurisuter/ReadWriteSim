package jus.poc.rw.v2;

import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;
import com.sun.swing.internal.plaf.synth.resources.synth;

import jus.poc.rw.Aleatory;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;

public class Writer extends Actor{
	
	
	public Writer(Aleatory useLaw, Aleatory vacationLaw, Aleatory iterationLaw,
			IResource[] selection, IObservator observator) {
		super(useLaw, vacationLaw, iterationLaw, selection, observator);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected synchronized void acquire(IResource resource) throws InterruptedException,
			DeadLockException {
		while(nbreader>0 && writer && nbreader < min)
		{
			wait();
		}
		writer = true;
	}

	@Override
	protected synchronized void release(IResource resource) throws InterruptedException {
		writer = false;
		nbreader = 0;
		notifyAll();
	}

}
