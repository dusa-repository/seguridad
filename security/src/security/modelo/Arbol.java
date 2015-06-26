package security.modelo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


/**
 * The persistent class for the arbol database table.
 * 
 */
@Entity
@Table(name="arbol")
public class Arbol implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_arbol", unique=true, nullable=false)
	private long idArbol;

	@Column(length=500)
	private String nombre;

	private long padre;

	@Column(length=500)
	private String url;

	@Column
	private Integer orden;

	@Column(name = "manejo_version")
	private Integer manejo;

	@Column(name = "menu")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean menu;

	//bi-directional many-to-many association to Grupo
	@ManyToMany(mappedBy="arbols")
	private Set<Grupo> gruposArbol;

	public Arbol() {
	}
	
	public Arbol(long idArbol, String nombre, long padre, String url,
			Set<Grupo> grupos,  Integer orden, Integer manejo, Boolean menu) {
		super();
		this.idArbol = idArbol;
		this.nombre = nombre;
		this.padre = padre;
		this.url = url;
		this.gruposArbol = grupos;
		this.orden = orden;
		this.manejo = manejo;
		this.menu = menu;
	}

	public long getIdArbol() {
		return this.idArbol;
	}

	public void setIdArbol(long idArbol) {
		this.idArbol = idArbol;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getPadre() {
		return this.padre;
	}

	public void setPadre(long padre) {
		this.padre = padre;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<Grupo> getGrupos() {
		return this.gruposArbol;
	}

	public void setGrupos(Set<Grupo> grupos) {
		this.gruposArbol = grupos;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getManejo() {
		return manejo;
	}

	public void setManejo(Integer manejo) {
		this.manejo = manejo;
	}

	public Set<Grupo> getGruposArbol() {
		return gruposArbol;
	}

	public void setGruposArbol(Set<Grupo> gruposArbol) {
		this.gruposArbol = gruposArbol;
	}

	public Boolean getMenu() {
		return menu;
	}

	public void setMenu(Boolean menu) {
		this.menu = menu;
	}

}