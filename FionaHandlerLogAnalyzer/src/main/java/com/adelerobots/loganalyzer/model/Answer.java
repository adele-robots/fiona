package com.adelerobots.loganalyzer.model;

import java.sql.Timestamp;

public class Answer {
	private Timestamp fechaRespuesta;
	private String status;
	private String topic;
	private String text;
	private String questionText;

	public static final String FACILITADA = "y";
	public static final String FALLIDA = "n";
	public static final String INCORRECTA = "b";

	public Answer(Timestamp fechaRespuesta, String text) {
		super();
		this.fechaRespuesta = fechaRespuesta;
		this.text = text;
		this.status = FACILITADA;
		this.topic = "";
		this.questionText = "";
	}

	public Answer(Timestamp fechaRespuesta, String text, String questionText) {
		super();
		this.fechaRespuesta = fechaRespuesta;
		this.text = text;
		this.status = FACILITADA;
		this.topic = "";
		this.questionText = questionText;
	}

	public Timestamp getFechaRespuesta() {
		return fechaRespuesta;
	}

	public void setFechaRespuesta(Timestamp fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	@Override
	public String toString() {
		String cad = "\tRESPUESTA: " + fechaRespuesta + "\tEstado: ";

		if (FACILITADA.equals(status))
			cad += "FACILITADA\tTopic:" + topic;
		else if (FALLIDA.equals(status))
			cad += "FALLIDA\t";
		else if (INCORRECTA.equals(status))
			cad += "INCORRECTA\t";

		return cad + "\t" + text + "\n";
	}

}
