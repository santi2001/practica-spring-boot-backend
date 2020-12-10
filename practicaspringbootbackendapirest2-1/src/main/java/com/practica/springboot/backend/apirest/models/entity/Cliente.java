package com.practica.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name = "Clientes")
public class Cliente implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotEmpty(message = "no puede ser vacio")
	@Size(min = 3, max = 12, message = "debe tener un tamaño entre 4 y 12 caracteres ")
	@Column(nullable = false)
	private String nombre;
	@NotEmpty(message = "no puede ser vacio")
	private String apellido;
	@Email(message = "el email ingresado no tiene un formato adecuado")
	@NotEmpty
	@Column(nullable = false, unique = true)
	private String email;
	@NotNull(message = "no puede ser vacio")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	private String foto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id") //puede ser opcional
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	//@NotNull(message = "debe seleccionarse una region")
	private Region region;
	@JsonIgnoreProperties( value = {"cliente","hibernateLazyInitializer", "handler"}, allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente",cascade = CascadeType.ALL)
	private List<Factura> listaFacturas;
	
	public Cliente() {
		this.listaFacturas= new ArrayList<>();
	}

	 @PrePersist
	public void cargar() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public List<Factura> getListaFacturas() {
		return listaFacturas;
	}

	public void setListaFacturas(List<Factura> listaFacturas) {
		this.listaFacturas = listaFacturas;
	}

}
