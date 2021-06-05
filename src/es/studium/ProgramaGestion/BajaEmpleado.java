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

public class BajaEmpleado implements WindowListener, ActionListener
{
	/* Elementos */
	// Frame
	Frame ventana = new Frame("Baja de Empleado");
	// Label
	Label lblMensajeBajaEmpleado = new Label("Selecciona el empleado a borrar:");
	// Choice
	Choice choEmpleados = new Choice();
	// Botones
	Button btnBorrarEmpleado = new Button("Borrar");

	// Dialogo de confirmacion
	Dialog dlgSeguroEmpleado = new Dialog(ventana, "¿Seguro?", true);
	Label lblSeguroEmpleado = new Label("¿Está seguro del borrado?");
	Button btnSiSeguroEmpleado = new Button("Sí");
	Button btnNoSeguroEmpleado = new Button("No");
	Dialog dlgConfirmacionBajaEmpleado = new Dialog(ventana, "Baja Empleado", true);
	Label lblConfirmacionBajaEmpleado = new Label("Baja de empleado correcta");

	// Base de datos
	BaseDatos bd = new BaseDatos();
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public BajaEmpleado()
	{
		ventana.setLayout(new FlowLayout());
		ventana.add(lblMensajeBajaEmpleado);
	//Rellenar el Choice
		//Conectar
		connection = bd.conectar();
		//Hacer un SELECT * FROM empleados
		sentencia = "SELECT * FROM empleados";
		try
		{
			//Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			choEmpleados.removeAll();
			
			// Si encuentra algo
			while (rs.next())
			{
				// Añade el empleado al Choice
				choEmpleados.add(rs.getInt("idEmpleados") + "-" + rs.getString("nombreEmpleados") + " "
						+ rs.getString("apellidosEmpleado") + "-" + rs.getString("dniEmpleado"));
			}
		} catch (SQLException sqle)
		{

		}
		ventana.add(choEmpleados);
		btnBorrarEmpleado.addActionListener(this);
		ventana.add(btnBorrarEmpleado);

		ventana.setSize(250, 140);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
		ventana.addWindowListener(this);
		ventana.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent ae)
	{
		// Si se selecciona el boton "btnBorrarEmpleado" 
		if (ae.getSource().equals(btnBorrarEmpleado))
		{
			// Aparece el dialogo de confirmacion
			dlgSeguroEmpleado.setLayout(new FlowLayout());
			dlgSeguroEmpleado.addWindowListener(this);
			dlgSeguroEmpleado.setSize(150, 100);
			dlgSeguroEmpleado.setResizable(false);
			dlgSeguroEmpleado.setLocationRelativeTo(null);
			dlgSeguroEmpleado.add(lblSeguroEmpleado);
			btnSiSeguroEmpleado.addActionListener(this);
			dlgSeguroEmpleado.add(btnSiSeguroEmpleado);
			btnNoSeguroEmpleado.addActionListener(this);
			dlgSeguroEmpleado.add(btnNoSeguroEmpleado);
			dlgSeguroEmpleado.setVisible(true);
			
		// Si no se esta seguro de la baja
		} else if (ae.getSource().equals(btnNoSeguroEmpleado))
		{
			// Sale del dialogo
			dlgSeguroEmpleado.setVisible(false);
		// Si se esta seguro de la baja
		} else if (ae.getSource().equals(btnSiSeguroEmpleado))
		{
			// Conectar
			// Borrado del empleado usando su ID
			// Por si acaso el empleado es jefe, cambia su campo "idEmpleadoJefeFK" a null 
			String[] elegido = choEmpleados.getSelectedItem().split("-");
			sentencia = "UPDATE empleados SET idEmpleadoJefeFK=null WHERE idEmpleados=" + elegido[0];
			try
			{
				// Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				statement.executeUpdate(sentencia);
				
				sentencia = "DELETE FROM empleados WHERE idEmpleados = " + elegido[0];
				statement.executeUpdate(sentencia);
				lblConfirmacionBajaEmpleado.setText("Baja de Empleado Correcta");
				
				new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");
			} catch (SQLException sqle)
			{
				lblConfirmacionBajaEmpleado.setText("Error en Baja");
			} finally
			{
				dlgConfirmacionBajaEmpleado.setLayout(new FlowLayout());
				dlgConfirmacionBajaEmpleado.addWindowListener(this);
				dlgConfirmacionBajaEmpleado.setSize(150, 100);
				dlgConfirmacionBajaEmpleado.setResizable(false);
				dlgConfirmacionBajaEmpleado.setLocationRelativeTo(null);
				dlgConfirmacionBajaEmpleado.add(lblConfirmacionBajaEmpleado);
				dlgConfirmacionBajaEmpleado.setVisible(true);
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
		if(dlgConfirmacionBajaEmpleado.isActive()) 
		{
			dlgConfirmacionBajaEmpleado.setVisible(false);
			dlgSeguroEmpleado.setVisible(false);
			ventana.setVisible(false);
		}
		else if(dlgSeguroEmpleado.isActive())
		{
			dlgSeguroEmpleado.setVisible(false);
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
