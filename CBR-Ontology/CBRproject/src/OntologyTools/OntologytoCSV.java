package OntologyTools;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import User.AppConfiguration;

/**
 * This class contains some methods to translate information that is part of an
 * ontology (.owl) in a structured table format (.csv). The user would choose
 * which methods to use in an executable code depending on how the data is
 * desired to be organized. The ontology files must be saved in RDF/XML syntax
 * (choose this option when using Save ass in Protégé).
 * 
 * @author Hugo
 *
 */
public class OntologytoCSV {

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

}
