package Logic;
import java.io.*;
public class EstateAgent extends Employee{
	public EstateAgent(String name) throws IllegalAccessException {
		super(name);
	}
	public EstateAgent(BufferedReader in) throws IOException { super(in); loadEstateAgentFromStream(in); }

	public void signLeasingContract (LeasingContract contract) throws Exception{ 
		Registry.addLeasingContract(contract);
	}
	public void signHiringContract (HiringContract contract) { 
		Registry.addHiringContract(contract);
	}
	public void saveEstateAgentToStream(PrintWriter out){
		super.saveEmployeeToStream(out);
	}
	public void loadEstateAgentFromStream(BufferedReader in) throws IOException {
	}
}
