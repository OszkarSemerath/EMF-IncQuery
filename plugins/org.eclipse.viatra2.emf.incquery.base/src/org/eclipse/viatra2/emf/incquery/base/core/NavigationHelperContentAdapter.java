package org.eclipse.viatra2.emf.incquery.base.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EReferenceImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;

public class NavigationHelperContentAdapter extends EContentAdapter {

	// target -> ref -> source
	protected HashMap<EObject, HashMap<EReference, HashSet<EObject>>> refMap;

	// class -> value -> attribute -> holder
	protected HashMap<Object, HashMap<EAttribute, HashSet<EObject>>> attrMap;
	protected HashMap<EClass, HashSet<EObject>> instanceMap;
	private NavigationHelperImpl navigationHelper;
	
	public NavigationHelperContentAdapter(NavigationHelperImpl navigationHelper) {
		this.navigationHelper = navigationHelper;
		this.refMap = new HashMap<EObject, HashMap<EReference, HashSet<EObject>>>();
		this.attrMap = new HashMap<Object, HashMap<EAttribute, HashSet<EObject>>>();
		this.instanceMap = new HashMap<EClass, HashSet<EObject>>();
	}
	
	public HashMap<Object, HashMap<EAttribute, HashSet<EObject>>> getAttrMap() {
		return attrMap;
	}
	
	public HashMap<EClass, HashSet<EObject>> getInstanceMap() {
		return instanceMap;
	}
	
	public HashMap<EObject, HashMap<EReference, HashSet<EObject>>> getRefMap() {
		return refMap;
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);

		Object feature = notification.getFeature();

