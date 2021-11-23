package com.egg.labOnline.Controladores;

import com.egg.labOnline.Entidades.Preparativos;
import com.egg.labOnline.Repositorios.PreparativosRepository;
import com.egg.labOnline.Servicios.PreparativosService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PreparativosControlador {

	@Autowired
	private PreparativosService sv;
	@Autowired
	private PreparativosRepository repo;

	@GetMapping("/admin/preparativos")
	public String prepa(ModelMap modelo) {
		try {
			List<Preparativos> lista = sv.showAll();
			modelo.addAttribute("lista", lista);
			modelo.addAttribute("activeLink", "Preparativos");
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
		}
		return "admin/preparativos";
	}

	@GetMapping("/createprepa")
	public String createPrepa(ModelMap modelo) {
		modelo.addAttribute("activeLink", "Preparativos");
		return "admin/crearPrepa";
	}

	@PostMapping("/admin/preparativos/save")
	public String save(ModelMap modelo, @RequestParam(required = false) String nombre,
			@RequestParam String instruccion) {
		try {
			sv.create(nombre, instruccion);
			return "redirect:/admin/preparativos";
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			return "redirect:/admin/preparativos";
		}
	}

	@GetMapping("/editprepa/{id}")
	public String editPrepa(ModelMap modelo, @PathVariable String id) {
		Preparativos preparativo = sv.findById(id);
		modelo.addAttribute("prepa", preparativo);
		modelo.addAttribute("activeLink", "Preparativos");
		return "admin/editPrepa";
	}

	@PostMapping("/admin/preparativos/saveeditt")
	public String saveEdit(ModelMap modelo, @RequestParam String id, @RequestParam String nombre,
			@RequestParam String instruccion) {
		try {
			System.out.println("ID=" + id);
			sv.edit(id, nombre, instruccion);
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
			modelo.addAttribute("activeLink", "Preparativos");
			return "redirect:admin/preparativos";
		}
		return "redirect:admin/preparativos";
	}
}
