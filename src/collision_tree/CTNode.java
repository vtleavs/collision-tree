/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collision_tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author vtleavs
 */
public class CTNode
{
    private Dimension sectorSize;
    private Point sectorLocation;
    private int branchHeight;
    
    private boolean leafNode = false;
    
    private CTNode branchA;
    private CTNode branchB;
    private CTNode branchC;
    private CTNode branchD;
    
    private CTNode parent;
    
    private ArrayList<CTMember> members;
    
    public CTNode(Point sectorLocation, Dimension sectorSize, int treeHeight, int branchHeight, CTNode parent)
    {
        this.sectorSize = sectorSize;
        this.sectorLocation = sectorLocation;
        this.branchHeight = branchHeight;
        
        this.parent = parent;
        
        if(branchHeight + 1 < treeHeight)
        {
            branchA = new CTNode(
                    new Point((int)(sectorLocation.getX()), (int)(sectorLocation.getY())), 
                    new Dimension((int)sectorSize.getWidth()/2, (int)sectorSize.getHeight()/2), 
                    treeHeight, 
                    branchHeight + 1,
                    this
            );
            branchB = new CTNode(
                    new Point((int)(sectorLocation.getX() + sectorSize.getWidth()/2), 
                        (int)(sectorLocation.getY())), 
                    new Dimension((int)sectorSize.getWidth()/2, (int)sectorSize.getHeight()/2), 
                    treeHeight, 
                    branchHeight + 1,
                    this
            );
            branchC = new CTNode(
                    new Point((int)(sectorLocation.getX()), 
                        (int)(sectorLocation.getY() + sectorSize.getHeight()/2)), 
                    new Dimension((int)sectorSize.getWidth()/2, (int)sectorSize.getHeight()/2), 
                    treeHeight, 
                    branchHeight + 1,
                    this
            );
            branchD = new CTNode(
                    new Point((int)(sectorLocation.getX() + sectorSize.getWidth()/2), 
                        (int)(sectorLocation.getY() + sectorSize.getHeight()/2)), 
                    new Dimension((int)sectorSize.getWidth()/2, (int)sectorSize.getHeight()/2), 
                    treeHeight, 
                    branchHeight + 1,
                    this
            );
        }
        else
        {
            leafNode = true;
            members = new ArrayList<>();
        }
    }
    
    public void addMember(CTMember member)
    {
        if(leafNode)
        {
            member.setParent(this);
            members.add(member);
        }
        else
            selectNode(member.getLocation()).addMember(member);
    }
    
    public void removeMember(CTMember member)
    {
        if(leafNode)
            members.remove(member);
        else
            selectNode(member.getLocation()).removeMember(member);
    }
    
    public CTNode selectNode(Point location)
    {
        if(location.getX() < sectorLocation.getX() + sectorSize.getWidth()/2
                && location.getX() < sectorLocation.getY() + sectorSize.getHeight()/2)
            return branchA;
        else if(location.getX() >= sectorLocation.getX() + sectorSize.getWidth()/2
                && location.getX() < sectorLocation.getY() + sectorSize.getHeight()/2)
            return branchB;
        else if(location.getX() < sectorLocation.getX() + sectorSize.getWidth()/2
                && location.getX() >= sectorLocation.getY() + sectorSize.getHeight()/2)
            return branchC;
        else if(location.getX() >= sectorLocation.getX() + sectorSize.getWidth()/2
                && location.getX() >= sectorLocation.getY() + sectorSize.getHeight()/2)
            return branchD;
        return null;
    }
    
    public boolean inSector(Point location)
    {
        return location.getX() > sectorLocation.getX()
                && location.getX() < sectorLocation.getX() + sectorSize.getWidth()
                && location.getY() > sectorLocation.getY()
                && location.getY() < sectorLocation.getY() + sectorSize.getHeight();
    }
    
    public ArrayList<CTMember> getMembersInRadius(Point location, int radius)
    {
        ArrayList<CTMember> result = new ArrayList<>();
        
        if(leafNode)
        {
            for(int i = 0; i < members.size(); ++i)
            {
                CTMember temp = members.get(i);
                if(pointInRadius(location, temp.getLocation(), radius))
                    result.add(temp);
            }
            
            return result;
        }
        else if (nodeInRadius(location, radius))
            return parent.getMembersInRadius(location, radius);
        
        
        result.addAll(branchA.getMembersInRadius(location, radius));
        result.addAll(branchB.getMembersInRadius(location, radius));
        result.addAll(branchC.getMembersInRadius(location, radius));
        result.addAll(branchD.getMembersInRadius(location, radius));
        
        return result;
    }
    
    private boolean pointInRadius(Point center, Point point, int radius)
    {
        return Math.sqrt( Math.pow(point.x - center.x, 2) + Math.pow(point.y - center.y, 2)) <= radius;
    }
    
     private boolean nodeInRadius(Point location, int radius)
    {       
        int angleRadius = (int)Math.sqrt(Math.pow(radius, 2) / 2);
        Point n = new Point(location.x, location.y - radius);
        Point ne = new Point(location.x + angleRadius, location.y - angleRadius);
        Point e = new Point(location.x + radius, location.y);
        Point se = new Point(location.x + angleRadius, location.y + angleRadius);
        Point s = new Point(location.x, location.y + radius);
        Point sw = new Point(location.x - angleRadius, location.y + angleRadius);
        Point w = new Point(location.x - radius, location.y);
        Point nw = new Point(location.x - angleRadius, location.y - angleRadius);
        
        return (inSector(n) && inSector(ne) && inSector(e) && inSector(se) 
                && inSector(s) && inSector(sw) && inSector(w) && inSector(nw));
    }   
    
    public void moveTo(CTMember member, Point to)
    {
        if(inSector(to) || this.branchHeight == 0)
        {
            this.removeMember(member);
            member.setLocation(to);
            this.addMember(member);
        }
        else
            parent.moveTo(member, to);
    }

    public ArrayList<CTMember> getAllMembers() 
    {
        if(leafNode)
            return members;
        
        ArrayList<CTMember> arl = new ArrayList<>();
        arl.addAll(branchA.getAllMembers());
        arl.addAll(branchB.getAllMembers());
        arl.addAll(branchC.getAllMembers());
        arl.addAll(branchD.getAllMembers());
        return arl;
    }

    public CTNode getBranchA() {
        return branchA;
    }

    public CTNode getBranchB() {
        return branchB;
    }

    public CTNode getBranchC() {
        return branchC;
    }

    public CTNode getBranchD() {
        return branchD;
    }

    public int getBranchHeight() {
        return branchHeight;
    }

    public ArrayList<CTMember> getMembers() {
        return members;
    }

    public CTNode getParent() {
        return parent;
    }

    public Point getSectorLocation() {
        return sectorLocation;
    }

    public Dimension getSectorSize() {
        return sectorSize;
    }

    public void setParent(CTNode parent) {
        this.parent = parent;
    }

    public void setSectorLocation(Point sectorLocation) {
        this.sectorLocation = sectorLocation;
    }

    public void setSectorSize(Dimension sectorSize) {
        this.sectorSize = sectorSize;
    }
}
