/*******************************************************************************
 * Copyright (c) 2004-2012 Gabor Bergmann and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Gabor Bergmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.index;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.network.Direction;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.network.Node;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.network.ReteContainer;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.network.Supplier;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.tuple.Tuple;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.tuple.TupleMask;

/**
 * Defines an abstract trivial indexer that identically projects the contents of some stateful node, and can therefore save space.
 * Can only exist in connection with a stateful store, and must be operated by another node (the active node). Do not attach parents directly!
 * @author Bergmann Gábor
 */
public abstract class IdentityIndexer extends StandardIndexer implements ProjectionIndexer {

	protected Node activeNode;

	protected abstract Collection<Tuple> getTuples();

	/**
	 * @param reteContainer
	 * @param mask
	 */
	public IdentityIndexer(ReteContainer reteContainer, int tupleWidth, Supplier parent, Node activeNode) {
		super(reteContainer, TupleMask.identity(tupleWidth));
		this.parent = parent;
		this.activeNode = activeNode;
	}

	public Collection<Tuple> get(Tuple signature) {
		if (contains(signature)) {
			return Collections.singleton(signature);
		} else return null;
	}

	/**
	 * @param signature
	 * @return
	 */
	protected boolean contains(Tuple signature) {
		return getTuples().contains(signature);
	}

	public Collection<Tuple> getSignatures() {
		return getTuples();
	}

	public Iterator<Tuple> iterator() {
		return getTuples().iterator();
	}

	@Override
	public Node getActiveNode() {
		return activeNode;
	}

	public void propagate(Direction direction, Tuple updateElement) {
		propagate(direction, updateElement, updateElement, true);	
	}

}