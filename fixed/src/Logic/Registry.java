package Logic;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

public class Registry{
	static protected HashMap<Integer, LeasingContract > storeLeasingWithId = new HashMap<Integer, LeasingContract>();
	static protected HashMap<Integer, HiringContract> storeHiringWithId = new HashMap<Integer, HiringContract>();
	static protected HashMap<Immobility, TreeSet<LeasingContract>> storeLeasing = new HashMap<Immobility, TreeSet<LeasingContract>>();
	static protected HashMap<Immobility, TreeSet<HiringContract>> storeHiring = new HashMap<Immobility, TreeSet<HiringContract>>();

	//добавить контракт в соотвествующее его объекту недвижимости отсортированное множество
	static protected void addToStoreLeasing(LeasingContract contract){
		Immobility im = contract.getImmobility();
		if (storeLeasing.get(im) == null)
			storeLeasing.put(im, new TreeSet<LeasingContract>());
		storeLeasing.get(im).add(contract);
	}
	// добавить контракт в соотвествующее его объекту недвижимости отсортированное множество
	static protected void addToStoreHiring(HiringContract contract){
		Immobility im = contract.getImmobility();
		if (storeHiring.get(im) == null)
			storeHiring.put(im, new TreeSet<HiringContract>());
		storeHiring.get(im).add(contract);
	}
	static public TreeSet<Contract> getLeasing(Immobility im){
		TreeSet<Contract> ret = new TreeSet<Contract>();
		TreeSet<LeasingContract> tmp = storeLeasing.get(im);
		if(tmp == null) return ret;
		for(LeasingContract con: tmp)
			ret.add((Contract)con);
		return ret;
	}
	static public TreeSet<Contract> getHiring(Immobility im){
		TreeSet<Contract> ret = new TreeSet<Contract>();
		TreeSet<HiringContract> tmp = storeHiring.get(im);
		if(tmp == null) return ret;
		for(HiringContract con: tmp)
			ret.add((Contract)con);
		return ret;
	}
	
	
//	создать список ret для объектов типа Immobility;
// 	для каждого элемента(curImmobility) из коллекции Manager<Immobility>.getStore()
//
//			если интервал req не полностью покрывается соответствующими 	
//				объекту curImmobility
//				элементами storeLeasing, то перейти к следующему эл-ту
//			если интервал пересекается с соответствующими 		
//				объекту curImmobility	
//				элементами storeHiring, то перейти к следующему эл-ту
//			вызвать метод test поля immobility с параметром req,  в случае 
//			если вызов для объекта завершились с возвратом true, добавить в 	
//			список ret значение протестированного объекта
//	вернуть значение ret;
	
