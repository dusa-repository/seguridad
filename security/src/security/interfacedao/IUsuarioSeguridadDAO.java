package security.interfacedao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import security.modelo.Grupo;
import security.modelo.UsuarioSeguridad;

public interface IUsuarioSeguridadDAO extends JpaRepository<UsuarioSeguridad, String> {

	UsuarioSeguridad findByLogin(String nombre);

	List<UsuarioSeguridad> findByGrupos(Grupo grupo);

	UsuarioSeguridad findByLoginAndEmail(String value, String value2);

}