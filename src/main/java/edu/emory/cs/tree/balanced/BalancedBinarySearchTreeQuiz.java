/*
 * Copyright 2020 Emory University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.cs.tree.balanced;
import edu.emory.cs.tree.BinaryNode;

/** @author Jinho D. Choi */
public class BalancedBinarySearchTreeQuiz<T extends Comparable<T>> extends AbstractBalancedBinarySearchTree<T, BinaryNode<T>> {
    @Override
    public BinaryNode<T> createNode(T key) {
        return new BinaryNode<>(key);
    }
    @Override
    protected void balance(BinaryNode<T> node) {
        BinaryNode<T> parent = node.getParent();
        BinaryNode<T> grandparent = node.getGrandParent();
        BinaryNode<T> uncle = node.getUncle();
        if (grandparent != null && parent != null && uncle != null
                && !parent.hasBothChildren()
                && grandparent.getRightChild() == parent
                && !uncle.hasBothChildren()) {
            rotation(node);
        }
    }
    protected BinaryNode<T> rotation(BinaryNode<T> node) {
        BinaryNode<T> gp = node.getGrandParent();
        BinaryNode<T> parent = node.getParent();
        BinaryNode<T> uncle = node.getUncle();
        BinaryNode<T> newRoot = gp;
        if (node.getUncle().hasLeftChild()) {
            if (node.getParent().hasLeftChild()) {
                rotateRight(parent);
                rotateLeft(gp);
                rotateRight(gp);
            } else if (node.getParent().hasRightChild()) {
                rotateLeft(gp);
                rotateRight(gp);
            }
        } else {
            if (node.getUncle().hasRightChild()) {
                if (node.getParent().hasLeftChild()) {
                    rotateRight(parent);
                    rotateLeft(uncle);
                    rotateLeft(gp);
                    rotateRight(gp);
                } else if (node.getParent().hasRightChild()) {
                    rotateLeft(gp);
                    rotateLeft(uncle);
                    rotateRight(gp);
                }
            }
        }
        // set new root after rotations
        if (newRoot.getLeftChild() == null) {
            newRoot.setRightChild(null);
            newRoot = newRoot.getLeftChild();
        } else if (newRoot.getRightChild() == null) {
            newRoot.setLeftChild(null);
            newRoot = newRoot.getRightChild();
        }
        return newRoot;
    }
}