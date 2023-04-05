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
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import CBR.Recommender;
import de.dfki.mycbr.core.similarity.AmalgamationFct;

/**
 * 
 * This class is executable and enables to retrieve cases for a query specified
 * through a GUI.
 *
 */
public class GUI2 {
	public JPanel Inframe, Outframe, Buttonframe, Labelframe, Fieldframe;
	public JEditorPane Output, Info;
	public JTextField InputTask, InputcaseStudyType, InputcaseStudy, InputonlineOffline, InputinputForTheModel,
			InputinputType, InputNumberofcases, InputAmalgam, TaskWeight, caseStudyTypeWeight, caseStudyWeight,
			onlineOfflineWeight, inputForTheModelWeight, inputTypeWeight;
	public JComboBox TaskList, caseStudyTypeList, onlineOfflineList, inputForTheModelList, AmalgamList;
	public JLabel InputlabelTask, InputlabelcaseStudyType, InputlabelcaseStudy, InputlabelonlineOffline,
			InputlabelinputForTheModel, InputlabelinputType, InputlabelNumberofcases, InputlabelAmalgam,
			OutputlabelAmalgam;
	public JButton SubmitQuery;
	public JScrollPane Scroll;
	public Recommender remy;
	public int mainWidth = 1170, mainHeight = 1000;
	public String[] TaskString = { "", "Feature extraction", "Fault detection", "Fault identification",
			"Health modelling", "Health assessment", "One step future state forecast",
			"Multiple steps future state forecast", "Remaining useful life estimation" },
			caseStudyTypeString = { "", "Rotary machines", "Reciprocating machines", "Electrical components",
					"Structures", "Energy cells and batteries", "Production lines", "Others" },
			onlineOfflineString = { "", "Online", "Off-line" }, inputForTheModelString = { "", "Signals",
					"Structured text-based", "Text-based maintenance/operation logs", "Time series" },
			AmalgamString = { "euclidean", "weighted sum" };

	/**
	 * 
	 */

