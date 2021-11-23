
package com.egg.labOnline.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.labOnline.Entidades.Analisis;

@Repository
public interface AnalisisRepository extends JpaRepository<Analisis, String> {

	@Query("SELECT a FROM Analisis a WHERE a.nombre LIKE :nombre")
	public List<Analisis> searchByName(@Param("nombre") String nombre);

	@Query("SELECT a FROM Analisis a WHERE a.unidadBioquimica LIKE :ub")
	public List<Analisis> searchByUd(@Param("ub") Integer ub);

	@Query("SELECT a FROM Analisis a JOIN ObraSocial os WHERE a.obraSocial.id LIKE os.id AND os.nombre LIKE :nombre")
	public List<Analisis> searchByObraSocial(@Param("nombre") String nombre);

}
