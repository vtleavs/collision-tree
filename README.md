# collision-tree
A data structure for efficient collision detection in a 2D space

## Description
This is a data structure to increase the efficiency of detecting collisions in 2-dimensional space.  It uses a quad-tree to store elements and their locations in the tree and allows you to move the elements around in space and to detect elements within in a certain radius from an element.

## How it works
A 2D-space quad-tree is essentially a binary search tree that instead of each node having two children, they each have four children.  Also, instead of representing a range of numbers, each node represents a region in 2D space which may or may not contain elements.  If a region does contain an element, they are stored in an List at the lowest possible branch node.  A branch node which is at the bottom of the tree is known as a leaf node, and these are where the elements are stored.

The following 2D space is divided into four and tesselated to an arbitrarily sized leaf-space:

![alt text](https://github.com/vtleavs/collision-tree/blob/master/images/Grid1.png)

This space is broken down into a quad-tree.  An example of one of these trees is shown below:

![alt text](https://github.com/vtleavs/collision-tree/blob/master/images/Tree.png)

![alt text](https://github.com/vtleavs/collision-tree/blob/master/images/Grid2.png)

Notice that the elements are contained within the leaf node.

Because of this structure, instead of having to poll every single element from every single element to calculate collision, it is only necessary to move up the tree just far enough to get the elements only within a given range.  This takes the calculation down from `Ω(n(n-1)/2)` which is about `θ(n^2)` to about `Ω(n)`.  Obviously if the range covers every element then it has a worst case of `O(n^2)`.

## Installing
Installing the library is very straightforward.  Download `CollisionTree.jar` from the releases page, navigate to your project properties and add it to your libraries

## Using `collision-tree`
Right now, I have only implemented the tree in Java, so the following tutorial is in Java.

To start, import the library:
```java
import collision_tree.CTTree;
import collision_tree.CTMember;
```
or
```java
import collision_tree.*;
```

The next step is to create the tree.  First, instantiate the tree:
```java
CTTree tree = new CTTree(depth, leafWidth, leafHeight);
```
`depth` is the number of levels the tree has.
`leafWidth` is the 2D width of a leaf node and `leafHeight` is the 2D height of the leaf node.

The size of the tree in 2D space will be `4^leafWidth x 4^leafHeight`.  Right now this doesn't give you a lot of control; I plan to make it more useful in future updates.

Now we have to add elements to the tree.  Before we can do this however, we need to put those elements into wrapper classes, `CTMember`.  This wrapper makes it so the tree can work efficiently regardless of the object type you give it.

We have our element, `object1`, and we then create a new `CTMember` and put it inside.
```java
int x, y = 10;
Object object1 = new Object();
Point2D location = new Point2D.Double(x, y);

CTMember<Object> object1Wrapper = new CTMember(object1, location);
```
Be sure to define the type of CTMember, in this case `CTMember<Object>`.  Then we add that to the tree:
```java
tree.addMember(object1Wrapper);
```
Note, there is no efficient way to get the wrapper class from the tree, so its best to either keep the wrapper handy, or to pass it to the element itself, which is what I recommend.

There, tree built!  Obviously its stupid to just have one element in the tree, so it makes sense to run the previous in a for loop.  In that case I recommend holding both the elements and the wrappers in a List.  Now all we have to do is figure out how to use the thing.

There are numerous methods you can utilize in the `CTTree` and `CTMember` classes.  The most commonly used ones are outlined below:

Method | Description
--- | ---
`new Tree(depth, leafWidth, leafHeight)` | Constructor for `CTTree`.
`tree.getAllMembers()` | Returns all of the members in the tree.  Runs in `θ(n)`.  Returns an `ArrayList<CTMember>`.
`tree.addMember(CTMember member)` | Adds a new member to the tree and propagates it down to be owned by the appropriate node.  Runs in `θ(1)`
`tree.removeMember(CTMember member)` | Finds the member based on location and removes the member from the node's `ArrayList`.  Runs in `θ(1)`
`new Member(element, location)` | Constructor for `CTMember`.
`member.moveTo(Point2D location)` | Changes position of element wrapper and relocates it in the tree as needed.  Runs in `θ(1)`
`member.getMembersInRadius(int radius)` | Returns all members within `radius` units from `member`.  Returns an `ArrayList<CTMember>`.  Runs in `θ(1)`
`member.getElement(E element)` | Returns the element associated with this member.  `element` is of type `E`, the type the member has been defined to have.
`member.getParent()` | Gets the node that owns `member`.  Returns a `CTNode`.

There are a number of other methods as well, check the javadocs for more info on those.

I will be showing you how to move elements around, and how to detect collisions between the elements.  We will be using the `Element` class, which is defined below:
```java
class Element {
  public Point2D location;
  public int sizeRadius;
  
  public Element(Point2D location, int sizeRadius) {
    this.location = location;
    this.sizeRadius = sizeRadius;
  }
}
```
Lets make ten `Element` instances and put them into the tree:
```java
ArrayList<CTMember> wrappers = new ArrayList<>();

for(int i = 0; i < 10; ++i) {
  Point2D location = new Point2D.Double(i*10, i*10);
  Element element = new Element(location, 5);
  CTMember wrapper = new CTMember(element, location);
  wrappers.add(wrapper);
  tree.addMember(wrapper);
}
```
Now that we have these ten elements in the tree, we can move them around and check collisions.
```java
while(!exitCase) {
  // for each element:
  for(int i = 0; i < wrappers.size(); ++i) {
    // make location in random direction 5 units from the member:
    CTMember wrapper = wrappers.get(i);
    Point2D updatedLocation = new Point2D.Double(
      wrapper.getLocation().getX() + Math.random()*10-5,
      wrapper.getLocation().getY() + Math.random()*10-5);
    
    //Move member to new location
    wrapper.moveTo(updatedLocation);
    
    // check to see if there are any collisions by checking to see if there are any members
    // within 2 times the radius of the element
    ArrayList<CTMember> collisions = wrapper.getMembersInRadius(10);
    
    if(collisions.size() > 0)
      System.out.println("Element " + i + " had a collision");
    
    // pause for 1 second for sanity's sake
    try { sleep(1000); } catch (InterruptedException ex) { }
  }
}
```
Full program:
```java
import collision_tree.CTTree;
import collision_tree.CTMember;
import java.util.ArrayList;
import java.awt.geom.Point2D;
import static java.lang.Thread.sleep;

class Element {
  public Point2D location;
  public int sizeRadius;
  
  public Element(Point2D location, int sizeRadius) {
    this.location = location;
    this.sizeRadius = sizeRadius;
  }
}

public class Main {
  public static void main(String[] args) {
    CTTree tree = new CTTree(4, 4, 4);
    ArrayList<CTMember> wrappers = new ArrayList<>();
    boolean exitCase = false;

    for(int i = 0; i < 10; ++i) {
      Point2D location = new Point2D.Double(i*10, i*10);
      Element element = new Element(location, 5);
      CTMember wrapper = new CTMember(element, location);
      wrappers.add(wrapper);
      tree.addMember(wrapper);
    }

    while(!exitCase) {
      // for each element:
      for(int i = 0; i < wrappers.size(); ++i) {
        // make location in random direction 5 units from the member:
        CTMember wrapper = wrappers.get(i);
        Point2D updatedLocation = new Point2D.Double(
          wrapper.getLocation().getX() + Math.random()*10-5,
          wrapper.getLocation().getY() + Math.random()*10-5);

        //Move member to new location
        wrapper.moveTo(updatedLocation);

        // check to see if there are any collisions by checking to see if there are any members
        // within 2 times the radius of the element
        ArrayList<CTMember> collisions = wrapper.getMembersInRadius(10);

        if(collisions.size() > 0)
          System.out.println("Element " + i + " had a collision");

        // pause for 1 second for sanity's sake
        try { sleep(1000); } catch (InterruptedException ex) { }
      }
    }
  }
}
```

## Developer
Please feel free to give me feedback on this project, and please submit bugs!  My website is ben.leavitts.us/programming.  Check out my other Github stuff as well!  If you want to see a working example of this code, check out my repository 'EvolutionGame'









