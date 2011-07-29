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

package org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.construction.psystem;

/**
 * A constraint that can incorporate other constraints (probably of the same type) and enforce them together
 * @author Bergmann Gábor
 *
 */
public interface IFoldableConstraint {
	/**
	 * 
	 * @param other another PConstraint
	 * @return true if other constraint was compatible at could be incorporated in this one.
	 */
	public boolean incorporate(PConstraint other);
}
