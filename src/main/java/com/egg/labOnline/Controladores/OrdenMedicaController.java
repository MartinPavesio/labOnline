package com.egg.labOnline.Controladores;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.Foto;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.OrdenMedica;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.Enums.Autorizado;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.UsuarioRepository;
import com.egg.labOnline.Servicios.AnalisisService;
import com.egg.labOnline.Servicios.ObraSocialService;
import com.egg.labOnline.Servicios.OrdenMedicaService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/ordenmedica")
public class OrdenMedicaController {

	@Autowired
	private AnalisisService AnSv;

	@Autowired
	private ObraSocialService OsSv;

	@Autowired
	private OrdenMedicaService OmSv;

	@Autowired
	private UsuarioRepository uRepo;

	// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("")
	public String listar(ModelMap modelo) {
		try {
			List<OrdenMedica> lista = OmSv.showAll();
			modelo.addAttribute("ordenMedica", lista);
			modelo.addAttribute("activeLink", "OrdenMedica");

		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
		}
		return ("admin/orden-medica");
	}

	// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/eliminar/{id}")
	public String borrar(ModelMap modelo, @PathVariable String id) {
		try {
			OmSv.delete(id);
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
		}

		return ("redirect:/ordenmedica");
	}

	/*
	 * // @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	 * 
	 * @GetMapping("/ver-analisis{id}") public String verAnalisis(ModelMap
	 * modelo, @PathVariable String id) { try { OrdenMedica om = OmSv.findById(id);
	 * modelo.addAttribute(om); } catch (Exception e) { modelo.addAttribute("error",
	 * e.getMessage()); }
	 * 
	 * return ("redirect:/ordenmedica"); }
	 */
	// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/modificar/{id}")
	public String viewEdit(ModelMap modelo, @PathVariable String id) {
		try {
			List<ObraSocial> Os = OsSv.showAll();
			modelo.addAttribute("obraSociales", Os);
			List<Analisis> an = AnSv.showAll();
			modelo.addAttribute("analisis", an);
			OrdenMedica om = OmSv.findById(id);
			modelo.addAttribute("om", om);
			modelo.addAttribute("activeLink", "OrdenMedica");
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
		}

		return "ordenMedicaEditar";
	}

	// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/save-edit")
	public String editar(ModelMap modelo, @RequestParam String id,
			@RequestParam(required = false) List<Analisis> analisis, @RequestParam(required = false) MultipartFile foto,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaOrden,
			@RequestParam Usuario usuario, @RequestParam(required = false) Autorizado autorizado,
			@RequestParam(required = false) ObraSocial obrasocial) throws ErrorServicio {
		try {
			OmSv.edit(id, analisis, foto, fechaOrden, usuario, obrasocial, autorizado);
			return ("redirect:/ordenmedica");
		} catch (ErrorServicio e) {
			modelo.addAttribute("error", e.getMessage());
			try {
				List<ObraSocial> Os = OsSv.showAll();
				modelo.addAttribute("obraSociales", Os);
				List<Analisis> an = AnSv.showAll();
				modelo.addAttribute("analisis", an);
				OrdenMedica om = OmSv.findById(id);
				modelo.addAttribute("om", om);
				modelo.addAttribute("activeLink", "OrdenMedica");
				return "ordenMedicaEditar";
			} catch (Exception e2) {
				// TODO: handle exception
			}
			return "ordenMedicaEditar";
		}

	}

	// @PreAuthorize("hasAnyRole('ROLE_PACIENTE')")
	@GetMapping("/crear")
	public String nuevaOm(ModelMap modelo) {
		try {
			List<ObraSocial> Os = OsSv.showAll();
			modelo.addAttribute("obraSociales", Os);
			List<Analisis> an = AnSv.showAll();
			modelo.addAttribute("analisis", an);
			List<Usuario> user = uRepo.findAll();
			modelo.addAttribute("users", user);
			modelo.addAttribute("activeLink", "OrdenMedica");
		} catch (Exception e) {
			modelo.addAttribute("error", e.getMessage());
		}
		return ("ordenMedicaCrear");
	}

	// @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/guardar")
	public String sendNew(ModelMap modelo, @RequestParam(required = false) List<Analisis> analisis,
			@RequestParam(required = false) MultipartFile foto,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaOrden,
			@RequestParam(required = false) Usuario usuario, @RequestParam(required = false) ObraSocial obrasocial)
			throws ErrorServicio {
		try {
			OmSv.create(analisis, foto, fechaOrden, usuario, obrasocial);
		} catch (ErrorServicio e) {
			modelo.addAttribute("error", e.getMessage());
			try {
				List<ObraSocial> Os = OsSv.showAll();
				modelo.addAttribute("obraSociales", Os);
				List<Analisis> an = AnSv.showAll();
				modelo.addAttribute("analisis", an);
				List<Usuario> user = uRepo.findAll();
				modelo.addAttribute("users", user);
				modelo.addAttribute("activeLink", "OrdenMedica");
			} catch (ErrorServicio e2) {

			}
			return ("ordenMedicaCrear");
		}

		return ("redirect:/ordenmedica");
	}

}
