package DisSolver;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
	
	
	public static int ListContain(ArrayList<Variable> list, Variable v)
	{
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).getName() .equals(v.getName()))
			{
				return i;
			}
		}
		return -1;
	}
	
	public static boolean StringListContain(ArrayList<String> list, String v)
	{
		for(int i=0; i<list.size(); i++)
		{
			if(list.get(i).equals(v))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean StringListContain(String[] list, String v)
	{
		for(int i=0; i<list.length; i++)
		{
			if(list[i].equals(v))
			{
				return true;
			}
		}
		return false;
	}
	
	public Variable getFullVariable(Variable[] table, String s)
	{
		for(Variable str : table)
		{
			if(str.getName().equals(s))
			{
				return str;
			}
		}
		return null;
	}
	
	public static boolean contain(Variable[] table, String value) {
		for (Variable s : table) 
		{
			if (s.getName().equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	public static Boolean ListContainExactV(ArrayList<Variable> list, Variable v)
	{
		for(Variable var : list)
		{
			if(var.isEqual(v))
			{
				return true;
			}
		}
		return false;
	}
	
	public static Boolean ListContainExact(Variable[] list, Variable v)
	{
		for(Variable var : list)
		{
			if(var.isEqual(v))
			{
				return true;
			}
		}
		return false;
	} 
	
	public static Boolean ListcontainExact(ArrayList<String> table, String value) 
	{
		for(int i=0; i<table.size(); i++)
		{
			if (table.get(i).equals(value)) 
			{
				return true;
			}
		}
		return false;
	}
	
	public static int ListcontainExact(ArrayList<Integer> table, int value) 
	{
		for(int i=0; i<table.size(); i++)
		{
			if (table.get(i)==value) 
			{
				return i;
			}
		}
		return -1;
	}
	
	public static int ContainAndChanged(ArrayList<Solution> table, Solution value)
	{
		for(int i=0; i<table.size(); i++)
		{
			if ((table.get(i).hasSameNamesOfVariables(value)) && (table.get(i).isChanged(value))) 
			{
				return i;
			}
		}
		return -1;
	}
	
	public static int ContainSameNames(ArrayList<Solution> table, Solution value)
	{
		for(int i=0; i<table.size(); i++)
		{
			if (table.get(i).hasSameNamesOfVariables(value)) 
			{
				return i;
			}
		}
		return -1;
	}
	public static Boolean ListContainPart(ArrayList<Variable> List, String[] part)
	{
		for(String str : part)
		{
			if(!ListContainExactV(List, new Variable(str)))
			{
				return false;
			}
		}
		return true;
	}
	
	public static Boolean ListContainNamesOfPart(ArrayList<Variable> List, String[] part)
	{
		for(String str : part)
		{
			if(ListContain(List, new Variable(str)) == -1)
			{
				return false;
			}
		}
		return true;
	}
	
	public static Boolean ListContainNamesOfPart(ArrayList<Variable> List, Variable[] part)
	{
		for(Variable str : part)
		{
			if(ListContain(List, str) == -1)
			{
				return false;
			}
		}
		return true;
	}
	
	
	public static Boolean ListContainNamesOfPart(ArrayList<Variable> List, ArrayList<Variable> part)
	{
		for(Variable str : part)
		{
			if(ListContain(List, str) == -1)
			{
				return false;
			}
		}
		return true;
	}
	
	public static Boolean ListContainNameOfELementOfPart(ArrayList<Variable> List, String[] part)
	{
		for(String str : part)
		{
			if(ListContain(List, new Variable(str)) != -1)
			{
				return true;
			}
		}
		return false;
	}
	
	public static Boolean ListContainELementOfPart(ArrayList<Variable> List, String[] part)
	{
		for(String str : part)
		{
			if(ListContainExactV(List, new Variable(str)))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEqual(Variable[] A, Variable[] B)
	{
		for(int i = 0;i<A.length ; i++)
		{
			if(!B[i].isEqual(A[i])) return false;
		}
		return true;
	}
	
	public static int ExtractInteger (String str)
	{
        Matcher matcher = Pattern.compile("\\d+").matcher(str);

        if (!matcher.find())
            throw new NumberFormatException("For input string [" + str + "]");

        return Integer.parseInt(matcher.group());
	}
	
	public static ArrayList<Variable> Sort (ArrayList<Variable> list)
	{
		for(int i = 0;i<list.size()-1;i++)
		{
			for(int j = 0;j<list.size()-i-1;j++)
			{
				
				if(ExtractInteger(list.get(j).getName()) > ExtractInteger(list.get(j+1).getName()))
				{
					Variable tmp = new Variable(list.get(j).getName(), list.get(j).getValue());
					list.set(j, list.get(j+1));
					list.set(j+1, tmp);
				}
			}
		}
		return list;
	}
	
	public static ArrayList<String> SortStrings (ArrayList<String> list)
	{
		for(int i = 0;i<list.size()-1;i++)
		{
			for(int j = 0;j<list.size()-i-1;j++)
			{
				
				if(ExtractInteger(list.get(j)) > ExtractInteger(list.get(j+1)))
				{
					String tmp = list.get(j);
					list.set(j, list.get(j+1));
					list.set(j+1, tmp);
				}
			}
		}
		return list;
	}
	
	public static int indexOf(ArrayList<Solution> solutions, Solution solution)
	{
		for(int i=0; i<solutions.size(); i++)
		{
			if(isEqual(solutions.get(i).getVariables(),solution.getVariables())){
				return i;
			}
		}
		return -1;
	}
	
	public static void Sleep(int time)
	{
		try {
			Thread.sleep(time);
		} 
		catch (InterruptedException ex) 
		{
			Thread.currentThread().interrupt();
		}
	}
	
	public static void copy(Solution A, Solution B)
	{
		B.setVariables(A.getVariables());
		
	}
	
}
