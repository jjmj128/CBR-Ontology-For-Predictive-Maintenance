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

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;

//This class is used as an intermediated between DLQueryEngine and the reasoner to perform the queries.
//The names of a class, individual or property must be parsed for the reasoner.
/**
 * 
 * 
 *
 */
public class DLQueryParserIRI {
	private final OWLOntology rootOntology;// The target ontology, defined y the object of type OWLOntology,
	// which is a class within the OWL API.

	public DLQueryParserIRI(OWLOntology rootOntology) {
		this.rootOntology = rootOntology;

		// Create a bidirectional short form provider to do the actual mapping.
		// It will generate names using the input
		// short form provider.

	}

	public OWLClassExpression parseClassExpression(String classIRI) {
		OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager().getOWLDataFactory();

		return dataFactory.getOWLClass(IRI.create(classIRI));
	}

	public OWLObjectPropertyExpression parsePropertyExpression(String propertyIRI) {
		OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager().getOWLDataFactory();

		return dataFactory.getOWLObjectProperty(IRI.create(propertyIRI));
	}

	public OWLIndividual parseIndividualExpression(String IndividualIRI) {
		OWLDataFactory dataFactory = rootOntology.getOWLOntologyManager().getOWLDataFactory();

		return dataFactory.getOWLNamedIndividual(IRI.create(IndividualIRI));
	}

}
