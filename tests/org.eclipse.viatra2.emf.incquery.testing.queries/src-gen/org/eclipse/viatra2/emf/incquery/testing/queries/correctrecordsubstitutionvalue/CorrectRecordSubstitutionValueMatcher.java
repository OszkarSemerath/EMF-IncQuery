package org.eclipse.viatra2.emf.incquery.testing.queries.correctrecordsubstitutionvalue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.viatra2.emf.incquery.runtime.api.EngineManager;
import org.eclipse.viatra2.emf.incquery.runtime.api.IMatchProcessor;
import org.eclipse.viatra2.emf.incquery.runtime.api.IMatcherFactory;
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryEngine;
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.viatra2.emf.incquery.runtime.api.impl.BaseGeneratedMatcher;
import org.eclipse.viatra2.emf.incquery.runtime.exception.IncQueryRuntimeException;
import org.eclipse.viatra2.emf.incquery.snapshot.EIQSnapshot.MatchRecord;
import org.eclipse.viatra2.emf.incquery.testing.queries.correctrecordsubstitutionvalue.CorrectRecordSubstitutionValueMatch;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.misc.DeltaMonitor;
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.tuple.Tuple;

/**
 * Generated pattern matcher API of the CorrectRecordSubstitutionValue pattern, 
 * providing pattern-specific query methods.
 * 
 * 
 * 	
 * pattern CorrectRecordSubstitutionValue(
 * 	Record : MatchRecord,
 * 	ParameterName : EString,
 * 	Value
 * ) = {
 * 	find MatchRecordWithSubstitution(Record, ParameterName, Substitution);
 * 	MatchSubstitutionRecord.derivedValue(Substitution, Value);
 * }
 * 
 * @see CorrectRecordSubstitutionValueMatch
 * @see CorrectRecordSubstitutionValueMatcherFactory
 * @see CorrectRecordSubstitutionValueProcessor
 * 
 */
public class CorrectRecordSubstitutionValueMatcher extends BaseGeneratedMatcher<CorrectRecordSubstitutionValueMatch> implements IncQueryMatcher<CorrectRecordSubstitutionValueMatch> {
  private final static int POSITION_RECORD = 0;
  
  private final static int POSITION_PARAMETERNAME = 1;
  
  private final static int POSITION_VALUE = 2;
  
  /**
   * Initializes the pattern matcher over a given EMF model root (recommended: Resource or ResourceSet). 
   * If a pattern matcher is already constructed with the same root, only a lightweight reference is created.
   * The scope of pattern matching will be the given EMF model root and below (see FAQ for more precise definition).
   * The match set will be incrementally refreshed upon updates from this scope.
   * @param emfRoot the root of the EMF containment hierarchy where the pattern matcher will operate. Recommended: Resource or ResourceSet.
   * @throws IncQueryRuntimeException if an error occurs during pattern matcher creation
   * 
   */
  public CorrectRecordSubstitutionValueMatcher(final Notifier emfRoot) throws IncQueryRuntimeException {
    this(EngineManager.getInstance().getIncQueryEngine(emfRoot));
  }
  
  /**
   * Initializes the pattern matcher within an existing EMF-IncQuery engine. 
   * If the pattern matcher is already constructed in the engine, only a lightweight reference is created.
   * The match set will be incrementally refreshed upon updates.
   * @param engine the existing EMF-IncQuery engine in which this matcher will be created.
   * @throws IncQueryRuntimeException if an error occurs during pattern matcher creation
   * 
   */
  public CorrectRecordSubstitutionValueMatcher(final IncQueryEngine engine) throws IncQueryRuntimeException {
    super(engine, FACTORY);
  }
  
  /**
   * Returns the set of all matches of the pattern that conform to the given fixed values of some parameters.
   * @param pRecord the fixed value of pattern parameter Record, or null if not bound.
   * @param pParameterName the fixed value of pattern parameter ParameterName, or null if not bound.
   * @param pValue the fixed value of pattern parameter Value, or null if not bound.
   * @return matches represented as a CorrectRecordSubstitutionValueMatch object.
   * 
   */
  public Collection<CorrectRecordSubstitutionValueMatch> getAllMatches(final MatchRecord pRecord, final String pParameterName, final Object pValue) {
    return rawGetAllMatches(new Object[]{pRecord, pParameterName, pValue});
  }
  
