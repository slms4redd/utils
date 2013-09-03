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
package it.geosolutions.unredd.stats.model.config;

import it.geosolutions.unredd.stats.RangeParser;
import junit.framework.TestCase;

/**
 * This is a test class for @{it.geosolutions.unredd.stats.model.config.Range};
 * 
 * @author DamianoG
 *
 */
public class RangeTest extends TestCase{

    public void testInvalidRanges(){
        
        RangeParser r = null;
        
        String [] notValidRanges = {"[4645645;54]","6;6)","(97667;899","[455;45","4545;454]","5454","hgg","(4w545;454]","",null,"(454.5;454]","(4,545;454]","(4;545;454]","(4454545;4)","-5;-7"};
        boolean fails = false;
        for(String range : notValidRanges){
            fails = false;
            try{
                r = new RangeParser(range);
                r.getLeftEndPoint();
            }
            catch(IllegalArgumentException e){
                fails = true;
            }
            if(!fails){
                fail("The wrong range '" + range + "' has been accepted...");
            }
        }
    }
    
    public void testValidRanges(){
        
        RangeParser r = null;
        
        String [] validRanges = {"(5;7)","[5;7]","(5;9)","(-5;9)","(-9;-5)","[5;9]","[-5;9]","[-9;-5]","(5;9]","[-5;9)","(;)","[;]","(65;)","[;65]"};
        int i=0;
        for(String range : validRanges){
            try{
                r = new RangeParser(range);
                if(!checkRange(r,i)){
                    fail("The valid range '" + range + "' (index is  '" + i + "') has been wrong instantiated...");
                }
            }
            catch(IllegalArgumentException e){
                fail("The valid range '" + range + "' throws a parse exception...");
            }
            i++;
        }
    }
    
    public static boolean checkRange(RangeParser r, int index) throws IllegalArgumentException{
        
        switch(index){
            case 0:
                if(r.getLeftEndPoint() == 6 && r.getRightEndPoint() == 6){ return true;} else{ return false;}
            case 1:
                if(r.getLeftEndPoint() == 5 && r.getRightEndPoint() == 7){ return true;} else{ return false;}
            case 2:
                if(r.getLeftEndPoint() == 6 && r.getRightEndPoint() == 8){ return true;} else{ return false;}
            case 3:
                if(r.getLeftEndPoint() == -4 && r.getRightEndPoint() == 8){ return true;} else{ return false;}
            case 4:
                if(r.getLeftEndPoint() == -8 && r.getRightEndPoint() == -6){ return true;} else{ return false;}
            case 5:
                if(r.getLeftEndPoint() == 5 && r.getRightEndPoint() == 9){ return true;} else{ return false;}
            case 6:
                if(r.getLeftEndPoint() == -5 && r.getRightEndPoint() == 9){ return true;} else{ return false;}
            case 7:
                if(r.getLeftEndPoint() == -9 && r.getRightEndPoint() == -5){ return true;} else{ return false;}
            case 8:
                if(r.getLeftEndPoint() == 6 && r.getRightEndPoint() == 9){ return true;} else{ return false;}
            case 9:
                if(r.getLeftEndPoint() == -5 && r.getRightEndPoint() == 8){ return true;} else{ return false;}
            case 10:
                if(r.getLeftEndPoint() == Integer.MIN_VALUE && r.getRightEndPoint() == Integer.MAX_VALUE){ return true;} else{ return false;}
            case 11:
                if(r.getLeftEndPoint() == Integer.MIN_VALUE && r.getRightEndPoint() == Integer.MAX_VALUE){ return true;} else{ return false;}
            case 12:
                if(r.getLeftEndPoint() == 66 && r.getRightEndPoint() == Integer.MAX_VALUE){ return true;} else{ return false;}
            case 13:
                if(r.getLeftEndPoint() == Integer.MIN_VALUE && r.getRightEndPoint() == 65){ return true;} else{ return false;}
        }
        throw new IllegalArgumentException("Invalid Index!!!");
    }
}
