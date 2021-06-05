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

public class ConsultaEmpleado implements WindowListener, ActionListener
{
	/* Elementos */

	// Frame
	Frame ventana = new Frame("Consulta Empleados");
	// TextArea
	TextArea listadoEmpleados = new TextArea(8, 40);
	//Botón PDF
	Button btnPDFEmpleado = new Button("PDF");
	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	// Base de datos 2 (para el FK de jefe)
	String sentencia2 = "";
	Connection connection2 = null;
	Statement statement2 = null;
	ResultSet rs2 = null;

	// Subordinado de un jefe
	String subordinado = "";
	int idSubordinado;

	public ConsultaEmpleado()
	{
		ventana.setLayout(new FlowLayout());
		// Conectar
		connection = bd.conectar();
		// Seleccionar todos los empleados
		sentencia = "SELECT * FROM empleados";
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
			listadoEmpleados.selectAll();
			listadoEmpleados.setText("");
			listadoEmpleados.append("Nº\tNombre\tApellidos\tDNI\tSubordinado\n");
			// Mientras encuentre empleados
			while (rs.next())
			{

				// Introduce el empleado en el listado
				listadoEmpleados.append(rs.getInt("idEmpleados") + "\t" + rs.getString("nombreEmpleados") + "\t"
						+ rs.getString("apellidosEmpleado") + "\t" + rs.getString("dniEmpleado") + "\t");
				idSubordinado = rs.getInt("idEmpleadoJefeFK");
				encontrarSubordinado();
				listadoEmpleados.append(subordinado + "\n");
			}
			
			new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"][Consultando empleados]");
		} catch (SQLException sqle)
		{

		} finally
		{
			listadoEmpleados.setEditable(false);
			ventana.add(listadoEmpleados);
			btnPDFEmpleado.addActionListener(this);
			ventana.add(btnPDFEmpleado);

			ventana.setSize(400, 220);
			ventana.setResizable(false);
			ventana.setLocationRelativeTo(null);
			ventana.addWindowListener(this);
			ventana.setVisible(true);
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

	public void encontrarSubordinado()
	{
		connection2 = bd.conectar();
		sentencia2 = "SELECT * FROM empleados WHERE idEmpleados = " + idSubordinado;

		try
		{
			if (idSubordinado == 0)
			{
				subordinado = "Nadie";
			} else
			{
				// Crear una sentencia
				statement2 = connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				// Crear un objeto ResultSet para guardar lo obtenido
				// y ejecutar la sentencia SQL
				rs2 = statement2.executeQuery(sentencia2);
				
				while(rs2.next()) {

					subordinado = rs2.getString("nombreEmpleados") + " " + rs2.getString("apellidosEmpleado");
					
				}
			}
		} catch (SQLException e)
		{
			e.printStackTrace();

		}

	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource().equals(btnPDFEmpleado)) 
		{
			// Se crea el documento
						Document documento = new Document();
						try
						{
						// Se crea el OutputStream para el fichero donde queremos dejar el pdf.
						FileOutputStream ficheroPdf = new FileOutputStream("Empleados.pdf");
						// Se asocia el documento al OutputStream y se indica que el espaciado entre
						// líneas será de 20. Esta llamada debe hacerse antes de abrir el documento
						PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);
						// Se abre el documento.
						documento.open();
						documento.add(new Paragraph("Programa de Gestión"));
						documento.add(new Paragraph("Listado de Empleados",
						FontFactory.getFont("arial", // fuente
						22, // tamaño
						Font.ITALIC, // estilo
						BaseColor.RED))); // color
						documento.add(new Paragraph(" "));
						PdfPTable tabla = new PdfPTable(5);
						tabla.addCell("ID del Empleado");
						tabla.addCell("Nombre");
						tabla.addCell("Apellidos");
						tabla.addCell("DNI");
						tabla.addCell("Subordinado");

						connection = bd.conectar();
						// Seleccionar todos los clientes
						sentencia = "SELECT * FROM empleados";
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
								tabla.addCell(rs.getInt("idEmpleados")+"");
								tabla.addCell(rs.getString("nombreEmpleados"));
								tabla.addCell(rs.getString("apellidosEmpleado"));
								tabla.addCell(rs.getString("dniEmpleado"));
								idSubordinado = rs.getInt("idEmpleadoJefeFK");
								encontrarSubordinado();
								tabla.addCell(subordinado);
							}
							
							new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"][PDF de empleados creado]");
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
						File path = new File ("Empleados.pdf");
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
