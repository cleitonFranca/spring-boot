package servidor.torcedor.digital.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import servidor.torcedor.digital.utils.PropertieResources;

public class SimpleJdbcConection {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleJdbcConection.class);

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = PropertieResources.getResources().getProperty("spring.datasource.driver-class-name");
	static final String DB_URL = PropertieResources.getResources().getProperty("spring.datasource.url");

	// Database credentials
	static final String USER = PropertieResources.getResources().getProperty("spring.datasource.username");
	static final String PASS = PropertieResources.getResources().getProperty("spring.datasource.password");
	
	
	
	private Connection connection;

	public Connection getCurrentConection() throws SQLException {
		
		try {

			if (java.util.Objects.isNull(connection)) {
				Class.forName(JDBC_DRIVER);
				return connection = DriverManager.getConnection(DB_URL, USER, PASS);
			}
			return connection;

		} catch (Exception e) {
			logger.error(e.getMessage());
			connection.close();
		}

		return connection;
	}
	
	// disconnect database
	public void disconnect() {
	  if(connection != null) {
	    try {
	      connection.close();
	      connection = null;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	  }
	}   

}
