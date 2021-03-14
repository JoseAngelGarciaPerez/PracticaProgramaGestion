package es.studium.ProgramaGestion;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaEmpleado implements WindowListener
{
	/* Elementos */
	
	// Frame
	Frame ventana = new Frame("Consulta Empleados");
	// TextArea
	TextArea listadoEmpleados = new TextArea(4, 30);
	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

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
			listadoEmpleados.append("Nº\tNombre\tApellidos\tDNI\n");
			// Mientras encuentre empleados
			while (rs.next())
			{
				// Introduce el empleado en el listado
				listadoEmpleados.append(rs.getInt("idEmpleados") + "\t" + rs.getString("nombreEmpleados") + "\t"
						+ rs.getString("apellidosEmpleado") + "\t" + rs.getString("dniEmpleado") + "\n");
			}
		} catch (SQLException sqle)
		{

		} finally
		{
			listadoEmpleados.setEditable(false);
			ventana.add(listadoEmpleados);

			ventana.setSize(300, 140);
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

}
