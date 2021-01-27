package se.maginteractive.test.controller.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.maginteractive.test.controller.store.impl.StoreControllerImplV2;
import se.maginteractive.test.payload.request.PurchaseRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StoreControllerImplV2Test {

    @InjectMocks
    private StoreControllerImplV2 controller;

    private static final ObjectMapper om = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void findProducts() throws Exception {
        mockMvc.perform(get("/api/v2/store/list"))
                .andExpect(status().isOk());
    }

    @Test
    void buy() throws Exception {
        //given
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().accountId(1L).productId(1L).build();

        mockMvc.perform(post("/api/v2/store/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(purchaseRequest)))
                .andExpect(status().isOk());
    }
}