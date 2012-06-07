package org.eclipse.viatra2.emf.incquery.gui.wizards.internal;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.jdt.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("restriction")
public class ObjectsListAdapter implements IListAdapter<ObjectParameter> {

	private ListDialogField<Resource> importList;
	
	public ObjectsListAdapter(ListDialogField<Resource> importList) {
		this.importList = importList;
	}
	
	@Override
	public void customButtonPressed(ListDialogField<ObjectParameter> field, int index) {
		ObjectParameter parameter = new ObjectParameter();
		ObjectParameterConfigurationDialog dialog = new ObjectParameterConfigurationDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				importList.getElements(), parameter);
		//a unique parameter object is needed because the dialog will be disposed after the ok button is pressed
		if (index == 0) {
			//Add
			if (dialog.open() == Dialog.OK) {
				field.addElement(parameter);
			}
		}
		else if (index == 1) {
			//Modify
			ObjectParameter firstElement = field.getSelectedElements().get(0);
			parameter.setObject(firstElement.getObject());
			parameter.setParameterName(firstElement.getParameterName());
			if (dialog.open() == Dialog.OK) {
				firstElement.setObject(parameter.getObject());
				firstElement.setParameterName(parameter.getParameterName());
			}
		}
	}

	@Override
	public void selectionChanged(ListDialogField<ObjectParameter> field) {
		if (field.getElements().size() > 0) {
			field.enableButton(1, true);
		}
		else {
			field.enableButton(1, false);
		}
	}

	@Override
	public void doubleClicked(ListDialogField<ObjectParameter> field) {
		
	}

}
