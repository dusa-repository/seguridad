package security.controlador;

import java.io.IOException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import security.componente.SecurityBotonera;
import security.componente.SecurityMensaje;
import security.componente.SecurityValidador;
import security.modelo.UsuarioSeguridad;

public class CReinicioPassword extends CSecurity {

	@Wire
	private Textbox txtCorreoUsuario;
	@Wire
	private Textbox txtCedulaUsuario;
	@Wire
	private Label lblNombreUsuario;
	@Wire
	private Div botoneraReinicio;
	@Wire
	private Window wdwRecordar;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private static final long serialVersionUID = 6988038390488496987L;

	@Override
	public void inicializar() throws IOException {

		SecurityBotonera botonera = new SecurityBotonera() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void salir() {
				wdwRecordar.onClose();
			}

			@Override
			public void limpiar() {
				txtCorreoUsuario.setValue("");
				txtCedulaUsuario.setValue("");
			}

			@Override
			public void guardar() {
				String password = KeyGenerators.string().generateKey();
				String correo;
				if (validar()) {
					UsuarioSeguridad usuario = servicioUsuarioSeguridad.buscarPorLoginyCorreo(
							txtCedulaUsuario.getValue(),
							txtCorreoUsuario.getValue());
					if (usuario != null) {
						correo = usuario.getEmail();
						usuario.setPassword(password);
						servicioUsuarioSeguridad.guardar(usuario);
						enviarEmailNotificacion(
								correo,
								"Ha Solicitado Reiniciar su Password, los nuevos datos para el inicio de sesion son: "
										+ " Usuario: "
										+ usuario.getLogin()
										+ "  " + " Password: " + password);
						limpiar();
						SecurityMensaje.mensajeInformacion(SecurityMensaje.reinicioContrasenna);
						salir();
					} else {
						SecurityMensaje.mensajeError(SecurityMensaje.cedulaNoExiste);
					}
				}
			}

			@Override
			public void eliminar() {
			}

			@Override
			public void seleccionar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void buscar() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void annadir() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void reporte() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void ayuda() {
				// TODO Auto-generated method stub
				
			}

		};
		botonera.getChildren().get(0).setVisible(false);
		botonera.getChildren().get(2).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(4).setVisible(false);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		
		Button guardar = (Button) botonera.getChildren().get(3);
		guardar.setLabel("Enviar");
		botoneraReinicio.appendChild(botonera);
	}

	protected boolean validar() {
		if (txtCedulaUsuario.getText().compareTo("") == 0
				|| txtCorreoUsuario.getText().compareTo("") == 0) {
			SecurityMensaje.mensajeError(SecurityMensaje.camposVacios);
			return false;
		} else
			return true;
	}

	/*
	 * Metodo que permite validar el correo electronico
	 */
	@Listen("onChange = #txtCorreoUsuario")
	public void validarCorreo() throws IOException {
		if (SecurityValidador.validarCorreo(txtCorreoUsuario.getValue()) == false) {
			SecurityMensaje.mensajeAlerta(SecurityMensaje.correoInvalido);
		}
	}

}
