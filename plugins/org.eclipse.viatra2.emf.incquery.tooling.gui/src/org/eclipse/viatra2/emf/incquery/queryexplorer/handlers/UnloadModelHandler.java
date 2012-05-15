package org.eclipse.viatra2.emf.incquery.queryexplorer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.viatra2.emf.incquery.queryexplorer.QueryExplorer;
import org.eclipse.viatra2.emf.incquery.queryexplorer.content.matcher.ObservablePatternMatcherRoot;

public class UnloadModelHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		if (selection != null && selection instanceof TreeSelection) {
			TreeSelection ts = (TreeSelection) selection;
			ObservablePatternMatcherRoot root = (ObservablePatternMatcherRoot) ts.getFirstElement();
			root.getEditorPart().getSite().getPage().removePartListener(QueryExplorer.getInstance().getModelPartListener());
			QueryExplorer.getInstance().getMatcherTreeViewerRoot().removePatternMatcherRoot(root.getKey());
		}
		
		QueryExplorer.getInstance().clearTableViewer();
		
		return null;
	}
}
