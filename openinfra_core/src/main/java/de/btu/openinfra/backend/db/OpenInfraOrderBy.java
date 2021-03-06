package de.btu.openinfra.backend.db;

import java.util.UUID;

/**
 * This class represents a order by object. This object will either contain an
 * OpenInfraOrderByEnum or an UUID.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
public class OpenInfraOrderBy {

    private UUID id;
    private boolean isUuid = false;
    private OpenInfraOrderByEnum column;

    /**
     * This constructor awaits a string that will be either parsed to an UUID or
     * an OpenInfraOrderByEnum.
     *
     * @param column The string for the order by.
     */
    public OpenInfraOrderBy(String column) {
        try {
            id = UUID.fromString(column);
            isUuid = true;
        } catch (IllegalArgumentException e) {
            // Will be thrown if it is not possible to cast the column to an
            // UUID. So it must be a value from the OpenInfraOrderByEnum
            this.column = OpenInfraOrderByEnum.valueOf(column.toUpperCase());
            isUuid = false;
        }
    }

    /**
     * This functions returns the content of the object. The content can be a
     * UUID or a OpenInfraOrderByEnum.
     *
     * @return Either a UUID or a OpenInfraOrderByEnum.
     */
    public Object getContent() {
        if (isUuid) {
            return getId();
        } else {
            return getColumn();
        }
    }

    public UUID getId() {
        return id;
    }

    public OpenInfraOrderByEnum getColumn() {
        return column;
    }

    public boolean isUuid() {
        return isUuid;
    }

    public void setColumn(OpenInfraOrderByEnum column) {
        this.column = column;
    }
}
