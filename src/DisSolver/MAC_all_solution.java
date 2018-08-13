/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DisSolver;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
//import org.jdom.Element;
//import org.jdom.input.SAXBuilder;

/**
 *
 * @author elgra
 */
public class MAC_all_solution {
//    static org.jdom.Document document;
//        static Element racine;
        List<domain> domain =new ArrayList() ;
        List<objet> variable =new ArrayList() ;
        List<relations> relation =new ArrayList() ;
        List<constraint> constraint =new ArrayList();
        public static int c;
   
         public static int variable(String n,ArrayList<Variable> Var){
         for(int i=0;i<Var.size();i++){
             if(n.equals(Var.get(i).getName())){
                 return i;
             }
         }
         return -1;
     }
        public ArrayList<Solution> remplirTableau(ArrayList<Variable> Var,ArrayList<int[]> domian,ArrayList<jConstraint> cont) {
            for(byte i = 0 ;i<Var.size();i++){
               variable.add(new objet(i,"V"+i,"D"+i));
               domain.add(new domain("D"+i,domian.get(i)[0]+".."+domian.get(i)[domian.get(i).length-1]));
            }
        
        for(int i=0;i<cont.size();i++){
            constraint.add(new constraint(cont.get(i).getName(),cont.get(i).getNbrVariables(),"V"+variable(cont.get(i).getVariables()[0],Var)+" "+"V"+variable(cont.get(i).getVariables()[1],Var),"R"+i));
            relation.add(new relations("R"+i,cont.get(i).getTuples().size(),cont.get(i).getType(),cont.get(i).getTuples()));
        }
           for(int i=0;i<constraint.size();i++){
                for(int j=0;j<relation.size();j++){
                    if(constraint.get(i).reference.equals(relation.get(j).name)){
                        constraint.get(i).relation=relation.get(j);
                    }
                }
            }
            int d =constraint.size();
      
             
            for(int i=0;i<d;i++){
                constraint.add(new constraint(constraint.get(i).name,constraint.get(i).arity,
                        constraint.get(i).scope,constraint.get(i).reference,constraint.get(i).relation));
                
            }
           for(int i=0;i<variable.size();i++){
                for(int j=0;j<domain.size();j++){
                    if(variable.get(i).name_domain.equals(domain.get(j).name)){
                        variable.get(i).valeur_domain.clear();
                        variable.get(i).valeur_domain.addAll(domain.get(j).valeur);
                    }
                }
                for(int j=0;j<constraint.size();j++){
                        if(variable.get(i).name.equals(constraint.get(j).scope.split(" ")[0])){
                            variable.get(i).constraint.add(constraint.get(j));
                        
                    }
                }
            }
           ArrayList<Solution> sol = new ArrayList<Solution>();
            AC2001 A=new AC2001(variable);
               variable=A.dimarer();
              if(variable==null){}else{
              BT A1=new BT(variable);
              sol=A1.dimarer(Var);}
//              System.out.println("'''''    "+MAC_all_solution.solutions.size()+"     "+MAC_all_solution.c);
              int[] S = new int[variable.size()];
                 for(int i=0;i<S.length;i++){
               S[i]=variable.get(i).valeur;
           }
                 
           return sol;
            
         }
}
class BT{
      List<objet> variable =new ArrayList() ;
       List<objet> VAR =new ArrayList() ;
        ArrayList<String> VariableName = new ArrayList<String>();
        ArrayList<Solution> solutions = new ArrayList<Solution>();
       int g;
      BT(List<objet> v){VAR=v;g=v.size();}
      ArrayList<Solution> dimarer(ArrayList<Variable> vars){
    	  if(VariableName.size() == 0)
    	  {
	    	  for(Variable v : vars)
	    	  {
	    		  VariableName.add(v.getName());
	    	  }
//	    	  VariableName = Tools.SortStrings(VariableName);
    	  }
          go();
          return solutions;
      }
      boolean go(){
          if(isComplet()){   
             // System.out.print("========================");
        	  Solution solution = new Solution(variable.size());
        	  for(int i=0;i<variable.size();i++)
              {
//        		  System.out.println("222222   "+VariableName);
            	  solution.getVariables()[i] = new Variable(VariableName.get(i),variable.get(i).valeur);
              }
        	  solutions.add(solution);
              return true;
          }else{
            int i=choix();
            variable.add(VAR.get(i));
//                   System.out.println("++++  " + variable.get(i).name+"     "+variable.get(i).valeur+"    "+variable.get(i).valeur_domain);
                    for(int j=0;j<variable.get(i).valeur_domain.size();j++){
                        variable.get(i).valeur=variable.get(i).valeur_domain.get(j);
                      //  Application.node_visit++;
                        if(filtre()){
                           
                                // if(go()){ return true;}
                                 go();
                          
                         }
                        
                        VAR=ubdate();
                       VAR.get(i).valeur=-1;
                    }
                    variable.remove(variable.size()-1);
           return false;}
      }
    private int choix() {
        for(int i=0;i<VAR.size();i++){if(VAR.get(i).valeur==-1){return i;}}
             return 0;
    }
    private boolean isComplet() {
          for(int i=0;i<VAR.size();i++){
           if(VAR.get(i).valeur==-1){
               return false;
           }   
          }return true;
   }
     List<objet> val = new ArrayList();
    private boolean filtre() {
        for(int i=0;i<g;i++){val.add(new objet(VAR.get(i).name,VAR.get(i).name_domain,VAR.get(i).valeur,VAR.get(i).valeur_domain,VAR.get(i).constraint));}
        for(int i=0;i<variable.size();i++){
            VAR.get(i).valeur_domain.clear();
            VAR.get(i).valeur_domain.add(variable.get(i).valeur);
        }
            AC2001 A=new AC2001(VAR);
            VAR=A.dimarer();
            if(VAR==null){ return false;}else{return true;}
    }

