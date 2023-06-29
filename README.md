@Override
	@Transactional
	public long getTotalOrders() {
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




package eStoreProduct.controller.admin;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eStoreProduct.DAO.common.OrderDAO;
import eStoreProduct.model.admin.entities.orderModel;

@Controller
public class adminOrderController {

	private OrderDAO od;
	private orderModel om;
	private static final Logger logger = LoggerFactory.getLogger(adminOrderController.class);

	@Autowired
	adminOrderController(OrderDAO ord, orderModel omd) {
		od = ord;
		om = omd;
	}



	
	@GetMapping("/listOrders")
	public String showOrders(Model model, @RequestParam(defaultValue = "0") int page) {
	    logger.info("adminOrderController url: listOrders returns: orderList.jsp ");

	    int pageSize = 5; // Number of records per page
	    List<orderModel> orders = od.getOrders(page, pageSize);
	    int totalPages = (int) Math.ceil(od.getTotalOrders() / (double) pageSize); // Calculate total pages
	    model.addAttribute("orders", orders);
	    model.addAttribute("page", page);
	    model.addAttribute("totalPages", totalPages); // Add totalPages to the model

	    return "orderList";
	}

	

	@GetMapping("/processOrders")
	public String processOrders(@RequestParam(value = "orderId") long orderId,
			@RequestParam(value = "adminId") int adminId, @RequestParam(defaultValue = "0") int page, Model model) {
		logger.info("adminOrderController  url: processOrders  returns: filteredOrderList.jsp ");

		System.out.println("procvessing");
		System.out.println(orderId + "" + adminId);
		od.updateOrderProcessedBy(orderId, adminId);
		 int pageSize = 5; // Number of records per page
		    List<orderModel> orders = od.getOrders(page, pageSize);
		model.addAttribute("orders", orders);
		return "filteredOrderList";
	}

	@GetMapping("/loadOrdersByDate")
	public String loadOrders(@RequestParam(value = "selectDateFilter") String selectDateFilter,@RequestParam(defaultValue = "0") int page,  Model model) {

		LocalDateTime currentDate = LocalDateTime.now();
		LocalDateTime startDate = null;
		LocalDateTime endDate = null;

		if (selectDateFilter.equals("daily")) {
			// Set the start and end date for daily filter
			startDate = currentDate.withHour(0).withMinute(0).withSecond(0);
			endDate = currentDate.withHour(23).withMinute(59).withSecond(59);
		} else if (selectDateFilter.equals("weekly")) {
			// Set the start and end date for weekly filter (assuming a week starts on Monday)
			startDate = currentDate.withHour(0).withMinute(0).withSecond(0)
					.minusDays(currentDate.getDayOfWeek().getValue() - 1);
			endDate = startDate.plusDays(6).withHour(23).withMinute(59).withSecond(59);
		} else if (selectDateFilter.equals("monthly")) {
			// Set the start and end date for monthly filter
			startDate = currentDate.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
			endDate = startDate.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
		} else {
			// No filter selected, load all orders
			logger.info("adminOrderController  url: loadOrdersByDate  returns: orderList.jsp ");

			 int pageSize = 5; // Number of records per page
			    List<orderModel> orders = od.getOrders(page, pageSize);
			model.addAttribute("orders", orders);
			return "orderList";
		}
		logger.info("adminOrderController  url: loadOrdersByDate  returns: filteredOrderList.jsp ");

		List<orderModel> orders = od.loadOrdersByDate(Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
		model.addAttribute("orders", orders);
		return "filteredOrderList";
	}

}






=---------------------------------------
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order List</title>
    <link rel="stylesheet" type="text/css" href="./css/orderList.css">
    <script src="./js/orderList.js"></script>
</head>
<body>

<div class="container mt-5">
    <h2>Order List</h2>
    <div class="filters">
        <label for="dateRangeFilter">Date Range Filter:</label>
        <select id="dateRangeFilter" onchange="changeByDate()">
            <option value="All">All</option>
            <option id="daily" value="daily">Daily</option>
            <option id="weekly" value="weekly">Weekly</option>
            <option id="monthly" value="monthly">Monthly</option>
        </select>
        <label for="processedStatusFilter">Processed Status Filter:</label>
        <select id="processedStatusFilter" onchange="processedStatusFilter()">
            <option value="">All</option>
            <option id="processed" value="processed">Processed</option>
            <option id="unprocessed" value="unprocessed">Unprocessed</option>
        </select>
    </div>
</div>

<div class="container mt-5">
    <table id="tableData" class="table table-bordered table-hover">
        <thead class="thead-dark">
            <tr>
                <th>Order ID</th>
                <th>Customer ID</th>
                <th>Bill Number</th>
                <th>Order Date</th>
                <th>Total</th>
                <th>GST</th>
                <th>Payment Reference</th>
                <th>Payment Mode</th>
                <th>Payment Status</th>
                <th>Shipment Status</th>
                <th>Shipping Address</th>
                <th>Shipping Pincode</th>
                <th>Shipment Date</th>
                <th>Processed By</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orders}">
            <tr>
                <td>${order.id}</td>
                <td>${order.ordr_cust_id}</td>
                <td>${order.billNumber}</td>
                <td>${order.orderDate}</td>
                <td>${order.total}</td>
                <td>${order.gst}</td>
                <td>${order.paymentReference}</td>
                <td>${order.paymentMode}</td>
                <td>${order.paymentStatus}</td>
                <td>${order.shipment_status}</td>
                <td>${order.shippingAddress}</td>
                <td>${order.shippingPincode}</td>
                <td>${order.shipmentDate}</td>
                <td>
                    <c:choose>
                        <c:when test="${empty order.ordr_processedby}">
                            <button id="red-button" class="btn btn-danger" data-order-id="${order.id}">Unprocessed</button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn ${order.ordr_processedby == null ? 'btn-danger' : 'btn-success'}" data-order-id="${order.id}">${order.ordr_processedby}</button>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <div class="pagination">
    <c:if test="${page > 0}">
        <a href="javascript:navigateToPage(${page - 1})">Previous</a>
    </c:if>
    
    <c:forEach var="pageNumber" begin="0" end="${totalPages - 1}">
        <c:choose>
            <c:when test="${pageNumber == page}">
                <span class="current-page">${pageNumber + 1}</span>
            </c:when>
            <c:otherwise>
                <a href="javascript:navigateToPage(${pageNumber})">${pageNumber + 1}</a>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    
    <c:if test="${page < totalPages - 1}">
        <a href="javascript:navigateToPage(${page + 1})">Next</a>
    </c:if>
</div>


</div>

</body>
</html>
------------------------------------------------------------------------------------

function navigateToPage(page) {
    window.location.href = "/listOrders?page=" + page;
}

---------------------------------------------------------------
@charset "ISO-8859-1";
.container {
    text-align: center;
}

.filters, #tableData {
    margin: 0 auto;
}

th, td {
    text-align: center;
}

.pagination {
    text-align: center;
    margin-top: 20px;
}

.current-page {
    font-weight: bold;
}
--------------------------------------------------------------------------------