		if (feature instanceof EReference) {
			handleRefChange(notification, (EReferenceImpl) feature);
		}
		if (feature instanceof EAttribute) {
			handleAttrChange(notification, (EAttribute) feature);
		}
	}

	@SuppressWarnings("unchecked")
	public void handleRefChange(Notification notification, EReference ref) {
		EObject notifier = (EObject) notification.getNotifier();
		
		if (notification.getEventType() == Notification.REMOVE_MANY) {
			for (EObject oldValue: (Collection<? extends EObject>) notification.getOldValue()) {
				handleRefRemove(ref, oldValue, null, notifier);
			}
		}
		else if (notification.getEventType() == Notification.ADD_MANY) {
			for (EObject newValue: (Collection<? extends EObject>) notification.getNewValue()) {
				handleRefAdd(ref, newValue, notifier);
			}
		}
		else if (notification.getEventType() == Notification.ADD) {
			handleRefAdd(ref, (EObject) notification.getNewValue(), notifier);
		}
		else if (notification.getEventType() == Notification.REMOVE) {
			handleRefRemove(ref, (EObject) notification.getOldValue(), (EObject) notification.getNewValue(), notifier);
		}
		else if (notification.getEventType() == Notification.SET) {
			EObject oldValue = (EObject) notification.getOldValue();
			EObject newValue = (EObject) notification.getNewValue();
			
			if (oldValue != null) {
				removeRefTuple(ref, oldValue, notifier);
				removeInstanceTuple(oldValue.eClass(), oldValue);

				if (ref.isContainment())
					navigationHelper.getVisitor().visitObjectForEAttributeDelete(oldValue, navigationHelper.getObservedFeatures());
			}
			if (newValue != null) {
				handleRefAdd(ref, newValue, notifier);
			}
		}
	}

	public void handleRefAdd(EReference ref, EObject newValue, EObject notifier) {
		insertRefTuple(ref, newValue, notifier);
		insertInstanceTuple(newValue.eClass(), newValue);
		if (ref.isContainment()) {
			navigationHelper.getVisitor().visitObjectForEAttributeInsert(newValue, navigationHelper.getObservedFeatures());
		}
	}

	public void handleRefRemove(EReference ref, EObject oldValue,
			EObject newValue, EObject notifier) {
		removeRefTuple(ref, oldValue, notifier);
		removeInstanceTuple(oldValue.eClass(), oldValue);

		if (ref.isContainment()) {
			navigationHelper.getVisitor().visitObjectForEAttributeDelete(oldValue, navigationHelper.getObservedFeatures());
		}

		if (newValue != null) {
			handleRefAdd(ref, newValue, notifier);
		}
	}

	public void handleAttrChange(Notification notification, EAttribute feature) {
		Object oldValue = notification.getOldValue();
		Object newValue = notification.getNewValue();
		EObject notifier = (EObject) notification.getNotifier();

		if (notification.getEventType() == Notification.SET) {
			if (oldValue != null) {
				removeAttrTuple(feature, oldValue, notifier);
			}
			if (newValue != null) {
				insertAttrTuple(feature, newValue, notifier);
			}
		}
	}

	public void insertAttrTuple(EAttribute attr, Object value, EObject holder) {
		if ((navigationHelper.getType() == NavigationHelperType.ALL) || navigationHelper.getObservedFeatures().contains(attr)) {
			if (attrMap.containsKey(value)) {
				if (attrMap.get(value).containsKey(attr)) {
					attrMap.get(value).get(attr).add(holder);
				} else {
					HashSet<EObject> set = new HashSet<EObject>();
					set.add(holder);
					attrMap.get(value).put(attr, set);
				}
			} else {
				HashMap<EAttribute, HashSet<EObject>> map = new HashMap<EAttribute, HashSet<EObject>>();
				HashSet<EObject> set = new HashSet<EObject>();
				set.add(holder);
				map.put(attr, set);
				attrMap.put(value, map);
			}
		}
	}

	public void removeAttrTuple(EAttribute attr, Object value, EObject holder) {
		if ((navigationHelper.getType() == NavigationHelperType.ALL) || navigationHelper.getObservedFeatures().contains(attr)) {
			if (attrMap.containsKey(value)
					&& attrMap.get(value).containsKey(attr)) {
				attrMap.get(value).get(attr).remove(holder);

				if (attrMap.get(value).get(attr).size() == 0) {
					attrMap.get(value).remove(attr);
				}

				if (attrMap.get(value).size() == 0) {
					attrMap.remove(value);
				}
			}
		}

	}

	public void insertRefTuple(EReference ref, EObject target, EObject source) {
		if ((navigationHelper.getType() == NavigationHelperType.ALL) || navigationHelper.getObservedFeatures().contains(ref)) {
			if (refMap.containsKey(target)) {
				if (refMap.get(target).containsKey(ref)) {
					refMap.get(target).get(ref).add(source);
				} else {
					HashSet<EObject> set = new HashSet<EObject>();
					set.add(source);
					refMap.get(target).put(ref, set);
				}
			} else {
				HashMap<EReference, HashSet<EObject>> map = new HashMap<EReference, HashSet<EObject>>();
				HashSet<EObject> set = new HashSet<EObject>();
				set.add(source);
				map.put(ref, set);
				refMap.put(target, map);
			}
		}
	}

	public void removeRefTuple(EReference ref, EObject target, EObject source) {
		if ((navigationHelper.getType() == NavigationHelperType.ALL) || navigationHelper.getObservedFeatures().contains(ref)) {
			if (refMap.containsKey(target)
					&& refMap.get(target).containsKey(ref)) {
				refMap.get(target).get(ref).remove(source);

				if (refMap.get(target).get(ref).size() == 0)
					refMap.get(target).remove(ref);

				if (refMap.get(target).size() == 0)
					refMap.remove(target);
			}
		}
	}

	public void insertInstanceTuple(EClass key, EObject value) {
		if (navigationHelper.getType() == NavigationHelperType.ALL || navigationHelper.getObservedClasses().contains(key)) {
			if (instanceMap.containsKey(key)) {
				instanceMap.get(key).add(value);
			} else {
				HashSet<EObject> set = new HashSet<EObject>();
				set.add(value);
				instanceMap.put(key, set);
			}
		}
	}

	private void removeInstanceTuple(EClass key, EObject value) {
		if (navigationHelper.getType() == NavigationHelperType.ALL || navigationHelper.getObservedClasses().contains(key)) {
			if (instanceMap.containsKey(key)) {
				instanceMap.get(key).remove(value);
				if (instanceMap.get(key).size() == 0) {
					instanceMap.remove(key);
				}
			}
		}
	}
	
}