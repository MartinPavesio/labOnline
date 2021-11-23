/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egg.labOnline.Servicios;

import com.egg.labOnline.Entidades.Preparativos;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.PreparativosRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PreparativosService {

	@Autowired
	PreparativosRepository preparatorioRepositorty;

	@Transactional
	public void validate(String nombre, String instruccion) throws ErrorServicio {
		if (nombre.isEmpty() || nombre == null) {
			throw new ErrorServicio("Nombre vacio");
		}
		if (instruccion.isEmpty() || instruccion == null) {
			throw new ErrorServicio("Sin instruccion");
		}
	}

	@Transactional
	public void create(String nombre, String instruccion) throws ErrorServicio {
		validate(nombre, instruccion);
		Preparativos preparativo = new Preparativos();
		preparativo.setNombre(nombre);
		preparativo.setInstruccion(instruccion);

		preparatorioRepositorty.save(preparativo);
	}

	@Transactional
	public void edit(String id, String nombre, String instruccion) throws ErrorServicio {
		validate(nombre, instruccion);
		Optional<Preparativos> rta = preparatorioRepositorty.findById(id);

		if (rta.isPresent()) {
			Preparativos preparativo = rta.get();
			preparativo.setNombre(nombre);
			preparativo.setInstruccion(instruccion);

			preparatorioRepositorty.save(preparativo);
		}

	}

	@Transactional(readOnly = true)
	public Preparativos findById(String id) {
		Optional<Preparativos> rta = preparatorioRepositorty.findById(id);
		if (rta.isPresent()) {
			Preparativos preparativo = rta.get();
			return preparativo;
		}
		return null;
	}

	@Transactional
	public void delete(String id) {
		preparatorioRepositorty.deleteById(id);
	}

	@Transactional
	public List<Preparativos> showAll() {
		return preparatorioRepositorty.findAll();
	}
}
