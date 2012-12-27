package it.geosolutions.unredd.geostore.model;

import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.enums.DataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User feedback comments, coming from portal interface.
 * 
 * @author Oscar Fonts
 */
public class UNREDDReport extends UNREDDResource<UNREDDReport.Attributes, UNREDDReport.ReverseAttributes> {
	
	public static final String CATEGORY_NAME = UNREDDCategories.REPORT.getName();
	
    public static class ReverseAttributes extends ReverseAttributeDef {
        private final static List<String> list = new ArrayList<String>();

        private ReverseAttributes(String name) {
            super(name);
            list.add(name);
        }
    }
    
    /**
     * Attributes for UNREDDReport.
     * 
     * Values are:
     * <ul>
     *   <li>{@link #CHARTSCRIPTID}
     *   <li>{@link #USERNAME}
     *   <li>{@link #USERMAIL}
     * </ul>
     */
    public static class Attributes extends AttributeDef {
        private final static Map<String, DataType> map = new HashMap<String, DataType>();

        /**
         * The user contact name, as filled in the statistics form.
         */
        public final static Attributes CHARTSCRIPTID  = new Attributes("ChartScriptId", DataType.NUMBER);
        /**
         * The user contact name, as filled in the statistics form.
         */
        public final static Attributes USERNAME  = new Attributes("UserName", DataType.STRING);
        /**
         * The user contact mail, as filled in the statistics form.
         */
        public final static Attributes USERMAIL  = new Attributes("UserMail", DataType.STRING);
        
        private Attributes(String name, DataType dataType) {
            super(name, dataType);
            map.put(name, dataType);
        }
    }
    
	public UNREDDReport() {
	}

	public UNREDDReport(Resource resource) {
		super(resource);
	}

	@Override
	public String getCategoryName() {
		return CATEGORY_NAME;
	}

	@Override
	protected Map<String, DataType> getAttributeMap() {
        return Attributes.map;
	}

	@Override
	protected List<String> getReverseAttributes() {
        return Collections.unmodifiableList(ReverseAttributes.list);
	}
    
}
