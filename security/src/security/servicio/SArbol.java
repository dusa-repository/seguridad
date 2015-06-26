package security.servicio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import security.interfacedao.IArbolDAO;
import security.modelo.Arbol;
import security.modelo.Grupo;

@Service("SArbol")
public class SArbol {

	@Autowired
	private IArbolDAO arbolDAO;

	public void guardar(Arbol arbol) {
		arbolDAO.save(arbol);
	}

	public Arbol buscar(long id) {

		return arbolDAO.findOne(id);
	}

	public List<Arbol> listarArbol() {
		return arbolDAO.buscarTodos();
	}

	public List<Arbol> buscarOrdenados() {
		List<String> ordenar = new ArrayList<String>();
		ordenar.add("orden");
		Sort o = new Sort(Sort.Direction.ASC, ordenar);
		return arbolDAO.findAll(o);
	}

	 public List<Arbol> buscarPorNombreArbol(String nombre) {
		 List<Arbol> arbol = new ArrayList<Arbol>();
	 arbol = arbolDAO.findByNombre(nombre);
	 return arbol;
	 }

	public List<Arbol> ordenarPorID(ArrayList<Long> ids) {

		List<Arbol> arboles;
		arboles = arbolDAO.buscar(ids);
		return arboles;

	}

	public Arbol buscarPorId(Long id) {
		Arbol arbol = arbolDAO.findByIdArbol(id);
		return arbol;
	}

	public List<Arbol> buscarporGrupo(Grupo grupo) {
		List<Arbol> arboles;
		arboles = arbolDAO.findByGruposArbol(grupo);
		return arboles;
	}

	public void eliminarUno(long clave) {
		arbolDAO.delete(clave);

	}

	public void eliminarVarios(List<Arbol> eliminarLista) {
		arbolDAO.delete(eliminarLista);

	}

	public List<Arbol> ordenarPorOrden(ArrayList<Long> ids) {
		return arbolDAO.findByIdArbolInOrderByOrdenAsc(ids);
	}

}
