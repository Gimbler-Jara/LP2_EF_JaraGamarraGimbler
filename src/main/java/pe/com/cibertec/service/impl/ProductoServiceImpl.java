package pe.com.cibertec.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.cibertec.model.entity.ProductoEntity;
import pe.com.cibertec.repository.ProductoRepository;
import pe.com.cibertec.service.ProductoService;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

	private final ProductoRepository productoRepository;

	@Override
	public List<ProductoEntity> listarProducto() {
		return productoRepository.findAll();
	}

	@Override
	public void crearProducto(ProductoEntity productoEntity) {
		productoRepository.save(productoEntity);
	}

	@Override
	public ProductoEntity buscarporId(Integer id) {
		return productoRepository.findById(id).get();
	}

	@Override
	public void eliminarProducto(Integer id) {
		ProductoEntity prod = buscarporId(id);
		if (prod != null) {
			productoRepository.delete(prod);
		}
	}

	@Override
	public void actualizarProducto(Integer id, ProductoEntity producto) {
		ProductoEntity prod = buscarporId(id);
		
		if(prod == null) {
			throw new RuntimeException("Producto no encontrado");
		}
		
		try {
			prod.setNombreProducto(producto.getNombreProducto());
			prod.setPrecio(producto.getPrecio());
			prod.setCategoriaEntity(producto.getCategoriaEntity());
			prod.setStockProducto(producto.getStockProducto());
			productoRepository.save(prod);
		} catch (Exception e) {
			throw new RuntimeException("Error al actualizar");
		}

	}

}
