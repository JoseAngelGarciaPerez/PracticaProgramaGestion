package es.studium.ProgramaGestion;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Login implements WindowListener, ActionListener, KeyListener
{
	/* Elementos */
	
	// Frame
	Frame ventanaLogin = new Frame("Login");
	
	// Dialogo
	Dialog dialogoLogin = new Dialog(ventanaLogin, "Error", true);
	
	// Lablels
	Label lblUsuario = new Label("Usuario:");
	Label lblClave = new Label("Clave:");
	Label lblError = new Label("Usuario y/o contraseña incorrectos");
	
	//TextFields
	TextField txtUsuario = new TextField(20);
	TextField txtClave = new TextField(20);
	
	// Botones
	Button btnAceptar = new Button("Acceder");
	Button btnCancelar = new Button("Cancelar");
	
	// Base de datos
	BaseDatos bd;
	String sentencia = "";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	
	public Login()
	{
		ventanaLogin.setLayout(new FlowLayout());
		
		ventanaLogin.add(lblUsuario);
		txtUsuario.addKeyListener(this);
		ventanaLogin.add(txtUsuario);
		ventanaLogin.add(lblClave);
		txtClave.setEchoChar('*');
		txtClave.addKeyListener(this);
		ventanaLogin.add(txtClave);
		btnAceptar.addActionListener(this);
		btnAceptar.addKeyListener(this);
		ventanaLogin.add(btnAceptar);
		btnCancelar.addActionListener(this);
		btnCancelar.addKeyListener(this);
		ventanaLogin.add(btnCancelar);
	
		ventanaLogin.addWindowListener(this);
		ventanaLogin.setSize(250,200);
		ventanaLogin.setLocationRelativeTo(null);
		ventanaLogin.setResizable(false);
		ventanaLogin.setVisible(true);
	}

	public static void main(String[] args)
	{
		new Login();
	}

	@Override
	public void actionPerformed(ActionEvent botonPulsado)
	{
		// Si se pulsa el boton "btnCancelar", se vacian los TextFields
		if(botonPulsado.getSource().equals(btnCancelar))
		{
			txtUsuario.selectAll();
			txtUsuario.setText("");
			txtClave.selectAll();
			txtClave.setText("");
			txtUsuario.requestFocus();
		}
		// Si se pulsa el boton "btnAceptar", se intenta el Login
		else if(botonPulsado.getSource().equals(btnAceptar))
		{
			hacerLogin();
		}
	}

	private void hacerLogin()
	{
		// Conectar BD
		bd = new BaseDatos();
		connection = bd.conectar();
		// Buscar al usuario introducido en los TextFields
		sentencia = "SELECT * FROM usuarios WHERE nombreUsuario='"
		+txtUsuario.getText()+"' AND claveUsuario = SHA2('"
				+txtClave.getText()+"',256);";
		try
		{
			//Crear una sentencia
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = statement.executeQuery(sentencia);
			if(rs.next()) // Si ha encontrado algo
			{
				// Si existe en la BD, mostrar Menú Principal
				// "tipo" determina el tipo de usuario (vale 1 para administrador y 0 para usuario basico)
				int tipo = rs.getInt("tipoUsuario");
				ventanaLogin.setVisible(false);
				new MenuPrincipal(tipo);
				
				LogMovimientos.usuario=txtUsuario.getText();
				
				new LogMovimientos("["+LogFechaHora.fechaActual()+"]["+txtUsuario.getText()+"][Acceso al sistema]");
			}
			else // Si no encuentra nada
			{
				// Si no existe en la BD, mostrar Diálogo de Error
				dialogoLogin.setLayout(new FlowLayout());
				dialogoLogin.add(lblError);
				dialogoLogin.addWindowListener(this);
				dialogoLogin.setSize(220,75);
				dialogoLogin.setLocationRelativeTo(null);
				dialogoLogin.setResizable(false);
				dialogoLogin.setVisible(true);
			}
		}
		catch (SQLException sqle)
		{
		}
		
		// Desconectar la BD
		bd.desconectar(connection);
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{}

	@Override
	public void windowClosed(WindowEvent arg0)
	{}

	@Override
	public void windowClosing(WindowEvent arg0)
	{
		if(dialogoLogin.isActive())
		{
			dialogoLogin.setVisible(false);
		}
		else
		{
			System.exit(0);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{}

	@Override
	public void windowIconified(WindowEvent arg0)
	{}

	@Override
	public void windowOpened(WindowEvent arg0)
	{}

	@Override
	public void keyPressed(KeyEvent kp)
	{	
		//Focus de arriba a abajo
		if((kp.getKeyCode()==40)&&(txtUsuario.hasFocus()))
		{
			txtClave.requestFocus();
		}
		if((kp.getKeyCode()==40)&&(txtClave.hasFocus()))
		{
			btnAceptar.requestFocus();
		}
		
		//Focus de abajo a arriba
		if((kp.getKeyCode()==38)&&((btnAceptar.hasFocus())||(btnCancelar.hasFocus()))) 
		{
			txtClave.requestFocus();
		}
		if((kp.getKeyCode()==38)&&(txtClave.hasFocus())) 
		{
			txtUsuario.requestFocus();
		}
		
		//Focus entre botones
		if((kp.getKeyCode()==39)&&(btnAceptar.hasFocus())) 
		{
			btnCancelar.requestFocus();
		}
		if((kp.getKeyCode()==37)&&(btnCancelar.hasFocus())) 
		{
			btnAceptar.requestFocus();
		}
		
		//Eventos con los botones
		if((kp.getKeyCode()==10)&&(!btnCancelar.hasFocus())) 
		{
			hacerLogin();
		}
		if((kp.getKeyCode()==10)&&(btnCancelar.hasFocus())) 
		{
			txtClave.setText("");
			txtUsuario.setText("");
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}
}