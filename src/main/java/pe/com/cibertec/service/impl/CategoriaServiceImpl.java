package pe.com.cibertec.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pe.com.cibertec.model.entity.CategoriaEntity;
import pe.com.cibertec.repository.CategoriaRepository;
import pe.com.cibertec.service.CategoriaService;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService{
	
	private final CategoriaRepository categoriaRepository;

	@Override
	public List<CategoriaEntity> listarCategoria() {
		return categoriaRepository.findAll();
	}

}
