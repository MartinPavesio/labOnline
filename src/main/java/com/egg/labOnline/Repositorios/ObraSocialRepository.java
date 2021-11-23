
package com.egg.labOnline.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.labOnline.Entidades.ObraSocial;

@Repository
public interface ObraSocialRepository extends JpaRepository<ObraSocial, String> {

	@Query("SELECT os FROM ObraSocial os WHERE os.nombre LIKE :nombre")
	public List<ObraSocial> buscarOSNombre(@Param("nombre") String nombre);

	@Query("SELECT os FROM ObraSocial os WHERE os.codigo = :codigo")
	public List<ObraSocial> buscarOSCodigo(@Param("codigo") Integer codigo);

	@Query("SELECT os FROM ObraSocial os WHERE os.telefono = :telefono")
	public List<ObraSocial> buscarOSTelefono(@Param("telefono") Integer telefono);

	@Query("SELECT os FROM ObraSocial os WHERE os.direccion LIKE :direccion")
	public List<ObraSocial> buscarOSDireccion(@Param("direccion") String direccion);

	@Query("SELECT os FROM ObraSocial os WHERE os.arancel = :arancel")
	public List<ObraSocial> buscarOSArancel(@Param("arancel") Double arancel);

	@Query("SELECT os FROM ObraSocial os WHERE os.mail LIKE :mail")
	public List<ObraSocial> buscarOSMail(@Param("mail") String mail);

	@Query(value = "SELECT os FROM ObraSocial os JOIN Usuario_obra_social uos JOIN Usuario u WHERE os.id LIKE uos.obra_social_id AND "
			+ "uos.usuario_id LIKE u.id AND WHERE u.dni LIKE :dni", nativeQuery = true)
	public List<ObraSocial> buscarOSUsuarioDni(@Param("dni") Integer dni);

}
