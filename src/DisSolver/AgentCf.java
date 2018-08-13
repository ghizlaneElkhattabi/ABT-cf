package DisSolver;

public class AgentCf 
{
	public static void main(String[] args) {
		DisSolver ds = new DisSolver();
		DisSolver ds1 = new DisSolver();
		
		int n = 20;
		ds.setType("MasterABT");
		ds.setNumberOfAgents(n);
		ds.setGui(false);
		ds.run();
		
		ds1.setType("AgentABT");
		String AgentName="A";
//		ds1.addAgent(AgentName+"1","A1.xml",AgentName,30);
//		ds1.addAgent(AgentName+"2","A2.xml",AgentName,30);
//		ds1.addAgent(AgentName+"3","A3.xml",AgentName,30);
//		ds1.addAgent(AgentName+"4","A4.xml",AgentName,30);
		for(int i=1;i<=n;i++)
		{
			ds1.addAgent(AgentName+""+i,"A"+i+".xml",AgentName,30);
		}
		ds1.setContainer("localhost");
		ds1.run();
	}
}

