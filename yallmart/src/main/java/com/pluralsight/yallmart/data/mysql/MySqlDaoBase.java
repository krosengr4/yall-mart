package com.pluralsight.yallmart.data.mysql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySqlDaoBase {
	private final DataSource dataSource;

	public MySqlDaoBase(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	protected Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}
