package DisSolver;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleAgent {
	Chocoo LocalCSP; 
	Parser parser;
	String AgentsName;
	String myname ;
	Solution myvalue;
	ArrayList<String> Children;
	HashMap<String, ArrayList<String>> ChildrenVariables;
	HashMap<String, ArrayList<String>> ParentsVariables;
	HashMap<String, ArrayList<jConstraint>> ParentConstraints;
	ArrayList<String> Parent;
	ArrayList<AgentView> AgentView;
	NogoodStore nogoodStore;
	ArrayList<Solution> Domain;
	Nogood Nogood;
	public Statistic statistics;
	int AgentC_Cost;
	
	public SimpleAgent(String myname, String XMLfile)
	{
		parser = new Parser(XMLfile);
		LocalCSP = new Chocoo(XMLfile);
		this.myname = myname;
		AgentsName = myname.substring(0, myname.indexOf(""+Tools.ExtractInteger(myname)));
		myvalue = new Solution();
		Children = parser.getNamesOfAgentsChildren();
		ParentConstraints = parser.getAgentsParent();
		ChildrenVariables = parser.getAgentsChildren();
		ParentsVariables = parser.getAgentsParentsVariables();
		Parent = parser.getNamesOfAgentsParent();
		AgentView = new ArrayList<AgentView>();
		nogoodStore = new NogoodStore();
		Domain = LocalCSP.getSolutions();
		Nogood = new Nogood();
		statistics = new Statistic();
		statistics.setCstrs(LocalCSP.getNbConstraints());
		AgentC_Cost = 0;
	}
	
	public SimpleAgent(String XMLfile)
	{
		parser = new Parser(XMLfile);
		LocalCSP = new Chocoo(XMLfile);
		myvalue = new Solution();
		Children = parser.getNamesOfAgentsChildren();
		ParentConstraints = parser.getAgentsParent();
		ChildrenVariables = parser.getAgentsChildren();
		ParentsVariables = parser.getAgentsParentsVariables();
		Parent = parser.getNamesOfAgentsParent();
		AgentView = new ArrayList<AgentView>();
		nogoodStore = new NogoodStore();
		Domain = LocalCSP.getSolutions();
		Nogood = new Nogood();
		statistics = new Statistic();
		statistics.setCstrs(LocalCSP.getNbConstraints());
		AgentC_Cost = 0;
	}
	
	public String getMyName()
	{
		return myname;
	}
	
	public String getAgentsName()
	{
		return AgentsName;
	}
	
	public Solution getMyValue()
	{
		return myvalue;
	}
	
	public ArrayList<String> getChildren()
	{
		return Children;
	}
	
	public HashMap<String, ArrayList<jConstraint>> getParentsConstraints()
	{
		return ParentConstraints;
	}
	
	public HashMap<String, ArrayList<String>> getChildrenNPI()
	{
		return ChildrenVariables;
	}
	
	public ArrayList<String> getNamesOfParents()
	{
		return Parent;
	}
	
	public ArrayList<AgentView> getAgentView()
	{
		return AgentView;
	}
	
	public NogoodStore getNogoodStores()
	{
		return nogoodStore;
	}
	
	public ArrayList<Solution> getDomain()
	{
		return Domain;
	}
	
	public Nogood getNogood()
	{
		return Nogood;
	}
	
	public Statistic getStatistics()
	{
		return statistics;
	}
	
	public int getAgentC_Cost()
	{
		return AgentC_Cost;
	}
	
	public void setNogood(Nogood ngd)
	{
		this.Nogood = ngd;
	}
	
	public void setAgentC_Cost(int cost)
	{
		AgentC_Cost = cost;
	}
	
	public void AddToParent(String agent)
	{
		Parent.add(agent);
	}
	
	public void AddToChildreen(String agent)
	{
		Children.add(agent);
	}
	
	public void AddToNogoodstore(Nogood ngd)
	{
		nogoodStore.add(ngd);
	}
	
	public void AddNPIsolutiontoNgdStore (Nogood ngd, String Sender)
	{
		Solution solu = new Solution(ngd.getRHS().getnbVariables());
		for(Solution s : Domain)
		{
			if(s.isEqual(ngd.getRHS()))
			{
				solu = new Solution(s);
				break;
			}
		}
//		System.out.println("-+-+-+-+-+-   " +ngd+"     "+ParentsVariables.get(Sender)+"   "+Sender+"   "+ getNPI_solutionsOf(solu, Sender));
		for(Solution sol : getNPI_solutionsOf(solu, Sender))
		{
//			Solution s1 = new Solution(2);
//			s1.getVariables()[0] = new Variable("M1.1", 1);
//			s1.getVariables()[1] = new Variable("M1.2", 2);
//			
//			Solution s2 = new Solution(2);
//			s2.getVariables()[1] = new Variable("M1.1", 1);
//			s2.getVariables()[0] = new Variable("M1.2", 2);
			
//			System.out.println(s1+"     ******      "+s2+"    ------->    "+s1.isEqual(s2));
			if(!sol.isEliminated(nogoodStore))
			{
				Nogood nogood = new Nogood();
				nogood.setLHS(ngd.getLHS());
				nogood.setRHS(sol);
				nogoodStore.add(nogood);
			}
		}
	}
	
	public ArrayList<Solution> getNPI_solutionsOf(Solution s, String Agent)
	{
		ArrayList<Solution> sols = new ArrayList<Solution>();
		for(int i=0; i<Domain.size(); i++)
		{
			if(Domain.get(i).isNeighbourhoodPartialInterchangeable(s, ParentsVariables.get(Agent)))
			{
				sols.add(Domain.get(i));
			}
		}
		return sols;
	}
	
	public Solution ChooseValue()
	{
		for(int i = 0;i<Domain.size();i++)	
		{
			if(!Domain.get(i).isEliminated(nogoodStore))
			{
				Domain.get(i).ClearNbrConstraints();
				Solution inconsistentSolution = Domain.get(i).isConsistent(AgentView, ParentConstraints);
				statistics.IncrementCstrs(Domain.get(i).getNbrConstraintsTested());
				if(inconsistentSolution == null)
				{
					if(myvalue == null)
					{
						myvalue = new Solution(Domain.get(0).getVariables().length);
					}
					Tools.copy(Domain.get(i), myvalue);
					return Domain.get(i);
				}
				else
				{
					Nogood ngd = new Nogood();
					ngd.AddToLhs(inconsistentSolution);
					ngd.setRHS(Domain.get(i));
					AddNPIsolutiontoNgdStore(ngd, inconsistentSolution.getAgentOwner(AgentsName));
//					nogoodStore.add(ngd);
				}
			}
		}
		return null;
	}
	
	public void UpdateAgentView (String Agent, Solution NewSolution, boolean SolutionIsEmpty)
	{
		if(SolutionIsEmpty)
		{
			for(int k=0; k<AgentView.size(); k++)
			{
				if(AgentView.get(k).getSolution().isEqual(NewSolution))
				{
					AgentView.remove(k);
					k--;
				}
			}
		}
		else
		{
			int i = 0;
			for (; i < AgentView.size(); i++) 
			{
				if (AgentView.get(i).getAgent().equals(Agent)) 
				{
					AgentView.set(i, new AgentView(Agent, NewSolution)); // mettre � jour l'Agent View
					break;
				}
			}
			if (i >= AgentView.size()) 
			{
				AgentView.add(new AgentView(Agent, NewSolution)); 
			}
		}
	}
	
	public void removeIncompatiblesNogoods(Solution NewSolution, boolean SolutionIsEmpty)
	{
		if(SolutionIsEmpty)
		{
			for(int j = 0; j<nogoodStore.getNogoods().size(); j++)
			{
				if(nogoodStore.getNogoods().get(j).ContainLHS(NewSolution))
				{
					nogoodStore.remove(j);
					j--;
				}
			}
		}
		else
		{
			for(int j = 0; j<nogoodStore.getNogoods().size(); j++)
			{
				if(!nogoodStore.getNogoods().get(j).isCoherent(AgentView, AgentsName))
				{
					nogoodStore.remove(j);
					j--;
				}
			}
		}
	}
	
//	if the nogood is accepted, it return An arrayList which contain Assignments that not exist in AgentView
//	else it return null;
	
	public ArrayList<Solution> NogoodCompatibleWithAgentView (Nogood ngd)
	{
		ArrayList<Solution> notExistInAgentView = new ArrayList<Solution>();
		ArrayList<Solution> affect = new ArrayList<Solution>();
		ArrayList<Solution> othersAffects = new ArrayList<Solution>();
		
		if(!ngd.getRHS().isEqual(myvalue))
		{
			return null;
		}
		
		for(AgentView a : AgentView)
		{
			if(Tools.ListcontainExact(Parent, a.getAgent()))
			{
				affect.add(a.getSolution());
			}
			else
			{
				othersAffects.add(a.getSolution());
			}
		}
		if(ngd.getLHS().size()>0)
		for(int hh = 0; hh < ngd.getLHS().size(); hh++)
		{
			if(Tools.ContainAndChanged(affect, ngd.getLHS().get(hh)) != -1)
			{
				return null;
			}
			else if(((Tools.ContainSameNames(othersAffects, ngd.getLHS().get(hh)) == -1) && (Tools.ContainSameNames(affect, ngd.getLHS().get(hh)) == -1)) || (Tools.ContainAndChanged(othersAffects, ngd.getLHS().get(hh)) != -1)) 
			{
				notExistInAgentView.add(ngd.getLHS().get(hh));
			}
		}
		return notExistInAgentView;
	}
	
	public Solution myvalueIsConsistent()
	{
		return myvalue.isConsistent(AgentView, ParentConstraints);
	}
	
	public Nogood ConstructNogood()
	{
		Nogood = new Nogood(); 
		Nogood = nogoodStore.solve(AgentsName);
		if(Nogood.getAgentTarget(AgentsName) == "")
		{
			Nogood = null;
		}
		return Nogood;
	}

	public void setMyValue(Solution  s) {
		this.myvalue = s;
		
	}
	
	
	
}
