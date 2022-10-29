package ru.gb;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.gb.api.ProductService;
import ru.gb.dto.Product;
import ru.gb.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CreateProductTest {

    private static ProductService productService;
    private static Product product = null;
    private static Faker faker = new Faker();
    private static int id = -1;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    @Test
    void createProductInFoodCategoryTest() throws IOException {
        Response<Product> response = productService.createProduct(product)
                .execute();
        assert response.body() != null;
        id =  response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    @Test
    void getProductByIdInFoodCategoryTest() throws IOException {
        Response<Product> response = productService.createProduct(product)
                .execute();
        assert response.body() != null;
        id =  response.body().getId();
        response = productService.getProductById(id)
                .execute();
        int id1 =  response.body().getId();
        assertTrue(id == id1);
    }

    @Test
    void modifyProductInFoodCategoryTest() throws IOException {
        Response<Product> response = productService.createProduct(product)
                .execute();
        assert response.body() != null;
        id =  response.body().getId();
        Product new_product = new Product(id, product.getTitle(), 11000, product.getCategoryTitle());
        response = productService.modifyProduct(new_product)
                .execute();
        int price =  response.body().getPrice();
        assertTrue(price == 11000);
    }

    @Test
    void getProductsInFoodCategoryTest() throws IOException {
        Response<Product> response = productService.createProduct(product)
                .execute();
        id =  response.body().getId();
        Response<ResponseBody> productList = productService.getProducts()
                .execute();
        assertTrue(productList.isSuccessful());
    }

    @Test
    void deleteProductInFoodCategoryTest() throws IOException {
        Response<Product> response = productService.createProduct(product)
                .execute();
        id =  response.body().getId();
        Response<ResponseBody> deleteResponse = productService.deleteProduct(id)
                .execute();
        assertTrue(deleteResponse.isSuccessful());
        response = productService.createProduct(product)
                .execute();
        id =  response.body().getId();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }



}
