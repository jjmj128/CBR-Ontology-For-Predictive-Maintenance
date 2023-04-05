package User;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;
import org.swrlapi.sqwrl.exceptions.SQWRLException;

public class SWRLAPIexec {

	private static OWLOntologyManager m = OWLManager.createOWLOntologyManager();

	private static String ont_file_name = "OPMAD.owl";
	private static String data_file_name = "CleanedDATA V10-02-2021.csv";
	private static String data_path = "C:\\Users\\Hugo\\Desktop\\Universidad\\SUPAERO\\S4\\CBR_Ontology\\InternshipProject\\data_and_mycbr\\";
	private static File ont_file = new File(data_path + ont_file_name);
	private static IRI main_iri = IRI.create("http://www.semanticweb.org/j.montero-jimenez/ontologies/2021/2/OPMAD");
	private static IRI documentIRI = IRI.create(ont_file);
	private static IRI artifact_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/ArtifactOntology");
	private static File artifact_file = new File(data_path + "ArtifactOntology.ttl");
	private static IRI artifact_file_iri = IRI.create(artifact_file);
	private static SimpleIRIMapper artifact_mapper = new SimpleIRIMapper(artifact_iri, artifact_file_iri);

	private static IRI ICE_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/InformationEntityOntology");
	private static File ICE_file = new File(data_path + "InformationEntityOntology.ttl");
	private static IRI ICE_file_iri = IRI.create(ICE_file);
	private static SimpleIRIMapper ICE_mapper = new SimpleIRIMapper(ICE_iri, ICE_file_iri);

	private static IRI ICEM_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/InformationEntityOntology");
	private static File ICEM_file = new File(data_path + "InformationEntityOntology.ttl");
	private static IRI ICEM_file_iri = IRI.create(ICEM_file);
	private static SimpleIRIMapper ICEM_mapper = new SimpleIRIMapper(ICEM_iri, ICEM_file_iri);

	private static IRI event_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/EventOntology");
	private static File event_file = new File(data_path + "EventOntology.ttl");
	private static IRI event_file_iri = IRI.create(event_file);
	private static SimpleIRIMapper event_mapper = new SimpleIRIMapper(event_iri, event_file_iri);

	private static IRI geo_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/GeospatialOntology");
	private static File geo_file = new File(data_path + "GeospatialOntology.ttl");
	private static IRI geo_file_iri = IRI.create(geo_file);
	private static SimpleIRIMapper geo_mapper = new SimpleIRIMapper(geo_iri, geo_file_iri);

	private static IRI relation_m_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/ExtendedRelationOntology");
	private static File relation_m_file = new File(data_path + "ExtendedRelationOntology.ttl");
	private static IRI relation_m_file_iri = IRI.create(relation_m_file);
	private static SimpleIRIMapper relation_m_mapper = new SimpleIRIMapper(relation_m_iri, relation_m_file_iri);

	private static IRI relation_iri = IRI
			.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/2021-03-01/ExtendedRelationOntology");
	private static File relation_file = new File(data_path + "ExtendedRelationOntology.ttl");
	private static IRI relation_file_iri = IRI.create(relation_file);
	private static SimpleIRIMapper relation_mapper = new SimpleIRIMapper(relation_iri, relation_file_iri);

	private static IRI ro_iri = IRI.create("http://www.ontologyrepository.com/CommonCoreOntologies/Upper/ROImport");
	private static File ro_file = new File(data_path + "ro-import.ttl");
	private static IRI ro_file_iri = IRI.create(ro_file);
	private static SimpleIRIMapper ro_mapper = new SimpleIRIMapper(ro_iri, ro_file_iri);

	private static IRI time_iri = IRI.create("http://www.ontologyrepository.com/CommonCoreOntologies/Mid/TimeOntology");
	private static File time_file = new File(data_path + "TimeOntology.ttl");
	private static IRI time_file_iri = IRI.create(time_file);
	private static SimpleIRIMapper time_mapper = new SimpleIRIMapper(time_iri, time_file_iri);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		////////////////////////////////////
		System.err.close(); // Hides errors and warnings, if errors are wanted to be monitored, these lines
							// must be commented.
		System.setErr(System.out);
		////////////////////////////////////////////

