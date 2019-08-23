package com.adelerobots.fioneg.dataclasses;

/**
 * Clase auxiliar utilizada para representar los datos necesarios
 * para construir el archivo de pagos a los sparkers, o alternativamente,
 * las llamadas a la API de Paypal con el método 'MassPay'
 * 
 * @author adele
 *
 */
public class PaymentReceiver {
	
	private String email;
	private String amount;
	private String note;
	
	public PaymentReceiver(){
		
	}
	
	public PaymentReceiver(String email, String amount){
		this.email = email;
		this.amount = amount;
	}
	
	public PaymentReceiver(String email, String amount, String note){
		this.email = email;
		this.amount = amount;
		this.note = note;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	

}
