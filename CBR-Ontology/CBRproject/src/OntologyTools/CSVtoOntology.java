package OntologyTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import User.AppConfiguration;
import User.GeneralMethods;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImplString;

/**
 * In this class many methods are defined to fill up an owl ontology with the
 * desired new classes, individuals or properties assertions following the
 * structure of a structured file (for example .csv) that has been transformed
 * in an object of the type List, which works like a table. This methods can be
 * invoked from other classes to modify a given ontology. First column of the
 * table must contain a reference index number, one for each row, that will be
 * used to identify the individuals. The ontology files must be saved in RDF/XML
 * syntax (choose this option when using Save ass in Protégé).
 * 
 * @author Hugo
 *
 */

public class CSVtoOntology {
	// An OWLOntologyManger, which is a class of the OWL API is necessary to
	// manipulate the ontology to be modified.
	private static OWLOntologyManager m = OWLManager.createOWLOntologyManager();

	// All the following static attributes are defined in the AppConfiguration
	// class.
	// For more information on these attributes or if any of them should be modified
	// go to class AppConfiguration.
	private static String ont_file_name = AppConfiguration.ont_file_name;

	private static String data_path = AppConfiguration.data_path;
	private static File ont_file = new File(data_path + ont_file_name);

	private static IRI main_iri = AppConfiguration.main_iri;

	private static IRI documentIRI = AppConfiguration.documentIRI;

	private static SimpleIRIMapper artifact_mapper = AppConfiguration.artifact_mapper;

	private static SimpleIRIMapper ICE_mapper = AppConfiguration.ICE_mapper;

	private static SimpleIRIMapper ICEM_mapper = AppConfiguration.ICEM_mapper;

	private static SimpleIRIMapper event_mapper = AppConfiguration.event_mapper;

	private static SimpleIRIMapper geo_mapper = AppConfiguration.geo_mapper;

	private static SimpleIRIMapper relation_m_mapper = AppConfiguration.relation_m_mapper;

	private static SimpleIRIMapper relation_mapper = AppConfiguration.relation_mapper;

	private static SimpleIRIMapper ro_mapper = AppConfiguration.ro_mapper;

	private static SimpleIRIMapper time_mapper = AppConfiguration.time_mapper;

	/**
	 * 
	 * @return the OWLOntologyManager defined for this class.
	 */
	public static OWLOntologyManager getManager() {
		return m;

	}

	/**
	 * A static method to configure the mappers of the imported ontologies. The
	 * configuration of mappers is necessary when working with ontologies that
	 * import other ontologies from local files. Each mapper links one local
	 * file(.ttl) with the URI denomination which is used to invoke that ontology.
	 * If working with ontologies from internet servers, setting local mapper may
	 * not be necessary. The working ontology depends on some of the BFO and CCO
	 * ontologies, and some of them have crossed dependencies among the BFO and CCO
	 * ontologies. So, sometimes one dependency ontology may be invoked by slightly
	 * different URI references, and a mapper is requiered for each of them. If an
	 * exception occurs trying to manipulate an ontology because of a dependency
	 * ontology that could not be found, one possible solution is to save the local
	 * file corresponding to this ontology and to add a mapper joining this file to
	 * the missing URI.
	 * 
	 * 
	 * 
	 * 
	 */
	public static void setmappers() {
		m.getIRIMappers().add(artifact_mapper);
		m.getIRIMappers().add(time_mapper);
		m.getIRIMappers().add(ro_mapper);
		m.getIRIMappers().add(relation_mapper);
		m.getIRIMappers().add(relation_m_mapper);
		m.getIRIMappers().add(geo_mapper);
		m.getIRIMappers().add(event_mapper);
		m.getIRIMappers().add(ICE_mapper);
		m.getIRIMappers().add(ICEM_mapper);
	}

	///////// The methods below create ontological entities or relations following
	///////// the structure

