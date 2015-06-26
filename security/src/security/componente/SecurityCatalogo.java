package security.componente;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Cell;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.XulElement;

public abstract class SecurityCatalogo<Clase> extends Window {

	private static final long serialVersionUID = 1L;
	Listbox lsbCatalogo;
	Button exportador;
	Button pagineo;
	SecurityMensaje msj = new SecurityMensaje();
	Textbox txtSY;
	Label labelSYNombre;
	Textbox txtRT;
	Label labelRTNombre;
	Label labelBuscado;

	public SecurityCatalogo(final Component cGenerico, String titulo,
			List<Clase> lista, boolean emergente, boolean udc,
			String... campos) {
		super("", "2", false);
		this.setId("cmpCatalogo" + titulo);
		this.setStyle("background-header:#FF7925; background: #f4f2f2");
		crearLista( titulo, lista, campos, emergente, udc);
		lsbCatalogo.addEventListener(Events.ON_SELECT,
				new EventListener<Event>() {

					@Override
					public void onEvent(Event arg0) throws Exception {
						Events.postEvent(cGenerico, new Event("onSeleccion"));
					}
				});
	}

	private void crearLista(String titulo, List<Clase> lista, String[] campos,
			boolean emergente, boolean param2) {
		exportador = crearBotonExportador();
		pagineo = crearBotonPagineo();
		this.setClosable(emergente);
		XulElement componente = crearEncabezadoCatalogo(titulo, emergente,
				param2);
		if (componente != null)
			this.appendChild(componente);
		this.appendChild(new Separator());
		lsbCatalogo = crearListbox(lista, campos, emergente);
		this.appendChild(lsbCatalogo);
	}

	private XulElement crearEncabezadoCatalogo(String titulo,
			boolean emergente, boolean param2) {
		if (emergente) {
			this.setWidth("80%");
			this.setTitle(titulo);
			return encabezado();
		} else {
			this.setWidth("auto");
			if (param2) {
				this.setWidth("80%");
				this.setTitle(titulo);
				this.setClosable(true);
			}
			this.appendChild(new Separator());
			Space espacio = new Space();
			espacio.setHeight("10px");
			espacio.setStyle("background:white");
			Hbox box = new Hbox();
			Cell c1 = new Cell();
			c1.setWidth("96%");
			c1.appendChild(espacio);
			box.appendChild(c1);
			Cell c2 = new Cell();
			c2.setWidth("2%");
			c2.appendChild(exportador);
			box.appendChild(c2);
			Cell c3 = new Cell();
			c3.setWidth("2%");
			c3.appendChild(pagineo);
			box.appendChild(c3);
			box.setStyle("background:white");
			box.setWidth("100%");
			box.setAlign("end");
			box.setHeight("10px");
			return box;
		}
	}

	protected XulElement encabezado() {
		return null;
	}

	private Listbox crearListbox(List<Clase> lista, String[] campos,
			boolean emergente) {
		Listbox listboxCatalogo = new Listbox();
		listboxCatalogo.setMold("paging");
		listboxCatalogo.setPagingPosition("top");
		listboxCatalogo.setPageSize(10);
		Auxhead cabecera = new Auxhead();
		cabecera.setVisible(true);
		Listhead lhdEncabezado = new Listhead();
		lhdEncabezado.setSizable(true);
		lhdEncabezado.setVisible(true);
		for (int i = 0; i < campos.length; i++) {
			Textbox cajaTexto = crearTexboxBusqueda(campos[i], emergente);
			Auxheader cabeceraFila = new Auxheader();
			cabeceraFila.appendChild(cajaTexto);
			cabecera.appendChild(cabeceraFila);
			Listheader listheader = new Listheader(campos[i]);
			lhdEncabezado.appendChild(listheader);
		}
		listboxCatalogo.appendChild(cabecera);
		listboxCatalogo.appendChild(lhdEncabezado);
		listboxCatalogo.setSizedByContent(true);
		listboxCatalogo.setSpan("true");
		listboxCatalogo.setModel(new ListModelList<Clase>(lista));
		listboxCatalogo.setItemRenderer(new ListitemRenderer<Clase>() {

			@Override
			public void render(Listitem fila, Clase objeto, int arg2)
					throws Exception {
				fila.setValue(objeto);
				String[] registros = crearRegistros(objeto);
				for (int i = 0; i < registros.length; i++) {
					Listcell celda = new Listcell(registros[i]);
					celda.setParent(fila);
				}
			}
		});
		listboxCatalogo.setMultiple(emergente);
		listboxCatalogo.setCheckmark(emergente);
		listboxCatalogo.setMultiple(!emergente);
		listboxCatalogo.setCheckmark(!emergente);
		return listboxCatalogo;
	}

