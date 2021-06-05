package es.studium.ProgramaGestion;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFechaHora
{
	public static String fechaActual(){
		Date ahora;
		SimpleDateFormat formateador;
		String fecha="";
		String hora="";
		String completo="";
	
		ahora = new Date();
	
		formateador = new SimpleDateFormat("dd/MM/yyyy");
		fecha = formateador.format(ahora);
	
		formateador = new SimpleDateFormat("HH:mm:ss");
		hora = formateador.format(ahora);
		
		completo= fecha + "  " + hora;
		
		return completo;
	}
}
