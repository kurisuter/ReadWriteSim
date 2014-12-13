package jus.poc.rw.v2;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.ObservateurMadeInRICM;
import jus.poc.rw.deadlock.DeadLockException;

public class LResource implements IResource {

	//nombre de lecteur qui a pu lire la derniere donnée
	private int nombreDeLecture;
	//nombre minimal de lecteur qui doit lire la donée 
	private int lectureMinimal;
	//nombre de reader présent
	private int readerPresent;
	//boolean si writer présent
	private boolean writer;
	
	private static int _ident=0;
	private int ident;
	
	public LResource(int lectureMinimal)
	{
		this.nombreDeLecture = 0;
		this.lectureMinimal = lectureMinimal;
		this.readerPresent = 0;
		this.writer = false;
		this.ident = LResource._ident;
		LResource._ident++;
	}
	
	@Override
	public synchronized void beginR(Actor arg0) throws InterruptedException,
			DeadLockException {
		ObservateurMadeInRICM.pereVert.requireResource(arg0, this);
		while(writer)
		{
			wait();
		}
		nombreDeLecture++;
		readerPresent++;
		ObservateurMadeInRICM.pereVert.acquireResource(arg0, this);
		
	}
	@Override
	public synchronized void beginW(Actor arg0) throws InterruptedException,
			DeadLockException {
		ObservateurMadeInRICM.pereVert.requireResource(arg0, this);
		while(readerPresent>0 && writer && nombreDeLecture < lectureMinimal)
		{
			wait();
		}
		writer = true;
		ObservateurMadeInRICM.pereVert.acquireResource(arg0, this);
		
	}
	@Override
	public synchronized void endR(Actor arg0) throws InterruptedException {
		ObservateurMadeInRICM.pereVert.releaseResource(arg0, this);
		readerPresent--;
		notify();
		
	}
	@Override
	public synchronized void endW(Actor arg0) throws InterruptedException {
		ObservateurMadeInRICM.pereVert.releaseResource(arg0, this);
		writer = false;
		nombreDeLecture = 0;
		notifyAll();
		
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
