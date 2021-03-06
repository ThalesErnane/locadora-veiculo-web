package com.algaworks.curso.jpa2.criteria;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.algaworks.curso.jpa2.modelo.Aluguel;
import com.algaworks.curso.jpa2.modelo.Carro;
import com.algaworks.curso.jpa2.modelo.ModeloCarro;

public class ExemplosCriteria {

	private static EntityManagerFactory factory;

	private EntityManager manager;

	@BeforeClass
	public static void init() {
		factory = Persistence.createEntityManagerFactory("locadoraVeiculoPU");

	}

	@Before
	public void setUP() {
		this.manager = factory.createEntityManager();
	}

	@After
	public void tearDown() {
		this.manager.close();
	}

	@Test
	public void projecoes() {
		// trazendo apenas um atributo do obj
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<String> criteriaQuery = builder.createQuery(String.class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		criteriaQuery.select(carro.<String>get("placa"));

		TypedQuery<String> query = manager.createQuery(criteriaQuery);
		java.util.List<String> placas = query.getResultList();

		for (String placa : placas) {
			System.out.println(placa);
		}
	}

	@Test
	public void funcoesDeAgregacao() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		// builder.max
		// builder.sum
		// builder.min

		CriteriaQuery<BigDecimal> criteriaQuery = builder.createQuery(BigDecimal.class);
		Root<Aluguel> aluguel = criteriaQuery.from(Aluguel.class);
		// select sum(avalorTotal) from Aluguel a
		criteriaQuery.select(builder.sum(aluguel.<BigDecimal>get("valorTotal")));

		TypedQuery<BigDecimal> query = manager.createQuery(criteriaQuery);
		BigDecimal total = query.getSingleResult();

		System.out.println("Soma de todos os alugueis: " + total);
	}

	@Test
	public void resultadoComplexo() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		criteriaQuery.multiselect(carro.get("placa"), carro.get("valorDiaria"));

		TypedQuery<Object[]> query = manager.createQuery(criteriaQuery);
		java.util.List<Object[]> resultado = query.getResultList();

		for (Object[] valores : resultado) {
			System.out.println("" + valores[0] + " - " + valores[1]);

		}
	}

	@Test
	public void resultadoTupla() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Tuple> criteriaQuery = builder.createTupleQuery();

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		criteriaQuery.multiselect(carro.get("placa").alias("placaCarro"), carro.get("valorDiaria").alias("valorCarro"));

		TypedQuery<Tuple> query = manager.createQuery(criteriaQuery);
		java.util.List<Tuple> resultado = query.getResultList();

		for (Tuple tuple : resultado) {
			System.out.println(tuple.get("placaCarro") + " - " + tuple.get("valorCarro"));
		}
	}

	@Test
	public void resultadoConstrutores() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<PrecoCarro> criteriaQuery = builder.createQuery(PrecoCarro.class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		criteriaQuery.select(builder.construct(PrecoCarro.class, carro.get("placa"), carro.get("valorDiaria")));

		TypedQuery<PrecoCarro> query = manager.createQuery(criteriaQuery);
		java.util.List<PrecoCarro> resultado = query.getResultList();

		for (PrecoCarro precoCarro : resultado) {
			System.out.println(precoCarro.getPlaca() + " - " + precoCarro.getValor());
		}

	}

	@Test
	public void exemploFuncao() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Carro> criteriaQuery = builder.createQuery(Carro.class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		javax.persistence.criteria.Predicate predicate = builder.equal(builder.upper(carro.<String>get("cor")),
				"amarelo".toUpperCase());

		criteriaQuery.select(carro);
		criteriaQuery.where(predicate);

		TypedQuery<Carro> query = manager.createQuery(criteriaQuery);
		java.util.List<Carro> carros = query.getResultList();

		for (Carro c : carros) {
			System.out.println(c.getPlaca() + " - " + c.getCor());
		}

	}

	@Test
	public void exemploOrdenacao() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Carro> criteriaQuery = builder.createQuery(Carro.class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		Order order = builder.desc(carro.get("valorDiaria"));

		criteriaQuery.select(carro);
		TypedQuery<Carro> query = manager.createQuery(criteriaQuery);
		List<Carro> carros = query.getResultList();

		for (Carro c : carros) {
			System.out.println(c.getPlaca() + " - " + c.getValorDiaria());
		}

	}

	@Test
	public void exemploJoinEFetch() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Carro> criteriaQuery = builder.createQuery(Carro.class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		Join<Carro, ModeloCarro> modelo = (Join) carro.fetch("modelo");

		criteriaQuery.select(carro);
		criteriaQuery.where(builder.equal(modelo.get("descricao"), "Fit"));

		TypedQuery<Carro> query = manager.createQuery(criteriaQuery);
		List<Carro> carros = query.getResultList();

		for (Carro c : carros) {
			System.out.println(c.getPlaca() + " - " + c.getModelo().getDescricao());
		}
	}

	@Test
	public void mediaDasDiariasDosCarros() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Double> criteriaQuery = builder.createQuery(Double.class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		criteriaQuery.select(builder.avg(carro.<Double>get("valorDiaria")));

		TypedQuery<Double> query = manager.createQuery(criteriaQuery);
		Double total = query.getSingleResult();
		System.out.println("M??dia di??ria: " + total);
	}

	@Test
	public void carrosComValorAcimaDaMedia() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Carro> criteriaQuery = builder.createQuery(Carro.class);
		Subquery<Double> subQuery = criteriaQuery.subquery(Double.class);

		Root<Carro> carro = criteriaQuery.from(Carro.class);
		Root<Carro> carroSub = subQuery.from(Carro.class);

		subQuery.select(builder.avg(carroSub.<Double>get("valorDiaria")));

		criteriaQuery.select(carro);
		criteriaQuery.where(builder.greaterThanOrEqualTo(carro.<Double>get("valorDiaria"), subQuery));

		TypedQuery<Carro> query = manager.createQuery(criteriaQuery);
		List<Carro> carros = query.getResultList();

		for (Carro c : carros) {
			System.out.println(c.getPlaca() + " - " + c.getValorDiaria());
		}
	}
	
	@Test
	public void exemploMetamodel() {
		// https://qastack.com.br/programming/3037593/how-to-generate-the-jpa-entity-metamodel
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Carro> criteriaQuery = builder.createQuery(Carro.class);
		
		Root<Carro> carro = criteriaQuery.from(Carro.class);
		Join<Carro, ModeloCarro> modelo = (Join) carro.fetch("modelo");
		
		criteriaQuery.select(carro);
		criteriaQuery.where(builder.equal(modelo.get("descriacao"), "Fit"));
		
		TypedQuery<Carro> query = manager.createQuery(criteriaQuery);
		List<Carro> carros = query.getResultList();
		
		for (Carro c : carros) {
			System.out.println(c.getPlaca() + " - " + c.getModelo().getDescricao());
		}
		
	}

}
