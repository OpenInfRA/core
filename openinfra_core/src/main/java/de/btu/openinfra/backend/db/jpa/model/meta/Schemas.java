package de.btu.openinfra.backend.db.jpa.model.meta;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.btu.openinfra.backend.db.jpa.model.OpenInfraModelObject;


/**
 * The persistent class for the schemas database table.
 *
 */
@Entity
@Table(schema="meta_data")
@NamedQueries({
    @NamedQuery(name="Schemas.findAll", query="SELECT s FROM Schemas s"),
    @NamedQuery(name="Schemas.count", query="SELECT COUNT(s) FROM Schemas s")
})
public class Schemas extends OpenInfraModelObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String schema;

	//bi-directional many-to-one association to DatabaseConnection
	@OneToMany(mappedBy="schemaBean")
	private List<DatabaseConnection> databaseConnections;

	public Schemas() {
	}

	public String getSchema() {
		return this.schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<DatabaseConnection> getDatabaseConnections() {
		return this.databaseConnections;
	}

	public void setDatabaseConnections(List<DatabaseConnection> databaseConnections) {
		this.databaseConnections = databaseConnections;
	}

	public DatabaseConnection addDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().add(databaseConnection);
		databaseConnection.setSchemaBean(this);

		return databaseConnection;
	}

	public DatabaseConnection removeDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().remove(databaseConnection);
		databaseConnection.setSchemaBean(null);

		return databaseConnection;
	}

}