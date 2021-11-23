package com.egg.labOnline.Servicios;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.labOnline.Entidades.Analisis;
import com.egg.labOnline.Entidades.Laboratorio;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.OrdenMedica;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.AnalisisRepository;
import com.egg.labOnline.Repositorios.LaboratorioRepository;
import com.egg.labOnline.Repositorios.ObraSocialRepository;
import com.egg.labOnline.Repositorios.OrdenMedicaRepository;
import com.egg.labOnline.Repositorios.UsuarioRepository;

@Service
public class ObraSocialService {

	/* INYEC OBJ.OS.REPOSITORY */
	@Autowired
	private ObraSocialRepository obraSocialRepository;

	@Autowired
	private AnalisisRepository aRepository;

	@Autowired
	private LaboratorioRepository lRepository;

	@Autowired
	private UsuarioRepository uRepository;

	@Autowired
	private OrdenMedicaRepository oRepository;

	/* CONSTRUCTOR POR DEFECTO */
	public ObraSocialService() {
	}

	/* CRUD ALTA */
	@Transactional
	public void create(String nombre, Integer codigo, Long telefono, String direccion, Double arancel, String mail)
			throws ErrorServicio {

		validate(nombre, codigo, telefono, direccion, arancel, mail);

		ObraSocial obraSocial = new ObraSocial();

		check(nombre, codigo, telefono, direccion, mail, obraSocial);

		obraSocial.setNombre(nombre);
		obraSocial.setCodigo(codigo);
		obraSocial.setTelefono(telefono);
		obraSocial.setDireccion(direccion);
		obraSocial.setArancel(arancel);
		obraSocial.setMail(mail);

		obraSocialRepository.save(obraSocial);

	}

	/* CRUD ELIMINACION */
	@Transactional
	public void delete(String id) throws ErrorServicio {

		if (id.isEmpty() || id == null) {
			throw new ErrorServicio("El id no puede estar vacio o ser nulo");
		}

		Optional<ObraSocial> respuesta = obraSocialRepository.findById(id);

		if (respuesta.isPresent()) {
			ObraSocial obraSocial = respuesta.get();
			List<Analisis> analisis = aRepository.findAll();
			List<Laboratorio> laboratorio = lRepository.findAll();
			List<Usuario> usuarios = uRepository.findAll();
			List<OrdenMedica> ordenes = oRepository.findAll();
			if (analisis != null) {
				for (Analisis aux : analisis) {
					if (aux.getObraSocial() != null) {
						if (aux.getObraSocial().equals(obraSocial)) {
							aux.setObraSocial(null);
						}
					}
				}
			}

			if (laboratorio != null) {
				for (Laboratorio aux : laboratorio) {
					if (aux.getObraSociales() != null) {
						Iterator it = aux.getObraSociales().iterator();
						while (it.hasNext()) {
							ObraSocial opc = (ObraSocial) it.next();
							if (opc.equals(obraSocial))
								;
							it.remove();
						}
					}
				}
			}

			if (usuarios != null) {
				for (Usuario aux : usuarios) {
					if (aux.getObraSocial() != null) {
						if (aux.getObraSocial().equals(obraSocial)) {
							aux.setObraSocial(null);
						}
					}
				}
			}

//			if (usuarios != null) {
//				for (Usuario aux : usuarios) {
//					if (aux.getObraSocial() != null) {
//						Iterator it = aux.getObraSocial().iterator();
//						while (it.hasNext()) {
//							ObraSocial opc = (ObraSocial) it.next();
//							if (opc.equals(obraSocial))
//								;
//							it.remove();
//						}
//					}
//				}
//			}

			if (ordenes != null) {
				for (OrdenMedica aux : ordenes) {
					if (aux.getObraSocial() != null) {
						if (aux.getObraSocial().equals(obraSocial)) {
							aux.setObraSocial(null);
						}
					}
				}
			}

			obraSocialRepository.delete(obraSocial);
		} else {
			throw new ErrorServicio("La obra social que desea eliminar no se encuentra en nuestra Cartilla");
		}

	}

