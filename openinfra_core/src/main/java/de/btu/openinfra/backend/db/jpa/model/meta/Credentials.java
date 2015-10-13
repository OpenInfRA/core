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
 * The persistent class for the credentials database table.
 *
 */
@Entity
@Table(schema="meta_data")
@NamedQueries({
    @NamedQuery(
            name="Credentials.findAll",
            query="SELECT c FROM Credentials c"),
    @NamedQuery(
            name="Credentials.findByUsernameAndPassword",
            query="SELECT c FROM Credentials c WHERE c.username = :username "
                    + "AND c.password = :password")
})
public class Credentials extends OpenInfraModelObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private String password;

	private String username;

	//bi-directional many-to-one association to DatabaseConnection
	@OneToMany(mappedBy="credential")
	private List<DatabaseConnection> databaseConnections;

	public Credentials() {
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<DatabaseConnection> getDatabaseConnections() {
		return this.databaseConnections;
	}

	public void setDatabaseConnections(List<DatabaseConnection> databaseConnections) {
		this.databaseConnections = databaseConnections;
	}

	public DatabaseConnection addDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().add(databaseConnection);
		databaseConnection.setCredential(this);

		return databaseConnection;
	}

	public DatabaseConnection removeDatabaseConnection(DatabaseConnection databaseConnection) {
		getDatabaseConnections().remove(databaseConnection);
		databaseConnection.setCredential(null);

		return databaseConnection;
	}

}