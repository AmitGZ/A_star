import java.util.LinkedList;
/**
 * Linked list constructor used to implement Open list
 */
public class List {
	private static LinkedList<Pixel> list;
	//Constructor
	List(){
		list = new LinkedList<Pixel>();
	}
	/**
	 * This method adds a pixel to the linked list based on its f_cost
	 * @param p a pixel to add
	 */
	public static void addOrganize(Pixel p){
		int index =0;
		while(index < list.size())
		{
			if(p.getFCost() < list.get(index).getFCost()) {
				list.add(index, p);
				return;
			}
			index++;
		}
		list.addLast(p);
	}
	/**
	 * This method removes a pixel from the linked list
	 * @param p a pixel to add
	 */
	public static void remove(Pixel p){
		for(int i=0 ;i<list.size(); i++)
			if(p == list.get(i))
				list.remove(i);
	}
	//This method removes the first pixel in the list
	public static Pixel pop(){
		return list.removeFirst();
	}
	//This method returns linked list size
	public static boolean isEmpty(){
		return (list.size() == 0);
	}
	//This method clears the list
	public static void clearList(){
		list.clear();
	}

}
