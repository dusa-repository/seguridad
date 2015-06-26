package security.componente;

import org.zkoss.zul.Messagebox;

public class SecurityMensaje {
	public static String CodigoUsado = "El Codigo del Tipo Cliente esta siendo Usado por otro Registro";
	public static String almacenNoExiste = "El Codigo del Almacen no Existe.";
	public static String contrasennasInvalidas = "Las contraseñas no coinciden.";
	public static String camposVaciosItem = "Debe Llenar Todos lo Campos Requeridos, de la seccion de Articulo (**).";
	public static String noPoseeExistencia = "No posee existencia suficiente para realizar la transaccion";
	public static String algunosEliminados = "Algunos registros no pudieron ser eliminados ya que se estan utilizando";
	public static String registroUtilizado = "El registro no se puede eliminar ya que esta siendo utilizado";
	public static String errorEnReporte = "Ha ocurrido un error originando el reporte";
	public static String claveSYNoEsta = "El Codigo de Producto no Existe.";
	public static String claveRTNoEsta = "El Codigo Definido por el Usuario no Existe.";
	public static String guardado = "Registro Guardado Exitosamente.";
	public static String claveUsada = "La Clave ha sido Usada por Otro Registro.";
	public static String camposVacios = "Debe Llenar Todos los Campos Requeridos.";
	public static String noSeleccionoItem = "No ha seleccionado ningun Item";
	public static String noHayRegistros = "No se Encontraron Registros";
	public static String editarSoloUno = "Solo puede Editar un Item a la vez, "
			+ "Seleccione un (1) solo Item y Repita la Operacion";
	public static String deseaEliminar = "¿Desea Eliminar el Registro?";
	public static String eliminado = "Registro Eliminado Exitosamente";
	public static String estaEditando = "No ha culminado la Edicion, ¿Desea Continuar Editando?";
	public static String noSeleccionoRegistro = "No ha seleccionado ningun Registro";
	public static String exportar = "¿Desea exportar los datos de la lista a formato CSV?";
	public static String enUso = "La interfaz esta siendo usada";
	public static String correoInvalido = "Formato de Correo No Valido";
	public static String usuarioNoRegistrado = "El Usuario no Esta Registrado.";
	public static String correoNoConcuerda = "El Correo no Concuerda con los Datos del Usuario.";
	public static String contrasennasNoCoinciden = "Las Contraseñas no Coinciden.";
	public static String eliminacionFallida = "No puede eliminar este grupo";
	public static String noPermitido = "El tipo de archivo que ha seleccionado no esta permitido, solo archivos con extension .jpeg y .png son permitidos";
	public static String tamanioMuyGrande = "El archivo que ha seleccionado excede el tamaño maximo establecido (100 KB)";
	public static String noEliminar = "El Registro no se puede Eliminar, Esta siendo Usado";
	public static String telefonoInvalido = "Formato de Telefono No Valido";
	public static String camposPresentaciones = "Debe Llenar Todos los Campos de la Lista de Presentaciones";
	public static String cedulaInvalida = "Formato de Cedula No Valido";
	public static String llenarListas = "Debe Llenar Todos los Campos de la Listas";
	public static String formatoImagenNoValido = "Formato de Imagen no Valido";
	public static String seleccioneFuncionalidades = "Seleccione las Funcionalidades";
	public static String cedulaNoExiste = "El Numero de Cedula que Ingreso No esta asociado a Ningun Usuario";
	public static String reinicioContrasenna = "Se envio un Correo Indicando los datos del Usuario";
	public static String loginUsado = "El Login no esta Disponible, esta siendo usado por otro Usuario";
	public static String seleccionarProveedor = "Debe seleccionar un proveedor previamente para mostrar las ordenes respectivas de compra";
	public static String itemRepetido = "Ya posee un Costo con ese codigo en la lista, modifique o elimine el existente";
	public static String seleccionarMarcaYVendedor = "Debe Seleccionar una Marca Y un Vendedor";
	public static String seleccionarMarca = "Debe Seleccionar una Marca";
	public static String seleccionarVendedor = "Debe Seleccionar un Vendedor";
	public static String limpiado = "Cupos Limpiados Exitosamente";
	public static String consumidoMayor = "La cantidad total debe ser mayor a la cantidad consumida";
	public static String fechasErroneas = "La fecha de Vigencia desde debe ser menor a la fecha de vigencia hasta";
	public static String archivoExcel = "Los Archivos deben ser de Tipo .xlsx";
	public static String usernameUsado = "El Username esta siendo Usado por Otro Registro.";
	public static String seleccionarStatus = "Debe Seleccionar un Status";
	public static String fechaPosterior = "La Fecha de Inicio no puede ser posterior a la Fecha Fin";
	public static String movil = "Especificar que el Usuario sera Movil, hara que el mismo no pueda Ingresar a este Sistema Administrativo";
	public static String seleccionarItem = "Debe Seleccionar un Item";
	public static String seleccionarEstado = "Debe Seleccionar un Estado";

	public static void mensajeInformacion(String msj) {
		Messagebox.show(msj, "Informacion", Messagebox.OK,
				Messagebox.INFORMATION);
	}

	public static void mensajeAlerta(String msj) {
		Messagebox.show(msj, "Alerta", Messagebox.OK, Messagebox.EXCLAMATION);
	}

	public static void mensajeError(String msj) {
		Messagebox.show(msj, "Error", Messagebox.OK, Messagebox.ERROR);
	}
}