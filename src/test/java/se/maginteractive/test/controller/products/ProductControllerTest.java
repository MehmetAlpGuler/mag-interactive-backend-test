package se.maginteractive.test.controller.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.maginteractive.test.controller.products.impl.ProductControllerImpl;
import se.maginteractive.test.payload.ProductDto;
import se.maginteractive.test.payload.request.ProductDeleteRequest;
import se.maginteractive.test.payload.request.ProductRequest;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductControllerImpl controller;

    private static final ObjectMapper om = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findAll() throws Exception {
        //given
        List<Product> products = List.of(Product.builder()
                .id(1L)
                .name("test")
                .count(100)
                .price(BigDecimal.valueOf(100L))
                .build());

        given(productService.findAll()).willReturn(products);

        //when
        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk());

        //then
        then(productService).should().findAll();
    }

    @Test
    void findById() throws Exception {
        //given
        given(productService.findById(1L)).willReturn(Optional.of(new Product()));

        //when
        mockMvc.perform(get("/product/{id}", 1L))
                .andExpect(status().isOk());

        //then
        then(productService).should().findById(1L);
    }

    @Test
    void create() throws Exception {
        //given
        Product product = Product.builder()
                .name("test")
                .count(100)
                .price(BigDecimal.valueOf(100L))
                .build();

        ProductRequest productRequest = ProductRequest.builder()
                .product(modelMapper.map(product, ProductDto.class))
                .build();

        given(productService.create(any())).willReturn(product);

        //when
        mockMvc.perform(post("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());

        //then
        then(productService).should().create(any());
    }

    @Test
    void update() throws Exception {
        //given
        Product product = Product.builder()
                .id(1L)
                .name("test")
                .count(100)
                .price(BigDecimal.valueOf(100L))
                .build();

        ProductDto productDto = ProductDto.builder()
                .id(1L)
                .name("test")
                .count(100)
                .price(BigDecimal.valueOf(100L))
                .build();

        ProductRequest productRequest = ProductRequest.builder()
                .product(productDto)
                .build();

        given(productService.update(anyLong(), any())).willReturn(product);

        //when
        mockMvc.perform(post("/product/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(productRequest)))
                .andExpect(status().isCreated());

        //then
        then(productService).should().update(anyLong(), any());
    }

    @Test
    void delete() throws Exception {
        //given
        Product product = Product.builder()
                .id(1L)
                .name("test")
                .count(100)
                .price(BigDecimal.valueOf(100L))
                .build();

        ProductDeleteRequest productDeleteRequest = ProductDeleteRequest.builder().id(1L).build();

        given(productService.deleteById(anyLong())).willReturn(product);

        //when
        mockMvc.perform(post("/product/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(productDeleteRequest)))
                .andExpect(status().isOk());

        //then
        then(productService).should().deleteById(anyLong());
    }
}