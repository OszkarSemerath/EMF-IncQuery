/*******************************************************************************
 * Copyright (c) 2004-2010 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra2.emf.incquery.runtime.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.viatra2.emf.incquery.base.logging.EMFIncQueryRuntimeLogger;
import org.eclipse.viatra2.patternlanguage.core.helper.CorePatternLanguageHelper;
import org.eclipse.viatra2.patternlanguage.core.patternLanguage.Pattern;

/**
 * Stateful sanitizer that maintains a set of admitted patterns. 
 * Patterns go through sanitization checks (validation + name uniqueness) before they can be admitted.
 * 
 * <p>INVARIANTS: <ul>
 * <li>the set of admitted patterns is closed with respect to references.
 * <li>the set of admitted patterns are free of errors.
 * <li>admitted patterns have unique qualified names.
 * </ul>
 * 
 * @author Bergmann Gábor
 *
 */
public class PatternSanitizer {
	Set<Pattern> admittedPatterns = new HashSet<Pattern>();
	Map<String, Pattern> patternsByName = new HashMap<String, Pattern>();
		
	EMFIncQueryRuntimeLogger logger;
	

	/**
	 * Creates an instance of the stateful sanitizer. 
	 * 
	 * @param logger where detected problems will be logged
	 */
	public PatternSanitizer(final EMFIncQueryRuntimeLogger logger) {
		super();

		this.logger = logger;	
	}
	

		
		
	/**
	 * Admits a new pattern, checking if it passes validation and name uniqueness checks. 
	 * Referenced patterns likewise go through the checks.
	 * Transactional semantics: will only admit any patterns if none of them have any errors.
	 * 
	 * @param pattern a pattern that should be validated.
	 * @return false if the pattern was not possible to admit, true if it passed all validation checks (or was already admitted before)
	 */
	public boolean admit(Pattern pattern) {
		return admit(Collections.singletonList(pattern));
	}
	
	/**
	 * Admits new patterns, checking whether they all pass validation and name uniqueness checks.  
	 * Referenced patterns likewise go through the checks. 
	 * Transactional semantics: will only admit any patterns if none of them have any errors.
	 * 
	 * @param patterns the collection of patterns that should be validated together.
	 * @return false if the patterns were not possible to admit, true if they passed all validation checks (or were already admitted before)
	 */
	public boolean admit(Collection<Pattern> patterns) {		
		Set<Pattern> newPatterns = getAllReferencedUnvalidatedPatterns(patterns);
		
		
		// TODO validate(toBeValidated) as a group
		Set<Pattern> inadmissible = new HashSet<Pattern>();
		Map<String, Pattern> newPatternsByName = new HashMap<String, Pattern>();
		for (Pattern current : newPatterns) {
			
			final String fullyQualifiedName = CorePatternLanguageHelper.getFullyQualifiedName(current);
			final boolean duplicate = patternsByName.containsKey(fullyQualifiedName) || newPatternsByName.containsKey(fullyQualifiedName);
			if (duplicate) {
				inadmissible.add(current);
				logger.logError("Duplicate (qualified) name of pattern: " + fullyQualifiedName);
			} else {
				newPatternsByName.put(fullyQualifiedName, current);
			}

			// TODO actual validation
			final boolean validationPassed = true; //validator == null || validator.validate(current, errorOnlyLogger);
			if (!validationPassed) {
				inadmissible.add(current);
			}	
			
		}
		
		boolean ok = inadmissible.isEmpty();
		if (ok) {
			admittedPatterns.addAll(newPatterns);
			patternsByName.putAll(newPatternsByName);
		}
		return ok;
	}




	/**
	 * Gathers all patterns that are not admitted yet, but are transitively referenced from the given patterns.
	 */
	protected Set<Pattern> getAllReferencedUnvalidatedPatterns(Collection<Pattern> patterns) {
		Set<Pattern> toBeValidated = new HashSet<Pattern>();

		LinkedList<Pattern> unexplored = new LinkedList<Pattern>();	
		
		for (Pattern pattern : patterns) {
			if (!admittedPatterns.contains(pattern)) {
				toBeValidated.add(pattern);
				unexplored.add(pattern);
			}
		} 
		
		while (!unexplored.isEmpty()) {
			Pattern current = unexplored.pollFirst();			
			final Set<Pattern> referencedPatterns = CorePatternLanguageHelper.getReferencedPatterns(current);
			for (Pattern referenced : referencedPatterns) {
				if (!admittedPatterns.contains(referenced) && !toBeValidated.contains(referenced)) {
					toBeValidated.add(referenced);
					unexplored.add(referenced);
				}
			}
		}
		return toBeValidated;
	}




	/**
	 * Returns the set of patterns that have been admitted so far.
	 * @return the admitted patterns
	 */
	public Set<Pattern> getAdmittedPatterns() {
		return Collections.unmodifiableSet(admittedPatterns);
	}
	
	
	
}
