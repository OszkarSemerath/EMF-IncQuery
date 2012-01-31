package org.eclipse.viatra2.emf.incquery.databinding.ui.observable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.viatra2.emf.incquery.runtime.api.IPatternSignature;
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.misc.DeltaMonitor;

/**
 * A PatternMatcher is associated to every IncQueryMatcher which is annotated with PatternUI annotation.
 * These elements will be the children of the top level elements is the treeviewer.
 * 
 * @author Tamas Szabo
 *
 */
public class PatternMatcher {
	
	private List<PatternMatch> matches;
	private IncQueryMatcher<? extends IPatternSignature> matcher;
	private DeltaMonitor<? extends IPatternSignature> deltaMonitor;
	private Runnable processMatchesRunnable;
	private Map<IPatternSignature, PatternMatch> sigMap;
	private PatternMatcherRoot parent;
	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this); 
	private boolean generated;
	
	public PatternMatcher(PatternMatcherRoot parent, IncQueryMatcher<? extends IPatternSignature> matcher, boolean generated) {
		this.parent = parent;
		this.matches = new ArrayList<PatternMatch>();
		this.sigMap = new HashMap<IPatternSignature, PatternMatch>();
		this.matcher = matcher;
		this.generated = generated;
		this.deltaMonitor = this.matcher.newDeltaMonitor(true);
		this.processMatchesRunnable = new Runnable() {		
			@Override
			public void run() {
				processNewMatches(deltaMonitor.matchFoundEvents);
				processLostMatches(deltaMonitor.matchLostEvents);
				deltaMonitor.clear();
			}
		};
		
		this.matcher.addCallbackAfterUpdates(processMatchesRunnable);
		this.processMatchesRunnable.run();
	}
	
	/**
	 * Call this method to remove the callback handler from the delta monitor of the matcher.
	 */
	public void dispose() {
		this.matcher.removeCallbackAfterUpdates(processMatchesRunnable);
		processMatchesRunnable = null;
	}
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
	
	private void processNewMatches(Collection<? extends IPatternSignature> signatures) {
		for (IPatternSignature s : signatures) {
			addSignature(s);
		}
	}

	private void processLostMatches(Collection<? extends IPatternSignature> signatures) {
		for (IPatternSignature s : signatures) {
			removeSignature(s);
		}
	}
	
	private void addSignature(IPatternSignature signature) {
		List<PatternMatch> oldValue = new ArrayList<PatternMatch>(matches);
		PatternMatch pm = new PatternMatch(this, signature);
		this.sigMap.put(signature, pm);
		this.matches.add(pm);
		this.propertyChangeSupport.firePropertyChange("matches", oldValue, matches);
	}
	
	private void removeSignature(IPatternSignature signature) {
		List<PatternMatch> oldValue = new ArrayList<PatternMatch>(matches);
		this.matches.remove(this.sigMap.remove(signature));
		this.propertyChangeSupport.firePropertyChange("matches", oldValue, matches);
	}

	public PatternMatcherRoot getParent() {
		return parent;
	}

	public String getText() {
		return this.matcher.getPatternName() + (isGenerated() ? " (Generated)" : " (Runtime)");
	}

	public List<PatternMatch> getMatches() {
		return matches;
	}

	public void setMatches(List<PatternMatch> matches) {
		this.matches = matches;
	}

	public boolean isGenerated() {
		return generated;
	}
}
