package security.componente;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;

public abstract class SecurityBotonera extends Hbox {

	private static final long serialVersionUID = 4170810016465632334L;

	public SecurityBotonera() {
		super();
		Button btnSeleccionar = new Button();
		Button btnBuscar = new Button();
		Button btnAnnadir = new Button();
		Button btnGuardar = new Button();
		Button btnEliminar = new Button();
		Button btnLimpiar = new Button();
		Button btnReporte = new Button();
		Button btnSalir = new Button();
		Button btnAyuda = new Button();

		this.appendChild(btnSeleccionar);
		this.appendChild(btnBuscar);
		this.appendChild(btnAnnadir);
		this.appendChild(btnGuardar);
		this.appendChild(btnEliminar);
		this.appendChild(btnLimpiar);
		this.appendChild(btnReporte);
		this.appendChild(btnSalir);
		this.appendChild(btnAyuda);

		btnSeleccionar.setImage("/public/imagenes/botones/seleccionar.png");
		btnBuscar.setImage("/public/imagenes/botones/volver.png");
		btnAnnadir.setImage("/public/imagenes/botones/annadir.png");
		btnGuardar.setImage("/public/imagenes/botones/guardar.png");
		btnEliminar.setImage("/public/imagenes/botones/eliminar.png");
		btnLimpiar.setImage("/public/imagenes/botones/limpiar.png");
		btnReporte.setImage("/public/imagenes/botones/reporte.png");
		btnSalir.setImage("/public/imagenes/botones/salir.png");
		btnAyuda.setImage("/public/imagenes/botones/ayuda.png");
		
		
		btnAyuda.setSclass("btn");
		btnEliminar.setSclass("btn");
		btnAnnadir.setSclass("btn");
		btnGuardar.setSclass("btn");
		btnBuscar.setSclass("btn");
		btnLimpiar.setSclass("btn");
		btnSalir.setSclass("btn");
		btnReporte.setSclass("btn");
		btnSeleccionar.setSclass("btn");
		btnSeleccionar.setWidth("115px");
		
		btnSeleccionar.setLabel("Seleccionar");
		btnBuscar.setLabel("Volver");
		btnAnnadir.setLabel("AÒadir");
		btnGuardar.setLabel("Guardar");
		btnEliminar.setLabel("Eliminar");
		btnLimpiar.setLabel("Limpiar");
		btnReporte.setLabel("Reporte");
		btnSalir.setLabel("Salir");
		btnAyuda.setLabel("Ayuda");

		btnSeleccionar.setTooltiptext("Seleccionar");
		btnBuscar.setTooltiptext("Volver");
		btnAnnadir.setTooltiptext("AÒadir");
		btnGuardar.setTooltiptext("Guardar");
		btnEliminar.setTooltiptext("Eliminar");
		btnLimpiar.setTooltiptext("Limpiar");
		btnReporte.setTooltiptext("Reporte");
		btnSalir.setTooltiptext("Salir");
		btnAyuda.setTooltiptext("Ayuda");


		btnSeleccionar.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						seleccionar();
					}
				});
		btnBuscar.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				buscar();
			}

		});
		btnAnnadir.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						annadir();
					}

				});
		btnGuardar.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						guardar();
					}

				});

		btnEliminar.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						eliminar();
					}
				});
		btnLimpiar.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						limpiar();
					}
				});
		btnReporte.addEventListener(Events.ON_CLICK,
				new EventListener<Event>() {
					@Override
					public void onEvent(Event arg0) throws Exception {
						reporte();
					}
				});
		btnSalir.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				salir();
			}
		});
		btnAyuda.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				ayuda();
			}
		});
		btnBuscar.setVisible(false);
	}

	/**a
	 * Metodo que guarda un registro nuevo si no a sido guardado con
	 * anterioridad, de ser as√≠ se modifica aquellos datos que el usuario
	 * cambie
	 */
	public abstract void seleccionar();

	public abstract void buscar();

	public abstract void annadir();

	public abstract void guardar();

	/**
	 * Metodo que limpia todos los campos para darle la libertad al usuario de
	 * a√±adir un nuevo registro o seleccionar uno ya existente
	 */
	public abstract void limpiar();

	public abstract void reporte();

	/**
	 * Metodo que permite cerrar el div que se genera al entrar a la vista
	 */
	public abstract void salir();

	/**
	 * Metodo que permite la eliminacion de un registro siempre y cuando no este
	 * asociado a otra entidad
	 */
	public abstract void eliminar();

	public abstract void ayuda();
}