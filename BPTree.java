import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    
    /**
     * Public constructor
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        // TODO : Complete
        this.branchingFactor = branchingFactor;
        root = new LeafNode();
    }
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
        // TODO : Complete
    	//System.out.println("Inserting " + key + "...");
    	root.insert(key, value);
    }
    
    
    /**
     * Gets the values that satisfy the given range 
     * search arguments.
     * 
     * Value of comparator can be one of these: 
     * "<=", "==", ">="
     * 
     * Example:
     *     If given key = 2.5 and comparator = ">=":
     *         return all the values with the corresponding 
     *      keys >= 2.5
     *      
     * If key is null or not found, return empty list.
     * If comparator is null, empty, or not according
     * to required form, return empty list.
     * 
     * @param key to be searched
     * @param comparator is a string
     * @return list of values that are the result of the 
     * range search; if nothing found, return empty list
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        // TODO : Complete
               
        //find leaf
        LeafNode startingLeaf; 
        startingLeaf = findStartingLeaf(key, root);
        //System.out.println("Found starter leaf: " + startingLeaf);
        
        if (comparator.contentEquals(">=")){
        	return rangeSearchHelperGreaterThanOrEquals(key, startingLeaf);
        }
        
        if (comparator.contentEquals("==")){
        	return rangeSearchHelperEqualsEquals(key, startingLeaf);
        }
        
        if (comparator.contentEquals("<=")){
        	return rangeSearchHelperLessThanOrEquals(key, startingLeaf);
        }
        
        return null;
    }
    
    @SuppressWarnings("unchecked")
	private LeafNode findStartingLeaf(K key, Node node) {
    	Node foundChild = null;
    	
    	// if leaf node, we found the starter node
    	if (node.getType().equals("LeafNode")) {
    		//System.out.println("Found starter leaf: " + node);
    		return (LeafNode) node;
    	}
    	
    	else {
    		int i = node.keys.size() - 1;
        	boolean found = false;
        	int foundIndex = 0;
        	
        	// convert node to Internal Node
        	InternalNode internalNode = (InternalNode) node;
        	
        	// Find the child which is going to have the new key 
        	// if the first key of the child is greater that key to add, move down the list
            while (i >= 0 && !found) {
            	//handle root
            	K largestRootKey = internalNode.keys.get(node.keys.size() - 1);
            	if (key.compareTo(largestRootKey) > 0) {
            		foundIndex = internalNode.children.size() - 1;
            		found = true;
            	}
            	
            	for (int j = internalNode.children.size() - 1; j >= 0 && !found; j--) {
            		//System.out.println("Child for comparison is " + internalNode.children.get(j).getFirstLeafKey());
            		if (internalNode.children.get(j).getFirstLeafKey().compareTo(key) <= 0) {
            			found = true;
            			//System.out.println("Found index = " + j);
            			foundIndex = j;
            		}
            	}
            	
            	if (!found) {
            		i--;
            	}
            }
            
            foundChild = internalNode.children.get(foundIndex);
            return findStartingLeaf(key, foundChild);
            
    	}

    }
    
    @SuppressWarnings("unchecked")
	private List<V> rangeSearchHelperEqualsEquals(K key, LeafNode childNode){
    	ArrayList<V> result = new ArrayList<V>();	

    	K currentKey = childNode.getFirstLeafKey();
		int i = 0;
		boolean found = false;
				
		//check if we need to evaluate the former node
		if (childNode.previous != null) {
    		K prevNodeLargestKey = childNode.previous.keys.get(childNode.previous.keys.size()-1);
    		if (prevNodeLargestKey.equals(key)) {
    			// if we get here, we need to evaluate the previous node
    			//find starting point
    			currentKey = childNode.previous.getFirstLeafKey();
    			childNode = childNode.previous;
        		while (key.compareTo(currentKey) > 0 ) {
        			i++;
        			currentKey = childNode.keys.get(i);
        		}
    		}
        		
    		// process the first leaf
    		for (int j = i; j < childNode.keys.size(); j++) {
    			currentKey = childNode.keys.get(j);
    			if (key.compareTo(currentKey) == 0) {
    				ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
    				for (V m : valuesToAdd) {
    					result.add(m);
    				}
    			}
    		}
    		
		} // end if
		
		else {
		 	//find starting point
    		while (key.compareTo(currentKey) > 0  && i < (childNode.keys.size() - 1)) {
    			i++;
    			currentKey = childNode.keys.get(i);
    		}
    			    		
    		// process the first leaf
	    	for (int j = i; j < childNode.keys.size(); j++) {
	    		currentKey = childNode.keys.get(j);
	    		if (key.compareTo(currentKey) == 0) {
	    			//System.out.println(childNode.values.get(j));
	    			ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
    				for (V m : valuesToAdd) {
    					result.add(m);
    				}
	    		}
	    	}
		} // end else
		
		//process the rest of the leaves
		while (childNode.next != null && !found) {
			childNode = childNode.next;
			// add the values
			for (int k = 0; k < childNode.keys.size(); k++) {
				if (childNode.keys.get(k).equals(key)) {
					ArrayList<V> valuesToAdd = childNode.leafValues.get(k);
    				for (V m : valuesToAdd) {
    					result.add(m);
    				}
				}
				
				else {
					found = true;
				}
    		} 
   		}
		return result;

    	
    }
    
	private List<V> rangeSearchHelperGreaterThanOrEquals(K key, LeafNode childNode){
    		ArrayList<V> result = new ArrayList<V>();	

    		K currentKey = childNode.getFirstLeafKey();
    		int i = 0;
    		
    		//check if we need to evaluate the former node
    		if (childNode.previous != null) {
	    		K prevNodeLargestKey = childNode.previous.keys.get(childNode.previous.keys.size()-1);
	    		if (prevNodeLargestKey.equals(key)) {
	    			// if we get here, we need to evaluate the previous node
	    			//find starting point
	    			currentKey = childNode.previous.getFirstLeafKey();
	    			childNode = childNode.previous;
	        		while (key.compareTo(currentKey) < 0 ) {
	        			i++;
	        			currentKey = childNode.keys.get(i);
	        		}
	    		}
	        		
	        		// process the first leaf
	    			//System.out.println("i = " + i);
	        		for (int j = i; j < childNode.keys.size(); j++) {
	        			currentKey = childNode.keys.get(j);
	        			if (key.compareTo(currentKey) <= 0) {
	        				ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
	        				for (V m : valuesToAdd) {
	        					result.add(m);
	        				}
	        			}
	        		}
    		} // end if
    		
    		else {
    		 	//find starting point
	    		while (key.compareTo(currentKey) > 0  && i < (childNode.keys.size() - 1)) {
	    			i++;
	    			currentKey = childNode.keys.get(i);
	    		}
	    			    		
	    		// process the first leaf
		    	for (int j = i; j < childNode.keys.size(); j++) {
		    		currentKey = childNode.keys.get(j);
		    		if (key.compareTo(currentKey) <= 0) {
		    			ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
	    				for (V m : valuesToAdd) {
	    					result.add(m);
	    				}
		    		}
		    	}
    		} // end else
    		
    		//process the rest of the leaves
    		while (childNode.next != null) {
    			childNode = childNode.next;
    			// add the values
    			for (int k = 0; k < childNode.keys.size(); k++) {
    				ArrayList<V> valuesToAdd = childNode.leafValues.get(k);
    				for (V m : valuesToAdd) {
    					result.add(m);
    				}
        		} 
       		}
    		return result;
    }
    
    @SuppressWarnings("unchecked")
	private List<V> rangeSearchHelperLessThanOrEquals(K key, LeafNode childNode){
    	ArrayList<V> result = new ArrayList<V>();	

		K currentKey = childNode.getFirstLeafKey();
		int i = 0;
		
		//check if we need to evaluate the next node
		while (childNode.next != null && childNode.next.getFirstLeafKey().equals(key)) {
   			childNode = childNode.next;
    		}
        		
		// process the first leaf
		//System.out.println("i = " + i);
		for (int j = i; j < childNode.keys.size(); j++) {
			currentKey = childNode.keys.get(j);
			if (key.compareTo(currentKey) >= 0) {
				ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
				for (V m : valuesToAdd) {
					result.add(m);
				}
			}
		}
		
		//process the rest of the leaves
		while (childNode.previous != null) {
			childNode = childNode.previous;
			// add the values
			for (int k = 0; k < childNode.keys.size(); k++) {
				ArrayList<V> valuesToAdd = childNode.leafValues.get(k);
				for (V m : valuesToAdd) {
					result.add(m);
				}
    		} 
   		}
		return result;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @SuppressWarnings("unchecked")
	@Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;
        
        /**
         * Package constructor
         */
        Node() {
            // TODO : Complete
        	keys = new ArrayList<K>();
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String getType() {
        	return this.getType();
        }
        
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();
            // TODO : Complete
            children = new ArrayList<Node>();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // TODO : Complete
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // TODO : Complete
            return (keys.size() >= branchingFactor);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        @SuppressWarnings("unchecked")
		void insert(K key, V value) {
            // TODO : Complete
        	
        	//start at the farthest right index
        	int i = keys.size() - 1;

        	boolean found = false;
        	int foundIndex = 0;
        	
        	// Find the child which is going to have the new key 
        	// if the first key of the child is greater that key to add, move down the list

            while (i >= 0 && !found) {
            	//handle root
            	K largestRootKey = this.keys.get(this.keys.size() - 1);
            	if (key.compareTo(largestRootKey) > 0) {
            		foundIndex = this.children.size() - 1;
            		found = true;
            	}
            	
            	for (int j = children.size() - 1; j >= 0 && !found; j--) {
            		//System.out.println("Child for comparison is " + this.children.get(j).getFirstLeafKey());
            		if (this.children.get(j).getFirstLeafKey().compareTo(key) <= 0) {
            			found = true;
            			//System.out.println("Found index = " + j);
            			foundIndex = j;
            		}
            	}
            	
            	if (!found) {
            		i--;
            	}
            }
            
            Node foundChild = this.children.get(foundIndex);

			foundChild.insert(key, value);
        	
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key) {
            // TODO : Complete
        	keys.add(key);
        	this.bubbleSort();
        	
        	if (isOverflow()) {
        		this.split();
        	}
        	
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        @SuppressWarnings("unchecked")
		Node split() {
        	//System.out.println("Performing internal split on " + this);
        	int splitIndex = branchingFactor / 2;
        	InternalNode left = new InternalNode();
        	InternalNode current = this;
        	K keyToPromote = keys.get(splitIndex);
        	//System.out.println("Key to promote it: " + keyToPromote);
        	InternalNode parent;
        	
        	//
        	// find the parent for use later
        	//
        	
        	// handle the case where a new parent is needed
        	if (this == root) {
        		//System.out.println("Creating new parent");
        		InternalNode newRoot = new InternalNode();
        		// update new root to add children
        		newRoot.children.add(left);
        		newRoot.children.add(current);
            	// sort the children
            	//parent.bubbleSortList(parent.children);
        		root = newRoot;
        		parent = (InternalNode) root;
        	}
        	
        	else {
        		parent = findParent((InternalNode) root, this);
        	}
        	
        	//System.out.println("Parent = " + parent);
        	
        	//
        	// create the new left node
        	//
        	for (int i = 0; i < splitIndex; i++) {
        		left.keys.add(current.keys.get(i));
        	}
        	
        	// update the current node
        	// remove the values sent to the new node using tmp array
        	for (int i = 0; i <= splitIndex; i++) {
        		//remove the first node each time
        		current.keys.remove(0);
        	}
        	
        	//System.out.println("left = " + left);
        	//System.out.println("current = " + current);
        	
        	// update the children
        	ArrayList<Node>leftChildren = new ArrayList<Node>();
        	
        	//System.out.println("Evalualting children re-assignment in Internal split");
        	//System.out.println("Current = " + current);
        	//System.out.println("Current children = " + current.children);
        	
        	//need to split the children array between the two new nodes
        	int countToRemove = 0; // for every node we add to left child, we will want to
        	                       // remove it from the existing children Array
        	for (int i = 0; i < current.children.size()/2; i++) {
        		//if (children.get(i).getFirstLeafKey().compareTo(keyToPromote) < 0) {
        			leftChildren.add(children.get(i));
        			countToRemove++;        			
        		//}
        	}
        	
        	// remove from the existing children arrayList
        	for (int i = 0; i < countToRemove; i++) {
        		current.children.remove(0);
        	}
        	
        	// add leftChildren to the left node
        	left.children = leftChildren;
        	
        	//update the parents for left children
        	//System.out.println("Evaluating the update of children and parents...");
        	//System.out.println("Left = " + left);
        	//System.out.println("Left children = " + left.children);
        	for (int i = 0; i < left.children.size(); i++) {
        		//System.out.println(left.children.get(i).getType());
        		if (left.children.get(i).getType().equals("LeafNode")) {
        			((LeafNode) left.children.get(i)).setParent(left);
        			//System.out.println("Internal node split - parent update completed for child " + left.children.get(i) + " to parent " + left);
        		}
        	}
        	
        	//System.out.println("left children = " + left.children);
        	//System.out.println("current children= " + current.children);
        	
        	//sort the children
        	left.bubbleSortList(left.children);
        	current.bubbleSortList(current.children);
        	
        	//
        	// promote the key
        	//
        	/*     	
        	System.out.println("Right before insert");
        	System.out.println("parent = " + parent);
        	System.out.println("left = " + left);
        	System.out.println("current = " + current);
        	System.out.println("From internal: left children " + left.children);
        	System.out.println("From internal: current children " + current.children);
        	System.out.println("From internal: parent children " + parent.children);
			*/
        	
        	// insert the promoted key into the parent
        	//System.out.println("bubble sort time");
        	if (!parent.children.get(0).equals(left)) {
        		parent.children.add(left);
        		parent.bubbleSortList(parent.children);
        	}
        	parent.insert(keyToPromote);
        	
        	/*
        	System.out.println("parent = " + parent);
        	System.out.println("left = " + left);
        	System.out.println("current = " + current);
        	System.out.println("From internal: left children " + left.children);
        	System.out.println("From internal: current children " + current.children);
        	*/
        	
            return left;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
            return null;
        }
        
        public String getType() {
        	return "InternalNode";
        }
        
        private void bubbleSort() {
        	//System.out.println("Calling bubbleSort (InternalNode) on " + this);
			int remaining = keys.size() - 1;
		      for(int x = 0; x < (keys.size()-1); x++) {
		         for(int y = 0; y < (remaining); y++) {
		            K tmpKey;
		            if (keys.get(y).compareTo(keys.get(y+1)) > 0) {
		              tmpKey =  keys.get(y+1); 
		              keys.set(y+1, keys.get(y));
		              keys.set(y, tmpKey);
		            }
		            
		         }
		         remaining--;
		      }
        }
        
        @SuppressWarnings("unchecked")
		private void bubbleSortList(List<BPTree<K, V>.Node> children2) {
        	//System.out.println("Calling bubbleSortList (InternalNode) on this " + this);
        	//System.out.println("Calling bubbleSortList (InternalNode) on children2 " + children2);
        	int remaining = children2.size() - 1;
		      for(int x = 0; x < (children2.size()-1); x++) {
		         for(int y = 0; y < (remaining); y++) {
		            Node tmp;
		            
		            if (children2.get(y).getFirstLeafKey().compareTo(children2.get(y+1).getFirstLeafKey()) > 0) {
		              tmp =  children2.get(y+1); 
		              children2.set(y+1, children2.get(y));
		              children2.set(y, tmp);
		            }
		            
		         }
		         remaining--;
		      }
		      
		      //System.out.println("Children2 after bubble sort: " + children2);
        }
        
        @SuppressWarnings("unchecked")
		private InternalNode findParent(InternalNode current, InternalNode childNode) {
        	InternalNode parent = null;
        	for (int i = 0; i < current.children.size(); i++) {
        		if (current.children.get(i).equals(childNode)) {
        			return current;
        		}
        	}
        	
        	if (current.children.get(0).getType().equals("InternalNode")) {
	        	for (int i = 0; i < current.children.size() && parent == null; i++) {
	        		parent = findParent((InternalNode) current.children.get(i), childNode);
	        	}
        	}
        	
        	return parent;
        }
    
    } // End of class InternalNode
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        private List<V> values;
        
        // Reference to the next leaf node
        private LeafNode next;
        
        // Reference to the previous leaf node
        private LeafNode previous;
        
        // Reference to the parent node
        private InternalNode parent;
        
        // All the value arrays for the leaf
        private ArrayList<ArrayList<V>> leafValues;
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();
            // TODO : Complete
            previous = null;
        	next = null;
        	values = new ArrayList<V>();
        	leafValues = new ArrayList<ArrayList<V>>();
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            // TODO : Complete
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // TODO : Complete
            return (keys.size() >= branchingFactor);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
            // TODO : Complete
        	//System.out.println("Inside leafNode insert. Inserting key: " + key + ". Node before insert is " + this);
        	//System.out.println("Inside leafNode insert. Inserting key: " + key + ". Parent before insert is " + this.getParent());
        	
        	//
        	// handle duplicate keys
        	//
        	LeafNode currentNode = this;
        	boolean isDuplicate = false;
        	
        	// check the current node
        	if (currentNode.containsKey(key)) {
        		//we have a duplicate in this key
        		isDuplicate = true;
        	} 
        	
        	// check the next node(s)
        	else if (currentNode.next != null && currentNode.next.containsKey(key)) {
        		//we have a duplicate in this key
        		currentNode = currentNode.next;
        		isDuplicate = true;
        	}
        	
        	// check the next node(s)
        	else if (currentNode.previous != null && currentNode.previous.containsKey(key)) {
        		//we have a duplicate in this key
        		currentNode = currentNode.previous;
        		isDuplicate = true;
        	}
        	
        	// handle duplicate insertion
        	if (isDuplicate) {
	        	ArrayList<V> valuesForKey = leafValues.get(keyIndex(key)); //find the right values index
	        	valuesForKey.add(value);
        	}
        	
        	else {
        		//this key is node a duplicate
        		//create a values ArrayList
        		ArrayList<V> valuesForKey = new ArrayList<V>();
        		valuesForKey.add(value);
        		currentNode.leafValues.add(valuesForKey);
        		currentNode.keys.add(key);
            	currentNode.bubbleSort();
            	//System.out.println("Inside leafNode insert. Inserting key: " + key + ". Node after insert is " + this);
            	
            	if (currentNode.isOverflow()) {
                	//System.out.println("Inside leafNode insert. Split required");
            		currentNode.split();
            	}
        	}
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        @SuppressWarnings("unchecked")
		Node split() {
            // TODO : Complete
        	//System.out.println("Performing leaf split on " + this);
        	int splitIndex = branchingFactor / 2;
        	LeafNode left = new LeafNode();
        	LeafNode current = this;
        	K keyToPromote = keys.get(splitIndex);
        	//System.out.println("Key to promote is: " + keyToPromote);
        	InternalNode parent;
        	
        	//
        	// promote the key
        	//
        	
        	// handle the case where no parent exists
        	if (current.getParent() == null) {
        		//System.out.println("Creating new parent");
        		InternalNode newParent = new InternalNode();
        		root = newParent;
        		current.setParent(newParent);
        		newParent.children.add(current);
        	}
        	
        	//System.out.println("Current before inserting: " + current);
        	//System.out.println("Parent before inserting: " + current.parent);
        	
        	// parent exists at this point
        	parent = current.getParent();
        	//System.out.println("The parent is " + parent);
        	
        	//
        	// create the new left node
        	//
        	for (int i = 0; i < splitIndex; i++) {
        		left.keys.add(current.keys.get(i));
        		left.leafValues.add(current.leafValues.get(i));
        	}
        	//System.out.println("Left is: " + left);
        	
        	// update the current node
        	// remove the values sent to the new node using tmp array
        	ArrayList<K> tmpKeys = new ArrayList<K>();
        	ArrayList<ArrayList<V>> tmpVals = new ArrayList<ArrayList<V>>();
        	for (int i = splitIndex; i < keys.size(); i++) {
        		tmpKeys.add(current.keys.get(i));
        		tmpVals.add(current.leafValues.get(i));
        	}
        	
        	// update current to temp array
        	current.setKeys(tmpKeys);
        	current.setValues(tmpVals);

        	
        	// create parent pointers for new left node
        	left.setParent(parent);
        	parent.children.add(left);
        	// sort the children
        	parent.bubbleSortList(parent.children);
        	
        	//insert the promoted key into the parent
        	parent.insert(keyToPromote);     	
        	
        	// create the rest of the pointers for new left node
        	left.setNext(current);
        	left.setPrevious(current.getPrevious());

        	//handle current.previous if exists
        	if (current.getPrevious() != null) {
        		current.getPrevious().setNext(left);
        	}
        	
        	// update pointers for current
        	current.setPrevious(left);
        	
            return left;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            // TODO : Complete
        	
        	ArrayList<V> result = new ArrayList<V>();
        	K currentKey;
        	
        	if (comparator.equals("==")) {
        		// process the first leaf
        		for (int j = 0; j < this.keys.size(); j++) {
        			currentKey = this.keys.get(j);
        			if (key.compareTo(currentKey) == 0) {
        				result.add(this.values.get(j));
        			}
        		}
        	}
        	
            return result;
        }
        
        private boolean containsKey(K key) {
            // TODO : Complete

        	K currentKey;

    		for (int j = 0; j < this.keys.size(); j++) {
    			currentKey = this.keys.get(j);
    			if (key.compareTo(currentKey) == 0) {
    				return true;
    			}
    		}
    	        	
            return false;
        }
        
        private int keyIndex (K key) {

        	K currentKey;

    		for (int j = 0; j < this.keys.size(); j++) {
    			currentKey = this.keys.get(j);
    			if (key.compareTo(currentKey) == 0) {
    				return j;
    			}
    		}
    	        	
            return -1;
        }

		private List<K> getKeys() {
			return keys;
		}


		private void setKeys(List<K> keys) {
			this.keys = keys;
		}
		
		private ArrayList<ArrayList<V>> getValues() {
			return leafValues;
		}


		private void setValues(ArrayList<ArrayList<V>> leafValues) {
			this.leafValues = leafValues;
		}


		private LeafNode getNext() {
			return next;
		}


		private void setNext(LeafNode next) {
			this.next = next;
		}


		private LeafNode getPrevious() {
			return previous;
		}


		private void setPrevious(LeafNode previous) {
			this.previous = previous;
		}


		private InternalNode getParent() {
			return parent;
		}


		private void setParent(InternalNode parent) {
			this.parent = parent;
		}
		
		public String getType() {
			return "LeafNode";
		}
		
		private void bubbleSort() {
			//System.out.println("Calling bubbleSort (LeafNode) on " + this);
			int remaining = keys.size() - 1;
		      for(int x = 0; x < (keys.size()-1); x++) {
		         for(int y = 0; y < (remaining); y++) {
		            K tmpKey;
		            ArrayList<V> tmpVal;

		            if (keys.get(y).compareTo(keys.get(y+1)) > 0) {
		              tmpKey =  keys.get(y+1); 
		              keys.set(y+1, keys.get(y));
		              keys.set(y, tmpKey);
		              
		              //update values to retain sort order of keys
		              tmpVal = leafValues.get(y+1);
		              leafValues.set(y+1, leafValues.get(y));
		              leafValues.set(y, tmpVal);
		            }

		         }
		         remaining--;
		      }
		}
        
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty BPTree with branching factor of 3
        //BPTree<Double, Double> BPTree = new BPTree<>(3);
        
        /*
        BPTree.insert(0.2,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.0,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.5,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.0,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.2,0.0);
        BPTree.insert(0.0,0.0);
        BPTree.insert(0.5,0.0);
        BPTree.insert(0.5,0.0);
        BPTree.insert(0.2,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.5,0.0);
        BPTree.insert(0.5,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.5,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.8,0.0);
        BPTree.insert(0.5,0.0);
        System.out.println(BPTree);
        BPTree.insert(0.5,0.0);
        System.out.println(BPTree);
        */
        /*
        BPTree.insert(0.2, 10.0);
        //System.out.println(BPTree);
        BPTree.insert(0.5, 20.0);
        //System.out.println(BPTree);
        BPTree.insert(0.0, 5.0);
        //System.out.println(BPTree);
        BPTree.insert(0.2, 7.0);
        //System.out.println(BPTree);
        BPTree.insert(0.2, 10.0);
        //System.out.println(BPTree);
        BPTree.insert(0.2, 30.0);
        //System.out.println(BPTree);
        BPTree.insert(0.8, 8.0);
        //System.out.println(BPTree);
        BPTree.insert(0.2, 9.0);
        //System.out.println(BPTree);
        BPTree.insert(0.8, 11.0);
        //System.out.println(BPTree);
        BPTree.insert(0.8, 6.0);
        //System.out.println(BPTree);
        BPTree.insert(0.8, 35.0);
        System.out.println(BPTree);
        BPTree.insert(0.5, 45.0);
        //System.out.println(BPTree);
        //BPTree.insert(12.0, 55.0);
        //System.out.println(BPTree);
        //BPTree.insert(2.0, 65.0);
        //System.out.println(BPTree);
        //BPTree.insert(8.0, 4.0);
        //System.out.println(BPTree);
        //BPTree.insert(0.0, 3.0);
        //System.out.println(BPTree);
        //BPTree.insert(6.0, 10.0);
        //System.out.println(BPTree);
        //BPTree.insert(7.0, 20.0);
        System.out.println(BPTree);
        //BPTree.insert(1.0, 5.0);
        //System.out.println(BPTree);
        //BPTree.insert(1.0, 7.0);
        
        BPTree.insert(10.0, 10.0);
        BPTree.insert(30.0, 30.0);
        BPTree.insert(8.0, 8.0);
        BPTree.insert(9.0, 9.0);
        BPTree.insert(11.0, 11.0);
        BPTree.insert(55.0, 55.0);
        BPTree.insert(55.0, 55.0);
        BPTree.insert(6.0, 6.0);
        BPTree.insert(35.0, 35.0);
        BPTree.insert(45.0, 45.0);
        BPTree.insert(55.0, 55.0);
        BPTree.insert(65.0, 65.0);
        //System.out.println(BPTree);
        BPTree.insert(4.0, 4.0);
        BPTree.insert(3.0, 3.0);
        BPTree.insert(10.0, 10.0);
        BPTree.insert(10.0, 10.0);
        
        
        System.out.println(BPTree);
        List<Double> filteredValues = BPTree.rangeSearch(10.0d, "<=");
        System.out.println("Filtered values: " + filteredValues.toString());
        
        //System.out.println("Values tree: ");
        //BPTree.printTreeValues();
        */
        /*
        for (int i = 1 ; i < 1000 ; i++) {
        	double id = i/1.0;
        	BPTree.insert(id, id);
        	//System.out.println(BPTree);
        }
        
        System.out.println(BPTree);
        */
        /*
        System.out.println(BPTree);
        List<Double> filteredValues = BPTree.rangeSearch(10.0d, "<=");
        System.out.println("Filtered values: " + filteredValues.toString());
        */
        //System.out.println("Values tree: ");
        //BPTree.printTreeValues();
        
        
        
        // create a pseudo random number generator
        /*
        Random rnd1 = new Random();

        // some value to add to the BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the BPTree
        // does not ensure BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            BPTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + BPTree.toString());
        }
        List<Double> filteredValues = BPTree.rangeSearch(0.0d, ">=");
        System.out.println("Filtered values: " + filteredValues.toString());
        */
    }
	
} // End of class BPTree
