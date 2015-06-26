package security.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;

import security.componente.SecurityBotonera;
import security.componente.SecurityCatalogo;
import security.componente.SecurityMensaje;
import security.componente.SecurityValidador;
import security.modelo.Grupo;
import security.modelo.UsuarioSeguridad;

public class CUsuario extends CSecurity {

	private static final long serialVersionUID = 7879830599305337459L;
	@Wire
	private Div divUsuario;
	@Wire
	private Div botoneraUsuario;
	@Wire
	private Div catalogoUsuario;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtApellido;
	@Wire
	private Textbox txtCorreo;
	@Wire
	private Textbox txtLoginUsuario;
	@Wire
	private Textbox txtPasswordUsuario;
	@Wire
	private Textbox txtPassword2Usuario;
	@Wire
	private Listbox ltbGruposDisponibles;
	@Wire
	private Listbox ltbGruposAgregados;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	@Wire
	private Image imagen;
	@Wire
	private Button fudImagenUsuario;
	@Wire
	private Media media;
	String id = "";
	SecurityCatalogo<UsuarioSeguridad> catalogo;
	protected List<UsuarioSeguridad> listaGeneral = new ArrayList<UsuarioSeguridad>();
	List<Grupo> gruposDisponibles = new ArrayList<Grupo>();
	List<Grupo> gruposOcupados = new ArrayList<Grupo>();
	URL url = getClass().getResource("/security/controlador/usuario.png");
	CArbol cArbol = new CArbol();
	SecurityBotonera botonera;

