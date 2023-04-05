package User;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;

/**
 * This class allows to perform SPARQL queries on an ontology using Jena API
 * 
 * @author Hugo
 *
 */
public class SPARQL {
	/**
	 * Executable method of the class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		FileManager.get().addLocatorClassLoader(SPARQL.class.getClassLoader());
		Model model = RDFDataMgr.loadModel(AppConfiguration.data_path + AppConfiguration.ont_file_name);
		String queryString =

				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
						+ "PREFIX def: <http://www.semanticweb.org/j.montero-jimenez/ontologies/2021/2/OPMAD#>"
						+ "PREFIX obo: <http://purl.obolibrary.org/obo/>" + "SELECT ?x ?y ?z ?a ?b ?c ?d ?e ?f ?g"
						+ "WHERE {" +

						"?x rdf:type def:Predictive_maintenance_system_module ."
						+ "?y rdf:type def:Predictive_Maintenance_Article ." + "?y def:explains ?x ."
						+ "?y def:has_publication_year ?z ." + "?x def:has_predictive_maintenance_function ?a ."
						+ "?b rdfs:subClassOf def:Predictive_maintenance_module_function ." + "?a rdf:type ?b ."
						+ "?c rdf:type def:item_type ." + "?y obo:RO_0010002 ?c ." + "?x obo:BFO_0000051 ?d ."
						+ "?e rdfs:subClassOf  def:maintainable_item_record ." + "?d rdf:type ?e ."
						+ "?d obo:RO_0010002 ?f ." +

						"}";

		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		RDFNode name;
		// Resource name;

		while (results.hasNext()) {

			QuerySolution sol = results.nextSolution();

			// name = sol.getResource("y");
			name = sol.get("f");
			System.out.println(name.toString());

		}

	}
}
