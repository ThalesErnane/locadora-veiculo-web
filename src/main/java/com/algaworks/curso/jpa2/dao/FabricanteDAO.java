package com.algaworks.curso.jpa2.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.algaworks.curso.jpa2.modelo.Fabricante;
import com.algaworks.curso.jpa2.service.NegocioException;
import com.algaworks.curso.jpa2.util.jpa.Transactional;

public class FabricanteDAO implements Serializable {

	@Inject
	private EntityManager em;
	
	public void salvar(Fabricante fabricante) {
		em.merge(fabricante);
	}

	@SuppressWarnings("unchecked")
	public List<Fabricante> buscarTodos() {
		return em.createQuery("from Fabricante").getResultList();
	}

	@Transactional // ao tentar fazer uma alteração no BD, sempre 
	public void excluir(Fabricante fabricante) throws NegocioException{
		 	// Fabricante	fabricanteTemp = em.find(Fabricante.class, fabricante.getCodigo());
			fabricante = buscarPeloCodigo(fabricante.getCodigo());
		 	em.remove(fabricante);
			em.flush();
	}

	public Fabricante buscarPeloCodigo(Long codigo) {
		return em.find(Fabricante.class, codigo);	
	}


}
