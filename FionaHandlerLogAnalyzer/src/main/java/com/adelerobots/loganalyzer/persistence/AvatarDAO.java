package com.adelerobots.loganalyzer.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.adelerobots.loganalyzer.LogAnalizer;
import com.adelerobots.loganalyzer.model.Avatar;

public class AvatarDAO {
	static final String TABLE_AVATAR = "AVATAR";

	/**
	 * Añade una nueva fila a la tabla AVATAR
	 * 
	 * @param avatar
	 */
	public static void aniadirAvatar(Avatar avatar) {

		try {
			AnalizerDB con = LogAnalizer.con;

			/*Statement stat = con.getConnection().createStatement();
			
			stat.executeUpdate("INSERT INTO " + TABLE_AVATAR + " (`avatarName`,`md5`,`email`) VALUES ('"
					+ avatar.getName() + "', '"
					+ avatar.getMd5() + "', '"
					+ avatar.getEmail() + "')");*/
			PreparedStatement stat = con.getConnection().prepareStatement(
					"INSERT INTO " + TABLE_AVATAR + " (`avatarName`,`md5`,`email`) VALUES ( ?, ?, ?)");
			stat.setString(1, avatar.getName());
			stat.setString(2, avatar.getMd5());
			stat.setString(3, avatar.getEmail());

			stat.executeUpdate();

			stat.close();
			//con.desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Busca todos los elementos de la tabla AVATAR y los recoge en un ArrayList
	 * 
	 * @return
	 */
	public static ArrayList<Avatar> getAvatars() {

		ArrayList<Avatar> avatares = new ArrayList<Avatar>();
		try {
			AnalizerDB con = new AnalizerDB();
			Statement st = con.getConnection().createStatement();

			ResultSet rs = st.executeQuery("SELECT * FROM " + TABLE_AVATAR);

			while (rs.next()) {

				String md5 = rs.getString("md5");
				Avatar avatar = new Avatar(md5);

				System.out.println(avatar);
				avatares.add(avatar);

			}
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avatares;

	}

	/**
	 * Busca el elemento de la tabla AVATAR que corresponda con un determinado
	 * md5
	 * 
	 * @param md5
	 * @return
	 */
	public static Avatar getAvatar(String md5) {
		Avatar avatar = null;
		try {
			AnalizerDB con = new AnalizerDB();
			PreparedStatement st = con.getConnection().prepareStatement(
					"SELECT * FROM " + TABLE_AVATAR + " WHERE md5= ?");
			st.setString(1, md5);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				avatar = new Avatar(md5);
				break;
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avatar;

	}

}