	private Textbox crearTexboxBusqueda(String valor, final boolean emergente) {
		Textbox cajaTexto = new Textbox();
		cajaTexto.setContext(valor);
		cajaTexto.setHflex("1");
		cajaTexto.setWidth("auto");
		cajaTexto.addEventListener(Events.ON_OK, new EventListener<KeyEvent>() {
			@Override
			public void onEvent(KeyEvent e) throws Exception {
				List<String> valores = new ArrayList<String>();
				Textbox cajaTexto = (Textbox) e.getTarget();
				Auxhead cabecera = (Auxhead) cajaTexto.getParent().getParent();
				Listbox listbox = (Listbox) cabecera.getParent();
				for (Component component : cabecera.getChildren()) {
					Textbox texbox = (Textbox) component.getChildren().get(0);
					valores.add(texbox.getValue().toLowerCase());
				}
				List<Clase> listaNueva = buscar(valores);
				listbox.setModel(new ListModelList<Clase>(listaNueva));
				String valor = cajaTexto.getValue();
				cajaTexto.setValue(valor);
				// Agregado para que las listas sean multiples en caso necesario
				lsbCatalogo.setMultiple(emergente);
				lsbCatalogo.setCheckmark(emergente);
				lsbCatalogo.setMultiple(!emergente);
				lsbCatalogo.setCheckmark(!emergente);
			}
		});
		cajaTexto.setPlaceholder("Buscar....");
		cajaTexto.setTooltiptext("Presione Enter para Filtrar la Informacion");
		return cajaTexto;
	}

	private Button crearBotonPagineo() {
		Button boton = new Button();
		boton.setTooltiptext("Presione para mostrar todos los registros en una sola lista, sin pagineo");
		boton.setSclass("catalogo");
		boton.setImage("/public/imagenes/botones/pagineo.png");
		boton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				pagineo();
			}
		});
		return boton;
	}

	private Button crearBotonExportador() {
		Button boton = new Button();
		boton.setTooltiptext("Exportar los Datos como un Archivo");
		boton.setSclass("catalogo");
		boton.setImage("/public/imagenes/botones/exportar.png");
		boton.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				exportar();
			}
		});
		return boton;
	}

	protected void pagineo() {
		if (lsbCatalogo.getPagingPosition().equals("top")) {
			lsbCatalogo.setMold("default");
			lsbCatalogo.setPagingPosition("both");
			pagineo.setTooltiptext("Presione para mostrar la lista con pagineo");
		} else {
			lsbCatalogo.setMold("paging");
			lsbCatalogo.setPagingPosition("top");
			lsbCatalogo.setPageSize(10);
			pagineo.setTooltiptext("Presione para mostrar todos los registros en una sola lista, sin pagineo");
		}
	}

	protected void exportar() {
		if (lsbCatalogo.getItemCount() != 0) {
			String s = ";";
			final StringBuffer sb = new StringBuffer();

			for (Object head : lsbCatalogo.getHeads()) {
				String h = "";
				if (head instanceof Listhead) {
					for (Object header : ((Listhead) head).getChildren()) {
						h += ((Listheader) header).getLabel() + s;
					}
					sb.append(h + "\n");
				}
			}
			for (Object item : lsbCatalogo.getItems()) {
				String i = "";
				for (Object cell : ((Listitem) item).getChildren()) {
					i += ((Listcell) cell).getLabel() + s;
				}
				sb.append(i + "\n");
			}
			Messagebox.show(SecurityMensaje.exportar, "Alerta", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION,
					new org.zkoss.zk.ui.event.EventListener<Event>() {
						public void onEvent(Event evt)
								throws InterruptedException {
							if (evt.getName().equals("onOK")) {
								Filedownload.save(sb.toString().getBytes(),
										"text/plain", "datos.csv");
							}
						}
					});
		} else
			SecurityMensaje.mensajeAlerta(SecurityMensaje.noHayRegistros);
	}

	/**
	 * Metodo que permite llamar un servicio dependiendo el controlador que
	 * busque, es decir que haga un filtro dentro del catalogo, ayudando asi al
	 * usuario a encontrar el registro buscado con mayor facilidad
	 */
	protected abstract List<Clase> buscar(List<String> valores);

	/**
	 * Metodo que permite por cada controlador indicar cuales son los registros
	 * que quiere mostrar en su catalogo, formando una matriz de String
	 */
	protected abstract String[] crearRegistros(Clase objeto);

	public Clase objetoSeleccionadoDelCatalogo() {
		return lsbCatalogo.getSelectedItem().getValue();
	}

	public Listbox getListbox() {
		return lsbCatalogo;
	}

	public void actualizarLista(List<Clase> lista) {
		lsbCatalogo.setModel(new ListModelList<Clase>(lista));
		lsbCatalogo.setMultiple(false);
		lsbCatalogo.setCheckmark(false);
		lsbCatalogo.setMultiple(true);
		lsbCatalogo.setCheckmark(true);
	}

	public List<Clase> obtenerSeleccionados() {
		List<Clase> valores = new ArrayList<Clase>();
		boolean entro = false;
		if (lsbCatalogo.getItemCount() != 0) {
			final List<Listitem> list1 = lsbCatalogo.getItems();
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i).isSelected()) {
					Clase clase = list1.get(i).getValue();
					entro = true;
					valores.add(clase);
				}
			}
			if (!entro) {
				valores.clear();
				return valores;
			}
			return valores;
		} else
			return null;
	}

	/**
	 * Metodo que permite limpiar los items seleccionados en el catalogo
	 */
	public void limpiarSeleccion() {

		lsbCatalogo.clearSelection();

	}

}