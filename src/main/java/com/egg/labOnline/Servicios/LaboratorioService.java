package com.egg.labOnline.Servicios;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.Laboratorio;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.LaboratorioRepository;

import groovyjarjarantlr4.v4.parse.ANTLRParser.throwsSpec_return;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class LaboratorioService {

	@Autowired
	private LaboratorioRepository laboratorioRepository;

	public LaboratorioService() {
	}

	@Transactional
	public void newLaboratorio(String nombre, List<ObraSocial> obraSociales, String direccion, Long telefono,
			String razonSocial, String cuit, List<Analisis> analisis) throws ErrorServicio {
		validate(nombre, obraSociales, direccion, telefono, razonSocial, cuit, analisis);

		Laboratorio laboratorio = new Laboratorio();

		check(nombre, telefono, direccion, cuit, laboratorio);

		laboratorio.setNombre(nombre);
		laboratorio.setTelefono(telefono);
		laboratorio.setCuit(cuit);
		laboratorio.setDireccion(direccion);
		laboratorio.setRazonSocial(razonSocial);
		laboratorio.setAnalisis(analisis);
		laboratorio.setObraSociales(obraSociales);

		laboratorioRepository.save(laboratorio);
	}

	public boolean checkNombre(String nombre, Laboratorio laboratorio) {

		if (nombre.equals(laboratorio.getNombre())) {
			return true;
		} else {
			List<Laboratorio> laboratorios = laboratorioRepository.findAll();

			if (!laboratorios.isEmpty()) {
				List<String> listaNombre = new ArrayList<String>();

				for (Laboratorio aux : laboratorios) {
					listaNombre.add(aux.getNombre());
				}

				if (listaNombre.contains(nombre)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}

	public boolean checkdireccion(String direccion, Laboratorio laboratorio) {

		if (direccion.equals(laboratorio.getDireccion())) {
			return true;
		} else {
			List<Laboratorio> laboratorios = laboratorioRepository.findAll();

			if (!laboratorios.isEmpty()) {
				List<String> listaDireccion = new ArrayList<String>();

				for (Laboratorio aux : laboratorios) {
					listaDireccion.add(aux.getDireccion());
				}

				if (listaDireccion.contains(direccion)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}

	public boolean checkRazonSocial(String razonsocial, Laboratorio laboratorio) {

		if (razonsocial.equals(laboratorio.getRazonSocial())) {
			return true;
		} else {
			List<Laboratorio> laboratorios = laboratorioRepository.findAll();

			if (!laboratorios.isEmpty()) {
				List<String> listaRazonSocial = new ArrayList<String>();

				for (Laboratorio aux : laboratorios) {
					listaRazonSocial.add(aux.getRazonSocial());
				}

				if (listaRazonSocial.contains(razonsocial)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}

	public boolean checktelefono(Long telefono, Laboratorio laboratorio) {

		if (telefono.equals(laboratorio.getTelefono())) {
			return true;
		} else {
			List<Laboratorio> laboratorios = laboratorioRepository.findAll();

			if (!laboratorios.isEmpty()) {
				List<Long> listaTelefono = new ArrayList<Long>();

				for (Laboratorio aux : laboratorios) {
					listaTelefono.add(aux.getTelefono());
				}

				if (listaTelefono.contains(telefono)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}

	public boolean checkCuit(String cuit, Laboratorio laboratorio) {

		if (cuit.equals(laboratorio.getCuit())) {
			return true;
		} else {
			List<Laboratorio> laboratorios = laboratorioRepository.findAll();

			if (!laboratorios.isEmpty()) {
				List<String> listaCuit = new ArrayList<String>();

				for (Laboratorio aux : laboratorios) {
					listaCuit.add(aux.getCuit());
				}

				if (listaCuit.contains(cuit)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}

	@Transactional
	public void editLaboratorio(String id, String nombre, List<ObraSocial> obraSociales, String direccion,

			Long telefono, String razonSocial, String cuit, List<Analisis> analisis) throws ErrorServicio {
		if (id.isEmpty() || id == null) {
			throw new ErrorServicio("El id no puede estar vacio");
		}

		Laboratorio laboratorio = laboratorioRepository.buscarLaboratorioporID(id).get(0);

		check(nombre, telefono, direccion, cuit, laboratorio);

		if (!nombre.isEmpty()) {
			laboratorio.setNombre(nombre);
		}

		if (telefono != null) {
			if (telefono.toString().length() != 8) {
				throw new ErrorServicio("El telefono no puede tener menos de 8 digitos");
			}
			laboratorio.setTelefono(telefono);
		}

		if (!cuit.isEmpty()) {
			laboratorio.setCuit(cuit);
		}

		if (!direccion.isEmpty()) {
			laboratorio.setDireccion(direccion);
		}

		if (!razonSocial.isEmpty()) {
			laboratorio.setRazonSocial(razonSocial);
		}

		if (analisis != null) {
			laboratorio.setAnalisis(analisis);
		}

		if (obraSociales != null) {
			laboratorio.setObraSociales(obraSociales);
		}

		laboratorioRepository.save(laboratorio);
	}

	@Transactional
	public void deleteLaboratorio(String id) throws ErrorServicio {
		if (id.isEmpty() || id == null) {
			throw new ErrorServicio("El id no puede estar vacio");
		}
		Laboratorio entidad = laboratorioRepository.buscarLaboratorioporID(id).get(0);
		laboratorioRepository.delete(entidad);
	}

	@Transactional(readOnly = true)
	public List<Laboratorio> listLaboratorio() throws ErrorServicio {
		List<Laboratorio> laboratorios = laboratorioRepository.findAll();
		if (laboratorios.isEmpty()) {
			throw new ErrorServicio("La lista esta vacía");
		} else {
			return laboratorios;
		}

	}

	@Transactional(readOnly = true)
	public List<Laboratorio> findbyOS(String nombre) throws ErrorServicio {
		if (nombre.isEmpty() || nombre == null) {
			throw new ErrorServicio("El nombre no puede estar vacío");
		}
		List<Laboratorio> laboratorios = laboratorioRepository.buscarporOS(nombre);
		if (laboratorios.isEmpty()) {
			throw new ErrorServicio("La lista esta vacía");
		} else {
			return laboratorios;
		}

	}

	@Transactional
	public Laboratorio FindbyId(String id) throws ErrorServicio {

		if (id.isEmpty() || id == null) {
			throw new ErrorServicio("el id no puede estar vacio");
		}
		Optional<Laboratorio> rta = laboratorioRepository.findById(id);
		if (rta.isPresent()) {
			return rta.get();
		} else {
			throw new ErrorServicio("el laboratorio no esta cargado");
		}
	}

	public void validate(String nombre, List<ObraSocial> obraSociales, String direccion, Long telefono,
			String razonSocial, String cuit, List<Analisis> analisis) throws ErrorServicio {
		if (nombre.isEmpty() || nombre == null) {
			throw new ErrorServicio("El nombre no puede estar vacío");
		}
		if (obraSociales.isEmpty() || obraSociales == null) {
			throw new ErrorServicio("La obra social no puede estar vacía");
		}
		if (telefono.toString().length() <= 8) {
			throw new ErrorServicio("El telefono no puede tener menos de 8 digitos");
		}
		if (telefono == null) {
			throw new ErrorServicio("El telefono no puede estar vacío");
		}
		if (razonSocial.isEmpty() || razonSocial == null) {
			throw new ErrorServicio("La razon social no puede estar vacía");
		}
		if (cuit.isEmpty() || cuit == null) {
			throw new ErrorServicio("El cuit no puede estar vacío");
		}
		if (analisis.isEmpty() || analisis == null) {
			throw new ErrorServicio("Los analisis no pueden estar vacíos");
		}
	}

	public void check(String nombre, Long telefono, String direccion, String cuit, Laboratorio laboratorio)
			throws ErrorServicio {

		Boolean boolNombre = checkNombre(nombre, laboratorio);
		Boolean boolTelefono = checktelefono(telefono, laboratorio);
		Boolean boolCuit = checkCuit(cuit, laboratorio);
		Boolean boolDir = checkdireccion(direccion, laboratorio);

		List<String> msg = new ArrayList<String>();
		String error = "";

		if (!boolNombre) {
			msg.add("Nombre");
		}

		if (!boolTelefono) {
			msg.add("Telefono");
		}

		if (!boolCuit) {
			msg.add("Cuit");
		}

		if (!boolDir) {
			msg.add("Direccion");
		}

		Integer size = msg.size();

		if (size != 0) {

			for (String string : msg) {
				error += string;

				if (!string.equals(msg.get(size - 1))) {
					error += ", ";
				} else {
					if (size.equals(1)) {
						error += " ya existe.";
					} else {
						error += " ya existen.";
					}
				}
			}
			throw new ErrorServicio(error);
		}

	}

}