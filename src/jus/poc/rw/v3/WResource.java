package jus.poc.rw.v3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadlock.DeadLockException;
import jus.poc.rw.deadlock.IDetector;

//priorité faible pour les lecteurs
public class WResource implements IResource{
	
	
	private static int _ident=0;
	private int ident;
	private IObservator obs;
	
	//nombre de readers présent
	private int readerPresent;
	//nombre de writer qui demande la ressource
	private int writterDemande;
	
	private final Lock lock = new ReentrantLock();
	private Condition cReader = lock.newCondition();
	private Condition cWriter = lock.newCondition();
	
	
	public WResource(IDetector detector, IObservator observator)
	{
		this.readerPresent = 0;
		this.writterDemande = 0;
		this.ident = WResource._ident;
		WResource._ident++;
		obs = observator;
	}
	
	/**
	 * On acquire le lock
	 * on verifie qu'aucun writer n'est present, si c'est le cas on fait un wait sur la condition dur reader
	 * sinon on renseigne qu'un reader est present
	 */
	@Override
	public void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		
		lock.lock();
		obs.requireResource(arg0, this);
		//l'utilisation du while est imposé, si un reader est reveiller, entre temps un writter peu l'empecher de continuer
		//on doit donc refaire le test
		while(writterDemande > 0)
		{
			cReader.await();
		}
		obs.acquireResource(arg0, this);
		readerPresent++;
		lock.unlock();
	}

	/**
	 * on recupere le lock
	 * On renseigne directement qu'un writer demande la ressource pour bloquer les readers
	 * On vérifie si un reader est présent, si c'est le cas, on fait un wait sur la condition du writer
	 * sinon, on lache le lock.
	 */
	@Override
	public void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		lock.lock();
		obs.requireResource(arg0, this);
		writterDemande++;
		//on peut mettre un if, ca quand un writter est reveiller alors il peut prendre la ressource, 
		//de plus aucun reader peut changer cette contrainte
		if(readerPresent>0 && writterDemande>1)
		{
			cWriter.await();
		}
		obs.acquireResource(arg0, this);
		lock.unlock();
	}

	/**
	 * a la fin d'un reader, on decremente le nombre de reader
	 * ensuite on regarde si il y a interer a signaler un writer
	 */
	@Override
	public void endR(Actor arg0) throws InterruptedException {
		lock.lock();
		obs.releaseResource(arg0, this);
		readerPresent--;
		// si aucun reader est present sur la ressource, alors si un writer et il peut prendre la ressource, on le reveil
		if(readerPresent == 0 && writterDemande != 0)
			cWriter.signal();
		lock.unlock();
		
	}

	/**
	 * 	si il n'y a pas de writter en attente, on reveil tout les reader
	 * si il y a des writter en attente, on en reveil un, les readers serait imédiatement
	 * remit en attente donc ca sert a rien de les reveiller
	 */
	@Override
	public void endW(Actor arg0) throws InterruptedException {
		lock.lock();
		obs.releaseResource(arg0, this);
		writterDemande--;
		if(writterDemande ==0)
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
