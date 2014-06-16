package net.sf.dvstar.kidsdialer.data;

import java.util.HashMap;
import java.util.Map;

import android.database.MatrixCursor;

public class ContactMatrixCursor extends MatrixCursor {
	private Map<String, Integer> mColumnNameMap;
	private  String[] mColumns;
	
	
	public ContactMatrixCursor(String[] columnNames) {
		super(columnNames);
		this.mColumns = columnNames;
	}

	public ContactMatrixCursor(String[] columnNames, int initialCapacity) {
		super(columnNames, initialCapacity);
		this.mColumns = columnNames;
	}

	@Override
    public int getColumnIndex(String columnName) {
		int index = -1;
		
        // Create mColumnNameMap on demand
        if (mColumnNameMap == null) {
            String[] columns = mColumns;
            int columnCount = columns.length;
            HashMap<String, Integer> map = new HashMap<String, Integer>(columnCount, 1);
            for (int i = 0; i < columnCount; i++) {
                map.put(columns[i], i);
            }
            mColumnNameMap = map;
        }
        Integer i = mColumnNameMap.get(columnName);
        if (i != null) {
        	index =  i.intValue();
        }
        
		return index;
    }
	
}
