import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RecordView extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JTextField nameField = new JTextField();
	private JTextField idField = new JTextField();
	private JTextField categoryField = new JTextField();
	private JTextField priceField = new JTextField();
	private JTextField stockField = new JTextField();
	private JTextField safeStockField = new JTextField();
	private JTextField memoField = new JTextField();
	
	private JButton doneButton = new JButton("Done");
	
	private ListView delegate;
	private ProductList list;
	private int recordIndex = -1;
	
	RecordView() {
		// Setup record panel.
		JPanel recordPanel = new JPanel();
		recordPanel.setLayout(new GridLayout(2, 7));
		recordPanel.add(new JLabel("제품명"));
		recordPanel.add(new JLabel("제품ID"));
		recordPanel.add(new JLabel("카테고리"));
		recordPanel.add(new JLabel("가격"));
		recordPanel.add(new JLabel("재고수"));
		recordPanel.add(new JLabel("최소재고량"));
		recordPanel.add(new JLabel("기타 메모"));
		recordPanel.add(nameField);
		recordPanel.add(idField);
		recordPanel.add(categoryField);
		recordPanel.add(priceField);
		recordPanel.add(stockField);
		recordPanel.add(safeStockField);
		recordPanel.add(memoField);
		
		doneButton.addActionListener(this);
		
		this.setLayout(new BorderLayout());
		this.add(recordPanel, BorderLayout.CENTER);
		this.add(doneButton, BorderLayout.EAST);
		this.pack();
		this.setVisible(true);
		
	}
	
	RecordView(ProductList list, ListView delegate) {
		this();
		this.list = list;
		this.delegate = delegate;
	}
	
	RecordView(ProductList list, int recordIndex, ListView delegate) {
		this(list, delegate);
		this.recordIndex = recordIndex;
		
		Object[] recordData = list.getRecords()[recordIndex].getArray();
		nameField.setText((String)recordData[0]);
		idField.setText((String)recordData[1]);
		categoryField.setText((String)recordData[2]);
		priceField.setText((String)((Integer)recordData[3]).toString());
		stockField.setText((String)((Integer)recordData[4]).toString());
		safeStockField.setText((String)((Integer)recordData[5]).toString());
		memoField.setText((String)recordData[6]);
	}
	
	private boolean isIllegal() {
		
		// Check if there's any blank attribute.
		if (nameField.getText() == "" || idField.getText() == "" ||
			categoryField.getText() == "" || priceField.getText() == "" ||
			stockField.getText() == "" || safeStockField.getText() == "") {
			System.out.println("Empty entry exist(s).");
			return true;
		}
		
		// Check id
		if (idField.getText().matches(".*-.*") == false) {
			System.out.print("ID error; ");
			return true;
		}
		
		// Check integer values(price, current stock, safe level).
		String intRegex = "(0|[1-9][0-9]*)";
		if (priceField.getText().matches(intRegex) == false ||
			stockField.getText().matches(intRegex) == false ||
			safeStockField.getText().matches(intRegex) == false) {
			System.out.print("Illegal value.");
			return true;
		}
		
		return false;
	}
	
	public void actionPerformed(ActionEvent e) {
		System.out.println("Done button pressed.");
		
		if (isIllegal()) {
			JOptionPane.showMessageDialog(null, "Illegal input.");
			return;
		}
		
		if (recordIndex == -1) {
			// Add
			try {
				list.addRecord(new ProductRecord(
					nameField.getText(), idField.getText(), categoryField.getText(),
					Integer.parseInt(priceField.getText()),
					Integer.parseInt(stockField.getText()),
					Integer.parseInt(safeStockField.getText()),
					memoField.getText()));
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
				return;
			}
		}
		else {
			// Modify
			try {
				list.modifyRecord(recordIndex, new ProductRecord(
						nameField.getText(), idField.getText(), categoryField.getText(),
						Integer.parseInt(priceField.getText()),
						Integer.parseInt(stockField.getText()),
						Integer.parseInt(safeStockField.getText()),
						memoField.getText()));
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "An error occured while saving.");
				return;
			}
		}
		
		if (delegate != null) delegate.updateTable();
		dispose();
	}
}