    private List<objet> ubdate() {
        List<objet> l = new ArrayList();
        for(int i=0;i<g;i++){
            l.add(val.get((val.size()-g)+i));
            val.remove((val.size()-g)+i);
        }
        variable.set(variable.size()-1,l.get(variable.size()-1));
        return l;
    }
}
 class AC2001 {
       List<objet> variable = new ArrayList();
    AC2001(List<objet> v){
        variable=v;
        charger_list(variable);
    }
    private void charger_list(List<objet> variable) {
        for(objet l:variable){
            for(int i:l.valeur_domain){
                for(constraint c:l.constraint){
                     l.last.add(new last(i,c.scope.split(" ")[1]));
                }
            }
        }
    }
    List<objet> dimarer(){
          if(go()){return variable;}
          else{return null;}
      }

    String tostring(String v){
         String s="";
         for(int i=1;i<v.toCharArray().length;i++){
             s+=v.toCharArray()[i];
         }
         return s;
     }
    private boolean go() {
        List<objet> Q=new ArrayList();
        for(objet i:variable){
            for(constraint j : i.constraint){
                if(revise(i,variable.get(Integer.parseInt(tostring(j.scope.split(" ")[1]))),j.relation.Tuples,j.relation.semantics,false)){
                    if(i.valeur_domain.isEmpty()){return false;}
                    if(!i.t1){
                      //  Application.node_visit++;
                        i.t1=true;
                        Q.add(i);
                    }
                    
                }
            }
        }
       return propagation(Q);
    }
    private boolean propagation(List<objet> Q) {
        while(!Q.isEmpty()){
            int t=-1;
            objet x=Q.get(0);
            x.t1=false;
            Q.remove(0);
            for(objet i:variable){
              for(constraint j:i.constraint){
                  if(j.scope.equals(i.name+" "+x.name)){
                    if(revise(i,x,j.relation.Tuples,j.relation.semantics,true)){
                        if(variable.get(Integer.parseInt(tostring(j.scope.split(" ")[1]))).valeur_domain.isEmpty()){return false;}
                        if(!variable.get(Integer.parseInt(tostring(j.scope.split(" ")[1]))).t1){
                           //  Application.node_visit++;
                             Q.add(i);
                             i.t1=true;
                             t=Integer.parseInt(tostring(j.scope.split(" ")[1]));
                        }
                    }
                  }
              }
            }
            if(t!=-1){
                variable.get(t).delta.clear();
                t=-1;
            }
            
        }
        return true;
    }
     
    private boolean revise(objet i, objet v, List<String> l,String semantics,boolean lazymode) {
         boolean change = false;
         for(int j=0;j<i.valeur_domain.size();j++){
             for(last s:i.last){
                 if(j>=0){
                    if(s.i==i.valeur_domain.get(j) & s.v1.equals(v.name)){
                           if(!lazymode | test(s.Support,v.delta)){
                                  if(!Support(s,v.valeur_domain,l,semantics)){
                                      i.delta.add(i.valeur_domain.get(j));
                                      i.valeur_domain.remove(j);
                                      change=true;
                                      j--;
                                  }
                           }
                    }
                 }
             }
         }
         return change;
    }
    private boolean test(int Support, List<Integer> delta) {
        for(int i:delta){
            if(i==Support){
                return true;
            }
        }
        return false;
    }

    private boolean Support(last s, List<Integer> valeur_domain, List<String> l, String semantics) {
     MAC_all_solution.c++;
       if(semantics.equals("conflicts")){
            int f=0;
            for(int i:valeur_domain){
               for(String s1:l){
                    if(s1.equals(s.i+" "+i)){
                      f++;
                   }else{
                      s.Support=i;
                    }
                }
            }if(f==valeur_domain.size()){return false;}else{return true;}
        }else{
            for(int i:valeur_domain){
                for(String s1:l){
                    if(s1.equals(s.i+" "+i)){
                        s.Support=i;
                      return true;
                   }
                }
            }return false;
        }
    }
    
}
class last{
    int i;
    String v1;
    int Support;
    last(int j,String s1){
         i=j; v1=s1;
    }
}
class domain{
        String name;
        List<Integer> valeur=new ArrayList(); 
        domain(String N,String V){
            name=N;
            int b =Integer.parseInt(V.split("\\.\\.")[0]);
            int b1=Integer.parseInt(V.split("\\.\\.")[1]);
          for(int i=b;i<=b1;i++){valeur.add(i);}
        }
		
    }
