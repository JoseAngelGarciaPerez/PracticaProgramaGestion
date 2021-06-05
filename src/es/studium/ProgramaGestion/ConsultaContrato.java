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
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ConsultaContrato implements WindowListener, ActionListener
{

	/* Elementos */

	// Frame
	Frame ventana = new Frame("Consulta Contratos");
	// TextArea
	TextArea listadoContratos = new TextArea(8, 40);

	// Botón PDF
	Button btnPDFContratos = new Button("PDF");

	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	// Base de datos 2 (para el FK de cliente)
	String sentencia2 = "";
	Connection connection2 = null;
	Statement statement2 = null;
	ResultSet rs2 = null;
	int idCliente;
	String nombreCliente = "";

	// Base de datos 3 (para el FK de empleado)
	String sentencia3 = "";
	Connection connection3 = null;
	Statement statement3 = null;
	ResultSet rs3 = null;
	int idEmpleado;
	String nombreEmpleado = "";

	public ConsultaContrato()
	{
		ventana.setLayout(new FlowLayout());
		// Conectar
		connection = bd.conectar();
		// Seleccionar todos los contratos
		sentencia = "SELECT * FROM contratos";
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
			listadoContratos.selectAll();
			listadoContratos.setText("");
			listadoContratos.append("Nº\tCliente\t\tEmpleado\tFecha\n");
			// Mientras encuentre empleados
			while (rs.next())
			{
				idCliente = rs.getInt("idClienteFK");
				idEmpleado = rs.getInt("idEmpleadoFK");

				datosCliente();
				datosEmpleado();

				String[] fechaAmericana = rs.getString("fechaContrato").split("-");
				String fechaEuropea = fechaAmericana[2] + "-" + fechaAmericana[1] + "-" + fechaAmericana[0];

				// Introduce el contrato en el listado
				listadoContratos.append(rs.getInt("idContrato") + "\t" + nombreCliente + "\t" + nombreEmpleado + "\t"
						+ fechaEuropea + "\n");

			}

			new LogMovimientos(
					"[" + LogFechaHora.fechaActual() + "][" + LogMovimientos.usuario + "][Consultando contratos]");
		} catch (SQLException sqle)
		{

		} finally
		{
			listadoContratos.setEditable(false);
			ventana.add(listadoContratos);
			btnPDFContratos.addActionListener(this);
			ventana.add(btnPDFContratos);

			ventana.setSize(400, 220);
			ventana.setResizable(false);
			ventana.setLocationRelativeTo(null);
			ventana.addWindowListener(this);
			ventana.setVisible(true);
		}
	}

	public void datosCliente()
	{
		connection2 = bd.conectar();
		sentencia2 = "SELECT * FROM clientes WHERE idCliente = " + idCliente;

		try
		{
			// Crear una sentencia
			statement2 = connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs2 = statement2.executeQuery(sentencia2);

			while (rs2.next())
			{

				nombreCliente = rs2.getString("nombreCliente") + " " + rs2.getString("apellidosCliente");

			}

		} catch (SQLException e)
		{
			e.printStackTrace();

		}

	}

	public void datosEmpleado()
	{
		connection3 = bd.conectar();
		sentencia3 = "SELECT * FROM empleados WHERE idEmpleados = " + idEmpleado;

		try
		{
			// Crear una sentencia
			statement3 = connection3.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs3 = statement3.executeQuery(sentencia3);

			while (rs3.next())
			{

				nombreEmpleado = rs3.getString("nombreEmpleados") + " " + rs3.getString("apellidosEmpleado");

			}

		} catch (SQLException e)
		{
			e.printStackTrace();

		}

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
		if (ae.getSource().equals(btnPDFContratos))
		{// Se crea el documento
			Document documento = new Document();
			try
			{
			// Se crea el OutputStream para el fichero donde queremos dejar el pdf.
			FileOutputStream ficheroPdf = new FileOutputStream("Contratos.pdf");
			// Se asocia el documento al OutputStream y se indica que el espaciado entre
			// líneas será de 20. Esta llamada debe hacerse antes de abrir el documento
			PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
			// Se abre el documento.
			documento.open();
			documento.add(new Paragraph("Programa de Gestión"));
			documento.add(new Paragraph("Listado de Contratos",
			FontFactory.getFont("arial", // fuente
			22, // tamaño
			Font.ITALIC, // estilo
			BaseColor.RED))); // color
			documento.add(new Paragraph(" "));
			PdfPTable tabla = new PdfPTable(4);
			tabla.addCell("ID del Contrato");
			tabla.addCell("Nombre del Cliente");
			tabla.addCell("Nombre del Empleado");
			tabla.addCell("Fecha");

			connection = bd.conectar();
			// Seleccionar todos los clientes
			sentencia = "SELECT * FROM contratos";
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
					idCliente = rs.getInt("idClienteFK");
					idEmpleado = rs.getInt("idEmpleadoFK");

					datosCliente();
					datosEmpleado();

					String[] fechaAmericana = rs.getString("fechaContrato").split("-");
					String fechaEuropea = fechaAmericana[2] + "-" + fechaAmericana[1] + "-" + fechaAmericana[0];

					// Introduce el contrato en el PDF
					tabla.addCell(rs.getInt("idContrato") + "");
					tabla.addCell(nombreCliente);
					tabla.addCell(nombreEmpleado);
					tabla.addCell(fechaEuropea);
				}
				
				new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"][PDF de contratos creado]");
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
			File path = new File ("Contratos.pdf");
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
