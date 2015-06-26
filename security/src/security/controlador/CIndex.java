package security.controlador;

import java.io.IOException;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zul.Window;

public class CIndex extends CSecurity {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void inicializar() throws IOException {
	}

	@Listen("onClick = #lblOlvidoClave")
	public void abrirVentana() {
		Window window = (Window) Executions.createComponents(

		"/vistas/seguridad/VReinicioPassword.zul", null, null);

		window.doModal();
	}
}
