package security.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

import security.modelo.Arbol;
import security.modelo.Grupo;
import security.modelo.MArbol;
import security.modelo.Nodos;
import security.modelo.UsuarioSeguridad;

public class CArbol extends CSecurity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Wire
	private Tree arbolMenu;
	@Wire
	private Include contenido;
	@Wire
	private Label etiqueta;
	@Wire
	private Image imagenes;
	TreeModel<?> _model;
	URL url = getClass().getResource("/security/controlador/usuario.png");
	@Wire
	private Tab tab;
	@Wire
	private Tabbox tabBox;
	@Wire
	private West west;
	@Wire
	private Listbox ltbRoles;
	@Wire
	private Window wdwIndex;
	private Tabbox tabBox2;
	private Include contenido2;
	private Tab tab2;
	HashMap<String, Object> mapGeneral = new HashMap<String, Object>();

	@Override
	public void inicializar() throws IOException {
		Clients.confirmClose("Mensaje de la Aplicacion:");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		UsuarioSeguridad u = servicioUsuarioSeguridad.buscarPorLogin(auth
				.getName());

		List<Grupo> grupos = servicioGrupo.buscarGruposUsuario(u);
		ltbRoles.setModel(new ListModelList<Grupo>(grupos));

		if (u.getImagen() == null) {
			imagenes.setContent(new AImage(url));
		} else {
			try {
				BufferedImage imag;
				imag = ImageIO.read(new ByteArrayInputStream(u.getImagen()));
				imagenes.setContent(imag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		arbolMenu.setModel(getModel());

		if (tabs.size() != 0) {
			tabs.clear();
		}

	}

	/* Permite asignarle los nodos cargados con el metodo getFooRoot() al arbol */
	public TreeModel<?> getModel() {
		if (_model == null) {
			_model = new MArbol(getFooRoot());
		}
		return _model;
	}

	/*
	 * Permite obtener las funcionalidades asociadas al usuario en session y asi
	 * crear un arbol estructurado, segun la distribucion de las mismas
	 */
	private Nodos getFooRoot() {
		Nodos root = new Nodos(null, 0, "");
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
				auth.getAuthorities());
		ArrayList<Arbol> arbole = new ArrayList<Arbol>();
		List<Arbol> arboles = new ArrayList<Arbol>();
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int k = 0; k < authorities.size(); k++) {
			String nombre = authorities.get(k).toString();
			if (validadorNumero(nombre)) {
				Arbol arbol = servicioArbol.buscar(Long.parseLong(nombre));
				if (arbol != null)
					ids.add(arbol.getIdArbol());
				arbole.add(arbol);
			}
		}
		arboles.addAll(servicioArbol.ordenarPorOrden(ids));
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

	/*
	 * Permite seleccionar un elemento del arbol, mostrandolo en forma de
	 * pestaña y su contenido es cargado en un div
	 */
	@Listen("onClick = #arbolMenu")
	public void selectedNode() {
		if (arbolMenu.getSelectedItem() != null) {
			Treecell celda = (Treecell) arbolMenu.getSelectedItem()
					.getChildren().get(0).getChildren().get(0);
			long item = Long.valueOf(celda.getId());
			boolean abrir = true;
			Tab taba = new Tab();
			final Arbol arbolItem = servicioArbol.buscarPorId(item);
			mapGeneral.put("titulo", arbolItem.getNombre());
			if (!arbolItem.getUrl().equals("inicio")) {
				if (arbolItem.getMenu() != null)
					if (arbolItem.getMenu())
						west.setOpen(false);
				for (int i = 0; i < tabs.size(); i++) {
					if (tabs.get(i).getLabel().equals(arbolItem.getNombre())) {
						abrir = false;
						taba = tabs.get(i);
					}
				}
				if (abrir) {
					String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
					contenido = new Include();
					contenido.setSrc(null);
					contenido.setSrc(ruta);
					Tab newTab = new Tab(arbolItem.getNombre());
					newTab.setClosable(true);
					newTab.addEventListener(Events.ON_CLOSE,
							new EventListener<Event>() {
								@Override
								public void onEvent(Event arg0)
										throws Exception {
									for (int i = 0; i < tabs.size(); i++) {
										if (tabs.get(i).getLabel()
												.equals(arbolItem.getNombre())) {
											if (i == (tabs.size() - 1)
													&& tabs.size() > 1) {
												tabs.get(i - 1).setSelected(
														true);
											}
											tabs.get(i).close();
											tabs.remove(i);
											if (tabs.isEmpty())
												if (!west.isOpen())
													west.setOpen(true);
										}
									}
								}
							});
					newTab.setSelected(true);
					Tabpanel newTabpanel = new Tabpanel();
					newTabpanel.appendChild(contenido);
					tabBox.getTabs().insertBefore(newTab, tab);
					newTabpanel.setParent(tabBox.getTabpanels());
					tabs.add(newTab);
					mapGeneral.put("tabsGenerales", tabs);
					mapGeneral.put("idArbol", arbolItem.getIdArbol());
					mapGeneral.put("west", west);
					Sessions.getCurrent().setAttribute("mapaGeneral",
							mapGeneral);
				} else
					taba.setSelected(true);
			} else {
				if (!arbolMenu.getSelectedItem().isOpen())
					arbolMenu.getSelectedItem().setOpen(true);
				else
					arbolMenu.getSelectedItem().setOpen(false);
			}

		}
		tabBox2 = tabBox;
		contenido2 = contenido;
		tab2 = tab;
	}

	public void abrirVentanas(Arbol arbolItem) {
		boolean abrir = true;
		Tab taba = new Tab();

		if (!arbolItem.getUrl().equals("inicio")) {
			if (arbolItem.getMenu() != null)
				if (arbolItem.getMenu())
					west.setOpen(false);
			for (int i = 0; i < tabs.size(); i++) {
				if (tabs.get(i).getLabel().equals(arbolItem.getNombre())) {
					abrir = false;
					taba = tabs.get(i);
				}
			}
			if (abrir) {
				String ruta = "/vistas/" + arbolItem.getUrl() + ".zul";
				contenido2 = new Include();
				contenido2.setSrc(null);
				contenido2.setSrc(ruta);

				Tab newTab = new Tab(arbolItem.getNombre());
				newTab.setSelected(true);
				Tabpanel newTabpanel = new Tabpanel();
				newTabpanel.appendChild(contenido2);
				tabBox2.getTabs().insertBefore(newTab, tab2);
				newTabpanel.setParent(tabBox2.getTabpanels());
				tabs.add(newTab);
			} else {
				taba.setSelected(true);
			}
		}
	}

	/* Metodo que permite abrir la ventana de editar usuario en una pestaña */
	@Listen("onClick = #lblEditarCuenta")
	public void abrirEditarCuenta() {
		boolean abrir = true;
		Tab taba = new Tab();
		for (int i = 0; i < tabs.size(); i++) {
			if (tabs.get(i).getLabel().equals("Editar Usuario")) {
				abrir = false;
				taba = tabs.get(i);
			}
		}
		if (abrir) {
			String ruta = "/vistas/seguridad/VEditarUsuario.zul";
			contenido = new Include();
			contenido.setSrc(null);
			contenido.setSrc(ruta);
			Tab newTab = new Tab("Editar Usuario");
			newTab.setClosable(true);
			newTab.addEventListener(Events.ON_CLOSE,
					new EventListener<Event>() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							for (int i = 0; i < tabs.size(); i++) {
								if (tabs.get(i).getLabel()
										.equals("Editar Usuario")) {
									if (i == (tabs.size() - 1)
											&& tabs.size() > 1) {
										tabs.get(i - 1).setSelected(true);
									}

									tabs.get(i).close();
									tabs.remove(i);
									if (tabs.isEmpty())
										if (!west.isOpen())
											west.setOpen(true);
								}
							}
						}
					});
			newTab.setSelected(true);
			Tabpanel newTabpanel = new Tabpanel();
			newTabpanel.appendChild(contenido);
			tabBox.getTabs().insertBefore(newTab, tab);
			newTabpanel.setParent(tabBox.getTabpanels());
			tabs.add(newTab);
			mapGeneral.put("west", west);
			mapGeneral.put("titulo", "Editar Usuario");
			mapGeneral.put("tabsGenerales", tabs);
			Sessions.getCurrent().setAttribute("mapaGeneral", mapGeneral);
		} else
			taba.setSelected(true);
	}

	@Listen("onClick = #mnuItem")
	public void cerrarTodas() {
		for (int i = 0; i < tabs.size(); i++) {
			tabs.get(i).close();
			tabs.remove(i);
			i--;
		}
		if (!west.isOpen())
			west.setOpen(true);
	}

	public boolean validadorNumero(String numero) {
		String PATTERN = "^[0-9]*";
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(numero);
		return matcher.matches();
	}
}