package User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import CBR.Recommender;

/**
 * 
 * This class is executable and enables to retrieve cases for a query specified
 * through an input file which name is introduced in a GUI.
 *
 */
public class GUI3 {
	/**
	 * This is the executable main method.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static void main(String[] args) throws FileNotFoundException, IOException {

		JPanel Inframe, Outframe, Buttonframe, Labelframe, Fieldframe;
		JEditorPane Output, Info;
		JTextField Input, Outfile;
		JLabel Inputlabel, Outfilelabel;
		JButton SubmitQuery;
		JScrollPane Scroll;
		Recommender remy;
		int mainWidth = 600, mainHeight = 500;
		HashMap<String, String> entry = new HashMap<>();

		Font fontLabel = new Font("Dialog", Font.BOLD + Font.ITALIC, 20);
		Font fontInput = new Font("Dialog", Font.PLAIN, 20);
		Font fontTable = new Font("Times New Roman", Font.PLAIN, 22);

		JLabel subtitle1 = new JLabel("  Input variables");
		subtitle1.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 20));
		subtitle1.setForeground(Color.RED);

		Inputlabel = new JLabel("  Input file name :");
		Input = new JTextField(20);
		Inputlabel.setFont(fontLabel);
		Input.setFont(fontInput);

		Outfilelabel = new JLabel("  Output file name :");
		Outfile = new JTextField(20);
		Outfilelabel.setFont(fontLabel);
		Outfile.setFont(fontInput);

		Output = new JEditorPane("text/html", "<b>Welcome to a small CBR recommender demo</b>");
		Output.setEditable(false);

		Scroll = new JScrollPane(Output);
		Scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		Scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		Scroll.setBorder(
				BorderFactory.createTitledBorder(null, "User dialog:", 1, 0, new Font("Dialog", Font.BOLD, 16))); // White
																													// window
																													// title
		Scroll.getViewport().setPreferredSize(new Dimension(mainWidth - 50, mainHeight - 440)); // White window size
																								// (width x height)

		SubmitQuery = new JButton("SUBMIT QUERY"); // Button text
		SubmitQuery.setFont(fontLabel);
		SubmitQuery.setToolTipText("Press me to process the Query.");

		Inframe = new JPanel();
		Inframe.setLayout(new GridLayout(4, 2, 10, 0)); // Set Inframe grid (nb rows, nb columns, hgap, vgap)

		Inframe.add(subtitle1);
		Inframe.add(new JLabel());
		Inframe.add(Inputlabel);
		Inframe.add(Input);
		Inframe.add(Outfilelabel);
		Inframe.add(Outfile);

		Outframe = new JPanel();
		Outframe.setSize(new Dimension(300, 250));
		Outframe.add(Scroll);

		Output.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		Output.setFont(fontTable);

		JFrame Main = new JFrame("Predictive maintenance with CBR method - GUI 3"); // Window name

		Main.getContentPane().setLayout(new BorderLayout());
		Main.getContentPane().add(Inframe, BorderLayout.NORTH);
		Main.add(SubmitQuery, BorderLayout.SOUTH);
		Main.getContentPane().add(Outframe, BorderLayout.CENTER);

		remy = new Recommender();
		remy.loadengine();

		Output.setText("Write the input file name and the program will create an output file!!! \n"
				+ "The format file is \".csv\" ");
		Main.addWindowListener(new WindowAdapter() {
			/**
			 * 
			 */
			public void windowClosing(WindowEvent e) {

				System.exit(0);
			}
		});

		Input.setText("input_file");
		Outfile.setText("retrieval_results");
		/**
		 * 
		 */

		SubmitQuery.addActionListener(new ActionListener() {
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e) {

				String path = AppConfiguration.data_path;

				List<List<String>> records = new ArrayList<>();
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(path + Input.getText() + ".csv"));

					String line;
					while ((line = br.readLine()) != null) {
						String[] values = line.split(";");
						records.add(Arrays.asList(values));

					}

				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				int size = records.get(0).size();
				int query_number = records.size();
				System.out.println(query_number);
				for (int k = 1; k < query_number; k++) {

					for (int j = 0; j < size; j++) {
						entry.put(records.get(0).get(j), records.get(k).get(j));
					}
					System.out.println(entry);
					// Here the method Export from the class Recommender is used to save the results
					// of the retrieval into
					// a .csv file which name has been specified through the GUI. The retrieval
					// results are obtained with the method solveOuery
					// from the class Recommender. The current date is sent to the method as the
					// parameter year.
					remy.Export(remy.solveOuery(entry.get("Task"), // Task
							entry.get("Case study type"), // Case study type
							entry.get("Case study"), // Case study
							entry.get("Online/Offline"), // Online/Off-line
							entry.get("Input for the model"), // Input for the model
							entry.get("Input type"), entry.get("Number of cases to retrieve"), // Number of cases
							entry.get("Amalgamation function"), // Amalgamation function
							LocalDateTime.now().getYear(), // The current year is automatically provided
							entry.get("w1"), // Task weight
							entry.get("w2"), // Case study type weight
							entry.get("w3"), // Case study weight
							entry.get("w4"), // Online/Off-line weight
							entry.get("w5"), // Input for the model weight
							entry.get("w6") // Input type weight
					), entry.get("Number of cases to retrieve"), Outfile.getText(), k);

					System.out.println("Query number " + k + " solved!!!");
					Output.setText("Query number " + k + " Imported, Solved and Exported!!!");
				}
			}
		});

		Main.pack();
		Main.setSize(mainWidth, mainHeight); // Main window size (width, height)
		Main.setVisible(true);

	}

}
