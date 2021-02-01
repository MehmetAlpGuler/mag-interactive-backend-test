package se.maginteractive.test.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.maginteractive.test.exception.ResourceNotFoundException;
import se.maginteractive.test.model.Product;
import se.maginteractive.test.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atMost;

@DisplayName("Product Service Tests")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl service;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder().id(1L).price(ZERO).build();
    }

    @DisplayName("Product Find All")
    @Test
    void findAll() {
        //given
        given(productRepository.findAll()).willReturn(List.of(new Product()));

        //when
        List<Product> foundProducts = service.findAll();

        //then
        then(productRepository).should().findAll();
        assertThat(foundProducts).hasSize(1);
    }

    @DisplayName("Product Find By Id")
    @Test
    void findById() {
        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        //when
        Optional<Product> foundProduct = service.findById(anyLong());

        //then
        then(productRepository).should().findById(anyLong());
        assertThat(foundProduct).isPresent();
    }

    @DisplayName("Product Find By Id Not Found")
    @Test
    void findById_not_found() {
        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        Optional<Product> foundProduct = service.findById(anyLong());

        //then
        then(productRepository).should().findById(anyLong());
        assertThat(foundProduct).isNotPresent();
    }

    @DisplayName("Product Create")
    @Test
    void create() {
        //given
        given(productRepository.save(any(Product.class))).willReturn(product);

        //when
        Product savedProduct = service.create(new Product());

        //then
        then(productRepository).should().save(any(Product.class));
        assertThat(savedProduct).isNotNull();
    }

    @DisplayName("Product Update")
    @Test
    void update() {
        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willReturn(product);

        //when
        Product savedProduct = service.update(anyLong(), product);

        //then
        then(productRepository).should().save(any(Product.class));
        assertThat(savedProduct).isNotNull();
    }

    @DisplayName("Product Update Record Not Found")
    @Test
    void update_not_found() {
        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when //then
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(anyLong(), product));
    }

    @DisplayName("Product Delete")
    @Test
    void delete() {
        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        //when
        Product deletedProduct = service.deleteById(anyLong());

        //then
        then(productRepository).should(atMost(2)).delete(deletedProduct);
        assertThat(deletedProduct).isNotNull();
    }

    @DisplayName("Product Delete Record Not Found")
    @Test
    void delete_not_found() {
        //given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        //when //then
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteById(anyLong()));
    }
}