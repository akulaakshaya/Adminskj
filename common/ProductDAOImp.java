package eStoreProduct.DAO.common;

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
import eStoreProduct.model.admin.entities.productStockModel;
import eStoreProduct.model.admin.entities.productsModel;
import eStoreProduct.model.admin.input.Category;
import eStoreProduct.model.admin.input.Product;
import eStoreProduct.utility.ProductStockPrice;

@Component
public class ProductDAOImp implements ProductDAO {

	 private static final Logger logger = LoggerFactory.getLogger(ProductDAOImp.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public Integer getMaxProductId() {
		String query = "SELECT MAX(p.id) FROM productsModel p";
		TypedQuery<Integer> maxIdQuery = entityManager.createQuery(query, Integer.class);
		Integer maxId = maxIdQuery.getSingleResult();
		return maxId != null ? maxId : 0;
	}

	@Override
	@Transactional
	public int getNoOfProducts() {
		logger.info("getting the number of products for the dashboard purpose");
		Session currentSession = entityManager.unwrap(Session.class);
		CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<productsModel> root = criteriaQuery.from(productsModel.class);
		criteriaQuery.select(criteriaBuilder.count(root));

		Long count = currentSession.createQuery(criteriaQuery).uniqueResultOptional().orElse(0L);
		return count.intValue();
	}

	@Override
	@Transactional
	public boolean createProduct(Product p) {
		logger.info("creating product with the help of Product model");
		int p_id = getMaxProductId();

		p_id = p_id + 1;
		System.out.println(p_id + "product_id\n");
		System.out.println(p.getProd_title() + " " + p.getProd_gstc_id() + " " + p.getProd_brand() + " "
				+ p.getImage_url() + " " + p.getProd_desc() + " " + p.getReorderLevel());

		productsModel productEntity = new productsModel();
		productEntity.setId(p_id);
		productEntity.setTitle(p.getProd_title());
		productEntity.setProductCategory(p.getProd_prct_id());
		productEntity.setHsnCode(p.getProd_gstc_id());
		productEntity.setBrand(p.getProd_brand());

		productEntity.setImageUrl(p.getImage_url());
		productEntity.setDescription(p.getProd_desc());
		productEntity.setReorderLevel(p.getReorderLevel());
		entityManager.merge(productEntity);

		return productEntity.getId() != null;

	}

	@Override
	@Transactional
	public productsModel getProductModelById(int prodid) {
		logger.info("retrieving the product based on id");
		productsModel pm = entityManager.find(productsModel.class, prodid);
		return pm;
	}

	@Override
	@Transactional
	public List<ProductStockPrice> getAllProducts() {
		logger.info("get all the products for the home page to display all the products at a time");
		String query = "SELECT new eStoreProduct.utility.ProductStockPrice(p.id, p.title, p.brand, p.imageUrl, p.description, ps.price)"
				+ " FROM eStoreProduct.model.admin.entities.productsModel p JOIN eStoreProduct.model.admin.entities.productStockModel ps ON p.id = ps.product";
		TypedQuery<ProductStockPrice> typedQuery = entityManager.createQuery(query, ProductStockPrice.class);
		return typedQuery.getResultList();
	}

	@Override
	@Transactional
	public List<Category> getAllCategories() {
		logger.info("for the nav bar dropdown of categories");
		String query = "SELECT new eStoreProduct.model.admin.input.Category(c.id, c.prct_title, c.description)"
				+ " FROM eStoreProduct.model.admin.entities.productCategoryModel c";

		TypedQuery<Category> typedQuery = entityManager.createQuery(query, Category.class);
		return typedQuery.getResultList();
	}

	@Override
	@Transactional
	public List<ProductStockPrice> getProductsByCategory(Integer category_id) {
		logger.info("based on the selection of category dropdown retrieving that category specific products only");
		String query = "SELECT new eStoreProduct.utility.ProductStockPrice(p.id, p.title, p.brand, p.imageUrl, p.description, ps.price)"
				+ " FROM eStoreProduct.model.admin.entities.productsModel p JOIN eStoreProduct.model.admin.entities.productStockModel ps"
				+ " on p.id=ps.product WHERE p.productCategory = :categoryId ";
		TypedQuery<ProductStockPrice> typedQuery = entityManager.createQuery(query, ProductStockPrice.class);
		typedQuery.setParameter("categoryId", category_id);
		return typedQuery.getResultList();
	}

	
	@Override
	@Transactional
	public ProductStockPrice getProductById(Integer productId) {
		logger.info("getting the productStockProce model based on productId");
		productsModel pm = entityManager.find(productsModel.class, productId);
		productStockModel psm = entityManager.find(productStockModel.class, productId);
		ProductStockPrice psp = new ProductStockPrice(pm.getId(), pm.getTitle(), pm.getBrand(), pm.getImageUrl(),
				pm.getDescription(), psm.getPrice());
		return psp;
	}

	@Override
	@Transactional
	public List<String> getAllProductCategories() {
		logger.info("getting al the product categories");
		String query = "SELECT c.prct_title FROM eStoreProduct.model.model.entities.productCategoryModel c";
		TypedQuery<String> typedQuery = entityManager.createQuery(query, String.class);
		return typedQuery.getResultList();
	}

	
	@Override
	@Transactional
	public boolean isPincodeValid(int pincode) {
		logger.info("checking whether the pincode is valid or not for that products");
		String query = "SELECT COUNT(*) FROM Region r WHERE :pincode BETWEEN r.pinFrom AND r.pinTo";
		Integer count = entityManager.createQuery(query, Integer.class).setParameter("pincode", pincode)
				.getSingleResult();
		return count > 0;
	}

	@Override
	public List<ProductStockPrice> filterProductsByPriceRange(double minPrice, double maxPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductStockPrice> sortProductsByPrice(List<ProductStockPrice> productList, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}
}
