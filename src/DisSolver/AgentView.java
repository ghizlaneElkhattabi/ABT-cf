package DisSolver;

public class AgentView {
	String agent;
	Solution solution;
	
	AgentView()
	{
		this.agent = "";
		this.solution = new Solution();
	}
	
	AgentView(String agt)
	{
		this.agent = agt;
		this.solution = new Solution();
	}
	
	AgentView(String agt, Solution s)
	{
		this.agent = agt;
		this.solution = new Solution(s.JustVarstoString());
	}
	
	public String getAgent()
	{
		return this.agent;
	}
	
	public Solution getSolution()
	{
		return this.solution;
	}
	
	public void setSolution(Solution sol)
	{
		this.solution = new Solution(sol.JustVarstoString());
	}
	
	public void setSolution(String sol)
	{
		this.solution = new Solution(sol);
	}
	
	public void setSolution(Variable[] vars)
	{
		this.solution = new Solution(vars.length);
		for(int i=0; i<vars.length; i++)
		{
			this.solution.getVariables()[i] = vars[i];
		}
	}
	
	public String toString()
	{
		return agent+";"+solution.JustVarstoString();
	}
	
	
}