  /**
   * Returns an arbitrarily chosen match of the pattern that conforms to the given fixed values of some parameters.
   * Neither determinism nor randomness of selection is guaranteed.
   * @param pRecord the fixed value of pattern parameter Record, or null if not bound.
   * @param pParameterName the fixed value of pattern parameter ParameterName, or null if not bound.
   * @param pValue the fixed value of pattern parameter Value, or null if not bound.
   * @return a match represented as a CorrectRecordSubstitutionValueMatch object, or null if no match is found.
   * 
   */
  public CorrectRecordSubstitutionValueMatch getOneArbitraryMatch(final MatchRecord pRecord, final String pParameterName, final Object pValue) {
    return rawGetOneArbitraryMatch(new Object[]{pRecord, pParameterName, pValue});
  }
  
  /**
   * Indicates whether the given combination of specified pattern parameters constitute a valid pattern match,
   * under any possible substitution of the unspecified parameters (if any).
   * @param pRecord the fixed value of pattern parameter Record, or null if not bound.
   * @param pParameterName the fixed value of pattern parameter ParameterName, or null if not bound.
   * @param pValue the fixed value of pattern parameter Value, or null if not bound.
   * @return true if the input is a valid (partial) match of the pattern.
   * 
   */
  public boolean hasMatch(final MatchRecord pRecord, final String pParameterName, final Object pValue) {
    return rawHasMatch(new Object[]{pRecord, pParameterName, pValue});
  }
  
  /**
   * Returns the number of all matches of the pattern that conform to the given fixed values of some parameters.
   * @param pRecord the fixed value of pattern parameter Record, or null if not bound.
   * @param pParameterName the fixed value of pattern parameter ParameterName, or null if not bound.
   * @param pValue the fixed value of pattern parameter Value, or null if not bound.
   * @return the number of pattern matches found.
   * 
   */
  public int countMatches(final MatchRecord pRecord, final String pParameterName, final Object pValue) {
    return rawCountMatches(new Object[]{pRecord, pParameterName, pValue});
  }
  
  /**
   * Executes the given processor on each match of the pattern that conforms to the given fixed values of some parameters.
   * @param pRecord the fixed value of pattern parameter Record, or null if not bound.
   * @param pParameterName the fixed value of pattern parameter ParameterName, or null if not bound.
   * @param pValue the fixed value of pattern parameter Value, or null if not bound.
   * @param processor the action that will process each pattern match.
   * 
   */
  public void forEachMatch(final MatchRecord pRecord, final String pParameterName, final Object pValue, final IMatchProcessor<? super CorrectRecordSubstitutionValueMatch> processor) {
    rawForEachMatch(new Object[]{pRecord, pParameterName, pValue}, processor);
  }
  
  /**
   * Executes the given processor on an arbitrarily chosen match of the pattern that conforms to the given fixed values of some parameters.  
   * Neither determinism nor randomness of selection is guaranteed.
   * @param pRecord the fixed value of pattern parameter Record, or null if not bound.
   * @param pParameterName the fixed value of pattern parameter ParameterName, or null if not bound.
   * @param pValue the fixed value of pattern parameter Value, or null if not bound.
   * @param processor the action that will process the selected match. 
   * @return true if the pattern has at least one match with the given parameter values, false if the processor was not invoked
   * 
   */
  public boolean forOneArbitraryMatch(final MatchRecord pRecord, final String pParameterName, final Object pValue, final IMatchProcessor<? super CorrectRecordSubstitutionValueMatch> processor) {
    return rawForOneArbitraryMatch(new Object[]{pRecord, pParameterName, pValue}, processor);
  }
  
  /**
   * Registers a new filtered delta monitor on this pattern matcher.
   * The DeltaMonitor can be used to track changes (delta) in the set of filtered pattern matches from now on, considering those matches only that conform to the given fixed values of some parameters. 
   * It can also be reset to track changes from a later point in time, 
   * and changes can even be acknowledged on an individual basis. 
   * See {@link DeltaMonitor} for details.
   * @param fillAtStart if true, all current matches are reported as new match events; if false, the delta monitor starts empty.
   * @param pRecord the fixed value of pattern parameter Record, or null if not bound.
   * @param pParameterName the fixed value of pattern parameter ParameterName, or null if not bound.
   * @param pValue the fixed value of pattern parameter Value, or null if not bound.
   * @return the delta monitor.
   * 
   */
  public DeltaMonitor<CorrectRecordSubstitutionValueMatch> newFilteredDeltaMonitor(final boolean fillAtStart, final MatchRecord pRecord, final String pParameterName, final Object pValue) {
    return rawNewFilteredDeltaMonitor(fillAtStart, new Object[]{pRecord, pParameterName, pValue});
  }
  
