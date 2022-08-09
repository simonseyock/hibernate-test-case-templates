package org.hibernate.bugs;

import org.hibernate.bugs.entities.EntityA;
import org.hibernate.bugs.entities.EntityB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

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
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		// set up data

		entityManager.getTransaction().begin();

		EntityA a1 = new EntityA();

		EntityB b1 = new EntityB();
		b1.setSomeField("some text");

		a1.getOtherBs().add(b1);

		entityManager.persist(a1);
		entityManager.persist(b1);

		entityManager.getTransaction().commit();

		entityManager.clear();

		// remove one entity from the many-to-many relation, on the owning side

		a1 = entityManager.getReference(EntityA.class, a1.getId());

		b1 = entityManager.getReference(EntityB.class, b1.getId());

		entityManager.getTransaction().begin();

		a1.getOtherBs().remove(b1);

		entityManager.persist(a1);

		entityManager.getTransaction().commit();

		entityManager.clear();

		// verify correct entries in audit table

		b1 = entityManager.getReference(EntityB.class, b1.getId());

		List<Object> results = entityManager.createNativeQuery("SELECT someField FROM EntityB_AUD WHERE id = " + b1.getId()).getResultList();

		assert results.size() == 2;
		assert results.get(0).equals("some text");
		assert results.get(1).equals("some text");

		entityManager.close();
	}
}
