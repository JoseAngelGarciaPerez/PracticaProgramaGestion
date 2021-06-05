package es.studium.ProgramaGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BajaCliente implements WindowListener, ActionListener
{
	/* Elementos */
	// Frame
	Frame ventana = new Frame("Baja de Cliente");
	// Label
	Label lblMensajeBajaCliente = new Label("Selecciona el cliente a borrar:");
	// Choice
	Choice choClientes = new Choice();
	// Botones
	Button btnBorrarCliente = new Button("Borrar");

	// Dialogo de confirmacion
	Dialog dlgSeguroCliente = new Dialog(ventana, "¿Seguro?", true);
	Label lblSeguroCliente = new Label("¿Está seguro del borrado?");
	Button btnSiSeguroCliente = new Button("Sí");
	Button btnNoSeguroCliente = new Button("No");
	Dialog dlgConfirmacionBajaCliente = new Dialog(ventana, "Baja Cliente", true);
	Label lblConfirmacionBajaCliente = new Label("Baja de cliente correcta");

	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BajaCliente()
	{
		ventana.setLayout(new FlowLayout());
		ventana.add(lblMensajeBajaCliente);
	//Rellenar el Choice
		//Conectar
		connection = bd.conectar();
		//Hacer un SELECT * FROM clientes
		sentencia = "SELECT * FROM clientes";
		try
		{
			//Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			choClientes.removeAll();
			
			// Si encuentra algo
			while (rs.next())
			{
				// Añade el cliente al Choice
				choClientes.add(rs.getInt("idCliente") + "-" + rs.getString("nombreCliente") + " "
						+ rs.getString("apellidosCliente") + "-" + rs.getString("dniCliente"));
			}
		} catch (SQLException sqle)
		{

		}
		ventana.add(choClientes);
		btnBorrarCliente.addActionListener(this);
		ventana.add(btnBorrarCliente);

		ventana.setSize(250, 140);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.addWindowListener(this);
		ventana.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent ae)
	{
		// Si se selecciona el boton "btnBorrarCliente" 
		if (ae.getSource().equals(btnBorrarCliente))
		{
			// Aparece el dialogo de confirmacion
			dlgSeguroCliente.setLayout(new FlowLayout());
			dlgSeguroCliente.addWindowListener(this);
			dlgSeguroCliente.setSize(150, 100);
			dlgSeguroCliente.setResizable(false);
			dlgSeguroCliente.setLocationRelativeTo(null);
			dlgSeguroCliente.add(lblSeguroCliente);
			btnSiSeguroCliente.addActionListener(this);
			dlgSeguroCliente.add(btnSiSeguroCliente);
			btnNoSeguroCliente.addActionListener(this);
			dlgSeguroCliente.add(btnNoSeguroCliente);
			dlgSeguroCliente.setVisible(true);
			
		// Si no se esta seguro de la baja
		} else if (ae.getSource().equals(btnNoSeguroCliente))
		{
			// Sale del dialogo
			dlgSeguroCliente.setVisible(false);
		// Si se esta seguro de la baja
		} else if (ae.getSource().equals(btnSiSeguroCliente))
		{
			// Conectar
			// Borrado del cliente usando su ID
			String[] elegido = choClientes.getSelectedItem().split("-");
			sentencia = "DELETE FROM clientes WHERE idCliente = " + elegido[0];
			try
			{
				// Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				statement.executeUpdate(sentencia);
				lblConfirmacionBajaCliente.setText("Baja de Cliente Correcta");
				
				new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");
				
			} catch (SQLException sqle)
			{
				lblConfirmacionBajaCliente.setText("Error en Baja");
			} finally
			{
				dlgConfirmacionBajaCliente.setLayout(new FlowLayout());
				dlgConfirmacionBajaCliente.addWindowListener(this);
				dlgConfirmacionBajaCliente.setSize(150, 100);
				dlgConfirmacionBajaCliente.setResizable(false);
				dlgConfirmacionBajaCliente.setLocationRelativeTo(null);
				dlgConfirmacionBajaCliente.add(lblConfirmacionBajaCliente);
				dlgConfirmacionBajaCliente.setVisible(true);
			}
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
		if(dlgConfirmacionBajaCliente.isActive()) 
		{
			dlgConfirmacionBajaCliente.setVisible(false);
			dlgSeguroCliente.setVisible(false);
			ventana.setVisible(false);
		}
		else if(dlgSeguroCliente.isActive())
		{
			dlgSeguroCliente.setVisible(false);
		}
		else 
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
}
