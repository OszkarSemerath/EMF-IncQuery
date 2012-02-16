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

package org.eclipse.viatra2.emf.incquery.runtime.api;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.viatra2.emf.incquery.runtime.exception.IncQueryRuntimeException;
import org.eclipse.viatra2.emf.incquery.runtime.internal.EMFPatternMatcherRuntimeContext;
import org.eclipse.viatra2.emf.incquery.runtime.internal.matcherbuilder.EPMBuilder;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.construction.ReteContainerBuildable;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.matcher.IPatternMatcherRuntimeContext;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.matcher.ReteEngine;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.network.Receiver;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.network.Supplier;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.remote.Address;
import org.eclipse.viatra2.patternlanguage.core.patternLanguage.Pattern;

/**
 * A EMF-IncQuery engine back-end, attached to a model such as an EMF resource. 
 * The engine hosts pattern matchers, and will listen on EMF update notifications stemming from the given model in order to maintain live results. 
 * 
 * Pattern matchers within this engine may be instantiated in the following ways:
 *  - Instantiate the specific matcher class generated for the pattern, by passing to the constructor either this engine or the EMF model root.
 *  - Use the matcher factory associated with the generated matcher class to achieve the same.
 *  - Use GenericMatcherFactory instead of the various generated factories.
 * 
 * The engine can be disposed in order to detach from the EMF model and stop listening on update notifications.
 * 
 * @author Bergmann Gábor
 *
 */
public class IncQueryEngine {

	/**
	 * The engine manager responsible for this engine.
	 */
	private EngineManager manager;
	/**
	 * The model to which the engine is attached.
	 */
	private Notifier emfRoot;
	/**
	 * The RETE pattern matcher component of the EMF-IncQuery engine.
	 */	
	private ReteEngine<Pattern> reteEngine = null;
	/**
	 * EXPERIMENTAL
	 */
	private int reteThreads = 0;
	// TODO IncQueryBase?
	
	/**
	 * @param manager
	 * @param emfRoot
	 */
	IncQueryEngine(EngineManager manager, Notifier emfRoot) {
		super();
		this.manager = manager;
		this.emfRoot = emfRoot;
	}	
	
	/**
	 * @return the root of the EMF model tree that this engine is attached to.
	 */
	public Notifier getEmfRoot() {
		return emfRoot;
	}
	
	/**
	 * Provides access to the internal RETE pattern matcher component of the EMF-IncQuery engine.
	 * @noreference A typical user would not need to call this method.
	 */
	public ReteEngine<Pattern> getReteEngine() throws IncQueryRuntimeException {
		if (reteEngine == null) {
			IPatternMatcherRuntimeContext<Pattern> context;
			if (emfRoot instanceof EObject) 
				context = new EMFPatternMatcherRuntimeContext.ForEObject<Pattern>((EObject)emfRoot);
			else if (emfRoot instanceof Resource) 
				context = new EMFPatternMatcherRuntimeContext.ForResource<Pattern>((Resource)emfRoot);
			else if (emfRoot instanceof ResourceSet) 
				context = new EMFPatternMatcherRuntimeContext.ForResourceSet<Pattern>((ResourceSet)emfRoot);
			else throw new IncQueryRuntimeException(IncQueryRuntimeException.INVALID_EMFROOT);
			
			reteEngine = buildReteEngineInternal(context);
			//if (reteEngine != null) engines.put(emfRoot, new WeakReference<ReteEngine<String>>(engine));
		}
		return reteEngine;
		
	}
	/**
	 * Disconnects the engine. 
	 * Matcher objects will continue to return stale results. 
	 * If no references are retained to the matchers or the engine, they can eventually be GC'ed, 
	 * 	and they won't block the EMF model from being GC'ed anymore. 
	 * 
	 * Cannot be reversed.
	 * @return true is an engine was found and disconnected, false if no engine was found for the given root.
	 */
	public void dispose() {
		manager.killInternal(emfRoot);
		killInternal();
	}
	
	private ReteEngine<Pattern> buildReteEngineInternal(IPatternMatcherRuntimeContext<Pattern> context) 
			throws IncQueryRuntimeException 
	{
		ReteEngine<Pattern> engine;
		engine = new ReteEngine<Pattern>(context, reteThreads);
		ReteContainerBuildable<Pattern> buildable = new ReteContainerBuildable<Pattern>(engine);
		EPMBuilder<Address<? extends Supplier>, Address<? extends Receiver>> builder = 
				new EPMBuilder<Address<? extends Supplier>, Address<? extends Receiver>> (buildable, context);
//		Collection<ViatraEMFPatternmatcherBuildAdvisor> advisors = 
//			BuilderRegistry.getContributedPatternBuildAdvisors();	
//		if (advisors==null || advisors.isEmpty()) {			
			engine.setBuilder(builder);
//			Set<Pattern> patternSet = BuilderRegistry.getContributedStatelessPatternBuilders().keySet(); 
//			try {
//				engine.buildMatchersCoalesced(patternSet);
//			} catch (RetePatternBuildException e) {
//				throw new IncQueryRuntimeException(e);
//			}
//		} else {
//			advisors.iterator().next().applyBuilder(engine, buildable, context);
//		}
		return engine;
	}


	/**
	 * To be called after already removed from engineManager.
	 */
	void killInternal() {
		if (reteEngine != null) {
			reteEngine.killEngine();
		}
	}

	
//	/**
//	 * EXPERIMENTAL: Creates an EMF-IncQuery engine that executes post-commit, or retrieves an already existing one.
//	 * @param emfRoot the EMF root where this engine should operate
//	 * @param reteThreads experimental feature; 0 is recommended
//	 * @return a new or previously existing engine
//	 * @throws IncQueryRuntimeException
//	 */	
//	public ReteEngine<String> getReteEngine(final TransactionalEditingDomain editingDomain, int reteThreads) throws IncQueryRuntimeException {
//		final ResourceSet resourceSet = editingDomain.getResourceSet();
//		WeakReference<ReteEngine<String>> weakReference = engines.get(resourceSet);
//		ReteEngine<String> engine = weakReference != null ? weakReference.get() : null;
//		if (engine == null) {
//			IPatternMatcherRuntimeContext<String> context = new EMFPatternMatcherRuntimeContext.ForTransactionalEditingDomain<String>(editingDomain);
//			engine = buildReteEngine(context, reteThreads);
//			if (engine != null) engines.put(resourceSet, new WeakReference<ReteEngine<String>>(engine));
//		}
//		return engine;
//	}	
}
