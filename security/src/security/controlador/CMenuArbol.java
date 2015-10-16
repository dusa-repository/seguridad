package security.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;

import security.componente.SecurityBotonera;
import security.componente.SecurityCatalogo;
import security.componente.SecurityMensaje;
import security.modelo.Arbol;
import security.modelo.Grupo;

public class CMenuArbol extends CSecurity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Longbox txtPadre;
	@Wire
	private Intbox txtOrden;
	@Wire
	private Textbox txtUrl;
	@Wire
	private Div divVMenuArbol;
	@Wire
	private Div botoneraMenuArbol;
	@Wire
	private Div divCatalogoMenuArbol;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	@Wire
	private Radio rdoSi;
	@Wire
	private Radio rdoNo;
	@Wire
	private Radio rdoSiMenu;
	@Wire
	private Radio rdoNoMenu;
	protected List<Arbol> listaGeneral = new ArrayList<Arbol>();

	SecurityBotonera botonera;
	SecurityCatalogo<Arbol> catalogo;
	long clave = 0;

	@SuppressWarnings("unchecked")
	@Override
	public void inicializar() throws IOException {
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("mapaGeneral");
		if (map != null) {
			if (map.get("tabsGenerales") != null) {
				tabs = (List<Tab>) map.get("tabsGenerales");
				titulo = (String) map.get("titulo");
				west = (West) map.get("west");
				map.clear();
				map = null;
			}
		}
		txtNombre.setFocus(true);
		mostrarCatalogo();
		botonera = new SecurityBotonera() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void seleccionar() {
				if (validarSeleccion()) {
					if (catalogo.obtenerSeleccionados().size() == 1) {
						mostrarBotones(false);
						abrirRegistro();
						Arbol arbol = catalogo.objetoSeleccionadoDelCatalogo();
						clave = arbol.getIdArbol();
						txtUrl.setValue(arbol.getUrl());
						txtNombre.setValue(arbol.getNombre());
						txtPadre.setValue(arbol.getPadre());
						if (arbol.getOrden() != null)
							txtOrden.setValue(arbol.getOrden());
						else
							txtOrden.setValue(null);
						if (arbol.getManejo() != null)
							if (arbol.getManejo() == 0)
								rdoNo.setChecked(true);
							else
								rdoSi.setChecked(true);
						else {
							rdoNo.setChecked(false);
							rdoSi.setChecked(false);
						}
						if (arbol.getMenu() != null)
							if (arbol.getMenu())
								rdoSiMenu.setChecked(true);
							else
								rdoNoMenu.setChecked(true);
						else {
							rdoNoMenu.setChecked(false);
							rdoSiMenu.setChecked(false);
						}
						txtNombre.setFocus(true);
					} else
						SecurityMensaje
								.mensajeAlerta(SecurityMensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divVMenuArbol, titulo, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				clave = 0;
			}

			@Override
			public void guardar() {

				if (validar()) {
					String url = txtUrl.getValue();
					String nombre = txtNombre.getValue();
					Long padre = txtPadre.getValue();
					Arbol arbol = new Arbol();
					arbol.setNombre(nombre);
					arbol.setPadre(padre);
					arbol.setUrl(url);
					arbol.setOrden(txtOrden.getValue());
					arbol.setIdArbol(clave);
					int maneja = 0;
					if (rdoSi.isChecked())
						maneja = 1;
					arbol.setManejo(maneja);
					Boolean menu = false;
					if (rdoSiMenu.isChecked())
						menu = true;
					arbol.setMenu(menu);
					servicioArbol.guardar(arbol);
					SecurityMensaje
							.mensajeInformacion(SecurityMensaje.guardado);
					limpiar();
					listaGeneral = servicioArbol.listarArbol();
					catalogo.actualizarLista(listaGeneral);
				}

			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Arbol> eliminarLista = catalogo
								.obtenerSeleccionados();
						List<Grupo> grupos = servicioGrupo
								.buscarArboles(eliminarLista);
						if (grupos.isEmpty())
							Messagebox
									.show("¿Desea Eliminar los "
											+ eliminarLista.size()
											+ " Registros?",
											"Alerta",
											Messagebox.OK | Messagebox.CANCEL,
											Messagebox.QUESTION,
											new org.zkoss.zk.ui.event.EventListener<Event>() {
												public void onEvent(Event evt)
														throws InterruptedException {
													if (evt.getName().equals(
															"onOK")) {
														servicioArbol
																.eliminarVarios(eliminarLista);
														SecurityMensaje
																.mensajeInformacion(SecurityMensaje.eliminado);
														listaGeneral = servicioArbol
																.listarArbol();
														catalogo.actualizarLista(listaGeneral);
													}
												}
											});
						else
							SecurityMensaje
									.mensajeError("Alguno de los menues han sido asignados a uno o mas grupos, por favor, verifique que no esten asignados a ningun grupo");
					}
				} else {
					/* Elimina un solo registro */
					if (clave != 0) {
						Arbol arbol = servicioArbol.buscar(clave);
						List<Arbol> arboles = new ArrayList<Arbol>();
						arboles.add(arbol);
						if (servicioGrupo.buscarArboles(arboles).isEmpty())
							Messagebox
									.show(SecurityMensaje.deseaEliminar,
											"Alerta",
											Messagebox.OK | Messagebox.CANCEL,
											Messagebox.QUESTION,
											new org.zkoss.zk.ui.event.EventListener<Event>() {
												public void onEvent(Event evt)
														throws InterruptedException {
													if (evt.getName().equals(
															"onOK")) {
														servicioArbol
																.eliminarUno(clave);
														SecurityMensaje
																.mensajeInformacion(SecurityMensaje.eliminado);
														limpiar();
														listaGeneral = servicioArbol
																.listarArbol();
														catalogo.actualizarLista(listaGeneral);
													}
												}
											});
						else
							SecurityMensaje
									.mensajeError("El menu ha sido asignado a uno o mas grupos, por favor, verifique que este no haya sido asignado a ningun grupo");

					} else
						SecurityMensaje
								.mensajeAlerta(SecurityMensaje.noSeleccionoRegistro);
				}

			}

			@Override
			public void buscar() {

				abrirCatalogo();

			}

			@Override
			public void ayuda() {

			}

			@Override
			public void annadir() {
				abrirRegistro();
				mostrarBotones(false);
			}
		};
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(1).setVisible(false);
		botonera.getChildren().get(3).setVisible(false);
		botonera.getChildren().get(5).setVisible(false);
		botoneraMenuArbol.appendChild(botonera);

	}

	public void mostrarBotones(boolean bol) {
		botonera.getChildren().get(1).setVisible(!bol);
		botonera.getChildren().get(2).setVisible(bol);
		botonera.getChildren().get(6).setVisible(false);
		botonera.getChildren().get(8).setVisible(false);
		botonera.getChildren().get(0).setVisible(bol);
		botonera.getChildren().get(3).setVisible(!bol);
		botonera.getChildren().get(5).setVisible(!bol);
	}

	public void limpiarCampos() {
		clave = 0;
		txtUrl.setValue("");
		txtNombre.setValue("");
		txtPadre.setValue(null);
		txtOrden.setValue(null);
		catalogo.limpiarSeleccion();
		rdoNo.setChecked(false);
		rdoSi.setChecked(false);
		rdoNoMenu.setChecked(false);
		rdoSiMenu.setChecked(false);
	}

	public boolean validarSeleccion() {
		List<Arbol> seleccionados = catalogo.obtenerSeleccionados();
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

	protected boolean validar() {
		if (!camposLLenos()) {
			SecurityMensaje.mensajeError(SecurityMensaje.camposVacios);
			return false;
		} else
			return true;
	}

	public boolean camposLLenos() {
		if (txtUrl.getText().compareTo("") == 0
				|| txtNombre.getText().compareTo("") == 0
				|| txtPadre.getText().compareTo("") == 0
				|| (!rdoNo.isChecked() && !rdoSi.isChecked())
				|| (!rdoNoMenu.isChecked() && !rdoSiMenu.isChecked())) {
			return false;
		} else
			return true;
	}

	public boolean camposEditando() {
		if (txtUrl.getText().compareTo("") != 0
				|| txtNombre.getText().compareTo("") != 0
				|| txtPadre.getText().compareTo("") != 0
				|| (rdoNo.isChecked() || rdoSi.isChecked())
				|| (rdoNoMenu.isChecked() || rdoSiMenu.isChecked())) {
			return true;
		} else
			return false;
	}

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
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

	public void mostrarCatalogo() {
		listaGeneral = servicioArbol.listarArbol();
		catalogo = new SecurityCatalogo<Arbol>(divCatalogoMenuArbol, "Arbol",
				listaGeneral, false, false, "Codigo", "Nombre", "Padre", "Url",
				"Orden", "Manejo Version", "Requiere Menu") {

			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Arbol> buscar(List<String> valores) {

				List<Arbol> lista = new ArrayList<Arbol>();

				for (Arbol arbol : listaGeneral) {
					String version = "No", menu = "No";
					Integer orden = 0;
					if (arbol.getManejo() != null)
						if (arbol.getManejo() == 1)
							version = "Si";
					if (arbol.getOrden() != null)
							orden = arbol.getOrden();
					if (arbol.getMenu() != null)
						if (arbol.getMenu())
							menu = "Si";
					if (String.valueOf(arbol.getIdArbol()).toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& arbol.getNombre().toLowerCase()
									.contains(valores.get(1).toLowerCase())
							&& String.valueOf(arbol.getPadre()).toLowerCase()
									.contains(valores.get(2).toLowerCase())
							&& arbol.getUrl().toLowerCase()
									.contains(valores.get(3).toLowerCase())
							&& String.valueOf(orden).toLowerCase()
									.contains(valores.get(4).toLowerCase())
							&& version.toLowerCase().contains(
									valores.get(5).toLowerCase())
							&& menu.toLowerCase().contains(
									valores.get(6).toLowerCase())) {
						lista.add(arbol);
					}
				}
				return lista;
			}

			@Override
			protected String[] crearRegistros(Arbol arbol) {
				String version = "No", menu = "No";
				Integer orden = 0;
				if (arbol.getManejo() != null)
					if (arbol.getManejo() == 1)
						version = "Si";
				if (arbol.getOrden() != null)
						orden = arbol.getOrden();
				if (arbol.getMenu() != null)
					if (arbol.getMenu())
						menu = "Si";
				String[] registros = new String[7];
				registros[0] = String.valueOf(arbol.getIdArbol());
				registros[1] = arbol.getNombre();
				registros[2] = String.valueOf(arbol.getPadre());
				registros[3] = arbol.getUrl();
				registros[4] = String.valueOf(orden);
				registros[5] = version;
				registros[6] = menu;
				return registros;
			}
		};
		catalogo.setParent(divCatalogoMenuArbol);
	}

}