	/* CRUD MODIFICACION */
	@Transactional
	public void edit(String id, String nombre, Integer codigo, Long telefono, String direccion, Double arancel,
			String mail) throws ErrorServicio {

		if (id.isEmpty() || id == null) {
			throw new ErrorServicio("El id no puede estar vacio o ser nulo");
		}

		Optional<ObraSocial> respuesta = obraSocialRepository.findById(id);

		if (respuesta.isPresent()) {

			ObraSocial obraSocial = respuesta.get();

			check(nombre, codigo, telefono, direccion, mail, obraSocial);
			if (!nombre.isEmpty()) {
				obraSocial.setNombre(nombre);
			}

			if (codigo == null) {
				if (codigo == null || codigo.toString().length() <= 4) {
					throw new ErrorServicio("El codigo no puede ser nulo o menor que 4 digitos");
				}
				obraSocial.setCodigo(codigo);
			}

			if (telefono == null) {
				if (telefono == null || telefono.toString().length() <= 4) {
					throw new ErrorServicio("El teléfono no puede ser nulo o menor que 4 digitos");
				}
				obraSocial.setTelefono(telefono);
			}

			if (direccion == null) {
				obraSocial.setDireccion(direccion);
			}

			if (arancel == null) {
				obraSocial.setArancel(arancel);
			}

			if (!mail.isEmpty()) {
				obraSocial.setMail(mail);
			}

			obraSocialRepository.save(obraSocial);

		}
	}

	/* CRUD DE TODAS LAS OBRAS SOCIALES */
	@Transactional(readOnly = true)
	public List<ObraSocial> showAll() throws ErrorServicio {

		List<ObraSocial> lista = obraSocialRepository.findAll();

		if (lista.isEmpty()) {
			return lista;
		}

		return lista;
	}

