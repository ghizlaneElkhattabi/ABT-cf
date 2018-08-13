package DisSolver;

import java.util.ArrayList;

public class NogoodStore {
	ArrayList<Nogood> Nogoods;
	
	public NogoodStore()
	{
		Nogoods = new ArrayList<Nogood>();
	}
	
	public ArrayList<Nogood> getNogoods()
	{
		return Nogoods;
	}
	
	public void add(Nogood ngd)
	{
		Nogoods.add(ngd);
	}
	
	public void remove(int i)
	{
		this.Nogoods.remove(i);
	}
	
	public ArrayList<Variable> getElementsOfNogoods()
	{
		ArrayList<Variable> NogoodList = new ArrayList<Variable>();
		for(Nogood ngd : Nogoods)
		{
			for(Solution sol : ngd.getLHS())
			{
				for(Variable v : sol.getVariables())
				{
					if (!Tools.ListContainExactV(NogoodList, v))
					{
						NogoodList.add(v);
					}
				}
			}
		}
//		System.out.println("********************   " + NogoodList);
		return NogoodList;
	}
	
	public Nogood solve(String name)
	{
		Nogood ngd = new Nogood();
		ArrayList<Variable> elements = new ArrayList<Variable>();
		elements = getElementsOfNogoods();
		if(elements.size()>0)
		{
			elements = Tools.Sort(elements);
			int j = elements.size()-1;
			    if(j>0)
				while(Tools.ExtractInteger(elements.get(j).getName()) == Tools.ExtractInteger(elements.get(j-1).getName()))
				{
					j--;
					if(j==0)break;
				}
			if(j==0)
			{
				Solution s = new Solution(elements.size());
				for(int i=0; i<elements.size(); i++)
				{
					s.getVariables()[i] = elements.get(i);
				}
				ngd.setRHS(s);
			}
			else
			{
				ArrayList<Variable> ins = new ArrayList<Variable>();
				for(int k=0; k<j; k++)
				{
					if(k == 0 || elements.get(k).getAgentOwner(name).equals(elements.get(k-1).getAgentOwner(name)))
					{
						ins.add(elements.get(k));
					}
					else if((k != 0 && !elements.get(k).getAgentOwner(name).equals(elements.get(k-1).getAgentOwner(name)))) 
					{
						Solution s = new Solution(ins.size());
						for(int g=0; g<ins.size(); g++)
						{
							s.getVariables()[g] = ins.get(g);
						}
						ngd.AddToLhs(s);
						ins.clear();
						ins.add(elements.get(k));
					}
					if((k == j-1))
					{
						Solution s = new Solution(ins.size());
						for(int g=0; g<ins.size(); g++)
						{
							s.getVariables()[g] = ins.get(g);
						}
						ngd.AddToLhs(s);
						ins.clear();
					}
				}
				Solution s = new Solution(elements.size()-j);
				for(int k=j;k<elements.size();k++)
				{
					s.getVariables()[k-j] = elements.get(k);
				}
				ngd.setRHS(s);
			}
		}
		return ngd;
	
	}
}
