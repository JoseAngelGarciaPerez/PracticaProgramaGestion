package es.studium.ProgramaGestion;

import java.awt.Button;
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

public class AltaCliente implements WindowListener, ActionListener
{
	/* Elementos */
	// Ventana
	Frame ventana = new Frame("Alta Cliente");

	// Labels y TextFields
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

	// Tabla de todos los TextFields
	TextField[] camposTxt = { txtApellidos, txtDireccion, txtDni, txtNombre, txtTelefono };

	// Botones
	Button btnAlta = new Button("Alta");
	Button btnCancelar = new Button("Cancelar");

	// Dialogo de confirmacion
	Dialog dlgConfirmarAltaCliente = new Dialog(ventana, "Alta Cliente", true);
	Label lblMensajeAltaCliente = new Label();

	// Base de datos
	BaseDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public AltaCliente()
	{
		ventana.setLayout(new FlowLayout());

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

		ventana.add(btnAlta);
		btnAlta.addActionListener(this);
		ventana.add(btnCancelar);
		btnCancelar.addActionListener(this);

		ventana.addWindowListener(this);
		ventana.setSize(300, 230);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource().equals(btnAlta))
		{
			// Conectar BD
			bd = new BaseDatos();
			connection = bd.conectar();

			try
			{
				// Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				// Comprueba si los TextFields estan vacios
				if (((txtNombre.getText().length()) != 0) && ((txtApellidos.getText().length()) != 0)
						&& ((txtDni.getText().length()) != 0) && ((txtTelefono.getText().length()) != 0)
						&& ((txtDireccion.getText().length()) != 0))
				{
					//Si no estan vacios, se realiza el alta
					
					// Sentencia para insertar datos
					sentencia = "INSERT INTO clientes VALUES (null, '" + txtNombre.getText() + "', '"
							+ txtApellidos.getText() + "', " + "'" + txtDni.getText() + "', '" + txtTelefono.getText()
							+ "', '" + txtDireccion.getText() + "')";
					statement.executeUpdate(sentencia);
					lblMensajeAltaCliente.setText("Alta de Cliente Correcta");
					
					new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");
				} else
				{
					// Si uno o mas TextFields estan vacios
					lblMensajeAltaCliente.setText("Faltan datos");
				}
			} catch (SQLException sqle)
			{
				// Si hay un error
				lblMensajeAltaCliente.setText("Error en ALTA");
			} finally
			{
				dlgConfirmarAltaCliente.setLayout(new FlowLayout());
				dlgConfirmarAltaCliente.addWindowListener(this);
				dlgConfirmarAltaCliente.setSize(150, 100);
				dlgConfirmarAltaCliente.setResizable(false);
				dlgConfirmarAltaCliente.setLocationRelativeTo(null);
				dlgConfirmarAltaCliente.add(lblMensajeAltaCliente);
				dlgConfirmarAltaCliente.setVisible(true);
			}
			// Vaciar los TextFields
			for (int i = 0; i < camposTxt.length; i++)
			{
				camposTxt[i].setText("");
			}
		// Si hay error, se vacian los TextFields
		} else
		{
			for (int i = 0; i < camposTxt.length; i++)
			{
				camposTxt[i].setText("");
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
		if (dlgConfirmarAltaCliente.isActive())
		{
			dlgConfirmarAltaCliente.setVisible(false);
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

}
