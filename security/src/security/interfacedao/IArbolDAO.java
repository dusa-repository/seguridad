package security.interfacedao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import security.modelo.Arbol;
import security.modelo.Grupo;

public interface IArbolDAO extends JpaRepository<Arbol, Long> {
	
	 public List<Arbol> findByNombre(String nombre);

	 @Query("select a from Arbol a order by a.idArbol asc")
	public List<Arbol> buscarTodos();
	
	 @Query("select a from Arbol a where a.idArbol = ?1 order by a.idArbol")
	public List<Arbol> buscar(ArrayList<Long> ids);

	public List<Arbol> findByGruposArbol(Grupo grupo);

	public Arbol findByIdArbol(Long id);

	public List<Arbol> findByIdArbolInOrderByOrdenAsc(ArrayList<Long> ids);

		
	
}
