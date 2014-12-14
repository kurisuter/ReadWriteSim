package jus.poc.rw.v3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.deadlock.DeadLockException;

//priorité faible pour les lecteurs
public class RResource implements IResource{
	
	
	private static int _ident=0;
	private int ident;
	
	//nombre de reader présent
	private int readerPresent;
	//boolean si writer présent
	private boolean writerPresent;
	
	private final Lock lock = new ReentrantLock();
	private Condition cReader = lock.newCondition();
	private Condition cWriter = lock.newCondition();
	
	
	public RResource(int lectureMinimal)
	{
		this.readerPresent = 0;
		this.writerPresent = false;
		this.ident = RResource._ident;
		RResource._ident++;
	}
	
	@Override
	public synchronized void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		this.readerPresent++;
		while(writerPresent)
		{
			cReader.await();
		}
	}

	@Override
	public synchronized void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		while(readerPresent>0 || writerPresent)
		{
			cWriter.await();
		}
		writerPresent=true;	
	}

	@Override
	public synchronized void endR(Actor arg0) throws InterruptedException {
		this.readerPresent--;
		cWriter.signal();
		
	}

	@Override
	public synchronized void endW(Actor arg0) throws InterruptedException {
		writerPresent=false;
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
