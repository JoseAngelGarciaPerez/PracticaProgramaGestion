package es.studium.ProgramaGestion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogMovimientos
{
	static String usuario="";
	
	public LogMovimientos(String log) {
	//FileWriter también puede lanzar una excepción
			try
			{
	// Destino de los datos
				FileWriter fw = new FileWriter("movimientos.log", true);
	// Buffer de escritura
				BufferedWriter bw = new BufferedWriter(fw);
	// Objeto para la escritura
				PrintWriter salida = new PrintWriter(bw);
	//Guardamos la cadena
				salida.println(log);
	//Cerrar el objeto salida, el objeto bw y el fw
				salida.close();
				bw.close();
				fw.close();
				System.out.println("Log guardado correctamente");
			} catch (IOException i)
			{
				System.out.println("Se produjo un error de Archivo");
			}
			}
}
