package CBR;

import java.util.Collection;
import java.util.Iterator;

import User.AppConfiguration;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.casebase.Instance;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.io.CSVImporter;

/**
 * 
 * 
 *
 */
public class CBREngine {

	/***************************************************
	 * 
	 * Block 1: Define variables name from myCBR Project
	 * 
	 ***************************************************/
	// The static attributes are obtained from the AppConfiguration class. Use that
	// class if any modification is needed.
	// The path below must be changed before running the code to match the local
	// folder where the case base and the results files will be stored
	private static String data_path = AppConfiguration.data_path;
	private static String projectName = AppConfiguration.projectName;
	private static String conceptName = AppConfiguration.conceptName;
	private static String csv = AppConfiguration.csv; // Name of the file containing the case base
	private static String columnseparator = AppConfiguration.columnseparator;
	private static String multiplevalueseparator = AppConfiguration.multiplevalueseparator;
	private static String casebase = AppConfiguration.casebase;

	// Getter for the Project meta data
	/**
	 * 
	 * @return casebase
	 */
	public static String getCaseBase() {
		return casebase;
	}

	/**
	 * 
	 * @param casebase
	 */
	public static void setCasebase(String casebase) {
		CBREngine.casebase = casebase;
	}

	/**
	 * 
	 * @return data_path
	 */
	public static String getPath() {
		return data_path;
	}

	/**
	 * 
	 * @return projectName
	 */
	public static String getProjectName() {
		return projectName;
	}

	/**
	 * 
	 * @param projectName
	 */
	public static void setProjectName(String projectName) {
		CBREngine.projectName = projectName;
	}

	/**
	 * 
	 * @return conceptName
	 */
	public static String getConceptName() {
		return conceptName;
	}

	/**
	 * 
	 * @param conceptName
	 */
	public static void setConceptName(String conceptName) {
		CBREngine.conceptName = conceptName;
	}

	/**
	 * 
	 * @return csv
	 */
	public static String getCsv() {
		return csv;
	}

	/**
	 * 
	 * @param csv
	 */
	public static void setCsv(String csv) {
		CBREngine.csv = csv;
	}

	/**
	 * This methods creates a myCBR project and loads the project from a .prj file.
	 * The project file is not modified, so the queries are performed on the
	 * case-base that is already loaded in such file.
	 */
	public Project createProjectFromPRJ() {

		System.out.println("Trying to load prj file with : " + data_path + " " + projectName + " " + conceptName + " ");
		Project project = null;
		try {
			project = new Project(data_path + projectName);

			while (project.isImporting()) {
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");
		} catch (Exception ex) {
			System.out.println("Error loading the project");
		}
		return project;
	}

	/**
	 * This method creates a myCBR project and loads the cases in this project. All
	 * cases instances in the project file are deleted and the case data-base
	 * contained in the .csv is loaded. This method must be used when it is desired
	 * to perform myCBR queries on a new data-base. myCBR gets all the cases
	 * information directly from the project file and not from the .csv.
	 * Recommendation: if it is wanted to visualize the myCBR model and the case
	 * base, the porject file can be openned using the myCBR Workbench application.
	 * The specification of the project's location and according file names has to
	 * be done at the beginning of this class.
	 * 
	 * @return Project instance containing model, sims and cases (if available)
	 */
	public Project createCBRProject() {

		Project project = null;
		try {
			project = new Project(data_path + projectName);
			while (project.isImporting()) {
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");
			Concept concept = project.getConceptByID(conceptName);

			Collection<Instance> list_of_cases = concept.getAllInstances();// Gets all instances of the desired concept
			Iterator<Instance> iterator = list_of_cases.iterator();// Creates iterator of the instances Collection
			if (list_of_cases != null) {
				while (iterator.hasNext()) {
					concept.removeInstance(iterator.next().getName());
					// If the list is not empty, all the instances are removed, so as it ensures
					// that none of the old concept
					// instances remains in the new data base.

				}
			}

			project.deleteCaseBase(casebase);// The old case base is deleted.
			project.createDefaultCB(casebase);// New case base is created.
			CSVImporter csvImporter = new CSVImporter(data_path + csv, concept);// Importer form .csv to .prj
			csvImporter.setCaseBase(project.getCB(casebase));// Specifies the case base where the data in the .csv must
																// be
			// stored within the .prj file.
			csvImporter.setSeparator(columnseparator);
			csvImporter.setSeparatorMultiple(multiplevalueseparator);
			csvImporter.readData();
			csvImporter.checkData();
			csvImporter.addMissingValues();
			csvImporter.addMissingDescriptions();
			csvImporter.doImport();
			while (csvImporter.isImporting()) {
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
		project.save();// Saves the project in the .prj after all the modifications concerning the new
						// data base
		return project;
	}

	/**
	 * This methods creates an EMPTY myCBR project. The specification of the
	 * project's location and according file names has to be done at the beginning
	 * of this class.
	 * 
	 * @return Project instance containing model, sims and cases (if available)
	 */
	public Project createemptyCBRProject() {

		Project project = null;
		try {
			project = new Project(data_path + projectName);
			while (project.isImporting()) {
				Thread.sleep(1000);
				System.out.print(".");
			}
			System.out.print("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return project;
	}
}
