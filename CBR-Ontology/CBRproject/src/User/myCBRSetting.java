/*
 * This file is part of the OWL API.

 *
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, The University of Manchester
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//This class uses content from the original file that can be found at https://github.com/phillord/owl-api/blob/master/contract/src/test/java/org/coode/owlapi/examples/DLQueryExample.java with the license above.

/** Author: Matthew Horridge<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 13-May-2010 <br>
 * An example that shows how to do a Protege like DLQuery. The example contains
 * several helper classes:<br>
 * DLQueryEngineIRI - This takes a string representing a class expression built
 * from the terms in the signature of some ontology. DLQueryPrinter - This takes
 * a string class expression and prints out the sub/super/equivalent classes and
 * the instances of the specified class expression. DLQueryParser - this parses
 * the specified class expression string */

package User;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import CBR.CBREngine;
import OntologyTools.DLQueryEngineIRI;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.IntegerDesc;
import de.dfki.mycbr.core.model.StringDesc;
import de.dfki.mycbr.core.model.SymbolDesc;
import de.dfki.mycbr.core.similarity.AdvancedIntegerFct;
import de.dfki.mycbr.core.similarity.Similarity;
import de.dfki.mycbr.core.similarity.StringFct;
import de.dfki.mycbr.core.similarity.SymbolFct;
import de.dfki.mycbr.core.similarity.config.StringConfig;

/**
 * This class has an executable method and other accessory methods. When
 * executed, this file will prepare the myCBR project (.prj) file for the case
 * retrieval. The case base (.csv) will be loaded in the project file and the
 * similarity values will be set for the case fields. The ontology data base
 * (.owl) can be used to perform some of the similarity values calculation. When
 * updating the case base, the file DatatoOntology must be executed at first.
 * Then, this file should be executed before performing retrieval queries. While
 * using the same case base, queries can be performed straight on and no
 * modification is required in the project file or the ontology data base.
 * 
 *
 */
public class myCBRSetting {
	private static OWLOntologyManager m = OWLManager.createOWLOntologyManager();

	// The attributes below are obtained from the AppConfiguration class.
	private static String ont_file_name = AppConfiguration.ont_file_name;

	private static String data_path = AppConfiguration.data_path;
	private static File ont_file = new File(data_path + ont_file_name);

	private static IRI main_iri = AppConfiguration.main_iri;

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

	/**
	 * The executable method of the class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		System.err.close(); // Hides errors and warnings, if errors are wanted to be monitored, these lines
							// must be commented.
		System.setErr(System.out);

		myCBRSetting.setmappers();// Setting the mappers to access to the ontology and its dependencies.

		CBREngine engine = new CBREngine();// Instances of CBREngine to modify the project file for myCBR (.prj)

		Project rec = engine.createCBRProject();// Instances of the project and loading the .csv case base

		// DefaultCaseBase cb =
		// (DefaultCaseBase)rec.getCaseBases().get(CBREngine.getCaseBase());
		Concept myConcept = rec.getConceptByID(CBREngine.getConceptName());

		/// Getting the concept attributes
		SymbolDesc taskDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Task");
		SymbolDesc caseStudyTypeDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Case study type");
		StringDesc caseStudyDesc = (StringDesc) myConcept.getAllAttributeDescs().get("Case study");
		SymbolDesc onlineOfflineDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Online/Off-line");
		SymbolDesc inputForTheModelDesc = (SymbolDesc) myConcept.getAllAttributeDescs().get("Input for the model");
		// SymbolDesc inputTypeDesc = (SymbolDesc)
		// myConcept.getAllAttributeDescs().get("Input type");
		IntegerDesc yearDesc = (IntegerDesc) myConcept.getAllAttributeDescs().get("Publication Year");

		/******************************************
		 * Add table similarity function - Case Study Type
		 ******************************************/
		SymbolFct caseStudyTypeFct = caseStudyTypeDesc.addSymbolFct("caseStudyTypeFct", true);

