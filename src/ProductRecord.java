public class ProductRecord {
	
	private String name;
	private String id;
	private String category;
	private int price;
	private int currentStock;
	private int minStock;
	private String memo;
	
	ProductRecord() {
		
	}
	
	ProductRecord(String newName, String newId, String newCategory, 
				  int newPrice, int newCurrentStock,
				  int newMinStock, String newMemo) {
		name = newName;
		id = newId;
		category = newCategory;
		price = newPrice;
		currentStock = newCurrentStock;
		minStock = newMinStock;
		memo = newMemo;
	}
	
	String getId() {
		return id;
	}
	
	public void print() {
		System.out.printf("%-15s%-15s%-10s%10d%15d%15d%10s\n",
						  name, id, category, price, currentStock, minStock, memo);
	}
	
	Object[] getArray() {
		if (memo == null) memo = "";
		Object[] array = {name, id, category, price, currentStock, minStock, memo};
		return array;
	}
}
