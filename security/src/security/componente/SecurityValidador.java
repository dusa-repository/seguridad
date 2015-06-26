package security.componente;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.util.media.Media;

public abstract class SecurityValidador {

	/* Valida que lo digitos ingresados sean números */
	public static boolean validarNumero(String numero) {
		String PATTERN = "^[0-9]*";
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(numero);
		return matcher.matches();
	}

	/*
	 * Valida que el correo electronico que se ingresa sea del formato
	 * xxxxx@XXXXX.com
	 */
	public static boolean validarCorreo(String correo) {
		String PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(correo);
		return matcher.matches();
	}

	/* Valida que el telefono que se ingresa sea del formato ####-####### */
	public static boolean validarTelefono(String telefono) {
		String PATTERN = "\\d{4}-\\d{7}";
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(telefono);
		return matcher.matches();
	}

	/* Valida que el archivo que se sube sea una Imagen */
	public static boolean validarTipoImagen(Media media) {
		String tipoImagen = media.getFormat();
		if (tipoImagen.equals("gif") || tipoImagen.equals("jpeg")
				|| tipoImagen.equals("png"))
			return true;
		else
			return false;
	}

	/*
	 * Valida que el archivo que se sube sea un documento de texto .doc .odt o
	 * .pdf
	 */
	public static boolean validarTipoDocumento(Media media) {
		String tipoImagen = media.getFormat();
		if (tipoImagen.equals("pdf") || tipoImagen.equals("odt")
				|| tipoImagen.equals("doc") || tipoImagen.equals("docx"))
			return true;
		else
			return false;
	}

	/*
	 * Valida que el archivo que se sube sea un documento de solo lectura .pdf
	 */
	public static boolean validarTipoDocumentoSoloLectura(Media media) {
		String tipoImagen = media.getFormat();
		if (tipoImagen.equals("pdf"))
			return true;
		else
			return false;
	}

	/* Valida que el archivo que se sube no exceda el tamanno de 1Mb */
	public static boolean validarTamannoImagen(Media media) {
		long tamannoImagen = media.getByteData().length;
		if (tamannoImagen <= 1048576)
			return true;
		else
			return false;
	}

	/* Valida que el archivo que se sube no exceda el tamanno de 3Mb */
	public static boolean validarTamannoDocumento(Media media) {
		long tamannoImagen = media.getByteData().length;
		if (tamannoImagen <= 3145728)
			return true;
		else
			return false;
	}
	

	public static boolean validarExcel(Media media) {
		String extencion = media.getFormat();
		if (extencion.equals("xlsx"))
			return true;
		else
			return false;
	}
}