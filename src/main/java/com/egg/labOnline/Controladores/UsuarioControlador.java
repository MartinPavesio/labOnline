package com.egg.labOnline.Controladores;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.Resultado;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.Enums.Sexo;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Servicios.ObraSocialService;
import com.egg.labOnline.Servicios.ResultadoService;
import com.egg.labOnline.Servicios.UsuarioService;

import groovyjarjarpicocli.CommandLine.Model;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

	@Autowired
	private UsuarioService service;

	@Autowired
	private ObraSocialService osService;

	@Autowired
	private ResultadoService rService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/registrar")
	public String registerView(ModelMap model) {

		List<ObraSocial> obraSocial;
		try {
			obraSocial = osService.showAll();
			model.addAttribute("obrasocial", obraSocial);

		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "registrar.html";
		}

		return "registrar.html";
	}

	@PostMapping("/registrar")

	public String registrar(ModelMap model, @RequestParam(required = false) String nombre,
			@RequestParam(required = false) String apellido, @RequestParam(required = false) Integer dni,
			@RequestParam(required = false) String clave, @RequestParam(required = false) String mail,
			@RequestParam(required = false) Integer telefono, @RequestParam(required = false) ObraSocial obraSocial,
			@RequestParam(required = false) Integer numeroAsociado,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNacimiento,
			@RequestParam(required = false) String direccion, @RequestParam(required = false) Sexo sexo,
			MultipartFile foto, @RequestParam(required = false) String clave2) {

		try {
			service.create(nombre, apellido, dni, clave, mail, telefono, obraSocial, numeroAsociado, fechaNacimiento,
					direccion, sexo, foto, clave2);

		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());

			try {
				List<ObraSocial> obraSociales = osService.showAll();
				model.addAttribute("obrasocial", obraSociales);
			} catch (ErrorServicio e1) {

			}

			return "registrar.html";

		}

		return "redirect:/";
	}

	@GetMapping("/editar/{id}")
	public String modificar(ModelMap model, @PathVariable String id) {

		try {

			List<ObraSocial> obraSocial = osService.showAll();
			model.addAttribute("obrasocial", obraSocial);

			Usuario usuario = service.searchById(id);
			model.addAttribute("usuario", usuario);
			model.addAttribute("activeLink", "MostrarUsuario");
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "editar.html";
		}

		return "editar.html";
	}

	@PostMapping("/editar")
	public String edit(ModelMap model, @RequestParam(required = false) String id,
			@RequestParam(required = false) String nombre, @RequestParam(required = false) String apellido,
			@RequestParam(required = false) Integer dni, @RequestParam(required = false) String mail,
			@RequestParam(required = false) Integer telefono, @RequestParam(required = false) ObraSocial obraSocial,
			@RequestParam(required = false) Integer numeroAsociado,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNacimiento,
			@RequestParam(required = false) String direccion, @RequestParam(required = false) Sexo sexo,
			@RequestParam(required = false) MultipartFile foto, HttpServletRequest request) {

		try {
			Usuario user = service.edit(id, nombre, apellido, dni, mail, telefono, obraSocial, numeroAsociado,
					fechaNacimiento, direccion, sexo, foto);
			if (request.isUserInRole("ROLE_ADMIN")) {
				Usuario admin = service.searchByName("Admin").get(0);
				service.loadUserByUsername(admin.getDni().toString());
			} else {
				service.loadUserByUsername(user.getDni().toString());
			}

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			try {
				List<ObraSocial> obraSociales = osService.showAll();
				model.addAttribute("obrasocial", obraSociales);

				Usuario usuario = service.searchById(id);
				model.addAttribute("usuario", usuario);
				model.addAttribute("activeLink", "MostrarUsuario");
			} catch (Exception e2) {

			}
			return "editar.html";
		}
		return "redirect:/usuario/mostrar";
	}

	@GetMapping("/mostrar")
	public String showAll(ModelMap model) {

		try {
			List<Usuario> usuario = service.showAll();
			model.addAttribute("usuario", usuario);
			model.addAttribute("activeLink", "MostrarUsuario");
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "mostrar.html";

		}
		return "mostrar.html";
	}

	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable String id, ModelMap model) {

		try {
			service.delete(id);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "redirect:/usuario/mostrar";
		}
		return "redirect:/usuario/mostrar";

	}

	@GetMapping("/buscardni")
	public String buscarDni(ModelMap model, @RequestParam Integer dni) {

		try {

			Usuario usuario = service.searchBydni(dni);
			model.addAttribute("usuario", usuario);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "";

		}
		return "";

	}

	@GetMapping("/buscaros")
	public String buscarObraSocial(ModelMap model, @RequestParam String nombre) {

		try {

			List<Usuario> usuario = service.searchByObraSocial(nombre);
			model.addAttribute("usuario", usuario);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "";

		}
		return "";

	}

}