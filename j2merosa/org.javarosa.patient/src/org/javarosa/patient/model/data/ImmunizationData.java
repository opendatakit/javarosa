package org.javarosa.patient.model.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import org.javarosa.core.util.externalizable.DeserializationException;
import org.javarosa.core.util.externalizable.ExtUtil;
import org.javarosa.core.util.externalizable.ExtWrapList;
import org.javarosa.core.util.externalizable.Externalizable;
import org.javarosa.core.util.externalizable.PrototypeFactory;

/**
 * @author Clayton Sims
 *
 */
public class ImmunizationData implements Externalizable {

	/** ImmunizationRow */
	Vector rows;
	
	/** The largest column we'll display due to age */
	int largestAgeColumn; /* Note that we don't persist this value */
	
	public ImmunizationData() {
		rows = new Vector();
	}
	
	public ImmunizationData(Vector rows) {
		this.rows = rows;
	}
	
	public Vector getImmunizationRows() {
		 return rows;
	}
	
	public void addRow(ImmunizationRow row) {
		rows.addElement(row);
	}
	
	/**
	 * @return the largestAgeColumn
	 */
	public int getLargestAgeColumn() {
		return largestAgeColumn;
	}

	/**
	 * @param largestAgeColumn the largestAgeColumn to set
	 */
	public void setLargestAgeColumn(int largestAgeColumn) {
		this.largestAgeColumn = largestAgeColumn;
	}

	/* (non-Javadoc)
	 * @see org.javarosa.core.services.storage.utilities.Externalizable#readExternal(java.io.DataInputStream)
	 */
	public void readExternal(DataInputStream in, PrototypeFactory pf) throws IOException, DeserializationException {
		rows = (Vector)ExtUtil.read(in, new ExtWrapList(ImmunizationRow.class));
	}

	/* (non-Javadoc)
	 * @see org.javarosa.core.services.storage.utilities.Externalizable#writeExternal(java.io.DataOutputStream)
	 */
	public void writeExternal(DataOutputStream out) throws IOException {
		ExtUtil.write(out, new ExtWrapList(rows));
	}
	public ImmunizationData clone() {
		ImmunizationData clone = new ImmunizationData();
		clone.largestAgeColumn = this.largestAgeColumn;
		Enumeration en = rows.elements();
		while(en.hasMoreElements()) {
			ImmunizationRow row = (ImmunizationRow)en.nextElement();
			clone.rows.addElement(row.clone());
		}
		return clone;
	}
}
