package jus.poc.rw;

public class Controleur {

	private int nbWritter=0;
	private int nbReader=0;
	
	public void writterInc()
	{
		nbWritter++;
		testCoherance();
	}
	public void writterFar()
	{
		nbWritter--;
		testCoherance();
	}
	public void readerInc()
	{
		nbReader++;
		testCoherance();
	}
	public void readerFar()
	{
		nbReader--;
		testCoherance();
	}
	private void testCoherance()
	{
		if (false)
		if ((nbWritter>0 && nbReader >0) || nbWritter>1 || nbWritter<0 || nbReader<0 ) 
		{
			System.out.println(
					nbWritter + nbReader +					
					"\n\nShoryu   Ken  !!!!!!!!!!!!!!!!!!!!!!!\n "+
					"┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬███┬\n" +
					"┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬███┬┴\n" +
					"┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬████┴┬┴┬\n" +
					"┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴█┴┬█┬┴┬┴┬┴┬┴┬██┴┬┴┬┴┬┴\n" +
					"┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬█┬┴██┬┴┬┴┬┴┬██┴┬┴┬┴┬┴┬\n" +
					"█┬┴┬┴┬┴┬█┬┴┬┴┬┴█┴███┴██┬┴┬███┬┴┬┴█┴┬┴\n" +
					"██┬┴┬┴┬┴██┬┴┬┴┬█┬█████┬┴┬███┬┴┬┴██┬┴┬\n" +
					"┴███┴┬┴┬██┴┬┴███┴████┬┴██████┬┴██┬┴┬┴\n" +
					"┬┴███┴┬┴█┴┬┴███┴████████████████┬┴┬┴┬\n" +
					"┴┬┴┬██┴┬██┴┬██████   ███    ████┴┬┴┬█\n" +
					"┬█┬┴┬█┬┴┬████████     ██     ███┬┴┬██\n" +
					"┴██┬┴███████████             ███████┴\n" +
					"┬██┴┬┴█████                   █████┴┬\n" +
					"┴┬█████████                   ████┴┬┴\n" +
					"┬┴┬████               ██      ███┴┬┴┬\n" +
					"┴█████                ██      ██┴┬┴┬┴\n" +
					"██████          ██    ██      ███████\n" +
					"┴█████    ██    ██    ██       █████┴\n" +
					"┬████     ██    ██     ██      ██┴┬┴┬\n" +
					"┴████     ██    ██     ██      ██┬┴┬┴\n" +
					"┬█████    ██    ██     ██      ███┬┴┬\n" +
					"┴┬████    ██    ██     ██      █████┴\n" +
					"┬█████    ██    ██     ██    ███┬┴███\n" +
					"███┬███   ██    ██     ████ ████┴┬┴┬┴\n" +
					"██┬┴████  ███   ████ ████████████┴┬┴┬\n" +
					"█┬┴┬┴┬██ █████ ████████      ██┬██┴┬┴\n" +
					"┬┴┬┴┬┴┬█████████████  ███    ██┴┬┴┬┴┬\n" +
					"┴┬┴┬┴┬███┬██┴█████████    ████┴┬┴┬┴┬┴\n" +
					"┬┴┬┴┬┴██┬██┴┬███┬┴┬█████████┬┴┬┴┬┴┬┴┬\n" +
					"┴┬┴┬┴┬┴┬┴██┬┴┬█┬┴┬┴██┬┴┬┴┬██┴┬┴┬┴┬┴┬┴\n" +
					"┬┴┬┴┬┴┬┴┬█┬┴████┬┴┬██┴┬┴┬┴┬██┴┬┴┬┴┬┴┬\n" +
					"┴┬┴┬┴┬┴┬┴┬┴┬████┴┬┴██┬┴┬┴┬┴┬█┬┴┬┴┬┴┬┴\n" +
					"┬┴┬┴┬┴┬┴┬┴┬┴┬┴██┬┴██┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬\n" +
					"┴┬┴┬┴┬┴┬┴┬┴┬┴┬██┴┬█┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴\n" +
					"┬┴┬┴┬┴┬┴┬┴┬┴┬███┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬\n" +
					"┴┬┴┬┴┬┴┬┴┬┴┬┴█┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴┬┴\n"
			);
			
		}
	}
}
