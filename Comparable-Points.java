/*
Comparable-points.java

Violeta Solorio's
October 27,2022

The program calculates the closest pairs of points through a series of overloaded methods
*/
import java.util.Arrays;
import java.util.Comparator;
import java.util.*;
import java.lang.Math;
public class Main {
    public static void main(String args[]) {
        long start = System.currentTimeMillis();
        
     Point [] points = new Point[100];
     Double max = 100.0;
     
     for( int i=0; i < points.length;i++){
         points[i] = new Point(Math.random() * max, Math.random()*max); //Initializes the points with the random x and y coordinate
     }
    
    System.out.println("Points sorted on Y-coordinates");
    Arrays.sort(points, new CompareY());//Sorts the points based on their y coordinates
    
    Pair p = Pair.getClosestPair(points);
    
    System.out.println("The shortest distance is" + p.getDistance() +"between the points"+ p);
    long end = System.currentTimeMillis();
    long totalTime = start-end;
    System.out.println("The execution time spent on the divide and conquer system is" + totalTime + "milliseconds");
    }
}
class Point implements Comparable<Point>{

    public double x;
    public double y;
    //Constructor
    public Point(double x, double y){
        this.x=x;
        this.y=y;
    }
    public int compareTo( Point p2){//The compareTo method compares the points and returns 1 if it is greater than or -1 if it is less than or zero if the points are equal
        if(x > p2.x){
            return 1;
        }else if(x < p2.x){//First x values are compared
            return -1;
        }else if(y > p2.y){
            return 1;
        }else if( y< p2.y){//Y values are compared if x values are equal
            return -1;
        }
        return 0;//Returns 0 if points are equal
    }
    public String toString(){
        return x +" , "+y;
    }
}
class CompareY implements Comparator<Point>{
    
    public int compare(Point one, Point two){//The compare method compares the points and returns 1 if it is greater than or -1 if it is less than or zero if the points are equal
        if( one.y > two.y){//First y values are compared
            return 1;
        }else if( one.y < two.y){
            return -1;
        }else if(one.x > two.x){//C values are compared if y values are equal
            return 1;
        }else if(one.x< two.x){
            return -1;
        }
        return 0;//Returns zero if points are equal
    }
    
}
class Pair extends Main{

    public Point p1;
    public Point p2;
    //Constructor
    public Pair(Point p1, Point p2){
        this.p1=p1;
        this.p2=p2;
    }
    public String toString(){
        return p1 +" "+p2;
    }
    public double getDistance(){
        return distance(p1,p2);
    }
    
    public static double distance(Point p1, Point p2){
        return distance(p1.x,p1.y,p2.x,p2.y);//Converts Point 1 and Point 2 to their X & Y Components & returns the distance
    }
    public static double distance(double x1, double y1, double x2, double y2){//Helper method 
        return Math.sqrt(Math.pow(x2-x1,2)+ Math.pow(y2-y1,2));//Calculates the distance of the points using the distance
    }
    /** Return the distance of the closest pair of points **/
    public static Pair getClosestPair(double [ ] [ ]  points){ 
        Point[] returnPoints = new Point[points.length];
        for( int i= 0; i < points.length;i++){
           returnPoints[i]= new Point(points[i][0], points[i][1]);
        }
        
        return getClosestPair(returnPoints);
    }
    public static ArrayList getLower(Point[] points){//Breaks down s into subset1 containing the mid point & returns an arraylist
        ArrayList<Point> s1 = new ArrayList<>();
        int size = points.length;
        int mid = size/2;
        int count=0;
        for( Point p : points){
            if(count<=mid){
                s1.add(p);
            }
        }
        return s1;
    }
    public static ArrayList getUpper(Point[] points){//Breaks down S into subset2 excluding the mid point & returns an arraylist
        ArrayList<Point> s2 = new ArrayList<>();
        int totalSize = points.length;
        int mid = totalSize/2;
        int count=0;
        for(Point p : points){
            if(count>mid){
                s2.add(p);
            }
        }
        return s2;
    }
    /** Return the distance of the closest pair of points */
    public static Pair getClosestPair(Point [ ]  points){
        Arrays.sort(points);
        Point[] yPoints = points.clone();
        Arrays.sort(yPoints, new CompareY());//Sorts the points based on the Y coordinates
        return distance(points,0,points.length-1,yPoints);
    }
    
    /** Return the distance of the closest pair of points in pointsOrderedOnX[low, high]. **/
    public static Pair distance(Point [ ] pointsOrderedOnX, int low, int high, Point [ ] pointsOrderedOnY){
        
        int mid = (high+low)/2;
        double pointDistance;
        if( low < high){
            return null;
        }else if(low+1 ==high){
            Pair p = new Pair(pointsOrderedOnX[low], pointsOrderedOnX[high]);//If condition is true, only two points remain which is what is returned
            return p;
        }
         Pair left = distance(pointsOrderedOnX, low, mid, pointsOrderedOnY);
         Pair right = distance(pointsOrderedOnX,mid+1, high, pointsOrderedOnY);
         
         Pair point=null;
            if(left == null & right ==null){//Both subsets are not valid pairs
                pointDistance = 0;
            }else if( right ==null){//Left takes precedense since right is null
                pointDistance = left.getDistance();
                point = left;
            }
            else if(left == null){//Right takes precedense since left is null
                pointDistance = right.getDistance();
                point = right;
            }
            else{
                if( left.getDistance()> right.getDistance()){//Point is set to right if right has a smaller distance
                point = right;
                pointDistance = right.getDistance();
                }
                else{//Point is set to left if left has a smaller distance
                    point=left;
                    pointDistance = left.getDistance();
                }
            }
          
            List<Point> leftStrip = new ArrayList<>();
            List<Point> rightStrip = new ArrayList<>();
            for( int i=0; i < pointsOrderedOnY.length;i++){
                if(pointsOrderedOnY[i].x <= pointsOrderedOnX[mid].x){//Checks that the x value is less than or equal to the mid value
                    if(pointsOrderedOnX[mid].x- pointsOrderedOnY[i].x <= pointDistance){//Checks that the value of the points is less than the closest point distance
                    leftStrip.add(pointsOrderedOnY[i]);
                    }
                }else if(pointsOrderedOnY[i].x > pointsOrderedOnX[mid].x){//Checks that the point is greater than the mid value
                    if( pointsOrderedOnY[i].x- pointsOrderedOnX[mid].x<=pointDistance){//Checks that the value of the points is less than the closest point distance
                        rightStrip.add(pointsOrderedOnY[i]);
                    }
                }
            }

               int r = 0;  // r is the index of a point in rightStrip
         
                for (int i=0; i < leftStrip.size();i++) {
                    while (r < rightStrip.size() && rightStrip.get(r).y <= leftStrip.get(i).y - pointDistance){ //The while loop iterates through the rightStrip to dismiss points below this condition: p.y-d
                        r++;
                    }
                        int r1 = r;//After skipping the points, r1 will be used to continue the iterating through the subsets
                        while(r1< rightStrip.size() && (rightStrip.get(r1).y - leftStrip.get(i).y) <= pointDistance){//Checks that r1 is not greater than rightStrip size and that the points are not greater the closest point distance
                        
                        if (distance(leftStrip.get(i), rightStrip.get(r1)) < pointDistance) {// Check if (leftStrip.get(i), rightStrip.get(r1) is a possible closest pair   
        
                            pointDistance = distance(leftStrip.get(i), rightStrip.get(r1));
                            point = new Pair(leftStrip.get(i),rightStrip.get(r1));
                            }
                        r1++;//Adds one to r1
                    }
                }
        return point; //Returns closest pairs
    }
}