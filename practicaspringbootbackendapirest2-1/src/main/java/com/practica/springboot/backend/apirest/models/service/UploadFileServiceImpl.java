package com.practica.springboot.backend.apirest.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final Logger logg= org.slf4j.LoggerFactory.getLogger(UploadFileServiceImpl.class);
	private final static String DIRECTORIO_UPLOAD = "uploads";
	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		
		Path rutaArchivo = getPath(nombreFoto);
		Resource recurso = new UrlResource(rutaArchivo.toUri());
		if(!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo= Paths.get("src/main/resources/static/imagen").resolve("nousuario.png").toAbsolutePath();
			recurso = new UrlResource(rutaArchivo.toUri());
			logg.error("no se pudo cargar la imagen"+nombreFoto);
		}
		
		return recurso;
	}

	@Override
	public String guardarImagen(MultipartFile archivo) throws IOException {
		// TODO Auto-generated method stub
		String nombreArchivo = UUID.randomUUID().toString() +"_"+ archivo.getOriginalFilename().replace(" ","");
		Path rutaArchivo= getPath(nombreArchivo);
		logg.info(rutaArchivo.toString());// es para ver en la consola la ruta del archivo
		Files.copy(archivo.getInputStream(),rutaArchivo);
		return nombreArchivo;
	}

	@Override
	public boolean eliminarImagen(String nombreFoto) {
		if(nombreFoto !=null && nombreFoto.length()>0)
		{
			 Path rutafotoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			 File archivoFotoAnterior = rutafotoAnterior.toFile();
			 if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead())
			 {
				 archivoFotoAnterior.delete();
				 return true;
			 }
		}		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		// TODO Auto-generated method stub
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
