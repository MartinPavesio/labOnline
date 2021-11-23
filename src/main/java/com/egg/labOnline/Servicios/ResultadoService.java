
package com.egg.labOnline.Servicios;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.Resultado;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.ResultadoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResultadoService {

	@Autowired
	private ResultadoRepository resultadoRepository;

	@Transactional
	public void crear(Analisis analisis, String resultado, Date fecha, Usuario usuario) throws ErrorServicio {

		validar(analisis, resultado, fecha, usuario);

		Resultado result = new Resultado();

		result.setAnalisis(analisis);
		result.setFecha(fecha);
		result.setResultado(resultado);
		result.setUsuario(usuario);

		resultadoRepository.save(result);

	}

	@Transactional
	public void modificar(String id, Analisis analisis, String resultado, Date fecha, Usuario usuario)
			throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id del resultado a modificar no puede estar vacio");
		}

		validar(analisis, resultado, fecha, usuario);

		Optional<Resultado> rta = resultadoRepository.findById(id);
		if (rta.isPresent()) {

			Resultado result = rta.get();

			result.setAnalisis(analisis);
			result.setFecha(fecha);
			result.setResultado(resultado);
			result.setUsuario(usuario);

			resultadoRepository.save(result);
		} else {
			throw new ErrorServicio("No se pudo encontrar un resultado con el id especificado");
		}

	}

	@Transactional
	public void eliminar(String id) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id del resultado a modificar no puede estar vacio");
		}

		Optional<Resultado> rta = resultadoRepository.findById(id);
		if (rta.isPresent()) {
			Resultado result = rta.get();
			resultadoRepository.delete(result);
		} else {
			throw new ErrorServicio("No se pudo encontrar un resultado con el id especificado");
		}
	}

	@Transactional(readOnly = true)
	public List<Resultado> showAll() throws ErrorServicio {
		List<Resultado> list = resultadoRepository.findAll();
		if (list.isEmpty()) {
			throw new ErrorServicio("No hay resultados para mostrar");
		} else {
			return list;
		}

	}

	@Transactional(readOnly = true)
	public Resultado findById(String id) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id del resultado a buscar no puede estar vacio");
		}

		Optional<Resultado> rta = resultadoRepository.findById(id);
		if (rta.isPresent()) {
			return rta.get();
		} else {
			throw new ErrorServicio("No se encontro un resultado con el id especificado");
		}
	}

	@Transactional(readOnly = true)
	public Resultado searchByUsuarioDni(Integer dni) throws ErrorServicio {

		if (dni == null) {
			throw new ErrorServicio("El dni del usuario a buscar no puede estar vacio");
		} else if (dni.toString().length() != 8) {
			throw new ErrorServicio("Valor de dni invalido");
		}

		List<Resultado> rta = resultadoRepository.findByUsuarioDni(dni);

		if (rta.isEmpty()) {
			throw new ErrorServicio("No existe un usuario con ese dni");
		} else {
			return rta.get(0);
		}

	}

	@Transactional(readOnly = true)
	public List<Resultado> searchByAnalisisId(String id) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El id del analisis a buscar no puede estar vacio");
		}

		List<Resultado> rta = resultadoRepository.findByAnalisisId(id);

		if (rta.isEmpty()) {
			throw new ErrorServicio("No se encontraron resultados relacionados con el analisis especificado");
		} else {
			return rta;
		}

	}

	@Transactional(readOnly = true)
	public List<Resultado> searchByDate(Date date) throws ErrorServicio {

		if (date == null) {
			throw new ErrorServicio("El campo de la fecha no puede estar vacio");
		} else if (date.before(new Date("2021-01-01"))) {
			throw new ErrorServicio("La fecha ingresada no es valida");
		}

		List<Resultado> rta = resultadoRepository.findByDate(date);

		if (rta.isEmpty()) {
			throw new ErrorServicio("No se encontraron resultados con la fecha especificada");
		} else {
			return rta;
		}

	}

	public void validar(Analisis analisis, String resultado, Date fecha, Usuario usuario) throws ErrorServicio {

		if (analisis == null) {
			throw new ErrorServicio("La seccion de analisis no puede estar vacia");
		}

		if (resultado == null || resultado.isEmpty()) {
			throw new ErrorServicio("La seccion de resultado no puede estar vacia");
		}

		if (fecha == null) {
			throw new ErrorServicio("El campo de la fecha no puede estar vacio");
		} else if (fecha.before(new Date("2021-01-01"))) {
			throw new ErrorServicio("La fecha ingresada no es valida");
		}

		if (usuario == null) {
			throw new ErrorServicio("El campom de usuario no puede estar vacio");
		}
	}

}
