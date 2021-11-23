package com.egg.labOnline.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.OrdenMedica;
import com.egg.labOnline.Entidades.Usuario;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository

public interface OrdenMedicaRepository extends JpaRepository<OrdenMedica, String> {

    @Query("SELECT om FROM OrdenMedica om WHERE om.usuario LIKE :usuario")

    public List<OrdenMedica> SearchByUser(@Param("usuario") Usuario usuario);

    @Query(value = "SELECT om FROM OrdenMedica om JOIN obraSocial_OrdenMedica oo JOIN obraSocial os"
            + "WHERE os.nombre LIKE: nombre AND om.id LIKE: oo.OrdenMedica_id ORDER BY oo.OrdenMedica_nombre"
            + "os.id LIKE oo.obraSocial_id", nativeQuery = true)
    public List<OrdenMedica> SearchByOs(@Param("ObraSocial") ObraSocial ObraSocial);

    @Query(value = "SELECT om FROM OrdenMedica om WHERE om. fechaOrden LIKE : fechaOrden", nativeQuery = true)
    public List<OrdenMedica> SearchByDate(@Param(" fechaOrden") Date fechaOrden);

    @Query(value = "SELECT om FROM OrdenMedica om  ORDER BY om.fechaOrden ASC", nativeQuery = true)
    public List<OrdenMedica> OrderByDateAsc(@Param(" fechaOrden") Date fechaOrden);

    @Query(value = "SELECT om FROM OrdenMedica om  ORDER BY om.fechaOrden DESC", nativeQuery = true)
    public List<OrdenMedica> OrderByDateDesc(@Param(" fechaOrden") Date fechaOrden);
    
}
