

/*
                (MySQL-API)
      This Class was created by MaxIstLegit
             09/03/2022 | 07:28
*/

import java.sql.*;

/**
 * <h1>Lightweight MySQL API</h1><br />
 * <p>
 * Usage: <br />
 * - <bold>Write in main class ( "<code>MySQLAPI coinMySQL = new MySQLAPI(String username, String password, String host, String database)</code>" ) <br />
 * - <bold>Make a try/catch with ( "<code>coinMySQL.startConnection()</code>" )</bold> <br />
 * - <bold>Then you can use it</bold> <br />
 * <br />
 * - <code>getConnection()</code> [Connection] - gets the connection <br />
 * - <code>isConnected()</code> [Boolean] - returns true if connected <br />
 * - <code>startConnection()</code> [void] - Connects the MySQLAPI Instance <br />
 * - <code>closeConnection()</code> [void] - Closes the MySQLAPI Instance <br />
 * - <code>createNewTable(String tableName, String[] entries, String[] entryTypes)</code> [void] - creates a new table <br />
 * - - INFORMATION: entires and entrytypes must match with the length of the array <br />
 * - - - WRONG: <code>createNewTable(String "sixnine", new String[]{"foo", "bar"}, new String[]{"VARCHAR(100)"})</code> <br />
 * - - - - that won't work <br />
 * - - - RIGHT: <code>createNewTable(String "foo", new String[]{"foo", "bar"}, new String[]{"VARCHAR(100)", "BIGINT(64)"})</code> <br />
 * <br />
 *
 * @author <bold>KeineSecrets</bold> <br />
 * @version <bold>1.0.0</bold> <br />
 */
public class MySQLAPI {

	final String username;
	final String password;
	final String host;
	final String port;
	final String database;

	Connection connection;

	public MySQLAPI(String username, String password, String host, String database) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.database = database;
		this.port = "3306";
	}

	public MySQLAPI(String username, String password, String host, String database, String port) {
		this.username = username;
		this.password = password;
		this.host = host;
		this.database = database;
		this.port = port;
	}

	public boolean isConnected() {
		return (connection != null);
	}

	/**
	 * Creates a new table
	 *
	 * @param tableName  The name of the Table
	 * @param entries    The Entries (names) (e.g. COINS)
	 * @param entryTypes The EntryTypes (e.g. VARCHAR(100) or BIGINT(64))
	 */
	public void createNewTable(String tableName, String[] entries, String[] entryTypes) {
		if (isConnected()) {
			StringBuilder stringBuilder = new StringBuilder();
			try {
				for (int i = 0; i < entries.length || i < entryTypes.length; i++) {
					stringBuilder.append(entries[i].toUpperCase()).append(" ").append(entryTypes[i]).append(", ");
				}
				String statement = stringBuilder.toString();
				if (statement.endsWith(", ")) {
					statement = statement.substring(0, statement.length() - 2);
				}
				PreparedStatement preparedStatement = this.getStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (" + statement + ")");
				preparedStatement.executeUpdate();
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {

		}
	}

	public void startConnection() {
		if (!isConnected()) {
			try {
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", username, password);
				System.out.println("Connected to database " + database + " with " + username + ".");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeConnection() {
		if (isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getConnection() {
		if (isConnected()) return connection;
		return null;
	}

	public PreparedStatement getStatement(String query) {
		PreparedStatement preparedStatement = null;
		if (isConnected()) {
			try {
				preparedStatement = getConnection().prepareStatement(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return preparedStatement;
	}

	public ResultSet getResult(String query) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		if (isConnected()) {
			try {
				preparedStatement = getStatement(query);
				resultSet = preparedStatement.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultSet;
	}
}
