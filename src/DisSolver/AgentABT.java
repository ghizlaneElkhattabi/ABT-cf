package DisSolver;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
@SuppressWarnings("serial")
public class AgentABT extends Agent {
	Boolean start = false;
	SimpleAgent agt;
	Object[] args;
	Boolean SolutionRemoved = false;
    Timer timer;
    double silence=0, maxcyle = 30;
    boolean SendSilence = false;


    protected void setup() 
	{
    	args = getArguments();
    	agt = new SimpleAgent(this.getLocalName(), (String) args[0]);
		timer = new Timer();
		maxcyle = Integer.parseInt((String) args[2]);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName("Meeting");
		sd.setType("Meeting");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		agt.setMyValue(null);
		addBehaviour(new CyclicBehaviour() {
			@Override
			public void action() 
			{
				removeTimer(this);
                MessageTemplate StartTemp = MessageTemplate.MatchPerformative(Message.START);
				ACLMessage Start =  myAgent.receive(StartTemp);
				if (Start!= null) 
				{
					agt.setAgentC_Cost(agt.getAgentC_Cost()+agt.getStatistics().getCstrs());
					agt.getStatistics().IncrementMSGs();
					agt.getStatistics().setCstrs(0);
					CheckAgentView();
					addBehaviour(new MainReception());
					temps();
					removeBehaviour(this);
				}
			}
		}); 
		// ---------------------------------------------------
	}
	
	public void temps()
    {
    int delay = 1000;
    int period = 1000;
    timer.scheduleAtFixedRate(new TimerTask() 
    {
        @Override
        public void run() 
        {
                if (silence >= maxcyle && !SendSilence)
                 {
                	SendSilence = true;
                	ACLMessage cfp = Message.CreatMessage(Message.SILENCE, "MASTER", "silence", agt.getStatistics().getMSGs()+";"+agt.getAgentC_Cost(),0);
                	send(cfp);
    				silence = 0;
                 }
                 silence ++;
                  
        }
    }, delay, period);
    }
	
	private class MainReception extends CyclicBehaviour
	{
		MessageTemplate ok = MessageTemplate.MatchPerformative(Message.OK); 
		MessageTemplate adl = MessageTemplate.MatchPerformative(Message.ADL);
		MessageTemplate nogood = MessageTemplate.MatchPerformative(Message.NOGOOD);
		MessageTemplate stop = MessageTemplate.MatchPerformative(Message.STOP);
		MessageTemplate Stats  = MessageTemplate.MatchPerformative(Message.STATISTICS);
														
		ACLMessage msgOK, msgadl, msgNOGOOD, msgSTOP,msgSTATS;

		@Override
		public void action() 
		{
			msgSTATS = myAgent.receive(Stats);
			if(msgSTATS != null)
			{
				ACLMessage cfp = Message.CreatMessage(Message.STATISTICS, "MASTER", "silence", ""+agt.getStatistics().getMSGs()+";"+agt.getAgentC_Cost(),0);
				send(cfp);
//				takeDown();
//				removeBehaviour(this);
			}
			
			msgOK = myAgent.receive(ok);
			if (msgOK != null) {
				agt.getStatistics().IncrementMSGs();
				if(Integer.parseInt(msgOK.getConversationId())> agt.getAgentC_Cost())
				{
					agt.setAgentC_Cost(Integer.parseInt(msgOK.getConversationId()));
				}
				setSilence();
				String title = msgOK.getContent();
//				System.out.println("\n(" + msgOK.getSender().getLocalName()	+ " --> " + getAID().getLocalName() + ") : ok? : "+ title + "\n");
				ProcessInfo(msgOK.getSender().getLocalName(), new Solution(title));
			}
			
			msgadl = myAgent.receive(adl);
			if (msgadl != null) {
				agt.getStatistics().IncrementMSGs();
				setSilence();
				String title = msgadl.getContent();
//				System.out.println("(" + msgadl.getSender().getLocalName()+ " --> " + getAID().getLocalName() + ") : " + title);
				SetLink(msgadl.getSender().getLocalName());
			}
			
			msgNOGOOD = myAgent.receive(nogood);
			if ((msgNOGOOD != null)&&(msgNOGOOD.getContent().contains(">"))) 
			{
				agt.getStatistics().IncrementMSGs();
				setSilence();
				ResolveConflict(msgNOGOOD.getContent(), msgNOGOOD.getSender().getLocalName());
			}
			
			msgSTOP = myAgent.receive(stop);
			if (msgSTOP != null) {
				agt.getStatistics().IncrementMSGs();
				timer.cancel();
//				System.out.println(agt.getMyValue().getAgentOwner(agt.getAgentsName())+"    --->    "+agt.getMyValue().toString()+"           {"+agt.getStatistics()+"   "+agt.getAgentC_Cost()+"}");
//				takeDown();
//				removeBehaviour(this);
			}
		}
	}
	
	public void SetLink(String Agent)
	{
		agt.getChildren().add(Agent);
		if(agt.getMyValue() == null)
		{
			ACLMessage cfp = Message.CreatMessage(Message.NOGOOD, "MASTER", "", "",0);
		}
		sendOk(Agent, agt.getAgentC_Cost());
	}
	
