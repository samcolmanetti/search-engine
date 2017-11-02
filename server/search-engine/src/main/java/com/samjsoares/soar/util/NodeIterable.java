package com.samjsoares.soar.util;

import java.util.*;

import org.jsoup.nodes.Node;


/**
 * Performs a depth-first traversal of a jsoup Node.
 */
public class NodeIterable implements Iterable<Node> {

    private Node root;

    /**
     * Creates an iterable starting with the given Node.
     *
     * @param root
     */
    public NodeIterable(Node root) {
        this.root = root;
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator(root);
    }

    /**
     * Inner class that implements the Iterator.
     *
     * @author downey
     *
     */
    private class NodeIterator implements Iterator<Node> {

        // this stack keeps track of the Nodes waiting to be visited
        Stack<Node> stack;

        /**
         * Initializes the Iterator with the root Node on the stack.
         *
         * @param node
         */
        public NodeIterator(Node node) {
            stack = new Stack<>();
            stack.push(root);
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Node next() {
            // if the stack is empty, we're done
            if (stack.isEmpty()) {
                throw new NoSuchElementException();
            }

            // otherwise pop the next Node off the stack
            Node node = stack.pop();
            //System.out.println(node);

            // push the children onto the stack in reverse order
            List<Node> nodes = new ArrayList<Node>(node.childNodes());
            Collections.reverse(nodes);
            for (Node child: nodes) {
                stack.push(child);
            }
            return node;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}