	/**
	 * This method will create one (or multiple) instance of the ontology for each
	 * cell in a column of a table containing instances names. Remember that the
	 * first column of the table must contain one different reference index number
	 * for each row to identify the individuals.
	 * 
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param column_number     the number of the column of the table considered. If
	 *                          the names of the instances do not correspond with
	 *                          the content of the cells (column_name=false), then
	 *                          the content of the column still works as a
	 *                          reference, so as for cells having exactly the same
	 *                          content, only one instance will be created.
	 * 
	 * @param type              the ontology class name to which the instances will
	 *                          belong
	 * @param column_name       a boolean specifying whether the denomination of the
	 *                          instances should be taken directly form the content
	 *                          of the cell (true) or from the argument 'String
	 *                          name' below (false)
	 * @param name              is the generic name that will take the instances, so
	 *                          as their individual names will be name + reference
	 *                          index taken from the first column of the table and
	 *                          in case that the content of the cell, was repeated
	 *                          before in the same column, the reference from the
	 *                          first row where such content appeared at first will
	 *                          be taken.
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells is expected to have multiple elements
	 *                          separated by ', '. If expected_multiple is true, the
	 *                          method will check for each cell if the separator ',
	 *                          ' exists and proceed to create an instance for each
	 *                          one of the elements of the cell individually.
	 */
	public static void InstanceColumn(OWLOntology o, List<List<String>> data_base, int column_number, String type,
			boolean column_name, String name, boolean expected_multiple) {

		boolean multiple_field;
		String instance_name;
		int jlimit; // This variable will count the number of elements in each cell if required
		String content_index; // It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated

		OWLDataFactory df = OWLManager.getOWLDataFactory();

		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));

		ArrayList<String> content = new ArrayList<String>();// This variable will store the content of the cells
		// when the content has not been found before in the column.
		ArrayList<String> content_references = new ArrayList<String>();
		// This variable store the reference index of a cell content that has not been
		// found before in the column.

		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column

			//
			if (data_base.get(i).get(column_number).toString().contains(",") & expected_multiple == true) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}
			if (content.size() > 0 && content.contains(data_base.get(i).get(column_number))) {// Checking if the content
																								// of the cell
				// has appeared before in the column
				content_index = content_references.get(content.lastIndexOf(data_base.get(i).get(column_number)));
				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index = data_base.get(i).get(0);
				content.add(data_base.get(i).get(column_number));
				content_references.add(content_index);
				// New content is added with the corresponding reference index

			}

			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();
				// Separating the different elements of the cell if required
			} else {
				jlimit = 1;// By default there is a single element to take in the cell
			}

			for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell
				if (multiple_field) {

					instance_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_");
				} else {

					if (column_name) {// When the name of the individual is the content of the cell
						instance_name = data_base.get(i).get(column_number).toString().replace(" ", "_");
						// The name in the cell is taken directly
					} else {

						instance_name = name.replace(" ", "_") + (content_index);
						// The name is made from the type name and the reference index
					}
				}

				OWLClass clsA = df.getOWLClass(IRI.create(main_iri + type.replace(" ", "_")));
				// Getting the class in the ontology to which the instances belong from the
				// IRI(the one corresponding to the working ontology)
				OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name));
				// Creating the individual with the IRI

				OWLAxiom axiom = df.getOWLClassAssertionAxiom(clsA, IndA);
				AddAxiom addAxiom = new AddAxiom(o, axiom);
				m.applyChange(addAxiom);
				// Applies individual generation axiom to the ontology

			}

		}

		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method will create one (or multiple) instance of the ontology for each
	 * cell in a column of a table containing class names. Numbered instances will
	 * be created taking the class name and adding the reference index contained in
	 * the first column of the table. Remember that the first column of the table
	 * must contain one different reference index number for each row to identify
	 * the individuals.
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param column_number     the number of the column of the table considered
	 * @param subclass_axiom    specifies if the there is a subclass relation with
	 *                          another class
	 * @param superclass_name   of the class considered
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells is expected to have multiple elements
	 *                          separated by ', '. If expected_multiple is true, the
	 *                          method will check for each cell if the separator ',
	 *                          ' exists and proceed to take each one of the
	 *                          elements of the cell individually.
	 */
	public static void ClassColumn(OWLOntology o, List<List<String>> data_base, int column_number,
			boolean subclass_axiom, String superclass_name, boolean expected_multiple) {

		int jlimit; // This variable will count the number of elements in each cell if required
		String class_name;
		boolean multiple_field;
		String instance_name;
		OWLDataFactory df = OWLManager.getOWLDataFactory();

		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));

		for (int i = 1; i < data_base.size(); i++) {
			// Checking all the cells in the column

			if (data_base.get(i).get(column_number).toString().contains(",") & expected_multiple == true) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}

			//
			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();
				// Separating the different elements of the cell if required

			} else {
				jlimit = 1;
				// By default there is a single element to take in the cell
			}
			for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell
				if (multiple_field) {
					class_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_");

					instance_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_") + data_base.get(i).get(0).toString();
					// Forming the name of the instances

				} else {
					class_name = data_base.get(i).get(column_number).toString().replace(" ", "_");
					instance_name = data_base.get(i).get(column_number).toString().replace(" ", "_")
							+ data_base.get(i).get(0).toString();
				}
				OWLClass clsA = df.getOWLClass(IRI.create(main_iri + class_name));

				OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name));
				// Creating the individual with the IRI
				OWLAxiom axiom = df.getOWLClassAssertionAxiom(clsA, IndA);
				AddAxiom addAxiom = new AddAxiom(o, axiom);
				m.applyChange(addAxiom);
				// Applies individual generation axiom to the ontology
				if (subclass_axiom) {

					OWLClass clsB = df.getOWLClass(IRI.create(main_iri + superclass_name.replace(" ", "_")));
					OWLAxiom axiom_subclass = df.getOWLSubClassOfAxiom(clsA, clsB);
					// Subclass declaration if required
					AddAxiom addAxiom_subclass = new AddAxiom(o, axiom_subclass);
					m.applyChange(addAxiom_subclass);

				}

			}

		}

		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method allows to establish ObjectProperty relations between all the
	 * instances of two different types which denominations are formed by a generic
	 * name of the type + an index (for example: myType23). The property relation is
	 * built between the instances matching the same row of the data table. The
	 * indexing criteria for the numbered instances is equivalent to that used by
	 * the method InstanceColumn.
	 * 
	 * 
	 * @param o            OWLOntology (OWL API) object containing the target
	 *                     ontology
	 * @param data_base    containing the information in the shape of a table that
	 *                     has been created from a file
	 * @param name1        generic name of the first type of individual, origin of
	 *                     the ObjectProperty relation.Individuals are numbered
	 *                     according to the reference index number in the first
	 *                     column of the table.
	 * @param name2        generic name of the second type of individual, target of
	 *                     the ObjectProperty relation.Individuals are numbered
	 *                     according to the reference index number in the first
	 *                     column of the table.
	 * @param ref_column_1 the reference column for the first type of individual, so
	 *                     as only one instance will be considered for cells in the
	 *                     column having exactly the same content.
	 * @param ref_column_2 the reference column for the second type of individual,
	 *                     so as only one instance will be considered for cells in
	 *                     the column having exactly the same content.
	 * @param propertyIRI  the IRI identification of the property inside the
	 *                     ontology. This must be checked for the considered
	 *                     property in the working ontology.
	 */
	public static void ObjectPropertiesNumberedInstances(OWLOntology o, List<List<String>> data_base, String name1,
			String name2, int ref_column_1, int ref_column_2, String propertyIRI) {
		String content_index_1;
		String content_index_2;// It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated
		ArrayList<String> content_1 = new ArrayList<>();
		ArrayList<String> content_2 = new ArrayList<>();// This variables will store the content of the cells
		ArrayList<String> content_references_1 = new ArrayList<String>();
		ArrayList<String> content_references_2 = new ArrayList<String>();
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));

		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied

		for (int i = 1; i < data_base.size(); i++) {
			// Checking all the cells in the column

			if (content_1.size() > 0 && content_1.contains(data_base.get(i).get(ref_column_1))) {// Checking if the
																									// content of the
																									// cell
				// has appeared before in the column
				content_index_1 = content_references_1.get(content_1.lastIndexOf(data_base.get(i).get(ref_column_1)));
				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index_1 = data_base.get(i).get(0);
				content_1.add(data_base.get(i).get(ref_column_1));
				content_references_1.add(content_index_1);
				// New content is added with the corresponding reference index

			}

			if (content_2.size() > 0 && content_2.contains(data_base.get(i).get(ref_column_2))) {// Checking if the
																									// content of the
																									// cell

				// has appeared before in the column
				content_index_2 = content_references_2.get(content_2.lastIndexOf(data_base.get(i).get(ref_column_2)));

			} else {
				content_index_2 = data_base.get(i).get(0);
				content_2.add(data_base.get(i).get(ref_column_2));
				content_references_2.add(content_index_2);
				// New content is added with the corresponding reference index

			}

			OWLIndividual IndA = df
					.getOWLNamedIndividual(IRI.create(main_iri + name1.replace(" ", "_") + (content_index_1)));
			OWLIndividual IndB = df
					.getOWLNamedIndividual(IRI.create(main_iri + name2.replace(" ", "_") + (content_index_2)));
			// Creating the individuals with the IRI
			OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
			AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
			m.applyChange(addAxiom_property);

		}
		try {
			m.saveOntology(o, documentIRI);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method allows to establish ObjectProperty relations between instances of
	 * a certain type that are numbered (origin) and instances that take their
	 * denomination directly from the cells of the data base table (target). The
	 * property relation is built between the instances matching the same row of the
	 * data table. The indexing criteria for the numbered instances is equivalent to
	 * that used by the method InstanceColumn.
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param name              generic name of the type of individual, origin of
	 *                          the ObjectProperty relation.Individuals are numbered
	 *                          according to the reference index number in the first
	 *                          column of the table.
	 * @param ref_column        the reference column for the type of individual, so
	 *                          as only one instance will be considered for cells in
	 *                          the column having exactly the same content.
	 * @param column_number     the number of the column of the table considered. If
	 *                          the names of the instances do not correspond with
	 *                          the content of the cells (column_name=false), then
	 *                          the content of the column still works as a
	 *                          reference, so as for cells having exactly the same
	 *                          content, only one instance will be created.
	 * @param propertyIRI       the IRI identification of the property inside the
	 *                          ontology. This must be checked for the considered
	 *                          property in the working ontology.
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells is expected to have multiple elements
	 *                          separated by ', '. If expected_multiple is true, the
	 *                          method will check for each cell if the separator ',
	 *                          ' exists and proceed to create an instance for each
	 *                          one of the elements of the cell individually.
	 */
	public static void ObjectPropertiesNumberedtoCell(OWLOntology o, List<List<String>> data_base, String name,
			int ref_column, int column_number, String propertyIRI, boolean expected_multiple) {

		boolean multiple_field;
		String instance_name;
		int jlimit; // This variable will count the number of elements in each cell if required
		String content_index; // It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated
		boolean repeated_content = false;
		ArrayList<String> content = new ArrayList<>();// This variable will store the content of the cells
		// when the content has not been found before in the column.
		ArrayList<String> content_references = new ArrayList<String>(); // This variable store the reference index of a
																		// cell content that has not been found before
																		// in the column.

		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied

		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column
			if (content.size() > 0 && content.contains(data_base.get(i).get(ref_column))) {// Checking if the content of
																							// the cell
				// has appeared before in the column
				content_index = content_references.get(content.lastIndexOf(data_base.get(i).get(ref_column)));
				repeated_content = true;
				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index = data_base.get(i).get(0);
				content.add(data_base.get(i).get(ref_column));
				content_references.add(content_index);
				// New content is added with the corresponding reference index

			}
			OWLIndividual IndA;// This will be the numbered individual, origin of the property
			if (repeated_content) {
				IndA = df.getOWLNamedIndividual(IRI.create(main_iri + name.replace(" ", "_") + (content_index)));
				// Creating the individual with the IRI
			} else {
				IndA = df.getOWLNamedIndividual(
						IRI.create(main_iri + name.replace(" ", "_") + data_base.get(i).get(0).toString()));
				// Creating the individual with the IRI
			}
			if (data_base.get(i).get(column_number).toString().contains(",") & expected_multiple) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}

			//
			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();
				// Separating the different elements of the cell if required
			} else {
				jlimit = 1;// By default there is a single element to take in the cell
			}
			for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell
				if (multiple_field) {

					instance_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_");
					// Forming the name of the instances
				} else {

					instance_name = data_base.get(i).get(column_number).toString().replace(" ", "_");
					// Forming the name of the instances
				}

				OWLIndividual IndB = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name.replace(" ", "_")));
				// Creating the individual with the IRI. This will be the second individual, the
				// one which name is taken
				// from the cell and it is the target of the property
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology

			}
		}

		try {
			m.saveOntology(o, documentIRI);// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method allows to establish ObjectProperty relations between instances
	 * that take their denomination directly from the cells of the data base table
	 * (origin) and instances of a certain type that are numbered (target). The
	 * property relation is built between the instances matching the same row of the
	 * data table. The indexing criteria for the numbered instances is equivalent to
	 * that used by the method InstanceColumn.
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param name              generic name of the type of individual, origin of
	 *                          the ObjectProperty relation.Individuals are numbered
	 *                          according to the reference index number in the first
	 *                          column of the table.
	 * @param ref_column        the reference column for the type of individual, so
	 *                          as only one instance will be considered for cells in
	 *                          the column having exactly the same content.
	 * @param column_number     the number of the column of the table considered. If
	 *                          the names of the instances do not correspond with
	 *                          the content of the cells (column_name=false), then
	 *                          the content of the column still works as a
	 *                          reference, so as for cells having exactly the same
	 *                          content, only one instance will be created.
	 * @param propertyIRI       the IRI identification of the property inside the
	 *                          ontology. This must be checked for the considered
	 *                          property in the working ontology.
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells is expected to have multiple elements
	 *                          separated by ', '. If expected_multiple is true, the
	 *                          method will check for each cell if the separator ',
	 *                          ' exists and proceed to create an instance for each
	 *                          one of the elements of the cell individually.
	 */
	public static void ObjectPropertiesCelltoNumbered(OWLOntology o, List<List<String>> data_base, String name,
			int ref_column, int column_number, String propertyIRI, boolean expected_multiple) {

		boolean multiple_field;
		String instance_name;
		int jlimit;// This variable will count the number of elements in each cell if required
		String content_index;// It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated
		boolean repeated_content = false;
		ArrayList<String> content = new ArrayList<>();// This variable will store the content of the cells
		// when the content has not been found before in the column.
		ArrayList<String> content_references = new ArrayList<String>();

		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied
		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the colum

			if (data_base.get(i).get(column_number).toString().contains(",") & expected_multiple == true) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}
			if (content.size() > 0 && content.contains(data_base.get(i).get(ref_column))) {// Checking if the content of
																							// the cell
				// has appeared before in the column
				content_index = content_references.get(content.lastIndexOf(data_base.get(i).get(ref_column)));
				repeated_content = true;
				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index = data_base.get(i).get(0);
				content.add(data_base.get(i).get(ref_column));
				content_references.add(content_index);
				// New content is added with the corresponding reference index

			}

			//
			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();
				// Separating the different elements of the cell if required
			} else {
				jlimit = 1;// By default there is a single element to take in the cell
			}
			for (int j = 0; j < jlimit; j++) {
				if (multiple_field) {

					instance_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_");
				} else {

					instance_name = data_base.get(i).get(column_number).toString().replace(" ", "_");
				}
				OWLIndividual IndB;// This will be the numbered individual, target of the property
				if (repeated_content) {
					IndB = df.getOWLNamedIndividual(IRI.create(main_iri + name.replace(" ", "_") + (content_index)));
					// Creating the individual with the IRI
				} else {
					IndB = df.getOWLNamedIndividual(
							IRI.create(main_iri + name.replace(" ", "_") + data_base.get(i).get(0).toString()));
					// Creating the individual with the IRI

				}

				OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name.replace(" ", "_")));
				// Creating the individual with the IRI. This will be the first individual, the
				// one which name is taken
				// from the cell and it is the target of the origin
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology

			}
		}

		try {
			m.saveOntology(o, documentIRI);// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method allows to establish ObjectProperty relations between instances
	 * that take their denomination directly from the cells of the data base table.
	 * The property relation is built between the instances matching the same row of
	 * the data table.The indexing criteria for the numbered instances is equivalent
	 * to that used by the method InstanceColumn. The first column is the origin of
	 * the property and the second column is the target. If one of the cells of one
	 * group has multiple elements and the corresponding cell in the second group
	 * has not, then the property will link each one of the elements of the multiple
	 * cell to the one in the other cell. If both cells are multiple, they must have
	 * the same number of elements, so as the property will link those elements one
	 * to one from one cell to the other in the same order.
	 * 
	 * @param o                   OWLOntology (OWL API) object containing the target
	 *                            ontology
	 * @param data_base           containing the information in the shape of a table
	 *                            that has been created from a file
	 * @param column_number_1     the number of the column of the table considered
	 *                            for the first group of individuals (origin)
	 * @param column_number_2     the number of the column of the table considered
	 *                            for the second group of individuals (target)
	 * @param propertyIRI         the IRI identification of the property inside the
	 *                            ontology. This must be checked for the considered
	 *                            property in the working ontology.
	 * @param expected_multiple_1 is a boolean to state if the content of the target
	 *                            cells of the first column is expected to have
	 *                            multiple elements separated by ', '. If
	 *                            expected_multiple is true, the method will check
	 *                            for each cell if the separator ', ' exists and
	 *                            proceed to create an instance for each one of the
	 *                            elements of the cell individually.
	 * @param expected_multiple_2 is a boolean to state if the content of the target
	 *                            cells of the second column is expected to have
	 *                            multiple elements separated by ', '. If
	 *                            expected_multiple is true, the method will check
	 *                            for each cell if the separator ', ' exists and
	 *                            proceed to create an instance for each one of the
	 *                            elements of the cell individually.
	 */
	public static void ObjectPropertiesCelltoCell(OWLOntology o, List<List<String>> data_base, int column_number_1,
			int column_number_2, String propertyIRI, boolean expected_multiple_1, boolean expected_multiple_2) {

		boolean multiple_field_1;
		boolean multiple_field_2;
		String instance_name_1;// Name that will be assigned to the individuals of the first group
		String instance_name_2;// Name that will be assigned to the individuals of the second group
		int jlimit;// This variable will count the number of elements in each cell if required
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied
		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the columns

			if (data_base.get(i).get(column_number_1).toString().contains(",") & expected_multiple_1) {
				multiple_field_1 = true;
				// Multiple elements will be taken from the cells in first group if they are
				// expected and ', ' separators exist

			} else {
				multiple_field_1 = false;

			}

			if (data_base.get(i).get(column_number_2).toString().contains(",") & expected_multiple_2) {
				multiple_field_2 = true;
				// Multiple elements will be taken from the cells in second group if they are
				// expected and ', ' separators exist
			} else {
				multiple_field_2 = false;

			}

			if (multiple_field_1 & multiple_field_2) {// When both cells have multiple elements

				if (GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size() == GeneralMethods
						.separate(data_base.get(i).get(column_number_2).toString()).size()) {
					// If both cell have multiple elements, they must have the same number of them
					jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();
					for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

						instance_name_1 = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString())
								.get(j).toString().replace(" ", "_");
						instance_name_2 = GeneralMethods.separate(data_base.get(i).get(column_number_2).toString())
								.get(j).toString().replace(" ", "_");

						// Forming the name of the instances

						OWLIndividual IndA = df
								.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
						OWLIndividual IndB = df
								.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));

						// Creating the individuals with the IRI
						OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
						AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
						m.applyChange(addAxiom_property);
						// Applies individual generation axiom to the ontology

					}

				} else {

					System.out.println("Error: first and second cell do not have the same number of elements.");
				}

			} else if (multiple_field_1 == true & multiple_field_2 == false) {// When first cell is multiple but not the
																				// second
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();
				for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

					instance_name_1 = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).get(j)
							.toString().replace(" ", "_");
					instance_name_2 = data_base.get(i).get(column_number_2).toString().replace(" ", "_");
					// Forming the name of the instances
					OWLIndividual IndA = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
					OWLIndividual IndB = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
					// Creating the individuals with the IRI
					OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
					AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
					m.applyChange(addAxiom_property);
					// Applies individual generation axiom to the ontology

				}

			} else if (multiple_field_1 == false & multiple_field_2 == true) {// When second cell is multiple but not
																				// the first
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();
				for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

					instance_name_2 = GeneralMethods.separate(data_base.get(i).get(column_number_2).toString()).get(j)
							.toString().replace(" ", "_");
					instance_name_1 = data_base.get(i).get(column_number_1).toString().replace(" ", "_");
					// Forming the name of the instances
					OWLIndividual IndA = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
					OWLIndividual IndB = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
					// Creating the individuals with the IRI
					OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
					AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
					m.applyChange(addAxiom_property);
					// Applies individual generation axiom to the ontology

				}

			} else {// When none of the cells have multiple elements
				instance_name_1 = data_base.get(i).get(column_number_1).toString().replace(" ", "_");
				instance_name_2 = data_base.get(i).get(column_number_2).toString().replace(" ", "_");
				// Forming the name of the instances
				OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
				OWLIndividual IndB = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
				// Creating the individuals with the IRI
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology
			}

		}

		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method allows to establish ObjectProperty relations between instances
	 * belonging to the class specified in a cell from the cells of the data base
	 * table (origin) and instances of a certain type that are numbered (target).
	 * The property relation is built between the instances matching the same row of
	 * the data table. The indexing criteria for the numbered instances is
	 * equivalent to that used by the method ClassColumn. The first column contains
	 * the names of classes for the first group of individuals and the second column
	 * is just the reference for the numbered individuals of the second group.
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param name              generic name of the type of individual, target of
	 *                          the ObjectProperty relation.Individuals are numbered
	 *                          according to the reference index number in the first
	 *                          column of the table.
	 * @param ref_column        the reference column for the type of individual, so
	 *                          as only one instance will be considered for cells in
	 *                          the column having exactly the same content.
	 * @param column_number     the number of the column of the table considered. If
	 *                          the names of the instances do not correspond with
	 *                          the content of the cells (column_name=false), then
	 *                          the content of the column still works as a
	 *                          reference, so as for cells having exactly the same
	 *                          content, only one instance will be created.
	 * @param propertyIRI       the IRI identification of the property inside the
	 *                          ontology. This must be checked for the considered
	 *                          property in the working ontology.
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells is expected to have multiple elements
	 *                          separated by ', '. If expected_multiple is true, the
	 *                          method will check for each cell if the separator ',
	 *                          ' exists and proceed to create an instance for each
	 *                          one of the elements of the cell individually.
	 */
	public static void ObjectPropertiesClassCelltoNumbered(OWLOntology o, List<List<String>> data_base, String name,
			int ref_column, int column_number, String propertyIRI, boolean expected_multiple) {

		boolean multiple_field;
		String instance_name;
		int jlimit;// This variable will count the number of elements in each cell if required
		String content_index;// It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated
		boolean repeated_content = false;
		ArrayList<String> content = new ArrayList<>();// This variable will store the content of the cells
		// when the content has not been found before in the column.
		ArrayList<String> content_references = new ArrayList<String>();
		// This variable store the reference index of a cell content that has not been
		// found before in the column.
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied
		for (int i = 1; i < data_base.size(); i++) {
			// Checking all the cells in the columns

			if (data_base.get(i).get(column_number).toString().contains(",") & expected_multiple) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}
			if (content.size() > 0 && content.contains(data_base.get(i).get(ref_column))) {// Checking if the content of
																							// the cell
				// has appeared before in the column
				content_index = content_references.get(content.lastIndexOf(data_base.get(i).get(ref_column)));
				repeated_content = true;
				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index = data_base.get(i).get(0);
				content.add(data_base.get(i).get(ref_column));
				content_references.add(content_index);
				// New content is added with the corresponding reference index

			}

			//
			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();
				// Separating the different elements of the cell if required
			} else {
				jlimit = 1;// By default there is a single element to take in the cell
			}
			for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell
				if (multiple_field) {

					instance_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_") + data_base.get(i).get(0);
				} else {

					instance_name = data_base.get(i).get(column_number).toString().replace(" ", "_")
							+ data_base.get(i).get(0);
				}
				OWLIndividual IndB;// This will be the target individual of the property
				if (repeated_content) {
					IndB = df.getOWLNamedIndividual(IRI.create(main_iri + name.replace(" ", "_") + (content_index)));
					// Creating the individual with the IRI.This will be the origin individual of
					// the property relation
				} else {
					IndB = df.getOWLNamedIndividual(
							IRI.create(main_iri + name.replace(" ", "_") + data_base.get(i).get(0).toString()));
					// Creating the individual with the IRI.This will be the origin individual of
					// the property relation
				}
				OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name.replace(" ", "_")));
				// Creating the individual with the IRI.This will be the origin individual of
				// the property relation
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology

			}
		}

		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method allows to establish ObjectProperty relations between instances of
	 * a certain type that are numbered (origin) and instances belonging to the
	 * class specified in a cell from the cells of the data base table (target). The
	 * property relation is built between the instances matching the same row of the
	 * data table. The indexing criteria for the numbered instances is equivalent to
	 * that used by the method ClassColumn. The first column is just the reference
	 * for the numbered individuals of the first group and the second column
	 * contains the names of classes for the second group of individuals.
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param name              generic name of the type of individual, target of
	 *                          the ObjectProperty relation.Individuals are numbered
	 *                          according to the reference index number in the first
	 *                          column of the table.
	 * @param ref_column        the reference column for the type of individual, so
	 *                          as only one instance will be considered for cells in
	 *                          the column having exactly the same content.
	 * @param column_number     the number of the column of the table considered. If
	 *                          the names of the instances do not correspond with
	 *                          the content of the cells (column_name=false), then
	 *                          the content of the column still works as a
	 *                          reference, so as for cells having exactly the same
	 *                          content, only one instance will be created.
	 * @param propertyIRI       the IRI identification of the property inside the
	 *                          ontology. This must be checked for the considered
	 *                          property in the working ontology.
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells is expected to have multiple elements
	 *                          separated by ', '. If expected_multiple is true, the
	 *                          method will check for each cell if the separator ',
	 *                          ' exists and proceed to create an instance for each
	 *                          one of the elements of the cell individually.
	 */
	public static void ObjectPropertiesNumberedtoClassCell(OWLOntology o, List<List<String>> data_base, String name,
			int ref_column, int column_number, String propertyIRI, boolean expected_multiple) {

		boolean multiple_field;
		String instance_name;// This is the name that will be given to the numbered individuals of the first
								// group
		int jlimit;// This variable will count the number of elements in each cell if required
		String content_index;// It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated
		boolean repeated_content = false;
		ArrayList<String> content = new ArrayList<>();// This variable will store the content of the cells
		// when the content has not been found before in the column.
		ArrayList<String> content_references = new ArrayList<String>();
		// This variable store the reference index of a cell content that has not been
		// found before in the column.
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied
		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column

			if (data_base.get(i).get(column_number).toString().contains(",") & expected_multiple) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}
			if (content.size() > 0 && content.contains(data_base.get(i).get(ref_column))) {// Checking if the content of
																							// the cell
				// has appeared before in the column
				content_index = content_references.get(content.lastIndexOf(data_base.get(i).get(ref_column)));
				repeated_content = true;
				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index = data_base.get(i).get(0);
				content.add(data_base.get(i).get(ref_column));
				content_references.add(content_index);
				// New content is added with the corresponding reference index

			}

			//
			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();

				// Separating the different elements of the cell if required
			} else {
				jlimit = 1;// By default there is a single element to take in the cell

			}
			for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell
				if (multiple_field) {

					instance_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_") + data_base.get(i).get(0);
					// Forming the name of the instances
				} else {

					instance_name = data_base.get(i).get(column_number).toString().replace(" ", "_")
							+ data_base.get(i).get(0);
					// Forming the name of the instances
				}
				OWLIndividual IndA;// This is the origin instance of the property relation, which belongs to the
									// first group of individuals
				if (repeated_content) {
					IndA = df.getOWLNamedIndividual(IRI.create(main_iri + name.replace(" ", "_") + (content_index)));
					// Creating the individual with the IRI
				} else {
					IndA = df.getOWLNamedIndividual(
							IRI.create(main_iri + name.replace(" ", "_") + data_base.get(i).get(0).toString()));
					// Creating the individual with the IRI

				}

				OWLIndividual IndB = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name.replace(" ", "_")));
				// Creating the individual with the IRI. This will be the target individual,
				// which belongs to the second group.
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology

			}
		}

		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method allows to create a property relation between instances which
	 * classes are specified in the cells of a column and the corresponding
	 * instances of another column which names are taken directly from the content
	 * of the cells. The first group of instances (origin) are named by the
	 * numbering criteria of the method ClassColumn, and the denomination of the
	 * instances of the second group (target) are named directly with the content of
	 * the cells of the second column. If one of the cells has multiple element and
	 * the other one has not, then the property will link each one of the elements
	 * in the multiple cell with the only element take from the corresponding cell.
	 * If both cells are multiple, then they must have the same number of elements,
	 * and they will be linked by the property one to one in the same order.
	 * 
	 * @param o                   o OWLOntology (OWL API) object containing the
	 *                            target ontology
	 * @param data_base           containing the information in the shape of a table
	 *                            that has been created from a file
	 * @param column_number_1     the number of the column of the table considered
	 *                            for the first group of individuals (origin)
	 * @param column_number_2     the number of the column of the table considered
	 *                            for the second group of individuals (target)
	 * @param propertyIRI         the IRI identification of the property inside the
	 *                            ontology. This must be checked for the considered
	 *                            property in the working ontology.
	 * @param expected_multiple_1 is a boolean to state if the content of the target
	 *                            cells of the first column is expected to have
	 *                            multiple elements separated by ', '. If
	 *                            expected_multiple is true, the method will check
	 *                            for each cell if the separator ', ' exists and
	 *                            proceed to create an instance for each one of the
	 *                            elements of the cell individually.
	 * @param expected_multiple_2 is a boolean to state if the content of the target
	 *                            cells of the second column is expected to have
	 *                            multiple elements separated by ', '. If
	 *                            expected_multiple is true, the method will check
	 *                            for each cell if the separator ', ' exists and
	 *                            proceed to create an instance for each one of the
	 *                            elements of the cell individually.
	 */
	public static void ObjectPropertiesClassCelltoCell(OWLOntology o, List<List<String>> data_base, int column_number_1,
			int column_number_2, String propertyIRI, boolean expected_multiple_1, boolean expected_multiple_2) {

		boolean multiple_field_1;
		boolean multiple_field_2;
		String instance_name_1;// This is the name that will be given to the numbered instances of the first
								// group
		String instance_name_2;// This is the name that will be given to the numbered instances of the second
								// group
		int jlimit;// This variable will count the number of elements in each cell if required
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));

		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));

		// Gets the ontology property to be applied

		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column

			if (data_base.get(i).get(column_number_1).toString().contains(",") & expected_multiple_1) {
				multiple_field_1 = true;

				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field_1 = false;

			}

			if (data_base.get(i).get(column_number_2).toString().contains(",") & expected_multiple_2) {
				multiple_field_2 = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field_2 = false;

			}

			if (multiple_field_1 & multiple_field_2) {// When both cells from each one of the columns have multiple
														// elements

				if (GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size() == GeneralMethods
						.separate(data_base.get(i).get(column_number_2).toString()).size()) {
					jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();
					// Separating the different elements of the cell if required

					for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

						instance_name_1 = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString())
								.get(j).toString().replace(" ", "_") + data_base.get(i).get(0);
						instance_name_2 = GeneralMethods.separate(data_base.get(i).get(column_number_2).toString())
								.get(j).toString().replace(" ", "_");
						// Forming the names of the instances

						OWLIndividual IndA = df
								.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
						OWLIndividual IndB = df
								.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));

						// Creating the individuals (origin and target) with the IRI

						OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
						AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
						m.applyChange(addAxiom_property);
						// Applies individual generation axiom to the ontology

					}

				} else {// If both cells are multiple, they must have the same number of elements

					System.out.println("Error: first and second cell do not have the same number of elements.");
				}

			} else if (multiple_field_1 == true & multiple_field_2 == false) {// When the cell of the first group is
																				// multiple but not the second
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();
				// Separating the different elements of the cell if required
				for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

					instance_name_1 = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).get(j)
							.toString().replace(" ", "_") + data_base.get(i).get(0);
					instance_name_2 = data_base.get(i).get(column_number_2).toString().replace(" ", "_");
					// Forming the names of the instances
					OWLIndividual IndA = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
					OWLIndividual IndB = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
					// Creating the individuals with the IRI
					OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
					AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
					m.applyChange(addAxiom_property);
					// Applies individual generation axiom to the ontology

				}

			} else if (multiple_field_1 == false & multiple_field_2 == true) {// When the cell of the second group is
																				// multiple but not the first
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_2).toString()).size();
				// Separating the different elements of the cell if required
				for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

					instance_name_2 = GeneralMethods.separate(data_base.get(i).get(column_number_2).toString()).get(j)
							.toString().replace(" ", "_");
					instance_name_1 = data_base.get(i).get(column_number_1).toString().replace(" ", "_")
							+ data_base.get(i).get(0);
					// Forming the names of the instances
					OWLIndividual IndA = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
					OWLIndividual IndB = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
					// Creating the individuals with the IRI
					OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
					AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
					m.applyChange(addAxiom_property);
					// Applies individual generation axiom to the ontology

				}

			} else {
				instance_name_1 = data_base.get(i).get(column_number_1).toString().replace(" ", "_")
						+ data_base.get(i).get(0);
				instance_name_2 = data_base.get(i).get(column_number_2).toString().replace(" ", "_");
				// Forming the names of the instances
				OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
				OWLIndividual IndB = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
				// Creating the individuals with the IRI
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology

			}

		}

		try {
			m.saveOntology(o, documentIRI);// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method allows to create a property relation between instances which
	 * names are taken directly from the content of the cells of the first column
	 * and the corresponding instances of the other group which classes are
	 * specified in the cells of the second column. The first group of instances
	 * (origin) are named directly with the content of the cells of the first
	 * column, and the denomination of the instances of the second group (target)
	 * are named by the numbering criteria of the method ClassColumn. If one of the
	 * cells has multiple element and the other one has not, then the property will
	 * link each one of the elements in the multiple cell with the only element take
	 * from the corresponding cell. If both cells are multiple, then they must have
	 * the same number of elements, and they will be linked by the property one to
	 * one in the same order.
	 * 
	 * @param o                   o OWLOntology (OWL API) object containing the
	 *                            target ontology
	 * @param data_base           containing the information in the shape of a table
	 *                            that has been created from a file
	 * @param column_number_1     the number of the column of the table considered
	 *                            for the first group of individuals (origin)
	 * @param column_number_2     the number of the column of the table considered
	 *                            for the second group of individuals (target)
	 * @param propertyIRI         the IRI identification of the property inside the
	 *                            ontology. This must be checked for the considered
	 *                            property in the working ontology.
	 * @param expected_multiple_1 is a boolean to state if the content of the target
	 *                            cells of the first column is expected to have
	 *                            multiple elements separated by ', '. If
	 *                            expected_multiple is true, the method will check
	 *                            for each cell if the separator ', ' exists and
	 *                            proceed to create an instance for each one of the
	 *                            elements of the cell individually.
	 * @param expected_multiple_2 is a boolean to state if the content of the target
	 *                            cells of the second column is expected to have
	 *                            multiple elements separated by ', '. If
	 *                            expected_multiple is true, the method will check
	 *                            for each cell if the separator ', ' exists and
	 *                            proceed to create an instance for each one of the
	 *                            elements of the cell individually.
	 */
	public static void ObjectPropertiesCelltoClassCell(OWLOntology o, List<List<String>> data_base, int column_number_1,
			int column_number_2, String propertyIRI, boolean expected_multiple_1, boolean expected_multiple_2) {

		boolean multiple_field_1;
		boolean multiple_field_2;
		String instance_name_1;// This is the name that will be given to the instances in the first group
		String instance_name_2;// This is the name that will be given to the instances in the second group
		int jlimit;// This variable will count the number of elements in each cell if required
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied
		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column

			if (data_base.get(i).get(column_number_1).toString().contains(",") & expected_multiple_1) {
				multiple_field_1 = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field_1 = false;

			}

			if (data_base.get(i).get(column_number_2).toString().contains(",") & expected_multiple_2) {
				multiple_field_2 = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field_2 = false;

			}

			if (multiple_field_1 & multiple_field_2) {// When both cells from each one of the columns have multiple
														// elements

				if (GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size() == GeneralMethods
						.separate(data_base.get(i).get(column_number_2).toString()).size()) {
					jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();

					// Separating the different elements of the cell if required
					for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

						instance_name_1 = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString())
								.get(j).toString().replace(" ", "_");
						instance_name_2 = GeneralMethods.separate(data_base.get(i).get(column_number_2).toString())
								.get(j).toString().replace(" ", "_") + data_base.get(i).get(0);
						// Forming the names of the instance

						OWLIndividual IndA = df
								.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
						OWLIndividual IndB = df
								.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
						// Creating the individual with the IRI
						OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
						AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
						m.applyChange(addAxiom_property);
						// Applies individual generation axiom to the ontology

					}

				} else {// If both cells are multiple, they must have the same number of elements.

					System.out.println("Error: first and second cell do not have the same number of elements.");
				}

			} else if (multiple_field_1 == true & multiple_field_2 == false) {// When the first cell has multiple
																				// elements but not the second
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();
				// Separating the different elements of the cell if required
				for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

					instance_name_1 = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).get(j)
							.toString().replace(" ", "_");
					instance_name_2 = data_base.get(i).get(column_number_2).toString().replace(" ", "_")
							+ data_base.get(i).get(0);
					// Forming the names of the instance
					OWLIndividual IndA = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
					OWLIndividual IndB = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
					// Creating the individual with the IRI
					OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
					AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
					m.applyChange(addAxiom_property);
					// Applies individual generation axiom to the ontology

				}

			} else if (multiple_field_1 == false & multiple_field_2 == true) {// When the second cell has multiple
																				// elements but not the first
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number_1).toString()).size();
				// Separating the different elements of the cell if required
				for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell

					instance_name_2 = GeneralMethods.separate(data_base.get(i).get(column_number_2).toString()).get(j)
							.toString().replace(" ", "_") + data_base.get(i).get(0);
					instance_name_1 = data_base.get(i).get(column_number_1).toString().replace(" ", "_");
					// Forming the names of the instance
					OWLIndividual IndA = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
					OWLIndividual IndB = df
							.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
					// Creating the individual with the IRI
					OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
					AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
					m.applyChange(addAxiom_property);
					// Applies individual generation axiom to the ontology

				}

			} else {
				instance_name_1 = data_base.get(i).get(column_number_1).toString().replace(" ", "_");
				instance_name_2 = data_base.get(i).get(column_number_2).toString().replace(" ", "_")
						+ data_base.get(i).get(0);
				// Forming the names of the instance
				OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name_1.replace(" ", "_")));
				OWLIndividual IndB = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name_2.replace(" ", "_")));
				// Creating the individual with the IRI
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology
			}

		}

		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method allows to establish ObjectProperty relations between one specific
	 * instance (origin) and a group of numbered instances (target) taking the name
	 * from column of the data table as reference and the numbering criteria of the
	 * method InstanceColumn.
	 * 
	 * @param o             OWLOntology (OWL API) object containing the target
	 *                      ontology
	 * @param data_base     containing the information in the shape of a table that
	 *                      has been created from a file
	 * @param name          of the specific instance to which the properties
	 *                      relations are wanted to be assigned
	 * @param instance_type generic name of the type of individual, target of the
	 *                      ObjectProperty relation.Individuals are numbered
	 *                      according to the reference index number in the first
	 *                      column of the table.
	 * @param ref_column    the reference column for the type of individual, so as
	 *                      only one instance will be considered
	 * @param propertyIRI   the IRI identification of the property inside the
	 *                      ontology. This must be checked for the considered
	 *                      property in the working ontology.
	 */
	public static void ObjectPropertiesObjecttoNumbered(OWLOntology o, List<List<String>> data_base, String name,
			String instance_type, int ref_column, String propertyIRI) {

		String content_index;// It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated
		boolean repeated_content = false;
		ArrayList<String> content_references = new ArrayList<String>();
		// This variable store the reference index of a cell content that has not been
		// found before in the column.
		ArrayList<String> content = new ArrayList<>();// This variable will store the content of the cells
		// when the content has not been found before in the column.
		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied
		OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + name.replace(" ", "_")));
		// Creating the individual with the IRI
		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column
			if (content.size() > 0 && content.contains(data_base.get(i).get(ref_column))) {// Checking if the content of
																							// the cell
				// has appeared before in the column
				content_index = content_references.get(content.lastIndexOf(data_base.get(i).get(ref_column)));
				repeated_content = true;

				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index = data_base.get(i).get(0);
				content.add(data_base.get(i).get(ref_column));
				content_references.add(content_index);
				// New content is added with the corresponding reference index

			}

			OWLIndividual IndB;// This will be the target ontology individual
			if (repeated_content) {
				IndB = df.getOWLNamedIndividual(
						IRI.create(main_iri + instance_type.replace(" ", "_") + (content_index)));
				// Creating the individual with the IRI
			} else {
				IndB = df.getOWLNamedIndividual(
						IRI.create(main_iri + instance_type.replace(" ", "_") + data_base.get(i).get(0).toString()));
				// Creating the individual with the IRI
			}

			OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
			AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
			m.applyChange(addAxiom_property);
			// Applies individual generation axiom to the ontology
		}
		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method allows to establish ObjectProperty relations between one specific
	 * instance (origin) and a group of numbered instances (target) taking the class
	 * type from a column of the data table as reference and the numbering criteria
	 * of the method ClassColumn.
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param name              of the specific instance to which the properties
	 *                          relations are wanted to be assigned
	 * @param column_number     the number of the column of the table considered.
	 * @param propertyIRI       the IRI identification of the property inside the
	 *                          ontology. This must be checked for the considered
	 *                          property in the working ontology.
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells of the column is expected to have multiple
	 *                          elements separated by ', '. If expected_multiple is
	 *                          true, the method will check for each cell if the
	 *                          separator ', ' exists and proceed to create an
	 *                          instance for each one of the elements of the cell
	 *                          individually.
	 */
	public static void ObjectPropertiesObjecttoCell(OWLOntology o, List<List<String>> data_base, String name,
			int column_number, String propertyIRI, boolean expected_multiple) {
		boolean multiple_field;
		String instance_name;// This is the name that will be given to the numbered individuals of the first
								// group
		int jlimit;// This variable will count the number of elements in each cell if required

		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLObjectProperty object_property = df.getOWLObjectProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied
		OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(main_iri + name.replace(" ", "_")));
		// Creating the individual with the IRI. This is the origin individual, which
		// name is provided.
		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column

			if (data_base.get(i).get(column_number).toString().contains(",") && expected_multiple) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}

			//
			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();
				// Separating the different elements of the cell if required
			} else {
				jlimit = 1;// By default there is a single element to take in the cell
			}
			for (int j = 0; j < jlimit; j++) {
				if (multiple_field) {

					instance_name = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_");
					// Forming the name of the instances
				} else {

					instance_name = data_base.get(i).get(column_number).toString().replace(" ", "_");
					// Forming the name of the instances
				}

				OWLIndividual IndB = df.getOWLNamedIndividual(IRI.create(main_iri + instance_name.replace(" ", "_")));
				// Creating the individual with the IRI. This is the origin individual, which
				// name is provided.
				OWLAxiom axiom_property = df.getOWLObjectPropertyAssertionAxiom(object_property, IndA, IndB);
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology

			}
		}

		try {
			m.saveOntology(o, documentIRI);
			// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method allows to establish ObjectProperty relations between instances of
	 * a certain type that are numbered (origin) and instances that take their
	 * denomination directly from the cells of the data base table (target). The
	 * property relation is built between the instances matching the same row of the
	 * data table. The indexing criteria for the numbered instances is equivalent to
	 * that used by the method InstanceColumn.
	 * 
	 * @param o                 OWLOntology (OWL API) object containing the target
	 *                          ontology
	 * @param data_base         containing the information in the shape of a table
	 *                          that has been created from a file
	 * @param name              generic name of the type of individual, origin of
	 *                          the ObjectProperty relation.Individuals are numbered
	 *                          according to the reference index number in the first
	 *                          column of the table.
	 * @param ref_column        the reference column for the type of individual, so
	 *                          as only one instance will be considered for cells in
	 *                          the column having exactly the same content.
	 * @param column_number     the number of the column of the table considered. If
	 *                          the names of the instances do not correspond with
	 *                          the content of the cells (column_name=false), then
	 *                          the content of the column still works as a
	 *                          reference, so as for cells having exactly the same
	 *                          content, only one instance will be created.
	 * @param propertyIRI       the IRI identification of the property inside the
	 *                          ontology. This must be checked for the considered
	 *                          property in the working ontology.
	 * @param expected_multiple is a boolean to state if the content of the target
	 *                          cells is expected to have multiple elements
	 *                          separated by ', '. If expected_multiple is true, the
	 *                          method will check for each cell if the separator ',
	 *                          ' exists and proceed to create an instance for each
	 *                          one of the elements of the cell individually.
	 */
	public static void DataPropertiesNumberedtoCell(OWLOntology o, List<List<String>> data_base, String name,
			int ref_column, int column_number, String propertyIRI, boolean expected_multiple) {

		boolean multiple_field;
		String property_value;
		int jlimit; // This variable will count the number of elements in each cell if required
		String content_index; // It is the reference index that will be assigned and added to the individuals
								// and it depends on the
		// index specified in the first column and on the cell content that are repeated
		boolean repeated_content = false;
		ArrayList<String> content = new ArrayList<>();// This variable will store the content of the cells
		// when the content has not been found before in the column.
		ArrayList<String> content_references = new ArrayList<String>(); // This variable store the reference index of a
																		// cell content that has not been found before
																		// in the column.

		OWLDataFactory df = OWLManager.getOWLDataFactory();
		m.getIRIMappers().add(new AutoIRIMapper(ont_file, true));
		OWLDataProperty data_property = df.getOWLDataProperty(IRI.create(propertyIRI));
		// Gets the ontology property to be applied

		for (int i = 1; i < data_base.size(); i++) {// Checking all the cells in the column
			if (content.size() > 0 && content.contains(data_base.get(i).get(ref_column))) {// Checking if the content of
																							// the cell
				// has appeared before in the column
				content_index = content_references.get(content.lastIndexOf(data_base.get(i).get(ref_column)));
				repeated_content = true;
				// The individual will take the reference index where that same cell content
				// appeared at first
			} else {
				content_index = data_base.get(i).get(0);
				content.add(data_base.get(i).get(ref_column));
				content_references.add(content_index);
				// New content is added with the corresponding reference index

			}
			OWLIndividual IndA;// This will be the numbered individual, origin of the property
			if (repeated_content) {
				IndA = df.getOWLNamedIndividual(IRI.create(main_iri + name.replace(" ", "_") + (content_index)));
				// Creating the individual with the IRI
			} else {
				IndA = df.getOWLNamedIndividual(
						IRI.create(main_iri + name.replace(" ", "_") + data_base.get(i).get(0).toString()));
				// Creating the individual with the IRI
			}
			if (data_base.get(i).get(column_number).toString().contains(",") & expected_multiple) {
				multiple_field = true;
				// Multiple elements will be taken from the cell if they are expected and ', '
				// separators exist
			} else {
				multiple_field = false;

			}

			//
			if (multiple_field) {
				jlimit = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).size();
				// Separating the different elements of the cell if required
			} else {
				jlimit = 1;// By default there is a single element to take in the cell
			}
			for (int j = 0; j < jlimit; j++) {// Checking all the elements in the cell
				if (multiple_field) {

					property_value = GeneralMethods.separate(data_base.get(i).get(column_number).toString()).get(j)
							.toString().replace(" ", "_");
					// Forming the name of the instances
				} else {

					property_value = data_base.get(i).get(column_number).toString().replace(" ", "_");
					// Forming the name of the instances
				}

				// Creating the individual with the IRI. This will be the second individual, the
				// one which name is taken
				// from the cell and it is the target of the property
				OWLAxiom axiom_property = df.getOWLDataPropertyAssertionAxiom(data_property, IndA,
						new OWLLiteralImplString(property_value));
				AddAxiom addAxiom_property = new AddAxiom(o, axiom_property);
				m.applyChange(addAxiom_property);
				// Applies individual generation axiom to the ontology

			}
		}

		try {
			m.saveOntology(o, documentIRI);// Saving ontology in the given file which has been assigned and IRI
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
