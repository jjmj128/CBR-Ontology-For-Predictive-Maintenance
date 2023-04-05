package CBR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import OntologyTools.LevenshteinDistanceDP;
import User.AppConfiguration;
import User.GeneralMethods;
import de.dfki.mycbr.core.DefaultCaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.model.StringDesc;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.retrieval.Retrieval;
import de.dfki.mycbr.core.retrieval.Retrieval.RetrievalMethod;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.core.similarity.SymbolFct;
import de.dfki.mycbr.core.similarity.config.AmalgamationConfig;
import de.dfki.mycbr.util.Pair;

/**
 * This class will be instanced by the executable GUIs to to receive the
 * parameters of the query (GUI2) or the input file (GUI3) which contains them.
 * The class will use the CBREngine class to process the query after setting the
 * similarity functions through the myCBR methods. It produces the output of
 * results through the screen of the GUI (GUI2) or the results file (GUI3)
 * 
 *
 */
public class Recommender {
	public CBREngine engine;
	public Project rec;
	public DefaultCaseBase cb;
	public Concept myConcept;
	private static DecimalFormat df = new DecimalFormat("0.000");

	/**
	 * Method to check if a given string is an integer.
	 * 
	 * @param strNum
	 * @return boolean
	 */
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {

		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * This method check if a string exist in a list taken into account possible
	 * misspelling
	 * 
	 * @param list of elements where a String similar to the one specified could
	 *             appear.
	 * @param s    is the target String
	 * @return true if a similar String has been found or false if not.
	 */
	public static boolean contains_similar(ArrayList<String> list, String s) {
		boolean contains = false;
		for (String elem : list) {
			if (LevenshteinDistanceDP.compute_Levenshtein_distanceDP(s, elem) < 4) {
				// If the Levenshtein distance, calculated with the appropriate method of the
				// class LevenshteinDistanceDP,
				// is less than a certain value, the similarity is enough to be considered the
				// string element as identical.
				contains = true;

			} else {

			}
		}

		return contains;
	}

	/**
	 * Instances the CBREngine and uses its method to create a myCBR project with
	 * the corresponding case base loaded
	 */
	public void loadengine() {

		engine = new CBREngine();

		rec = engine.createProjectFromPRJ();// Using this method, the project is loaded from the project file without
											// modifications.

		cb = (DefaultCaseBase) rec.getCaseBases().get(AppConfiguration.casebase);
		myConcept = rec.getConceptByID(AppConfiguration.conceptName);
	}

	/**
	 * In this method the similarity functions that depend on the specific query are
	 * defined together with the global similarity value of each case (amalgama)
	 * according to the given query. It is initialized with all the field values of
	 * the query case, so the method is run once at each query.
	 * 
	 * @param task
	 * @param caseStudyType
	 * @param caseStudy
	 * @param onlineOffline
	 * @param inputForTheModel
	 * @param inputType
	 * @param numberofcasesText
	 * @param amalga
	 * @param year
	 * @param taskWeightT
	 * @param caseStudyTypeWeightT
	 * @param caseStudyWeightT
	 * @param onlineOfflineWeightT
	 * @param inputForTheModelWeightT
	 * @param inputTypeWeightT
	 * @return List with pairs corresponding to the cases instances that best match
	 *         the query and their global similarity value
	 */
	public List<Pair<Instance, Similarity>> solveOuery(String task, String caseStudyType, String caseStudy,
			String onlineOffline, String inputForTheModel, String inputType, String numberofcasesText, String amalga,
			int year, String taskWeightT, String caseStudyTypeWeightT, String caseStudyWeightT,
			String onlineOfflineWeightT, String inputForTheModelWeightT, String inputTypeWeightT) {

		Retrieval ret = new Retrieval(myConcept, cb);
		ret.setRetrievalMethod(RetrievalMethod.RETRIEVE_SORTED);

		Instance query = ret.getQueryInstance();

		// Loading data form case base to implement similarity functions

		// Insert values into the query: Symbolic Description

		SymbolDesc taskDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Task");
		SymbolDesc caseStudyTypeDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Case study type");
		StringDesc caseStudyDesc = (StringDesc) myConcept.getAllAttributeDescs().get("Case study");
		SymbolDesc onlineOfflineDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Online/Off-line");
		SymbolDesc inputForTheModelDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Input for the model");
		SymbolDesc inputTypeDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Input type");
		IntegerDesc yearDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("Publication Year");

		/******************************************
		 * Add table similarity function - Input type
		 * 
		 * The similarity values for this field depend on the query, which must be
		 * compared to all the symbolic values existing in the case base individually.
		 ******************************************/

		// This list will save as Strings all the symbolic values existing for the cases
		// in the .prj file.
		List<String> inputType_data = new ArrayList<>();

		Iterator<Instance> iterator = myConcept.getAllInstances().iterator();// Gets iterator for the collection of
		// all the concept instances (cases) existing in the case base of the .prj file.
		if (myConcept.getAllInstances() != null) {
			while (iterator.hasNext()) {
				inputType_data.add(iterator.next().getAttForDesc(inputTypeDesc).getValueAsString());
				// For each of the cases, the value of the Input Type field is obtained in
				// String.

			}
		}

		// The similarity function of type symbol for the field Input Type is created.
		// The query is added to the list of allowed symbols of the attribute Input
		// type.
		inputTypeDesc.addSymbol(inputType);
		SymbolFct inputTypeFct = inputTypeDesc.addSymbolFct("inputTypeFct", true);

		ArrayList<String> CaseVariables1 = new ArrayList<String>();
		ArrayList<String> CaseVariables2 = new ArrayList<String>();

		for (int i = 1; i < inputType_data.size(); i++) {
			// For each of the cases, the String value of the Input Type variables is
			// compared to the one in the query.
			// The values of this field for the query and the cases are in origin a String
			// containing variables separated
			// by ', '. So, the method Recommender.separate is used to transform them into a
			// list.
			CaseVariables1 = GeneralMethods.separate(inputType);

			CaseVariables2 = GeneralMethods.separate(inputType_data.get(i).toString());
			// The similarity value is obtained using the method Recommender.SimilarityValue
			// above in this class.
			System.out.println(CaseVariables1);
			System.out.println(CaseVariables2);

			double similarity_value = GeneralMethods.SimilarityValue(CaseVariables1, CaseVariables2);
			System.out.println(inputType.replace("_", " "));
			System.out.println(inputType_data.get(i).toString().replace("_", " "));
			System.out.println(similarity_value);
			// Setting similarity in the .prj file of myCBR
			inputTypeFct.setSimilarity(inputType.replace("_", " "), inputType_data.get(i).toString().replace("_", " "),
					similarity_value);

		}

		/******************************************
		 * Amalgamation function
		 ******************************************/

		float taskWeight, caseStudyTypeWeight, caseStudyWeight, onlineOfflineWeight, inputForTheModelWeight,
				inputTypeWeight;
		taskWeight = taskWeightT.equals("") ? 0 : Float.valueOf(taskWeightT);
		caseStudyTypeWeight = caseStudyTypeWeightT.equals("") ? 0 : Float.valueOf(caseStudyTypeWeightT);
		caseStudyWeight = caseStudyWeightT.equals("") ? 0 : Float.valueOf(caseStudyWeightT);
		onlineOfflineWeight = onlineOfflineWeightT.equals("") ? 0 : Float.valueOf(onlineOfflineWeightT);
		inputForTheModelWeight = inputForTheModelWeightT.equals("") ? 0 : Float.valueOf(inputForTheModelWeightT);
		inputTypeWeight = inputTypeWeightT.equals("") ? 0 : Float.valueOf(inputTypeWeightT);
		String[] amalgavec = new String[] { "euclidean", "weighted sum" };
		// Selection of method for the amalgamation function.

		for (int i = 0; i < amalgavec.length; i++) {
			String amalgafct = amalgavec[i];
			if (amalgafct.equals("euclidean")) {
				myConcept.addAmalgamationFct(AmalgamationConfig.EUCLIDEAN, amalgafct, false);
			}
			if (amalgafct.equals("weighted sum")) {
				myConcept.addAmalgamationFct(AmalgamationConfig.WEIGHTED_SUM, amalgafct, false);
			}

			// Adding the similarity function of each field to the global amalgamation model

			myConcept.getFct(amalgafct).setActiveFct(taskDesc, taskDesc.getFct("taskFct"));
			myConcept.getFct(amalgafct).setActiveFct(caseStudyTypeDesc, caseStudyTypeDesc.getFct("caseStudyTypeFct"));
			myConcept.getFct(amalgafct).setActiveFct(caseStudyDesc, caseStudyDesc.getFct("caseStudyFct"));
			myConcept.getFct(amalgafct).setActiveFct(onlineOfflineDesc, onlineOfflineDesc.getFct("onlineOfflineFct"));
			myConcept.getFct(amalgafct).setActiveFct(inputForTheModelDesc,
					inputForTheModelDesc.getFct("inputFortheModelFct"));
			myConcept.getFct(amalgafct).setActiveFct(inputTypeDesc, inputTypeDesc.getFct("inputTypeFct"));
			myConcept.getFct(amalgafct).setActiveFct(yearDesc, yearDesc.getFct("yearFct"));

			myConcept.getFct(amalgafct).setWeight("Task", taskWeight);
			myConcept.getFct(amalgafct).setWeight("Case study type", caseStudyTypeWeight);
			myConcept.getFct(amalgafct).setWeight("Case study", caseStudyWeight);
			myConcept.getFct(amalgafct).setWeight("Online/Off-line", onlineOfflineWeight);
			myConcept.getFct(amalgafct).setWeight("Input for the model", inputForTheModelWeight);
			myConcept.getFct(amalgafct).setWeight("Input type", inputTypeWeight);
			myConcept.getFct(amalgafct).setWeight("Publication Year", 1.0);

			// Fields without any of the correct names or not containing information are not
			// considered (weight=0).

			for (AttributeDesc att : myConcept.getAllAttributeDescs().values()) {

				if (!(att.getName().equals("Task")) && !(att.getName().equals("Case study type"))
						&& !(att.getName().equals("Case study")) && !(att.getName().equals("Online/Off-line"))
						&& !(att.getName().equals("Input for the model")) && !(att.getName().equals("Input type"))
						&& !(att.getName().equals("Publication Year"))) {
					myConcept.getFct(amalgafct).setWeight(att.getName(), 0.0);

				}

				if ((att.getName().equals("Task") && task.equals(""))
						|| (att.getName().equals("Case study type") && caseStudyType.equals(""))
						|| (att.getName().equals("Case study") && caseStudy.equals(""))
						|| (att.getName().equals("Online/Off-line") && onlineOffline.equals(""))
						|| (att.getName().equals("Input for the model") && inputForTheModel.equals(""))
						|| (att.getName().equals("Input type") && inputType.equals(""))) {
					myConcept.getFct(amalgafct).setWeight(att.getName(), 0.0);

				}

			}
		}

		// Adding fields content to the query and getting results.
		myConcept.setActiveAmalgamFct(myConcept.getFct(amalga));

		/**********************
		 * Save project file
		 * 
		 * So as the modifications in the similarity functions are stored in the .prj
		 * file.
		 ***************************************/

		myConcept.getProject().save();

		/////////////////

		query.addAttribute(taskDesc, taskDesc.getAttribute(task));
		query.addAttribute(caseStudyTypeDesc, caseStudyTypeDesc.getAttribute(caseStudyType));
		try {
			query.addAttribute(caseStudyDesc, caseStudyDesc.getAttribute(caseStudy));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		query.addAttribute(onlineOfflineDesc, onlineOfflineDesc.getAttribute(onlineOffline));

		query.addAttribute(inputForTheModelDesc, inputForTheModelDesc.getAttribute(inputForTheModel));

		query.addAttribute(inputTypeDesc, inputTypeDesc.getAttribute(inputType));

		try {
			query.addAttribute(yearDesc, yearDesc.getAttribute(year));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ret.start();

		List<Pair<Instance, Similarity>> result = ret.getResult();

		// Removing the Input type query form the list of allowed symbols of the
		// attribute
		inputTypeDesc.removeSymbol(inputType);
		myConcept.getProject().save();

		return result;

	}

	/******************************************
	 * Print results
	 ******************************************/

	/**
	 * 
	 * This method is used by GUI2 after having perform the query with solveOuery
	 * method above.
	 * 
	 * @param result            of pairs of case instance and similarity value with
	 *                          the query.
	 * @param numberofcasesText to specify how many cases are being retrieved.
	 * @return answer to be displayed on the GUI 2 showing the labels and
	 *         description of the cases retrieved.
	 */
	public String print_results(List<Pair<Instance, Similarity>> result, String numberofcasesText) {
		int numberofcases;
		String answer = "";
		numberofcases = (numberofcasesText.equals("")) ? cb.getCases().size() : Integer.valueOf(numberofcasesText);

		if (result.size() > 0) {
			// get the best case name
			String casename = result.get(0).getFirst().getName();
			String casename1 = casename.substring(0, 4);
			String casename2 = Integer.toString(1 + Integer.valueOf(casename.substring(4)));
			// get the similarity value
			Double sim = result.get(0).getSecond().getValue();
			df.format(sim);
			if (sim > 0) {
				answer = "I found " + casename1 + casename2 + " with a similarity of "
						+ String.format(Locale.ROOT, "%.3f", sim) + " as the best match.";
				answer = answer + " The " + numberofcases
						+ " best cases shown in a table: <br /> <br /> <table border=\"1\">";
				ArrayList<Hashtable<String, String>> liste = new ArrayList<Hashtable<String, String>>();
				// if more case results are requested than we have in our case base at all:
				if (numberofcases >= cb.getCases().size()) {
					numberofcases = cb.getCases().size();
				}

				String[] head = { "Reference", "Sim", "Task", "Case study type", "Case study", "Online/Off-line",
						"Input for the model", "Model Approach", "Models", "Input type", "Number of input variables",
						"Performance indicator", "Performance", "Complementary notes", "Publication Year",
						"Publication identifier" };

				// Table title
				answer = answer + "<tr><td> <b>Case</b> </td><td> <b>Description</b> </td></tr>";
				for (int i = 0; i < numberofcases; i++) {
					liste.add(getAttributes(result.get(i), rec.getConceptByID(AppConfiguration.conceptName)));

					String part1 = result.get(i).getFirst().getName().substring(0, 4);
					String part2 = result.get(i).getFirst().getName().substring(4);
					part2 = Integer.toString(Integer.valueOf(part2) + 1);

					answer = answer + "<tr><td>" + "<b>" + part1 + part2 + "</b>" + "<br /> Sim = "
							+ String.format(Locale.ROOT, "%.3f", result.get(i).getSecond().getValue()) + "</td><td>";
					answer = answer + "<b>Reference, Similarity and Input variables</b> <br />";
					for (int j = 0; j < head.length; j++) {
						if (head[j].equals("Model Approach")) {
							answer = answer + "<b>Recommendation</b><br />";
						}
						answer = answer + head[j] + ": " + liste.get(i).get(head[j]).toString() + "<br />";
					}
					answer = answer + "</td></tr>";
				}

			} else {
				answer = "<br /> <table border=\"1\"> <tr><td> ERROR : query not found. "
						+ "Please choose at least one Input Variable. </td></tr> </table>";
			}

			answer = answer + "</table>";
		} else {
			System.out.println("Retrieval Result is empty");
		}

		return answer;

	}

	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////

	/**
	 * This method is used by GUI3 after having perform the query with solveOuery
	 * method.
	 * 
	 * @param result            of pairs of case instance and similarity value with
	 *                          the query.
	 * @param numberofcasesText to specify how many cases are being retrieved.
	 * @param outputname        to set the path of the results file
	 * @param iteration         meaning the row number of the input file being
	 *                          processed at the moment (each row is one query).
	 */
	public void Export(List<Pair<Instance, Similarity>> result, String numberofcasesText, String outputname,
			int iteration) {

		int numberofcases;
		numberofcases = (numberofcasesText.equals("")) ? cb.getCases().size() : Integer.valueOf(numberofcasesText);

		if (result.size() > 0) {

			// get the similarity value
			Double sim = result.get(0).getSecond().getValue();
			df.format(sim);
			if (sim > 0) {
				ArrayList<Hashtable<String, String>> liste = new ArrayList<Hashtable<String, String>>();
				// if more case results are requested than we have in our case base at all:
				if (numberofcases >= cb.getCases().size()) {
					numberofcases = cb.getCases().size();
				}
				try {
					PrintWriter pw = new PrintWriter(
							new File(AppConfiguration.data_path + outputname + iteration + ".csv"));

					StringBuilder sb = new StringBuilder();

					String[] head = { "Reference", "Sim", "Task", "Case study type", "Case study", "Online/Off-line",
							"Input for the model", "Model Approach", "Models", "Input type",
							"Number of input variables", "Performance indicator", "Performance", "Complementary notes",
							"Publication identifier" };

					for (int i = 0; i < head.length; i++) {
						if (i == head.length - 1) {
							sb.append(head[i]);
							sb.append("\r\n");
						} else {
							sb.append(head[i]);
							sb.append(";");
						}
					}

					for (int i = 0; i < numberofcases; i++) {
						liste.add(getAttributes(result.get(i), rec.getConceptByID(AppConfiguration.conceptName)));

						for (int j = 0; j < head.length; j++) {
							String replace = liste.get(i).get(head[j]).toString().replace(";", ",");
							sb.append(replace);
							if (j == head.length - 1) {
								sb.append("\r\n");
							} else {
								sb.append(";");
							}
						}
					}

					pw.write(sb.toString());
					pw.close();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.println("Error: choose input value.");
			}
		} else {
			System.out.println("Retrieval Result is empty");
		}

	}
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////

	/**
	 * This method delivers a Hashtable which contains the Attributes names
	 * (Attributes of the case) combined with their respective values.
	 * 
	 * @author weber,koehler,namuth
	 * @param r       = An Instance.
	 * @param concept = A Concept
	 * @return List = List containing the Attributes of a case with their values.
	 */
	public static Hashtable<String, String> getAttributes(Pair<Instance, Similarity> r, Concept concept) {

		Hashtable<String, String> table = new Hashtable<String, String>();
		ArrayList<String> cats = getCategories(r);
		// Add the similarity of the case
		// table.put("Sim", String.valueOf(r.getSecond().getValue()));
		table.put("Sim", String.valueOf(df.format(r.getSecond().getValue())));
		for (String cat : cats) {
			// Add the Attribute name and its value into the Hashtable
			table.put(cat, r.getFirst().getAttForDesc(concept.getAllAttributeDescs().get(cat)).getValueAsString());
		}
		return table;
	}

	/**
	 * This Method generates an ArrayList, which contains all Categories of aa
	 * Concept.
	 * 
	 * @author weber,koehler,namuth
	 * @param r = An Instance.
	 * @return List = List containing the Attributes names.
	 */
	public static ArrayList<String> getCategories(Pair<Instance, Similarity> r) {

		ArrayList<String> cats = new ArrayList<String>();

		// Read all Attributes of a Concept
		Set<AttributeDesc> catlist = r.getFirst().getAttributes().keySet();

		for (AttributeDesc cat : catlist) {
			if (cat != null) {
				// Add the String literals for each Attribute into the ArrayList
				cats.add(cat.getName());
			}
		}
		return cats;
	}

	/**
	 * To display the initial text on the GUI2.
	 * 
	 * @return String listoffunctions
	 */

	public String displayAmalgamationFunctions() {

		String listoffunctions = "Welcome to the myCBR Graphical User Interface ! <br /><br />";
		listoffunctions = listoffunctions + "* Input Variables : variables used in the query "
				+ "to retrieve and calculate similarites. <br />";
		listoffunctions = listoffunctions
				+ "- Task, Publication year, Case Study Type, Online/Off-line, Input for the model, Input type : "
				+ "Drop down list <br />";
		listoffunctions = listoffunctions + "- Case Study : Free text <br /><br />";

		listoffunctions = listoffunctions + "* Additional inputs : inputs to complement the retrieval method. <br />";
		listoffunctions = listoffunctions + "- Number of cases to retrieve : Integer number. <br />";
		listoffunctions = listoffunctions + "- Amalgamation function to use : Drop down list <br />";
		AmalgamationFct current = myConcept.getActiveAmalgamFct();
		System.out.println("Amalgamation Function is used = " + current.getName());

		return listoffunctions;
	}
}
