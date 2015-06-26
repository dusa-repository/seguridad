package security.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.West;

import security.componente.SecurityBotonera;
import security.componente.SecurityCatalogo;
import security.componente.SecurityMensaje;
import security.modelo.Arbol;
import security.modelo.Grupo;
import security.modelo.MArbol;
import security.modelo.Nodos;

public class CGrupo extends CSecurity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Tree treeGrupo;
	@Wire
	private Textbox txtNombreGrupo;
	@Wire
	private Div botoneraGrupo;
	@Wire
	private Div divCatalogoGrupo;
	@Wire
	private Div divGrupo;
	long id = 0;
	@Wire
	private Groupbox gpxDatos;
	@Wire
	private Groupbox gpxRegistro;
	TreeModel<?> _model;
	SecurityCatalogo<Grupo> catalogo;
	protected List<Grupo> listaGeneral = new ArrayList<Grupo>();
	SecurityBotonera botonera;

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
		treeGrupo.setModel(getModel());
		treeGrupo.setCheckmark(true);
		treeGrupo.setMultiple(true);
		txtNombreGrupo.setFocus(true);
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
						Grupo grupo = catalogo.objetoSeleccionadoDelCatalogo();
						txtNombreGrupo.setValue(grupo.getNombre());
						id = grupo.getIdGrupo();
						visualizarFuncionalidades();

					} else
						SecurityMensaje
								.mensajeAlerta(SecurityMensaje.editarSoloUno);
				}
			}

			@Override
			public void salir() {
				cerrarVentana(divGrupo, titulo, tabs);

			}

			@Override
			public void reporte() {
			}

			@Override
			public void limpiar() {
				mostrarBotones(false);
				limpiarCampos();
				id = 0;
			}

			@Override
			public void guardar() {
				if (validar()) {
					List<Arbol> listaArbol = servicioArbol.listarArbol();
					Set<Arbol> arboles = new HashSet<Arbol>();
					Treechildren treeChildren = treeGrupo.getTreechildren();
					Collection<Treeitem> lista = treeChildren.getItems();
					for (int i = 0; i < listaArbol.size(); i++) {
						for (Iterator<?> iterator = lista.iterator(); iterator
								.hasNext();) {
							Treeitem treeitem = (Treeitem) iterator.next();
							if (listaArbol.get(i).getNombre()
									.equals(treeitem.getLabel())) {
								if (treeitem.isSelected()) {

									Arbol arbol = listaArbol.get(i);
									arboles.add(arbol);
								}
							}
						}
					}
					Boolean estatus = true;
					String nombre = txtNombreGrupo.getValue();
					Grupo grupo1 = new Grupo(id, estatus, nombre, arboles);
					servicioGrupo.guardarGrupo(grupo1);
					SecurityMensaje
							.mensajeInformacion(SecurityMensaje.guardado);
					limpiar();
					listaGeneral = servicioGrupo.buscarTodosOrdenados();
					catalogo.actualizarLista(listaGeneral);
				}

			}

			@Override
			public void eliminar() {
				if (gpxDatos.isOpen()) {
					/* Elimina Varios Registros */
					if (validarSeleccion()) {
						final List<Grupo> eliminarLista = catalogo
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
													servicioGrupo
															.guardarVarios(eliminarLista);
													SecurityMensaje
															.mensajeInformacion(SecurityMensaje.eliminado);
													listaGeneral = servicioGrupo
															.buscarTodosOrdenados();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
					}
				} else {
					/* Elimina un solo registro */
					if (id != 0) {
						Messagebox
								.show(SecurityMensaje.deseaEliminar,
										"Alerta",
										Messagebox.OK | Messagebox.CANCEL,
										Messagebox.QUESTION,
										new org.zkoss.zk.ui.event.EventListener<Event>() {
											public void onEvent(Event evt)
													throws InterruptedException {
												if (evt.getName()
														.equals("onOK")) {
													Grupo grupo = servicioGrupo
															.buscarGrupo(id);
													grupo.setEstado(false);
													servicioGrupo
															.guardarGrupo(grupo);
													SecurityMensaje
															.mensajeInformacion(SecurityMensaje.eliminado);
													limpiar();
													listaGeneral = servicioGrupo
															.buscarTodosOrdenados();
													catalogo.actualizarLista(listaGeneral);
												}
											}
										});
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
		botoneraGrupo.appendChild(botonera);

	}

	private void mostrarCatalogo() {
		listaGeneral = servicioGrupo.buscarTodosOrdenados();
		catalogo = new SecurityCatalogo<Grupo>(divCatalogoGrupo,
				"Catalogo de Grupos", listaGeneral, false, false, "Nombre",
				"Estado") {
			/**
					 * 
					 */
			private static final long serialVersionUID = 1L;

			@Override
			protected String[] crearRegistros(Grupo grupo) {
				String estado = "Activo";
				if (!grupo.getEstado())
					estado = "Inactivo";
				String[] registros = new String[2];
				registros[0] = grupo.getNombre();
				registros[1] = estado;
				return registros;
			}

			@Override
			protected List<Grupo> buscar(List<String> valores) {
				List<Grupo> lista = new ArrayList<Grupo>();

				for (Grupo grupo : listaGeneral) {
					String estado = "Activo";
					if (!grupo.getEstado())
						estado = "Inactivo";
					if (grupo.getNombre().toLowerCase()
							.contains(valores.get(0).toLowerCase())
							&& estado.toLowerCase().contains(
									valores.get(1).toLowerCase())) {
						lista.add(grupo);
					}
				}
				return lista;
			}
		};
		catalogo.setParent(divCatalogoGrupo);
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
		txtNombreGrupo.setFocus(true);
		txtNombreGrupo.setValue("");
		txtNombreGrupo.setDisabled(false);
		Treechildren treeChildren = treeGrupo.getTreechildren();
		Collection<Treeitem> lista = treeChildren.getItems();
		for (int i = 0; i < treeChildren.getItemCount(); i++) {
			for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
				Treeitem treeitem = (Treeitem) iterator.next();
				treeitem.setSelected(false);
			}
		}
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (treeitem.isOpen()) {
				treeitem.setOpen(false);
				lista = treeGrupo.getTreechildren().getItems();
				iterator = lista.iterator();
			}
		}
		id = 0;
		treeGrupo.setVisible(true);
		catalogo.limpiarSeleccion();
	}

	public void visualizarFuncionalidades() {
		treeGrupo.setVisible(true);
		Treechildren treeChildren = treeGrupo.getTreechildren();
		Collection<Treeitem> lista = treeChildren.getItems();
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (!treeitem.isOpen()) {
				treeitem.setOpen(true);
				lista = treeChildren.getItems();
				iterator = lista.iterator();
			}
		}
		Grupo grupo = servicioGrupo.buscarGrupo(id);
		List<Arbol> listaArbol = servicioArbol.buscarporGrupo(grupo);
		for (int i = 0; i < listaArbol.size(); i++) {
			for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
				Treeitem treeitem = (Treeitem) iterator.next();
				if (listaArbol.get(i).getNombre().equals(treeitem.getLabel())) {
					treeitem.setSelected(true);
				}
			}
		}
		for (Iterator<?> iterator = lista.iterator(); iterator.hasNext();) {
			Treeitem treeitem = (Treeitem) iterator.next();
			if (treeitem.isOpen()) {
				treeitem.setOpen(false);
				lista = treeGrupo.getTreechildren().getItems();
				iterator = lista.iterator();
			}
		}
	}

	private boolean validar() {
		if (txtNombreGrupo.getValue().equals("")) {
			SecurityMensaje.mensajeError(SecurityMensaje.camposVacios);
			return false;
		}
		return true;
	}

	public boolean validarSeleccion() {
		List<Grupo> seleccionados = catalogo.obtenerSeleccionados();
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

	@Listen("onClick = #gpxRegistro")
	public void abrirRegistro() {
		gpxDatos.setOpen(false);
		gpxRegistro.setOpen(true);
		mostrarBotones(false);
	}

	@Listen("onOpen = #gpxDatos")
	public void abrirCatalogo() {
		gpxDatos.setOpen(false);
		if (txtNombreGrupo.getText().compareTo("") != 0) {
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
			limpiarCampos();
			gpxDatos.setOpen(true);
			gpxRegistro.setOpen(false);
			mostrarBotones(true);
		}
	}

	@Listen("onSelect = #treeGrupo")
	public void selectedNode(SelectEvent<Treeitem, String> event) {
		if (!validarNodoHijo(event)) {
			List<Arbol> listaArbol2 = servicioArbol.listarArbol();
			Treechildren treeChildren = treeGrupo.getTreechildren();
			Collection<Treeitem> listaItems = treeChildren.getItems();
			Treeitem itemSeleccionado = event.getReference();
			Treecell celda = (Treecell) itemSeleccionado.getChildren().get(0)
					.getChildren().get(0);
			long idArbol = Long.valueOf(celda.getContext());
			List<Long> ids = new ArrayList<Long>();
			for (int o = 0; o < listaArbol2.size(); o++) {
				long id = listaArbol2.get(o).getIdArbol();
				ids.add(id);
			}
			Collections.sort(ids);
			List<Arbol> listaArbol = new ArrayList<Arbol>();
			for (int y = 0; y < ids.size(); y++) {
				listaArbol.add(servicioArbol.buscar(ids.get(y)));
			}
			Arbol arbolItem = servicioArbol.buscarPorId(idArbol);
			listaArbol.remove(arbolItem);
			long temp = arbolItem.getPadre();
			long temp2 = 0;
			long temp3 = temp;
			boolean encontradoHermanoHijo = false;
			boolean encontradoHermanoPadre = false;
			for (int i = 0; i < listaArbol.size(); i++) {
				if (temp == listaArbol.get(i).getIdArbol()) {
					for (Iterator<?> iterator = listaItems.iterator(); iterator
							.hasNext();) {
						Treeitem item = (Treeitem) iterator.next();
						if (listaArbol.get(i).getNombre()
								.equals(item.getLabel())) {
							temp2 = listaArbol.get(i).getPadre();
							for (int j = 0; j < listaArbol.size(); j++) {
								if (temp3 == listaArbol.get(j).getPadre()) {
									for (Iterator<?> iterator2 = listaItems
											.iterator(); iterator2.hasNext();) {
										Treeitem item2 = (Treeitem) iterator2
												.next();
										if (listaArbol.get(j).getNombre()
												.equals(item2.getLabel())) {
											if (item2.isSelected()) {
												encontradoHermanoHijo = true;
											}
										}
									}
								}
								if (temp2 == listaArbol.get(j).getPadre()
										&& listaArbol.get(i).getIdArbol() != listaArbol
												.get(j).getIdArbol()) {
									for (Iterator<?> iterator2 = listaItems
											.iterator(); iterator2.hasNext();) {
										Treeitem item2 = (Treeitem) iterator2
												.next();
										if (listaArbol.get(j).getNombre()
												.equals(item2.getLabel())) {
											if (item2.isSelected()) {
												encontradoHermanoPadre = true;
											}
										}
									}
								}
							}
							if (!item.isSelected()) {
								item.setSelected(true);
							} else {
								if (!encontradoHermanoHijo
										&& !encontradoHermanoPadre) {
									item.setSelected(false);
								}
								if (!encontradoHermanoHijo
										&& encontradoHermanoPadre
										&& !itemSeleccionado.isSelected()) {
									item.setSelected(false);
									encontradoHermanoHijo = true;
									encontradoHermanoPadre = false;
								}
							}
						}
					}
					temp = listaArbol.get(i).getPadre();
					i = -1;
				}
			}
		}
	}

	public boolean validarNodoHijo(SelectEvent<Treeitem, String> event) {
		Treeitem itemSeleccionado = event.getReference();
		if (itemSeleccionado.isSelected()) {
			itemSeleccionado.setOpen(true);
			Treecell celda = (Treecell) itemSeleccionado.getChildren().get(0)
					.getChildren().get(0);
			long item = Long.valueOf(celda.getContext());
			if (itemSeleccionado.getChildren().size() > 1) {
				Treechildren treeChildren = (Treechildren) itemSeleccionado
						.getChildren().get(1);
				Collection<Treeitem> listaItems = treeChildren.getItems();
				for (Iterator<?> iterator = listaItems.iterator(); iterator
						.hasNext();) {
					Treeitem itemTree = (Treeitem) iterator.next();
					celda = (Treecell) itemTree.getChildren().get(0)
							.getChildren().get(0);
					long itemHijo = Long.valueOf(celda.getContext());
					Arbol arbol = servicioArbol.buscarPorId(itemHijo);
					if (item == arbol.getPadre()) {
						itemTree.setSelected(true);
					}
				}

			}
		} else {
			itemSeleccionado.setOpen(true);
			Treecell celda = (Treecell) itemSeleccionado.getChildren().get(0)
					.getChildren().get(0);
			long item = Long.valueOf(celda.getContext());
			if (itemSeleccionado.getChildren().size() > 1) {
				Treechildren treeChildren = (Treechildren) itemSeleccionado
						.getChildren().get(1);
				Collection<Treeitem> listaItems = treeChildren.getItems();
				for (Iterator<?> iterator = listaItems.iterator(); iterator
						.hasNext();) {
					Treeitem itemTree = (Treeitem) iterator.next();
					celda = (Treecell) itemTree.getChildren().get(0)
							.getChildren().get(0);
					long itemHijo = Long.valueOf(celda.getContext());
					Arbol arbol = servicioArbol.buscarPorId(itemHijo);
					if (item == arbol.getPadre()) {
						itemTree.setSelected(false);
					}
				}
			}
		}
		return false;
	}

	public TreeModel<?> getModel() {
		if (_model == null) {
			_model = new MArbol(getFooRoot());
		}
		return _model;
	}

	private Nodos getFooRoot() {
		Nodos root = new Nodos(null, 0, "");
		List<Arbol> arboles = servicioArbol.buscarOrdenados();
		List<Long> idsPadre = new ArrayList<Long>();
		List<Nodos> nodos = new ArrayList<Nodos>();
		return crearArbol(root, nodos, arboles, 0, idsPadre);
	}

	private Nodos crearArbol(Nodos roote, List<Nodos> nodos,
			List<Arbol> arboles, int i, List<Long> idsPadre) {
		for (int z = 0; z < arboles.size(); z++) {
			Nodos oneLevelNode = new Nodos(null, 0, "");
			Nodos two = new Nodos(null, 0, "");
			if (arboles.get(z).getPadre() == 0) {
				oneLevelNode = new Nodos(roote, (int) arboles.get(z)
						.getIdArbol(), arboles.get(z).getNombre());
				roote.appendChild(oneLevelNode);
				idsPadre.add(arboles.get(z).getIdArbol());
				nodos.add(oneLevelNode);
			} else {
				for (int j = 0; j < idsPadre.size(); j++) {
					if (idsPadre.get(j) == arboles.get(z).getPadre()) {
						oneLevelNode = nodos.get(j);
						two = new Nodos(oneLevelNode, (int) arboles.get(z)
								.getIdArbol(), arboles.get(z).getNombre());
						oneLevelNode.appendChild(two);
						idsPadre.add(arboles.get(z).getIdArbol());
						nodos.add(two);
					}
				}
			}
		}
		return roote;
	}

}
