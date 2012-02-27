package org.eclipse.viatra2.patternlanguage.jvmmodel

import com.google.inject.Inject
import java.util.Collection
import org.eclipse.emf.common.notify.Notifier
import org.eclipse.viatra2.emf.incquery.runtime.api.EngineManager
import org.eclipse.viatra2.emf.incquery.runtime.api.IMatchProcessor
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryEngine
import org.eclipse.viatra2.emf.incquery.runtime.api.IncQueryMatcher
import org.eclipse.viatra2.emf.incquery.runtime.api.impl.BaseGeneratedMatcher
import org.eclipse.viatra2.emf.incquery.runtime.exception.IncQueryRuntimeException
import org.eclipse.viatra2.gtasm.patternmatcher.incremental.rete.tuple.Tuple
import org.eclipse.viatra2.patternlanguage.core.patternLanguage.Pattern
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.xbase.compiler.ImportManager

/**
 * {@link IncQueryMatcher} implementation inferrer.
 * 
 * @author Mark Czotter
 */
class PatternMatcherClassInferrer {

	@Inject extension EMFJvmTypesBuilder
	@Inject extension EMFPatternLanguageJvmModelInferrerUtil
	@Inject extension JavadocInferrer

	/**
	 * Infers the {@link IncQueryMatcher} implementation class from a {@link Pattern}.
	 */
	def JvmDeclaredType inferMatcherClass(Pattern pattern, boolean isPrelinkingPhase, String matcherPackageName, JvmTypeReference matchClassRef) {
		val matcherClass = pattern.toClass(pattern.matcherClassName) [
   			it.packageName = matcherPackageName
   			it.documentation = pattern.javadocMatcherClass.toString
			//it.annotations += pattern.toAnnotation(typeof (SuppressWarnings), "unused")
   			it.superTypes += pattern.newTypeRef(typeof(BaseGeneratedMatcher), cloneWithProxies(matchClassRef))
   			it.superTypes += pattern.newTypeRef(typeof(IncQueryMatcher), cloneWithProxies(matchClassRef))
   		]
   		matcherClass.inferMatcherClassConstructors(pattern)
   		matcherClass.inferMatcherClassMethods(pattern, matchClassRef)
   		matcherClass.inferMatcherClassToMatchMethods(pattern, matchClassRef)
   		return matcherClass
   	}
   	
	/**
   	 * Infers constructors for Matcher class based on the input 'pattern'.
   	 */
   	def inferMatcherClassConstructors(JvmDeclaredType matcherClass, Pattern pattern) {
   		matcherClass.members += pattern.toConstructor(pattern.matcherClassName) [
			it.visibility = JvmVisibility::PUBLIC
			it.documentation = pattern.javadocMatcherConstructorNotifier.toString
			it.parameters += pattern.toParameter("notifier", pattern.newTypeRef(typeof (Notifier)))
			it.exceptions += pattern.newTypeRef(typeof (IncQueryRuntimeException))
			it.body = [it | pattern.matcherConstructorBodyNotifier(it)]
		]
		
		matcherClass.members += pattern.toConstructor(pattern.matcherClassName) [
			it.visibility = JvmVisibility::PUBLIC
			it.documentation = pattern.javadocMatcherConstructorEngine.toString
			it.parameters += pattern.toParameter("engine", pattern.newTypeRef(typeof (IncQueryEngine)))
			it.exceptions += pattern.newTypeRef(typeof (IncQueryRuntimeException))
			it.body = ['''super(engine, FACTORY);''']
		]
   	}
   	
