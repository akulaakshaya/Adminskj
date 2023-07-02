package eStoreProduct.TestClasses.DAO;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import eStoreProduct.DAO.admin.adminDAOImp;
import eStoreProduct.model.admin.entities.adminModel;
import javax.persistence.NoResultException;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class adminDAOTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private adminDAOImp adminDAO;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAdmin_WhenAdminExists_ShouldReturnAdminModel() {
        // Mock the TypedQuery and adminModel
        TypedQuery<adminModel> typedQueryMock = mock(TypedQuery.class);
        adminModel adminMock = mock(adminModel.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(adminModel.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.setParameter(eq("email"), anyString()))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.setParameter(eq("password"), anyString()))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getSingleResult()).thenReturn(adminMock);

        // Call the method to test
        adminModel result = adminDAO.getAdmin("varmaakula506@gmail.com", "123123");

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(adminModel.class));
        verify(typedQueryMock).setParameter(eq("email"), anyString());
        verify(typedQueryMock).setParameter(eq("password"), anyString());
        verify(typedQueryMock).getSingleResult();

        // Assert the result
        assertNotNull(result);
       assertEquals(result, adminMock);
    }

    @Test
    public void testGetAdmin_WhenAdminDoesNotExist_ShouldReturnNull() {
        // Mock the TypedQuery
        TypedQuery<adminModel> typedQueryMock = mock(TypedQuery.class);

        // Mock the EntityManager behavior
        when(entityManager.createQuery(anyString(), eq(adminModel.class)))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.setParameter(eq("email"), anyString()))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.setParameter(eq("password"), anyString()))
                .thenReturn(typedQueryMock);
        when(typedQueryMock.getSingleResult()).thenThrow(new NoResultException());

        // Call the method to test
        adminModel result = adminDAO.getAdmin("varmaakula506@gmail.com", "123123");

        // Verify the expected interactions
        verify(entityManager).createQuery(anyString(), eq(adminModel.class));
        verify(typedQueryMock).setParameter(eq("email"), anyString());
        verify(typedQueryMock).setParameter(eq("password"), anyString());
        verify(typedQueryMock).getSingleResult();

        // Assert the result
        assertNull(result, "Expected result to be null");
    }

    @Test
    public void testUpdateAdmin_ShouldMergeAdminModel() {
        // Mock the adminModel
        adminModel adminMock = mock(adminModel.class);

        // Call the method to test
        adminDAO.updateAdmin(adminMock);

        // Verify the expected interaction
        verify(entityManager).merge(adminMock);
    }
}
