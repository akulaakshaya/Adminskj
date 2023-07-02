package eStoreProduct.TestClasses.DAO;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.admin.OrderValueWiseShippingChargeDAOImpl;
import eStoreProduct.model.admin.entities.OrderValueWiseShippingChargesModel;
import eStoreProduct.model.admin.input.OrderValueWiseShippingChargesInput;
import eStoreProduct.model.admin.output.OrderValueWiseShippingCharge;

public class OrderValueWiseShippingChargesDAOTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private OrderValueWiseShippingChargeDAOImpl chargeDAO;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll_WhenChargesExist_ShouldReturnCharges() {
        // Mocking the TypedQuery and its result
        TypedQuery<OrderValueWiseShippingCharge> query = mock(TypedQuery.class);
        List<OrderValueWiseShippingCharge> expectedCharges = new ArrayList<>();
        expectedCharges.add(new OrderValueWiseShippingCharge(1, 100, 200, 10.0));
        when(entityManager.createQuery(anyString(), eq(OrderValueWiseShippingCharge.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedCharges);

        // Perform the test
        List<OrderValueWiseShippingCharge> actualCharges = chargeDAO.getAll();

        // Verify the result
        Assert.assertEquals(actualCharges, expectedCharges);
        verify(entityManager).createQuery(anyString(), eq(OrderValueWiseShippingCharge.class));
        verify(query).getResultList();
    }

    @Test
    public void testGetAll_WhenExceptionOccurs_ShouldReturnEmptyList() {
        // Mocking the TypedQuery to throw an exception
        TypedQuery<OrderValueWiseShippingCharge> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(OrderValueWiseShippingCharge.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException());

        // Perform the test
        List<OrderValueWiseShippingCharge> actualCharges = chargeDAO.getAll();

        // Verify the result
        Assert.assertTrue(actualCharges.isEmpty());
        verify(entityManager).createQuery(anyString(), eq(OrderValueWiseShippingCharge.class));
        verify(query).getResultList();
    }

    @Test
    public void testAddCharge_ShouldMergeOrderValueWiseShippingChargesModel() {
        // Mocking the OrderValueWiseShippingChargesInput object
        OrderValueWiseShippingChargesInput charge = new OrderValueWiseShippingChargesInput();
        charge.setId(1);
        charge.setFrom(100);
        charge.setTo(200);
        charge.setShippingAmount(10.0);

        // Perform the test
        boolean result = chargeDAO.addCharge(charge);

        // Verify the merge operation
        verify(entityManager).merge(any(OrderValueWiseShippingChargesModel.class));
        Assert.assertTrue(result);
    }

    @Test
    public void testAddCharge_WhenExceptionOccurs_ShouldReturnFalse() {
        // Mocking the OrderValueWiseShippingChargesInput object
        OrderValueWiseShippingChargesInput charge = new OrderValueWiseShippingChargesInput();
        charge.setId(1);
        charge.setFrom(100);
        charge.setTo(200);
        charge.setShippingAmount(10.0);

        // Mocking the EntityManager to throw an exception
        doThrow(new RuntimeException()).when(entityManager).merge(any(OrderValueWiseShippingChargesModel.class));

        // Perform the test
        boolean result = chargeDAO.addCharge(charge);

        // Verify the result
        verify(entityManager).merge(any(OrderValueWiseShippingChargesModel.class));
        Assert.assertFalse(result);
    }

    @Test
    public void testDeleteCharge_WhenChargeExists_ShouldRemoveOrderValueWiseShippingChargesModel() {
        // Mocking the OrderValueWiseShippingChargesInput object
        OrderValueWiseShippingChargesInput charge = new OrderValueWiseShippingChargesInput();
        charge.setId(1);

        // Mocking the EntityManager to return an existing charge
        OrderValueWiseShippingChargesModel existingCharge = mock(OrderValueWiseShippingChargesModel.class);
        when(entityManager.find(eq(OrderValueWiseShippingChargesModel.class), eq(1))).thenReturn(existingCharge);

        // Perform the test
        boolean result = chargeDAO.deleteCharge(charge);

        // Verify the remove operation
        verify(entityManager).remove(eq(existingCharge));
        Assert.assertTrue(result);
    }

    @Test
    public void testDeleteCharge_WhenChargeDoesNotExist_ShouldReturnFalse() {
        // Mocking the OrderValueWiseShippingChargesInput object
        OrderValueWiseShippingChargesInput charge = new OrderValueWiseShippingChargesInput();
        charge.setId(1);

        // Mocking the EntityManager to return null for the charge
        when(entityManager.find(eq(OrderValueWiseShippingChargesModel.class), eq(1))).thenReturn(null);

        // Perform the test
        boolean result = chargeDAO.deleteCharge(charge);

        // Verify the result
        Assert.assertFalse(result);
    }

    @Test
    public void testUpdateCharge_WhenChargeExists_ShouldUpdateOrderValueWiseShippingChargesModel() {
        // Mocking the OrderValueWiseShippingChargesInput object
        OrderValueWiseShippingChargesInput charge = new OrderValueWiseShippingChargesInput();
        charge.setId(1);
        charge.setFrom(100);
        charge.setTo(200);
        charge.setShippingAmount(10.0);

        // Mocking the EntityManager to return an existing charge
        OrderValueWiseShippingChargesModel existingCharge = mock(OrderValueWiseShippingChargesModel.class);
        when(entityManager.find(eq(OrderValueWiseShippingChargesModel.class), eq(1))).thenReturn(existingCharge);

        // Perform the test
        boolean result = chargeDAO.updateCharge(charge);

        // Verify the update operation
        verify(existingCharge).setFrom(eq(100.0d)); // Update the argument type to double
        verify(existingCharge).setTo(eq(200.0d));
        verify(existingCharge).setShippingAmount(eq(10.0));
        verify(entityManager).merge(eq(existingCharge));
        Assert.assertTrue(result);
    }


    @Test
    public void testUpdateCharge_WhenChargeDoesNotExist_ShouldReturnFalse() {
        // Mocking the OrderValueWiseShippingChargesInput object
        OrderValueWiseShippingChargesInput charge = new OrderValueWiseShippingChargesInput();
        charge.setId(1);

        // Mocking the EntityManager to return null for the charge
        when(entityManager.find(eq(OrderValueWiseShippingChargesModel.class), eq(1))).thenReturn(null);

        // Perform the test
        boolean result = chargeDAO.updateCharge(charge);

        // Verify the result
        Assert.assertFalse(result);
    }
}
