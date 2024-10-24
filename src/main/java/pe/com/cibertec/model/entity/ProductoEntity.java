package pe.com.cibertec.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_producto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "producto_id", nullable = false)
	private Integer productoId;
	
	@Column(name = "nombre_producto", nullable = false)
	private String nombreProducto;
	
	@Column(name = "precio", nullable = false)
	private Double precio;
	
	@Column(name = "stockProducto", nullable = false)
	private Integer stockProducto;
	
	@ManyToOne
	@JoinColumn(name = "fk_categoria", nullable = false)
	private CategoriaEntity categoriaEntity;
	
}
