package com.egg.labOnline.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.Preparativos;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Servicios.AnalisisService;
import com.egg.labOnline.Servicios.ObraSocialService;
import com.egg.labOnline.Servicios.PreparativosService;

@Controller
@RequestMapping("/analisis")
public class AnalisisControlador {

	@Autowired
	private AnalisisService service;

	@Autowired
	private ObraSocialService oService;

	@Autowired
	private PreparativosService pService;

	@GetMapping("")
	public String fullView(ModelMap model) {
		try {
			List<Analisis> list = service.showAll();
			model.addAttribute("list", list);
			model.addAttribute("activeLink", "Analisis");
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "admin/analisis";
		}

		return "admin/analisis";
	}

	@GetMapping("/crear")
	public String crearView(ModelMap model) {
		List<ObraSocial> list;
		try {
			list = oService.showAll();
			model.addAttribute("list", list);
			model.addAttribute("activeLink", "CrearAnalisis");
			List<Preparativos> preparativos = pService.showAll();
			model.addAttribute("preparativos", preparativos);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "admin/nuevo-analisis";
		}

		return "admin/nuevo-analisis";
	}

	@PostMapping("/crear")
	public String create(@RequestParam String nombre, @RequestParam(required = false) Integer ub,
			@RequestParam ObraSocial os, @RequestParam(required = false) Preparativos preparativo, ModelMap model) {
		try {
			System.out.println("UB=" + ub);
			service.create(nombre, ub, os, preparativo);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			try {
				List<ObraSocial> list = oService.showAll();
				model.addAttribute("list", list);
				model.addAttribute("activeLink", "CrearAnalisis");
				List<Preparativos> preparativos = pService.showAll();
				model.addAttribute("preparativos", preparativos);
			} catch (Exception e2) {

			}
			return "admin/nuevo-analisis";
		}
		return "redirect:/analisis";
	}

	@GetMapping("/edit/{id}")
	public String editView(@PathVariable String id, ModelMap model) {
		List<ObraSocial> list;
		try {
			Analisis analisis = service.findById(id);
			model.addAttribute("analisis", analisis);
			list = oService.showAll();
			model.addAttribute("list", list);
			List<Preparativos> preparativos = pService.showAll();
			model.addAttribute("preparativos", preparativos);
			model.addAttribute("activeLink", "CrearAnalisis");
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "admin/editar-analisis";
		}
		return "admin/editar-analisis";
	}

	@PostMapping("/edit")
	public String edit(@RequestParam String id, @RequestParam(required = false) String nombre,
			@RequestParam(required = false) Integer ub, @RequestParam(required = false) ObraSocial os,
			@RequestParam(required = false) Preparativos preparativo, ModelMap model) {
		try {
			service.edit(id, nombre, ub, os, preparativo);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			Analisis analisis;
			try {
				analisis = service.findById(id);
				model.addAttribute("analisis", analisis);
				List<ObraSocial> list = oService.showAll();
				model.addAttribute("list", list);
				model.addAttribute("activeLink", "CrearAnalisis");
			} catch (ErrorServicio e1) {

			}

			return "admin/editar-analisis";
		}
		return "redirect:/analisis";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable String id, ModelMap model) {
		try {
			service.delete(id);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "redirect:/analisis";
		}
		return "redirect:/analisis";
	}

	public String searchName(@RequestParam(required = false) String name, ModelMap model) {

		try {
			List<Analisis> analisis = service.searchByName(name);
			model.addAttribute("analisis", analisis);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "admin/buscar";
		}
		return "admin/buscar";
	}

	@GetMapping("/ub")
	public String searchUb(@RequestParam(required = false) Integer ub, ModelMap model) {

		try {
			List<Analisis> analisis = service.searchByUb(ub);
			model.addAttribute("analisis", analisis);
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "admin/analisis.html";
		}
		return "admin/analisis";
	}

}
