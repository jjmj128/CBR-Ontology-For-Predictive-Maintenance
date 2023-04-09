package User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import OntologyTools.CSVtoOntology;

/**
 * This class is executable. The function of this class is to fill up an
 * ontology .owl file with the desired new classes, properties or individuals
 * following the structure of data provided in a .csv file. The methods of the
 * class Ontology are used for that purpose. The ontology files must be saved in
 * RDF/XML syntax (choose this option when using Save ass in Protégé).
 * 
 * @author Juan
 *
 */
public class CSVtoOntologyExec {
	// Some attributes are taken directly from the class AppConfiguration.
	private static OWLOntologyManager m = CSVtoOntology.getManager();

	private static String base_ont_file_name = AppConfiguration.base_ont_file_name;
	private static String data_file_name = AppConfiguration.csv;
	private static String data_path = AppConfiguration.data_path;

	private static File base_ont_file = new File(data_path + base_ont_file_name);
	private static IRI main_iri = AppConfiguration.main_iri;
	private static IRI common_iri = AppConfiguration.common_iri;
	private static IRI obo_iri = AppConfiguration.obo_iri;
	private static IRI documentIRI = AppConfiguration.documentIRI;

	/**
	 * Executable method of the class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		////////////////////////////////////
		System.err.close(); // Hides errors and warnings, if errors are wanted to be monitored, these lines
							// must be commented.
		System.setErr(System.out);
		////////////////////////////////////////////

		CSVtoOntology.setmappers();// Setting the mappers for the ontology imports
		OWLOntology o;
		List<List<String>> data_base = new ArrayList<>();
		// Creating a variable that will store the content of the .csv file containing
		// the database in the shape of a table
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(data_path + data_file_name));

			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(";");
				data_base.add(Arrays.asList(values));

			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			o = m.loadOntologyFromOntologyDocument(base_ont_file);
			////// Loading data base into the ontology file

			CSVtoOntology.InstanceColumn(o, data_base, 0, "Predictive maintenance case", false, "Case", false);
			// Creates as many instances of the class 'Predictive maintenance case 'as cells
			// exist in the specified column of the data table(0).
			// Those instances are named as 'Case'+reference index contained in the
			// corresponding cell of the first column.
			CSVtoOntology.InstanceColumn(o, data_base, 17, "Predictive Maintenance Article", false, "Article", false);
			// Creates one instance of the class Predictive Maintenance Article for each one
			// of the titles contained in the cells of the
			// specified column (17). When a title is repeated, no new instance is created.
			// The instances are named as 'Article'+
			// reference index in of the first column corresponding to the first row where
			// each title appeared.
			CSVtoOntology.InstanceColumn(o, data_base, 1, "Publication year", true, "", false);
			// Creates one instance of the class Publication year for each one of the year
			// numbers in the column specified. Only one
			// instance is created for repeated year numbers. The names of the instances are
			// taken directly as the year number contained in the corresponding cells.
			CSVtoOntology.ClassColumn(o, data_base, 2, true, "Predictive_maintenance_module_function", false);
			// One instance for each cell in the specified column (2) is created. The class
			// of the instance is the content of the corresponding cell.
			// The classes are subclasses of 'Predictive_maintenance_module_function'. The
			// instances are named taken the class name + the reference
			// index taken from the first column of the data table.
			CSVtoOntology.ClassColumn(o, data_base, 3, true, "Maintainable item", false);
			// One instance for each cell in the specified column (3) is created. The class
			// of the instance is the content of the corresponding cell.
			// The classes are subclasses of Maintainable item. The instances are named
			// taken the class name + the reference
			// index taken from the first column of the data table.
			CSVtoOntology.ClassColumn(o, data_base, 5, true, "maintainable item record", false);
			// One instance for each cell in the specified column (5) is created. The class
			// of the instance is the content of the corresponding cell.
			// The classes are subclasses of maintainable item record. The instances are
			// named taken the class name + the reference
			// index taken from the first column of the data table.
			CSVtoOntology.InstanceColumn(o, data_base, 4, "item type", true, "", false);
			// Creates one instance of the class item type for each one of the names in the
			// specified column (4). Only one
			// instance is created for repeated names. The name of the instances is taken
			// directly from the corresponding cells.
			CSVtoOntology.InstanceColumn(o, data_base, 7, "Data_variable", true, "", true);
			// Creates one instance of the type Data_varibale for each one of the variable
			// names contained in the cell. Multiple names
			// can be found i the same cell separated by ', '. If the name of a variable is
			// repeated in more than one cell, only one
			// instance will be created.
			CSVtoOntology.InstanceColumn(o, data_base, 10, "Model type", true, "", true);
			// Creates one instance of the type Model type for each one of the terms
			// contained in the cell. Multiple terms
			// can be found i the same cell separated by ', '. If the term is repeated in
			// more than one cell, only one
			// instance will be created.
			CSVtoOntology.ClassColumn(o, data_base, 11, true, "Predictive maintenance model", true);
			// One instance for each cell in the specified column (11) is created. The class
			// of the instance is the content of the corresponding cell.
			// The classes are subclasses of 'Predictive maintenance model'. The instances
			// are named taken the class name + the reference
			// index taken from the first column of the data table.
			// Multiple terms
			// can be found i the same cell separated by ', '.

			CSVtoOntology.InstanceColumn(o, data_base, 0, "Predictive maintenance system module", false,
					"Predictive maintenance system module", false);
			// Creates as many instances of the class Predictive maintenance system module
			// as cells exist in the specified column of the data table(0).
			// Those instances are named as 'Predictive maintenance system module'+reference
			// index contained in the corresponding cell of the first column.
			CSVtoOntology.InstanceColumn(o, data_base, 12, "Module synchronization", true, "", false);
			// Creates one instance of the class Module synchronization for each one of the
			// names in the specified column (12). Only one
			// instance is created for repeated names. The name of the instances is taken
			// directly from the corresponding cells. Indeed,
			// for this class there are four possible instances in this particular case:
			// Online, Off-line, Both and Unknown synchronization.
			CSVtoOntology.InstanceColumn(o, data_base, 17, "Article title", false, "Article title", false);
			// Creates one instance of the class Article title for each one of the titles
			// contained in the cells of the
			// specified column (17). When a title is repeated, no new instance is created.
			// The instances are named as 'Article title'+
			// reference index in of the first column corresponding to the first row where
			// each title appeared.

			CSVtoOntology.InstanceColumn(o, data_base, 18, "Article identifier", false, "Article identifier", false);// Problems
			// Creates one instance of the class Article identifier for each one of the
			// titles contained in the cells of the
			// specified column (18). When a title is repeated, no new instance is created.
			// The instances are named as 'Article identifier'+
			// reference index in of the first column corresponding to the first row where
			// each title appeared.

			//////////////// Creation of the instance Case-Base, which is an Information
			//////////////// Bearing Entity of the ontology linked
			/////////////// to all the Information Content Entities in the actual case base.

			OWLDataFactory df = OWLManager.getOWLDataFactory();
			OWLClass clsA = df.getOWLClass(IRI.create(main_iri + "Predictive_Maintenance_case_base"));
			////// Getting the class in the ontology
			OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + "Case-Base"));
			/// Creating the ontology individual from the IRI

			OWLAxiom axiom = df.getOWLClassAssertionAxiom(clsA, IndA);
			AddAxiom addAxiom = new AddAxiom(o, axiom);
			m.applyChange(addAxiom);
			//// Applying the instance assertion axiom to the ontology

			////////////////////////////////////

			CSVtoOntology.ObjectPropertiesNumberedInstances(o, data_base, "Article identifier", "Article", 17, 18,
					common_iri + "designates");
			// Links all the instances of 'Article identifier' with their corresponding
			// instance of 'Article' through the object property 'designates' in the
			// ontology.
			// The columns 17 and 18 from the table are taken as a reference. In this case,
			// both column have the same number of different elements, as each article title
			// matches one identifier.
			CSVtoOntology.ObjectPropertiesNumberedInstances(o, data_base, "Article title", "Article", 17, 17,
					common_iri + "designates");
			// Links all the numbered instances of 'Article title' with their corresponding
			// numbered instance of 'Article' through the object property 'designates' in
			// the ontology.
			// The same reference column can be taken for both types.
			CSVtoOntology.ObjectPropertiesNumberedInstances(o, data_base, "Case",
					"Predictive maintenance system module", 0, 0, common_iri + "designates");
			// Links all the numbered instances of 'Case' with their corresponding numbered
			// instance of 'Predictive maintenance system module' through the object
			// property 'designates' in the ontology.
			// The same reference column can be taken for both types.
			CSVtoOntology.ObjectPropertiesObjecttoNumbered(o, data_base, "Case-Base", "Case", 0,
					obo_iri + "RO_0010002");
			// All the numbered instances of 'Predictive maintenance case' are attached to
			// the 'Case-Base' through the object property 'is carrier of'.
			CSVtoOntology.ObjectPropertiesObjecttoNumbered(o, data_base, "Case-Base", "Article title", 17,
					obo_iri + "RO_0010002");
			// All the numbered instances of 'Article title' are attached to the 'Case-Base'
			// through the object property 'is carrier of'.
			CSVtoOntology.ObjectPropertiesObjecttoNumbered(o, data_base, "Case-Base", "Article identifier", 18,
					obo_iri + "RO_0010002");
			// All the numbered instances of 'Article identifier' are attached to the
			// 'Case-Base' through the object property 'is carrier of'.
			CSVtoOntology.ObjectPropertiesObjecttoCell(o, data_base, "Case-Base", 1, obo_iri + "RO_0010002", false);
			// All the numbered instances of 'Publication year' are attached to the
			// 'Case-Base' through the object property 'is carrier of'.
			// The tag of the years are taken from the specified column (1). Each year will
			// be linked once.
			CSVtoOntology.ObjectPropertiesObjecttoCell(o, data_base, "Case-Base", 7, obo_iri + "RO_0010002", true);
			// All the numbered instances of 'Data_variable' are attached to the 'Case-Base'
			// through the object property 'is carrier of'.
			// The names of the variables are taken from the cells, which can contain more
			// than one variable (expected_multiple=true).
			CSVtoOntology.ObjectPropertiesObjecttoCell(o, data_base, "Case-Base", 10, obo_iri + "RO_0010002", true);
			// All the numbered instances of 'Model_type' are attached to the 'Case-Base'
			// through the object property 'is carrier of'.
			CSVtoOntology.ObjectPropertiesObjecttoCell(o, data_base, "Case-Base", 4, obo_iri + "RO_0010002", false);
			// All the numbered instances of 'item type' are attached to the 'Case-Base'
			// through the object property 'is carrier of'.
			CSVtoOntology.ObjectPropertiesNumberedInstances(o, data_base, "Article", "Article title", 17, 17,
					main_iri + "has_title");
			// Links all the numbered instances of 'Article' with their corresponding
			// numbered instance of 'Article title' through the object property 'has_title'
			// in the ontology.
			CSVtoOntology.ObjectPropertiesNumberedInstances(o, data_base, "Article", "Article identifier", 17, 18,
					main_iri + "has_identifier");
			// Links all the numbered instances of 'Article' with their corresponding
			// numbered instance of 'Article identifier' through the object property
			// 'has_identifier' in the ontology.
			CSVtoOntology.ObjectPropertiesNumberedtoCell(o, data_base, "Article", 17, 1,
					main_iri + "has_publication_year", false);
			// Links all the numbered instances of 'Article' with their corresponding
			// instance of 'Publication year' through the object property
			// 'has_publication_year' in the ontology.
			CSVtoOntology.ObjectPropertiesNumberedtoClassCell(o, data_base, "Article", 17, 11, obo_iri + "RO_0010002",
					true);
			// Links all the numbered instances of 'Article' with their corresponding
			// instances of 'Predictive maintenance model' through the object property 'is
			// carrier of' in the ontology.
			// The cells of the reference column 11 can contain more than one element (more
			// than one model).
			CSVtoOntology.ObjectPropertiesNumberedtoCell(o, data_base, "Article", 17, 4, obo_iri + "RO_0010002", false);
			// Links all the numbered instances of 'Article' with their corresponding
			// instances of 'item type' through the object property 'is carrier of' in the
			// ontology.
			CSVtoOntology.ObjectPropertiesCelltoNumbered(o, data_base, "Article", 17, 1, common_iri + "describes",
					false);
			// Links all the instances of 'Publication year' with their corresponding
			// numbered instances of 'Article' through the object property 'describes' in
			// the ontology.
			CSVtoOntology.ObjectPropertiesNumberedtoClassCell(o, data_base, "Predictive maintenance system module", 0,
					11, obo_iri + "RO_0010002", true);
			// Links all the numbered instances of 'Predictive maintenance system module'
			// with their corresponding instances of 'Predictive maintenance model' through
			// the object property 'is carrier of' in the ontology.
			CSVtoOntology.ObjectPropertiesNumberedtoClassCell(o, data_base, "Predictive maintenance system module", 0,
					5, obo_iri + "BFO_0000051", true);
			// Links all the numbered instances of 'Predictive maintenance system module'
			// with their corresponding instances of 'maintainable item record' through the
			// object property 'has part' in the ontology.
			CSVtoOntology.ObjectPropertiesCelltoNumbered(o, data_base, "Predictive maintenance system module", 0, 12,
					common_iri + "describes", false);
			// Links all the instances of 'Module synchronization' with their corresponding
			// numbered instances of 'Predictive maintenance system module' through the
			// object property 'describes' in the ontology.
			CSVtoOntology.ObjectPropertiesCelltoClassCell(o, data_base, 10, 11, common_iri + "describes", true, true);
			// Links all the instances of 'Model type' with their corresponding numbered
			// instances of 'Predictive maintenance model' through the object property
			// 'describes' in the ontology.
			// Both reference columns 10 and 11 can contain multiple elements separated by
			// ', '. If both cells have multiple elements, they each pair of cells must have
			// the same number of elements
			// so as to establish the property relation one to one. If one of the cells is
			// not multiple (single element), then all the elements of the other cell will
			// be linked to the first one.
			CSVtoOntology.ObjectPropertiesNumberedtoClassCell(o, data_base, "Predictive maintenance system module", 0,
					2, main_iri + "has_predictive_maintenance_function", false);
			// Links all the numbered instances of 'Predictive maintenance system module'
			// with numbered instances of the class specified in the reference column 2 (all
			// of them are subclasses of 'Predicitve_miantenance_function').
			// The object property used in the ontology is
			// 'has_predicitve_miantenance_function'.
			CSVtoOntology.ObjectPropertiesClassCelltoCell(o, data_base, 5, 7, obo_iri + "RO_0010002", false, true);
			// Links all the numbered instances of the subclasses of 'maintainable item
			// record' (as specified in column 5) to the instances of 'Data_variable' (as
			// they appear in column 7) through the object property 'is carrier of'.
			// Remember that the column of the variables can contain more than one term in
			// each cell.
			CSVtoOntology.ObjectPropertiesCelltoClassCell(o, data_base, 4, 3, common_iri + "describes", false, false);
			// Links all the instances of 'item type'of column 4 to the instances of the
			// subclasses of 'Maintainable item' (as specified in column 3) through the
			// object property 'describes'.
			CSVtoOntology.ObjectPropertiesNumberedtoClassCell(o, data_base, "Predictive maintenance system module", 0,
					3, obo_iri + "BFO_0000051", false);
			// Links all the numbered instances of 'Predictive maintenance system module'
			// with numbered instances of the class specified in the reference column 3 (all
			// of them are subclasses of 'Maintainable item').
			// The object property used in the ontology is 'has part'.
			CSVtoOntology.ObjectPropertiesNumberedtoCell(o, data_base, "Predictive maintenance system module", 0, 12,
					main_iri + "has_synchronization", false);
			// Links all the instances of 'Module Synchronization' with their corresponding
			// numbered instances of 'Predictive maintenance system module' through the
			// object property 'has_synchronization' in the ontology.
			CSVtoOntology.DataPropertiesNumberedtoCell(o, data_base, "Article title", 17, 17,
					main_iri + "has_text_value", false);
			// Data property declaration to state the literal containing the titles to the
			// instances of 'Article title'.
			CSVtoOntology.DataPropertiesNumberedtoCell(o, data_base, "Article identifier", 18, 18,
					main_iri + "has_text_value", false);
			// Data property declaration to state the literal containing the identifiers to
			// the instances of 'Article identifier'.
			CSVtoOntology.DataPropertiesNumberedtoCell(o, data_base, "Case", 0, 0, main_iri + "has_text_value", false);
			// Data property declaration to state the literal containing the references to
			// the instances of 'Predictive maintenance article'.

			try {
				m.saveOntology(o, documentIRI);
				// Saving ontology in the given file which has been assigned and IRI
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("ok");

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
