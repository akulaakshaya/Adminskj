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

import eStoreProduct.DAO.admin.RegionDAOImpl;
import eStoreProduct.model.admin.entities.RegionModel;
import eStoreProduct.model.admin.input.Regions;
import eStoreProduct.model.admin.output.RegionsOutput;

public class RegionDAOImplTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private RegionDAOImpl regionDAO;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetRegions_WhenRegionsExist_ShouldReturnRegions() {
        // Mocking the TypedQuery and its result
        TypedQuery<RegionsOutput> query = mock(TypedQuery.class);
        List<RegionsOutput> expectedRegions = new ArrayList<>();
        expectedRegions.add(new RegionsOutput(1, "Region 1", 100, 200, 0, 0));
        when(entityManager.createQuery(anyString(), eq(RegionsOutput.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedRegions);

        // Perform the test
        List<RegionsOutput> actualRegions = regionDAO.getRegions();

        // Verify the result
        Assert.assertEquals(actualRegions, expectedRegions);
        verify(entityManager).createQuery(anyString(), eq(RegionsOutput.class));
        verify(query).getResultList();
    }

    @Test
    public void testGetRegions_WhenExceptionOccurs_ShouldReturnEmptyList() {
        // Mocking the TypedQuery to throw an exception
        TypedQuery<RegionsOutput> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(RegionsOutput.class))).thenReturn(query);
        when(query.getResultList()).thenThrow(new RuntimeException());

        // Perform the test
        List<RegionsOutput> actualRegions = regionDAO.getRegions();

        // Verify the result
        Assert.assertTrue(actualRegions.isEmpty());
        verify(entityManager).createQuery(anyString(), eq(RegionsOutput.class));
        verify(query).getResultList();
    }

    @Test
    public void testAddRegion_ShouldMergeRegionModel() {
        // Mocking the Regions object
        Regions region = new Regions();
        region.setRegionId(1);
        region.setRegionName("Region 1");
        region.setRegionPinFrom(100);
        region.setRegionPinTo(200);
        region.setRegionPriceWaiver(0);
        region.setRegionSurcharge(0);

        // Perform the test
        regionDAO.addRegion(region);

        // Verify the merge operation
        verify(entityManager).merge(any(RegionModel.class));
    }

    @Test
    public void testRemoveRegion_WhenRegionExists_ShouldRemoveRegionModel() {
        // Mocking the EntityManager and RegionModel
        RegionModel region = mock(RegionModel.class);
        when(entityManager.find(eq(RegionModel.class), anyInt())).thenReturn(region);

        // Perform the test
        regionDAO.removeRegion(1);

        // Verify the remove operation
        verify(entityManager).remove(region);
    }

    @Test
    public void testRemoveRegion_WhenRegionDoesNotExist_ShouldNotRemoveRegionModel() {
        // Mocking the EntityManager to return null
        when(entityManager.find(eq(RegionModel.class), anyInt())).thenReturn(null);

        // Perform the test
        regionDAO.removeRegion(1);

        // Verify that remove operation was not called
        verify(entityManager, never()).remove(any(RegionModel.class));
    }
}
