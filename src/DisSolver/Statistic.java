package DisSolver;

public class Statistic {
	int MSGs;
	int Cstrs;
	int CCCs;
	int time;
	
	public Statistic()
	{
		MSGs = 0;
		Cstrs = 0;
		time = 0;
	}
	
	public Statistic(String statistics)
	{
//		agent = statistics.split("\\;")[0];
		MSGs = Integer.parseInt(statistics.split("\\;")[0]);
		Cstrs = Integer.parseInt(statistics.split("\\;")[1]);
	}
	
	public int getMSGs()
	{
		return MSGs;
	}
	
	public int getCstrs()
	{
		return Cstrs;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public void setMSGs(int msgs)
	{
		this.MSGs = msgs;
	}
	
	public void setCstrs(int cstrs)
	{
		this.Cstrs = cstrs;
	}
	
	public void setTime(int time)
	{
		this.time = time;
	}
	
	public void IncrementMSGs()
	{
		this.MSGs++;
	}
	
	public void IncrementCstrs()
	{
		this.Cstrs++;
	}
	
	public void IncrementCstrs(int nbrCstrs)
	{
		this.Cstrs += nbrCstrs;
	}
	
	public String toString()
	{
		return " MSGs : "+this.getMSGs()+"  -  Cstrs : "+this.getCstrs();
	}
}
