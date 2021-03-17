package com.prueba.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prueba.entity.User;
import com.prueba.repository.UserRepository;
import com.prueba.exceptions.*;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class UserService {
	
	static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	
	//obtener lista de todos los usuarios
	@GetMapping("/find/all")
	public ResponseEntity<?> findAll(){
		Map map = new HashMap<String,Object>();
		try {
			List<User> users = userRepository.findAll();
			if(users.isEmpty()) {
				throw new UserException("no se encontro informacion en la base de datos");
			}
			return new ResponseEntity<List<User>>(users,HttpStatus.OK);
		} catch (UserException e) {
			map.put("message","No se encontro ningun usuario");
			e.printStackTrace(System.out);
			logger.error(e.getMessage());
			return new ResponseEntity<Map>(map,HttpStatus.NO_CONTENT);
		}	
	}
	
	//Buscar usuario por ID (recibe id del usuario)
	@GetMapping("/find/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id){
		Map map = new HashMap<String,Object>();
		try {
			List<User> users = new ArrayList();
			User user = userRepository.findById(id).orElse(null);
			if (user != null) {
				users.add(user);
				return new ResponseEntity<List>(users,HttpStatus.OK);
			}
			throw new UserException("No se encontro ningun dato");
		} catch (UserException e) {
			map.put("message","No se encontro ningun usuario");
			e.printStackTrace(System.out);
			logger.error(e.getMessage());
			return new ResponseEntity<Map>(map,HttpStatus.NO_CONTENT);
		}
		
	}
	
	//Procesar usuarios (Recibe id de usuario a procesar)
	@PostMapping("/process")
	public ResponseEntity<?> processUser(@RequestBody String[] idsString){
		Map map = new HashMap<String,Object>();
		try {
			Long[] ids = new Long[idsString.length];
			for(int i =0; i< idsString.length;i++) {
				ids[i] = Long.valueOf(idsString[i]);
				User user = userRepository.findById(ids[i]).orElse(null);
				if(user != null) {
					user.setProcessed(true);
					userRepository.save(user);
					map.put("message "+i, "el cliente ".concat(String.valueOf(ids[i])).concat(" se proceso con exito"));
				}
			}
			return new ResponseEntity<Map>(map,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			logger.error(e.getMessage());
			map.put("message", "Ha ocurrido un error al procesar los usuarios, Verifique de nuevo");
			return new ResponseEntity<Map>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	//Creacion de usuarios (Recibe el objeto en formato JSON)
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		Map map = new HashMap<String,Object>();
		try {
			if(user.getName().equals("")||user.getLastName().equals("")) {
				throw new UserException("Los datos no pueden ir vacios");
			}
			userRepository.save(user);
			map.put("message", "La insercion se realizo con exito");
			System.out.println(user.getName());
			return new ResponseEntity<Map>(map,HttpStatus.OK);
		} catch (UserException e) {
			e.printStackTrace(System.out);
			logger.error(e.getMessage());
			map.put("message", "Ha habido un fallo al procesar los datos");
			map.put("action", "por favor verifique la informacion e intente de nuevo");
			return new ResponseEntity<Map>(map,HttpStatus.BAD_REQUEST);
		}
	}
}
