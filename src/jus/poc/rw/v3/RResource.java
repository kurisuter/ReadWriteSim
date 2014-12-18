package jus.poc.rw.v3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;

//priorité faible pour les lecteurs
public class RResource implements IResource{
	
	
	private static int _ident=0;
	private int ident;
	private IObservator obs;
	
	//nombre de reader présent
	private int readerPresent;
	//boolean si writer présent
	private boolean writerPresent;
	
	private final Lock lock = new ReentrantLock();
	private Condition cReader = lock.newCondition();
	private Condition cWriter = lock.newCondition();
	
	
	public RResource(IObservator obse)
	{
		this.readerPresent = 0;
		this.writerPresent = false;
		this.ident = RResource._ident;
		RResource._ident++;
		this.obs = obse;
	}
	
	@Override
	public void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		lock.lock();
		obs.requireResource(arg0, this);
		this.readerPresent++;
		if(writerPresent)
		{
			cReader.await();
		}
		obs.acquireResource(arg0, this);
		lock.unlock();
	}

	@Override
	public void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		lock.lock();
		obs.requireResource(arg0, this);
		while(readerPresent>0 || writerPresent)
		{
			cWriter.await();
		}
		writerPresent=true;
		obs.acquireResource(arg0, this);
		lock.unlock();
	}

	@Override
	public void endR(Actor arg0) throws InterruptedException {
		lock.lock();
		obs.releaseResource(arg0, this);
		this.readerPresent--;
		if(readerPresent==0)
			cWriter.signal();
		lock.unlock();
		
	}

	@Override
	public void endW(Actor arg0) throws InterruptedException {
		lock.lock();
		obs.releaseResource(arg0, this);
		writerPresent=false;
		if(readerPresent>0)
			cReader.signalAll();
		else
			cWriter.signal();
		lock.unlock();
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
