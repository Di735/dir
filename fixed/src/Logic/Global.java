package Logic;
import java.io.*;
import java.text.*;


public class Global {
	public static Employee activeUser = null;
	public final static String PATH_TO_IMAGES_FOLDER = "Images/";
	
	public final static Manager<Customer> customers = new Manager<Customer>(new LoaderSaver<Customer>() { 
		public Customer loadFromStream(BufferedReader in) throws IOException {return new Customer(in);}
		public void saveToStream(PrintWriter out, Customer obj) { obj.saveCustomerToStream(out);}
	});
	public final static Manager<CallAgent> callAgents = new Manager<CallAgent>(new LoaderSaver<CallAgent>() {
		public CallAgent loadFromStream(BufferedReader in) throws IOException {return new CallAgent(in);}
		public void saveToStream(PrintWriter out, CallAgent obj) { obj.saveCallAgentToStream(out);}
	});
	public final static Manager<EstateAgent> estateAgents = new Manager<EstateAgent>(new LoaderSaver<EstateAgent>() { 
		public EstateAgent loadFromStream(BufferedReader in) throws IOException {return new EstateAgent(in);}
		public void saveToStream(PrintWriter out, EstateAgent obj) { obj.saveEstateAgentToStream(out);}
	});
	public final static Manager<Admin> admins = new Manager<Admin>(new LoaderSaver<Admin>() { 
		public Admin loadFromStream(BufferedReader in) throws IOException {return new Admin(in);}
		public void saveToStream(PrintWriter out, Admin obj) { obj.saveAdminToStream(out);}
	});
	public final static Manager<Inhabited> inhabitedImmobilities = new Manager<Inhabited>(new LoaderSaver<Inhabited>() { 
		public Inhabited loadFromStream(BufferedReader in) throws IOException {return new Inhabited(in);}
		public void saveToStream(PrintWriter out, Inhabited obj) { obj.saveInhabitedToStream(out);}
	});
	public final static Manager<Uninhabited> uninhabitedImmobilities = new Manager<Uninhabited>(new LoaderSaver<Uninhabited>() { 
		public Uninhabited loadFromStream(BufferedReader in) throws IOException {return new Uninhabited(in);}
		public void saveToStream(PrintWriter out, Uninhabited obj) { obj.saveUninhabitedToStream(out);}
	});
	private Global(){}
	
	public final static Immobility getImmobility(int id){
		Immobility ret = inhabitedImmobilities.getElement(id);
		if(ret == null) ret = uninhabitedImmobilities.getElement(id);
		return ret;
	}
	

	public final static String customersFile = "customersFile.data";
	public final static String callAgentsFile = "callAgentsFile.data";
	public final static String estateAgentsFile = "estateAgentsFile.data";
	public final static String adminsFile = "adminsFile.data";
	public final static String inhabitedImmobilitiesFile = "inhabitedImmobilitiesFile.data";
	public final static String uninhabitedImmobilitiesFile = "uninhabitedImmobilitiesFile.data";
	public final static String registryFile = "registryFile.data";
	public static void save() throws FileNotFoundException{
		PrintWriter out;
		out = new PrintWriter(customersFile);
		customers.saveToStream(out);
		out.close();
		out = new PrintWriter(callAgentsFile);
		callAgents.saveToStream(out);
		out.close();

		out = new PrintWriter(estateAgentsFile);
		estateAgents.saveToStream(out);
		out.close();

		out = new PrintWriter(adminsFile);
		admins.saveToStream(out);
		out.close();

		out = new PrintWriter(inhabitedImmobilitiesFile);
		inhabitedImmobilities.saveToStream(out);
		out.close();

		out = new PrintWriter(uninhabitedImmobilitiesFile);
		uninhabitedImmobilities.saveToStream(out);
		out.close();
		
		out = new PrintWriter(registryFile);
		Registry.saveToStream(out);
		out.close();
		
	}
	public static void load() throws IOException, ParseException{
		BufferedReader in;

		in = new BufferedReader(new FileReader (customersFile));
		customers.loadFromStream(in);
		in.close();
		
		in = new BufferedReader(new FileReader (callAgentsFile));
		callAgents.loadFromStream(in);
		in.close();

		in = new BufferedReader(new FileReader (estateAgentsFile));
		estateAgents.loadFromStream(in);
		in.close();

		in = new BufferedReader(new FileReader (adminsFile));
		admins.loadFromStream(in);
		in.close();

		in = new BufferedReader(new FileReader (inhabitedImmobilitiesFile));
		inhabitedImmobilities.loadFromStream(in);
		in.close();

		in = new BufferedReader(new FileReader (uninhabitedImmobilitiesFile));
		uninhabitedImmobilities.loadFromStream(in);
		in.close();
		
		in = new BufferedReader(new FileReader (registryFile));
		Registry.loadFromStream(in);
		in.close();
		
	}
	
	///
	public static  DateFormat getDateFormat(){
		return new SimpleDateFormat("dd:MM:yyyy");	
	}
}
