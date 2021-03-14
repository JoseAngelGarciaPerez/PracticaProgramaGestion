package es.studium.ProgramaGestion;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AltaEmpleados implements WindowListener, ActionListener, ItemListener
{
	/* Elementos */
	// Ventana
	Frame ventana = new Frame("Alta Empleado");

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

	Label lblTipoEmpleado = new Label("Tipo de empleado:");
	CheckboxGroup cgTipoEmpleado = new CheckboxGroup();
	Checkbox checkJefe = new Checkbox("Jefe", false, cgTipoEmpleado);
	Checkbox checkNormal = new Checkbox("Normal", true, cgTipoEmpleado);

	// Choice
	Choice choEmpleados = new Choice();
	
	// Tabla de todos los TextFields
	TextField[] camposTxt = { txtApellidos, txtDireccion, txtDni, txtNombre, txtTelefono };

	// Botones
	Button btnAlta = new Button("Alta");
	Button btnCancelar = new Button("Cancelar");

	// Dialogo de confirmacion
	Dialog dlgConfirmarAltaEmpleado = new Dialog(ventana, "Alta Empleado", true);
	Label lblMensajeAltaEmpleado = new Label();

	// Fragmento ventana Jefe
	Label lblSerJefe = new Label("¿De quien es jefe?");
	CheckboxGroup CGqueJefe = new CheckboxGroup();
	Checkbox checkPropio = new Checkbox("Propio", false, CGqueJefe);
	Checkbox checkOtro = new Checkbox("De otro empleado", false, CGqueJefe);
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelarSerJefe = new Button("Cancelar");

	int idEmpleado = 0;

	// Base de datos
	BaseDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public AltaEmpleados()
	{
		ventana.setLayout(new FlowLayout());

		ventana.add(lblTipoEmpleado);
		ventana.add(checkJefe);
		checkJefe.addItemListener(this);
		ventana.add(checkNormal);
		checkNormal.addItemListener(this);
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
		ventana.add(lblSerJefe);
		ventana.add(checkPropio);
		checkPropio.addItemListener(this);
		checkPropio.disable();
		ventana.add(checkOtro);
		checkOtro.addItemListener(this);
		checkOtro.disable();
		ventana.add(choEmpleados);
		choEmpleados.disable();

		rellenarChoice();

		ventana.add(btnAlta);
		btnAlta.addActionListener(this);
		ventana.add(btnCancelar);
		btnCancelar.addActionListener(this);

		ventana.addWindowListener(this);
		ventana.setSize(300, 320);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);

	}


	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource().equals(btnAlta))
		{
			// Si el empleado es un empleado normal
			if (checkNormal.getState() == true)
			{
				// Conectar BD
				bd = new BaseDatos();
				connection = bd.conectar();

				try
				{
					// Crear una sentencia
					statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

					// Comprueba si los TextFields estan vacios
					if (((txtNombre.getText().length()) != 0) && ((txtApellidos.getText().length()) != 0)
							&& ((txtDni.getText().length()) != 0) && ((txtTelefono.getText().length()) != 0)
							&& ((txtDireccion.getText().length()) != 0))
					{
						// Si no estan vacios, se realiza el alta

						// Sentencia para insertar datos
						sentencia = "INSERT INTO empleados VALUES (null, '" + txtNombre.getText() + "', '"
								+ txtApellidos.getText() + "', " + "'" + txtDni.getText() + "', '"
								+ txtTelefono.getText() + "', '" + txtDireccion.getText() + "', null)";
						statement.executeUpdate(sentencia);
						lblMensajeAltaEmpleado.setText("Alta de Empleado Correcta");

					} else
					{
						// Si uno o mas TextFields estan vacios
						lblMensajeAltaEmpleado.setText("Faltan datos");
					}
				} catch (SQLException sqle)
				{
					// Si hay un error
					lblMensajeAltaEmpleado.setText("Error en ALTA");
				} finally
				{
					dlgConfirmarAltaEmpleado.setLayout(new FlowLayout());
					dlgConfirmarAltaEmpleado.addWindowListener(this);
					dlgConfirmarAltaEmpleado.setSize(150, 100);
					dlgConfirmarAltaEmpleado.setResizable(false);
					dlgConfirmarAltaEmpleado.setLocationRelativeTo(null);
					dlgConfirmarAltaEmpleado.add(lblMensajeAltaEmpleado);
					dlgConfirmarAltaEmpleado.setVisible(true);
				}
				// Vaciar los TextFields
				for (int i = 0; i < camposTxt.length; i++)
				{
					camposTxt[i].setText("");
				}
			// Si el empleado es jefe
			} else if (checkJefe.getState() == true)
			{
				// Si es su propio jefe
				if (checkPropio.getState() == true)
				{
					// Conectar BD
					bd = new BaseDatos();
					connection = bd.conectar();

					try
					{
						// Crear una sentencia
						statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);

						// Comprueba si los TextFields estan vacios
						if (((txtNombre.getText().length()) != 0) && ((txtApellidos.getText().length()) != 0)
								&& ((txtDni.getText().length()) != 0) && ((txtTelefono.getText().length()) != 0)
								&& ((txtDireccion.getText().length()) != 0))
						{
							// Si no estan vacios, se realiza el alta

							// Sentencia para insertar datos (crea al empleado)
							sentencia = "INSERT INTO empleados VALUES (null, '" + txtNombre.getText() + "', '"
									+ txtApellidos.getText() + "', " + "'" + txtDni.getText() + "', '"
									+ txtTelefono.getText() + "', '" + txtDireccion.getText() + "', null)";
							statement.executeUpdate(sentencia);

							// Sentencia que elige al último empleado creado (que es el que acabamos de crear)
							sentencia = "SELECT * FROM empleados ORDER BY idEmpleados desc LIMIT 1";

							rs = statement.executeQuery(sentencia);
							// Toma el id del empleado
							while (rs.next())
							{
								idEmpleado = rs.getInt("idEmpleados");
							}
							// Le da el valor del id al campo idEmpleadoJefeFK
							sentencia = "UPDATE empleados SET idEmpleadoJefeFK=" + idEmpleado + " WHERE idEmpleados="
									+ idEmpleado;
							statement.executeUpdate(sentencia);

							lblMensajeAltaEmpleado.setText("Alta de Empleado Correcta");

						} else
						{
							// Si uno o mas TextFields estan vacios
							lblMensajeAltaEmpleado.setText("Faltan datos");
						}
					} catch (SQLException sqle)
					{
						// Si hay un error
						lblMensajeAltaEmpleado.setText("Error en ALTA");
					} finally
					{
						dlgConfirmarAltaEmpleado.setLayout(new FlowLayout());
						dlgConfirmarAltaEmpleado.addWindowListener(this);
						dlgConfirmarAltaEmpleado.setSize(150, 100);
						dlgConfirmarAltaEmpleado.setResizable(false);
						dlgConfirmarAltaEmpleado.setLocationRelativeTo(null);
						dlgConfirmarAltaEmpleado.add(lblMensajeAltaEmpleado);
						dlgConfirmarAltaEmpleado.setVisible(true);
					}
					// Vaciar los TextFields
					for (int i = 0; i < camposTxt.length; i++)
					{
						camposTxt[i].setText("");
					}
				// Si es jefe de otro empleado
				} else if (checkOtro.getState() == true)
				{
					// Divide los campos del empleado subordinado elegido en el choice
					String[] elegido = choEmpleados.getSelectedItem().split("-");
					// Conectar BD
					bd = new BaseDatos();
					connection = bd.conectar();

					try
					{
						// Crear una sentencia
						statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);

						// Comprueba si los TextFields estan vacios
						if (((txtNombre.getText().length()) != 0) && ((txtApellidos.getText().length()) != 0)
								&& ((txtDni.getText().length()) != 0) && ((txtTelefono.getText().length()) != 0)
								&& ((txtDireccion.getText().length()) != 0))
						{
							// Si no estan vacios, se realiza el alta

							// Sentencia para insertar datos (introduce en el campo "idEmpleadoJefeFK" el id del empleado subordinado)
							sentencia = "INSERT INTO empleados VALUES (null, '" + txtNombre.getText() + "', '"
									+ txtApellidos.getText() + "', " + "'" + txtDni.getText() + "', '"
									+ txtTelefono.getText() + "', '" + txtDireccion.getText() + "', "+elegido[0]+")";
							statement.executeUpdate(sentencia);

							lblMensajeAltaEmpleado.setText("Alta de Empleado Correcta");

						} else
						{
							// Si uno o mas TextFields estan vacios
							lblMensajeAltaEmpleado.setText("Faltan datos");
						}
					} catch (SQLException sqle)
					{
						// Si hay un error
						lblMensajeAltaEmpleado.setText("Error en ALTA");
					} finally
					{
						dlgConfirmarAltaEmpleado.setLayout(new FlowLayout());
						dlgConfirmarAltaEmpleado.addWindowListener(this);
						dlgConfirmarAltaEmpleado.setSize(150, 100);
						dlgConfirmarAltaEmpleado.setResizable(false);
						dlgConfirmarAltaEmpleado.setLocationRelativeTo(null);
						dlgConfirmarAltaEmpleado.add(lblMensajeAltaEmpleado);
						dlgConfirmarAltaEmpleado.setVisible(true);
					}
					// Vaciar los TextFields
					for (int i = 0; i < camposTxt.length; i++)
					{
						camposTxt[i].setText("");
					}
				}
			}
		// Si hay error, vacia los TextFields
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
		if (dlgConfirmarAltaEmpleado.isActive())
		{
			dlgConfirmarAltaEmpleado.setVisible(false);
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

	private void rellenarChoice()
	// Metodo que rellena el choice con todos los empleados
	{
		bd = new BaseDatos();
		// Conectar
		connection = bd.conectar();
		// Selecciona todos los clientes
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

	@Override
	public void itemStateChanged(ItemEvent ie)
	{
		if (checkJefe.getState() == true)
		{
			checkPropio.enable();
			checkOtro.enable();

			if (checkPropio.getState() == true)
			{
				choEmpleados.disable();
			} else
			{
				choEmpleados.enable();
			}
		} else if (checkNormal.getState() == true)
		{
			checkPropio.setState(false);
			checkPropio.disable();
			checkOtro.setState(false);
			checkOtro.disable();
			choEmpleados.disable();
		}

	}
}