		caseStudyTypeFct.setSimilarity("Rotary machines", "Reciprocating machines", 0.75d);
		caseStudyTypeFct.setSimilarity("Rotary machines", "Electrical components", 0.10d);
		caseStudyTypeFct.setSimilarity("Rotary machines", "Structures", 0.65d);
		caseStudyTypeFct.setSimilarity("Rotary machines", "Energy cells and batteries", 0.10d);
		caseStudyTypeFct.setSimilarity("Rotary machines", "Production lines", 0.30d);
		caseStudyTypeFct.setSimilarity("Rotary machines", "Others", 0.20d);
		//
		caseStudyTypeFct.setSimilarity("Reciprocating machines", "Electrical components", 0.10d);
		caseStudyTypeFct.setSimilarity("Reciprocating machines", "Structures", 0.75d);
		caseStudyTypeFct.setSimilarity("Reciprocating machines", "Energy cells and batteries", 0.10d);
		caseStudyTypeFct.setSimilarity("Reciprocating machines", "Production lines", 0.30d);
		caseStudyTypeFct.setSimilarity("Reciprocating machines", "Others", 0.20d);
		//
		caseStudyTypeFct.setSimilarity("Electrical components", "Structures", 0.10d);
		caseStudyTypeFct.setSimilarity("Electrical components", "Energy cells and batteries", 0.90d);
		caseStudyTypeFct.setSimilarity("Electrical components", "Production lines", 0.30d);
		caseStudyTypeFct.setSimilarity("Electrical components", "Others", 0.20d);
		//
		caseStudyTypeFct.setSimilarity("Structures", "Energy cells and batteries", 0.10d);
		caseStudyTypeFct.setSimilarity("Structures", "Production lines", 0.30d);
		caseStudyTypeFct.setSimilarity("Structures", "Others", 0.20d);
		//
		caseStudyTypeFct.setSimilarity("Energy cells and batteries", "Production lines", 0.30d);
		caseStudyTypeFct.setSimilarity("Energy cells and batteries", "Others", 0.20d);
		//
		caseStudyTypeFct.setSimilarity("Production lines", "Others", 0.20d);

		/******************************************
		 * Add table similarity function - Case Study
		 ******************************************/
		StringFct caseStudyFct = caseStudyDesc.addStringFct(StringConfig.LEVENSHTEIN, "caseStudyFct", true);
		caseStudyFct.setCaseSensitive(false);

		/******************************************
		 * Add table similarity function - Online/Off-line
		 ******************************************/
		SymbolFct onlineOfflineFct = onlineOfflineDesc.addSymbolFct("onlineOfflineFct", true);
		onlineOfflineFct.setSimilarity("Online", "Online", 1.0d);
		onlineOfflineFct.setSimilarity("Off-line", "Off-line", 1.0d);
		onlineOfflineFct.setSimilarity("Online", "Off-line", 0.0d);
		onlineOfflineFct.setSimilarity("Both", "Off-line", 1.0d);
		onlineOfflineFct.setSimilarity("Both", "Online", 1.0d);
		onlineOfflineFct.setSimilarity("Both", "Both", 1.0d);
		onlineOfflineFct.setSimilarity("Unknown synchronization", "Unknown synchronization", 0.5d);
		onlineOfflineFct.setSimilarity("Unknown synchronization", "Both", 1.0d);
		onlineOfflineFct.setSimilarity("Unknown synchronization", "Online", 0.5d);
		onlineOfflineFct.setSimilarity("Unknown synchronization", "Off-line", 0.5d);

		/******************************************
		 * Add table similarity function - Input for the model
		 ******************************************/
		SymbolFct inputFortheModelFct = inputForTheModelDesc.addSymbolFct("inputForTheModelFct", true);

		inputFortheModelFct.setSimilarity("Signals", "Structured text-based", 0.40d);
		inputFortheModelFct.setSimilarity("Signals", "Text-based maintenance/operation logs", 0.20d);
		inputFortheModelFct.setSimilarity("Signals", "Time series", 0.70d);
		//
		inputFortheModelFct.setSimilarity("Structured text-based", "Text-based maintenance/operation logs", 0.70d);
		inputFortheModelFct.setSimilarity("Structured text-based", "Time series", 0.90d);
		//
		inputFortheModelFct.setSimilarity("Text-based maintenance/operation logs", "Time series", 0.20d);
		/******************************************
		 * Add advanced numerical similarity function - Year of study
		 ******************************************/
		// The current date of the query is compared to the publication year of each
		// case study in order to give more relevance to the recent cases.
		// The function is calculated by interpolation between the given pair of values
		// of distance (year of publication-current year) and similarity value.
		AdvancedIntegerFct yearFct = yearDesc.addAdvancedIntegerFct("yearFct", true);
		yearDesc.setMax(LocalDateTime.now().getYear());
		yearDesc.setMin(LocalDateTime.now().getYear() - 40);