		m.getIRIMappers().add(artifact_mapper);
		m.getIRIMappers().add(time_mapper);
		m.getIRIMappers().add(ro_mapper);
		m.getIRIMappers().add(relation_mapper);
		m.getIRIMappers().add(relation_m_mapper);
		m.getIRIMappers().add(geo_mapper);
		m.getIRIMappers().add(event_mapper);
		m.getIRIMappers().add(ICE_mapper);
		m.getIRIMappers().add(ICEM_mapper);

		try {
			// Create an OWL ontology using the OWLAPI

			File file = new File(
					"C:\\Users\\Hugo\\Desktop\\Universidad\\SUPAERO\\S4\\CBR_Ontology\\InternshipProject\\data_and_mycbr\\OPMAD.owl");
			OWLOntology o = m.loadOntologyFromOntologyDocument(file);
			// IRI example_iri =
			// IRI.create("http://www.semanticweb.org/hugo/ontologies/2021/2/ontology");
			// OWLDataFactory df = OWLManager.getOWLDataFactory();

			// map the ontology IRI to a physical IRI (files for example)

			// IRI documentIRI = IRI.create(file);
			// PrefixManager pm = new DefaultPrefixManager();
			// System.out.println( pm.getDefaultPrefix());
			// pm.setDefaultPrefix(example_iri.toString());

			// Set up a mapping, which maps the ontology to the document IRI
			// SimpleIRIMapper mapper = new SimpleIRIMapper(example_iri, documentIRI);
			// System.out.println(documentIRI);
			// System.out.println(example_iri);
			// m.getIRIMappers().add(mapper);
			// set up a mapper to read local copies of ontologies

			// Now create the ontology using the ontology IRI (not the
			// physical URI)
			// OWLOntology o = m.createOntology(example_iri);
			// save the ontology to its physical location - documentIRI
			// m.getIRIMappers().add(new AutoIRIMapper(file, true));
			// OWLClass clsA = df.getOWLClass(IRI.create(example_iri + "#car"));
			// OWLClass clsA = df.getOWLClass(IRI.create(example_iri + "#coche"));
			// OWLIndividual IndA = df.getOWLNamedIndividual("coche", pm);
			// OWLIndividual IndA = df.getOWLNamedIndividual(IRI.create(example_iri +
			// "#coche"));
			// OWLClass clsA = df.getOWLClass(IRI.create(documentIRI + "#living_being"));

			// OWLDeclarationAxiom da = df.getOWLDeclarationAxiom(clsA);

			// OWLAxiom axiom = df.getOWLClassAssertionAxiom(clsA, IndA);

			// Now create the axiom
			// OWLAxiom axiom = df.getOWLSubClassOfAxiom(clsA, clsB);
			// OWLAxiom axiom = df.getOWLSubClassOfAxiom(clsA, clsB);
			// OWLAxiom axiom = df.getOWLClassAssertionAxiom(clsA);
			// add the axiom to the ontology.
			// AddAxiom addAxiom = new AddAxiom(o, axiom);

			// We now use the manager to apply the change
			// m.applyChange(addAxiom);

			// try {
			// m.saveOntology(o, documentIRI);
			// } catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(o);
			int number_of_cases = 1;

			SQWRLResult result;
			try {

				result = queryEngine.runSQWRLQuery("q", "tbox:sca(Fault_detection, ?c) -> sqwrl:select(?c)");
				Pattern p = Pattern.compile("\\d+");
				Matcher match = p.matcher(result.toString());
				// System.out.println(result.toString());
				match.find();
				number_of_cases = Integer.parseInt(match.group());
				System.out.println(result);
				// System.out.println(number_of_cases);
			} catch (SQWRLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SWRLParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Create SQWRL query engine using the SWRLAPI
			// SQWRLQueryEngine queryEngine =
			// SWRLAPIFactory.createSQWRLQueryEngine(ontology);
			// SWRLAPIOWLOntology swrlont= SWRLAPIFactory.createSWRLAPIOntology(ontology);

			m.getOntologyFormat(o).asPrefixOWLDocumentFormat().setDefaultPrefix(main_iri + "#");

			// Create and execute a SQWRL query using the SWRLAPI

			// SQWRLResult result = queryEngine.runSQWRLQuery("q1",
			// "abox:caa(CommonCoreOntologies:Artifact, ?i) -> sqwrl:select(?i)");

			// Process the SQWRL result

		} catch (RuntimeException e) {
			System.err.println("Error starting application: " + e.getMessage());
			System.exit(-1);
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static void Usage() {
		System.err.println("Usage: " + SWRLAPIexec.class.getName() + " [ <owlFileName> ]");
		System.exit(1);
	}
}