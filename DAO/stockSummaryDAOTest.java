package eStoreProduct.TestClasses.DAO;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import eStoreProduct.DAO.admin.stockSummaryDAOImp;
import eStoreProduct.model.admin.entities.productStockModel;
import eStoreProduct.model.admin.entities.productsModel;
import eStoreProduct.model.admin.output.stockSummaryModel;

public class stockSummaryDAOTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private stockSummaryDAOImp stockSummaryDAO;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetStocks_ShouldReturnStockSummaryModels() {
        // Mock the TypedQuery and stockSummaryModel
        TypedQuery<stockSummaryModel> typedQueryMock = mock(TypedQuery.class);
        stockSummaryModel stockSummaryMock = mock(stockSummaryModel.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(stockSummaryModel.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList())
                .thenReturn(Collections.singletonList(stockSummaryMock));

        // Call the method to test
        List<stockSummaryModel> result = stockSummaryDAO.getStocks();

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(stockSummaryModel.class));
        verify(typedQueryMock).getResultList();

        // Assert the result
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), stockSummaryMock);
    }

    @Test
    public void testGetStocks_WhenExceptionThrown_ShouldReturnEmptyList() {
        // Mock the TypedQuery
        TypedQuery<stockSummaryModel> typedQueryMock = mock(TypedQuery.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(stockSummaryModel.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList())
                .thenThrow(new RuntimeException("Test Exception"));

        // Call the method to test
        List<stockSummaryModel> result = stockSummaryDAO.getStocks();

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(stockSummaryModel.class));
        verify(typedQueryMock).getResultList();

        // Assert the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTotalStocks_ShouldReturnCount() {
        // Mock the TypedQuery
        TypedQuery<Long> typedQueryMock = mock(TypedQuery.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getSingleResult())
                .thenReturn(10L);

        // Call the method to test
        long result = stockSummaryDAO.getTotalStocks();

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(typedQueryMock).getSingleResult();

        // Assert the result
        assertEquals(result, 10L);
    }

    @Test
    public void testGetTotalStocks_WhenExceptionThrown_ShouldReturnZero() {
        // Mock the TypedQuery
        TypedQuery<Long> typedQueryMock = mock(TypedQuery.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(Long.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getSingleResult())
                .thenThrow(new RuntimeException("Test Exception"));

        // Call the method to test
        long result = stockSummaryDAO.getTotalStocks();

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(Long.class));
        verify(typedQueryMock).getSingleResult();

        // Assert the result
        assertEquals(result, 0L);
    }

    @Test
    public void testGetStocks_WithPageAndPageSize_ShouldReturnStockSummaryModels() {
        // Mock the TypedQuery and stockSummaryModel
        TypedQuery<stockSummaryModel> typedQueryMock = mock(TypedQuery.class);
        stockSummaryModel stockSummaryMock = mock(stockSummaryModel.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(stockSummaryModel.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList())
                .thenReturn(Collections.singletonList(stockSummaryMock));

        // Call the method to test
        List<stockSummaryModel> result = stockSummaryDAO.getStocks(1, 10);

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(stockSummaryModel.class));
        verify(typedQueryMock).setFirstResult(10);
        verify(typedQueryMock).setMaxResults(10);
        verify(typedQueryMock).getResultList();

        // Assert the result
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0), stockSummaryMock);
    }

    @Test
    public void testGetStocks_WithPageAndPageSize_WhenExceptionThrown_ShouldReturnEmptyList() {
        // Mock the TypedQuery
        TypedQuery<stockSummaryModel> typedQueryMock = mock(TypedQuery.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(stockSummaryModel.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getResultList())
                .thenThrow(new RuntimeException("Test Exception"));

        // Call the method to test
        List<stockSummaryModel> result = stockSummaryDAO.getStocks(1, 10);

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(stockSummaryModel.class));
        verify(typedQueryMock).setFirstResult(10);
        verify(typedQueryMock).setMaxResults(10);
        verify(typedQueryMock).getResultList();

        // Assert the result
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdateStocks_ShouldUpdateProductAndStock() {
        // Mock the product and productStock
        productsModel productMock = mock(productsModel.class);
        productStockModel productStockMock = mock(productStockModel.class);

        // Mock the EntityManager behavior
        when(entityManager.find(eq(productsModel.class), anyInt()))
                .thenReturn(productMock);
        when(entityManager.find(eq(productStockModel.class), anyInt()))
                .thenReturn(productStockMock);

        // Call the method to test
        stockSummaryDAO.updateStocks(1, "imageurl", "desc", 10, 100, 9.99, 19.99);

        // Verify the expected interactions
        verify(entityManager).find(eq(productsModel.class), anyInt());
        verify(entityManager).find(eq(productStockModel.class), anyInt());
        verify(entityManager).merge(eq(productMock));
        verify(entityManager).merge(eq(productStockMock));
    }

    @Test
    public void testUpdateStocks_WhenProductNotFound_ShouldNotUpdateProduct() {
        // Mock the productStock
        productStockModel productStockMock = mock(productStockModel.class);

        // Mock the EntityManager behavior
        when(entityManager.find(eq(productsModel.class), anyInt()))
                .thenReturn(null);
        when(entityManager.find(eq(productStockModel.class), anyInt()))
                .thenReturn(productStockMock);

        // Call the method to test
        stockSummaryDAO.updateStocks(1, "imageurl", "desc", 10, 100, 9.99, 19.99);

        // Verify the expected interactions
        verify(entityManager).find(eq(productsModel.class), anyInt());
        verify(entityManager).find(eq(productStockModel.class), anyInt());
        verify(entityManager, never()).merge(any());
    }

    @Test
    public void testUpdateStocks_WhenStockNotFound_ShouldNotUpdateStock() {
        // Mock the product
        productsModel productMock = mock(productsModel.class);

        // Mock the EntityManager behavior
        when(entityManager.find(eq(productsModel.class), anyInt()))
                .thenReturn(productMock);
        when(entityManager.find(eq(productStockModel.class), anyInt()))
                .thenReturn(null);

        // Call the method to test
        stockSummaryDAO.updateStocks(1, "imageurl", "desc", 10, 100, 9.99, 19.99);

        // Verify the expected interactions
        verify(entityManager).find(eq(productsModel.class), anyInt());
        verify(entityManager).find(eq(productStockModel.class), anyInt());
        verify(entityManager, never()).merge(any()); // Assert that merge is never invoked
    }

   
    
   
    @Test
    public void testUpdateStocks_WhenExceptionThrown_ShouldPrintError() {
        // Mock the EntityManager behavior
        when(entityManager.find(eq(productsModel.class), anyInt()))
                .thenThrow(new RuntimeException("Test Exception"));

        // Call the method to test
        stockSummaryDAO.updateStocks(1, "imageurl", "desc", 10, 100, 9.99, 19.99);

        // Verify the expected interactions
        verify(entityManager).find(eq(productsModel.class), anyInt());
        // Other interactions cannot be verified due to exception handling

        // No need to assert anything since it's just logging and printing messages
    }

    @Test
    public void testUpdatePrice_ShouldUpdateProductStockPrice() {
        // Mock the productStock
        productStockModel productStockMock = mock(productStockModel.class);

        // Mock the EntityManager behavior
        when(entityManager.find(eq(productsModel.class), anyInt()))
                .thenReturn(null);
        when(entityManager.find(eq(productStockModel.class), anyInt()))
                .thenReturn(productStockMock);

        // Call the method to test
        stockSummaryDAO.updatePrice(1, 9.99, 19.99);

        // Verify the expected interactions
        verify(entityManager).find(eq(productsModel.class), anyInt());
        verify(entityManager).find(eq(productStockModel.class), anyInt());
        verify(entityManager).merge(eq(productStockMock));
    }

    @Test
    public void testUpdatePrice_WhenStockNotFound_ShouldNotUpdatePrice() {
        // Mock the EntityManager behavior
        when(entityManager.find(eq(productsModel.class), anyInt()))
                .thenReturn(null);
        when(entityManager.find(eq(productStockModel.class), anyInt()))
                .thenReturn(null);

        // Call the method to test
        stockSummaryDAO.updatePrice(1, 9.99, 19.99);

        // Verify the expected interactions
        verify(entityManager).find(eq(productsModel.class), anyInt());
        verify(entityManager).find(eq(productStockModel.class), anyInt());
        verify(entityManager, never()).merge(any());
    }

    @Test
    public void testUpdatePrice_WhenExceptionThrown_ShouldPrintError() {
        // Mock the EntityManager behavior
        when(entityManager.find(eq(productsModel.class), anyInt()))
                .thenThrow(new RuntimeException("Test Exception"));

        // Call the method to test
        stockSummaryDAO.updatePrice(1, 9.99, 19.99);

        // Verify the expected interactions
        verify(entityManager).find(eq(productsModel.class), anyInt());
        // Other interactions cannot be verified due to exception handling

        // No need to assert anything since it's just logging and printing messages
    }
}
