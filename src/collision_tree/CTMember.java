/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collision_tree;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Represents a leaf of the collision tree.
 * Acts as a wrapper for any element you want to put in the tree
 * 
 * 
 * @param <E> The type of the object you wish to add to the tree
 * 
 * @version 0.5
 * @author vtleavs
 */
public class CTMember<E>
{
    private Point location;
    private CTNode parent;
    private E element;
    
    /**
     * 
     * @param element The element associated with this member
     * @param location The location of this member in 2D space
     */
    public CTMember(E element, Point location)
    {
        this.location = location;
        this.element = element;
    }
    
    /**
     * Move this member to a new location in the grid.
     * 
     * @param location The location in the grid to move this member to
     */
    public void moveTo(Point location)
    {
        if(parent.inSector(location))
            this.location = location;
        else
            parent.moveTo(this, location);
    }
    
    /**
     * Get all members within a certain radius of this member.
     * Very useful for collision detection.
     * 
     * @param radius The radius in which to seek
     * @return The members in the radius
     */
    public ArrayList<CTMember> getMembersInRadius(int radius)
    {
        ArrayList<CTMember> members = parent.getMembersInRadius(this.location, radius);
        members.remove(this);

        return members;
    }
    
    /**
     * 
     * @return The parent node of this member
     */
    public CTNode getParent() {
        return parent;
    }
    
    /**
     * 
     * @param parent The parent node of this member
     */
    public void setParent(CTNode parent)
    {
        this.parent = parent;
    }
    
    /**
     * 
     * @return The element associated with this member
     */
    public E getElement() {
        return element;
    }
    
    /**
     * 
     * @param element The element associated with this member
     */
    public void setElement(E element) {
        this.element = element;
    }

    /**
     * 
     * @return The location of this member in 2D space
     */
    public Point getLocation() {
        return location;
    }

    /**
     * 
     * @param location The location of this member in 2D space
     */
    public void setLocation(Point location) {
        this.location = location;
    }
}
