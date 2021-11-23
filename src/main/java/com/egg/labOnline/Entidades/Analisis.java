package com.egg.labOnline.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Analisis {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String nombre;
	private Integer unidadBioquimica;

	@OneToOne
	private ObraSocial obraSocial;

	@OneToOne
	private Preparativos preparativos;

	public Analisis() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getUnidadBioquimica() {
		return unidadBioquimica;
	}

	public void setUnidadBioquimica(Integer unidadBioquimica) {
		this.unidadBioquimica = unidadBioquimica;
	}

	public ObraSocial getObraSocial() {
		return obraSocial;
	}

	public void setObraSocial(ObraSocial obraSocial) {
		this.obraSocial = obraSocial;
	}

	public Preparativos getPreparativos() {
		return preparativos;
	}

	public void setPreparativos(Preparativos preparativos) {
		this.preparativos = preparativos;
	}

}
