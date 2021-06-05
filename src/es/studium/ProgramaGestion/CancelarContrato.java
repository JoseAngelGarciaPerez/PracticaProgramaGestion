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

public class CancelarContrato implements WindowListener, ActionListener
{
	/* Elementos */
	// Frame
	Frame ventana = new Frame("Cancelar Contrato");
	// Label
	Label lblMensajeCancelarContrato = new Label("Selecciona el contrato a cancelar:");
	// Choice
	Choice choContratos = new Choice();
	// Botones
	Button btnCancelar = new Button("Cancelar");

	// Dialogo de confirmacion
	Dialog dlgSeguroCancelar = new Dialog(ventana, "¿Seguro?", true);
	Label lblSeguroCancelar = new Label("¿Está seguro de cancelar?");
	Button btnSiSeguroCancelar = new Button("Sí");
	Button btnNoSeguroCancelar = new Button("No");
	Dialog dlgConfirmacionCancelacion = new Dialog(ventana, "Cancelación", true);
	Label lblConfirmacionCancelacion = new Label("Contrato cancelado");

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

	public CancelarContrato()
	{
		ventana.setLayout(new FlowLayout());
		ventana.add(lblMensajeCancelarContrato);
		// Rellenar el Choice
		// Conectar
		connection = bd.conectar();
		// Hacer un SELECT * FROM contratos
		sentencia = "SELECT * FROM contratos";
		try
		{
			// Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			choContratos.removeAll();

			// Si encuentra algo
			while (rs.next())
			{
				// Toma los ID del cliente y el empleado
				idCliente = rs.getInt("idClienteFK");
				idEmpleado = rs.getInt("idEmpleadoFK");
				
				datosCliente();
				datosEmpleado();
				
				// Añade el contrato al Choice
				choContratos.add(rs.getInt("idContrato") + "-" + nombreCliente + "-" + nombreEmpleado);
			}
		} catch (SQLException sqle)
		{

		}
		ventana.add(choContratos);
		btnCancelar.addActionListener(this);
		ventana.add(btnCancelar);

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
		if (ae.getSource().equals(btnCancelar))
		{
			// Aparece el dialogo de confirmacion
			dlgSeguroCancelar.setLayout(new FlowLayout());
			dlgSeguroCancelar.addWindowListener(this);
			dlgSeguroCancelar.setSize(150, 100);
			dlgSeguroCancelar.setResizable(false);
			dlgSeguroCancelar.setLocationRelativeTo(null);
			dlgSeguroCancelar.add(lblSeguroCancelar);
			btnSiSeguroCancelar.addActionListener(this);
			dlgSeguroCancelar.add(btnSiSeguroCancelar);
			btnNoSeguroCancelar.addActionListener(this);
			dlgSeguroCancelar.add(btnNoSeguroCancelar);
			dlgSeguroCancelar.setVisible(true);

			// Si no se esta seguro de la baja
		} else if (ae.getSource().equals(btnNoSeguroCancelar))
		{
			// Sale del dialogo
			dlgSeguroCancelar.setVisible(false);
			// Si se esta seguro de la baja
		} else if (ae.getSource().equals(btnSiSeguroCancelar))
		{
			// Conectar
			// Borrado del empleado usando su ID
			// Por si acaso el empleado es jefe, cambia su campo "idEmpleadoJefeFK" a null
			String[] elegido = choContratos.getSelectedItem().split("-");
			sentencia = "UPDATE contratos SET idEmpleadoFK=null, idClienteFK=null WHERE idContrato=" + elegido[0];
			try
			{
				// Crear una sentencia
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				statement.executeUpdate(sentencia);

				sentencia = "DELETE FROM contratos WHERE idContrato = " + elegido[0];
				statement.executeUpdate(sentencia);
				lblConfirmacionCancelacion.setText("Contrato cancelado");
				
				new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+LogMovimientos.usuario+"]["+sentencia+"]");
			} catch (SQLException sqle)
			{
				lblConfirmacionCancelacion.setText("Error en la cancelación");
			} finally
			{
				dlgConfirmacionCancelacion.setLayout(new FlowLayout());
				dlgConfirmacionCancelacion.addWindowListener(this);
				dlgConfirmacionCancelacion.setSize(150, 100);
				dlgConfirmacionCancelacion.setResizable(false);
				dlgConfirmacionCancelacion.setLocationRelativeTo(null);
				dlgConfirmacionCancelacion.add(lblConfirmacionCancelacion);
				dlgConfirmacionCancelacion.setVisible(true);
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
		if (dlgConfirmacionCancelacion.isActive())
		{
			dlgConfirmacionCancelacion.setVisible(false);
			dlgSeguroCancelar.setVisible(false);
			ventana.setVisible(false);
		} else if (dlgSeguroCancelar.isActive())
		{
			dlgSeguroCancelar.setVisible(false);
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
}
