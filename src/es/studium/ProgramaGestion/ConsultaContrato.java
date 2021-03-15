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

public class ConsultaContrato implements WindowListener
{
	
	/* Elementos */

	// Frame
	Frame ventana = new Frame("Consulta Contratos");
	// TextArea
	TextArea listadoContratos = new TextArea(8, 40);
	
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

				// Introduce el contrato en el listado
				listadoContratos.append(rs.getInt("idContrato") + "\t" + nombreCliente + "\t" + nombreEmpleado + "\t" + rs.getDate("fechaContrato") + "\n");

			}
		} catch (SQLException sqle)
		{

		} finally
		{
			listadoContratos.setEditable(false);
			ventana.add(listadoContratos);

			ventana.setSize(400, 200);
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

}
