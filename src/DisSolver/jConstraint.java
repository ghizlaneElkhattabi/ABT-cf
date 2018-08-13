package DisSolver;

import java.util.ArrayList;

public class jConstraint {
	String name;
	String type;
	int nbrVariables;
	String[] Variables;
	ArrayList<Integer[]> tuples;
	
	public jConstraint()
	{
		Variables = new String[2];
		tuples = new ArrayList<Integer[]>();
	}
	
	public jConstraint(int nbrVars)
	{
		this.nbrVariables = nbrVars;
		Variables = new String[nbrVars];
		tuples = new ArrayList<Integer[]>();
	}
	
	public jConstraint(String name, String type, int nbrVariables, String[] variables)
	{
		this.name = name;

		this.type = type;
		
		this.nbrVariables = nbrVariables;
		
		this.Variables = new String[this.nbrVariables];
		
		for(int i=0; i<variables.length; i++)
		{
			this.Variables[i] = variables[i];
		}
		
		this.tuples = new ArrayList<Integer[]>();
	}
	
	public jConstraint(String name, String type, int nbrVariables, String[] variables, ArrayList<Integer[]> tuples)
	{
		this.name = name;

		this.type = type;
		
		this.nbrVariables = nbrVariables;
		
		this.Variables = new String[this.nbrVariables];
		
		for(int i=0; i<variables.length; i++)
		{
			this.Variables[i] = variables[i];
		}
		
		this.tuples = new ArrayList<Integer[]>();
		for(int i = 0; i<tuples.size(); i++)
		{
			Integer[] a = new Integer[2];
			for(int j=0; j<tuples.get(i).length; j++)
			{
				a[j] = tuples.get(i)[j];
			}
			this.tuples.add(a);
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getType()
	{
		return type;
	}
	
	public int getNbrVariables()
	{
		return nbrVariables;
	}
	
	public String[] getVariables()
	{
		return Variables;
	}
	
	public ArrayList<Integer[]> getTuples()
	{
		return tuples;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public void setNbrVariables(int nbrVariables)
	{
		this.nbrVariables = nbrVariables;
	}
	
	public void setVariables(String[] variables)
	{
		for(int i=0; i<variables.length; i++)
		{
			this.Variables[i] = variables[i];
		}
	}
	
	public void setVariables(String variables)
	{
		this.nbrVariables = variables.split("\\ ").length;
		for(int i=0; i<this.nbrVariables; i++)
		{
			this.Variables[i] = variables.split("\\ ")[i];
		}
	}
	
	public void setTuples(ArrayList<Integer[]> tuples)
	{
		for(int i = 0; i<tuples.size(); i++)
		{
			Integer[] a = new Integer[2];
			for(int j=0; j<tuples.get(i).length; j++)
			{
				a[j] = tuples.get(i)[j];
			}
			this.tuples.add(a);
		}
	}
	
	public void AddToTuples(Integer[] tuple)
	{
		this.tuples.add(tuple);
	}
	
	public void AddToTuples(String tuple)
	{
		Integer[] a = new Integer[2];
		for(int i=0; i<tuple.split("\\ ").length; i++)
		{
			a[i] = Integer.parseInt(tuple.split("\\ ")[i]);
		}
		this.tuples.add(a);
	}
	
	public boolean isSatisfied (Variable variable1,Variable variable2)
	{
		Boolean consistent = true;
		if((type.equals("eq")) || (type.equals("EQ")))
		{
			consistent = (variable1.getValue() == variable2.getValue());
		}
		else if((type.equals("neq")) || (type.equals("NEQ")))
		{
			consistent = (variable1.getValue() != variable2.getValue());
		}
		else if(type.equals("conflicts"))
		{
			int i = 0;
			for(i=0; i<2; i++)
			{
				if(Variables[i].equals(variable2.getName()))
				{
					break;
				}
			}
			
			ArrayList<Integer> fbd = new ArrayList<Integer>();
//			String[] aa = constraint.split("\\/")[1].split("\\,");
			for(int ii=0; ii<tuples.size(); ii++)
			{
				if(tuples.get(ii)[tuples.get(ii).length-1-i] == variable1.getValue())
				{
					fbd.add(tuples.get(ii)[i]);
				}
			}
			
			consistent = (Tools.ListcontainExact(fbd, variable2.getValue()) == -1);
		}
		else if(type.equals("supports"))
		{
			int i = 0;
			for(i=0; i<2; i++)
			{
				if(Variables[i].equals(variable2.getName()))
				{
					break;
				}
			}
			
			ArrayList<Integer> support = new ArrayList<Integer>();
//			String[] aa = constraint.split("\\/")[1].split("\\,");
			for(int ii=0; ii<tuples.size(); ii++)
			{
				if(tuples.get(ii)[tuples.get(ii).length-1-i] == variable1.getValue())
				{
					support.add(tuples.get(ii)[i]);
				}
			}
			consistent = (Tools.ListcontainExact(support, variable2.getValue()) != -1);
		}
		return consistent;
	}
	public String toString()
	{
		String s = "";
		for(int i=0; i<nbrVariables; i++)
		{
			s += Variables[i]+" ";
		}
		s = s.substring(0, s.length()-1);
		String tu = "";
		if(tuples.size()>0)
		{
		for(Integer[] t : tuples)
		{
			for(Integer i : t)
			{
				tu += i+" ";
			}
			tu = tu.substring(0, tu.length()-1)+",";
		}
		tu = tu.substring(0,tu.length()-1);
		}
		return name+"/"+s+"/"+type+"/"+tu;
	}
}
