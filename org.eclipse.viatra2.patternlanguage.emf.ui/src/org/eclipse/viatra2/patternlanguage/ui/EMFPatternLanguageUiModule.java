/*
 * generated by Xtext
 */
package org.eclipse.viatra2.patternlanguage.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.editor.contentassist.XtextContentAssistProcessor;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Use this class to register components to be used within the IDE.
 */
public class EMFPatternLanguageUiModule extends org.eclipse.viatra2.patternlanguage.ui.AbstractEMFPatternLanguageUiModule {
	public EMFPatternLanguageUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	@Override
	public void configure(Binder binder) {
		super.configure(binder);
		binder.bind(String.class)
				.annotatedWith(
						Names.named(XtextContentAssistProcessor.COMPLETION_AUTO_ACTIVATION_CHARS))
				.toInstance(".,");
	}
}
