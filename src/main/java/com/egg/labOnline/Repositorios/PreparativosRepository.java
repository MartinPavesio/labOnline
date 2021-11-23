
package com.egg.labOnline.Repositorios;

import com.egg.labOnline.Entidades.Preparativos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PreparativosRepository extends JpaRepository<Preparativos, String> {
    
    
}
