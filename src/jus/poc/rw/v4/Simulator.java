package jus.poc.rw.v4;

import jus.poc.rw.Actor;
import jus.poc.rw.Aleatory;
import jus.poc.rw.Controleur;
import jus.poc.rw.ObservateurMadeInRICM;
import jus.poc.rw.Reader;
import jus.poc.rw.Writer;
import jus.poc.rw.deadLock.Detector;
import jus.poc.rw.v1.KResource;
import java.util.*;

/**
 * Main class for the Readers/Writers application. This class firstly creates a pool of read/write resources  
 * implementing interface IResource. Then it creates readers and writers operating on these resources.
 * @author P.Morat & F.Boyer
 */
public class Simulator{
	
	protected static final String OPTIONFILENAME = "option.xml";
	/** the version of the protocole to be used */
	protected static String version;
	/** the number of readers involve in the simulation */
	protected static int nbReaders;
	/** the number of writers involve in the simulation */
	protected static int nbWriters;
	/** the number of resources involve in the simulation */
	protected static int nbResources;
	/** the number of resources used by an actor */
	protected static int nbSelection;
	/** the law for the reader using delay */
	protected static int readerAverageUsingTime;
	protected static int readerDeviationUsingTime;
	/** the law for the reader vacation delay */
	protected static int readerAverageVacationTime;
	protected static int readerDeviationVacationTime;
	/** the law for the writer using delay */
	protected static int writerAverageUsingTime;
	protected static int writerDeviationUsingTime;
	/** the law for the writer vacation delay */
	protected static int writerAverageVacationTime;
	protected static int writerDeviationVacationTime;
	/** the law for the writer number of iterations */
	protected static int writerAverageIteration;
	protected static int writerDeviationIteration;
	/** the chosen policy for priority */
	protected static String policy;
	/**
	 * make a permutation of the array
	 * @param array the array to be mixed
	 */
	protected static void mixe(Object[] array) {
		int i1, i2;
		Object a;
		for(int k = 0; k < 2 * array.length; k++){
			i1 = Aleatory.selection(1, array.length)[0];
			i2 = Aleatory.selection(1, array.length)[0];
			a = array[i1]; array[i1] = array[i2]; array[i2] = a;
		}
	}
	/**
	 * Retreave the parameters of the application.
	 * @param file the final name of the file containing the options. 
	 */
	protected static void init(String file) {
		// retreave the parameters of the application
		final class Properties extends java.util.Properties {
			private static final long serialVersionUID = 1L;
			public int get(String key){return Integer.parseInt(getProperty(key));}
			public Properties(String file) {
				try{
					loadFromXML(ClassLoader.getSystemResourceAsStream(file));
				}catch(Exception e){e.printStackTrace();}			
			} 
		}
		Properties option = new Properties("jus/poc/rw/options/"+file);
		version = option.getProperty("version");
		nbReaders = Math.max(0,new Aleatory(option.get("nbAverageReaders"),option.get("nbDeviationReaders")).next());
		nbWriters = Math.max(0,new Aleatory(option.get("nbAverageWriters"),option.get("nbDeviationWriters")).next());
		nbResources = Math.max(0,new Aleatory(option.get("nbAverageResources"),option.get("nbDeviationResources")).next());
		nbSelection = Math.max(0,Math.min(new Aleatory(option.get("nbAverageSelection"),option.get("nbDeviationSelection")).next(),nbResources));
		readerAverageUsingTime = Math.max(0,option.get("readerAverageUsingTime"));
		readerDeviationUsingTime = Math.max(0,option.get("readerDeviationUsingTime"));
		readerAverageVacationTime = Math.max(0,option.get("readerAverageVacationTime"));
		readerDeviationVacationTime = Math.max(0,option.get("readerDeviationVacationTime"));
		writerAverageUsingTime = Math.max(0,option.get("writerAverageUsingTime"));
		writerDeviationUsingTime = Math.max(0,option.get("writerDeviationUsingTime"));
		writerAverageVacationTime = Math.max(0,option.get("writerAverageVacationTime"));
		writerDeviationVacationTime = Math.max(0,option.get("writerDeviationVacationTime"));
		writerAverageIteration = Math.max(0,option.get("writerAverageIteration"));
		writerDeviationIteration = Math.max(0,option.get("writerDeviationIteration"));
		policy = option.getProperty("policy");
	}
	public static void main(String... args) throws Exception{
		// set the application parameters
		init((args.length==1)?args[0]:OPTIONFILENAME);
		
		
		/*
		 * Les reader comme els writter represente chacun un thread
		 * on instancie donc un thread pour chaque reader et pour chaque writer
		 * que l'on start directement 
		 */
		Actor[] la= new Actor[nbReaders+nbWriters]; 
		Detector d = new Detector();
		Actor a;
		YRessource r[] = new YRessource[2];
		r[0] = new YRessource(d, new ObservateurMadeInRICM(new Controleur()));
		r[1] = new YRessource(d, new ObservateurMadeInRICM(new Controleur()));
		
		for(int i = 0; i < nbReaders; i++)
		{
			YRessource r1[] = new YRessource[2];
			r1[0] = r[0];
			r1[1] = r[1];
			a = new Reader(new Aleatory(0,0),
					new Aleatory(readerAverageVacationTime, readerDeviationVacationTime),
					new Aleatory(readerAverageUsingTime, readerDeviationUsingTime), 
					r1, null);
			la[i]=a;
		}
		//create & run : writters
		for(int i = 0; i < nbWriters; i++)
		{
			YRessource r1[] = new YRessource[2];
			r1[0] = r[0];
			r1[1] = r[1];
			a = new Writer(new Aleatory(0,0),
					new Aleatory(writerAverageVacationTime, writerDeviationVacationTime),
					new Aleatory(writerAverageUsingTime,writerDeviationUsingTime),
					r1, null);
			la[nbReaders+i]=a;
		}
		d.init(la, r);
		for (int i = 0; i<la.length;i++){
			new Thread(la[i]).start();
		}
	}
}