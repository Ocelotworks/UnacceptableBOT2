package com.unacceptableuse.unacceptablebot.handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.unacceptableuse.unacceptablebot.UnacceptableBot;

public class MySQLConnection {
	private static final String dbClassName = "com.mysql.jdbc.Driver";

	private static final String CONNECTION = "jdbc:mysql://hp.matrixdevuk.pw/";

	protected Connection c;

	public void connect() throws SQLException, ClassNotFoundException {
		Class.forName(dbClassName);

		Properties p = new Properties();
		p.put("user", "unacceptablebot");
		p.put("password",
				UnacceptableBot.getConfigHandler().getPassword("mysql"));
		// p.put("password","PASSWORDDDDDD"); // Don't commit with your
		// password, that's just dumb... Love matrixdevuk

		c = DriverManager.getConnection(CONNECTION, p);

	}

	public String getSetting(String setting) {
		try {
			ResultSet rs = query("SELECT * FROM  teknogeek_settings.Global_Settings WHERE  `Setting` =  '"
					+ setting + "' LIMIT 1");
			return rs.next() ? rs.getString(2) : null;
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error " + e.getErrorCode() + " " + e.getLocalizedMessage();
		}
	}

	public boolean setSetting(String setting, String value) {
		try {
			return excecute("INSERT INTO `teknogeek_settings`.`Global_Settings` (`Setting`, `Value`) VALUES ('"
					+ setting + "', '" + value + "');");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean incrementValue(String setting, int amt) {
		try {
			return excecute("UPDATE `teknogeek_settings`.`Global_Settings` SET 'Value' = 'Value'+"
					+ amt + " WHERE 'Setting' = " + setting + ");");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public ResultSet query(String sql) throws SQLException {
		return c.prepareStatement(sql).executeQuery();
	}

	public boolean excecute(String sql) throws SQLException {
		return c.prepareStatement(sql).execute();
	}

	public void disconnect() throws SQLException {
		c.close();
	}
}
