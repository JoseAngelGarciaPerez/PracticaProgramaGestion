package es.studium.ProgramaGestion;

import java.awt.FlowLayout;
import java.awt.Frame;
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
	
	// Tabla de todos los menuItems
	MenuItem[] mnItems = {mniAltaCliente, mniAltaEmpleado, mniBajaCliente, mniBajaEmpleado, mniConsultaCliente, mniConsultaEmpleado,
			mniModificacionCliente, mniModificacionEmpleado};
	
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
		menu.add(menuClientes);
		menu.add(menuEmpleados);
		
		ventana.setMenuBar(menu);
		
		ventana.addWindowListener(this);
		// Se le añade ActionListener a todos los menuItem
		for(int i = 0; i< mnItems.length; i++) 
		{
			mnItems[i].addActionListener(this);
		}
		
		ventana.setResizable(false);
		ventana.setSize(200,200);
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
		ventana.setVisible(false);
		new Login();
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
		
	}

}
