package jus.poc.rw.deadLock;

import jus.poc.rw.Actor;
import jus.poc.rw.IResource;
import jus.poc.rw.deadlock.DeadLockException;
import jus.poc.rw.deadlock.IDetector;
import jus.poc.rw.v4.YRessource;

import java.util.*;

public class Detector implements IDetector{ 
	//indice du premier writer
	int firstWriter;
	
	// tableau des actor
	Actor[] actor;
	// tableau des ressources
	YRessource[] resource;
	 
	int nbResource;
	int nbActor;
	// liste d'attente de chaque ressource, [x][y] == 1 si l'acteur y attend la ressource x 
	Boolean[][] Ressource_attendu_Actor;
	// liste des ressource utiliser de chaque actor, [x][y] == 1 si la ressource y est utiliser par l'acteur x
	Boolean[][] Actor_utilise_Ressource;
	
	public Detector() {
	}
	
	/**
	 * Detect renvoie vrai si il y un cycle dans le graph des dependances.
	 * On crée pour cela les differentes arrete du graphe orienté
	 * ensuite on vérifie qu'il n'y a pas de cycle
	 * par manque de temps cette methode est loin d'etre optimal.
	 */
	public boolean Detect(){
		
		ArrayList<Integer> gauche = new ArrayList<Integer>();
		ArrayList<Integer> droite = new ArrayList<Integer>();
		 
		//pour tout les writer
		for(int i =firstWriter; i<nbActor; i++)
			for(int j=0; j<nbResource; j++) 
				if(Actor_utilise_Ressource[i][j]) 
					for(int h=0; h<nbActor; h++) 
						if(Ressource_attendu_Actor[j][h]) 
							{
							gauche.add(i);
							droite.add(h);
							}
		//pour tout les reader
		for(int i=0; i<firstWriter;i++)
			for(int j=0; j<nbResource;j++)
				if(Actor_utilise_Ressource[i][j])
					for(int h=firstWriter; h<nbActor;h++)
						if(Ressource_attendu_Actor[j][h])
							{
							gauche.add(i);
							droite.add(h);
							}
		
		//on cherche un couple (u,v) tq il existe un chemin de u à v et de v à u
		int indice =0;
		int dernier_indice = droite.size();
		while(indice < dernier_indice)
		{
			int x = droite.get(indice);
			int y = gauche.get(indice);
			for(int i=indice+1; i<dernier_indice;i++)
			{
				if(droite.get(i).intValue() == y && gauche.get(i).intValue() == x)
				{
					return true;
				}
			}
			indice++;
		}
		return false;
	}
	/**
	 * firstR < firstW
	 */
	public void init(Actor[] actor, IResource[] resources, int firstW){
		this.actor = actor;
		this.firstWriter=firstW;
		this.nbActor = actor.length;
		this.nbResource = resources.length;
		this.Ressource_attendu_Actor = new Boolean[nbResource][nbActor];
		this.Actor_utilise_Ressource =  new Boolean[nbActor][nbResource];
		
		for(int i=0; i<nbActor;i++)
			for(int j=0; j<nbResource;j++)
				Actor_utilise_Ressource[i][j] = false;
		for(int i=0; i<nbResource;i++)
			for(int j=0; j<nbActor;j++)
				Ressource_attendu_Actor[i][j] = false;
		
	}
	@Override
	public void freeResource(Actor arg0, IResource arg1) {
		Actor_utilise_Ressource[arg0.ident()][arg1.ident()] = false;
	}

	@Override
	public void useResource(Actor arg0, IResource arg1) {
		Actor_utilise_Ressource[arg0.ident()][arg1.ident()] = true;
		Ressource_attendu_Actor[arg1.ident()][arg0.ident()] = false;
	}

	@Override
	public void waitResource(Actor arg0, IResource arg1)
			throws DeadLockException {
		Ressource_attendu_Actor[arg1.ident()][arg0.ident()] = true;
		if(Detect())
		{
			arg0.setRessourcePlantante(arg1);			
			throw new DeadLockException(arg0,arg1);
		}
	}

}
