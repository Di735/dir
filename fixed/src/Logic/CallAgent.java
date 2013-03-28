package Logic;
import java.io.*;
import java.util.*;

public class CallAgent extends Employee{
	public CallAgent(String name) throws IllegalAccessException {
		super(name);
	}
	protected CallAgent(BufferedReader in) throws IOException { super(in); loadCallAgentFromStream(in); }
	public List<Immobility> sendRequest (Requirements requirement, Registry registry) {
		return Registry.responseSearchRequest(requirement);
	}
	public void saveCallAgentToStream(PrintWriter out){
		super.saveEmployeeToStream(out);
	}
	public void loadCallAgentFromStream(BufferedReader in){
	}
} 
