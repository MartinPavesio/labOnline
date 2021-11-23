package com.egg.labOnline.Controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.Laboratorio;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Servicios.AnalisisService;
import com.egg.labOnline.Servicios.LaboratorioService;
import com.egg.labOnline.Servicios.ObraSocialService;

@Controller
@RequestMapping("/laboratorio")
public class LaboratorioControlador {

	@Autowired
	private LaboratorioService service;

	@Autowired
	private ObraSocialService oService;

	@Autowired
	private AnalisisService aService;

	@GetMapping("")
	public String fullView(ModelMap model) {
		try {
			List<Laboratorio> list = service.listLaboratorio();
			List<ObraSocial> obrasociales = oService.showAll();
			List<Analisis> analisis = aService.showAll();
			model.addAttribute("list", list);
			model.addAttribute("analisis", analisis);
			model.addAttribute("obrasociales", obrasociales);
			model.addAttribute("activeLink", "Laboratorio");

		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "admin/laboratorio";
		}

		return "admin/laboratorio";
	}

	@GetMapping("/crear")
	public String create(ModelMap model) {
		try {
			List<ObraSocial> obrasociales = oService.showAll();
			List<Analisis> analisis = aService.showAll();
			model.addAttribute("analisis", analisis);
			model.addAttribute("obrasociales", obrasociales);
			model.addAttribute("activeLink", "Laboratorio");
			return "laboratorioCrear";
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "laboratorioCrear";
		}

	}

	@PostMapping("/crear")
	public String crear(@RequestParam String nombre, @RequestParam String cuit, @RequestParam String direccion,
			@RequestParam(required = false) Long telefono, @RequestParam String razonsocial, ModelMap model,
			@RequestParam List<Analisis> analisis, @RequestParam List<ObraSocial> os) {

		try {
			service.newLaboratorio(nombre, os, direccion, telefono, razonsocial, cuit, analisis);
			return "redirect:/laboratorio";
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());

			try {
				List<ObraSocial> obrasociales = oService.showAll();
				List<Analisis> lisanalisis = aService.showAll();
				model.addAttribute("analisis", lisanalisis);
				model.addAttribute("obrasociales", obrasociales);
				model.addAttribute("activeLink", "Laboratorio");
			} catch (ErrorServicio e1) {

			}

			return "laboratorioCrear";
		}

	}

	@GetMapping("/modificar/{id}")
	public String modificar(@PathVariable String id, ModelMap model) {
		try {

			List<ObraSocial> obraSocial = oService.showAll();
			model.addAttribute("obrasocial", obraSocial);
			List<Analisis> analisislist = aService.showAll();
			model.addAttribute("analisis", analisislist);
			Laboratorio laboratorio = service.FindbyId(id);
			model.addAttribute("lab", laboratorio);
			model.addAttribute("activeLink", "Laboratorio");
			return "laboratorioEditar";
		} catch (ErrorServicio e) {
			model.addAttribute("error", e.getMessage());
			return "laboratorioEditar";
		}

	}

	@PostMapping("/modificar")
	public String mod(@RequestParam(required = false) String nombre, @RequestParam(required = false) String cuit,
			@RequestParam(required = false) String direccion, @RequestParam(required = false) Long telefono,
			@RequestParam(required = false) String razonsocial, String id,
			@RequestParam(required = false) List<Analisis> analisis,
			@RequestParam(required = false) List<ObraSocial> os, ModelMap model) {
		try {
			service.editLaboratorio(id, nombre, os, direccion, telefono, razonsocial, cuit, analisis);
			return "redirect:/laboratorio";
		} catch (ErrorServicio e) {

			try {
				List<ObraSocial> obraSocial = oService.showAll();
				model.addAttribute("obrasocial", obraSocial);
				List<Analisis> analisislist = aService.showAll();
				model.addAttribute("analisis", analisislist);
				Laboratorio laboratorio = service.FindbyId(id);
				model.addAttribute("lab", laboratorio);
				model.addAttribute("activeLink", "Laboratorio");
			} finally {
				model.addAttribute("error", e.getMessage());
				return "laboratorioEditar";
			}

		}

	}

	@GetMapping("/eliminar/{id}")
	public String borrarView(@PathVariable String id, ModelMap model) {
		try {
			service.deleteLaboratorio(id);
		} catch (ErrorServicio e) {
			return "redirect:/laboratorio";
		}
		return "redirect:/laboratorio";
	}
}