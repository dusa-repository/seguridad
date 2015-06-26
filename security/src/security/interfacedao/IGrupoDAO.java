package security.interfacedao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import security.modelo.Arbol;
import security.modelo.Grupo;
import security.modelo.UsuarioSeguridad;

public interface IGrupoDAO extends JpaRepository<Grupo, Long> {
	
	public List<Grupo> findByUsuarios(UsuarioSeguridad usuario);

	public List<Grupo> findByEstadoTrue();

	public List<Grupo> findByIdGrupoNotInAndEstadoTrue(List<Long> ids);

	public List<Grupo> findByNombre(String nombreGrupo);

	public List<Grupo> findByNombreStartingWithAllIgnoreCase(String valor);

	@Query("Select g from Grupo g order by g.idGrupo asc")
	public List<Grupo> findAllOrderById();

	public List<Grupo> findByUsuariosOrderByNombreAsc(UsuarioSeguridad u);

	public List<Grupo> findByArbols(Set<Arbol> arbols);
}
