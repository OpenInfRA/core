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
 * The persistent class for the databases database table.
 *
 */
@Entity
@Table(schema="meta_data")
@NamedQueries({
    @NamedQuery(name="Databases.findAll", query="SELECT d FROM Databases d"),
    @NamedQuery(
            name="Databases.findByDatabase",
            query="SELECT d FROM Databases d WHERE d.database = :database")
})
public class Databases extends OpenInfraModelObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String database;

	//bi-directional many-to-one association to DatabaseConnection
	@OneToMany(mappedBy="databaseBean")
	private List<DatabaseConnection> databaseConnections;

	public Databases() {
	}

	public String getDatabase() {
		return this.database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public List<DatabaseConnection> getDatabaseConnections() {
		return this.databaseConnections;
	}

	public void setDatabaseConnections(List<DatabaseConnection> databaseConnections) {
		this.databaseConnections = databaseConnections;
	}

	public DatabaseConnection addDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().add(databaseConnection);
		databaseConnection.setDatabaseBean(this);

		return databaseConnection;
	}

	public DatabaseConnection removeDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().remove(databaseConnection);
		databaseConnection.setDatabaseBean(null);

		return databaseConnection;
	}

}