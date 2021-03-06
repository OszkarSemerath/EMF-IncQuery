/*******************************************************************************
 * Copyright (c) 2010-2012, Tamas Szabo, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tamas Szabo - initial API and implementation
 *******************************************************************************/

package org.eclipse.viatra2.emf.incquery.base.itc.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CheckTcRelations {

	public static Test suite() {
		TestSuite suite = new TestSuite(CheckTcRelations.class.getName());

		suite.addTestSuite(DRedTestCase.class);
		suite.addTestSuite(DFSTestCase.class);
		suite.addTestSuite(CountingTestCase.class);
		suite.addTestSuite(Counting2TestCase.class);
		suite.addTestSuite(KingOptTestCase.class);
		suite.addTestSuite(IncSCCTestCase.class);
		
		return suite;
	}

}
