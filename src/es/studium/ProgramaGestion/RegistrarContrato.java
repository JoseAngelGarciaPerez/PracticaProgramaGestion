package es.studium.ProgramaGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistrarContrato implements WindowListener, ActionListener
{
	/* Elementos */
	// Ventana
	Frame ventana = new Frame("Registrar Contrato");

	// Choices de clientes y empleados
	Choice choClientes = new Choice();
	Choice choEmpleados = new Choice();

	// Labels
	Label lblCliente = new Label("Seleccione al cliente:");
	Label lblEmpleado = new Label("Seleccione al empleado contratado:");
	Label lblFecha = new Label("Indica la fecha de registro (dd-mm-yyyy):");

	// TextField
	TextField txtFecha = new TextField(10);

	// Botones
	Button btnRegistrar = new Button("Registrar");

	// Dialogo
	Dialog dlgMensaje = new Dialog(ventana, "Registro", true);
	Label lblMensaje = new Label();

	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public RegistrarContrato()
	{
		ventana.setLayout(new FlowLayout());

		ventana.add(lblCliente);
		ventana.add(choClientes);
		rellenarChoiceClientes();

		ventana.add(lblEmpleado);
		ventana.add(choEmpleados);
		rellenarChoiceEmpleados();

		ventana.add(lblFecha);
		ventana.add(txtFecha);

		ventana.add(btnRegistrar);
		btnRegistrar.addActionListener(this);

		ventana.addWindowListener(this);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setSize(250, 225);
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
		if (dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		} else
		{
			ventana.setVisible(false);
		}
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
		if (ae.getSource().equals(btnRegistrar))
		{
			String[] cliente = choClientes.getSelectedItem().split("-");
			String[] empleado = choEmpleados.getSelectedItem().split("-");
			String[] fecha = txtFecha.getText().split("-");

			connection = bd.conectar();
			sentencia = "INSERT INTO contratos VALUES(null,'" + fecha[2] + "-" + fecha[1] + "-" + fecha[0] + "' , "
					+ cliente[0] + "," + empleado[0] + ")";
			try
			{
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				statement.executeUpdate(sentencia);
				lblMensaje.setText("Registro completado");
			} catch (SQLException e)
			{
				e.printStackTrace();
				lblMensaje.setText("ERROR en el registro");

			}

			dlgMensaje.setLayout(new FlowLayout());
			dlgMensaje.add(lblMensaje);

			dlgMensaje.addWindowListener(this);
			dlgMensaje.setSize(150, 80);
			dlgMensaje.setResizable(false);
			dlgMensaje.setLocationRelativeTo(null);
			dlgMensaje.setVisible(true);
		}

	}

	private void rellenarChoiceClientes()
	// Metodo que rellena el choice con todos los clientes
	{
		// Conectar
		connection = bd.conectar();
		// Selecciona todos los clientes
		sentencia = "SELECT * FROM clientes";
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			choClientes.removeAll();
			// Mientras encuentre clientes
			while (rs.next())
			{
				// Añade el cliente al Choice
				choClientes.add(rs.getInt("idCliente") + "-" + rs.getString("nombreCliente") + " "
						+ rs.getString("apellidosCliente") + "-" + rs.getString("dniCliente"));
			}
		} catch (SQLException sqle)
		{

		}
	}

	private void rellenarChoiceEmpleados()
	// Metodo que rellena el choice de los empleados
	{
		// Conectar
		connection = bd.conectar();
		// Selecciona todos los empleados
		sentencia = "SELECT * FROM empleados";
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			choEmpleados.removeAll();
			// Mientras encuentre empleados
			while (rs.next())
			{
				// Añade el empleado al Choice
				choEmpleados.add(rs.getInt("idEmpleados") + "-" + rs.getString("nombreEmpleados") + " "
						+ rs.getString("apellidosEmpleado") + "-" + rs.getString("dniEmpleado"));
			}
		} catch (SQLException sqle)
		{

		}
	}
}
