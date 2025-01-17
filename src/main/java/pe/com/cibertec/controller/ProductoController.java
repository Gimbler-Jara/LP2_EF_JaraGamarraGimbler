package pe.com.cibertec.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
import pe.com.cibertec.service.impl.PdfService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/producto")
public class ProductoController {

	private final ProductoService productoService;

	private final CategoriaService categoriaService;
	
	private final UsuarioService usuarioService;
	
	private final PdfService pdfService;

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
	
	@GetMapping("/editar_producto/{id}")
	public String mostrarProductoEditar(@PathVariable("id") Integer id, Model model, HttpSession session) {
		if (session.getAttribute("usuario") == null) {
			return "redirect:/";
		}
		
		String usuarioSesion = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEncontrado = usuarioService.buscarUsuarioPorCorreo(usuarioSesion);
		model.addAttribute("usuario", usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellidos());
		
		ProductoEntity producto = productoService.buscarporId(id);
		List<CategoriaEntity> listaCategoria = categoriaService.listarCategoria();
		model.addAttribute("categorias", listaCategoria);
		model.addAttribute("producto",producto);
		return "editar_producto";
	}
	
	
	@PostMapping("/editar_producto/{id}")
	public String editarUsuario(@PathVariable("id") Integer id,@ModelAttribute("user") ProductoEntity producto, Model model) {
		productoService.actualizarProducto(id, producto);
		return "redirect:/producto/";
	}
	
	
	@GetMapping("/detalle_producto/{id}")
	public String verDetalle(Model model, @PathVariable("id") Integer id , HttpSession session) {
		
		if (session.getAttribute("usuario") == null) {
			return "redirect:/";
		}
		
		String usuarioSesion = session.getAttribute("usuario").toString();
		UsuarioEntity usuarioEncontrado = usuarioService.buscarUsuarioPorCorreo(usuarioSesion);
		model.addAttribute("usuario", usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellidos());
		
		ProductoEntity producto = productoService.buscarporId(id);
		model.addAttribute("producto",producto);
		return "detalle_producto";
	}

	@GetMapping("/delete/{id}")
	public String deleteUsuario(@PathVariable("id") Integer id) {
		productoService.eliminarProducto(id);
		return "redirect:/producto/";
	}
	
	@GetMapping("/generar_pdf")
	public ResponseEntity<InputStreamResource> generarPDf(HttpSession sesion) throws IOException {
		
		String usuarioSesion = sesion.getAttribute("usuario").toString();
		UsuarioEntity usuarioEncontrado = usuarioService.buscarUsuarioPorCorreo(usuarioSesion);
		
		Map<String, Object> datosPdf = new HashMap<String, Object>();
		datosPdf.put("factura", productoService.listarProducto());
		datosPdf.put("usuario", usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellidos());
		ByteArrayInputStream pdfBytes = pdfService.generarPdf("template_pdf", datosPdf);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=productos.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdfBytes));
		
	}
	
}
