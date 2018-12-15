package application;
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
 * @author Kelly East (kgeast@wisc.edu), sapan (sapan@cs.wisc.edu)
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
        this.branchingFactor = branchingFactor;
        root = new LeafNode();
    }
    
    
    /**
     * Inserts the key and value in the appropriate nodes in the tree
     * 
     * Note: key-value pairs with duplicate keys can be inserted into the tree.
     * 
     * @param key
     * @param value
     */
    @Override
    public void insert(K key, V value) {
    	// don't except null input
    	if (key == null || value == null) {
    		return;
    	}
    	
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
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        // confirm we have a valid comparator
    	if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        
    	// confirm we have a valid key
    	if (key == null) {
    		return new ArrayList<V>();
    	}

        return root.rangeSearch(key, comparator);
    }
    
    /**
     * Returns a string representation for the tree
     * This method is provided to students in the implementation.
     * @return a string representation
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
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * Checks if the node is overloaded based on the BPTree branching factor
         * @return boolean
         */
        abstract boolean isOverflow();
        
        /**
        * Creates a string representation of the node
        */
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
     * @author kgeast, sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();
            children = new ArrayList<Node>();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
        	// return the key at index 0
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
        	// overflow occurs if the size of keys is equal or larger to
        	// the branching factor
            return (keys.size() >= branchingFactor);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
        	
        	if (key == null || value == null) {
        		// don't continue with null input
        		return;
        	}
        	
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
            		if (this.children.get(j).getFirstLeafKey().compareTo(key) <= 0) {
            			found = true;
            			foundIndex = j;
            		}
            	}
            	
            	if (!found) {
            		i--;
            	}
            }
            
            Node foundChild = this.children.get(foundIndex);
            
            // perform the insert into this key
			foundChild.insert(key, value);
        	
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key) {

        	if (key == null) {
        		// don't continue with null input
        		return;
        	}
        	
        	// insert the key, then sort the node
        	keys.add(key);
        	this.bubbleSort();
        	
        	// check for overflow, if yes, split
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
        	// split location is half branching factor
        	int splitIndex = branchingFactor / 2; 
        	
        	// create a new node
        	InternalNode left = new InternalNode();
        	
        	// track the existing node
        	InternalNode current = this;
        	
        	// determine what key will move up in the tree
        	K keyToPromote = keys.get(splitIndex);
        	
        	//
        	// find the parent for use later
        	//
        	
        	InternalNode parent;
        	
        	// handle the case where a new parent is needed
        	if (this == root) {
        		InternalNode newRoot = new InternalNode();
        		// update new root to add children
        		newRoot.children.add(left);
        		newRoot.children.add(current);
        		root = newRoot;
        		parent = (InternalNode) root;
        	}
        	
        	else {
        		// parent is not the root, find the parent
        		parent = findParent((InternalNode) root, this);
        	}
        	
        	//
        	// create the new left node
        	//
        	for (int i = 0; i < splitIndex; i++) {
        		// add keys
        		left.keys.add(current.keys.get(i));
        	}
        	
        	// update the current node
        	// remove the values sent to the new node using tmp array
        	for (int i = 0; i <= splitIndex; i++) {
        		//remove the first node each time
        		current.keys.remove(0);
        	}

        	// update the children
        	ArrayList<Node>leftChildren = new ArrayList<Node>();

        	//need to split the children array between the two new nodes
        	int countToRemove = 0; // for every node we add to left child, we will want to
        	                       // remove it from the existing children Array
        	for (int i = 0; i < current.children.size()/2; i++) {
       			leftChildren.add(children.get(i));
       			countToRemove++;        			
        	}
        	
        	// remove from the existing children arrayList
        	for (int i = 0; i < countToRemove; i++) {
        		current.children.remove(0);
        	}
        	
        	// add leftChildren to the left node
        	left.children = leftChildren;
        	
        	//update the parents for left children
        	for (int i = 0; i < left.children.size(); i++) {
        		if (getType(left.children.get(i)).equals("LeafNode")) {
        			((LeafNode) left.children.get(i)).setParent(left);
        		}
        	}
        	
        	//sort the children
        	left.bubbleSortList(left.children);
        	current.bubbleSortList(current.children);
        	
        	//
        	// promote the key
        	//
        	
        	// insert the promoted key into the parent
        	if (!parent.children.get(0).equals(left)) {
        		parent.children.add(left);
        		parent.bubbleSortList(parent.children);
        	}
        	parent.insert(keyToPromote);
        	
            return left;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	
        	// find appropriate leaf
            LeafNode startingLeaf; 
            startingLeaf = findStartingLeaf(key, this);
            
            // call into the leafNode search function
            return startingLeaf.rangeSearch(key, comparator);
        }
        
        /**
         * Find the initial leaf to evaluate for range search
         * Once the initial leaf is found, check if previous and next leaves
         * need to be evaluated as well
         * 
         * @param key
         * @param node
         * returns: LeafNode
         */
        @SuppressWarnings("unchecked")
    	private LeafNode findStartingLeaf(K key, Node node) {
        	if (key == null || node == null) {
        		// don't continue with null input
        		return null;
        	}
        	
        	Node foundChild = null;
        	
        	// if leaf node, we found the starter node
        	if (getType(node).equals("LeafNode")) {
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
                	// handle root
                	K largestRootKey = internalNode.keys.get(node.keys.size() - 1);
                	if (key.compareTo(largestRootKey) > 0) {
                		foundIndex = internalNode.children.size() - 1;
                		found = true;
                	}
                	
                	// continue comparing to children of the node
                	for (int j = internalNode.children.size() - 1; j >= 0 && !found; j--) {
                		if (internalNode.children.get(j).getFirstLeafKey().compareTo(key) <= 0) {
                			found = true;
                			foundIndex = j;
                		}
                	}
                	
                	if (!found) {
                		// if we didn't find the right leaf, decrement i to look at next node's children
                		i--;
                	}
                }
                
                foundChild = internalNode.children.get(foundIndex);
                return findStartingLeaf(key, foundChild);
                
        	}

        }
        
        /**
         * Returns the specific type of node - either internal or leaf
         * 
         * @param node
         * returns: String associated with type of node
         */
        private String getType(Node node) {
        	// InternalNode
        	if (node instanceof BPTree.InternalNode) {
        		return "InternalNode";
        	}
        	
        	// LeafNode
        	if (node instanceof BPTree.LeafNode) {
        		return "LeafNode";
        	}
        	
        	// Else
        	else return "Node";
        }
        
        /**
         * Performs a bubble sort on the node to put the keys in order
         */
        private void bubbleSort() {
			int remaining = keys.size() - 1;
		      for(int x = 0; x < (keys.size()-1); x++) {
		         for(int y = 0; y < (remaining); y++) {
		            K tmpKey;
		            // determine if a swap is needed
		            if (keys.get(y).compareTo(keys.get(y+1)) > 0) {
		              // perform the swap
		              tmpKey =  keys.get(y+1); 
		              keys.set(y+1, keys.get(y));
		              keys.set(y, tmpKey);
		            }
		            
		         }
		         remaining--;
		      }
        }
        
        /**
         * Performs a bubble sort on a list of nodes to put in order based on the first
         * leaf key
         */
        private void bubbleSortList(List<BPTree<K, V>.Node> childrenToSort) {
        	
        	if (childrenToSort == null) {
        		return;
        	}
        	
        	// process the list
        	int remaining = childrenToSort.size() - 1;
		      for(int x = 0; x < (childrenToSort.size()-1); x++) {
		         for(int y = 0; y < (remaining); y++) {
		            Node tmp;
		            
		            // determine if a swap is needed
		            if (childrenToSort.get(y).getFirstLeafKey().compareTo
		            		(childrenToSort.get(y+1).getFirstLeafKey()) > 0) {
		              
		              // perform the swap
		              tmp =  childrenToSort.get(y+1); 
		              childrenToSort.set(y+1, childrenToSort.get(y));
		              childrenToSort.set(y, tmp);
		            }
		            
		         }
		         remaining--;
		      }		      
        }
        
        /**
         * Finds the parent of a given internal node
         * 
         * @param current
         * @param childNode
         */
        @SuppressWarnings("unchecked")
		private InternalNode findParent(InternalNode current, InternalNode childNode) {
        	
        	if (current == null || childNode == null) {
        		return null;
        	}
        	
        	InternalNode parent = null;
        	
        	// determine if we are already in the parent node
        	for (int i = 0; i < current.children.size(); i++) {
        		if (current.children.get(i).equals(childNode)) {
        			return current;
        		}
        	}
        	
        	// check that the node is internal, evaluate children to see if they match
        	// the node being evaluated and this is the parent node
        	if (getType(current.children.get(0)).equals("InternalNode")) {
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
     * @author kgeast, sapan
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
        	// return key at index 0
            return keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            // Overflow occurs if size is equal or greater than the 
        	// branching factor
            return (keys.size() >= branchingFactor);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
        	
        	if (key == null || value == null) {
        		// don't continue with null input
        		return;
        	}
        	
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
        	else if (currentNode.getNext() != null && currentNode.getNext().containsKey(key)) {
        		//we have a duplicate in this key
        		currentNode = currentNode.getNext();
        		isDuplicate = true;
        	}
        	
        	// check the previous node(s)
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
            	
            	if (currentNode.isOverflow()) {
                	// if overflow, we need to perform a split
            		currentNode.split();
            	}
        	}
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            // setup
        	
        	// find where we need to split
        	int splitIndex = branchingFactor / 2;
        	
        	// create new left node
        	LeafNode left = new LeafNode();
        	
        	// track the current node
        	LeafNode current = this;
        	
        	// find the key to move up the tree
        	K keyToPromote = keys.get(splitIndex);
        	
        	// create a node to track parent
        	InternalNode parent;
        	
        	//
        	// promote the key
        	//
        	
        	// handle the case where no parent exists
        	if (current.getParent() == null) {
        		InternalNode newParent = new InternalNode();
        		
        		// update root and children
        		root = newParent;
        		current.setParent(newParent);
        		newParent.children.add(current);
        	}

        	// parent exists at this point
        	parent = current.getParent();
        	
        	//
        	// create the new left node
        	//
        	for (int i = 0; i < splitIndex; i++) {
        		// add the keys and values
        		left.keys.add(current.keys.get(i));
        		left.leafValues.add(current.leafValues.get(i));
        	}
        	
        	// update the current node
        	// remove the values sent to the new node using temp array
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
        	
        	ArrayList<V> result = new ArrayList<V>();
        	
        	if (key == null || comparator == null) {
        		// don't continue with empty input
        		return result;
        	}

        	if (comparator.equals("==")) {
        		return rangeSearchHelperEqualsEquals(key, this);
        	} // end if
        	
        	else if (comparator.equals(">=")) {
        		return rangeSearchHelperGreaterThanOrEquals(key, this);
        	} // end if
        	
        	else if (comparator.equals("<=")) {
        		return rangeSearchHelperLessThanOrEquals(key, this);
        	} // end if
        	
            return result;
        }
        
        /**
         * Performs the range search for the == comparator
         * Evaluates leaf nodes beginning with the node passed in to find all keys the match 
         * the key parameter and returns a list of their values
         * 
         * @param key
         * @param childNode
         * returns: List<V> of values associated with keys equal to the passed in key
         */
        private List<V> rangeSearchHelperEqualsEquals(K key, LeafNode childNode){
        	ArrayList<V> result = new ArrayList<V>();	
        	
        	if (key == null || childNode == null) {
        		// don't continue with null input
        		return result;
        	}
        	
        	K currentKey = childNode.getFirstLeafKey();
    		int i = 0;
    		boolean found = false;
    				
    		// check if we need to evaluate the former node
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
        					// add all values from the valuesToAdd list to result
        					result.add(m);
        				}
        			}
        		}
        		
    		} // end if
    		
    		else {
    		 	// find starting point
        		while (key.compareTo(currentKey) > 0  && i < (childNode.keys.size() - 1)) {
        			i++;
        			currentKey = childNode.keys.get(i);
        		}
        			    		
        		// process the first leaf
    	    	for (int j = i; j < childNode.keys.size(); j++) {
    	    		currentKey = childNode.keys.get(j);
    	    		if (key.compareTo(currentKey) == 0) {
    	    			// if we found it, add to the result list
    	    			ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
        				for (V m : valuesToAdd) {
        					result.add(m);
        				}
    	    		}
    	    	}
    		} // end else
    		
    		// process the rest of the leaves to look for matches to key
    		while (childNode.getNext() != null && !found) {
    			childNode = childNode.getNext();
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
        
        /**
         * Performs the range search for the >= comparator
         * Evaluates leaf nodes beginning with the node passed in to find all keys the match 
         * the key parameter or are greater and returns a list of their values
         * 
         * @param key
         * @param childNode
         * returns: List<V> of values associated with keys equal to 
         * or greater than the passed in key
         */
    	private List<V> rangeSearchHelperGreaterThanOrEquals(K key, LeafNode childNode){
        		ArrayList<V> result = new ArrayList<V>();		
            	
            	if (key == null || childNode == null) {
            		// don't continue with null input
            		return result;
            	}
        		
        		K currentKey = childNode.getFirstLeafKey();
        		int i = 0;
        		
        		// check if we need to evaluate the former node
        		if (childNode.previous != null) {
    	    		K prevNodeLargestKey = childNode.previous.keys.get(childNode.previous.keys.size()-1);
    	    		if (prevNodeLargestKey.equals(key)) {
    	    			// if we get here, we need to evaluate the previous node
    	    			// find starting point
    	    			currentKey = childNode.previous.getFirstLeafKey();
    	    			childNode = childNode.previous;
    	        		while (key.compareTo(currentKey) < 0 ) {
    	        			i++;
    	        			currentKey = childNode.keys.get(i);
    	        		}
    	    		}
    	        		
    	        		// process the first leaf
    	        		for (int j = i; j < childNode.keys.size(); j++) {
    	        			currentKey = childNode.keys.get(j);
    	        			if (key.compareTo(currentKey) <= 0) {
    	        				ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
    	        				for (V m : valuesToAdd) {
    	        					// add values to result
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
        		while (childNode.getNext() != null) {
        			childNode = childNode.getNext();
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
        
        /**
         * Performs the range search for the <= comparator
         * Evaluates leaf nodes beginning with the node passed in to find all keys the match 
         * the key parameter or are less than and returns a list of their values
         * 
         * @param key
         * @param childNode
         * returns: List<V> of values associated with keys equal to 
         * or less than the passed in key
         */
        private List<V> rangeSearchHelperLessThanOrEquals(K key, LeafNode childNode){
        	ArrayList<V> result = new ArrayList<V>();	
        	
        	if (key == null || childNode == null) {
        		// don't continue with null input
        		return result;
        	}
        	
    		K currentKey = childNode.getFirstLeafKey();
    		int i = 0;
    		
    		// check if we need to evaluate the next node
    		while (childNode.getNext() != null && childNode.getNext().getFirstLeafKey().equals(key)) {
       			childNode = childNode.getNext();
        		}
            		
    		// process the first leaf
    		for (int j = i; j < childNode.keys.size(); j++) {
    			currentKey = childNode.keys.get(j);
    			if (key.compareTo(currentKey) >= 0) {
    				ArrayList<V> valuesToAdd = childNode.leafValues.get(j);
    				for (V m : valuesToAdd) {
    					result.add(m);
    				}
    			}
    		}
    		
    		// process the rest of the leaves
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
        
        /**
         * Check the leaf to see if it contains the key
         * Return true if so, else, false
         * 
         * @param key
         * @return true if the leaf contains the key
         */
        private boolean containsKey(K key) {
        	
        	if (key == null) {
        		// don't continue with null input
        		return false;
        	}
        	
        	K currentKey;

    		for (int j = 0; j < this.keys.size(); j++) {
    			currentKey = this.keys.get(j);
    			if (key.compareTo(currentKey) == 0) {
    				return true;
    			}
    		}
    	        	
            return false;
        }
        
        /**
         * Called when we know the leaf contains the key
         * Returns what index the key is located at in the leaf
         * 
         * @param key
         * @return the index the key is located at 
         */
        private int keyIndex (K key) {
        
        	if (!this.containsKey(key)) {
        		// this leaf doesn't actually contain the key
        		System.out.println("Cannot call contains key method on a leaf that "
        				+ "does not contain the key");
        		return -1;
        	}
        	
        	K currentKey;
        	
        	// loop through to find the index
    		for (int j = 0; j < this.keys.size(); j++) {
    			currentKey = this.keys.get(j);
    			if (key.compareTo(currentKey) == 0) {
    				return j;
    			}
    		}
    	    
    		// return -1 as a default. Should not hit this
            return -1;
        }
        
        //
        // getters and setters
        //
        
        /**
         * Getter for keys
         * @return keys
         */
		private List<K> getKeys() {
			return keys;
		}

		/**
         * Setter for keys
         * @param keys
         */
		private void setKeys(List<K> keys) {
			this.keys = keys;
		}
		
		/**
         * Getter for values list
         * @return leafValues
         */
		private ArrayList<ArrayList<V>> getValues() {
			return leafValues;
		}

		/**
         * Setter for leafValues
         * @param leafValues
         */
		private void setValues(ArrayList<ArrayList<V>> leafValues) {
			this.leafValues = leafValues;
		}

		/**
         * Getter for next
         * @return next
         */
		private LeafNode getNext() {
			return next;
		}

		/**
         * Setter for next
         * @param next
         */
		private void setNext(LeafNode next) {
			this.next = next;
		}

		/**
         * Getter for previous
         * @return previos
         */
		private LeafNode getPrevious() {
			return previous;
		}

		/**
         * Setter for previous
         * @param previous
         */
		private void setPrevious(LeafNode previous) {
			this.previous = previous;
		}

		/**
         * Getter for parent
         * @return parent
         */
		private InternalNode getParent() {
			return parent;
		}

		/**
         * Setter for parent
         * @param parent
         */
		private void setParent(InternalNode parent) {
			this.parent = parent;
		}
		
		/**
         * Performs a bubble sort on the node to put the keys in order
         * Also orders the associated values list
         */
		private void bubbleSort() {
			
			int remaining = keys.size() - 1;
		      for(int x = 0; x < (keys.size()-1); x++) {
		         for(int y = 0; y < (remaining); y++) {
		            K tmpKey;
		            ArrayList<V> tmpVal;
		            
		            // determine if a swap is needed
		            if (keys.get(y).compareTo(keys.get(y+1)) > 0) {
		              // perform the swap on keys
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
        // BPTree<Double, Double> BPTree = new BPTree<>(3);
        
    	/*
        BPTree.insert(1.0, 1.0);
        BPTree.insert(2.0, 2.0);
        System.out.println(BPTree);
        List<Double> filteredValues = BPTree.rangeSearch(1.0d, "<=");
        System.out.println("Filtered values: " + filteredValues.toString());
        */
    	
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
