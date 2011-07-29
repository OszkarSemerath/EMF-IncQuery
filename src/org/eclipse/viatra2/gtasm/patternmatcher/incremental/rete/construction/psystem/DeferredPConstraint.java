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

import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.construction.Stub;

/**
 * @author Bergmann Gábor
 *
 */
public abstract class DeferredPConstraint<StubHandle> extends PConstraint {

	public abstract boolean isReadyAt(Stub<StubHandle> stub);

}
