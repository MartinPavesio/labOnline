package com.egg.labOnline.Servicios;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.Laboratorio;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.OrdenMedica;
import com.egg.labOnline.Entidades.Preparativos;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.AnalisisRepository;
import com.egg.labOnline.Repositorios.LaboratorioRepository;
import com.egg.labOnline.Repositorios.OrdenMedicaRepository;
import com.egg.labOnline.Repositorios.UsuarioRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnalisisService {

	@Autowired
	private AnalisisRepository analisisRepository;

	@Autowired
	private LaboratorioRepository lRepository;

	@Autowired
	private UsuarioRepository uRepository;

	@Autowired
	private OrdenMedicaRepository oRepository;

	@Transactional
	public void create(String nombre, Integer ub, ObraSocial os, Preparativos preparativo) throws ErrorServicio {

		validate(nombre, ub, os);

		Analisis analisis = new Analisis();

		Boolean bool1 = checkOSAvailability(nombre, os, analisis);

		if (bool1) {

			analisis.setNombre(nombre);
			analisis.setUnidadBioquimica(ub);
			analisis.setObraSocial(os);
			if (preparativo == null) {
			} else {
				analisis.setPreparativos(preparativo);
			}
			analisisRepository.save(analisis);
		} else {

			if (!bool1) {
				throw new ErrorServicio("No puede haber 2 analisis mismo obra social");
			}

		}
	}

	@Transactional
	public void edit(String id, String nombre, Integer ub, ObraSocial os, Preparativos preparativo)
			throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id del analisis a modificar no puede estar vacio");
		}

		Optional<Analisis> rta = analisisRepository.findById(id);

		if (rta.isPresent()) {
			Analisis analisis = rta.get();

			Boolean bool1 = true;

			if (os != null) {
				bool1 = checkOSAvailability(nombre, os, analisis);
			}

			if (bool1) {
				if (!nombre.isEmpty()) {
					analisis.setNombre(nombre);
				}

				if (ub != null) {
					analisis.setUnidadBioquimica(ub);
				}

				if (os != null) {
					analisis.setObraSocial(os);
				}

				if (preparativo != null) {
					analisis.setPreparativos(preparativo);
				}

				analisisRepository.save(analisis);
			} else {

				if (!bool1) {
					throw new ErrorServicio("No puede haber 2 analisis mismo nombre");
				}

			}

		} else {
			throw new ErrorServicio("No se pudo encontrar una analisis con el id especificado");
		}

	}

	@Transactional
	public void delete(String id) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id del analisis a modificar no puede estar vacio");
		}

		Optional<Analisis> rta = analisisRepository.findById(id);

		if (rta.isPresent()) {
			Analisis analisis = rta.get();
			List<Laboratorio> laboratorio = lRepository.findAll();
			List<OrdenMedica> ordenes = oRepository.findAll();
			if (laboratorio != null) {
				for (Laboratorio aux : laboratorio) {
					if (aux.getAnalisis() != null) {
						Iterator it = aux.getAnalisis().iterator();
						while (it.hasNext()) {
							Analisis opc = (Analisis) it.next();
							if (opc.equals(analisis)) {
								it.remove();
							}
						}
					}
				}
			}

			if (ordenes != null) {
				for (OrdenMedica aux : ordenes) {
					if (aux.getAnalisis() != null) {
						Iterator it = aux.getAnalisis().iterator();
						while (it.hasNext()) {
							Analisis opc = (Analisis) it.next();
							if (opc.equals(analisis)) {
								it.remove();
							}
						}
					}
				}
			}

			analisisRepository.delete(analisis);
		} else {
			throw new ErrorServicio("No se pudo encontrar una analisis con el id especificado");
		}
	}

	@Transactional(readOnly = true)
	public List<Analisis> showAll() throws ErrorServicio {

		List<Analisis> list = analisisRepository.findAll();

		if (list.isEmpty()) {
			throw new ErrorServicio("No hay analisis para mostrar");
		} else {
			return list;
		}
	}

	@Transactional(readOnly = true)
	public Analisis findById(String id) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id del analisis a buscar no puede estar vacio");
		}

		Optional<Analisis> rta = analisisRepository.findById(id);
		if (rta.isPresent()) {
			return rta.get();
		} else {
			throw new ErrorServicio("No se encontro un analisis con ese id");
		}
	}

	@Transactional(readOnly = true)
	public List<Analisis> searchByName(String nombre) throws ErrorServicio {

		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El campo de nombre a buscar no puede estar vacio");
		}

		List<Analisis> list = analisisRepository.searchByName(nombre);

		if (list.isEmpty()) {
			throw new ErrorServicio("No hay analisis para mostrar");
		} else {
			return list;
		}
	}

	@Transactional(readOnly = true)
	public List<Analisis> searchByUb(Integer ub) throws ErrorServicio {

		if (ub == null) {
			throw new ErrorServicio("El campo de unidad bioquimica a buscar no puede estar vacio");
		} else if (ub <= 0) {
			throw new ErrorServicio("Valor de unidad bioquimica invalido");
		}

		List<Analisis> list = analisisRepository.searchByUd(ub);

		if (list.isEmpty()) {
			throw new ErrorServicio("No hay analisis para mostrar");
		} else {
			return list;
		}
	}

	public void validate(String nombre, Integer ub, ObraSocial os) throws ErrorServicio {

		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El campo de nombre no puede estar vacio");
		}

		if (ub == null) {
			throw new ErrorServicio("El campo de unidad bioquimica no puede estar vacio");
		}
		if (ub <= 0) {
			throw new ErrorServicio("Valor de unidad bioquimica invalido");
		}

		if (os == null) {
			throw new ErrorServicio("El campo de obra social no puede estar vacio");
		}
	}

	public boolean checkOSAvailability(String nombre, ObraSocial os, Analisis analisis) {

		if (nombre.equals(analisis.getNombre()) && os.equals(analisis.getObraSocial())) {
			return true;
		} else {
			List<Analisis> lista = analisisRepository.searchByName(nombre);

			if (!lista.isEmpty()) {
				List<ObraSocial> listaOS = new ArrayList<ObraSocial>();

				for (Analisis aux : lista) {
					listaOS.add(aux.getObraSocial());
				}

				if (listaOS.contains(os)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}
}