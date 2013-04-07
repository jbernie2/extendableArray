/*
 * Author: John Bernier 4/13
 *
 * extendableArray.java
 *  functionally equivalent to an ArrayList, but with different
 *  internal mechanisms for allowing for unlimited array space.
 *  
 *  this implementation uses a two dimensional array where each
 *  array row increases in size by a power when an element is 
 *  inserted into the array, the index is hashed into the first array
 *  by finding the log based 2 value, the element is then positioned
 *  into the second array by subtracting the lowest index value in 
 *  that specific array from the index given by the user.
 *
 * Performance:
 *  The performance advantage that this structure has over the ArrayList
 *  is that the entire array does not have to be copied over everytime 
 *  the array is expanded. However, there is more overhead for each operation,
 *  but all operations should occur in roughly the same amount of time. where 
 *  as with an ArrayList some operations may take much longer
 *
 * Time Complexity:
 *  access time O(1)
 *  placement time O(1)
 *
 * Space Complexty:
 *  2^(Ceiling (log(array size)based 2))
 * 
 */

public class extendableArray{

    //size of the array
    int length;
    //max size is 2^32
    long maxSize;

    private Object[][] arrayTable;

    public extendableArray(){
        length = 0;
        maxSize = (long)Math.pow(2,32);
        arrayTable = new Object[31][];
    }
    //adds an element to the end of the array
    public boolean push(Object element){
        //check if table is still within its bounds
        if(length >= maxSize -1){
            return false;
        }
        
        //hash element number to table position
        length++;
        int tableIndex = hashIndex(length);

        //check if table position needs to be initialized
        if(arrayTable[tableIndex] == null){
            arrayTable[tableIndex] = new Object[(int)Math.pow(2,tableIndex)];
        }

        //get the elements position within the table row
        int arrayIndex = length - (int)Math.pow(2,tableIndex);

        //place element in table
        arrayTable[tableIndex][arrayIndex] = element;

        return true;
    }
    //removes last element from the array
    public Object pop(){
        if(length == 0){
            return null;
        }
        int tableIndex = hashIndex(length);
        int arrayIndex = length - (int)Math.pow(2,tableIndex);
        
        Object element = arrayTable[tableIndex][arrayIndex];
        arrayTable[tableIndex][arrayIndex] = null;
        length--;

        return element;
    }
    //places an element in the arrayTable
    public boolean put(Object element, int index){
 
        int temp = length;
        //if the position the element is being placed is beyond the
        //current bounds of the array, then the size is now equal to 
        //that position.
        if(index > length){
            length = index;
        
            if(push(element)){
                return true;
            }
            else{
                length = temp;
                return false;
            }
        }
        //if the index is within the bounds, place at the corresponding
        //position
        else{
            int tableIndex = hashIndex(index+1);
            int arrayIndex = (index+1) - (int)Math.pow(2,tableIndex);
            arrayTable[tableIndex][arrayIndex] = element;
            return true;
        }
    }
    //returns an object from a given position in the array
    public Object get(int index){
        int tableIndex = hashIndex(index+1);
        int arrayIndex = (index+1) - (int)Math.pow(2,tableIndex);
        return arrayTable[tableIndex][arrayIndex];
    }
    //eliminates any trailing null values on the end of the array
    public void trim(){
        int tableIndex = hashIndex(length);
        int arrayIndex = length - (int)Math.pow(2,tableIndex);
        
        while(arrayTable[tableIndex][arrayIndex] == null){
            length--;
            tableIndex = hashIndex(length);
            arrayIndex = length - (int)Math.pow(2,tableIndex);
        }
    }
    //hash function that determines the table row of an element
    //finds the floor of the log based 2 of the index
    private int hashIndex(int num){
        return (int) Math.floor((Math.log(num)/Math.log(2)));
    }
}
