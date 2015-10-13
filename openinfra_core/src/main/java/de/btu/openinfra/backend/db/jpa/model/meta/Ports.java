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
 * The persistent class for the ports database table.
 *
 */
@Entity
@Table(schema="meta_data")
@NamedQueries({
    @NamedQuery(name="Ports.findAll", query="SELECT p FROM Ports p"),
    @NamedQuery(
            name="Ports.findByPort",
            query="SELECT p FROM Ports p WHERE p.port = :port")
})
public class Ports extends OpenInfraModelObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer port;

	//bi-directional many-to-one association to DatabaseConnection
	@OneToMany(mappedBy="portBean")
	private List<DatabaseConnection> databaseConnections;

	public Ports() {
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public List<DatabaseConnection> getDatabaseConnections() {
		return this.databaseConnections;
	}

	public void setDatabaseConnections(List<DatabaseConnection> databaseConnections) {
		this.databaseConnections = databaseConnections;
	}

	public DatabaseConnection addDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().add(databaseConnection);
		databaseConnection.setPortBean(this);

		return databaseConnection;
	}

	public DatabaseConnection removeDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().remove(databaseConnection);
		databaseConnection.setPortBean(null);

		return databaseConnection;
	}

}