	static public List<Immobility> responseSearchRequest (Requirements req){
		TreeSet<Immobility> ts = new TreeSet<Immobility>();
		ts.addAll(Global.inhabitedImmobilities.getStore());
		ts.addAll(Global.uninhabitedImmobilities.getStore());
		ArrayList<Immobility> ret = new ArrayList<Immobility>();

		Date from = null;
		Date to = null;
		
		for(String s : req.getRequirements()){
			StringTokenizer st = new StringTokenizer(s);
			if(!st.nextToken().equals("free")) continue;
			try {
				String type = st.nextToken();
				if(type.equals("before")) to = Global.getDateFormat().parse(st.nextToken());
				if(type.equals("after"))  from = Global.getDateFormat().parse(st.nextToken());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		if(from == null) to = null;
		if(to == null) from = null;
		for(Immobility im : ts){
			if(from != null && !checkCover(from, to, getLeasing(im))) continue;
			if(from != null && checkIntersect(from, to, getHiring(im))) continue;
			if(!im.test(req)) continue;
			ret.add(im);
		}
		return ret;
	
	}
//	проверить на непересечение с аналогичными контрактами во времени, и добавить, если всё в порядке, в 
//		вызвать метод put объекта storeLeasingWithId с параметрами (contract.getId(), contract)
//		вызвать метод  addToStoreLeasing с параметром contract
	static public void addLeasingContract (LeasingContract contract) throws Exception { 

		if(checkIntersect(contract.getDate(), contract.getIssue(), getLeasing(contract.getImmobility())))
			throw new Exception("Пересекается с аналогичныым контрактом"); 
		storeLeasingWithId.put(contract.getId(), contract);
		addToStoreLeasing(contract);
		
	}
//		проверить на непересечение с аналогичными контрактами во времени, и полное 					покрытие другими
//		if(Register.checkIntersect(contract.getDate(), contract.getIssueDate(), 
//			getHiring(contract.getImmobility))) throw new 
//			RuntimeException(“Пересекается с аналогичныым контрактом”); 
//		if(!Register.checkCover(contract.getDate(), contract.getIssueDate(), 
//			getLeasing(contract.getImmobility))) throw new 
//			RuntimeException(“Не полностью покрывается контрактами сдачи”); 
//
//		вызвать метод put объекта storeHiringWithId с параметрами contract.getId(), contract
//		вызвать метод  addToStoreHiring с параметром contract
	static public void addHiringContract (HiringContract contract) {

		if(checkIntersect(contract.getDate(), contract.getIssue(), getHiring(contract.getImmobility()))) 
			throw new RuntimeException("Пересекается с аналогичныым контрактом");
		 
		if(!checkCover(contract.getDate(), contract.getIssue(), getLeasing(contract.getImmobility()))) 
			throw new RuntimeException("Не полностью покрывается контрактами сдачи"); 
		storeHiringWithId.put(contract.getId(), contract);
		addToStoreHiring(contract);
	}

	//обновляет дату истечение соответствующего договора
	static public void updateIssueDateOfHiringContract(HiringContract contract, Date newIssueDate){
		contract.checkNewIssueDate(newIssueDate);
		removeHiringContract(contract);
		
		int runTime = 0;
		if(checkIntersect(contract.getDate(), contract.getIssue(), getHiring(contract.getImmobility())))
			runTime = 1;
			
		 
		if(!checkCover(contract.getDate(), contract.getIssue(), getLeasing(contract.getImmobility()))) 
			runTime = 2;
		
		if(runTime != 0){
			addHiringContract(contract);
		} else {
			if(runTime == 1)
				throw new RuntimeException("Пересекается с аналогичныым контрактом");
			if(runTime == 2)
				throw new RuntimeException("Не полностью покрывается контрактами сдачи");
		}
		
		
		contract.setNewIssueDate(newIssueDate);
		addHiringContract(contract);
	}
	//удаляет договор из реестра, private метод
	private static void removeHiringContract(HiringContract contract) {
		storeHiringWithId.remove(contract.getId());
		storeHiring.get(contract.getImmobility()).remove(contract);
	}
	
	//обновляет дату истечение соответствующего договора
	static public void updateIssueDateOfLeasingContract(LeasingContract contract, Date newIssueDate) throws Exception{
		contract.checkNewIssueDate(newIssueDate);
		
		removeLeasingContract(contract);
		boolean runTime = false;
		if(checkIntersect(contract.getDate(), newIssueDate, getLeasing(contract.getImmobility())))
			runTime = true;
		
		if(runTime){
			addLeasingContract(contract);
			throw new Exception("Пересекается с аналогичныым контрактом");
		}
		
		contract.setNewIssueDate(newIssueDate);
		addLeasingContract(contract);
	}
	//удаляет договор из реестра, private метод
	private static void removeLeasingContract(LeasingContract contract) {
		storeLeasingWithId.remove(contract.getId());
		storeLeasing.get(contract.getImmobility()).remove(contract);
	}
//	возвращает contract с указанным id, если такой был добавлен ранее, иначе null
	static public Contract getContract(int id){
		Contract ret = storeLeasingWithId.get(id);
		if(ret == null) ret = storeHiringWithId.get(id);
		return ret;
	}
//		 проверить, покрывается ли данный отрезок договорами из набора ts полностью
	static protected boolean checkCover(Date from, Date to, TreeSet<Contract> treeSet){ 
		for(Contract contract : treeSet){
			if(dif(from, contract.getDate()) >= 0 && dif(to, contract.getIssue()) <= 0) return true;
		}
		return false;
	}
	public static LeasingContract getActiveLeasingContract(Immobility im, Date from, Date to){
		TreeSet<Contract> ts = getLeasing(im);
		for(Contract contract : ts){
			if(dif(from, contract.getDate()) >= 0 && dif(to, contract.getIssue()) <= 0) return (LeasingContract) contract;
		}
		return null;
	}
	
//		 проверить, пересекается ли данный отрезок с договорами из набора ts
	static protected boolean checkIntersect(Date from, Date to, TreeSet<Contract> treeSet){
		for(Contract contract : treeSet){
			Date minR = min(to, contract.getIssue());
			Date maxL = max(from, contract.getDate());
			if(dif(maxL, minR) <= 0) return true;
		}
		return false;
	}
	
	static Date max(Date a, Date b){
		return dif(a, b) > 0 ? a : b;
	}
	static Date min(Date a, Date b){
		return dif(a, b) > 0 ? b : a;
	}
	static int dif(Date a, Date b){
		if(a.getYear() != b.getYear()) return a.getYear() - b.getYear();
		if(a.getMonth() != b.getMonth()) return a.getMinutes() - b.getMonth();
		if(a.getDate() != b.getDate()) return a.getDate() - b.getDate();
		return 0;
	}
	
//		для соответствующих данной недвижимости договоров съема(getHiring)
//			найти максимум среди их времён истечения
//		вернуть максимум из найденного значения и текущей даты
	static public Date getEarliestIssueDate (Immobility im, Date from, Date to){
		TreeSet<Contract> ts = getHiring(im);
		Date out = new Date();
		for(Contract contract : ts){
			Date minR = min(to, contract.getIssue());
			Date maxL = max(from, contract.getDate());
			if(dif(maxL, minR) > 0) continue;
			
			if(dif(out, contract.getIssue()) < 0){
				out = contract.getIssue();
			}
		}
		return out;
	}

//	записать в поток  размер storeLeasingWithId
//	для каждого значения storeLeasingWithId вызвать метод saveToStream(out) 
//	записать в поток  размер storeHiringWithId
//	для каждого значения storeHiringWithId вызвать метод saveToStream(out)
	
	static public void saveToStream(PrintWriter out){
		out.println(storeLeasingWithId.size());
		for(Entry<Integer, LeasingContract> en : storeLeasingWithId.entrySet()){
			en.getValue().saveLeasingContractToStream(out);
		}
		
		out.println(storeHiringWithId.size());
		for(Entry<Integer, HiringContract> en : storeHiringWithId.entrySet()){
			en.getValue().saveHiringContractToStream(out);
		}
		
	}

//	считать число из потока – кол-во элементов списка
//	нужное кол-во раз:
//		вызвать статический метод loadFromStream(in) класса LeasingContract
//		полученный объект добавить в коллекцию вызовом addLeasingContract 
//	считать число из потока – кол-во элементов списка
//	нужное кол-во раз:
//		вызвать статический метод loadFromStream(in) класса HiringContract
//		полученный объект добавить в коллекцию вызовом addHiringContract 
	static public void loadFromStream(BufferedReader in) throws IOException, ParseException{
		{
			int cnt = Integer.parseInt(in.readLine());
			for(int i = 0; i < cnt; i++){
				LeasingContract contract = new LeasingContract(in);
				storeLeasingWithId.put(contract.getId(), contract);
				addToStoreLeasing(contract);
			}
		}
		
		{
			int cnt = Integer.parseInt(in.readLine());
			for(int i = 0; i < cnt; i++){
				HiringContract contract = new HiringContract(in);
				storeHiringWithId.put(contract.getId(), contract);
				addToStoreHiring(contract);
			}
		}
		
	}
	public static Collection<LeasingContract> getAllLeasingContracts() {
		return storeLeasingWithId.values();
	}
	
	public static Collection<HiringContract> getAllHiringContracts() {
		return storeHiringWithId.values();
	}
}
