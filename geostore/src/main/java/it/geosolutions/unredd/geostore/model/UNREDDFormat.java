package it.geosolutions.unredd.geostore.model;

public enum UNREDDFormat {
	
	    VECTOR("vector"),
	    RASTER("raster");

	    private String name;

	    private UNREDDFormat(String format) {
	        this.name = format;
	    }

	    public String getName() {
	        return name;
	    }

        public static UNREDDFormat parseName(String name) {
            for (UNREDDFormat f : values()) {
                if(f.getName().equals(name))
                    return f;
            }
            return null;
        }
	}