	public void sendOk(String Agent, int cost)
	{
		ACLMessage cfp = Message.CreatMessage(Message.OK, Agent, "("+agt.getMyValue().toString()+")", "",cost);
		send(cfp);
	}
	public void CheckAddLink(Nogood ngd)
	{
		for(Solution sol : ngd.getLHS())
		{
			if(!Tools.ListcontainExact(agt.getNamesOfParents(), sol.getAgentOwner(agt.getAgentsName())))
			{
				ACLMessage cfp = Message.CreatMessage(Message.ADL, sol.getAgentOwner(agt.getAgentsName()), "adl", "AddLink",0);
				send(cfp);
				agt.AddToParent(sol.getAgentOwner(agt.getAgentsName()));
			}
		}
	}
	
	public void ProcessInfo(String Agent, Solution Msg)
	{
		agt.getStatistics().setCstrs(0);
		Update(Agent, Msg, false);
		CheckAgentView();
	}
	
	public void ResolveConflict(String message, String agent)
	{
		Nogood ngd = new Nogood(message);
		ArrayList<Solution> ElementNotExistInAgentView = new ArrayList<Solution>();
		ElementNotExistInAgentView = agt.NogoodCompatibleWithAgentView(ngd);
		if(ElementNotExistInAgentView != null)
		{
//			System.out.println("\n( "+agent+" --> "+ngd.getAgentTarget(agt.getAgentsName())+" )   :  ngd  :  " + message+"\n");
			CheckAddLink(ngd);
			for(int i = 0; i<ElementNotExistInAgentView.size(); i++)
			{
				Update(ElementNotExistInAgentView.get(i).getAgentOwner(agt.getAgentsName()), ElementNotExistInAgentView.get(i), false);
			}
			//not just the current ngd, but all NPI solution
			agt.AddToNogoodstore(ngd);
			SolutionRemoved = true;
			CheckAgentView();
		}
		else 
		{
//			System.err.println("\n non accept� ( "+agent+" --> "+ngd.getAgentTarget(agt.getAgentsName())+" )   :  ngd  :  " + message+"\n");
			if(agt.NogoodCompatibleWithAgentView(new Nogood(new ArrayList<Solution>(), ngd.getRHS())) != null)
			{
				sendOk(agent, agt.getAgentC_Cost());
			}
		}
	}

	public void Update (String Agent, Solution Msg, boolean SolIsNull)
	{
		agt.UpdateAgentView(Agent, Msg, SolIsNull);
		agt.removeIncompatiblesNogoods(Msg, SolIsNull);
	}
	public void setSilence()
	{
		if (SendSilence) 
		{
			ACLMessage cfp = Message.CreatMessage(Message.DISCONFIRMSILENCE, "MASTER", "", "",0);
			send(cfp);
		}
		SendSilence = false;
		silence = 0;
	}
	
	
	public void Backtrack()
	{
		agt.setNogood(agt.ConstructNogood());
		if(agt.getNogood() != null)
		{
			ACLMessage cfp = Message.CreatMessage(Message.NOGOOD, agt.getNogood().getAgentTarget(agt.getAgentsName()), agt.getNogood().toString(), "",0);
			send(cfp);
			Update(agt.getNogood().getAgentTarget(agt.getAgentsName()), agt.getNogood().getRHS(), true);
			CheckAgentView();
		}
		else
		{
			ACLMessage cfp = Message.CreatMessage(Message.NOGOOD, "MASTER", "", "",0);
			send(cfp);
		}
	}
	
	public void CheckAgentView()
	{
		Boolean solutionIsConsistent = true;
		if(agt.getMyValue()!=null && !SolutionRemoved)
		{
			agt.getMyValue().ClearNbrConstraints();
			solutionIsConsistent = (agt.getMyValue().isConsistent(agt.getAgentView(), agt.getParentsConstraints()) == null);
			agt.getStatistics().IncrementCstrs(agt.getMyValue().getNbrConstraintsTested());
		}
		else
		{
			solutionIsConsistent = false;
		}
		if( !solutionIsConsistent )
		{
			if(agt.ChooseValue() != null)
			{
				SolutionRemoved = false;
				agt.setAgentC_Cost(agt.getAgentC_Cost()+agt.getStatistics().getCstrs());
				for (int i=0; i < agt.getChildren().size(); i++)  
				{
					if(agt.getChildren().get(i).length()>1)
					{
//						send: OK 
						if(agt.getMyValue() == null)
						{
							ACLMessage cfp = Message.CreatMessage(Message.NOGOOD, "MASTER", "", "",0);
							send(cfp);
							return;
						}
						sendOk(agt.getChildren().get(i), agt.getAgentC_Cost());
					}
				}
				agt.getStatistics().setCstrs(0);
				return;
			}
			else{
				agt.setAgentC_Cost(agt.getAgentC_Cost()+agt.getStatistics().getCstrs());
				agt.getStatistics().setCstrs(0);
				Backtrack();
			}
		}
		else
		{
			agt.setAgentC_Cost(agt.getAgentC_Cost()+agt.getStatistics().getCstrs());
			agt.getStatistics().setCstrs(0);
		}
	}
}
