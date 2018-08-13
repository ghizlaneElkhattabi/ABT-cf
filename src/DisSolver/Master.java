package DisSolver;

public class Master {

    public static void main(String[] args) {

        int nbAgents = 4;

        DisSolver js = new DisSolver();
        js.setType("MasterABT");
        js.setNumberOfAgents(nbAgents);
        js.setGui(true);
        js.run();

        DisSolver ds1 = new DisSolver();
        ds1.setType("AgentABT");

        ds1.addAgent("A1", "Problemm1.xml", "A", 80);
        ds1.addAgent("A2", "Problemm2.xml", "A", 80);
        ds1.addAgent("A3", "Problemm3.xml", "A", 80);
        ds1.addAgent("A4", "Problemm4.xml", "A", 80);

        ds1.setContainer("localhost");
        ds1.run();
    }

}
