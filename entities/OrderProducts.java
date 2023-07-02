package eStoreProduct.model.customer.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import eStoreProduct.model.admin.entities.productsModel;
import eStoreProduct.model.admin.entities.orderModel;

@Entity
@Table(name = "slam_OrderProducts")
public class OrderProducts implements Serializable
{

	@Id
	@Column(name = "Ordr_id")
	private Integer order_id;

	@Id
	@Column(name = "prod_id")
	private Integer prod_id;
	
	
	@Column(name = "orpr_qty")
	private Integer quantity;

	@Column(name = "orpr_gst")
	private double gst;

	@Column(name = "orpr_price")
	private double price;
	
	@Column(name = "orpr_shipment_status")
	private String shipmentStatus;
	
	@ManyToOne
	@JoinColumn(name = "ordr_id", referencedColumnName = "Ordr_id", insertable = false, updatable = false)
	orderModel ordprd;

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer long1) {
		this.order_id = long1;
	}


	public String getShipmentStatus() {
		return shipmentStatus;
	}

	public void setShipmentStatus(String shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}

	public orderModel getOrdprd() {
		return ordprd;
	}

	public void setOrdprd(orderModel ordprd) {
		this.ordprd = ordprd;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getGst() {
		return gst;
	}

	public void setGst(double gst) {
		this.gst = gst;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	// Getters and setters
	// ...
}