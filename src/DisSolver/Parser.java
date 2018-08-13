package DisSolver;
import jade.domain.DFGUIManagement.GetParents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.NodeList;

import choco.kernel.model.constraints.Constraint;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

public class Parser {
	String NameFile;
	File xmlfile;
	Document doc;
	NodeList nodes = null;
 
	public Parser(){
		
	}
	
	public Parser(String NameFile) {
		this.NameFile = NameFile;
		try {

			SAXBuilder builder = new SAXBuilder(false);
			doc = builder.build(new File(NameFile));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	// -----------------------------------------------------------------------------------------------

	public ArrayList<String> getVariables() {
		ArrayList<String> variables = new ArrayList<String>();
		String info;
		Element root = doc.getRootElement().getChild("variables");
		// Print servlet information
		List servlets = root.getChildren("variable");

		Iterator i = servlets.iterator();
		while (i.hasNext()) {
			info = "";
			Element element = (Element) i.next();
			info = element.getAttribute("name").getValue() + "/"
					+ element.getAttribute("domain").getValue()+"/"+isExternal(element.getAttribute("name").getValue());
			variables.add(info);
		}
		return variables;
	}

	public boolean isExternal(String variablenString)
	{
		for(String str : getAgentsChildren().keySet())
		{
			if(Tools.StringListContain(getAgentsChildren().get(str),variablenString))
			{
				return true;
			}
		}
		for(String str : getAgentsParent().keySet())
		{
			for(int i=0; i<getAgentsParent().get(str).size(); i++)
			{
				if(Tools.StringListContain(getAgentsParent().get(str).get(i).getVariables(), variablenString))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	// --------------------------------------------------------------------

	public ArrayList<jConstraint> getContraintes() {
		ArrayList<jConstraint> contraintes = new ArrayList<jConstraint>();

		jConstraint info;
		Element root = doc.getRootElement().getChild("constraints");
		// Print servlet information
		List servlets = root.getChildren("constraint");

		Iterator i = servlets.iterator();
		while (i.hasNext()) {
			Element element = (Element) i.next();
			info = new jConstraint(element.getAttribute("scope").getValue().toString().split("\\ ").length);
			info.setName(element.getAttribute("name").getValue());
			info.setType(element.getAttribute("reference").getValue());
			info.setVariables(element.getAttribute("scope").getValue());
			if(info.getType().contains("R"))
			{
				info.tuples = new ArrayList<Integer[]>();
				for(String str : getRelation(info.getType()).split("\\/")[2].split("\\,"))
				{
					info.AddToTuples(str);
				}
				info.setType(getRelation(info.getType()).split("\\/")[1]);
			}
			contraintes.add(info);
		}
		return contraintes;

	}

	// --------------------------------------------------------------------

	public ArrayList<Domaiin> getDomaines() {
		ArrayList<Domaiin> domaines = new ArrayList<Domaiin>();

		Domaiin info = new Domaiin();
		Element root = doc.getRootElement().getChild("domains");
		// Print servlet information
		List servlets = root.getChildren("domain");

		Iterator i = servlets.iterator();
		while (i.hasNext()) {
			String DomaineValue="";
			Element element = (Element) i.next();
			if(element.getText().contains(".."))
			{
				String[] MinMax = element.getText().split("\\.\\.");
				info.elements = new ArrayList<Integer>();
				for(int j = Integer.valueOf(MinMax[0]);j<=Integer.valueOf(MinMax[1]);j++)
				{
					info.elements.add(j);
					DomaineValue = DomaineValue+ ";"+j;
				}
				DomaineValue = DomaineValue.substring(1);
			}
			else
			{
				info.elements = new ArrayList<Integer>();
				for(String str:element.getText().split("\\;"))
				{
					info.elements.add(Integer.parseInt(str));
				}
				DomaineValue = element.getText();
			}
			info.name = element.getAttribute("name").getValue();
			info.size = Integer.parseInt(element.getAttribute("nbValues").getValue());
			domaines.add(info);
		}
		return domaines;
	}

	// --------------------------------------------------------------------

	public ArrayList<String> getAgentsFils() {
		ArrayList<String> agents = new ArrayList<String>();

		String info;

		Element root = doc.getRootElement().getChild("agents_neighbours")
				.getChild("agents_children");
		// Print servlet information
		List servlets = root.getChildren("agent");

		Iterator i = servlets.iterator();
		while (i.hasNext()) {
			info = "";
			Element element = (Element) i.next();
			info = element.getAttribute("name").getValue() + "/"
					+ element.getAttribute("variable").getValue();
			agents.add(info);
		}
		return agents;

	}

public ArrayList<String> getNamesOfAgentsParent() {
		
		ArrayList<String> agents = new ArrayList<String>();
		Element root = doc.getRootElement().getChild("agents_neighbours")
				.getChild("agents_parent");
		// Print servlet information
		List servlets = root.getChildren("agent");
		Iterator i = servlets.iterator();
		
		while (i.hasNext()) {
			Element element = (Element) i.next();
			agents.add(element.getAttribute("name").getValue());
		
		}
		Tools.SortStrings(agents);
		return agents;
		
	}

public ArrayList<String> getNamesOfAgentsChildren() {
	
	ArrayList<String> agents = new ArrayList<String>();
	Element root = doc.getRootElement().getChild("agents_neighbours")
			.getChild("agents_children");
	// Print servlet information
	List servlets = root.getChildren("agent");
	Iterator i = servlets.iterator();
	
	while (i.hasNext()) {
		Element element = (Element) i.next();
		agents.add(element.getAttribute("name").getValue());
	
	}
	Tools.SortStrings(agents);
	return agents;

}

	public HashMap<String, ArrayList<jConstraint>> getAgentsParent() {
		
		HashMap<String, ArrayList<jConstraint>> agents = new HashMap<String, ArrayList<jConstraint>>();
		String info;
		Element root = doc.getRootElement().getChild("agents_neighbours")
				.getChild("agents_parent");
		// Print servlet information
		List servlets = root.getChildren("agent");
		Iterator i = servlets.iterator();
		
		while (i.hasNext()) {
			info = "";
			Element element = (Element) i.next();
//			info = element.getAttribute("name").getValue() +";";
			List cont = element.getChild("constraints").getChildren("constraint");
			Iterator ic = cont.iterator();
			ArrayList<jConstraint> Constr = new ArrayList<jConstraint>();
			while (ic.hasNext()) {
				jConstraint C = new jConstraint(2);
				Element element1 = (Element) ic.next();
				C.setName(element1.getAttribute("reference").getValue());
				C.setType(element1.getAttribute("reference").getValue());
				C.setVariables(element1.getAttribute("scope").getValue());
				if(C.getType().contains("R"))
				{
					C.tuples = new ArrayList<Integer[]>();
					for(String str : getRelation(C.getType()).split("\\/")[2].split("\\,"))
					{
						C.AddToTuples(str);
					}
					C.setType(getRelation(C.getType()).split("\\/")[1]);
				}
				Constr.add(C);
			}
			agents.put(element.getAttribute("name").getValue(), Constr);
		}
		return agents;

	}
	
public HashMap<String, ArrayList<String>> getAgentsChildren() {
		
		HashMap<String, ArrayList<String>> agents = new HashMap<String, ArrayList<String>>();
		Element root = doc.getRootElement().getChild("agents_neighbours")
				.getChild("agents_children");
		// Print servlet information
		List servlets = root.getChildren("agent");
		Iterator i = servlets.iterator();
		
		while (i.hasNext()) {
			Element element = (Element) i.next();
//			info = element.getAttribute("name").getValue() +";";
			List cont = element.getChild("constraints").getChildren("constraint");
			Iterator ic = cont.iterator();
			ArrayList<String> variables = new ArrayList<String>();
			while (ic.hasNext()) {
				Element element1 = (Element) ic.next();
				element1.getAttribute("scope").getValue();
				for(String str : element1.getAttribute("scope").getValue().split("\\ "))
				{
//					System.out.println("//////////   " + str+"      "+Tools.ExtractInteger(str)+"    "+element.getAttributeValue("name")+"        "+Tools.ExtractInteger(element.getAttributeValue("name")));
					if(Tools.ExtractInteger(str) != Tools.ExtractInteger(element.getAttributeValue("name")))
					{
						variables.add(str);
					}
				}
			}
//			System.out.println("''''     "+variables);
			agents.put(element.getAttribute("name").getValue(), variables);
		}
		return agents;

	}

public HashMap<String, ArrayList<String>> getAgentsParentsVariables() {
	
	HashMap<String, ArrayList<String>> agents = new HashMap<String, ArrayList<String>>();
	String info;
	Element root = doc.getRootElement().getChild("agents_neighbours")
			.getChild("agents_parent");
	// Print servlet information
	List servlets = root.getChildren("agent");
	Iterator i = servlets.iterator();
	
	while (i.hasNext()) {

		info = "";
		Element element = (Element) i.next();
//		info = element.getAttribute("name").getValue() +";";
		List cont = element.getChild("constraints").getChildren("constraint");
		Iterator ic = cont.iterator();
		ArrayList<String> variables = new ArrayList<String>();
		while (ic.hasNext()) {
			Element element1 = (Element) ic.next();
			element1.getAttribute("scope").getValue();
			for(String str : element1.getAttribute("scope").getValue().split("\\ "))
			{
//				System.out.println("//////////   " + str+"      "+Tools.ExtractInteger(str)+"    "+element.getAttributeValue("name")+"        "+Tools.ExtractInteger(element.getAttributeValue("name")));
				if(Tools.ExtractInteger(str) != Tools.ExtractInteger(element.getAttributeValue("name")))
				{
					variables.add(str);
				}
			}
		}
//		System.out.println("''''     "+variables);
		agents.put(element.getAttribute("name").getValue(), variables);
	}
	return agents;

}

	public String getExternal_Constrainte(String name) {
		ArrayList<String> Predicates = new ArrayList<String>();
		String info = "";

		Element root = doc.getRootElement().getChild("predicates");
		// Print servlet information
		List servlets = root.getChildren("predicate");
		if(servlets.size()>0)
		{
		Iterator i = servlets.iterator();
		while (i.hasNext()) {
			info = name +"/";
			Element element = (Element) i.next();
			if (element.getAttribute("name").getValue().toString().equals(name)) {
				info = info+element.getChild("expression").getChild("functional")
						.getValue()
						+ "/" + element.getChild("parameters").getText();
				break;
			}
		}
		}
		else
		{
			info = name + "/"+"/";
		}
		return info;

	}
	public ArrayList<String> getRelationsList() {
		ArrayList<String> Relations = new ArrayList<String>();
		try
		{
		String info;
		Element root = doc.getRootElement().getChild("relations");
		// Print servlet information
		List servlets = root.getChildren("relation");

		Iterator i = servlets.iterator();
		while (i.hasNext()) {
			info = "";
			Element element = (Element) i.next();
			info = element.getAttribute("name").getValue()+"/"+element.getAttribute("semantics").getValue()+"/"+element.getValue();
			Relations.add(info);
		}
		return Relations;
		}
		catch(Exception e)
		{
			return Relations;
		}
	}
	
	public String getRelation (String R)
	{
		ArrayList<String> Relations = new ArrayList<String>();
		Relations = getRelationsList();
		String rela="";
		for (String rel : Relations)
		{
			if(rel.split("\\/")[0].equals(R))
			{
				rela = rel;
			}
		}
		String[] r = rela.split("\\|");
		rela = "";
		for(String a : r)
		{
			rela += a+",";
		}
		if(rela.contains(","))
		{
		rela = rela.substring(0, rela.length()-1);
		}
		return rela;
	}
	public void afficher(String []name){
		for (String str: name)
		System.out.println(str);
	}
	// ----------------------------------------------------------------------------------------------------------------------
//	public static void main(String[] args) {
//		Parser p = new Parser("Problem2.xml");
//		/*
//		 * System.out.println(p.getAgents()); System.out.println();
//		 * System.out.println(p.getVariables()); System.out.println();
//		 * System.out.println(p.getContraintes()); System.out.println();
//		 * System.out.println(p.getDomaines()); System.out.println();
//		 */
//		System.out.println(p.getAgentsParent());
//
//	}
}
