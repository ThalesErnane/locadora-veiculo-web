package com.algaworks.curso.jpa2.modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Motorista.class)
public abstract class Motorista_ extends com.algaworks.curso.jpa2.modelo.Pessoa_ {

	public static volatile SingularAttribute<Motorista, String> numeroCNH;
	public static volatile SingularAttribute<Motorista, Sexo> sexo;

	public static final String NUMERO_CN_H = "numeroCNH";
	public static final String SEXO = "sexo";

}

