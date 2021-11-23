package com.egg.labOnline.Servicios;

import com.egg.labOnline.Entidades.Foto;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.FotoRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {

	@Autowired
	private FotoRepository fotoRepository;

	@Transactional
	public Foto crear(MultipartFile archivo) throws ErrorServicio {

		if (archivo != null) {
			try {
				Foto imagen = new Foto();
				imagen.setMime(archivo.getContentType());
				imagen.setNombre(archivo.getName());
				imagen.setContenido(archivo.getBytes());

				return fotoRepository.save(imagen);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else {
			throw new ErrorServicio("El archivo correspondiente a la foto no puede estar vacio");
		}

		return null;

	}

	@Transactional
	public Foto editar(String id, MultipartFile archivo) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id de la imagen a editar no puede estar vacio");
		}

		if (archivo != null) {
			try {
				Optional<Foto> rta = fotoRepository.findById(id);
				if (rta.isPresent()) {

					Foto imagen = rta.get();
					imagen.setMime(archivo.getContentType());
					imagen.setNombre(archivo.getName());
					imagen.setContenido(archivo.getBytes());

					return fotoRepository.save(imagen);
				} else {
					throw new ErrorServicio("No se creo una foto con ese id");
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else {
			throw new ErrorServicio("El archivo correspondiente a la foto no puede estar vacio");
		}

		return null;

	}

}