   	/**
   	 * Infers methods for Matcher class based on the input 'pattern'.
   	 */
   	def inferMatcherClassMethods(JvmDeclaredType matcherClass, Pattern pattern, JvmTypeReference matchClassRef) {
   		// Adding type-safe matcher calls
		// if the pattern not defines parameters, the Matcher class contains only the hasMatch method
		if (!pattern.parameters.isEmpty) {
			 matcherClass.members += pattern.toMethod("getAllMatches", pattern.newTypeRef(typeof(Collection), cloneWithProxies(matchClassRef))) [
   				it.documentation = pattern.javadocGetAllMatchesMethod.toString
   				for (parameter : pattern.parameters){
					it.parameters += parameter.toParameter(parameter.name, parameter.calculateType)				
   				}
   				it.body = ['''
   					return rawGetAllMatches(new Object[]{«FOR p : pattern.parameters SEPARATOR ', '»«p.name»«ENDFOR»});
   				''']
   			]
   			matcherClass.members += pattern.toMethod("getOneArbitraryMatch", cloneWithProxies(matchClassRef)) [
   				it.documentation = pattern.javadocGetOneArbitraryMatchMethod.toString
   				for (parameter : pattern.parameters){
					it.parameters += parameter.toParameter(parameter.name, parameter.calculateType)				
   				}
   				it.body = ['''
   					return rawGetOneArbitraryMatch(new Object[]{«FOR p : pattern.parameters SEPARATOR ', '»«p.name»«ENDFOR»});
   				''']
   			]
   			matcherClass.members += pattern.toMethod("hasMatch", pattern.newTypeRef(typeof(boolean))) [
   				it.documentation = pattern.javadocHasMatchMethod.toString
   				for (parameter : pattern.parameters){
					it.parameters += parameter.toParameter(parameter.name, parameter.calculateType)				
   				}
   				it.body = ['''
   					return rawHasMatch(new Object[]{«FOR p : pattern.parameters SEPARATOR ', '»«p.name»«ENDFOR»});
   				''']
   			]
   			matcherClass.members += pattern.toMethod("countMatches", pattern.newTypeRef(typeof(int))) [
   				it.documentation = pattern.javadocCountMatchesMethod.toString
   				for (parameter : pattern.parameters){
					it.parameters += parameter.toParameter(parameter.name, parameter.calculateType)				
   				}
   				it.body = ['''
   					return rawCountMatches(new Object[]{«FOR p : pattern.parameters SEPARATOR ', '»«p.name»«ENDFOR»});
   				''']
   			]
   			matcherClass.members += pattern.toMethod("forEachMatch", null) [
   				it.documentation = pattern.javadocForEachMatchMethod.toString
   				for (parameter : pattern.parameters){
					it.parameters += parameter.toParameter(parameter.name, parameter.calculateType)				
   				}
				it.parameters += pattern.toParameter("processor", pattern.newTypeRef(typeof (IMatchProcessor), cloneWithProxies(matchClassRef).wildCardSuper))
   				it.body = ['''
   					rawForEachMatch(new Object[]{«FOR p : pattern.parameters SEPARATOR ', '»«p.name»«ENDFOR»}, processor);
   				''']
   			]
   			matcherClass.members += pattern.toMethod("forOneArbitraryMatch", pattern.newTypeRef(typeof(boolean))) [
   				it.documentation = pattern.javadocForOneArbitraryMatchMethod.toString
   				for (parameter : pattern.parameters){
					it.parameters += parameter.toParameter(parameter.name, parameter.calculateType)				
   				}
   				it.parameters += pattern.toParameter("processor", pattern.newTypeRef(typeof (IMatchProcessor), cloneWithProxies(matchClassRef).wildCardSuper))
   				it.body = ['''
   					return rawForOneArbitraryMatch(new Object[]{«FOR p : pattern.parameters SEPARATOR ', '»«p.name»«ENDFOR»}, processor);
   				''']
   			]
		} else {
			matcherClass.members += pattern.toMethod("hasMatch", pattern.newTypeRef(typeof(boolean))) [
   				it.documentation = pattern.javadocHasMatchMethodNoParameter.toString
   				it.body = ['''
   					return rawHasMatch(new Object[]{});
   				''']
   			]
		}
   	}
   	
   	/**
   	 * Infers tupleToMatch, arrayToMatch methods for Matcher class based on the input 'pattern'.
   	 */   	
   	def inferMatcherClassToMatchMethods(JvmDeclaredType matcherClass, Pattern pattern, JvmTypeReference matchClassRef) {
	   	matcherClass.members += pattern.toMethod("tupleToMatch", cloneWithProxies(matchClassRef)) [
   			it.annotations += pattern.toAnnotation(typeof (Override))
   			it.parameters += pattern.toParameter("t", pattern.newTypeRef(typeof (Tuple)))
   			it.body = ['''
   				try {
   					return new «pattern.matchClassName»(«FOR p : pattern.parameters SEPARATOR ', '»(«p.calculateType.simpleName») t.get(«pattern.parameters.indexOf(p)»)«ENDFOR»);	
   				} catch(ClassCastException e) {
   					throw new IncQueryRuntimeException(e.getMessage());
   				}
   			''']
   		]
   		matcherClass.members += pattern.toMethod("arrayToMatch", cloneWithProxies(matchClassRef)) [
   			it.annotations += pattern.toAnnotation(typeof (Override))
   			it.parameters += pattern.toParameter("match", pattern.newTypeRef(typeof (Object)).addArrayTypeDimension)
   			it.body = ['''
   				try {
   					return new «pattern.matchClassName»(«FOR p : pattern.parameters SEPARATOR ', '»(«p.calculateType.simpleName») match[«pattern.parameters.indexOf(p)»]«ENDFOR»);
   				} catch(ClassCastException e) {
   					throw new IncQueryRuntimeException(e.getMessage());
   				}
   			''']
   		]
   	}
   	
	/**
   	 * Infers body for Matcher class constructor (Notifier) based on the input 'pattern'.
   	 */
   	def matcherConstructorBodyNotifier(Pattern pattern, ImportManager manager) {
  		manager.addImportFor(pattern.newTypeRef(typeof (EngineManager)).type)
  		return '''this(EngineManager.getInstance().getIncQueryEngine(notifier));'''
  	}
	
}