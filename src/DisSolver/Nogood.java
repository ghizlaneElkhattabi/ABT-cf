package DisSolver;

import java.util.ArrayList;

public class Nogood {
	ArrayList<Solution> LHS;
	Solution RHS;
	
	public Nogood()
	{
		LHS = new ArrayList<Solution>();
		RHS = new Solution();
	}
	
	public Nogood(ArrayList<Solution> lhs, Solution rhs)
	{
		LHS = new ArrayList<Solution>();
		for(Solution str: lhs)
		{
			LHS.add(str);
		}
		RHS = new Solution(rhs.JustVarstoString());
	}
	
	public Nogood(String nogood)
	{
		LHS = new ArrayList<Solution>();
		RHS = new Solution();
		
		if(!nogood.split("\\>")[0].contains("NULL"))
		{
			nogood = nogood.substring(1, nogood.length()-1);
			for(String str: nogood.split("\\)\\>\\(")[0].split("\\)\\^\\("))
			{
				Solution s = new Solution(str.split("\\,").length);
				s.setVariables(str.split("\\,"));
				LHS.add(s);
			}
		}
		else
		{
			nogood = nogood.substring(0, nogood.length()-1);
		}
		RHS = new Solution(nogood.split("\\>")[1].substring(1, nogood.split("\\>")[1].length()).split("\\,").length);
		RHS.setVariables(nogood.split("\\>")[1].substring(1, nogood.split("\\>")[1].length()).split("\\,"));
//		RHS= new Solution(s);
	}
	
	public void setLHS(ArrayList<Solution> lhs)
	{
		LHS = new ArrayList<Solution>();
		for(Solution str: lhs)
		{
			LHS.add(str);
		}
	}
	
	public void setRHS(Solution rhs)
	{
		RHS = new Solution(rhs.JustVarstoString());
	}
	
	public Solution getRHS()
	{
		return RHS;
	}
	
	public ArrayList<Solution> getLHS()
	{
		return LHS;
	}
	
	public void AddToLhs(Solution elt)
	{
		LHS.add(elt);
	}
	
	
	public String toString ()
	{
		String nogood = "";
		if((RHS.getnbVariables() == 0) && (LHS.size() == 0))
		{
			return nogood;
		}
		if (LHS.equals(null) || LHS.size() == 0) {
			nogood = "NULL>";
		}
		else
		{
			for(Solution str : LHS)
			{
				nogood += "(";
				for(Variable v : str.getVariables())
				{
					nogood += v.toString()+",";
				}
				nogood = nogood.substring(0, nogood.length()-1)+")^";
			}
			nogood = nogood.substring(0, nogood.length()-1)+">";
		}
		nogood += "(";
		for(Variable v : RHS.getVariables())
		{
			nogood += v.toString()+",";
		}
		nogood = nogood.substring(0, nogood.length()-1)+")";
		
		if (RHS.equals(null) || RHS.getnbVariables() == 0) {
			nogood = "NULL";
		}
		return nogood;
	}
	
	
	public boolean isCoherent(ArrayList<AgentView>agtView, String name)
	{
		for(AgentView agt : agtView)
		{
			for(Solution s : this.getLHS())
			{
				if(agt.getSolution().ContainNameOfAndChanged(s, name))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean ContainLHS(Solution s)
	{
		for(Solution sol : this.getLHS())
		{
			if(sol.isEqual(s))
			{
				return true;
			}
		}
		return false;
	}
	
	public String getAgentTarget(String name)
	{
		if(this.getRHS().getnbVariables()>0)
		{
			name += Tools.ExtractInteger(this.getRHS().getVariables()[0].getName());
		}
		else
		{
			name = "";
		}
		return name;
	}
}
