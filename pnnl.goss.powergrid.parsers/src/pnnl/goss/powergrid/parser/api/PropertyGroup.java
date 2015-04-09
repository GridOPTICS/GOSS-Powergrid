package pnnl.goss.powergrid.parser.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A PropertyGroup is analogous to an object's state. 
 * 
 * @author Craig Allwardt
 *
 */
public class PropertyGroup implements Serializable, Iterable<Property> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5191651946438218250L;

	/**
	 * Private list of properties that this container holds.
	 */
	private List<Property> propertyGroupList = new ArrayList<Property>();
	
	/**
	 * Group name
	 */
	private String groupName;
	
	public void addProperty(Property property){
		propertyGroupList.add(property);
	}

	@Override
	public Iterator<Property> iterator() {
		return propertyGroupList.iterator();
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public List<String> getPropertyNames(){
		List<String> names = new ArrayList<String>();
		
		for(Property p: propertyGroupList){
			if (p.getName() != null){
				names.add(p.getName());
			}
		}
		
		return names;
	}
}
