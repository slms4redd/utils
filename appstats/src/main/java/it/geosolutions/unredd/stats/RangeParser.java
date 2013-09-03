/*
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.unredd.stats;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author DamianoG
 *
 * This class hold the concept of Range in Integer Numbers. 
 * This class is used to decouple the stats definition from custom statistical framework ranges (f.e. we don't want use here JAI classes)
 * The user can create finite or infinite ranges with positive or negative Integer Numbers passing to the constructor a String that represents the math notation for Intervals
 * 
 * see http://en.wikipedia.org/wiki/Interval_(mathematics) and http://en.wikipedia.org/wiki/ISO_31-11
 * 
 * Syntax:
 *      &lt;digit&lt;          := 0 | 1 | 2 | ... | 9
 *      &lt;number&lt;         := &lt;digit&lt; | &lt;digit&lt;&lt;number&lt;
 *      &lt;leftInclusion&lt;  := ( | [
 *      &lt;rightInclusion&lt; := ) | ]
 *      &lt;rangeElement&lt;   := null | &lt;digit&lt;
 *      &lt;range&lt;          := &lt;leftInclusion&lt;&lt;rangeElement&lt;;&lt;rangeElement&lt;&lt;rightInclusion&lt;    
 * 
 * examples: 
 *      (;0]    "take all numbers less than 0"
 *      (0;10]  "take all numbers between 1 and 10"
 *      (10;)   "take all numbers greater than 10"
 */
public class RangeParser {

    /**
     * The pattern used to check the validity of the interval as String
     */
    private static final String INTERVAL_PATTERN = "(\\[|\\()-?(\\d*);-?(\\d)*(\\]|\\))";
    
    /**
     * The left inclusive endpoint of the range
     * Integer.MIN_VALUE must be considered by client code as "negative infinite"
     */
    private int leftEndPoint;
    
    /**
     * The right inclusive endpoint of the range
     * Integer.MAX_VALUE must be considered by client code as "positive infinite"
     */    
    private int rightEndPoint;
    
    /**
     * This flag indicates if the ranges must be interpreted as exclusion ranges
     * f.e. "don't use the values in this interval" instead of "use just the values in this interval" 
     * it just a some kind of "metadata" for the range.
     */
    private Boolean isExludeRange = false;
    
    public RangeParser(){}
    
    /**
     * Create a Range object parsing the input string and setting the isExcludeRange as the provided input parameter
     * 
     * @param range the range as string specified in ISO_31-11 standard
     * @param isExludeRange
     * @throws IllegalArgumentException  if the string is null or not parsable or if is not a valid range ( left endpoint > endpoint)
     */
    public RangeParser(String range, boolean isExludeRange) throws IllegalArgumentException{
        this.buildRange(range, isExludeRange);
    }
    
    /**
     * Create a Range object parsing the input string and setting the isExcludeRange to false
     * 
     * @param range the range as string specified in ISO_31-11 standard
     * @throws IllegalArgumentException if the string is null or not parsable or if is not a valid range ( left endpoint > endpoint)
     */
    public RangeParser(String range) throws IllegalArgumentException{
        this.buildRange(range, false);
    }
    
    private void buildRange(String range, Boolean isExludeRange) throws IllegalArgumentException{
        
        this.isExludeRange = isExludeRange;
        
        if(range == null || range.isEmpty()){
            throw new IllegalArgumentException("The String provided as range is null or empty...");
        }
        Pattern intervalPattern = Pattern.compile(INTERVAL_PATTERN);
        Matcher matcher = intervalPattern.matcher(range);
        if(!matcher.matches()){
            throw new IllegalArgumentException("The String provided as range doesn't match with the pattern (\\[|\\()-?(\\d*);-?(\\d*)(\\]|\\))");
        }
        // Ok the interval specified match with the pattern... let's continue without any other syntax validity checks...
        String [] endpoints = range.split(";");
        String left = endpoints[0];
        String right = endpoints[1];
        int leftInclusion = (left.charAt(0) == '[')?0:+1;
        int rightInclusion = (right.charAt(right.length()-1) == ']')?0:-1;
        if(left.length() == 1){
            this.leftEndPoint = Integer.MIN_VALUE;
        }
        else{
            this.leftEndPoint = Integer.parseInt(left.substring(1)) + leftInclusion;
        }
        if(right.length() == 1){
            this.rightEndPoint = Integer.MAX_VALUE;
        }
        else{
            this.rightEndPoint = Integer.parseInt(right.substring(0, right.length()-1)) + rightInclusion;
        }
        if(this.leftEndPoint > this.rightEndPoint){
            throw new IllegalArgumentException("The String provided as range has leftEndPoint greater than rightEndPoint");
        }
    }

    public int getLeftEndPoint() {
        return leftEndPoint;
    }

    public void setLeftEndPoint(int leftEndPoint) {
        this.leftEndPoint = leftEndPoint;
    }

    public int getRightEndPoint() {
        return rightEndPoint;
    }

    public void setRightEndPoint(int rightEndPoint) {
        this.rightEndPoint = rightEndPoint;
    }

    public Boolean isExludeRange() {
        return isExludeRange;
    }

    public void setExludeRange(Boolean isExludeRange) {
        this.isExludeRange = isExludeRange;
    }

    
}
