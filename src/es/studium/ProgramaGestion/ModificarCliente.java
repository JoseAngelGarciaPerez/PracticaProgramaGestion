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

public class ModificarCliente implements WindowListener, ActionListener
{

	/* Elementos */
	
	// Frame
	Frame ventana = new Frame("Modificar Cliente");

	// Labels y TextFields
	Label lblSeleccion = new Label("Seleccione al cliente:");

	// Choice
	Choice choClientes = new Choice();

	// Boton
	Button btnSeleccionar = new Button("Seleccionar");

	// Ventana de Modificacion
	Label lblIdCliente = new Label("Cliente seleccionado: ");

	Label lblNombre = new Label("Nombre:");
	TextField txtNombre = new TextField(20);

	Label lblApellidos = new Label("Apellidos:");
	TextField txtApellidos = new TextField(20);

	Label lblDni = new Label("DNI:");
	TextField txtDni = new TextField(20);

	Label lblTelefono = new Label("Telefono:");
	TextField txtTelefono = new TextField(20);

	Label lblDireccion = new Label("Direccion:");
	TextField txtDireccion = new TextField(20);

	Button btnModificar = new Button("Modificar");

	String idElegido = "";

	// Dialogo de confirmacion
	Dialog dlgConfirmacion = new Dialog(ventana, "Modificacion", true);
	Label lblConfirmacion = new Label();

	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ModificarCliente()
	{
		ventana.setLayout(new FlowLayout());
		ventana.add(lblSeleccion);
		rellenarChoice();
		ventana.add(choClientes);
		btnSeleccionar.addActionListener(this);
		ventana.add(btnSeleccionar);

		ventana.setSize(250, 140);
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
		// Si se selecciona un cliente del Choice
		if (ae.getSource().equals(btnSeleccionar))
		{
			ventanaModificar();
		}
		// Si se selecciona el boton "btnModificar"
		if (ae.getSource().equals(btnModificar))
		{
			connection = bd.conectar();
			sentencia = "SELECT * FROM clientes WHERE idCliente = " + idElegido;
			new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");
			
			try
			{
				// Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				// Crear un objeto ResultSet para guardar lo obtenido
				// y ejecutar la sentencia SQL
				rs = statement.executeQuery(sentencia);
				// Sentencia que actualiza los datos del cliente con los datos de los TextFields
				sentencia = "UPDATE clientes SET nombreCliente = '" + txtNombre.getText() + "',"
						+ " apellidosCliente = '" + txtApellidos.getText() + "', dniCliente = '" + txtDni.getText()
						+ "'," + " telefonoCliente = " + txtTelefono.getText() + ", direccionCliente = '"
						+ txtDireccion.getText() + "' " + "WHERE (idCliente = '" + idElegido + "')";
				statement.executeUpdate(sentencia);
				lblConfirmacion.setText("Modificacion realizada");
				
				new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");

			// Si hay una excepcion SQL
			} catch (SQLException sqle)
			{
				lblConfirmacion.setText("ERROR en la modificacion");
			} finally
			{
				rellenarChoice();
				dlgConfirmacion.setLayout(new FlowLayout());
				dlgConfirmacion.addWindowListener(this);
				dlgConfirmacion.setSize(150, 100);
				dlgConfirmacion.setResizable(false);
				dlgConfirmacion.setLocationRelativeTo(null);
				dlgConfirmacion.add(lblConfirmacion);
				dlgConfirmacion.setVisible(true);
			}
		}

	}

	public void ventanaModificar()
	// Metodo que selecciona al cliente elegido y modifica el frame "ventana" para mostrar los datos del cliente
	{
		String[] elegido = choClientes.getSelectedItem().split("-");
		lblIdCliente.setText("Número de ID del cliente selecionado: " + elegido[0]);
		ventana.add(lblIdCliente);
		ventana.add(lblNombre);
		ventana.add(txtNombre);
		ventana.add(lblApellidos);
		ventana.add(txtApellidos);
		ventana.add(lblDni);
		ventana.add(txtDni);
		ventana.add(lblTelefono);
		ventana.add(txtTelefono);
		ventana.add(lblDireccion);
		ventana.add(txtDireccion);

		// Conectar
		connection = bd.conectar();
		// Hacer un SELECT * FROM clientes

		idElegido = elegido[0];
		sentencia = "SELECT * FROM clientes WHERE idCliente = " + elegido[0];
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

			// Si encuentra al cliente
			while (rs.next())
			{
				// Introduce en los TextFields los datos del cliente
				txtNombre.setText(rs.getString("nombreCliente"));
				txtApellidos.setText(rs.getString("apellidosCliente"));
				txtDni.setText(rs.getString("dniCliente"));
				txtTelefono.setText(rs.getString("telefonoCliente"));
				txtDireccion.setText(rs.getString("direccionCliente"));
			}
			// Si no encuentra al cliente
		} catch (SQLException sqle)
		{
			System.out.println("ERROR");
		} 
		finally
		{
			ventana.add(btnModificar);
			btnModificar.addActionListener(this);

			ventana.addWindowListener(this);
			ventana.setSize(300, 300);
			ventana.setResizable(false);
			ventana.setLocationRelativeTo(null);
			ventana.setVisible(true);
		}


	}
	
	private void rellenarChoice()
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

}