	/* TRAER OS POR ID */
	@Transactional(readOnly = true)
	public ObraSocial findById(String id) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("La obra social que busca no se encuentra en nuestra Cartilla");
		}

		Optional<ObraSocial> respuesta = obraSocialRepository.findById(id);
		if (respuesta.isPresent()) {
			ObraSocial obraSocial = respuesta.get();
			return obraSocial;
		} else {
			throw new ErrorServicio("No se pudo encontrar al usuario con el id indicado");
		}
	}

	/* TRAER USUARIOS DE OS */
	@Transactional(readOnly = true)
	public List<Usuario> showUserOsByName(String nombre) throws ErrorServicio {

		ObraSocial listaOS = obraSocialRepository.buscarOSNombre(nombre).get(0);
		if (listaOS.getUsuario().isEmpty()) {
			throw new ErrorServicio("No tiene usuarios asignados");
		} else {
			return listaOS.getUsuario();
		}

	}

	/* BUSCAR OS VINCULADA A DNI DE USUARIOS */
	@Transactional(readOnly = true)
	public List<ObraSocial> searchByDni(Integer dni) throws ErrorServicio {

		if (dni == null || dni.intValue() == 0) {
			throw new ErrorServicio("El DNI no puede ser nulo ni 0(cero)");
		}

		List<ObraSocial> lista = obraSocialRepository.buscarOSUsuarioDni(dni);

		if (lista.isEmpty()) {
			throw new ErrorServicio("El usuario que busca no se encuentra vinculado a nuestras obras sociales");
		}

		return lista;

	}

	/* VALIDACION NOMBRE */
	public Boolean checkNombreAvailability(String nombre, ObraSocial obraSocial) {

		if (nombre.equals(obraSocial.getNombre())) {
			return true;
		} else {
			List<ObraSocial> lista = obraSocialRepository.findAll();

			if (!lista.isEmpty()) {
				List<String> listaNombre = new ArrayList<String>();

				for (ObraSocial aux : lista) {
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

	/* VALIDACION CODIGO */
	public Boolean checkCodigoAvailability(Integer codigo, ObraSocial obraSocial) {

		if (codigo.equals(obraSocial.getCodigo())) {
			return true;
		} else {
			List<ObraSocial> lista = obraSocialRepository.findAll();

			if (!lista.isEmpty()) {
				List<Integer> listaCodigo = new ArrayList<Integer>();

				for (ObraSocial aux : lista) {
					listaCodigo.add(aux.getCodigo());
				}

				if (listaCodigo.contains(codigo)) {
					return false;

				} else {
					return true;
				}
			}

		}

		return true;

	}

	/* VALIDACION TELEFONO */
	public Boolean checkTelefonoAvailability(Long telefono, ObraSocial obraSocial) {

		if (telefono.equals(obraSocial.getTelefono())) {
			return true;
		} else {
			List<ObraSocial> lista = obraSocialRepository.findAll();

			if (!lista.isEmpty()) {
				List<Long> listaTelefono = new ArrayList<Long>();

				for (ObraSocial aux : lista) {
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

	/* VALIDACION DIRECCION */
	public Boolean checkDireccionAvailability(String direccion, ObraSocial obraSocial) {

		if (direccion.equals(obraSocial.getDireccion())) {
			return true;
		} else {
			List<ObraSocial> lista = obraSocialRepository.findAll();

			if (!lista.isEmpty()) {
				List<String> listaDireccion = new ArrayList<String>();

				for (ObraSocial aux : lista) {
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

	/* VALIDACION MAIL */
	public Boolean checkMailAvailability(String mail, ObraSocial obraSocial) {

		if (mail.equals(obraSocial.getDireccion())) {
			return true;
		} else {
			List<ObraSocial> lista = obraSocialRepository.findAll();

			if (!lista.isEmpty()) {
				List<String> listaMail = new ArrayList<String>();

				for (ObraSocial aux : lista) {
					listaMail.add(aux.getDireccion());
				}

				if (listaMail.contains(mail)) {
					return false;

				} else {
					return true;
				}
			}

		}

		return true;

	}

	/* VALIDACION DE ATRIBUTOS NULL OR EMPTY */
	public void validate(String nombre, Integer codigo, Long telefono, String direccion, Double arancel, String mail)
			throws ErrorServicio {
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El nombre no puede ser nulo o estar vacío");
		}
		if (codigo == null || codigo.toString().length() <= 4) {
			throw new ErrorServicio("El codigo no puede ser nulo o menor que 4 digitos");
		}
		if (telefono == null || telefono.toString().length() <= 4) {
			throw new ErrorServicio("El teléfono no puede ser nulo o menor que 4 digitos");
		}
		if (direccion == null || direccion.isEmpty()) {
			throw new ErrorServicio("La dirección no puede ser nula o estar vacía");
		}
		if (arancel == null || arancel == 0) {
			throw new ErrorServicio("El arancel no puede ser nulo o estar vacío");
		}
		if (mail == null || mail.isEmpty()) {
			throw new ErrorServicio("El mail no puede ser nulo o estar vacío");
		}
	}

	public void check(String nombre, Integer codigo, Long telefono, String direccion, String mail,
			ObraSocial obraSocial) throws ErrorServicio {

		boolean boolNombre = checkNombreAvailability(nombre, obraSocial);
		boolean boolCodigo = checkCodigoAvailability(codigo, obraSocial);
		boolean boolTelefono = checkTelefonoAvailability(telefono, obraSocial);
		boolean boolDir = checkDireccionAvailability(direccion, obraSocial);
		boolean boolMail = checkMailAvailability(mail, obraSocial);

		List<String> msg = new ArrayList<String>();

		String error = "";

		if (!boolNombre) {
			msg.add("Nombre");
		}

		if (!boolTelefono) {
			msg.add("Telefono");
		}

		if (!boolCodigo) {
			msg.add("Codigo");
		}

		if (!boolDir) {
			msg.add("Direccion");
		}

		if (!boolMail) {
			msg.add("Mail");
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