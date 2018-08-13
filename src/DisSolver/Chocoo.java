package DisSolver;
import java.util.ArrayList;
import java.util.List;

import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.model.variables.real.RealVariable;
import choco.kernel.solver.Solver;

public class Chocoo {
	int nbvar, index;
	Model m;
	IntegerVariable[] var;
	
	RealVariable[] realvars;
	
	Solver s;
	ArrayList<int[]> Domaines;
	ArrayList<Domaiin> domain;
	Parser parser;
	int MaxIndex;
	String Filename;
	ArrayList<Solution> SolutionsWithoutInterchangeability;
	ArrayList<Solution> Solutions;

	public Solver getSolver()
	{
		return s;
	}
	
	public Chocoo(String Filename) {
		this.Filename = Filename;
		parser = new Parser(Filename); // récupérer les données é partir du							// fichier xml
		nbvar = parser.getVariables().size();
		domain = parser.getDomaines(); // récupérer les domaines des variables
		Domaines = new ArrayList<int[]>();
		index = 0; // l'indice de la solution courante pour y revenir
		MaxIndex = 0; // le nombre de solutions du probléme
		SolutionsWithoutInterchangeability = new ArrayList<Solution>();
		MAC_all_solution mac = new MAC_all_solution();
//		SolutionsAfterInterchangeabilty = new ArrayList<Solution>();
		m = new CPModel();
		s = new CPSolver();
		var = new IntegerVariable[nbvar]; // var contient les variables du
											// probléme
		for (int i = 0; i < nbvar; i++) {
			var[i] = Choco.makeIntVar(
					parser.getVariables().get(i).split("/")[0], getDomain(parser
							.getVariables().get(i).split("\\/")[0]));
			m.addVariable(var[i]);
		}
//		
		solve();
		Solutions = new ArrayList<Solution>();
		for(int j=0; j<SolutionsWithoutInterchangeability.size(); j++)
		{
			boolean exist = false;
			Solution solution = new Solution(SolutionsWithoutInterchangeability.get(j).getnbVariables());
			for (int k = 0; k < SolutionsWithoutInterchangeability.get(j).getnbVariables(); k++) {
				solution.getVariables()[k] = new Variable(SolutionsWithoutInterchangeability.get(j).getVariables()[k].toString(), getType(SolutionsWithoutInterchangeability.get(j).getVariables()[k].getName()));
			}
			for(int i=0; i<Solutions.size(); i++)
			{
				if(Solutions.get(i).containSameExternalVariableValues(solution))
				{
					exist = true;
					break;
				}
			}
			if(!exist)
			{
//				System.out.println("***  " + solution);
				Solutions.add(solution);
			}
		}
	}
	
	
	public void solve()
	{
		ArrayList<Variable> Var = new ArrayList<Variable>();
		ArrayList<int[]> domain = new ArrayList<int[]>();
		ArrayList<jConstraint> constraints = new ArrayList<jConstraint>();
		for(IntegerVariable v : var)
		{
			Var.add(new Variable(v.getName(), -1));
			domain.add(v.getValues());
		}
		MAC_all_solution m = new MAC_all_solution();
		SolutionsWithoutInterchangeability = m.remplirTableau(Var, domain, parser.getContraintes());
	}
	
	public Solution getSolution()
	{
		Solution solution = new Solution(var.length);
		for (int i = 0; i < var.length; i++) {
			solution.getVariables()[i] = new Variable(s.getVar(var[i]).toString());
		}
		return solution;
	}
	
	  public int getNbConstraints()
	  {
		  return MAC_all_solution.c;
	  }
  
	  public Boolean hasNextSolution()
	  {
		  return(s.nextSolution());
	  }
  
	// fonction qui retourne un tableau (variable) contenant la solution trouvée
	public String[] getLocalSolution() {
		String[] variable;
		variable = new String[nbvar];

		if (s.getNbSolutions() > 0) {
			for (int i = 0; i < nbvar; i++) {
				variable[i] = s.getVar(var[i]).toString();
			}
		} else {
//			System.err.println("Pas de solution locale!! ");
		}
		return variable;
	}

	public String getDomainName(String VarName)
	{
		for (int i = 0; i < nbvar; i++) {
			if(parser.getVariables().get(i).split("\\/")[0].equals(VarName))
			{
				return parser.getVariables().get(i).split("\\/")[1];
			}
		}
		return "";
	}
	
	
	public String getType(String VarName)
	{
		for (int i = 0; i < nbvar; i++) {
			if(parser.getVariables().get(i).split("\\/")[0].equals(VarName))
			{
				return parser.getVariables().get(i).split("\\/")[2];
			}
		}
		return "";
	}
	
	// fonction qui retourne un tableau contenant le domaine d'une variable
	// passée en argument
	public int[] getDomain(String var) {
		domain = new ArrayList<Domaiin>();
		domain = parser.getDomaines();

		int i = 0;
		while (!(domain.get(i).getName().equals(getDomainName(var)))) {
			i++;
		}
		int[] domval = new int[domain.get(i).size];
		for (int j = 0; j < domain.get(i).size; j++) {

			domval[j] = domain.get(i).getElements().get(j);
		}
		return domval;
	}

	public void NextSolution() {
		s.nextSolution();
	}

	public void resolve() {
		s = new CPSolver();
		s.read(m);
		s.solve();
	}
	
	public void setIndex(int i) {
		index = i;
	}

	public int getIndex() {
		return index;
	}

	public int getMaxIndex() {
		return MaxIndex;
	}

 	public ArrayList<Solution> getSolutions() {
		return Solutions;
	}
	
//	public ArrayList<Solution> getSolutionswithInterchangeability() {
//		return SolutionsAfterInterchangeabilty;
//	}
}
