package User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.semanticweb.owlapi.model.IRI;

public class OntologytoCSVExec {

	public static String outputname = AppConfiguration.DataBasefromOntology;
	public static String data_path = AppConfiguration.data_path;
	public static IRI main_iri = AppConfiguration.main_iri;

	public static void main(String[] args) {

		FileManager.get().addLocatorClassLoader(SPARQL.class.getClassLoader());
		Model model = RDFDataMgr.loadModel(AppConfiguration.data_path + AppConfiguration.ont_file_name);
		String queryString =

				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
						+ "PREFIX def: <http://www.semanticweb.org/j.montero-jimenez/ontologies/2021/2/OPMAD#>"
						+ "PREFIX obo: <http://purl.obolibrary.org/obo/>"
						+ "PREFIX cco: <http://www.ontologyrepository.com/CommonCoreOntologies/>"
						+ "SELECT ?f ?g ?i ?k ?l ?m ?o ?p ?c ?q ?g ?i ?t ?u " + "WHERE {" +

						"?a rdf:type def:Predictive_maintenance_system_module ." + "?a  obo:RO_0010002 ?d . "
						+ "?b  obo:RO_0010002 ?d . " + "?b rdf:type def:Predictive_Maintenance_Article ."
						+ "?c rdfs:subClassOf  def:Predictive_maintenance_model ." + "?d rdf:type ?c ."
						+ "?e rdf:type def:Predictive_maintenance_case ." + "?e cco:designates ?a ."
						+ "?e def:has_text_value ?f ." + "?b def:has_publication_year ?g ."
						+ "?i rdfs:subClassOf def:Predictive_maintenance_module_function ." + "?h rdf:type ?i ."
						+ "?a def:has_predictive_maintenance_function ?h ." + "?a obo:BFO_0000051 ?j ."
						+ "?j rdf:type ?k ." + "?k rdfs:subClassOf def:Maintainable_item ."
						+ "?l rdf:type def:item_type ." + "?b obo:RO_0010002 ?l ."
						+ "?m rdfs:subClassOf def:maintainable_item_record ." + "?n rdf:type ?m ."
						+ "?a obo:BFO_0000051 ?n ." + "?o rdf:type def:Data_variable ." + "?n obo:RO_0010002 ?o ."
						+ "?p rdf:type def:Model_type ." + "?p cco:describes ?d ." + "?q cco:describes ?a ."
						+ "?q rdf:type def:Module_synchronization ." + "?b def:has_title ?r ."
						+ "?r def:has_text_value ?t ." + "?b def:has_identifier ?s ." + "?s def:has_text_value ?u ."
						+ "}ORDER BY (?f)";

		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		ResultSet results = qexec.execSelect();
		ResultSetRewindable results_table = results.rewindable();
		RDFNode name;

		// It must match the order of the variables in the SELECT command of the SPARQL
		// query.
		List<String> variables = results.getResultVars();
		System.out.println(variables.toString());
		// The column of the table that will be take as a reference. In this case, the
		// first column contains the reference indices.
		int reference_column_number = 0;
		// A list of lists that will contain the results of the SPARQL query. It is a
		// list of column list.

		List<List<String>> SPARQLtable_models = new ArrayList<>();

		for (int i = 0; i < variables.size(); i++) {
			// Each one of the list is a column of the table
			List<String> column = new ArrayList<>();

			for (int j = 0; j < results_table.size(); j++) {

				QuerySolution sol = results_table.nextSolution();

				column.add(sol.get(variables.get(i)).toString());

			}
			SPARQLtable_models.add(column);
			results_table.reset();
		}

		// System.out.println(hola.toString().replace("[", "").replace("]",
		// "").replace("*, ", ""));

		List<List<List<String>>> table = new ArrayList<>();

		for (int i = 0; i < variables.size(); i++) {
			List<List<String>> column = new ArrayList<>();

			for (int j = 0; j < Collections
					.max(GeneralMethods.StringListoIntList(SPARQLtable_models.get(reference_column_number))); j++) {

				List<String> cell = new ArrayList<>();
				cell.add("*");
				column.add(cell);
			}
			table.add(column);
		}

		for (int i = 0; i < variables.size(); i++) {

			for (int j = 0; j < results_table.size(); j++) {
				int row_index = Integer.parseInt(SPARQLtable_models.get(reference_column_number).get(j)) - 1;
				if (variables.get(i) != "p") {
					if (SPARQLtable_models.get(i).get(j).contains(main_iri.toString()) && (table.get(i).get(row_index)
							.contains(SPARQLtable_models.get(i).get(j).substring(main_iri.toString().length()))
							|| table.get(i).get(row_index).contains(SPARQLtable_models.get(i).get(j)
									.substring(main_iri.toString().length()).replace("_", " ")))) {

					} else if (SPARQLtable_models.get(i).get(j).contains(main_iri.toString()) == false
							&& (table.get(i).get(row_index).contains(SPARQLtable_models.get(i).get(j)) || table.get(i)
									.get(row_index).contains(SPARQLtable_models.get(i).get(j).replace("_", " ")))) {

					} else {
						if (SPARQLtable_models.get(i).get(j).contains(main_iri.toString())) {

							table.get(i).get(row_index).add(SPARQLtable_models.get(i).get(j)
									.substring(main_iri.toString().length()).replace("_", " "));
						} else {
							if (i == variables.indexOf("u")) {
								table.get(i).get(row_index).add(SPARQLtable_models.get(i).get(j));
							} else {
								table.get(i).get(row_index).add(SPARQLtable_models.get(i).get(j).replace("_", " "));
								if (variables.get(i) == "c") {
									int variable_index = variables.indexOf("p");
									table.get(variable_index).get(row_index).add(SPARQLtable_models.get(variable_index)
											.get(j).substring(main_iri.toString().length()).replace("_", " "));

								}
							}

						}

					}

				}
				if (table.get(i).get(row_index).contains("*")) {
					table.get(i).get(row_index).remove("*");

				}
			}
		}

		List<List<String>> table_to_write = new ArrayList<>();

		for (int i = 0; i < variables.size(); i++) {
			List<String> column = new ArrayList<>();
			for (int j = 0; j < table.get(reference_column_number).size(); j++) {
				column.add(table.get(i).get(j).toString().replace("[", "").replace("]", ""));

			}
			table_to_write.add(column);

		}

		String[] head_out = { "Reference", "Publication year", "Task", "Case study", "Case study type",
				"Input for the model", "Input type", "Model Type", "Models", "Online/Off-line", "Study title",
				"Publication identifier" };

		try {
			PrintWriter pw = new PrintWriter(new File(data_path + outputname));

			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < head_out.length; i++) {
				if (i == head_out.length - 1) {
					sb.append(head_out[i]);
					sb.append("\r\n");
				} else {
					sb.append(head_out[i]);
					sb.append(";");
				}
			}

			for (int j = 0; j < table_to_write.get(reference_column_number).size(); j++) {

				for (int i = 0; i < head_out.length; i++) {

					String replace = table_to_write.get(i).get(j).toString().replace(";", ",");

					sb.append(replace);
					if (i == head_out.length - 1) {
						sb.append("\r\n");
					} else {
						sb.append(";");
					}
				}
			}
			System.out.println(sb);
			pw.write(sb.toString());
			pw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
