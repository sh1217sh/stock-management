public class ProductFileParser {

	ProductFileParser() {
		
	}
	
	ProductRecord[] parse(String fileName) {
		
		java.io.File inputFile = new java.io.File(fileName);
		java.util.Scanner scanner = null;
		
		// Open file.
		try {
			scanner = new java.util.Scanner(inputFile);
		}
		catch (Exception e) {
			System.out.println("Unknown file");
			return null;
		}
		
		// Create new array of product records and return.
		ProductRecord[] records = new ProductRecord[100]; // Assume there's no more than 100 items.
		int i = 0;
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			if (isComment(line) == false) {
				
				if (isIllegal(line)) {
					System.out.println(line);
					
					scanner.close();
					return null;
				}
				if (i > 100) {
					System.out.println("Warning: Only first 100 entries imported");
					
					scanner.close();
					return records;
				}
				
				records[i] = parseLine(line);
				i++;
			}
		}
		
		scanner.close();
		return records;
		
	}
	
	private boolean isComment(String line) {
		
		// If the line is empty.
		if (line.equals("")) return true;
		
		// If the line starts with "//".
		if (line.substring(0, 2).equals("//")) return true;
		
		return false;
	}
	
	private boolean isIllegal(String line) {
		
		String[] splittedLine = splitAndTrim(line);
		
		// Check the number of attributes.
		if (splittedLine.length < 5) {
			System.out.print("Irregular product line; ");
			return true;
		}
		
		// Check if there's any blank attribute.
		for (String s : splittedLine) {
			if (s.equals("")) {
				System.out.print("Illegal value; ");
				return true;
			}
		}
		
		// Check id
		if (splittedLine[1].matches(".*-.*") == false) {
			System.out.print("ID error; ");
			return true;
		}
		
		// Check integer values(price, current stock, safe level).
		String intRegex = "(0|[1-9][0-9]*)";
		if (splittedLine[2].matches(intRegex) == false ||
			splittedLine[3].matches(intRegex) == false ||
			splittedLine[4].matches(intRegex) == false) {
			System.out.print("Illegal value; ");
			return true;
		}
		
		return false;
	}
	
	private ProductRecord parseLine(String line) {
		
		// Split and trim the string.
		String[] splittedLine = splitAndTrim(line);
		
		String name = splittedLine[0];
		String id = splittedLine[1];
		int price = Integer.parseInt(splittedLine[2]);
		int currentStock = Integer.parseInt(splittedLine[3]);
		int minStock = Integer.parseInt(splittedLine[4]);
		
		// Parse memo if exist.
		String memo = null;
		if (splittedLine.length >= 6) memo = splittedLine[5];
		
		// Get category from product id.
		String category;
		int categoryInt = Integer.parseInt(id.split("-")[0]);
		switch (categoryInt) {
		case 1:
			category = "Food";
			break;
		case 2:
			category = "Office";
			break;
		case 3:
			category = "Misc.";
			break;
		case 4:
			category = "Health";
			break;
		case 5:
			category = "Clothing";
			break;
		default:
			category = "Etc";
		}
		
		return new ProductRecord(name, id, category, price, currentStock, minStock, memo);
	}
	
	private String[] splitAndTrim(String line) {
		String[] splittedLine = line.split(":");
		for (int i = 0; i < splittedLine.length; i++) {
			splittedLine[i] = splittedLine[i].trim();
		}
		return splittedLine;
	}
}
