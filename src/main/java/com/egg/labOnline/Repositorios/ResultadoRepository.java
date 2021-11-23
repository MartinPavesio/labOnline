
package com.egg.labOnline.Repositorios;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.labOnline.Entidades.Resultado;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, String> {

	@Query(value = "SELECT r FROM Resultado r JOIN Usuario u WHERE r.usuario_id LIKE u.id"
			+ "	AND u.dni LIKE :dni", nativeQuery = true)
	public List<Resultado> findByUsuarioDni(@Param("dni") Integer dni);

	@Query(value = "SELECT r FROM Resultado r JOIN Analisis a WHERE r.usuario_id LIKE a.id"
			+ "	AND a.id LIKE :id", nativeQuery = true)
	public List<Resultado> findByAnalisisId(@Param("id") String id);

	@Query("SELECT r FROM Resultado r WHERE r.fecha LIKE :date")
	public List<Resultado> findByDate(@Param("date") Date date);
}
