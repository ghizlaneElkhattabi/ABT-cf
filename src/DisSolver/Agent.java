package DisSolver;

public class Agent 
{
	public static void main(String[] args) {
		DisSolver ds = new DisSolver();
		DisSolver ds1 = new DisSolver();
		
		int n = 5;
		ds.setType("MasterABT");
		ds.setNumberOfAgents(n);
		ds.setGui(false);
		ds.run();
		
		ds1.setType("AgentABT");
		String AgentName="A";
		for(int i=1;i<=n;i++)
		{
			ds1.addAgent(AgentName+""+i,"A"+i+".xml",AgentName,30);
		}
		ds1.setContainer("localhost");
		ds1.run();
	}
}

