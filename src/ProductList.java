import java.io.BufferedWriter;
import java.io.FileWriter;

public class ProductList {
	
	private ProductRecord[] records;
	private String fileName;
	
	ProductList() {
		
	}
	
	ProductList(String fileName) {
		parseFromFile(fileName);
	}
	
	void parseFromFile(String fileName) {
		this.fileName = fileName;
		ProductFileParser parser = new ProductFileParser();
		records = parser.parse(fileName);
	}
	
	public void displayProductList(String productListFileName) {
		
		parseFromFile(productListFileName);
		
		if (records == null) return;
		
		if (duplicateExists()) {
			System.out.println("ID conflict");
			return;
		}
		
		if (records[0] != null) {
			// Print header.
			System.out.printf("%-15s%-15s%-10s%10s%15s%15s%10s\n", 
							  "Product Name", "Product ID", "Category",
							  "Price", "Current Stock", "Safe Level", "Memo");
			for(int i = 0; i < 90; i++) System.out.print("-");
			System.out.print("\n");
			
			
			for (ProductRecord record : records) {
				if (record == null) break;
				record.print();
			}
		}
		else System.out.println("The list is empty.");
	}
	
	boolean duplicateExists() {
		
		for (int i = 0; i < records.length; i++) {
			if (records[i] == null) break;
			
			for (int j = i + 1; j < records.length; j++) {
				if (records[j] == null) break;
				
				if (records[i].getId().equals(records[j].getId())) return true;
			}
		}
		
		return false;
	}
	
	ProductRecord[] getRecords() {
		return records;
	}
	
	int getRecordCount() {
		int count = 0;
		int i = 0;
		
		while (records[i++] != null) count++;
		
		return count;
	}
	
	void addRecord(ProductRecord newRecord) throws Exception {
		
		int count = getRecordCount();
		
		if (count >= records.length) {
			throw new Exception("Too many records.");
		}
		
		records[count] = newRecord;
		
		if (duplicateExists()) {
			records[count] = null;
			throw new Exception("Duplicate exists.");
		}
	}
	
	void deleteRecord(int index) throws Exception {
		if (index < 0 || index >= records.length) throw new Exception("Out of bound.");
		if (records[index] == null) throw new Exception("Does not exist.");
		
		for (int i = index; i < getRecordCount(); i++) {
			records[i] = records[i + 1];
		}
	}
	
	void modifyRecord(int index, ProductRecord newRecord) throws Exception {
		if (index < 0 || index >= records.length) throw new Exception("Out of bound.");
		if (records[index] == null) throw new Exception("Does not exist.");
		
		ProductRecord temp;
		
		temp = records[index];
		records[index] = newRecord;
		
		if (duplicateExists()) {
			records[index] = temp;
			throw new Exception("Duplicate exists.");
		}
	}
	
	void saveToFile() throws Exception {
		
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		
		for (ProductRecord record: records) {
			if (record == null) break;
			
			Object[] recordData = record.getArray();
			String s = (String)recordData[0] + ":" +
					   (String)recordData[1] + ":" +
					   (Integer)recordData[3] + ":" +
					   (Integer)recordData[4] + ":" +
					   (Integer)recordData[5] + ":" +
					   (String)recordData[6];
			out.write(s);
			out.newLine();
		}
		
		out.close();
		
		parseFromFile(fileName);
	}

}
