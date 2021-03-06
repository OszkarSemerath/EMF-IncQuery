/*******************************************************************************
 * Copyright (c) 2010-2012, Mark Czotter, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Mark Czotter - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra2.emf.incquery.tooling.generator.util;

import org.eclipse.xtext.common.types.JvmLowerBound;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmWildcardTypeReference;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;

import com.google.inject.Inject;

/**
 * Custom {@link JvmTypesBuilder} for EMFPatternLanguage.
 * 
 * @author Mark Czotter
 * 
 */
@SuppressWarnings("restriction")
public class EMFJvmTypesBuilder extends JvmTypesBuilder {

	@Inject
	private TypesFactory factory = TypesFactory.eINSTANCE;
   	
	/**
	 * Creates a {@link JvmWildcardTypeReference} with a {@link JvmLowerBound}
	 * constraint to 'clone' parameter.
	 * 
	 * @param clone
	 * @return {@link JvmWildcardTypeReference} with a {@link JvmLowerBound}
	 *         contraint.
	 */
   	public JvmWildcardTypeReference wildCardSuper(JvmTypeReference clone) {
		JvmWildcardTypeReference result = factory.createJvmWildcardTypeReference();
		JvmLowerBound lowerBound = factory.createJvmLowerBound();
		lowerBound.setTypeReference(clone);
		result.getConstraints().add(lowerBound);
		return result;
   	}
	
}
