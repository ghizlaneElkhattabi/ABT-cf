package DisSolver;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Domaiin {
	String name;
	int size;
	ArrayList<Integer> elements;
	
	public Domaiin()
	{
		name ="";
		size = 0;
		elements = new ArrayList<Integer>();
	}
	
	public Domaiin(String name, int size, ArrayList<Integer> elts)
	{
		this.name = name;
		this.size = size;
		this.elements = new ArrayList<Integer>();
		for(Integer i : elts)
		{
			this.elements.add(i);
		}
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getSize()
	{
		return this.size;
	}
	
	public ArrayList<Integer> getElements()
	{
		return this.elements;
	}
	
	
	
	}
