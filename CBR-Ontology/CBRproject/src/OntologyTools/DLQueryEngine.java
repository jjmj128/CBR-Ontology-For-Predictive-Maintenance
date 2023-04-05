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
import org.semanticweb.owlapi.util.ShortFormProvider;

/**
 * This class is the one that contains the methods to perform queries on the
 * given ontology using an HermiT reasoner.
 * 
 * @author
 *
 */
public class DLQueryEngine {
	private final Reasoner reasoner;// The ontological reasoner is created using the HermiT library.
	private final DLQueryParser parser;// Object of the class DLQueryParser in this same package.
	// The function of the class DLQueryParser is to translate the names of owl
	// entities of the ontology into expressions
	// than can be operated by the reasoner.
	// For more information on the parser and its methods go to DLQueryParser.

	/**
	 * Constructs a DLQueryEngine. This will answer "DL queries" using the specified
	 * reasoner. A short form provider specifies how entities are rendered.
	 * 
	 * @param reasoner          The reasoner to be used for answering the queries.
	 * @param shortFormProvider A short form provider.
	 */
	public DLQueryEngine(Reasoner reasoner, ShortFormProvider shortFormProvider) {
		this.reasoner = reasoner;
		OWLOntology rootOntology = reasoner.getRootOntology();
		parser = new DLQueryParser(rootOntology, shortFormProvider);

	}

	/**
	 * Gets the superclasses of a class expression parsed from a string.
	 * 
	 * @param classExpressionString The string from which the class expression will
	 *                              be parsed.
	 * @param direct                Specifies whether direct superclasses should be
	 *                              returned or not.
	 * @return The superclasses of the specified class expression
	 * @throws ParserException If there was a problem parsing the class expression.
	 */
	public Set<OWLClass> getSuperClasses(String classExpressionString, boolean direct) throws ParserException {
		if (classExpressionString.trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser
				.parseClassExpression(classExpressionString.replace(" ", "_").trim());
		NodeSet<OWLClass> superClasses = reasoner.getSuperClasses(classExpression, direct);
		return superClasses.getFlattened();
	}

	/**
	 * Gets the equivalent classes of a class expression parsed from a string.
	 * 
	 * @param classExpressionString The string from which the class expression will
	 *                              be parsed.
	 * @return The equivalent classes of the specified class expression
	 * @throws ParserException If there was a problem parsing the class expression.
	 */
	public Set<OWLClass> getEquivalentClasses(String classExpressionString) throws ParserException {
		if (classExpressionString.replace(" ", "_").trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser
				.parseClassExpression(classExpressionString.replace(" ", "_").trim());
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
	 * Gets the subclasses of a class expression parsed from a string.
	 * 
	 * @param classExpressionString The string from which the class expression will
	 *                              be parsed.
	 * @param direct                Specifies whether direct subclasses should be
	 *                              returned or not.
	 * @return The subclasses of the specified class expression
	 * @throws ParserException If there was a problem parsing the class expression.
	 */
	public Set<OWLClass> getSubClasses(String classExpressionString, boolean direct) throws ParserException {
		if (classExpressionString.replace(" ", "_").trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser
				.parseClassExpression(classExpressionString.replace(" ", "_").trim());
		NodeSet<OWLClass> subClasses = reasoner.getSubClasses(classExpression, direct);
		return subClasses.getFlattened();
	}

	/**
	 * Gets the individuals of the ontology to which the specified ObjectProperty is
	 * pointing from the specified origin individual of the ontology.
	 * 
	 * @param propertyExpressionString is the ObjectProperty that links both
	 *                                 individuals
	 * 
	 * @param IndExpressionString      is the individual of origin of the relation
	 *                                 established by the ObjectProperty
	 * @return Set containing all the individuals of the query
	 */
	public Set<OWLNamedIndividual> getIndividualsProperty(String propertyExpressionString, String IndExpressionString) {

		Set<OWLObjectPropertyExpression> PropertyExpression = parser
				.parsePropertyExpression(propertyExpressionString.trim());
		Set<OWLIndividual> individualsOfProperty = parser
				.parseIndividualExpression(IndExpressionString.replace(" ", "_").trim());

		NodeSet<OWLNamedIndividual> propertyInstances = reasoner.getObjectPropertyValues(
				((OWLNamedIndividual) individualsOfProperty.iterator().next()),
				PropertyExpression.iterator().next().getNamedProperty());
		return propertyInstances.getFlattened();
	}

	/**
	 * Gets the instances of a class expression parsed from a string.
	 * 
	 * @param classExpressionString The string from which the class expression will
	 *                              be parsed.
	 * @param direct                Specifies whether direct instances should be
	 *                              returned or not.
	 * @return The instances of the specified class expression
	 * @throws ParserException If there was a problem parsing the class expression.
	 */

	public Set<OWLNamedIndividual> getInstances(String classExpressionString, boolean direct) throws ParserException {
		if (classExpressionString.replace(" ", "_").trim().length() == 0) {
			return Collections.emptySet();
		}
		OWLClassExpression classExpression = parser
				.parseClassExpression(classExpressionString.replace(" ", "_").trim());
		NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(classExpression, direct);
		return individuals.getFlattened();
	}

	/**
	 * Gets the individuals of a Set that belong to the specified class in the
	 * ontology.
	 * 
	 * @param originalSet           is a Set of ontology individuals.
	 * @param classExpressionString is a class to filter the individuals.
	 * @param direct                Specifies whether direct instances should be
	 *                              returned or not.
	 * @return a new Set containing only the individuals form the original Set that
	 *         belong to the specified class
	 * @throws ParserException
	 */
	public Set<OWLNamedIndividual> checkClass(Set<OWLNamedIndividual> originalSet, String classExpressionString,
			boolean direct) throws ParserException {
		Set<OWLNamedIndividual> newSet = new TreeSet<OWLNamedIndividual>(originalSet);
		OWLClassExpression classExpression = parser
				.parseClassExpression(classExpressionString.replace(" ", "_").trim());
		if (classExpressionString.replace(" ", "_").trim().length() == 0) {
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

	public Set<OWLClass> getClass(String IndExpressionString, boolean direct) throws ParserException {
		if (IndExpressionString.replace(" ", "_").trim().length() == 0) {
			return Collections.emptySet();
		}
		Set<OWLIndividual> IndExpression = parser
				.parseIndividualExpression(IndExpressionString.replace(" ", "_").trim());
		NodeSet<OWLClass> Class = reasoner.getTypes((OWLNamedIndividual) IndExpression.iterator().next(), direct);
		return Class.getFlattened();
	}

}
