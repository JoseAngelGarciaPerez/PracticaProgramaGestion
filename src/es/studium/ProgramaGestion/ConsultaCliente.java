package es.studium.ProgramaGestion;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaCliente implements WindowListener, ActionListener
{
	/* Elementos */
	
	// Frame
	Frame ventana = new Frame("Consulta Clientes");
	// TextArea
	TextArea listadoClientes = new TextArea(4, 30);
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
		} catch (SQLException sqle)
		{

		} finally
		{

		}
		listadoClientes.setEditable(false);
		ventana.add(listadoClientes);

		ventana.setSize(300, 140);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.addWindowListener(this);
		ventana.setVisible(true);
	}

	public static void main(String[] args)
	{
		new ConsultaCliente();

	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		// TODO Auto-generated method stub

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
