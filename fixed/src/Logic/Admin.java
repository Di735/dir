package Logic;
import java.io.*;

public class Admin extends Employee{

	public final static int  ROLE_ADMIN = 0, 		
	ROLE_ESTATE_AGENT = 1, ROLE_CALL_AGENT = 2;
	public Admin(String name) throws IllegalAccessException {
		super(name);
	}
	protected Admin(BufferedReader in) throws IOException { super(in); loadAdminFromStream(in); }
	public void saveAdminToStream(PrintWriter out){
		super.saveEmployeeToStream(out);
	}
	public void loadAdminFromStream(BufferedReader in){
	}
	
	/*создаёт объект типа,соответствующего role, с именем name
		проверяет вызовом containsValue() соответствующего менеджера, был ли создан 
		такой объект ранее, в случае если объект уже был создан – возвращает false, иначе
		добавляет созданный объект в соответствующий менеджер и возвращает true*/
	
	
	// TODO global
	public boolean  createUser(String name,  int role) throws IllegalAccessException{
		
		switch(role){
			case ROLE_ADMIN:{
				Admin adm = new Admin(name);
				if(Global.admins.containsValue(adm)) return true;
				Global.admins.add(adm);
				return true;
			}
			case ROLE_ESTATE_AGENT:{
				EstateAgent estA = new EstateAgent(name);
				if(Global.estateAgents.containsValue(estA)) return true;
				Global.estateAgents.add(estA);
				return true;
			}
			case ROLE_CALL_AGENT:{
				CallAgent callA = new CallAgent(name);
				if(Global.callAgents.containsValue(callA)) return true;
				Global.callAgents.add(callA);
				return true;
			}
			default: throw new RuntimeException("таких ролей не бывает! :)");
		}
	}
} 
