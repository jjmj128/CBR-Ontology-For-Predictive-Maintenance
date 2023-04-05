package User;

import java.io.File;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

/**
 * This class works as a configuration file for all the classes in the project,
 * so as its attributes will be taken and used as they are specified here.
 * 
 * @author Juan
 *
 */
public class AppConfiguration {

	public static String data_path = "C:\\Users\\Montero\\Desktop\\Repositorio\\CBR-Ontologia Branch 2\\CBR_Ontology\\InternshipProject\\data\\";
	// Local folder path where the files used by the code are stored (case base in
	// .csv, ontology case base, myCBR porject file, etc).
	public static String projectName = "PredictMaint_myCBR.prj";
	// Name of the project .prj file to be used for myCBR retrieval. This file is
	// the one read by myCBR
	public static String conceptName = "Case";
	// Name of the main concept inside the myCBR project. The concept is the
	// definition for the cases considered for the queries and retrieval
	public static String csv = "CleanedDATA V12-05-2021.csv"; // Name of the .csv file containing the case base
	public static String DataBasefromOntology = "DataBasefromOntology.csv";
	public static String columnseparator = ";";// Separation between columns specified for the .csv file that will be
												// imported by myCBR to fill up the project case base.
	public static String multiplevalueseparator = ";";// Separation between multiple values inside each cell specified
														// for the .csv file that will be imported by myCBR to fill up
														// the project case base.
	public static String casebase = "MaintCB";// Name of the case base inside the myCBR .prj file which contains all the
												// case instances with their attributes values assigned.

	public static String base_ont_file_name = "OPMAD.owl";
	// Name of the ontology .owl file that will be copied to be filled up with the
	// desired entities assertions. This file will remain unchanged
	// after the execution of DatatoOntology. The ontology files must be saved in
	// RDF/XML syntax (choose this option when using Save ass in Protégé).
	public static String ont_file_name = "OPMADdatabase.owl";
	// Name of the ontology .owl file where the ontology with all the new entities
	// added will be stored. This file can be
	// empty at the beginning. After the execution of DatatoOntology,the file will
	// contain all the information of the file referenced as base_ont_file_name
	// and all the entities that are wanted to be added. The ontology files must be
	// saved in RDF/XML syntax (choose this option when using Save ass in Protégé).
	public static File ont_file = new File(data_path + ont_file_name);// File object created from the specified folder
																		// path and name.
	public static File base_ont_file = new File(data_path + base_ont_file_name);// File object created from the
																				// specified folder path and name.
	public static IRI main_iri = IRI.create("http://www.semanticweb.org/j.montero-jimenez/ontologies/2021/2/OPMAD#");
	// IRI identifier of the ontology used as data base.
	public static IRI common_iri = IRI.create("http://www.ontologyrepository.com/CommonCoreOntologies/");
	// IRI of the Common Core Ontologies (CCO), where some of the basic properties
	// and classes are defined.
	public static IRI obo_iri = IRI.create("http://purl.obolibrary.org/obo/");
	// IRI of the obolibrary, where some of the basic properties and classes are
	// defined.

	public static IRI documentIRI = IRI.create(ont_file);// IRI identifier for the file where the ontology will be
															// stored.

	//// All the attributes below are necessary for the definition of the ontology
	//// mapping when working with local imports.
	// The working ontology may invoke some of the basic ontologies (BFO, CCO, etc)
	//// through their corresponding IRI. Those imported ontologies
	// could also have other dependencies referenced by their IRI. So, if local
	//// imports are used (dependencies ontologies contained in .ttl files)
	// the OWL API needs to match each referenced import IRI with the file where the
	//// ontology is contained (mapper).
	// One file (ontology) could have been invoked by more than one slightly
	//// different IRI.
	// The definition is as follows: an IRI object is created, a file object is
	//// created using the dependency .ttl file name and the folder path,
	// and finally the mapper is built by linking both of them.
	public static IRI artifact_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/ArtifactOntology");
	public static File artifact_file = new File(data_path + "ArtifactOntology.ttl");
	public static IRI artifact_file_iri = IRI.create(artifact_file);
	public static SimpleIRIMapper artifact_mapper = new SimpleIRIMapper(artifact_iri, artifact_file_iri);

	public static IRI ICE_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/InformationEntityOntology");
	public static File ICE_file = new File(data_path + "InformationEntityOntology.ttl");
	public static IRI ICE_file_iri = IRI.create(ICE_file);
	public static SimpleIRIMapper ICE_mapper = new SimpleIRIMapper(ICE_iri, ICE_file_iri);

	public static IRI ICEM_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/InformationEntityOntology");
	public static File ICEM_file = new File(data_path + "InformationEntityOntology.ttl");
	public static IRI ICEM_file_iri = IRI.create(ICEM_file);
	public static SimpleIRIMapper ICEM_mapper = new SimpleIRIMapper(ICEM_iri, ICEM_file_iri);

	public static IRI event_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/EventOntology");
	public static File event_file = new File(data_path + "EventOntology.ttl");
	public static IRI event_file_iri = IRI.create(event_file);
	public static SimpleIRIMapper event_mapper = new SimpleIRIMapper(event_iri, event_file_iri);

	public static IRI geo_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/GeospatialOntology");
	public static File geo_file = new File(data_path + "GeospatialOntology.ttl");
	public static IRI geo_file_iri = IRI.create(geo_file);
	public static SimpleIRIMapper geo_mapper = new SimpleIRIMapper(geo_iri, geo_file_iri);

	public static IRI relation_m_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/ExtendedRelationOntology");
	public static File relation_m_file = new File(data_path + "ExtendedRelationOntology.ttl");
	public static IRI relation_m_file_iri = IRI.create(relation_m_file);
	public static SimpleIRIMapper relation_m_mapper = new SimpleIRIMapper(relation_m_iri, relation_m_file_iri);

	public static IRI relation_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/ExtendedRelationOntology");
	public static File relation_file = new File(data_path + "ExtendedRelationOntology.ttl");
	public static IRI relation_file_iri = IRI.create(relation_file);
	public static SimpleIRIMapper relation_mapper = new SimpleIRIMapper(relation_iri, relation_file_iri);

	public static IRI ro_iri = IRI.create("http://www.ontologyrepository.com/CommonCoreOntologies/Upper/ROImport");
	public static File ro_file = new File(data_path + "ro-import.ttl");
	public static IRI ro_file_iri = IRI.create(ro_file);
	public static SimpleIRIMapper ro_mapper = new SimpleIRIMapper(ro_iri, ro_file_iri);

	public static IRI time_iri = IRI.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/TimeOntology");
	public static File time_file = new File(data_path + "TimeOntology.ttl");
	public static IRI time_file_iri = IRI.create(time_file);
	public static SimpleIRIMapper time_mapper = new SimpleIRIMapper(time_iri, time_file_iri);

}
