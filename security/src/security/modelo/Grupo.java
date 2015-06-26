package security.modelo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the grupo database table.
 * 
 */
@Entity
@Table(name = "grupo")
public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_grupo", unique = true, nullable = false)
	private long idGrupo;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean estado;

	@Column(length = 500)
	private String nombre;

	// bi-directional many-to-many association to Arbol
	@ManyToMany
	@JoinTable(name = "arbol_grupo", joinColumns = { @JoinColumn(name = "id_grupo", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "id_arbol", nullable = false) })
	private Set<Arbol> arbols;

	// bi-directional many-to-many association to Usuario
	@ManyToMany(mappedBy = "grupos")
	private Set<UsuarioSeguridad> usuarios;

	public Grupo() {
	}

	public Grupo(long idGrupo, boolean estado, String nombre, Set<Arbol> arbols) {
		super();
		this.idGrupo = idGrupo;
		this.estado = estado;
		this.nombre = nombre;
		this.arbols = arbols;
	}

	public long getIdGrupo() {
		return this.idGrupo;
	}

	public void setIdGrupo(long idGrupo) {
		this.idGrupo = idGrupo;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Arbol> getArbols() {
		return this.arbols;
	}

	public void setArbols(Set<Arbol> arbols) {
		this.arbols = arbols;
	}

	public Set<UsuarioSeguridad> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Set<UsuarioSeguridad> usuarios) {
		this.usuarios = usuarios;
	}

}