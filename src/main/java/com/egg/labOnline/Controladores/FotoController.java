package com.egg.labOnline.Controladores;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.egg.labOnline.Entidades.OrdenMedica;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.FotoRepository;
import com.egg.labOnline.Servicios.OrdenMedicaService;
import com.egg.labOnline.Servicios.UsuarioService;

@Controller
@RequestMapping("/foto")
public class FotoController {

	@Autowired
	private UsuarioService uService;

	@Autowired
	private OrdenMedicaService oService;

	@Autowired
	private FotoRepository repo;

	@GetMapping("/usuario/{id}")
	public ResponseEntity<byte[]> usuarioFoto(@PathVariable String id) {
		try {
			Usuario usuario = uService.searchById(id);

			if (usuario.getFoto() == null) {
				throw new ErrorServicio("El usuario no tiene una imagen");
			}

			byte[] imagen = usuario.getFoto().getContenido();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			return new ResponseEntity<>(imagen, headers, HttpStatus.OK);

		} catch (ErrorServicio e) {
			Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping("/orden/{id}")
	public ResponseEntity<byte[]> ordenFoto(@PathVariable String id) {
		try {
			OrdenMedica orden = oService.findById(id);

			if (orden.getFoto() == null) {
				throw new ErrorServicio("La orden no tiene una imagen");
			}

			byte[] imagen = orden.getFoto().getContenido();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			return new ResponseEntity<>(imagen, headers, HttpStatus.OK);

		} catch (ErrorServicio e) {
			Logger.getLogger(FotoController.class.getName()).log(Level.SEVERE, null, e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

}
