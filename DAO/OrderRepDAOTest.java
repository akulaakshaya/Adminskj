package eStoreProduct.TestClasses.DAO;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.admin.OrderRepDAOImpl;
import eStoreProduct.model.admin.entities.SlamOrderModel;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class OrderRepDAOTest {
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private OrderRepDAOImpl orderRepDAO;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllOrders_WithExistingOrders_ReturnsOrderList() {
        // Mock the query result
        List<SlamOrderModel> expectedOrders = new ArrayList<>();
        SlamOrderModel order1 = new SlamOrderModel();
        SlamOrderModel order2 = new SlamOrderModel();
        expectedOrders.add(order1);
        expectedOrders.add(order2);
        TypedQuery<SlamOrderModel> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT o FROM SlamOrderModel o", SlamOrderModel.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedOrders);

        // Call the method under test
        List<SlamOrderModel> actualOrders = orderRepDAO.getAllOrders();

        // Print actual and expected orders
        System.out.println("Actual Orders: " + actualOrders);
        System.out.println("Expected Orders: " + expectedOrders);

        // Verify the interactions and assertions
        verify(entityManager).createQuery("SELECT o FROM SlamOrderModel o", SlamOrderModel.class);
        verify(query).getResultList();
        Assert.assertEquals(actualOrders, expectedOrders);
    }


    @Test
    public void testGetAllOrders_WithException_ReturnsEmptyList() {
        // Mock the exception
        TypedQuery<SlamOrderModel> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT o FROM SlamOrderModel o", SlamOrderModel.class)).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException("Database connection failed"));

        // Call the method under test
        List<SlamOrderModel> actualOrders = orderRepDAO.getAllOrders();

        // Verify the interactions and assertions
        verify(entityManager).createQuery("SELECT o FROM SlamOrderModel o", SlamOrderModel.class);
        verify(query).getResultList();
        Assert.assertTrue(actualOrders.isEmpty());
    }
}
