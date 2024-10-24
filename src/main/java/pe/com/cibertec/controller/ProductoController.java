package pe.com.cibertec.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import pe.com.cibertec.model.entity.CategoriaEntity;
import pe.com.cibertec.model.entity.ProductoEntity;
import pe.com.cibertec.model.entity.UsuarioEntity;
import pe.com.cibertec.service.CategoriaService;
import pe.com.cibertec.service.ProductoService;
import pe.com.cibertec.service.UsuarioService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/producto")
public class ProductoController {

	private final ProductoService productoService;

	private final CategoriaService categoriaService;
	
	private final UsuarioService usuarioService;

	@GetMapping("/")
	public String listarProducto(Model model , HttpSession session) {
		if (session.getAttribute("usuario") == null) {
			return "redirect:/";
		}
		
		String usuarioSesion = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEncontrado = usuarioService.buscarUsuarioPorCorreo(usuarioSesion);
		model.addAttribute("usuario", usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellidos());
		
		List<ProductoEntity> listaProducto = productoService.listarProducto();
		model.addAttribute("listaprod", listaProducto);
		return "lista_productos";
	}

	@GetMapping("/registrar_producto")
	public String mostrarRegistrarProducto(Model model, HttpSession session) {
		if (session.getAttribute("usuario") == null) {
			return "redirect:/";
		}
		
		String usuarioSesion = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEncontrado = usuarioService.buscarUsuarioPorCorreo(usuarioSesion);
		model.addAttribute("usuario", usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellidos());
		
		List<CategoriaEntity> listaCategoria = categoriaService.listarCategoria();

		model.addAttribute("categorias", listaCategoria);
		model.addAttribute("producto", new ProductoEntity());
		return "registrar_producto";
	}

	@PostMapping("/registrar_producto")
	public String registrarProducto(@ModelAttribute("producto") ProductoEntity prod, Model model) {

		productoService.crearProducto(prod);
		return "redirect:/producto/";
	}
}
