package com.algaworks.curso.jpa2.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.algaworks.curso.jpa2.modelo.Fabricante;
import com.algaworks.curso.jpa2.modelo.ModeloCarro;
import com.algaworks.curso.jpa2.service.NegocioException;
import com.algaworks.curso.jpa2.util.jpa.Transactional;

public class ModeloCarroDAO implements Serializable{
	
	@Inject
	private EntityManager em;
	
	public void salvar(ModeloCarro modeloCarro) {
		em.merge(modeloCarro);
	}
	
	public ModeloCarro buscarPeloCodigo(Long codigo) {
		return em.find(ModeloCarro.class, codigo);
	}

	public List<ModeloCarro> buscarTodos() {
		return em.createQuery("from ModeloCarro").getResultList();
	}
	
	@Transactional
	public void excluir(ModeloCarro modeloCarro) throws NegocioException {
		modeloCarro = buscarPeloCodigo(modeloCarro.getCodigo());	
		
		try {
			em.remove(modeloCarro);
			em.flush();
		} catch (PersistenceException e) {
			
			throw new NegocioException("Este modelo não pode ser excluído");
		 
		}
		
	} 

	
}
