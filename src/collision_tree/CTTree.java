/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collision_tree;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Collision Tree.
 * 
 * Structure used to store objects efficiently in a 2D grid.  Also allows moving
 * objects around and collision detection both with great efficiency.
 * 
 * 
 * Runtime of moving elements is about (n^2)/500M
 * Runtime of collision detection is about (n^2)/10M
 * Runtime of move + collision check is about (n^2)/5M
 * 
 * 
 * @author vtleavs
 */
public class CTTree
{
    private int treeDepth = 6;
    private int leafNodeWidth = 16; // leafNodeWidth units per leafNode width
    private int leafNodeHeight = 9; // leafNodeHeight units per leafNode height
    
    private CTNode head = new CTNode(null, null, treeDepth, leafNodeHeight, null);
    
    /**
     * Constructor for a tree.
     * The size of the tree in space is as follows:
     * 
     * Width = leafNodeWidth * (4^treeDepth)
     * Height = leafNodeHeight * (4^treeDepth)
     * 
     * 
     * @param treeDepth The number of layers in the tree
     * @param leafNodeWidth Width of a leaf node in 2D space
     * @param leafNodeHeight Height of a leaf node in 2D space
     */
    public CTTree(int treeDepth, int leafNodeWidth, int leafNodeHeight)
    {
        this.treeDepth = treeDepth;
        head = new CTNode(new Point(0, 0), gridSize(), treeDepth, 0, null);
        this.leafNodeWidth = leafNodeWidth;
        this.leafNodeHeight = leafNodeHeight;
    }
    
    /**
     * Add a member to the tree.
     * 
     * The member will be propagated down the tree based on location.
     * 
     * @param member The member to be added
     */
    public void addMember(CTMember member)
    {
        head.addMember(member);
    }
    
    /**
     * Removes a member to the tree.
     * 
     * The will be removed from the tree.  It will not be destroyed.
     * 
     * @param member The member to be removed
     */
    public void removeMember(CTMember member)
    {
        head.removeMember(member);
    }
         
    /**
     * Get dimensions of the grid in 2D space.
     * 
     * Width = leafNodeWidth * (4^treeDepth)
     * Height = leafNodeHeight * (4^treeDepth)
     * 
     * @return The Dimensions of the grid in 2D space
     */
    public Dimension gridSize()
    {
        int width = leafNodeWidth * (int)(Math.pow(2, treeDepth));
        int height = leafNodeHeight * (int)(Math.pow(2, treeDepth));
        return new Dimension(width, height);
    }
    
    /**
     * Get all the members in the tree
     * 
     * @return All the members in the tree
     */
    public ArrayList<CTMember> getAllMembers()
    {
        return head.getAllMembers();
    }
    
    /**
     * Get area of the grid in 2D space.
     * 
     * Width = leafNodeWidth * (4^treeDepth)
     * Height = leafNodeHeight * (4^treeDepth)
     * 
     * @return The area occupied by the grid
     */
    public int getGridArea()
    {
        return (int)(gridSize().getWidth()*2);
    }

    /**
     * @return The head node of the tree
     */
    public CTNode getHead() {
        return head;
    }

    /**
     * 
     * @return Height of a leaf node in 2D space
     */
    public int getLeafNodeHeight() {
        return leafNodeHeight;
    }

    /**
     * 
     * @return Width of a leaf node in 2D space
     */
    public int getLeafNodeWidth() {
        return leafNodeWidth;
    }

    /**
     * 
     * @return Layer depth of the tree
     */
    public int getTreeDepth() {
        return treeDepth;
    }

    /**
     * 
     * @param leafNodeHeight Height of a leaf node in 2D space
     */
    public void setLeafNodeHeight(int leafNodeHeight) {
        this.leafNodeHeight = leafNodeHeight;
    }

    /**
     * 
     * @param leafNodeWidth Width of a leaf node in 2D space
     */
    public void setLeafNodeWidth(int leafNodeWidth) {
        this.leafNodeWidth = leafNodeWidth;
    }

    /**
     * 
     * @param treeDepth Layer depth of the tree
     */
    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }    
}
