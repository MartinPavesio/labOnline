
package com.egg.labOnline.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.labOnline.Entidades.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	@Query("SELECT u FROM Usuario u WHERE u.nombre = :nombre")
	public List<Usuario> buscarNombre(@Param("nombre") String nombre);

	@Query("SELECT u FROM Usuario u WHERE u.dni LIKE :dni")
	public List<Usuario> buscarDni(@Param("dni") Integer dni);

	@Query(value = "SELECT u FROM Usuario u JOIN Usuario_obra_social us JOIN Obra_social os WHERE os.nombre LIKE :nombre AND "
			+ "us.obra_social_id LIKE os.id AND u.id LIKE us.usuario_id", nativeQuery = true)
	public List<Usuario> buscarObraSocial(@Param("nombre") String nombre);

}
