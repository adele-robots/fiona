package com.adelerobots.loganalyzer.persistence;

import com.adelerobots.loganalyzer.LogAnalizer;
import com.adelerobots.loganalyzer.model.Answer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AnswerDAO {

	final static String TABLE_ANSWER = "RESPUESTA";

	/**
	 * Añade una nueva fila a la tabla RESPUESTA, la cual tiene una foreign key
	 * de la tabla CONEXION
	 * 
	 * @param idConexion
	 *            foreign key de la tabla CONEXION
	 * @param answer
	 */
	public static void aniadirRespuesta(Integer idConexion, Answer answer) {
		try {
			AnalizerDB con = LogAnalizer.con;

			/*java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			String date = sdf.format(answer.getFechaRespuesta());
			Statement stat = con.getConnection().createStatement();

			stat.executeUpdate("INSERT INTO "
					+ TABLE_ANSWER
					+ " (`fecRespuesta`, `status`,`topic`, `texto`, `textoPregunta`, `conexion_idConexion`) VALUES ('"
					+ date + "', '" + status + "', '" + answer.getTopic()
					+ "', '" + answer.getText() + "', '" + answer.getQuestionText() + "', '" + idConexion + "')");*/
			PreparedStatement stat = con.getConnection().prepareStatement(
					"INSERT INTO " + TABLE_ANSWER + " (`fecRespuesta`, `status`,`topic`, `texto`, `textoPregunta`, `conexion_idConexion`) VALUES ( ?, ?, ?, ?, ?, ?)");
			stat.setObject(1, answer.getFechaRespuesta());
			stat.setString(2, answer.getStatus());
			stat.setString(3, answer.getTopic());
			stat.setString(4, answer.getText());
			stat.setString(5, answer.getQuestionText());
			stat.setInt(6, idConexion);

			stat.executeUpdate();

			stat.close();
			//con.desconectar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
