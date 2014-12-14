package jus.poc.rw.v3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.deadlock.DeadLockException;

//priorité faible pour les lecteurs
public class WResource implements IResource{
	
	
	private static int _ident=0;
	private int ident;
	
	//nombre de readers présent
	private int readerPresent;
	//nombre de writer présent et en attente
	private int writerPresent;
	
	private final Lock lock = new ReentrantLock();
	private Condition cReader = lock.newCondition();
	private Condition cWriter = lock.newCondition();
	
	
	public WResource(int lectureMinimal)
	{
		this.readerPresent = 0;
		this.writerPresent = 0;
		this.ident = WResource._ident;
		WResource._ident++;
	}
	
	@Override
	public synchronized void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		while(writerPresent>0)
		{
			cReader.await();
		}
		readerPresent++;
	}

	@Override
	public synchronized void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		writerPresent++;
		while(writerPresent>1 || readerPresent>0)
		{
			cWriter.await();
		}
	}

	@Override
	public synchronized void endR(Actor arg0) throws InterruptedException {
		readerPresent--;
		cWriter.signal();
		
	}

	@Override
	public synchronized void endW(Actor arg0) throws InterruptedException {
		writerPresent--;
		cReader.signalAll();
		cWriter.signal();
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
