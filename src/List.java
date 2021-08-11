import java.util.LinkedList;

public class List {
	private static LinkedList<Pixel> list;
	
	List(){
		list = new LinkedList<Pixel>();
	}
	
	public static void addOrganize(Pixel p){
		int index =0;
		while(index < list.size())
		{
			if(p.getFcost() < list.get(index).getFcost()) {
				list.add(index, p);
				return;
			}
			index++;
		}
		list.addLast(p);
	}
	
	public static void remove(Pixel p){
		for(int i=0 ;i<list.size(); i++)
			if(p == list.get(i))
				list.remove(i);
	}
	
	public static Pixel pop(){
		return list.removeFirst();
	}
	
	public static boolean isEmpty(){
		return (list.size() == 0);
	}

}
