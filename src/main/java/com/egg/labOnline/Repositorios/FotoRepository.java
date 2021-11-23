
package com.egg.labOnline.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.egg.labOnline.Entidades.Foto;

@Repository
public interface FotoRepository extends JpaRepository<Foto, String> {

}
