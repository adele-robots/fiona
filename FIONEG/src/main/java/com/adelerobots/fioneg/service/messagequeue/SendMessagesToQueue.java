package com.adelerobots.fioneg.service.messagequeue;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SendMessagesToQueue extends ServicioNegocio {
	
	public interface IPCLibrary extends Library {
		IPCLibrary INSTANCE = (IPCLibrary)		
		Native.loadLibrary("c",IPCLibrary.class);

		class MsgBuf extends Structure{
			NativeLong mtype; /* type of message */
			byte mtext[] = new byte[1];
		}

		/* Equivalente a la construcción de mi propia estructura
		 * en C para el intercambio de mensajes */
		class MyMsgBuf extends MsgBuf{
			public NativeLong tipoMensaje;
			public byte[] contenido = new byte[1024];
		}
		
		// Inicializar cola, o si existe, recuperar 
		// int msgget(key_t key, int msgflag);
		int msgget(NativeLong key, int msgflg);
		// Enviar mensaje a la cola de mensajes
		// int msgsnd(int msqid, struct msgbuf *ptrkey, int length, int flag);
		int msgsnd(int msqid, MsgBuf ptrkey, int msgsz, int msgflg);
		// Recibir mensaje en la cola de mensajes
		// int msgrcv(int msqid, struct msgbuf *ptrkey, int length, long msgtype, int flag);
		int msgrcv(int msqid, MsgBuf ptrkey, int length, long msgtype, int flag);	       
	}
	
	private static final int CTE_POSICION_PID = 0;
	private static final int CTE_POSICION_MENSAJE = 1;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SendMessagesToQueue.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029013: Enviar mensaje a cola de mensajes de ChatSpark");		
				
		final BigDecimal pidDecimal = datosEntrada.getDecimal(CTE_POSICION_PID);
		final String message = datosEntrada.getString(CTE_POSICION_MENSAJE);
		
		Integer pid = pidDecimal.intValue();
		
		// Recuperar la cola existente
		int idCola = IPCLibrary.INSTANCE.msgget(new NativeLong(pid), 0);
		
		 if(idCola<0){// Error al recuperar la cola de mensajes
			LOGGER.error("No se ha posido crear la cola de mensajes. IdCola:"+idCola);        	
			LOGGER.error("Error msgget: " + Native.getLastError());
			LOGGER.error("PID recibido: " + pid);
        }else{
        	System.out.println("Se ha recuperado la cola de mensajes con id:" + idCola);        	
        	// Enviar mensaje a la cola de mensajes
        	IPCLibrary.MyMsgBuf mensaje = new IPCLibrary.MyMsgBuf();
        	mensaje.tipoMensaje = new NativeLong(1);
        	//mensaje.contenido = message.getBytes();
        	try {
				mensaje.contenido = message.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {				
				e.printStackTrace();
			}        	
        	int devSend = IPCLibrary.INSTANCE.msgsnd(idCola, mensaje, mensaje.contenido.length, 1);
        	if(devSend != 0){
        		LOGGER.error("Devolución del send: "+devSend);
        		LOGGER.error("Valor del error: " + Native.getLastError());
        	}
        }
		 
		 return new IContexto[0];
	}
}
