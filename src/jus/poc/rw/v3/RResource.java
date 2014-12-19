package jus.poc.rw.v3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;

/**
 * Priorité faible pour les lecteurs
 * 
 * Description :
 * 		Un writer ne peut prendre la ressource que si aucun reader ne la demande et est en attente
 * ce qui arrive lorsqu'un writer a pris la resource
 *
 */
public class RResource implements IResource{
	
	
	private static int _ident=0;
	private int ident;
	private IObservator obs;
	
	//nombre de reader présent
	private int readerPresent=0;
	//boolean si writer présent
	private boolean writerPresent=false;
	
	//le lock commun aux writer et aux reader 
	private final Lock lock = new ReentrantLock();
	//Condition pour le reader
	private Condition cReader = lock.newCondition();
	//Condition pour le writer
	private Condition cWriter = lock.newCondition();
	
	public RResource(IObservator obse)
	{
		this.ident = RResource._ident;
		RResource._ident++;
		this.obs = obse;
	}
	
	/**
	 * On acquire le lock pour eviter que les variables soit modifier par plusieurs thread en paralele
	 * on signal que un reader est présent, pour qu'il bloque les writer, meme si il n'acquiere pas la resource
	 * ensuite 	si un writer est present alors on fait un await sur la condition des readers
	 * 			sinon on on lache le lock
	 * On peut mettre un if, car on sait que lorsque le reader sera reveiller, un writer ne pourra pas modifier
	 * la valeur de writerPresent car la valeur de readerPresent a était incrementer, et donc le writer
	 * sera bloqué 
	 */
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

	/**
	 * 
	 */
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
