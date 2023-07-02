package eStoreProduct.DAO.common;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import eStoreProduct.DAO.admin.CategoryDAOImp;
import eStoreProduct.model.admin.entities.orderModel;

@Component
public class OrderDAOImp implements OrderDAO {

	 private static final Logger logger = LoggerFactory.getLogger(OrderDAOImp.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void insertOrder(orderModel order) {
		entityManager.persist(order);
	}

	@Override
	@Transactional
	public List<orderModel> getAllOrders() {
		logger.info("getting all the orders from the OrderModel entity class");
		Session currentSession = entityManager.unwrap(Session.class);
		CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
		CriteriaQuery<orderModel> criteriaQuery = criteriaBuilder.createQuery(orderModel.class);
		Root<orderModel> root = criteriaQuery.from(orderModel.class);
		criteriaQuery.select(root);

		TypedQuery<orderModel> query = currentSession.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	@Transactional
	public long getTotalOrders() {
		logger.info("getting how many orders are there in orders for creating pagination");
		Session currentSession = entityManager.unwrap(Session.class);
		CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		Root<orderModel> root = countQuery.from(orderModel.class);
		countQuery.select(criteriaBuilder.count(root));

		TypedQuery<Long> query = currentSession.createQuery(countQuery);
		return query.getSingleResult();
	}

	@Override
	@Transactional
	public List<orderModel> getOrders(int page, int pageSize) {
		logger.info("getting records specific to that page no");
		Session currentSession = entityManager.unwrap(Session.class);
		CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
		CriteriaQuery<orderModel> criteriaQuery = criteriaBuilder.createQuery(orderModel.class);
		Root<orderModel> root = criteriaQuery.from(orderModel.class);
		criteriaQuery.select(root);

		TypedQuery<orderModel> query = currentSession.createQuery(criteriaQuery);
		query.setFirstResult(page * pageSize);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	@Override
	@Transactional
	public int getNoOfOrders() {
		logger.info("getting the no of orders for the dashboard display purpose");
		Session currentSession = entityManager.unwrap(Session.class);
		CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<orderModel> root = criteriaQuery.from(orderModel.class);
		criteriaQuery.select(criteriaBuilder.count(root));

		Long count = currentSession.createQuery(criteriaQuery).uniqueResultOptional().orElse(0L);
		return count.intValue();
	}

	@Override
	@Transactional
	public void updateOrderProcessedBy(Long orderId, Integer processedBy) {
		// Retrieve the order entity based on the order ID
		logger.info("updating the order processed by column based on the input admin id");
		orderModel order = entityManager.find(orderModel.class, orderId);

		// Check if the order exists
		if (order != null) {
			// Set the processed by information on the order entity
			order.setOrdr_processedby(processedBy);

			// Save the updated order entity to the database
			entityManager.merge(order);
		}
	}

	@Override
	@Transactional
	public List<orderModel> loadOrdersByDate(Timestamp startDate, Timestamp endDate) {
		logger.info("on clicking filter based on selection retrieving that related orders");
		System.out.println("loading");
		Session currentSession = entityManager.unwrap(Session.class);
		CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
		CriteriaQuery<orderModel> criteriaQuery = criteriaBuilder.createQuery(orderModel.class);
		Root<orderModel> root = criteriaQuery.from(orderModel.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.between(root.get("orderDate"), startDate, endDate));

		TypedQuery<orderModel> query = currentSession.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void updateOrderShipmentStatus(int orderId, String status) {
		// Retrieve the order entity based on the order ID
		logger.info("updating the shipment status of orderProduct based on the orderid");
		orderModel order = entityManager.find(orderModel.class, orderId);

		// Check if the order exists
		if (order != null) {
			// Set the processed by information on the order entity
			order.setShipment_status(status);

			// Save the updated order entity to the database
			entityManager.merge(order);
		}
	}

}