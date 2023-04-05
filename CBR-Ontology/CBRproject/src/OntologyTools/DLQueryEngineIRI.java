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

package OntologyTools;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

/**
 * This class is the one that contains the methods to perform queries on the
 * given ontology using an HermiT reasoner. The IRI identifiers of the entities
 * are used.
 * 
 * @author
 *
 */
public class DLQueryEngineIRI {
	private final Reasoner reasoner;// The ontological reasoner is created using the HermiT library.
	private final DLQueryParserIRI parser;// Object of the class DLQueryParserIRI in this same package.
	// The function of the class DLQueryParserIRI is to translate the names of owl
	// entities of the ontology into expressions
	// than can be operated by the reasoner.
	// For more information on the parser and its methods go to DLQueryParserIRI.

	/**
	 * Constructs a DLQueryEngineIRI. This will answer "DL queries" using the
	 * specified reasoner.
	 * 
	 * @param reasoner The reasoner to be used for answering the queries.
	 */
	public DLQueryEngineIRI(Reasoner reasoner) {
		this.reasoner = reasoner;
		OWLOntology rootOntology = reasoner.getRootOntology();
		parser = new DLQueryParserIRI(rootOntology);

	}

	/**
	 * Gets the superclasses of a class specified by its IRI.
	 * 
	 * @param classIRI The IRI that identifies the class.
	 * @param direct   Specifies whether direct superclasses should be returned or
	 *                 not.
	 * @return The superclasses of the specified class expression
	 */
	public Set<OWLClass> getSuperClasses(String classIRI, boolean direct) {
		if (classIRI.trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser.parseClassExpression(classIRI);
		NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(classExpression, direct);
		return superClasses.getFlattened();
	}

	/**
	 * Gets the equivalent classes of a class identified by its IRI.
	 * 
	 * @param classIRI The IRI that identifies the class.
	 * @return The equivalent classes of the specified class expression
	 */
	public Set<OWLClass> getEquivalentClasses(String classIRI) {
		if (classIRI.length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser.parseClassExpression(classIRI);
		Node<OWLClass> equivalentClasses = reasoner.getEquivalentClasses(classExpression);
		Set<OWLClass> result;
		if (classExpression.isAnonymous()) {
			result = equivalentClasses.getEntities();
		} else {
			result = equivalentClasses.getEntitiesMinus(classExpression.asOWLClass());
		}
		return result;
	}

	/**
	 * Gets the subclasses of a class identified by its IRI.
	 * 
	 * @param classIRI The IRI that identifies the class.
	 * @param direct   Specifies whether direct subclasses should be returned or
	 *                 not.
	 * @return The subclasses of the specified class expression
	 */
	public Set<OWLClass> getSubClasses(String classIRI, boolean direct) {
		if (classIRI.length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser.parseClassExpression(classIRI.replace(" ", "_").trim());
		NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
		return subClasses.getFlattened();
	}

	/**
	 * Gets the individuals of the ontology to which the specified ObjectProperty is
	 * pointing from the specified origin individual of the ontology.
	 * 
	 * @param propertyIRI   is the IRI identifier of the ObjectProperty that links
	 *                      both individuals
	 * 
	 * @param IndividualIRI is the individual of origin of the relation established
	 *                      by the ObjectProperty
	 * @return Set containing all the individuals of the query
	 */
	public Set<OWLNamedIndividual> getIndividualsProperty(String propertyIRI, String IndividualIRI) {

		OWLObjectPropertyExpression PropertyExpression = parser.parsePropertyExpression(propertyIRI);
		OWLIndividual individualOfProperty = parser.parseIndividualExpression(IndividualIRI);

		NodeSet<OWLNamedIndividual> propertyInstances = reasoner.getObjectPropertyValues(
				((OWLNamedIndividual) individualOfProperty), PropertyExpression.getNamedProperty());
		return propertyInstances.getFlattened();
	}

	/**
	 * Gets the instances of a class expression parsed from a string.
	 * 
	 * @param classIRI The IRI that identifies the class.
	 * @param direct   Specifies whether direct instances should be returned or not.
	 * @return The instances of the specified class expression
	 */

	public Set<OWLNamedIndividual> getInstances(String classIRI, boolean direct) {
		if (classIRI.length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser.parseClassExpression(classIRI);
		NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression, direct);
		return individuals.getFlattened();
	}

	/**
	 * Gets the individuals of a Set that belong to the specified class in the
	 * ontology.
	 * 
	 * @param originalSet is a Set of ontology individuals.
	 * @param classIRI    is a class to filter the individuals.
	 * @param direct      Specifies whether only direct instances should be returned
	 *                    or also instances of subclasses.
	 * @return a new Set containing only the individuals form the original Set that
	 *         belong to the specified class.
	 */
	public Set<OWLNamedIndividual> checkClass(Set<OWLNamedIndividual> originalSet, String classIRI, boolean direct)
			throws ParserException {
		Set<OWLNamedIndividual> newSet = new TreeSet<OWLNamedIndividual>(originalSet);
		OWLClassExpression classExpression = parser.parseClassExpression(classIRI);
		if (classIRI.length() == 0) {
			return Collections.emptySet();
		}
		for (OWLNamedIndividual ind : originalSet) {

			if (reasoner.getTypes(ind, direct).containsEntity((OWLClass) classExpression)) {

			} else {

				newSet.remove(ind);
			}

		}
		return newSet;
	}

	/**
	 * 
	 * @param IndividualIRI the IRI of the instance from which the class is wanted
	 *                      to be known.
	 * @param direct        Specifies whether only direct class should be returned
	 *                      or also superclasses.
	 * @return a Set of classes to which the individual belongs.
	 */
	public Set<OWLClass> getClass(String IndividualIRI, boolean direct) {
		if (IndividualIRI.length() == 0) {
			return Collections.emptySet();
		}
		OWLIndividual IndExpression = parser.parseIndividualExpression(IndividualIRI);
		NodeSet<OWLClass> Class = reasoner.getTypes((OWLNamedIndividual) IndExpression, direct);
		return Class.getFlattened();
	}

}
