
package com.egg.labOnline.Servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.egg.labOnline.Entidades.Foto;
import com.egg.labOnline.Entidades.ObraSocial;
import com.egg.labOnline.Entidades.OrdenMedica;
import com.egg.labOnline.Entidades.Resultado;
import com.egg.labOnline.Entidades.Usuario;
import com.egg.labOnline.Enums.Sexo;
import com.egg.labOnline.ErrorService.ErrorServicio;
import com.egg.labOnline.Repositorios.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
	private UsuarioRepository repositorio;

	@Autowired
	private OrdenMedicaService oService;

	@Autowired
	private FotoService fService;

	@Transactional
	public void create(String nombre, String apellido, Integer dni, String clave, String mail, Integer telefono,
			ObraSocial obraSocial, Integer numeroAsociado, Date fechaNacimiento, String direccion, Sexo sexo,
			MultipartFile foto, String clave2) throws ErrorServicio {

		validate(nombre, apellido, dni, clave, mail, telefono, obraSocial, numeroAsociado, fechaNacimiento, direccion,
				sexo, foto, clave2);

		Usuario usuario = new Usuario();
		boolean bool = validateMail(mail, usuario);
		boolean bool2 = validateDni(dni, usuario);
		if (bool && bool2) {

			usuario.setApellido(apellido);
			usuario.setNombre(nombre);
			usuario.setDni(dni);
			String encriptada = new BCryptPasswordEncoder().encode(clave);
			usuario.setClave(encriptada);
			usuario.setMail(mail);
			usuario.setTelefono(telefono);
			usuario.setObraSocial(obraSocial);
			usuario.setNumeroAsociado(numeroAsociado);
			usuario.setFechaNacimiento(fechaNacimiento);
			usuario.setDireccion(direccion);
			usuario.setResultados(null);
			usuario.setSexo(sexo);

			Foto imagen = fService.crear(foto);
			usuario.setFoto(imagen);

			repositorio.save(usuario);
		} else {
			if (!bool) {
				if (!bool2) {
					throw new ErrorServicio("El dni y el mail que esta ingreasndo ya existen");
				} else {
					throw new ErrorServicio("en el mail ya existe");
				}
			}
			if (!bool2) {
				throw new ErrorServicio("el dni ya existe");

			}
		}
	}

	@Transactional
	public Usuario edit(String id, String nombre, String apellido, Integer dni, String mail, Integer telefono,
			ObraSocial obraSocial, Integer numeroAsociado, Date fechaNacimiento, String direccion, Sexo sexo,
			MultipartFile foto) throws ErrorServicio {
		{

			Optional<Usuario> rta = repositorio.findById(id);
			if (rta.isPresent()) {

				Usuario usuario = rta.get();
				boolean bool = validateMail(mail, usuario);
				boolean bool2 = validateDni(dni, usuario);
				if (bool && bool2) {

					if (!apellido.isEmpty()) {
						usuario.setApellido(apellido);
					}

					if (!nombre.isEmpty()) {
						usuario.setNombre(nombre);
					}

					if (dni != null) {
						if (dni.toString().length() < 8) {
							throw new ErrorServicio("el dni no puede ser menor de 8 digitos ");

						}
						usuario.setDni(dni);
					}

					if (!mail.isEmpty()) {
						usuario.setMail(mail);
					}

					if (telefono != null) {
						if (telefono.toString().length() < 8) {
							throw new ErrorServicio("el tefono no puede ser menor de 8 digitos ");

						}
						usuario.setTelefono(telefono);
					}

					if (obraSocial != null) {
						usuario.setObraSocial(obraSocial);
					}

					if (numeroAsociado != null) {
						usuario.setNumeroAsociado(numeroAsociado);
					}

					if (fechaNacimiento != null) {
						usuario.setFechaNacimiento(fechaNacimiento);
					}

					if (!direccion.isEmpty()) {
						usuario.setDireccion(direccion);
					}

					if (sexo != null) {
						usuario.setSexo(sexo);
					}

					if (foto.getSize() != 0) {
						Foto imagen = fService.editar(usuario.getFoto().getId(), foto);
						usuario.setFoto(imagen);
					}

					repositorio.save(usuario);
					return usuario;
				} else {
					if (!bool) {
						if (!bool2) {
							throw new ErrorServicio("El dni y el mail que esta ingreasndo ya existen");

						} else {
							throw new ErrorServicio("en el mail ya existe");

						}
					}
					if (!bool2) {
						throw new ErrorServicio("el dni ya existe");

					}
				}
				return usuario;

			} else {
				throw new ErrorServicio("No se pudo encontrar el usuario");

			}

		}
	}

	public Usuario searchById(String id) throws ErrorServicio {

		if (id == null || id.isEmpty()) {
			throw new ErrorServicio("El parametro de id a buscar no puede estar vacio");
		}

		Optional<Usuario> rta = repositorio.findById(id);
		if (rta.isPresent()) {

			Usuario usuario = rta.get();
			return usuario;
		} else {

			throw new ErrorServicio("No se pudo encontrar al usuario con el id indicado ");

		}

	}

	@Transactional
	public void delete(String id) throws ErrorServicio {

		if (id.isEmpty() || id == null) {
			throw new ErrorServicio(" el id del usuarioa modificar no puede estar vacio");
		}

		Optional<Usuario> rta = repositorio.findById(id);
		if (rta.isPresent()) {

			Usuario usuario = rta.get();
			List<OrdenMedica> ordenes = oService.showAll();
			if (ordenes != null) {
				for (OrdenMedica ordenMedica : ordenes) {
					if (ordenMedica.getUsuario().equals(usuario)) {
						oService.delete(ordenMedica.getId());
					}
				}
			}

			repositorio.delete(usuario);
		} else {
			throw new ErrorServicio("No se pudo encontrar el usuario");
		}

	}

	@Transactional(readOnly = true)
	public List<Usuario> showAll() throws ErrorServicio {

		List<Usuario> lista = repositorio.findAll();

		if (lista.isEmpty()) {

			throw new ErrorServicio("la lista esta vacia");

		} else {

			return lista;

		}

	}

	@Transactional(readOnly = true)
	public List<Usuario> searchByName(String nombre) throws ErrorServicio {

		if (nombre.isEmpty() || nombre == null) {
			throw new ErrorServicio(" el nombre no puede estar vacio");
		}

		
		List<Usuario> lista = repositorio.buscarNombre(nombre);

		if (lista.isEmpty()) {
			throw new ErrorServicio("no se encontro usuario con ese nombre");

		} else {

			return lista;
		}

	}

	@Transactional(readOnly = true)
	public Usuario searchBydni(Integer dni) throws ErrorServicio {

		if (dni == null) {
			throw new ErrorServicio("el dni no puede estar vacio  ");
		} else if (dni.toString().length() < 11) {
			throw new ErrorServicio("el formato es incorrecto");

		}

		List<Usuario> lista = repositorio.buscarDni(dni);

		if (lista.isEmpty()) {
			throw new ErrorServicio("no existe usuario con ese dni");
		} else {
			return lista.get(0);
		}
	}

	@Transactional(readOnly = true)
	public List<Usuario> searchByObraSocial(String nombre) throws ErrorServicio {

		if (nombre.isEmpty() || nombre == null) {
			throw new ErrorServicio(" el nombre no puede estar vacio");

		}
		List<Usuario> lista = repositorio.buscarObraSocial(nombre);

		if (lista.isEmpty()) {
			throw new ErrorServicio("no se encontro ningun usuario con esa obra social");

		} else {
			return lista;
		}

	}

	public void validate(String nombre, String apellido, Integer dni, String clave, String mail, Integer telefono,
			ObraSocial obraSocial, Integer numeroAsociado, Date fechaNacimiento, String direccion, Sexo sexo,
			MultipartFile foto, String clave2) throws ErrorServicio {

		if (nombre.isEmpty()) {

			throw new ErrorServicio(" el nombre no puede estar vacio");
		} else if (nombre == null) {
			throw new ErrorServicio("el nombre no puede ser nulo");
		}
		if (apellido.isEmpty()) {
			throw new ErrorServicio(" el apellido no puede estar vacio");
		} else if (apellido == null) {
			throw new ErrorServicio(" el apellido no puede ser nulo");

		}

		if (clave.isEmpty()) {
			throw new ErrorServicio(" la clave  no puede estar vacia");

		} else if (clave == null) {
			throw new ErrorServicio(" la clave  no puede estar nula");

		}
		if (clave2.isEmpty()) {
			throw new ErrorServicio(" la clave  no puede estar vacia");

		} else if (clave2 == null) {
			throw new ErrorServicio(" la clave  no puede estar nula");

		}

		if (!clave.equals(clave2)) {
			throw new ErrorServicio("las claves no pueden ser distintas");
		}

		if (mail.isEmpty()) {
			throw new ErrorServicio(" el mail no puede estar vacio");

		} else if (mail == null) {
			throw new ErrorServicio(" el mail no puede estar nulo");
		}

		if (direccion.isEmpty()) {
			throw new ErrorServicio(" la clave  no puede estar vacia");

		} else if (direccion == null) {
			throw new ErrorServicio(" la clave  no puede estar nula");

		}

		if (telefono == null) {
			throw new ErrorServicio("el tefono no puede estar vacio");

		} else if (telefono.toString().length() < 8 && telefono.toString().length() != 8) {
			throw new ErrorServicio("el tefono debe tener 8 digitos ");

		}
		if (dni == null) {
			throw new ErrorServicio("el dni no puede estar vacio");
		} else if (dni.toString().length() < 8) {
			throw new ErrorServicio("el dni no puede ser menor de 8 digitos ");

		}

		if (foto.getSize() == 0) {
			throw new ErrorServicio("la foto  no puede estar vacia ");

		}
		if (obraSocial == null) {
			throw new ErrorServicio(" la seccion obra social  no puede estar vacia");

		}

		if (sexo == null) {

			throw new ErrorServicio(" el sexo no puede ser nulo");

		} else if (!validateSex(sexo)) {

			throw new ErrorServicio(" el sexo no puede ser nulo");
		}

	}

	public boolean validateSex(Sexo sexo) {

		boolean var = false;
		for (Sexo c : Sexo.values()) {
			var = var || c.equals(sexo) ? true : false;
		}
		return var;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Integer dni = Integer.parseInt(username);
		Usuario usuario = repositorio.buscarDni(dni).get(0);
		if (usuario != null) {
			List<GrantedAuthority> permisos = new ArrayList<>();

			if (usuario.getNombre().equalsIgnoreCase("ADMIN")) {
				GrantedAuthority permiso2 = new SimpleGrantedAuthority("ROLE_ADMIN");
				permisos.add(permiso2);
			} else {
				GrantedAuthority permiso1 = new SimpleGrantedAuthority("ROLE_PACIENTE");
				permisos.add(permiso1);
			}

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuariosession", usuario);

			User user = new User(usuario.getDni().toString(), usuario.getClave(), permisos);
			return user;
		} else {
			return null;
		}

	}

	public boolean validateMail(String mail, Usuario usuario) {

		if (mail.equals(usuario.getMail())) {
			return true;
		} else {
			List<Usuario> lista = repositorio.findAll();

			if (!lista.isEmpty()) {
				List<String> listaMails = new ArrayList<String>();

				for (Usuario aux : lista) {
					listaMails.add(aux.getMail());
				}

				if (listaMails.contains(mail)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}

	public boolean validateDni(Integer dni, Usuario usuario) {

		if (dni.equals(usuario.getDni())) {
			return true;
		} else {
			List<Usuario> lista = repositorio.findAll();

			if (!lista.isEmpty()) {
				List<Integer> listaDni = new ArrayList<Integer>();

				for (Usuario aux : lista) {
					listaDni.add(aux.getDni());
				}

				if (listaDni.contains(dni)) {
					return false;

				} else {
					return true;
				}
			}

		}
		return true;

	}
}