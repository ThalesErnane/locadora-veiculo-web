package com.algaworks.curso.jpa2.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.algaworks.curso.jpa2.dao.CarroDAO;
import com.algaworks.curso.jpa2.modelo.Carro;
import com.algaworks.curso.jpa2.modelolazy.LazyCarroDataModel;
import com.algaworks.curso.jpa2.service.NegocioException;
import com.algaworks.curso.jpa2.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaCarroBean implements Serializable {

	@Inject
	CarroDAO carroDAO;

	private List<Carro> carros = new ArrayList<>();

	private LazyCarroDataModel lazyCarros;

	private Carro carroSelecionado;
	private Carro carroSelecionadoParaExcluir;

	public List<Carro> getCarros() {
		return carros;
	}

	public void excluir() {
		try {
			carroDAO.excluir(carroSelecionadoParaExcluir);
			this.carros.remove(carroSelecionadoParaExcluir); // remove o carro da List
			FacesUtil.addSuccessMessage(
					"Carro placa " + carroSelecionadoParaExcluir.getPlaca() + " excluído com sucesso.");
		} catch (NegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	public Carro getCarroSelecionado() {
		return carroSelecionado;
	}

	public void setCarroSelecionado(Carro carroSelecionado) {
		this.carroSelecionado = carroSelecionado;
	}

	@PostConstruct
	public void inicializar() {
		// carros = carroDAO.buscarTodos();
		lazyCarros = new LazyCarroDataModel(carroDAO);
	}

	public void buscarCarroComAcessorios() {
		carroSelecionado = carroDAO.buscarCarroComAcessorios(carroSelecionado.getCodigo());
	}

	public LazyCarroDataModel getLazyCarros() {
		return lazyCarros;
	}

	public Carro getCarroSelecionadoParaExcluir() {
		return carroSelecionadoParaExcluir;
	}

	public void setCarroSelecionadoParaExcluir(Carro carroSelecionadoParaExcluir) {
		this.carroSelecionadoParaExcluir = carroSelecionadoParaExcluir;
	}

}
