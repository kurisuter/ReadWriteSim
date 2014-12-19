package jus.poc.rw.deadLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.deadlock.DeadLockException;
import jus.poc.rw.deadlock.IDetector;
import java.util.*;

public class Detector implements IDetector{

	ArrayList<IResource> resources_libres;
	//ArrayList<IResource>  resources_utilise;
	//ArrayList<Actor> acteurs_users;
	//ArrayList<Actor> acteurs_demandant;
	
	public Detector(Actor[] actor, IResource[] resource)
	{
		resources_libres = new ArrayList<IResource>();
		for(int i =0 ; i <resource.length;i++){
			resources_libres.add(resource[i]);
		}
	}
	
	@Override
	public void freeResource(Actor arg0, IResource arg1) {
		// TODO Auto-generated method stub
		resources_libres.add(arg1);
	}

	@Override
	public void useResource(Actor arg0, IResource arg1) {
		arg0.removeWaiting(arg1);
		resources_libres.remove(arg1);
	}

	@Override
	public void waitResource(Actor arg0, IResource arg1)
			throws DeadLockException {
		arg0.addWaiting(arg1);
		
	}

}
