package simpledb;

import java.io.Serializable;
import java.util.*;

import jdk.nashorn.internal.runtime.arrays.IteratorAction;

/**
 * TupleDesc describes the schema of a tuple.
 */
public class TupleDesc implements Serializable {
	
	private List<TDItem> tdItemList = new ArrayList<TupleDesc.TDItem>();

    /**
     * A help class to facilitate organizing the information of each field
     * */
    public static class TDItem implements Serializable {
    	

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * */
        Type fieldType;
        
        /**
         * The name of the field
         * */
        String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    /**
     * @return
     *        An iterator which iterates over all the field TDItems
     *        that are included in this TupleDesc
     * */
    public Iterator<TDItem> iterator() {
        return tdItemList.iterator();
    }
    

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     * @param fieldAr
     *            array specifying the names of the fields. Note that names may
     *            be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        // some code goes here
    	for(int i = 0; i < typeAr.length; i++) {
    		TDItem item = new TDItem(typeAr[i], fieldAr[i]);
    		tdItemList.add(item);
    	}   	
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * 
     * @param typeAr
     *            array specifying the number of and types of fields in this
     *            TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        // some code goes here
    	for(int i = 0; i < typeAr.length; i++) {
    		TDItem item = new TDItem(typeAr[i], null);
    		tdItemList.add(item);
    	} 
    }

    /**
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        // some code goes here
        return tdItemList.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 
     * @param i
     *            index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {
        // some code goes here
        return tdItemList.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 
     * @param i
     *            The index of the field to get the type of. It must be a valid
     *            index.
     * @return the type of the ith field
     * @throws NoSuchElementException
     *             if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        // some code goes here
        return tdItemList.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 
     * @param name
     *            name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException
     *             if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        // some code goes here
    	for(int i = 0; i < tdItemList.size(); i++) {
    		if(tdItemList.get(i).fieldName.equals(name)) {
    			return i;
    		}
    	}
        throw new NoSuchElementException();
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     *         Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        // some code goes here
    	int size = 0;
    	for(TDItem item: tdItemList) {
           size+=item.fieldType.getLen();
    	}
        return size;
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 
     * @param td1
     *            The TupleDesc with the first fields of the new TupleDesc
     * @param td2
     *            The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        // some code goes here
    	Type[] type = new Type[td1.numFields()+td2.numFields()];
    	String[] fields = new String[td1.numFields()+td2.numFields()];
 
    	for(int i = 0; i < td1.numFields(); i++) {
    		type[i] = td1.getFieldType(i);
    		fields[i] = td1.getFieldName(i);
    	}
  
    	for(int i = 0; i < td2.numFields(); i++) {
    		type[i] = td2.getFieldType(i);
    		fields[i] = td2.getFieldName(i);
    	}
        return new TupleDesc(type, fields);
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they are the same size and if the n-th
     * type in this TupleDesc is equal to the n-th type in td.
     * 
     * @param o
     *            the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */
    public boolean equals(Object o) {
    	 TupleDesc that = (TupleDesc)o;
    	 if(this.getSize()==that.getSize()) {
    		 for(int i = 0; i < this.numFields(); i++) {
    		    if(!this.getFieldType(i).equals(that.getFieldType(i))) return false;
    		 }
    		 return true;
    	 }
    	 else {
    		 return false;
    	 }
    }

    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
    	int result = 1;
    	int c = 0;
    	for(int i = 0; i < this.numFields(); i++) {
        	c+=this.getFieldType(i).hashCode();
    	}   	
        return 37*result+c;
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     * 
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
    	StringBuffer buffer = new StringBuffer();
    	for(int i = 0; i < this.numFields(); i++) {
    		buffer.append(this.getFieldType(i).toString() + "("+this.getFieldName(i)+"),");
    	}
        return buffer.toString();
    }
    
    public static void main(String[] args) {
		Type ty1 = Type.INT_TYPE;
		Type ty2 = Type.INT_TYPE;
        Integer t1= new Integer(1);
        Integer t2 = new Integer(1);
        TupleDesc desc1 =Utility.getTupleDesc(3, "t2");
        TupleDesc desc2 =Utility.getTupleDesc(3, "t1");
        System.out.println(desc1.equals(desc2));
        System.out.println(desc1.hashCode());
        System.out.println(desc2.hashCode());
        System.out.println(desc1.toString());

		//System.out.println(t1.equals(t2));
	}
}
