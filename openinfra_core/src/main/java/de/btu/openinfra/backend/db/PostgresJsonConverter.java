package de.btu.openinfra.backend.db;

import java.sql.SQLException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.postgresql.util.PGobject;

/**
 * This class will convert a JSON formated object into a PostgreSQL object and
 * vice versa. It will make use of the simple-JSON library and the PGobject
 * from the JDBC driver.
 * <p>
 * This converter must be activated for the appropriated column in the model
 * class.
 *
 * @author <a href="http://www.b-tu.de">BTU</a> DBIS
 *
 */
@Converter
public class PostgresJsonConverter implements
    AttributeConverter<JSONObject, PGobject> {

	@Override
    public PGobject convertToDatabaseColumn(JSONObject json) {
	    PGobject pgObject = new PGobject();
	    try {
	        // set the type of the pgObject to JSON
	        pgObject.setType("json");
	        // set the value of the pgObject to the JSON string
	        pgObject.setValue(json.toJSONString());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	    return pgObject;
	}

	@Override
    public JSONObject convertToEntityAttribute(PGobject pgObject) {
	    try {
            return (JSONObject)new JSONParser().parse(pgObject.toString());
        } catch (ParseException pe) {
            // TODO Auto-generated catch block
            System.out.println(pe.getMessage());
            return null;
        } catch (ClassCastException cce) {
            System.out.println(cce.getMessage());
            return null;
        }
	}

}
