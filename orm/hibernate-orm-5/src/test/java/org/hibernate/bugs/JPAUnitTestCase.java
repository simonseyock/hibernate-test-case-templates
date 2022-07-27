package org.hibernate.bugs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.bugs.entities.EntityA;
import org.hibernate.bugs.entities.EntityB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void hhh123Test() throws Exception {

		// a1 - b1, b2
		// a2 - b2, b3

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		EntityA a1 = new EntityA();
		EntityA a2 = new EntityA();

		EntityB b1 = new EntityB();
		EntityB b2 = new EntityB();
		EntityB b3 = new EntityB();

		entityManager.persist(a1);
		entityManager.persist(a2);
		entityManager.persist(b1);
		entityManager.persist(b2);
		entityManager.persist(b3);

		a1.getOthers().add(b1);
		a1.getOthers().add(b2);

		a2.getOthers().add(b2);
		a2.getOthers().add(b3);

		// The following does work if the relations are not audited

//		b1.getOthers().add(a1);
//
//		b2.getOthers().add(a1);
//		b2.getOthers().add(a2);
//
//		b3.getOthers().add(a2);

		entityManager.getTransaction().commit();

		entityManager.clear();

		entityManager.getTransaction().begin();

		a1 = entityManager.getReference(EntityA.class, a1.getId());
		a2 = entityManager.getReference(EntityA.class, a2.getId());

		b1 = entityManager.getReference(EntityB.class, b1.getId());
		b2 = entityManager.getReference(EntityB.class, b2.getId());
		b3 = entityManager.getReference(EntityB.class, b3.getId());

		// a1 - b1, b3
		// a2 - b1, b2

		a1.getOthers().remove(b2);
		a1.getOthers().add(b3);

		a2.getOthers().remove(b3);
		a2.getOthers().add(b1);

		// The following does work if the relations are not audited

//		b2.getOthers().remove(a1);
//
//		b3.getOthers().add(a1);
//		b3.getOthers().remove(a2);

		entityManager.getTransaction().commit();

		entityManager.clear();

		a1 = entityManager.getReference(EntityA.class, a1.getId());
		a2 = entityManager.getReference(EntityA.class, a2.getId());

		b1 = entityManager.getReference(EntityB.class, b1.getId());
		b2 = entityManager.getReference(EntityB.class, b2.getId());
		b3 = entityManager.getReference(EntityB.class, b3.getId());

		assert b1.getOthers().size() == 2;
		assert b1.getOthers().contains(a1);
		assert b1.getOthers().contains(a2);

		assert b2.getOthers().size() == 1;
		assert b2.getOthers().contains(a2);

		assert b3.getOthers().size() == 1;
		assert b3.getOthers().contains(a1);

		entityManager.close();
	}
}
