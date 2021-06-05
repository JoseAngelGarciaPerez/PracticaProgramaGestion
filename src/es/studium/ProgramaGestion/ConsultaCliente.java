package es.studium.ProgramaGestion;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ConsultaCliente implements WindowListener, ActionListener
{
	/* Elementos */
	
	// Frame
	Frame ventana = new Frame("Consulta Clientes");
	// TextArea
	TextArea listadoClientes = new TextArea(4, 30);
	//Botón para PDF
	Button btnPDFClientes = new Button("PDF");
	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ConsultaCliente()
	{
		ventana.setLayout(new FlowLayout());
		// Conectar
		connection = bd.conectar();
		// Seleccionar todos los clientes
		sentencia = "SELECT * FROM clientes";
		// La información está en ResultSet
		// Recorrer el RS y por cada registro,
		// meter una línea en el TextArea
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			listadoClientes.selectAll();
			listadoClientes.setText("");
			listadoClientes.append("Nº\tNombre\tApellidos\tDNI\n");
			// Mientras encuentre clientes
			while (rs.next())
			{
				// Introduce el cliente en el listado
				listadoClientes.append(rs.getInt("idCliente") + "\t" + rs.getString("nombreCliente") + "\t"
						+ rs.getString("apellidosCliente") + "\t" + rs.getString("dniCliente") + "\n");
			}
			
			new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"][Consultando clientes]");
		} catch (SQLException sqle)
		{

		} finally
		{

		}
		listadoClientes.setEditable(false);
		ventana.add(listadoClientes);
		btnPDFClientes.addActionListener(this);
		ventana.add(btnPDFClientes);

		ventana.setSize(300, 160);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.addWindowListener(this);
		ventana.setVisible(true);
	}


	@Override
	public void windowActivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		ventana.setVisible(false);

	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{
		// TODO Auto-generated method stub

	}


	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource().equals(btnPDFClientes)) 
		{
			// Se crea el documento
			Document documento = new Document();
			try
			{
			// Se crea el OutputStream para el fichero donde queremos dejar el pdf.
			FileOutputStream ficheroPdf = new FileOutputStream("Clientes.pdf");
			// Se asocia el documento al OutputStream y se indica que el espaciado entre
			// líneas será de 20. Esta llamada debe hacerse antes de abrir el documento
			PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
			// Se abre el documento.
			documento.open();
			documento.add(new Paragraph("Programa de Gestión"));
			documento.add(new Paragraph("Listado de Clientes",
			FontFactory.getFont("arial", // fuente
			22, // tamaño
			Font.ITALIC, // estilo
			BaseColor.RED))); // color
			documento.add(new Paragraph(" "));
			PdfPTable tabla = new PdfPTable(4);
			tabla.addCell("ID del Cliente");
			tabla.addCell("Nombre");
			tabla.addCell("Apellidos");
			tabla.addCell("DNI");

			connection = bd.conectar();
			// Seleccionar todos los clientes
			sentencia = "SELECT * FROM clientes";
			// La información está en ResultSet
			// Recorrer el RS y por cada registro,
			// meter una línea en el TextArea
			try
			{
				// Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				// Crear un objeto ResultSet para guardar lo obtenido
				// y ejecutar la sentencia SQL
				rs = statement.executeQuery(sentencia);
				// Mientras encuentre clientes
				while (rs.next())
				{
					tabla.addCell(rs.getInt("idCliente")+"");
					tabla.addCell(rs.getString("nombreCliente"));
					tabla.addCell(rs.getString("apellidosCliente"));
					tabla.addCell(rs.getString("dniCliente"));
					
					// Introduce el cliente en el listado
					listadoClientes.append(rs.getInt("idCliente") + "\t" + rs.getString("nombreCliente") + "\t"
							+ rs.getString("apellidosCliente") + "\t" + rs.getString("dniCliente") + "\n");
				}
				
				new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"][PDF de clientes creado]");
			} catch (SQLException sqle)
			{

			} finally
			{

			}
			
			documento.add(tabla);
			documento.close();
			//Abrimos el archivo PDF recién creado
			try
			{
			File path = new File ("Clientes.pdf");
			Desktop.getDesktop().open(path);
			}
			catch (IOException ex)
			{
			System.out.println("Se ha producido un error al abrir el archivo PDF");
			}
			}
			catch ( Exception e )
			{
			e.printStackTrace();
			}
		}
		
	}

}
