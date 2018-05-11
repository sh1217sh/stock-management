import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

public class ListView extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private ProductList productList;
	private DefaultTableModel model;
	private JTable table;
	
	private JButton addButton = new JButton("Add");
	private JButton deleteButton = new JButton("Delete");
	private JButton modifyButton = new JButton("Modify");
	private JButton saveButton = new JButton("Save File");
	
	ListView() {
		
		// Setup the table.
		String[] columnNames = {"제품명", "제품ID", "카테고리", "가격", "재고수", "최소재고량", "기타메모"};
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				// Always return false to prevent editing.
				return false;
			}
		};
		table.getTableHeader().setReorderingAllowed(false); // Prevent column reordering.
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Setup button panel.
		JPanel buttonPanel = new JPanel();
		addButton.addActionListener(this);
		deleteButton.addActionListener(this);
		modifyButton.addActionListener(this);
		saveButton.addActionListener(this);
		buttonPanel.setLayout(new GridLayout(4, 1));
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(saveButton);
		
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.EAST);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	ListView(ProductList productList) {
		this();
		this.productList = productList;
		
		// Read data and update table.
		for (ProductRecord record: productList.getRecords()) {
			if (record == null) break;
			model.addRow(record.getArray());
		}
		model.fireTableDataChanged();
	}
	
	void updateTable() {
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		for (ProductRecord record: productList.getRecords()) {
			if (record == null) break;
			model.addRow(record.getArray());
		}
		model.fireTableDataChanged();
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addButton) {
			System.out.println("Add button pressed.");
			new RecordView(productList, this);
		}
		else if (e.getSource() == deleteButton) {
			System.out.println("Delete button pressed.");
			
			int row = table.getSelectedRow();
			
			if (row == -1) {
				// If no row selected.
				JOptionPane.showMessageDialog(null, "Please select a row.");
				return;
			}
			
			try {
				productList.deleteRecord(row);
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
				return;
			}
			updateTable();
		}
		else if (e.getSource() == modifyButton) {
			System.out.println("Modify button pressed.");
			
			int row = table.getSelectedRow();
			
			if (row == -1) {
				// If no row selected.
				JOptionPane.showMessageDialog(null, "Please select a row.");
				return;
			}
			
			new RecordView(productList, row, this);
		}
		else if (e.getSource() == saveButton) {
			System.out.println("Save button pressed.");
			
			try {
				productList.saveToFile();
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
				return;
			}
			
			updateTable();
		}
	}
}
