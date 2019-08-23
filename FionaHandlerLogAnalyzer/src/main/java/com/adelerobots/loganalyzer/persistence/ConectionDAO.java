package com.adelerobots.loganalyzer.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.adelerobots.loganalyzer.LogAnalizer;
import com.adelerobots.loganalyzer.model.Conection;

public class ConectionDAO {

	static final String TABLE_CONECTION = "CONEXION";

	/**
	 * Añade una nueva fila a la tabla CONEXION. La foreign key md5 de la tabla
	 * AVATAR es un atributo del propio objeto Conection
	 * 
	 * @param conection
	 * @return
	 */
	public static Integer aniadirConexion(Conection conection) {
		Integer id = -1;
		try {
			AnalizerDB con = LogAnalizer.con;
			String status = "n";
			if (conection.isStatus())
				status = "y";

			/*Statement stat = con.getConnection().createStatement();

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			String date = sdf.format(conection.getFechaConexion());

			stat.executeUpdate(
					"INSERT INTO "
							+ TABLE_CONECTION
							+ "(`fecConexion`,`pais`,`ciudad`,`region`,`numFacilitadas`, `numFallidas`, `status`,`dialogo`,`avatar_md5`) VALUES ('"
							+ date + "', '"
							+ conection.getCountry() + "', '"
							+ conection.getCity() + "', '"
							+ conection.getRegion() + "', '"
							+ conection.getNumFacilitadas() + "', '"
							+ conection.getNumFallidas() + "', '"
							+ status + "', '" 
							+ conection.getDialogo() + "', '"
							+ conection.getMd5() + "')",
					Statement.RETURN_GENERATED_KEYS);*/
			PreparedStatement stat = con.getConnection().prepareStatement(
					"INSERT INTO " + TABLE_CONECTION + " (`fecConexion`,`pais`,`ciudad`,`region`,`numFacilitadas`, `numFallidas`, `numIncorrectas`, `status`,`dialogo`,`avatar_md5`,`ip`) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					PreparedStatement.RETURN_GENERATED_KEYS);
			stat.setObject(1, conection.getFechaConexion());
			stat.setString(2, conection.getCountry());
			stat.setString(3, conection.getCity());
			stat.setString(4, conection.getRegion());
			stat.setInt(5, conection.getNumFacilitadas());
			stat.setInt(6, conection.getNumFallidas());
			stat.setInt(7, conection.getNumIncorrectas());
			stat.setString(8, status);
			stat.setString(9, conection.getDialogo());
			stat.setString(10, conection.getMd5());
			stat.setString(11, conection.getIP());

			stat.executeUpdate();

			ResultSet rs = stat.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getInt(1);
			}

			stat.close();
			//con.desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
}