class objet{
        String name ;
        String name_domain;
        int valeur=-1;
        Byte id;
        List<Integer> valeur_domain = new ArrayList();
        List<constraint> constraint = new ArrayList();
        List<String> var_supp=new ArrayList();
        List<Integer> delta = new ArrayList();
        List<last> last = new ArrayList();
        boolean t=false;
        boolean t1=false;
        objet(String N,String ND){
            name = N;
            name_domain=ND;
        }
         objet(Byte ID,String N,String ND){
            id=ID;
            name = N;
            name_domain=ND;
        }
        objet(String n ,String nd,int v ,List<Integer> vd ,List<constraint> c ){
             name =n ;
             name_domain=nd;
             valeur=v;
             valeur_domain.addAll(vd);
             constraint = c;
        }
        objet(String n ,String nd,int v ,int vd ,List<constraint> c ){
             name =n ;
             name_domain=nd;
             valeur=v;
             valeur_domain.add(vd);
             constraint = c;
        }
    }
class constraint{
        String name ;
        int arity;
        String scope;
        String reference;
        relations relation;
        constraint(String N,int A,String S,String R){
            name=N;
            arity=A;
            scope=S;
            reference=R;
        }
          constraint(String N,int A,String S,String R,relations Re){
            name=N;
            arity=A;
            scope=S.split(" ")[1]+" "+S.split(" ")[0];
            reference=R;
            relation=new relations(Re.name,Re.arity,Re.semantics,Re.Tuples);
            
        }
    }
class relations{
        String name ;
        int arity;
        String semantics;
        List<String> Tuples = new ArrayList();
      
        relations(String N,int A,String S,String T){
            name=N;
            arity=A;
            semantics=S;
            Tuples.addAll(Arrays.asList(T.split("\\|")));
        }
         relations(String N,int A,String S,ArrayList<Integer[]> T){
            name=N;
            arity=A;
            semantics=S;
            for(int i=0;i<T.size();i++){
                Tuples.add(T.get(i)[0]+" "+T.get(i)[1]);
            }
            //Tuples.addAll(Arrays.asList(T.split("\\|")));
        }
        relations(String N,int A,String S,List<String> T){
            name=N;
            arity=A;
            semantics=S;
            for(int i=0;i<T.size();i++){
                Tuples.add(T.get(i).split(" ")[1]+" "+T.get(i).split(" ")[0]);
            }
        }
    }