package es.studium.ProgramaGestion;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipal implements WindowListener, ActionListener
{
	/*Elementos*/
	
	// Ventana
	Frame ventana = new Frame("Programa de Gestión");
	
	// Barra Menu
	MenuBar menu = new MenuBar();
	
	// Menu Clientes y sus items
	Menu menuClientes = new Menu("Clientes");
	MenuItem mniAltaCliente = new MenuItem("Alta");
	MenuItem mniBajaCliente = new MenuItem("Baja");
	MenuItem mniModificacionCliente = new MenuItem("Modificación");
	MenuItem mniConsultaCliente = new MenuItem("Consulta");
	
	// Menu Empleados y sus items
	Menu menuEmpleados = new Menu("Empleados");
	MenuItem mniAltaEmpleado = new MenuItem("Alta");
	MenuItem mniBajaEmpleado = new MenuItem("Baja");
	MenuItem mniModificacionEmpleado = new MenuItem("Modificación");
	MenuItem mniConsultaEmpleado = new MenuItem("Consulta");
	
	// Menu Contratos y sus items
	Menu menuContratos = new Menu("Contratos");
	MenuItem mniRegistrarContrato = new MenuItem("Registrar");
	MenuItem mniCancelarContrato = new MenuItem("Cancelar");
	MenuItem mniConsultaContratos = new MenuItem("Consulta");
	
	// Menu Opciones y sus items
	Menu menuOpciones = new Menu("Opciones");
	MenuItem mniInformacion = new MenuItem("Información");
	MenuItem mniSalir = new MenuItem("Salir");
	
	// Tabla de todos los menuItems
	MenuItem[] mnItems = {mniAltaCliente, mniAltaEmpleado, mniBajaCliente, mniBajaEmpleado, mniConsultaCliente, mniConsultaEmpleado,
			mniModificacionCliente, mniModificacionEmpleado, mniRegistrarContrato, mniCancelarContrato, mniConsultaContratos, mniInformacion, mniSalir};
	
	// Ventana de informacion del programa
	Dialog dlgInformacion = new Dialog(ventana, "Información", true);
	Label lblInfo1 = new Label("Programa creado por:");
	Label lblInfo2 = new Label("José Ángel García Pérez");
	Label lblInfo3 = new Label("1ºDAM");
	
	public MenuPrincipal(int tipo) 
	{
		ventana.setLayout(new FlowLayout());
		// Si tipo = 1, muestra todos los menuItems. Si no, solo muestra el menuItem "Alta" de cada menu
		menuClientes.add(mniAltaCliente);
		if(tipo == 1) 
		{
			menuClientes.add(mniBajaCliente);
			menuClientes.add(mniModificacionCliente);
			menuClientes.add(mniConsultaCliente);
		}
		menuEmpleados.add(mniAltaEmpleado);
		if(tipo == 1) 
		{
			menuEmpleados.add(mniBajaEmpleado);
			menuEmpleados.add(mniModificacionEmpleado);
			menuEmpleados.add(mniConsultaEmpleado);
		}
		menuContratos.add(mniRegistrarContrato);
		if(tipo == 1) 
		{
			menuContratos.add(mniCancelarContrato);
			menuContratos.add(mniConsultaContratos);
		}
		
		menuOpciones.add(mniInformacion);
		menuOpciones.add(mniSalir);
		
		menu.add(menuClientes);
		menu.add(menuEmpleados);
		menu.add(menuContratos);
		menu.add(menuOpciones);
		
		ventana.setMenuBar(menu);
		
		ventana.addWindowListener(this);
		// Se le añade ActionListener a todos los menuItem
		for(int i = 0; i< mnItems.length; i++) 
		{
			mnItems[i].addActionListener(this);
		}
		
		ventana.setResizable(false);
		ventana.setSize(250,200);
		ventana.setLocationRelativeTo(null);
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
		if(dlgInformacion.isActive()) 
		{
			dlgInformacion.setVisible(false);
		}
		else 
		{
			ventana.setVisible(false);
			new Login();
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
		// Clientes
		if(ae.getSource().equals(mniAltaCliente)) 
		{
			new AltaCliente();
		}
		else if(ae.getSource().equals(mniBajaCliente)) 
		{
			new BajaCliente();
		}
		else if(ae.getSource().equals(mniConsultaCliente)) 
		{
			new ConsultaCliente();
		}
		else if(ae.getSource().equals(mniModificacionCliente))
		{
			new ModificarCliente();
		}
		
		// Empleados
		else if(ae.getSource().equals(mniAltaEmpleado)) 
		{
			new AltaEmpleados();
		}
		else if(ae.getSource().equals(mniBajaEmpleado)) 
		{
			new BajaEmpleado();
		}
		else if(ae.getSource().equals(mniConsultaEmpleado)) 
		{
			new ConsultaEmpleado();
		}
		else if(ae.getSource().equals(mniModificacionEmpleado)) 
		{
			new ModificarEmpleado();
		}
		// Contratos
		else if(ae.getSource().equals(mniRegistrarContrato)) 
		{
			new RegistrarContrato();
		}
		else if(ae.getSource().equals(mniCancelarContrato)) 
		{
			new CancelarContrato();
		}
		else if(ae.getSource().equals(mniConsultaContratos)) 
		{
			new ConsultaContrato();
		}
		
		// Opciones
		else if(ae.getSource().equals(mniInformacion)) 
		{
			dlgInformacion.setLayout(new FlowLayout());
			dlgInformacion.add(lblInfo1);
			dlgInformacion.add(lblInfo2);
			dlgInformacion.add(lblInfo3);
			
			dlgInformacion.addWindowListener(this);
			dlgInformacion.setSize(100,125);
			dlgInformacion.setResizable(false);
			dlgInformacion.setLocationRelativeTo(null);
			dlgInformacion.setVisible(true);
		}
		else if(ae.getSource().equals(mniSalir)) 
		{
			ventana.setVisible(false);
			new Login();
		}
		
	}

}
