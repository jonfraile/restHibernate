package com.ipartek.formacion.ejemplos.perrera.model;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Clase de utilidad para obtener la sesion de hibernate.
 *
 * @author documentacion hibernate
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory;

	static {
		try {

			AnnotationConfiguration config = new AnnotationConfiguration();
			config.addAnnotatedClass(HibernateUtil.class);

			/*
			 * Utilizar uno de los dos!! comentar la linea del que no sirva.
			 * Depende de donde este el fichero de configuracion de Hibernate
			 * "hibernate.cfg.xml"
			 */
			// Use this if config files are in src folder
			// Lo encuentra en la raiz del proyecto porque tenemos dos sources:
			// java y resources
			config.configure();
			// Use this if config files are in a subfolder of src, such as
			// "resources"
			// config.configure("/resources/hibernate.cfg.xml");

			sessionFactory = config.buildSessionFactory();

		} catch (Throwable ex) {
			// Log exception!
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Session getSession() throws HibernateException {
		return sessionFactory.openSession();
	}
}