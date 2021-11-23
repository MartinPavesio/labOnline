package com.egg.labOnline.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Servicios.ObraSocialService;

@Controller
@RequestMapping("/obrasocial")
public class ObraSocialControlador {

	@Autowired
	private ObraSocialService obraSocialService;

	@PreAuthorize("hasAnyRole('ROLE_PACIENTE', 'ROLE_ADMIN')") 
	@GetMapping("")
	public String listarObrasSociales(ModelMap modelo) {
		try {
			modelo.addAttribute("listOS", obraSocialService.showAll());
			modelo.addAttribute("activeLink", "OSociales");
			return "obrasocial";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			return "obrasocial";
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/crear")
	public String crearObraSocial(ModelMap modelo) {
		modelo.addAttribute("activeLink", "OSociales");
		return "obrasocialcrear";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/crear")
	public String guardarObraSocial(@RequestParam String nombre, @RequestParam(required = false) Integer codigo,
			@RequestParam(required = false) Long telefono, @RequestParam String direccion,
			@RequestParam(required = false) Double arancel, @RequestParam String mail, ModelMap modelo) {
		try {
			obraSocialService.create(nombre, codigo, telefono, direccion, arancel, mail);
			return "redirect:/obrasocial";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			modelo.addAttribute("activeLink", "OSociales");
			return "obrasocialcrear";
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/eliminar/{id}")
	public String eliminarObraSocial(@PathVariable String id, ModelMap modelo) {

		try {
			obraSocialService.delete(id);
			return "redirect:/obrasocial";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			return "redirect:/obrasocial";
		}

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/modificar/{id}")
	public String modificarObraSocial(@PathVariable String id, ModelMap modelo) {
		try {
			ObraSocial os = obraSocialService.findById(id);
			modelo.addAttribute("obrasocial", os);
			modelo.addAttribute("activeLink", "OSociales");
			return "obrasocialmodificar";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			return "obrasocialmodificar";
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/modificar")
	public String modificarObraSocial(@RequestParam String id, @RequestParam(required = false) String nombre,
			@RequestParam(required = false) Integer codigo, @RequestParam(required = false) Long telefono,
			@RequestParam(required = false) String direccion, @RequestParam(required = false) Double arancel,
			@RequestParam(required = false) String mail, ModelMap modelo) {
		try {
			obraSocialService.edit(id, nombre, codigo, telefono, direccion, arancel, mail);
			return "redirect:/obrasocial";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());

			try {
				ObraSocial os = obraSocialService.findById(id);
				modelo.addAttribute("obrasocial", os);
				modelo.addAttribute("activeLink", "OSociales");
			} catch (Exception e2) {

			}
			return "obrasocialmodificar";
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/OSporDniUsuario")
	public String osPorDniUsuario(@RequestParam Integer dni, ModelMap modelo) {
		try {
			modelo.addAttribute("osPorDniUsuario", obraSocialService.searchByDni(dni));
			return "OSporDniUsuario";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			return "OSporDniUsuario";
		}

	}

}
