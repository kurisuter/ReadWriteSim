package jus.poc.rw;

import jus.poc.rw.control.ControlException;
import jus.poc.rw.control.IObservator;

public class ObservateurMadeInRICM implements IObservator{

	public static ObservateurMadeInRICM pereVert = new ObservateurMadeInRICM();
	@Override
	public void acquireResource(Actor arg0, IResource arg1)
			throws ControlException {
		System.out.println(arg0 + 	" acqueri	-> " + arg1);
		
	}

	@Override
	public void init(int arg0, int arg1) throws ControlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseResource(Actor arg0, IResource arg1)
			throws ControlException {
		System.out.println(arg0 + 	" lache 	-> " + arg1);
		
	}

	@Override
	public void requireResource(Actor arg0, IResource arg1)
			throws ControlException {
		System.out.println(arg0 + 	" demande	-> " + arg1);
		
	}

	@Override
	public void restartActor(Actor arg0, IResource arg1)
			throws ControlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startActor(Actor arg0) throws ControlException {
		System.out.println(arg0 + " \"pret a travailler\"");
		
	}

	@Override
	public void stopActor(Actor arg0) throws ControlException {
		System.out.println(arg0 + " : \"travail terminer\"");
		
	}

}