		yearFct.addAdditionalPoint(20.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(10.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(5.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(3.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(2.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(1.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(0.5, Similarity.get(1.0));
		yearFct.addAdditionalPoint(0.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(-0.5, Similarity.get(1.0));
		yearFct.addAdditionalPoint(-1.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(-2.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(-3.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(-4.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(-5.0, Similarity.get(1.0));
		yearFct.addAdditionalPoint(-6.0, Similarity.get(0.96));
		yearFct.addAdditionalPoint(-7.0, Similarity.get(0.92));
		yearFct.addAdditionalPoint(-8.0, Similarity.get(0.88));
		yearFct.addAdditionalPoint(-9.0, Similarity.get(0.84));
		yearFct.addAdditionalPoint(-10.0, Similarity.get(0.8));
		yearFct.addAdditionalPoint(-11.0, Similarity.get(0.76));
		yearFct.addAdditionalPoint(-12.0, Similarity.get(0.72));
		yearFct.addAdditionalPoint(-13.0, Similarity.get(0.68));
		yearFct.addAdditionalPoint(-14.0, Similarity.get(0.64));
		yearFct.addAdditionalPoint(-15.0, Similarity.get(0.6));
		yearFct.addAdditionalPoint(-16.0, Similarity.get(0.56));
		yearFct.addAdditionalPoint(-17.0, Similarity.get(0.52));
		yearFct.addAdditionalPoint(-18.0, Similarity.get(0.48));
		yearFct.addAdditionalPoint(-19.0, Similarity.get(0.44));
		yearFct.addAdditionalPoint(-20.0, Similarity.get(0.4));
		yearFct.addAdditionalPoint(-21.0, Similarity.get(0.36));
		yearFct.addAdditionalPoint(-22.0, Similarity.get(0.32));
		yearFct.addAdditionalPoint(-23.0, Similarity.get(0.28));
		yearFct.addAdditionalPoint(24.0, Similarity.get(0.24));
		yearFct.addAdditionalPoint(-25.0, Similarity.get(0.2));
		yearFct.addAdditionalPoint(-26.0, Similarity.get(0.16));
		yearFct.addAdditionalPoint(-27.0, Similarity.get(0.12));
		yearFct.addAdditionalPoint(-28.0, Similarity.get(0.08));
		yearFct.addAdditionalPoint(-29.0, Similarity.get(0.04));
		yearFct.addAdditionalPoint(-30.0, Similarity.get(0.0));
		yearFct.addAdditionalPoint(-31.0, Similarity.get(0.0));
		yearFct.addAdditionalPoint(-32.0, Similarity.get(0.0));
		yearFct.addAdditionalPoint(-35.0, Similarity.get(0.0));
		yearFct.addAdditionalPoint(-40.0, Similarity.get(0.0));
		yearFct.addAdditionalPoint(-45.0, Similarity.get(0.0));

		OWLOntology o;
		try {

			o = m.loadOntologyFromOntologyDocument(ont_file);
			System.out.println("Loaded ontology: " + o.getOntologyID());
			m.getOntologyFormat(o).asPrefixOWLDocumentFormat().setDefaultPrefix(main_iri + "#");

			// We need a reasoner to do our query answering. Here the HermiT reasoner is
			// used.

			Reasoner reasoner = createReasoner(o);

			// Entities are named using IRIs. These are usually too long for use
			// in user interfaces. To solve this
			// problem, and so a query can be written using short class,
			// property, individual names we use a short form
			// provider. In this case, we'll just use a simple short form
			// provider that generates short forms from IRI
			// fragments.

			DLQueryEngineIRI dlQueryEngine = new DLQueryEngineIRI(reasoner);

			/******************************************
			 * Add table similarity function - Task
			 ******************************************/
			SymbolFct taskFct = taskDesc.addSymbolFct("taskFct", true);
			// For each one of the subclasses of 'Predictive maintenance function', the code
			// will search in the ontology all the instances of
			// 'Predictive maintenance model' that are linked with instances of such
			// subclass. Then, the list of possible models subclasses concerning each
			// class will be compared to get an ontological similarity value.
			// All the existing functions (tasks) will be compared in pairs one by one.

			String propertyExpression = "function_uses_model";// This is the name of the inferred ontology object
																// property that will be used to link the
			// 'Predictive maintenance function' classes with the instances of 'Predictive
			// maintenance model' that can be used to perform the task.
			// The information in the ontology will be used to calculate ontological
			// similarity values. The HermiT reasoner must be used to be able to infer
			// relations.
			String propertyIRI = main_iri + propertyExpression;// Getting the IRI identifier of the property

			String functionClassExpression = "Predictive maintenance module function";// Class for the different
																						// functions or tasks
			// that may be performed by a 'Predictive maintenance module'.
			String modelClassExpression = "Predictive maintenance model";// Class for the different models used by a
																			// 'Predictive maintenance module'
			String functionClassIRI = main_iri + functionClassExpression;
			String modelClassIRI = main_iri + modelClassExpression;
			// Getting the IRI identifiers.
			ArrayList<OWLClass> functions = new ArrayList<>();// The subclasses of 'Predictive maintenance function'
																// will be stored in this variable.

			ArrayList<OWLClass> models = new ArrayList<>(); // The subclasses of 'Predictive maintenance model' will be
															// stored in this variable.

			functions = GeneralMethods.SetToArray(dlQueryEngine.getSubClasses(functionClassIRI, false));// Getting the
																										// list of
																										// function from
																										// the ontology
																										// through the
																										// appropriate
																										// query
			// All the subclasses are taken to the bottom node (owl:Nothing), because some
			// subclasses of function can be divided in other subclasses. This is why
			// boolean parameter 'direct' is set to false.
			models = GeneralMethods.SetToArray(dlQueryEngine.getSubClasses(modelClassIRI, true));// Getting the list of
																									// function from the
																									// ontology through
																									// the appropriate
																									// query
			// Only direct subclasses are desired, that is why the boolean 'direct' is set
			// to true.
			if (functions.contains(reasoner.getBottomClassNode().entities().toArray()[0])) {
				functions.remove(reasoner.getBottomClassNode().entities().toArray()[0]);
				// Remove the bottom node owl:Nothing from the list of subclasses.
			}

			if (models.contains(reasoner.getBottomClassNode().entities().toArray()[0])) {
				models.remove(reasoner.getBottomClassNode().entities().toArray()[0]);
				// Remove the bottom node owl:Nothing from the list of subclasses.
			}
			int i_fun = 0;

			for (OWLClass fun : functions) {// Iterating through the list of functions classes
				ArrayList<String> models_1 = new ArrayList<>();// List of models concerning the first of the function
																// subclasses in the pair to be compared.

				ArrayList<OWLNamedIndividual> classIndividuals = new ArrayList<>();// List of individuals belonging to
																					// the first function subclass in
																					// the comparison pair
				classIndividuals = GeneralMethods.SetToArray(dlQueryEngine.getInstances(fun.toStringID(), true));
				// Setting the ArrayList classIndividuals to the list of individuals belonging
				// to the OWLClass in the current iteration.
				// A DL query on the ontology is used for that purpose
				for (OWLNamedIndividual ind : classIndividuals) {// Iterating through the list of individuals
					Set<OWLNamedIndividual> propertyIndividuals_Set = dlQueryEngine.getIndividualsProperty(propertyIRI,
							ind.toStringID());

					ArrayList<OWLNamedIndividual> propertyIndividuals = new ArrayList<>();// List of models individuals
																							// concerning the first
																							// function subclass in the
																							// comparison pair
					propertyIndividuals = GeneralMethods.SetToArray(propertyIndividuals_Set);
					// Setting the ArrayList propertyIndividuals to the list of model individuals
					// concerning the function subclass in the current iteration.
					// For each individual of the function subclass, all individuals of 'Predictive
					// maintenance model' that are linked to it
					// through the ontological ObjectProperty 'function_uses_model' are retrieved.

					for (OWLNamedIndividual p_ind : propertyIndividuals) {// Iterating through all the model
																			// individuals.

						// If at least one individual of one class is found, then is added to the list
						// of models for the corresponding function.
						String model_type_1 = dlQueryEngine.getClass(p_ind.toStringID(), true).iterator().next()
								.toStringID().substring(main_iri.toString().length()).replace("_", " ");

						models_1.add(model_type_1);

						// Finally, it is obtained the complete list of models classes that can be used
						// for the function (task) according to the data base (first member of the
						// comparison pair).

					}

				}

				for (int i = 0; i < functions.size(); i++) {// Iterating through the list of functions
					// Same procedure is conducted for the second function of the comparison pair
					ArrayList<String> models_2 = new ArrayList<>();// List of models concerning the second of the
																	// function subclasses in the par to be compared.
					if (i == i_fun) {
						// Each subclass do not need to be compared to itself
						models_2 = models_1;
					} else {
						ArrayList<OWLNamedIndividual> classIndividuals_2 = new ArrayList<>();// List of individuals
																								// belonging to the
																								// second function
																								// subclass in the
																								// comparison pair
						classIndividuals_2 = GeneralMethods
								.SetToArray(dlQueryEngine.getInstances(functions.get(i).toStringID(), true));

						for (OWLNamedIndividual ind_2 : classIndividuals_2) {// Iterating through the list of
																				// individuals
							Set<OWLNamedIndividual> propertyIndividuals_2_Set = dlQueryEngine
									.getIndividualsProperty(propertyIRI, ind_2.toStringID());

							ArrayList<OWLNamedIndividual> propertyIndividuals_2 = new ArrayList<>();// List of models
																									// concerning the
																									// second function
																									// subclass in the
																									// comparison pair
							propertyIndividuals_2 = GeneralMethods.SetToArray(propertyIndividuals_2_Set);
							// Setting the ArrayList propertyIndividuals to the list of model individuals
							// concerning the function subclass in the current iteration.
							// For each individual of the function subclass, all individuals of 'Predictive
							// maintenance model' that are linked to it
							// through the ontological ObjectProperty 'function_uses_model' are retrieved.

							for (OWLNamedIndividual p_ind_2 : propertyIndividuals_2) {// Iterating through all the model
																						// individuals.

								// If at least one individual of one class is found, then is added to the list
								// of models for the corresponding function.
								String model_type_2 = dlQueryEngine.getClass(p_ind_2.toStringID(), true).iterator()
										.next().toStringID().substring(main_iri.toString().length()).replace("_", " ");

								models_2.add(model_type_2);

								// Finally, it is obtained the complete list of models classes that can be used
								// for the function (task) according to the data base (second member of the
								// comparison pair).

							}

						}

					}

					models_1 = GeneralMethods.removeDuplicates(models_1);
					models_2 = GeneralMethods.removeDuplicates(models_2);
					System.out.println(fun.toStringID().substring(main_iri.toString().length()).replace("_", " "));
					for (String m : models_1) {

						System.out.println(m);
					}

					System.out.println("-------------------------------------------------");
					System.out.println(
							functions.get(i).toStringID().substring(main_iri.toString().length()).replace("_", " "));
					for (String m : models_2) {

						System.out.println(m);
					}

					double similarity_value = GeneralMethods.SimilarityValue(models_1, models_2);

					// Calculating the ontological similarity value by comparing the list of
					// suitable models for each task according to the data base.

					taskFct.setSimilarity(fun.toStringID().substring(main_iri.toString().length()).replace("_", " "),
							functions.get(i).toStringID().substring(main_iri.toString().length()).replace("_", " "),
							similarity_value);

					System.out.println(similarity_value);
				}

				i_fun += 1;

			}
		} catch (OWLOntologyCreationException e) {
			System.out.println("Could not load ontology: " + e.getMessage());
		}

		/**********************
		 * Save project file
		 ***************************************/
		myConcept.getProject().save();

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
}
