package com.test;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.qdf.core.Qdf;

public class UserService {

	public void tx() {

		try {
			
			Connection connection = Qdf.me().getPool().getConnection();
			
			System.out.println(connection);
			
			String sql = "update user set age = 10 where id = ?";
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, "1");
			ps.executeUpdate();
			
			int i = 1/0;
			
			ps.setString(1, "2");
			ps.executeUpdate();
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
