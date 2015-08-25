package de.btu.openinfra.backend.db;

/**
 * This enumeration defines the keys that can be used in the meta data table and
 * will be used by the core.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public enum OpenInfraMetaDataEnum {

	/**
	 * Descriptor for array of attribute types that will define the columns for
	 * the list of topic instances of a specific topic characteristic.
	 */
	LIST_VIEW_COLUMNS ("list_view_columns");

	private String metaDataDescriptor;

    private OpenInfraMetaDataEnum(String metaDataDescriptor) {
        this.metaDataDescriptor = metaDataDescriptor;
    }

    public String getmetaDataDescriptor() {
        return this.metaDataDescriptor;
    }
}