	@SuppressWarnings("unchecked")
	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> mapa = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (mapa != null) {
			if (mapa.get("tabsGenerales") != null) {
				titulo = (String) mapa.get("titulo");
				tabs = (List<Tab>) mapa.get("tabsGenerales");
				west = (West) mapa.get("west");
				mapa.clear();
				mapa = null;
			}
		}
		llenarListas(null);
		mostrarCatalogo();
		try {
			imagen.setContent(new AImage(url));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		gpxRegistro.setOpen(false);
		botonera = new SecurityBotonera() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void salir() {
				cerrarVentana(divUsuario, titulo, tabs);
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
			}

			@Override
			public void guardar() {
				if (validar()) {
					if (buscarPorLogin()) {
						Set<Grupo> gruposUsuario = new HashSet<Grupo>();
						for (int i = 0; i < ltbGruposAgregados.getItemCount(); i++) {
							Grupo grupo = ltbGruposAgregados.getItems().get(i)
									.getValue();
							gruposUsuario.add(grupo);
						}

						String correo = txtCorreo.getValue();
						String login = txtLoginUsuario.getValue();
						String password = txtPasswordUsuario.getValue();
						String nombre = txtNombre.getValue();
						String apellido = txtApellido.getValue();
						byte[] imagenUsuario = null;
						if (media instanceof org.zkoss.image.Image) {
							imagenUsuario = imagen.getContent().getByteData();

						} else {
							try {
								imagen.setContent(new AImage(url));
							} catch (IOException e) {
								e.printStackTrace();
							}
							imagenUsuario = imagen.getContent().getByteData();
						}
						UsuarioSeguridad usuario = new UsuarioSeguridad(login, correo, password,
								imagenUsuario, true, nombre, apellido,
								fechaHora, horaAuditoria,
								nombreUsuarioSesion(), gruposUsuario);
						servicioUsuarioSeguridad.guardar(usuario);
						limpiar();
						listaGeneral = servicioUsuarioSeguridad.buscarTodos();
						catalogo.actualizarLista(listaGeneral);
						SecurityMensaje
								.mensajeInformacion(SecurityMensaje.guardado);
					}
				}
			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<UsuarioSeguridad> eliminarLista = catalogo
								.obtenerSeleccionados();
						Messagebox
								.show("¿Desea Eliminar los "
										+ eliminarLista.size() + " Registros?",
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													for (int i = 0; i < eliminarLista
															.size(); i++) {
														eliminarLista.get(i)
																.setEstado(
																		false);
													}
													servicioUsuarioSeguridad
															.guardarVarios(eliminarLista);
													SecurityMensaje
															.mensajeInformacion(SecurityMensaje.eliminado);
													listaGeneral = servicioUsuarioSeguridad
															.buscarTodos();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					if (!id.equals("")) {
						Messagebox
								.show("¿Esta Seguro de Eliminar el Usuario?",
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													UsuarioSeguridad usuario = servicioUsuarioSeguridad
															.buscarPorLogin(id);
													usuario.setEstado(false);
													servicioUsuarioSeguridad
															.guardar(usuario);
													limpiar();
													SecurityMensaje
															.mensajeInformacion(SecurityMensaje.eliminado);
												}
											}
										});
					} else
						SecurityMensaje
								.mensajeAlerta(SecurityMensaje.noSeleccionoRegistro);
				}
			}

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						UsuarioSeguridad usuario = catalogo
								.objetoSeleccionadoDelCatalogo();
						txtCorreo.setValue(usuario.getEmail());
						txtLoginUsuario.setValue(usuario.getLogin());
						txtPasswordUsuario.setValue(usuario.getPassword());
						txtPassword2Usuario.setValue(usuario.getPassword());
						txtNombre.setValue(usuario.getNombre());
						txtApellido.setValue(usuario.getApellido());
						BufferedImage imag;
						if (usuario.getImagen() != null) {
							try {
								imag = ImageIO.read(new ByteArrayInputStream(
										usuario.getImagen()));
								imagen.setContent(imag);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						txtLoginUsuario.setDisabled(true);
						id = usuario.getLogin();
						llenarListas(usuario);
					} else
						SecurityMensaje
								.mensajeAlerta(SecurityMensaje.editarSoloUno);
				}
			}

			@Override
			public void buscar() {
				abrirCatalogo();
			}

			@Override
			public void annadir() {
				abrirRegistro();
				mostrarBotones(false);
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
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botoneraUsuario.appendChild(botonera);
	}

	@Listen("onOpen = #gpxDatos")
	public void abrirCatalogo() {
		gpxDatos.setOpen(false);
		if (camposEditando()) {
			Messagebox.show(SecurityMensaje.estaEditando, "Alerta",
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onYes")) {
								gpxDatos.setOpen(false);
								gpxRegistro.setOpen(true);
							} else {
								if (evt.getName().equals("onNo")) {
									gpxDatos.setOpen(true);
									gpxRegistro.setOpen(false);
									limpiarCampos();
									mostrarBotones(true);
								}
							}
						}
					});
		} else {
			gpxDatos.setOpen(true);
			gpxRegistro.setOpen(false);
			mostrarBotones(true);
		}
	}

	public boolean camposEditando() {
		if (txtApellido.getText().compareTo("") != 0
				|| txtLoginUsuario.getText().compareTo("") != 0
				|| txtPasswordUsuario.getText().compareTo("") != 0
				|| txtPassword2Usuario.getText().compareTo("") != 0
				|| txtNombre.getText().compareTo("") != 0) {
			return true;
		} else
			return false;
	}

	protected void limpiarCampos() {
		gruposDisponibles.clear();
		gruposOcupados.clear();
		ltbGruposAgregados.getItems().clear();
		ltbGruposDisponibles.getItems().clear();
		txtLoginUsuario.setValue("");
		txtPasswordUsuario.setValue("");
		txtPassword2Usuario.setValue("");
		txtApellido.setValue("");
		txtNombre.setValue("");
		txtCorreo.setValue("");
		try {
			imagen.setContent(new AImage(url));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		id = "";
		llenarListas(null);
		catalogo.limpiarSeleccion();
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
	}

	protected void mostrarBotones(boolean bol) {
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
	}

	protected boolean validarSeleccion() {
		List<UsuarioSeguridad> seleccionados = catalogo.obtenerSeleccionados();
		if (seleccionados == null) {
			SecurityMensaje.mensajeAlerta(SecurityMensaje.noHayRegistros);
			return false;
		} else {
			if (seleccionados.isEmpty()) {
				SecurityMensaje.mensajeAlerta(SecurityMensaje.noSeleccionoItem);
				return false;
			} else {
				return true;
			}
		}
	}

	private void mostrarCatalogo() {
		listaGeneral = servicioUsuarioSeguridad.buscarTodos();
		catalogo = new SecurityCatalogo<UsuarioSeguridad>(catalogoUsuario, "Usuario",
				listaGeneral, false, false, "Cedula", "Correo", "Nombre",
				"Apellido", "Estado") {

			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			@Override
			protected List<UsuarioSeguridad> buscar(List<String> valores) {

				List<UsuarioSeguridad> user = new ArrayList<UsuarioSeguridad>();

				for (UsuarioSeguridad actividadord : listaGeneral) {
					String estado = "Activo";
					if (!actividadord.isEstado())
						estado = "Inactivo";
					if (actividadord.getLogin().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& actividadord.getEmail().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& actividadord.getNombre().toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& actividadord.getApellido().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& estado.toLowerCase().contains(
									valores.get(4).toLowerCase())) {

						user.add(actividadord);
					}
				}
				return user;
			}

			@Override
			protected String[] crearRegistros(UsuarioSeguridad usuarios) {
				String estado = "Activo";
				if (!usuarios.isEstado())
					estado = "Inactivo";
				String[] registros = new String[5];
				registros[0] = usuarios.getLogin();
				registros[1] = usuarios.getEmail();
				registros[2] = usuarios.getNombre();
				registros[3] = usuarios.getApellido();
				registros[4] = estado;
				return registros;
			}

		};
		catalogo.setParent(catalogoUsuario);
	}

	/* Validaciones de pantalla para poder realizar el guardar */
	public boolean validar() {
		if (txtApellido.getText().compareTo("") == 0
				|| txtCorreo.getText().compareTo("") == 0
				|| txtLoginUsuario.getText().compareTo("") == 0
				|| txtNombre.getText().compareTo("") == 0
				|| txtPassword2Usuario.getText().compareTo("") == 0
				|| txtPasswordUsuario.getText().compareTo("") == 0) {
			SecurityMensaje.mensajeError(SecurityMensaje.camposVacios);
			return false;
		} else {
			if (!SecurityValidador.validarCorreo(txtCorreo.getValue())) {
				SecurityMensaje.mensajeError(SecurityMensaje.correoInvalido);
				return false;
			} else {
				if (!txtPasswordUsuario.getValue().equals(
						txtPassword2Usuario.getValue())) {
					SecurityMensaje
							.mensajeError(SecurityMensaje.contrasennasNoCoinciden);
					return false;
				} else
					return true;
			}
		}
	}

	/* Valida que los passwords sean iguales */
	@Listen("onChange = #txtPassword2Usuario")
	public void validarPassword() {
		if (!txtPasswordUsuario.getValue().equals(
				txtPassword2Usuario.getValue())) {
			SecurityMensaje
					.mensajeAlerta(SecurityMensaje.contrasennasNoCoinciden);
		}
	}

	/* Valida el correo electronico */
	@Listen("onChange = #txtCorreoUsuario")
	public void validarCorreo() {
		if (!SecurityValidador.validarCorreo(txtCorreo.getValue())) {
			SecurityMensaje.mensajeAlerta(SecurityMensaje.correoInvalido);
		}
	}

	/* LLena las listas dado un usario */
	public void llenarListas(UsuarioSeguridad usuario) {
		gruposDisponibles = servicioGrupo.buscarTodos();
		if (usuario == null) {
			ltbGruposDisponibles.setModel(new ListModelList<Grupo>(
					gruposDisponibles));
		} else {
			gruposOcupados = servicioGrupo.buscarGruposDelUsuario(usuario);
			ltbGruposAgregados
					.setModel(new ListModelList<Grupo>(gruposOcupados));
			if (!gruposOcupados.isEmpty()) {
				List<Long> ids = new ArrayList<Long>();
				for (int i = 0; i < gruposOcupados.size(); i++) {
					long id = gruposOcupados.get(i).getIdGrupo();
					ids.add(id);
				}
				gruposDisponibles = servicioGrupo.buscarGruposDisponibles(ids);
				ltbGruposDisponibles.setModel(new ListModelList<Grupo>(
						gruposDisponibles));
			}
		}
		ltbGruposAgregados.setMultiple(false);
		ltbGruposAgregados.setCheckmark(false);
		ltbGruposAgregados.setMultiple(true);
		ltbGruposAgregados.setCheckmark(true);

		ltbGruposDisponibles.setMultiple(false);
		ltbGruposDisponibles.setCheckmark(false);
		ltbGruposDisponibles.setMultiple(true);
		ltbGruposDisponibles.setCheckmark(true);
	}

	/* Permite subir una imagen a la vista */
	@Listen("onUpload = #fudImagenUsuario")
	public void processMedia(UploadEvent event) {
		media = event.getMedia();
		imagen.setContent((org.zkoss.image.Image) media);

	}

	/*
	 * Permite mover uno o varios elementos seleccionados desde la lista de la
	 * izquierda a la lista de la derecha
	 */
	@Listen("onClick = #pasar1")
	public void moverDerecha() {
		// gruposDisponibles = servicioGrupo.buscarTodos();
		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem = ltbGruposDisponibles.getItems();
		if (listItem.size() != 0) {
			for (int i = 0; i < listItem.size(); i++) {
				if (listItem.get(i).isSelected()) {
					Grupo grupo = listItem.get(i).getValue();
					gruposDisponibles.remove(grupo);
					gruposOcupados.add(grupo);
					ltbGruposAgregados.setModel(new ListModelList<Grupo>(
							gruposOcupados));
					listitemEliminar.add(listItem.get(i));
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbGruposDisponibles.removeItemAt(listitemEliminar.get(i)
					.getIndex());
		}
		ltbGruposAgregados.setMultiple(false);
		ltbGruposAgregados.setCheckmark(false);
		ltbGruposAgregados.setMultiple(true);
		ltbGruposAgregados.setCheckmark(true);
	}

	/*
	 * Permite mover uno o varios elementos seleccionados desde la lista de la
	 * derecha a la lista de la izquierda
	 */
	@Listen("onClick = #pasar2")
	public void moverIzquierda() {
		List<Listitem> listitemEliminar = new ArrayList<Listitem>();
		List<Listitem> listItem2 = ltbGruposAgregados.getItems();
		if (listItem2.size() != 0) {
			for (int i = 0; i < listItem2.size(); i++) {
				if (listItem2.get(i).isSelected()) {
					Grupo grupo = listItem2.get(i).getValue();
					gruposOcupados.remove(grupo);
					gruposDisponibles.add(grupo);
					ltbGruposDisponibles.setModel(new ListModelList<Grupo>(
							gruposDisponibles));
					listitemEliminar.add(listItem2.get(i));
				}
			}
		}
		for (int i = 0; i < listitemEliminar.size(); i++) {
			ltbGruposAgregados.removeItemAt(listitemEliminar.get(i).getIndex());
		}
		ltbGruposDisponibles.setMultiple(false);
		ltbGruposDisponibles.setCheckmark(false);
		ltbGruposDisponibles.setMultiple(true);
		ltbGruposDisponibles.setCheckmark(true);
	}

	/* Busca si existe un usuario con el mismo login */
	@Listen("onChange = #txtLoginUsuario")
	public boolean buscarPorLogin() {
		UsuarioSeguridad usuario = servicioUsuarioSeguridad.buscarPorLogin(txtLoginUsuario
				.getValue());
		if (usuario == null)
			return true;
		else {
			if (usuario.getLogin().equals(id))
				return true;
			else {
				SecurityMensaje.mensajeAlerta(SecurityMensaje.loginUsado);
				txtLoginUsuario.setValue("");
				txtLoginUsuario.setFocus(true);
				return false;
			}
		}
	}

	/* Busca si existe un usuario con la misma cedula nombre escrita */
	@Listen("onChange = #txtCedulaUsuario")
	public void buscarPorCedula() {
		UsuarioSeguridad usuario = servicioUsuarioSeguridad.buscarPorLogin(txtLoginUsuario
				.getValue());
		if (usuario != null)
			llenarCampos(usuario);
	}

	/* LLena los campos del formulario dado un usuario */
	public void llenarCampos(UsuarioSeguridad usuario) {
		txtCorreo.setValue(usuario.getEmail());
		txtLoginUsuario.setValue(usuario.getLogin());
		txtPasswordUsuario.setValue(usuario.getPassword());
		txtPassword2Usuario.setValue(usuario.getPassword());
		txtNombre.setValue(usuario.getNombre());
		txtApellido.setValue(usuario.getApellido());
		BufferedImage imag;
		if (usuario.getImagen() != null) {
			try {
				imag = ImageIO.read(new ByteArrayInputStream(usuario
						.getImagen()));
				imagen.setContent(imag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		id = usuario.getLogin();
		llenarListas(usuario);
	}

	public void recibirGrupo(List<Grupo> lista, Listbox l) {
		ltbGruposDisponibles = l;
		gruposDisponibles = lista;
		ltbGruposDisponibles.setModel(new ListModelList<Grupo>(
				gruposDisponibles));
		ltbGruposDisponibles.setMultiple(false);
		ltbGruposDisponibles.setCheckmark(false);
		ltbGruposDisponibles.setMultiple(true);
		ltbGruposDisponibles.setCheckmark(true);
	}

}