	public GUI2() {
		Font fontLabel = new Font("Dialog", Font.BOLD + Font.ITALIC, 20);
		Font fontInput = new Font("Dialog", Font.PLAIN, 20);
		Font fontTable = new Font("Times New Roman", Font.PLAIN, 22);

		JLabel subtitle1 = new JLabel("  Input variables");
		subtitle1.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 20));
		subtitle1.setForeground(Color.RED);
		JLabel subtitle2 = new JLabel("  Additional inputs");
		subtitle2.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 20));
		subtitle2.setForeground(Color.RED);
		JLabel weightcolumn = new JLabel("  Variable weights");
		weightcolumn.setFont(new Font("Dialog", Font.BOLD + Font.ITALIC, 20));
		weightcolumn.setForeground(Color.RED);

		InputlabelTask = new JLabel("  Task :");
		InputTask = new JTextField(20);
		TaskList = new JComboBox(TaskString);
		InputlabelTask.setFont(fontLabel);
		TaskList.setFont(fontInput);

		InputlabelcaseStudyType = new JLabel("  Case study type :");
		InputcaseStudyType = new JTextField(20);
		caseStudyTypeList = new JComboBox(caseStudyTypeString);
		InputlabelcaseStudyType.setFont(fontLabel);
		caseStudyTypeList.setFont(fontInput);

		InputlabelcaseStudy = new JLabel("  Case study :");
		InputcaseStudy = new JTextField(20);
		InputlabelcaseStudy.setFont(fontLabel);
		InputcaseStudy.setFont(fontInput);

		InputlabelonlineOffline = new JLabel("  Online/Off-line :");
		InputonlineOffline = new JTextField(20);
		onlineOfflineList = new JComboBox(onlineOfflineString);
		InputlabelonlineOffline.setFont(fontLabel);
		onlineOfflineList.setFont(fontInput);

		InputlabelinputForTheModel = new JLabel("  Input for the model :");
		InputinputForTheModel = new JTextField(20);
		inputForTheModelList = new JComboBox(inputForTheModelString);
		InputlabelinputForTheModel.setFont(fontLabel);
		inputForTheModelList.setFont(fontInput);

		InputlabelinputType = new JLabel("  Input type :");
		InputinputType = new JTextField(20);
		InputlabelinputType.setFont(fontLabel);
		InputinputType.setFont(fontInput);

		InputlabelNumberofcases = new JLabel("  Number of cases to retrieve:");
		InputNumberofcases = new JTextField(20);
		InputlabelNumberofcases.setFont(fontLabel);
		InputNumberofcases.setFont(fontInput);

		InputlabelAmalgam = new JLabel("  Amalgamation function to use:");
		InputAmalgam = new JTextField(20);
		AmalgamList = new JComboBox(AmalgamString);
		InputlabelAmalgam.setFont(fontLabel);
		AmalgamList.setFont(fontInput);

		TaskWeight = new JTextField(5);
		caseStudyTypeWeight = new JTextField(5);
		caseStudyWeight = new JTextField(5);
		onlineOfflineWeight = new JTextField(5);
		inputForTheModelWeight = new JTextField(5);
		inputTypeWeight = new JTextField(5);
		TaskWeight.setFont(fontInput);
		caseStudyTypeWeight.setFont(fontInput);
		caseStudyWeight.setFont(fontInput);
		onlineOfflineWeight.setFont(fontInput);
		inputTypeWeight.setFont(fontInput);
		inputForTheModelWeight.setFont(fontInput);

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
		Inframe.setLayout(new GridLayout(0, 3, 10, 0)); // Set Inframe grid (nb rows, nb columns, hgap, vgap)

		/**
		 * */
		Inframe.add(subtitle1);
		Inframe.add(new JLabel());
		Inframe.add(weightcolumn);
		Inframe.add(InputlabelTask);
		Inframe.add(TaskList);
		Inframe.add(TaskWeight);
		Inframe.add(InputlabelcaseStudyType);
		Inframe.add(caseStudyTypeList);
		Inframe.add(caseStudyTypeWeight);
		Inframe.add(InputlabelcaseStudy);
		Inframe.add(InputcaseStudy);
		Inframe.add(caseStudyWeight);
		Inframe.add(InputlabelinputType);
		Inframe.add(InputinputType);
		Inframe.add(inputTypeWeight);
		Inframe.add(InputlabelonlineOffline);
		Inframe.add(onlineOfflineList);
		Inframe.add(onlineOfflineWeight);
		Inframe.add(InputlabelinputForTheModel);
		Inframe.add(inputForTheModelList);
		Inframe.add(inputForTheModelWeight);
		Inframe.add(subtitle2);
		Inframe.add(new JLabel());
		Inframe.add(new JLabel());
		Inframe.add(InputlabelNumberofcases);
		Inframe.add(InputNumberofcases);
		Inframe.add(new JLabel());
		Inframe.add(InputlabelAmalgam);
		Inframe.add(AmalgamList);
		Inframe.add(new JLabel());

		Outframe = new JPanel();
		Outframe.setSize(new Dimension(300, 250));
		Outframe.add(Scroll);

		Output.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		Output.setFont(fontTable);

		JFrame Main = new JFrame("Predictive maintenance with CBR method - GUI 2"); // Window name

		Main.getContentPane().setLayout(new BorderLayout());
		Main.getContentPane().add(Inframe, BorderLayout.NORTH);
		Main.add(SubmitQuery, BorderLayout.SOUTH);
		Main.getContentPane().add(Outframe, BorderLayout.CENTER);
		// Creating the Recommender class object that will perform the retrieval
		// operations.
		remy = new Recommender();
		remy.loadengine();
		Output.setText(remy.displayAmalgamationFunctions()); // Display beginning text

		InputTask.setText("");
		TaskList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputTask.setText(((JComboBox) e.getSource()).getSelectedItem().toString());
			}
		});
		InputcaseStudyType.setText("");
		caseStudyTypeList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputcaseStudyType.setText(((JComboBox) e.getSource()).getSelectedItem().toString());
			}
		});
		InputcaseStudy.setText("");

		InputonlineOffline.setText("");
		onlineOfflineList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputonlineOffline.setText(((JComboBox) e.getSource()).getSelectedItem().toString());
			}
		});
		InputinputForTheModel.setText("");
		inputForTheModelList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputinputForTheModel.setText(((JComboBox) e.getSource()).getSelectedItem().toString());
			}
		});
		InputNumberofcases.setText("");

		InputAmalgam.setText("euclidean");
		AmalgamList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InputAmalgam.setText(((JComboBox) e.getSource()).getSelectedItem().toString());
			}
		});

		TaskWeight.setText("1.0");
		caseStudyTypeWeight.setText("1.0");
		caseStudyWeight.setText("1.0");
		onlineOfflineWeight.setText("1.0");
		inputForTheModelWeight.setText("1.0");
		inputTypeWeight.setText("1.0");

		Main.addWindowListener(new WindowAdapter() {

			/**
			 * 
			 */

			public void windowClosing(WindowEvent e) {

				System.exit(0);
			}
		});

		/**
		 * Initiates the query through the GUI.
		 */

		SubmitQuery.addActionListener(new ActionListener() {

			/**
			 * Sends the query parameters to the solveOuery method in the class Recommender
			 * to perform the retrieval. After obtaining the textual description of the
			 * cases of interest with the variable recommendation, its is displayed on the
			 * GUI using the method print_results of the class Recommender. The current date
			 * is sent as the parameter year.
			 */
			public void actionPerformed(ActionEvent e) {

				CheckforAmalgamSelection();
				String recomendation = remy.print_results(remy.solveOuery(InputTask.getText(),
						InputcaseStudyType.getText(), InputcaseStudy.getText(), InputonlineOffline.getText(),
						InputinputForTheModel.getText(), InputinputType.getText(), InputNumberofcases.getText(),
						InputAmalgam.getText(), LocalDateTime.now().getYear(), // The current year is automatically
																				// provided
						TaskWeight.getText(), caseStudyTypeWeight.getText(), caseStudyWeight.getText(),
						onlineOfflineWeight.getText(), inputForTheModelWeight.getText(), inputTypeWeight.getText()),
						InputNumberofcases.getText());
				Output.setText(recomendation);
			}
		});

		Main.pack();
		Main.setSize(mainWidth, mainHeight); // Main window size (width, height)
		Main.setVisible(true);
	}

	/**
	 * 
	 */
	public void CheckforAmalgamSelection() {

		List<AmalgamationFct> liste = remy.myConcept.getAvailableAmalgamFcts();

		for (int i = 0; i < liste.size(); i++) {
			if ((liste.get(i).getName()).equals(InputAmalgam.getText())) {

				remy.myConcept.setActiveAmalgamFct(liste.get(i));
			}
		}
	}

	/**
	 * This is the main method to be executed.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		GUI2 mygui = new GUI2();
	}

}
