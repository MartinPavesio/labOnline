package com.egg.labOnline.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.labOnline.Entidades.Laboratorio;




@Repository
public interface LaboratorioRepository extends JpaRepository<Laboratorio,String>{
	@Query("SELECT l FROM Laboratorio l WHERE l.id LIKe :id")
	public List<Laboratorio> buscarLaboratorioporID(@Param("id") String id);
	@Query(value = "SELECT p from Laboratorio p JOIN laboratorio_obra_social m JOIN obraSocial o"
			+ " WHERE p.id LIKE m.laboratorio_id AND m.obrasocial_id LIKE o.id"
			+ " AND o.nombre LIKE :nombre", nativeQuery = true)
	public List<Laboratorio> buscarporOS(@Param("nombre") String nombre);

}