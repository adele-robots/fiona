package com.adelerobots.fioneg.service.messagequeue;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import com.adelerobots.fioneg.context.ContextoMensaje;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class ReceiveMessagesFromQueue extends ServicioNegocio {
	
	public interface IPCLibrary extends Library {
		public static final int IPC_NOWAIT = 04000;

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
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(ReceiveMessagesFromQueue.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029042: Recibir mensaje de la cola de mensajes de ChatSpark");		
				
		IContexto[] salida = new IContexto[0];
		
		final BigDecimal pidDecimal = datosEntrada.getDecimal(CTE_POSICION_PID);				
		Integer pid = pidDecimal.intValue();
		
		// Recuperar la cola existente
		int idCola = IPCLibrary.INSTANCE.msgget(new NativeLong(pid), 0);
		
		 if(idCola<0){// Error al recuperar la cola de mensajes
			LOGGER.error("No se ha posido crear la cola de mensajes. IdCola:"+idCola);        	
			LOGGER.error("Error msgget: " + Native.getLastError());
			LOGGER.error("PID recibido: " + pid);
        }else{
        	System.out.println("Se ha recuperado la cola de mensajes con id:" + idCola);       	
        }
		 
		// Recibir mensaje de respuesta
        IPCLibrary.MyMsgBuf mensajeRecibido =  new IPCLibrary.MyMsgBuf();        
        int bytesRecibidos = IPCLibrary.INSTANCE.msgrcv(idCola, mensajeRecibido, mensajeRecibido.contenido.length, 2, IPCLibrary.IPC_NOWAIT);
        String contenido = "";
        if(bytesRecibidos > 0){
        	try {
        		contenido = new String(mensajeRecibido.contenido,"UTF-8");
        		int end = contenido.indexOf("\0");
        		if ( end == -1 ) {
        			LOGGER.error("El mensaje C no termina en \\0 , por lo que es mayor que el buffer(1024) y esta truncado");
        		}
        		else {
        			contenido = contenido.substring(0, end);
        		}
        		LOGGER.info("Se ha recibido el mensaje C: " + contenido);
        		salida = ContextoMensaje.rellenarContexto(contenido);
        	} catch (UnsupportedEncodingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }else{
        	if(Native.getLastError() != 42){
        		LOGGER.error("Valor del error en el msgrcv: " + Native.getLastError());
        	}        	
        }       	
		
		return salida;		
	}
}
