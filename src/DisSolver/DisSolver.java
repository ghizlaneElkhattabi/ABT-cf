package DisSolver;
import java.text.BreakIterator;
import java.util.ArrayList;

import choco.cp.solver.constraints.global.automata.fast_multicostregular.valselector.MCRValSelector;

import jade.Boot;
import jade.MicroBoot;
import jade.core.Agent;
import jade.tools.sniffer.Sniffer;
import jade.util.leap.List;


public class DisSolver {
    String AgentType;
    ArrayList<String> Agents;
    Boot b;
    MicroBoot Mb;
    Sniffer s;
    String[] args;
    boolean mainc = true;
    int nbrOfAgents = 0;
    public DisSolver() 
    {
		// TODO Auto-generated constructor stub
    	s = new Sniffer();
    	Agents = new ArrayList<String>();
    	args = new String[2];
    	
	}
    boolean Gui = false;
    
    public void setType(String type)
    {
    	AgentType = type;
    }
    public void setNumberOfAgents(int nbrOfAgents)
    {
    	this.nbrOfAgents = nbrOfAgents;
    }
    
    
    
    public void run()
    {
		
    	if(mainc)
    	{
    		String agent = "MASTER:"+"DisSolver."+AgentType+"("+nbrOfAgents+")";
    		Agents.add(agent);
    		//b.main(args);
    	}
    	else
    	{
    		///Mb.main(args);
    	}
    	getArguments();
    	b.main(args);
    	try {
			this.finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void sniffMaster(boolean b)
    {
    	if(b)
    	Agents.add("SnifferMAster:jade.tools.sniffer.Sniffer(MASTER)");
    }

	public void addAgent(String AgentName,String LocalProblem,String SuffixAgentName,int SilenceTime)
	{
		if(!AgentType.toUpperCase().contains("MASTER"))
		{
		   String agent = AgentName+":"+"DisSolver."+AgentType+"("+LocalProblem+","+SuffixAgentName+","+SilenceTime+")";
		   Agents.add(agent);
		}
	}
	
	public void setSniffer(boolean stat)
	{
		//s.setup();
	}
	
	public String getAllAgent()
	{
		String ret = "";
		for(String agent : Agents)
		{
			ret = ret+agent + ";";
			//System.out.println(agent);
		}
		return ret;
	}
	
	public void setGui(boolean gui)
	{
		Gui = gui;
	}
	
	public void getArguments()
	{
		
		if(Gui)
		{
			
			args[0] = "-gui";
				args[1] = getAllAgent();
				
			
		}
		else
			args[0] = "-agents";
			args[1] = getAllAgent();
		}
	
	
	public int getNbrMessages(){
		MasterABT m = new MasterABT();
		return m.st.getCstrs();
	}
	
	
	public int getNbrConstraints(){
		MasterABT m = new MasterABT();
		return m.st.getCstrs();
	}
	
	public int getNbrMasterMessages()
	{
		MasterABT m = new MasterABT();
		return m.k;
	}
	
	public void setContainer(String IP)
	{
		mainc = false;
		int l = args.length;
		String[] tmp = new String[l+3];
		for(int i = 0 ; i<l ;i++)
		{
			tmp[i] = args[i];
		}
	    tmp[args.length] = "-container";
	    tmp[args.length+1] = "-host";
	    tmp[args.length+2] = IP;
	    args = new String[tmp.length];
	    for(int i = 0 ; i<args.length ;i++)
		{
			args[i] = tmp[i];
		}
		
	}
	
	}