  /**
   * Retrieve the set of values that occur in matches for Record.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<MatchRecord> rawAccumulateAllValuesOfRecord(final Object[] parameters) {
    Set<MatchRecord> results = new HashSet<MatchRecord>();
    rawAccumulateAllValues(POSITION_RECORD, parameters, results);
    return results;
  }
  
  /**
   * Retrieve the set of values that occur in matches for Record.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<MatchRecord> getAllValuesOfRecord() {
    return rawAccumulateAllValuesOfRecord(emptyArray());
  }
  
  /**
   * Retrieve the set of values that occur in matches for Record.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<MatchRecord> getAllValuesOfRecord(final CorrectRecordSubstitutionValueMatch partialMatch) {
    return rawAccumulateAllValuesOfRecord(partialMatch.toArray());
  }
  
  /**
   * Retrieve the set of values that occur in matches for Record.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<MatchRecord> getAllValuesOfRecord(final String pParameterName, final Object pValue) {
    MatchRecord pRecord = null;
    return rawAccumulateAllValuesOfRecord(new Object[]{pRecord, pParameterName, pValue});
  }
  
  /**
   * Retrieve the set of values that occur in matches for ParameterName.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<String> rawAccumulateAllValuesOfParameterName(final Object[] parameters) {
    Set<String> results = new HashSet<String>();
    rawAccumulateAllValues(POSITION_PARAMETERNAME, parameters, results);
    return results;
  }
  
  /**
   * Retrieve the set of values that occur in matches for ParameterName.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<String> getAllValuesOfParameterName() {
    return rawAccumulateAllValuesOfParameterName(emptyArray());
  }
  
  /**
   * Retrieve the set of values that occur in matches for ParameterName.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<String> getAllValuesOfParameterName(final CorrectRecordSubstitutionValueMatch partialMatch) {
    return rawAccumulateAllValuesOfParameterName(partialMatch.toArray());
  }
  
  /**
   * Retrieve the set of values that occur in matches for ParameterName.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<String> getAllValuesOfParameterName(final MatchRecord pRecord, final Object pValue) {
    String pParameterName = null;
    return rawAccumulateAllValuesOfParameterName(new Object[]{pRecord, pParameterName, pValue});
  }
  
  /**
   * Retrieve the set of values that occur in matches for Value.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<Object> rawAccumulateAllValuesOfValue(final Object[] parameters) {
    Set<Object> results = new HashSet<Object>();
    rawAccumulateAllValues(POSITION_VALUE, parameters, results);
    return results;
  }
  
  /**
   * Retrieve the set of values that occur in matches for Value.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<Object> getAllValuesOfValue() {
    return rawAccumulateAllValuesOfValue(emptyArray());
  }
  
  /**
   * Retrieve the set of values that occur in matches for Value.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<Object> getAllValuesOfValue(final CorrectRecordSubstitutionValueMatch partialMatch) {
    return rawAccumulateAllValuesOfValue(partialMatch.toArray());
  }
  
  /**
   * Retrieve the set of values that occur in matches for Value.
   * @return the Set of all values, null if no parameter with the given name exists, empty set if there are no matches
   * 
   */
  public Set<Object> getAllValuesOfValue(final MatchRecord pRecord, final String pParameterName) {
    Object pValue = null;
    return rawAccumulateAllValuesOfValue(new Object[]{pRecord, pParameterName, pValue});
  }
  
  @Override
  public CorrectRecordSubstitutionValueMatch tupleToMatch(final Tuple t) {
    try {
    	return new CorrectRecordSubstitutionValueMatch((org.eclipse.viatra2.emf.incquery.snapshot.EIQSnapshot.MatchRecord) t.get(POSITION_RECORD), (java.lang.String) t.get(POSITION_PARAMETERNAME), (java.lang.Object) t.get(POSITION_VALUE));	
    } catch(ClassCastException e) {engine.getLogger().logError("Element(s) in tuple not properly typed!",e);	//throw new IncQueryRuntimeException(e.getMessage());
    	return null;
    }
    
  }
  
  @Override
  public CorrectRecordSubstitutionValueMatch arrayToMatch(final Object[] match) {
    try {
    	return new CorrectRecordSubstitutionValueMatch((org.eclipse.viatra2.emf.incquery.snapshot.EIQSnapshot.MatchRecord) match[POSITION_RECORD], (java.lang.String) match[POSITION_PARAMETERNAME], (java.lang.Object) match[POSITION_VALUE]);
    } catch(ClassCastException e) {engine.getLogger().logError("Element(s) in array not properly typed!",e);	//throw new IncQueryRuntimeException(e.getMessage());
    	return null;
    }
    
  }
  
  @Override
  public CorrectRecordSubstitutionValueMatch newEmptyMatch() {
    return arrayToMatch(new Object[getParameterNames().length]);
  }
  
  public final static IMatcherFactory<CorrectRecordSubstitutionValueMatch,CorrectRecordSubstitutionValueMatcher> FACTORY =  new CorrectRecordSubstitutionValueMatcherFactory();
}
