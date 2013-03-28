package Logic;
import java.io.*;

public class Employee extends Person{
	protected String password; 
	protected Employee(BufferedReader in) throws IOException{
		super(in);
		loadEmployeeFromStream(in);
	}
	public Employee(String name) throws IllegalAccessException{
		super(name);
		password = "";
	}
	/* Возвратить результат сравнения используемого пароля с pass на эквивалентность, 
	удалив из pass все вхождения символа ‘|’; */
	
	public boolean authorize(String pass){
		while(pass.contains("|"))
			pass = pass.replaceAll("|", "");
		return pass.equals(password);
	}
	//присвоить полю сотруднику password значение параметра pass, удалив из него вхождения символа ‘|’;
	public void setPassword (String pass){
		while(pass.contains("|"))
			pass = pass.replaceAll("|", "");
		password = pass;
	}
	/*
	 * вызвать метод saveToStream родительского класса
	записать в поток все поля(в порядке: password)
	 */
	public void saveEmployeeToStream(PrintWriter out){
		super.savePersonToStream(out);
		out.println(password);
	}
	/*
	 * загрузить объект
	 * сначала поля родительского класса
	 * потом поле password
	 * 
	 */
	public void loadEmployeeFromStream(BufferedReader in) throws IOException{
		setPassword(in.readLine());
	}
	/*вернуть отрицательное(положительное) челое число, если поле name текущего 
			объекта лексикографически меньше(больше) поля name объетка p
		в случае лексикографического равенства вернуть 0.*/
	
	@Override
	public int compareTo(Person p){ 
		return getName().compareTo(p.getName());
	}
}
