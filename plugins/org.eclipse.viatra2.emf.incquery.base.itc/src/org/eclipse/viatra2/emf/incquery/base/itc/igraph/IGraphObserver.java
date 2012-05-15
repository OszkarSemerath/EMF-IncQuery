package org.eclipse.viatra2.emf.incquery.base.itc.igraph;

import java.io.Serializable;

/**
 * Interface GraphObserver is used to observ the changes in a graph; edge and node insertion/deleteion.
 * 
 * @author Tamas Szabo
 *
 */
public interface IGraphObserver<V> extends Serializable {
	
	/**
	 * Used to notify when an edge is inserted into the graph. 
	 * 
	 * @param source the source of the edge
	 * @param target the target of the edge
	 */
	public void edgeInserted(V source, V target);
	
	/**
	 * Used to notify when an edge is deleted from the graph.
	 * 
	 * @param source the source of the edge
	 * @param target the target of the edge
	 */
	public void edgeDeleted(V source, V target);
	
	/**
	 * Used to notify when a node is inserted into the graph.
	 * 
	 * @param n the node
	 */
	public void nodeInserted(V n);
	
	/**
	 * Used to notify when a node is deleted from the graph.
	 * 
	 * @param n the node
	 */
	public void nodeDeleted(V n);
}
