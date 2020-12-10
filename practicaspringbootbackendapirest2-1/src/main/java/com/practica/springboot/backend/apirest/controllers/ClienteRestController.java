package com.practica.springboot.backend.apirest.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.practica.springboot.backend.apirest.models.entity.Cliente;
import com.practica.springboot.backend.apirest.models.entity.Region;
import com.practica.springboot.backend.apirest.models.service.IClienteService;
import com.practica.springboot.backend.apirest.models.service.IRegionService;
import com.practica.springboot.backend.apirest.models.service.IUploadFileService;



@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private IRegionService regionService;
	// enrutado
	@GetMapping("/clientes")
	public List<Cliente> obtenerClientes() {
		return this.clienteService.findAll();
	}

	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> obtenerClientesPorPagina(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return this.clienteService.findAll(pageable);
		
	}
	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> obtenerCliente(@PathVariable long id) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		try {
			cliente = this.clienteService.findAndById(id);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (cliente == null) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", "no se encontro al usuario");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	@Secured("ROLE_ADMIN")
	@PostMapping("/clientes")
	public ResponseEntity<?> registrarCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente clientenew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			// List<String> errors = result.getFieldErrors().stream().map(error ->"El campo
			// '"+ error.getField()+ "
			// '"+error.getDefaultMessage()).collect(Collectors.toList());
			// result.getFieldErrors().forEach(action);

			List<FieldError> list = result.getFieldErrors();
			List<String> errors = new ArrayList<String>();
			for (FieldError error : list) {
				String mensaje = "El campo '" + error.getField() + " '" + error.getDefaultMessage();
				errors.add(mensaje);
			}

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			clientenew = this.clienteService.saveCliente(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la inserccion en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		response.put("mensaje", "Cliente agregado con exito");
		response.put("cliente", clientenew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {
		Map<String, Object> response = new HashMap<>();
		Cliente cliente = clienteService.findAndById(id);// trae de la bd el cliente

		if (!archivo.isEmpty()) // verificamos que se haya recibido un archivo
		{	
			String nombreArchivo = null;
			try {
				nombreArchivo = uploadFileService.guardarImagen(archivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente " );
				response.put("error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR); 
			}
			String nombrefotoAnterior = cliente.getFoto();
			uploadFileService.eliminarImagen(nombrefotoAnterior);
			
			cliente.setFoto(nombreArchivo);
			clienteService.saveCliente(cliente);
			response.put("cliente", cliente);
			response.put("mensaje", "has subido corretamente la imagen "+ nombreArchivo);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	// metodo handler
	@GetMapping("/uploads/img/{nombreFoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto)
	{
		
		Resource recurso = null;
		
		try {
			recurso = uploadFileService.cargar(nombreFoto);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 HttpHeaders httpHeaders = new HttpHeaders();
		 httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ recurso.getFilename() + "\"");
		return new ResponseEntity<Resource> (recurso,httpHeaders, HttpStatus.OK);
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> actualizarCliente(@Valid @RequestBody Cliente cliente, BindingResult result,
			@PathVariable Long id) {
		Cliente clienteActual = this.clienteService.findAndById(id);
		Cliente clienteActualizado = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(error -> "El campo '" + error.getField() + " '" + error.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (clienteActual == null) {
			response.put("mensaje", "error: no se pudo editar el cliente con el ID "
					.concat(id.toString().concat(" no existe el cliente en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt()); // cargamos los datos de esta forma para no perder la
			clienteActual.setRegion(cliente.getRegion());													// referencia del cliente buscado
			clienteActualizado = this.clienteService.saveCliente(clienteActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "Error al actualizar en la base de datos");
		response.put("cliente", clienteActualizado);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	@Secured("ROLE_ADMIN")
	@DeleteMapping("clientes/{id}")
	public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Cliente cliente = clienteService.findAndById(id);
			String nombrefotoAnterior = cliente.getFoto();
			uploadFileService.eliminarImagen(nombrefotoAnterior);
			this.clienteService.deleteCliente(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al intentar eliminar al cliente de la base de datos ");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente fue eliminado con exito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	@Secured("ROLE_ADMIN")
	// region service
	@GetMapping("/clientes/regiones")
	public List<Region> listarRegiones() {
		return this.clienteService.findAllRegiones();
	}
	
	@PostMapping("/clientes/regiones")
	public ResponseEntity<?> agregarRegion(@Valid @RequestBody Region region, BindingResult result)
	{
		Region regionnew = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors())
		{
			List<FieldError> list = result.getFieldErrors();
			List<String> errors = new ArrayList<String>();
			for (FieldError error : list) {
				String mensaje = "El campo '" + error.getField() + " '" + error.getDefaultMessage();
				errors.add(mensaje);
			}
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			regionnew = this.regionService.saveCliente(region);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la inserccion en la base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		response.put("mensaje", "Region agregado con exito");
		response.put("region", regionnew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

}
