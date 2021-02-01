package se.maginteractive.test.controller.store;

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
import se.maginteractive.test.model.Transaction;
import se.maginteractive.test.controller.store.impl.StoreControllerImpl;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.payload.request.PurchaseRequest;
import se.maginteractive.test.service.ProductService;
import se.maginteractive.test.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StoreControllerImpl controller;

    private static final ObjectMapper om = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void findProducts() throws Exception {
        //given
        List<Product> products = List.of(Product.builder()
                .id(1L)
                .name("test")
                .count(100)
                .price(BigDecimal.valueOf(100L))
                .build());

        given(productService.findAll()).willReturn(products);

        //when
        mockMvc.perform(get("/api/v1/store/list"))
                .andExpect(status().isOk());

        //then
        then(productService).should().findAll();
    }

    @Test
    void buy() throws Exception {
        //given
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().accountId(1L).productId(1L).build();

        given(transactionService.buyProductByAccountIdAndProductId(1L, 1L)).willReturn(new Transaction());

        //when
        mockMvc.perform(post("/api/v1/store/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(purchaseRequest)))
                .andExpect(status().isOk());

        //then
        then(transactionService).should().buyProductByAccountIdAndProductId(1L, 1L);
    }
}