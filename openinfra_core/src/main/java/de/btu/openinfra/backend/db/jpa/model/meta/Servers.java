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
 * The persistent class for the servers database table.
 *
 */
@Entity
@Table(schema="meta_data")
@NamedQueries({
    @NamedQuery(name="Servers.findAll", query="SELECT s FROM Servers s"),
    @NamedQuery(
            name="Servers.findByServer",
            query="SELECT s FROM Servers s WHERE s.server = :server")
})
public class Servers extends OpenInfraModelObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String server;

	//bi-directional many-to-one association to DatabaseConnection
	@OneToMany(mappedBy="serverBean")
	private List<DatabaseConnection> databaseConnections;

	public Servers() {
	}

	public String getServer() {
		return this.server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public List<DatabaseConnection> getDatabaseConnections() {
		return this.databaseConnections;
	}

	public void setDatabaseConnections(List<DatabaseConnection> databaseConnections) {
		this.databaseConnections = databaseConnections;
	}

	public DatabaseConnection addDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().add(databaseConnection);
		databaseConnection.setServerBean(this);

		return databaseConnection;
	}

	public DatabaseConnection removeDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().remove(databaseConnection);
		databaseConnection.setServerBean(null);

		return databaseConnection;
	}

}