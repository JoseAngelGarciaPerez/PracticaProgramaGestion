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

public class ModificarEmpleado implements WindowListener, ActionListener, ItemListener
{

	/* Elementos */

	// Frame
	Frame ventana = new Frame("Modificar Empleado");

	// Labels y TextFields
	Label lblSeleccion = new Label("Seleccione al empleado:");

	// Choice
	Choice choEmpleadoModificar = new Choice();
	Choice choEmpleadoJefeOtro = new Choice();

	// Boton
	Button btnSeleccionar = new Button("Seleccionar");

	// Ventana de Modificacion
	Label lblIdEmpleado = new Label("Empleado seleccionado: ");

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

	Button btnModificar = new Button("Modificar");

	String idElegido = "";
	String idSubordinado = "";

	// Fragmento ventana Jefe
	Label lblSerJefe = new Label("¿De quien es jefe?");
	CheckboxGroup CGqueJefe = new CheckboxGroup();
	Checkbox checkPropio = new Checkbox("Propio", false, CGqueJefe);
	Checkbox checkOtro = new Checkbox("De otro empleado", false, CGqueJefe);

	// Dialogo de confirmacion
	Dialog dlgConfirmacion = new Dialog(ventana, "Modificacion", true);
	Label lblConfirmacion = new Label();

	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ModificarEmpleado()
	{
		ventana.setLayout(new FlowLayout());
		ventana.add(lblSeleccion);
		rellenarChoiceModificar();
		ventana.add(choEmpleadoModificar);
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
		// Si se selecciona un empleado del Choice
		if (ae.getSource().equals(btnSeleccionar))
		{
			ventanaModificar();
		}
		// Si se selecciona el boton "btnModificar"
		if (ae.getSource().equals(btnModificar))
		{
			connection = bd.conectar();
			sentencia = "SELECT * FROM empleados WHERE idEmpleados = " + idElegido;
			new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");
			// Si el empleado es un empleado normal
			if (checkNormal.getState() == true)
			{

				try
				{
					// Crear una sentencia
					statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);
					// Crear un objeto ResultSet para guardar lo obtenido
					// y ejecutar la sentencia SQL
					rs = statement.executeQuery(sentencia);
					// Sentencia que actualiza los datos del empleado con los datos de los TextFields y los checks
					sentencia = "UPDATE empleados SET nombreEmpleados = '" + txtNombre.getText() + "',"
							+ " apellidosEmpleado = '" + txtApellidos.getText() + "', dniEmpleado = '"
							+ txtDni.getText() + "'," + " telefonoEmpleado = " + txtTelefono.getText()
							+ ", direccionEmpleado = '" + txtDireccion.getText() + "', idEmpleadoJefeFK = null " + "WHERE (idEmpleados = '"
							+ idElegido + "')";
					statement.executeUpdate(sentencia);

					lblConfirmacion.setText("Modificacion realizada");
					
					new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");

					// Si hay una excepcion SQL
				} catch (SQLException sqle)
				{
					lblConfirmacion.setText("ERROR en la modificacion");
				} finally
				{
					rellenarChoiceModificar();
					dlgConfirmacion.setLayout(new FlowLayout());
					dlgConfirmacion.addWindowListener(this);
					dlgConfirmacion.setSize(150, 100);
					dlgConfirmacion.setResizable(false);
					dlgConfirmacion.setLocationRelativeTo(null);
					dlgConfirmacion.add(lblConfirmacion);
					dlgConfirmacion.setVisible(true);
				}
			}
			// Si el empleado es un jefe
			else if(checkJefe.getState()==true) 
			{
				// Si es su propio jefe
				if(checkPropio.getState()==true) 
				{
					try
					{
						// Crear una sentencia
						statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						// Crear un objeto ResultSet para guardar lo obtenido
						// y ejecutar la sentencia SQL
						rs = statement.executeQuery(sentencia);
						// Sentencia que actualiza los datos del empleado con los datos de los TextFields y los checks
						sentencia = "UPDATE empleados SET nombreEmpleados = '" + txtNombre.getText() + "',"
								+ " apellidosEmpleado = '" + txtApellidos.getText() + "', dniEmpleado = '"
								+ txtDni.getText() + "'," + " telefonoEmpleado = " + txtTelefono.getText()
								+ ", direccionEmpleado = '" + txtDireccion.getText() + "', idEmpleadoJefeFK = '" + idElegido + "' WHERE (idEmpleados = '"
								+ idElegido + "')";
						statement.executeUpdate(sentencia);
						
						lblConfirmacion.setText("Modificacion realizada");
						
						new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");

						// Si hay una excepcion SQL
					} catch (SQLException sqle)
					{
						lblConfirmacion.setText("ERROR en la modificacion");
					} finally
					{
						rellenarChoiceModificar();
						dlgConfirmacion.setLayout(new FlowLayout());
						dlgConfirmacion.addWindowListener(this);
						dlgConfirmacion.setSize(150, 100);
						dlgConfirmacion.setResizable(false);
						dlgConfirmacion.setLocationRelativeTo(null);
						dlgConfirmacion.add(lblConfirmacion);
						dlgConfirmacion.setVisible(true);
					}
				}
				// Si es jefe de otro empleado
				else if(checkOtro.getState()==true) 
				{
					// Obtenemos el id del subordinado
					String[] subordinadoElegido = choEmpleadoJefeOtro.getSelectedItem().split("-");
					idSubordinado = subordinadoElegido[0];
					
					try
					{
						// Crear una sentencia
						statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						// Crear un objeto ResultSet para guardar lo obtenido
						// y ejecutar la sentencia SQL
						rs = statement.executeQuery(sentencia);
						// Sentencia que actualiza los datos del empleado con los datos de los TextFields y los checks
						sentencia = "UPDATE empleados SET nombreEmpleados = '" + txtNombre.getText() + "',"
								+ " apellidosEmpleado = '" + txtApellidos.getText() + "', dniEmpleado = '"
								+ txtDni.getText() + "'," + " telefonoEmpleado = " + txtTelefono.getText()
								+ ", direccionEmpleado = '" + txtDireccion.getText() + "', idEmpleadoJefeFK = '" + idSubordinado + "' WHERE (idEmpleados = '"
								+ idElegido + "')";
						statement.executeUpdate(sentencia);
						
						lblConfirmacion.setText("Modificacion realizada");
						
						new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");

						// Si hay una excepcion SQL
					} catch (SQLException sqle)
					{
						lblConfirmacion.setText("ERROR en la modificacion");
					} finally
					{
						rellenarChoiceModificar();
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
		}

	}

	public void ventanaModificar()
	// Metodo que selecciona al empleado elegido y modifica el frame "ventana" para
	// mostrar los datos del empleado
	{
		String[] elegido = choEmpleadoModificar.getSelectedItem().split("-");
		lblIdEmpleado.setText("Número de ID del cliente selecionado: " + elegido[0]);
		ventana.add(lblTipoEmpleado);
		ventana.add(checkJefe);
		checkJefe.addItemListener(this);
		ventana.add(checkNormal);
		checkNormal.addItemListener(this);
		ventana.add(lblIdEmpleado);
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
		ventana.add(choEmpleadoJefeOtro);
		choEmpleadoJefeOtro.disable();

		rellenarChoiceOtro();

		// Conectar
		connection = bd.conectar();
		// Hacer un SELECT * FROM empleados

		idElegido = elegido[0];
		sentencia = "SELECT * FROM empleados WHERE idEmpleados = " + elegido[0];
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

			// Si encuentra al empleado
			while (rs.next())
			{
				// Introduce en los TextFields los datos del empleado
				txtNombre.setText(rs.getString("nombreEmpleados"));
				txtApellidos.setText(rs.getString("apellidosEmpleado"));
				txtDni.setText(rs.getString("dniEmpleado"));
				txtTelefono.setText(rs.getString("telefonoEmpleado"));
				txtDireccion.setText(rs.getString("direccionEmpleado"));

				checkNormal.setState(true);
			}
			// Si no encuentra al cliente
		} catch (SQLException sqle)
		{
			System.out.println("ERROR");
		} finally
		{
			ventana.add(btnModificar);
			btnModificar.addActionListener(this);

			ventana.addWindowListener(this);
			ventana.setSize(300, 420);
			ventana.setResizable(false);
			ventana.setLocationRelativeTo(null);
			ventana.setVisible(true);
		}

	}

	private void rellenarChoiceModificar()
	// Metodo que rellena el choice de seleccion de empleado a modificar con todos los empleados
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
			choEmpleadoModificar.removeAll();
			// Mientras encuentre empleados
			while (rs.next())
			{
				// Añade el empleado al Choice
				choEmpleadoModificar.add(rs.getInt("idEmpleados") + "-" + rs.getString("nombreEmpleados") + " "
						+ rs.getString("apellidosEmpleado") + "-" + rs.getString("dniEmpleado"));
			}
		} catch (SQLException sqle)
		{

		}
	}

	private void rellenarChoiceOtro()
	// Metodo que rellena el choice de seleccion de empleado subordinado con todos los empleados
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
			choEmpleadoJefeOtro.removeAll();
			// Mientras encuentre empleados
			while (rs.next())
			{
				// Añade el empleado al Choice
				choEmpleadoJefeOtro.add(rs.getInt("idEmpleados") + "-" + rs.getString("nombreEmpleados") + " "
						+ rs.getString("apellidosEmpleado") + "-" + rs.getString("dniEmpleado"));
			}
		} catch (SQLException sqle)
		{

		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0)
	{
		if (checkJefe.getState() == true)
		{
			checkPropio.enable();
			checkOtro.enable();

			if (checkPropio.getState() == true)
			{
				choEmpleadoJefeOtro.disable();
			} else
			{
				choEmpleadoJefeOtro.enable();
			}
		} else if (checkNormal.getState() == true)
		{
			checkPropio.setState(false);
			checkPropio.disable();
			checkOtro.setState(false);
			checkOtro.disable();
			choEmpleadoJefeOtro.disable();
		}

	}

}
