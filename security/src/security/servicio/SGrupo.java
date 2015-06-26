package security.servicio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import security.interfacedao.IGrupoDAO;
import security.modelo.Arbol;
import security.modelo.Grupo;
import security.modelo.UsuarioSeguridad;

@Service("SGrupo")
public class SGrupo {

	@Autowired
	private IGrupoDAO grupoDAO;

	public void guardarGrupo(Grupo grupo) {
		grupoDAO.save(grupo);
	}

	public List<Grupo> buscarTodos() {
		return grupoDAO.findByEstadoTrue();
	}

	public Grupo buscarGrupo(long id) {
		return grupoDAO.findOne(id);
	}

	public List<Grupo> buscarGruposDelUsuario(UsuarioSeguridad usuario) {
		return grupoDAO.findByUsuarios(usuario);
	}

	public List<Grupo> buscarGruposDisponibles(List<Long> ids) {
		return grupoDAO.findByIdGrupoNotInAndEstadoTrue(ids);
	}

	public List<Grupo> buscarPorNombre(String nombreGrupo) {
		return grupoDAO.findByNombre(nombreGrupo);
	}

	public List<Grupo> filtroNombre(String valor) {
		return grupoDAO.findByNombreStartingWithAllIgnoreCase(valor);
	}

	public void eliminarUno(long id) {
		grupoDAO.delete(id);
	}

	public List<Grupo> buscarTodosOrdenados() {
		return grupoDAO.findAllOrderById();
	}

	public void eliminarVarios(List<Grupo> eliminarLista) {
		grupoDAO.delete(eliminarLista);

	}

	public List<Grupo> buscarGruposUsuario(UsuarioSeguridad u) {
		return grupoDAO.findByUsuariosOrderByNombreAsc(u);
	}

	public List<Grupo> buscarArboles(List<Arbol> eliminarLista) {
		Set<Arbol> arbols = new HashSet<Arbol>(eliminarLista);
		return grupoDAO.findByArbols(arbols);
	}

	public void guardarVarios(List<Grupo> eliminarLista) {
		grupoDAO.save(eliminarLista);
	}

}