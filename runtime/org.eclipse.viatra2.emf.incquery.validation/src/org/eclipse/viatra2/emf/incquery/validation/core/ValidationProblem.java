package org.eclipse.viatra2.emf.incquery.validation.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.viatra2.emf.incquery.runtime.api.IPatternSignature;
import org.eclipse.viatra2.emf.incquery.runtime.api.impl.BasePatternSignature;

public class ValidationProblem<Signature extends IPatternSignature> {

	Constraint<Signature> kind;
	
	Signature affectedElements;
	
	public ValidationProblem(Constraint<Signature> _kind, Signature _affectedElements) 
	{
		this.kind = _kind;
		this.affectedElements = _affectedElements;
	}
	
	public IMarker createMarker(IFile file) throws CoreException {
		IMarker marker = file.createMarker("org.eclipse.emf.validation.problem");
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute(IMarker.TRANSIENT, true);
		
		String message = kind.getMessage(affectedElements);
//		if (message==null) {
//			message = kind.getMessage();
//		}
		marker.setAttribute(IMarker.MESSAGE, message );    
		EObject location = kind.getLocationObject(affectedElements);
		if (location!=null) {
			/*
			 * Based on EMF Validation's MarkerUtil class inner attributes
			 */
			marker.setAttribute(EValidator.URI_ATTRIBUTE, EcoreUtil.getURI(location).toString());
			marker.setAttribute(IMarker.LOCATION, BasePatternSignature.prettyPrintValue(location));//TODO find other place for this
		}
		
		return marker;
	}
	
	/**
	 * Two validation problems are equal iff:
	 * - they have the same kind
	 * - they affect the same collection of elements
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ValidationProblem) {
			@SuppressWarnings("unchecked")
			ValidationProblem<? extends IPatternSignature> vp2 = (ValidationProblem<? extends IPatternSignature>)obj;
			return this.kind.equals(vp2.kind) && 
				affectedElements.equals(vp2.affectedElements);
		}
		return false;
	}
	
	
	@Override
	public int hashCode() {
		int hash = 31 + this.kind.hashCode();
		hash = 31*hash + affectedElements.hashCode();
		return hash;
	}
}
