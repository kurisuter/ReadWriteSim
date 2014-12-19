package jus.poc.rw.v4;

import java.util.concurrent.Semaphore;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.control.IObservator;
import jus.poc.rw.deadLock.Detector;
import jus.poc.rw.deadlock.DeadLockException;

public class YRessource implements IResource{

	Semaphore readWrite = new Semaphore(1);
	Semaphore Mutex = new Semaphore(1);
	int nbLect = 0;
	
	int r=0;
	int w=0;
	private int ident;
	private IObservator obs;
	private static int _ident;
	private Detector detector;
	
	public YRessource(Detector d, IObservator obse)
	{
		this.ident = YRessource._ident;
		YRessource._ident++;
		this.obs = obse;
		this.detector = d;
	}
	
	@Override
	public void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		try{detector.waitResource(arg0, this);}
		catch(DeadLockException e){throw e;}
		Mutex.acquire(); //on acquire le mutex pour eviter de changer les variables en paralele

		if(nbLect == 0) //si aucun lecteur est present
			readWrite.acquire(); //on fait un acquire sur le semaphore commun pour le writer et les lecteurs
			//bloquant si un writer est present, et comme on a le mutex, bloquant pour tout les reader qui demande la ressource
		obs.acquireResource(arg0, this);
		detector.useResource(arg0, this);
		nbLect++; //on incremente le nombre de reader
		
		Mutex.release(); //on libere le mutex
	}

	@Override
	public void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		obs.requireResource(arg0, this);
		try{detector.waitResource(arg0, this);}
		catch(DeadLockException e){throw e;}
		readWrite.acquire(); //on acquire le semaphore commun, bloquant si il y a des readers
		obs.acquireResource(arg0, this);
		detector.useResource(arg0, this);
	}

	@Override
	public void endR(Actor arg0) throws InterruptedException {
		Mutex.acquire();
		obs.releaseResource(arg0, this);
		detector.freeResource(arg0, this);
		nbLect--; //on decremente le nombre de lecteur
		if(nbLect==0)
			readWrite.release(); //si il n'y a plus de reader on doit lacher le semaphore pour permetre au writer de la prendre
		Mutex.release();
	}

	@Override
	public void endW(Actor arg0) throws InterruptedException {
		obs.releaseResource(arg0, this);
		detector.freeResource(arg0, this);
		readWrite.release(); //on lache tout simplement le writer
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
	
	public int identifiant()
	{
		return ident;
	}
	public String toString()
	{
		return "n°"+ident;
	}
}
