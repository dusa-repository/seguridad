package security.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;

import security.componente.SecurityBotonera;
import security.componente.SecurityMensaje;
import security.componente.SecurityValidador;
import security.modelo.UsuarioSeguridad;

public class CEditarUsuario extends CSecurity {

	@Wire
	private Textbox txtNombreUsuarioEditar;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtApellido;
	@Wire
	private Textbox txtCorreo;
	@Wire
	private Textbox txtClaveUsuarioNueva;
	@Wire
	private Textbox txtClaveUsuarioConfirmar;
	@Wire
	private Image imgUsuario;
	@Wire
	private Fileupload fudImagenUsuario;
	@Wire
	private Div botoneraEditarUsuario;
	@Wire
	private Div divEditarUsuario;
	private String id = "";
	private Media media;
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	URL url = getClass().getResource("/security/controlador/usuario.png");
	private static final long serialVersionUID = 2439502647179786175L;

	@SuppressWarnings("unchecked")
	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (mapa != null) {
			if (mapa.get("tabsGenerales") != null) {
				tabs = (List<Tab>) mapa.get("tabsGenerales");
				west = (West) mapa.get("west");
				mapa.clear();
				mapa = null;
			}
		}
		UsuarioSeguridad usuario = servicioUsuarioSeguridad.buscarPorLogin(nombreUsuarioSesion());
		id = nombreUsuarioSesion();
		txtNombreUsuarioEditar.setValue(usuario.getLogin());
		txtNombre.setValue(usuario.getNombre());
		txtApellido.setValue(usuario.getApellido());
		txtCorreo.setValue(usuario.getEmail());
		txtClaveUsuarioConfirmar.setValue(usuario.getPassword());
		txtClaveUsuarioNueva.setValue(usuario.getPassword());
		if (usuario.getImagen() == null) {
			imgUsuario.setContent(new AImage(url));
		} else {
			try {
				BufferedImage imag;
				imag = ImageIO.read(new ByteArrayInputStream(usuario
						.getImagen()));
				imgUsuario.setContent(imag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		SecurityBotonera botonera = new SecurityBotonera() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void salir() {
				cerrarVentana(divEditarUsuario, "Editar Usuario", tabs);
			}

			@Override
			public void limpiar() {
				UsuarioSeguridad usuario = servicioUsuarioSeguridad
						.buscarPorLogin(nombreUsuarioSesion());
				id = nombreUsuarioSesion();
				txtNombreUsuarioEditar.setValue(usuario.getLogin());
				txtNombre.setValue(usuario.getNombre());
				txtApellido.setValue(usuario.getApellido());
				txtCorreo.setValue(usuario.getEmail());
				txtClaveUsuarioConfirmar.setValue(usuario.getPassword());
				txtClaveUsuarioNueva.setValue(usuario.getPassword());
				if (usuario.getImagen() == null) {
					try {
						imgUsuario.setContent(new AImage(url));
					} catch (IOException e) {

						e.printStackTrace();
					}
				} else {
					try {
						BufferedImage imag;
						imag = ImageIO.read(new ByteArrayInputStream(usuario
								.getImagen()));
						imgUsuario.setContent(imag);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void guardar() {
				if (validar()) {
					if (txtClaveUsuarioNueva.getValue().equals(
							txtClaveUsuarioConfirmar.getValue())) {
						UsuarioSeguridad usuario = servicioUsuarioSeguridad.buscarPorLogin(id);
						byte[] imagenUsuario = null;
						imagenUsuario = imgUsuario.getContent().getByteData();
						String password = txtClaveUsuarioConfirmar.getValue();
						usuario.setPassword(password);
						usuario.setImagen(imagenUsuario);
						usuario.setNombre(txtNombre.getValue());
						usuario.setApellido(txtApellido.getValue());
						usuario.setEmail(txtCorreo.getValue());
						servicioUsuarioSeguridad.guardar(usuario);
						SecurityMensaje.mensajeInformacion(SecurityMensaje.guardado);
						limpiar();
					} else {
						SecurityMensaje.mensajeError(SecurityMensaje.contrasennasNoCoinciden);
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
		botoneraEditarUsuario.appendChild(botonera);
	}

	/* Valida que los passwords sean iguales */
	@Listen("onChange = #txtClaveUsuarioConfirmar")
	public void validarPassword() {
		if (!txtClaveUsuarioNueva.getValue().equals(
				txtClaveUsuarioConfirmar.getValue())) {
			SecurityMensaje.mensajeAlerta(SecurityMensaje.contrasennasNoCoinciden);
		}
	}

	/* Valida el correo electronico */
	@Listen("onChange = #txtCorreoUsuario")
	public void validarCorreo() {
		if (!SecurityValidador.validarCorreo(txtCorreo.getValue())) {
			SecurityMensaje.mensajeAlerta(SecurityMensaje.correoInvalido);
		}
	}

	protected boolean validar() {
		if (txtClaveUsuarioConfirmar.getValue().equals("")
				|| txtClaveUsuarioNueva.getValue().equals("")
				|| txtApellido.getText().compareTo("") == 0
				|| txtCorreo.getText().compareTo("") == 0
				|| txtNombre.getText().compareTo("") == 0) {
			SecurityMensaje.mensajeError(SecurityMensaje.camposVacios);
			return false;
		} else {
			if (!SecurityValidador.validarCorreo(txtCorreo.getValue())) {
				SecurityMensaje.mensajeError(SecurityMensaje.correoInvalido);
				return false;
			} else {
				if (!txtClaveUsuarioConfirmar.getValue().equals(
						txtClaveUsuarioNueva.getValue())) {
					SecurityMensaje.mensajeError(SecurityMensaje.contrasennasNoCoinciden);
					return false;
				} else
					return true;
			}
		}
	}

	@Listen("onUpload = #fudImagenUsuario")
	public void processMedia(UploadEvent event) throws IOException {
		media = event.getMedia();
		imgUsuario.setContent(new AImage(url));
		if (SecurityValidador.validarTipoImagen(media)) {
			if (SecurityValidador.validarTamannoImagen(media)) {
				imgUsuario.setHeight("150px");
				imgUsuario.setWidth("150px");
				imgUsuario.setContent((org.zkoss.image.Image) media);
				imgUsuario.setVisible(true);
			} else {
				SecurityMensaje.mensajeError(SecurityMensaje.tamanioMuyGrande);
				imgUsuario.setContent(new AImage(url));
			}
		} else {
			SecurityMensaje.mensajeError(SecurityMensaje.formatoImagenNoValido);
			imgUsuario.setContent(new AImage(url));
		}
	}

}
