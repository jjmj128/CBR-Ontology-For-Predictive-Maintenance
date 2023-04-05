package User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.manchestersyntax.renderer.ManchesterOWLSyntaxPrefixNameShortFormProvider;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import OntologyTools.DLQueryEngineIRI;

public class Prueba {
	private static String ont_file_name = AppConfiguration.base_ont_file_name;

	private static String data_path = AppConfiguration.data_path;
	private static File ont_file = new File(data_path + ont_file_name);
	private static OWLOntologyManager m = OWLManager.createOWLOntologyManager();

	private static IRI main_iri = AppConfiguration.main_iri;
	private static IRI common_iri = AppConfiguration.common_iri;

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

	private static Reasoner createReasoner(final OWLOntology rootOntology) {
		// We need to create an instance of OWLReasoner. An OWLReasoner provides
		// the basic query functionality that we need, for example the ability
		// obtain the subclasses of a class etc. To do this we use a reasoner
		// factory.
		// Create a reasoner factory.
		Configuration config = new Configuration();
		Reasoner hermit = new Reasoner(config, rootOntology);
		// The HermiT reasoner is used

		return hermit;
	}

	public static void main(String[] args) {

		Prueba.setmappers();

		OWLOntology o;
		try {
			o = m.loadOntologyFromOntologyDocument(ont_file);

			System.out.println("Loaded ontology: " + o.getOntologyID());
			m.getOntologyFormat(o).asPrefixOWLDocumentFormat().setDefaultPrefix(main_iri + "#");

			// We need a reasoner to do our query answering. Here the HermiT reasoner is
			// used.

			Reasoner reasoner = createReasoner(o);

			ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
			DLQueryEngineIRI dlQueryEngine = new DLQueryEngineIRI(reasoner);

			OWLDataFactory dataFactory = o.getOWLOntologyManager().getOWLDataFactory();
			// Set up the real parser
			ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(dataFactory, "BFO_0000002");
			parser.setDefaultOntology(o);
			// Specify an entity checker that will be used to check a class
			// expression contains the correct names.

			OWLOntologyManager manager = o.getOWLOntologyManager();
			Set<OWLOntology> importsClosure = o.getImportsClosure();
			// Create a bidirectional short form provider to do the actual mapping.
			// It will generate names using the input
			// short form provider.
			BidirectionalShortFormProvider bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager,
					importsClosure, shortFormProvider);

			OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
			parser.setOWLEntityChecker(entityChecker);
			// Do the actual parsing
			// System.out.println(parser.parseClassExpression());

			ArrayList<OWLNamedIndividual> classIndividuals = new ArrayList<>();
			classIndividuals = GeneralMethods
					.SetToArray(dlQueryEngine.getInstances(common_iri + "GreenwichMeanTimeZoneIdentifier", false));
			for (OWLNamedIndividual ind : classIndividuals) {
				// System.out.println(dlQueryEngine.getClass(ind.toStringID().substring(common_iri.toString().length()),
				// false).iterator().next().toStringID().substring(common_iri.toString().length()));
				// System.out.println(ind.toStringID().substring(common_iri.toString().length()));

				ArrayList<OWLClass> Classes = new ArrayList<>();

				ManchesterOWLSyntaxPrefixNameShortFormProvider namer = new ManchesterOWLSyntaxPrefixNameShortFormProvider(
						o);

				// System.out.println(namer.getShortForm(ind.getIRI()));

				Classes = GeneralMethods.SetToArray(dlQueryEngine.getClass(ind.toStringID(), true));
				for (OWLClass c : Classes) {

					System.out.println(c);

				}

			}

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<String> hola = new ArrayList<>();
		hola.add("*");
		hola.add("h");
		hola.add("o");
		hola.add("l");
		hola.add("a");
		System.out.println(hola.toString().replace("[", "").replace("]", "").replace("*, ", ""));

		List<List<List<String>>> table = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			List<List<String>> column = new ArrayList<>();

			for (int j = 0; j < 10; j++) {

				List<String> cell = new ArrayList<>();
				cell.add("*");
				column.add(cell);
			}
			table.add(column);
		}

	}

}
