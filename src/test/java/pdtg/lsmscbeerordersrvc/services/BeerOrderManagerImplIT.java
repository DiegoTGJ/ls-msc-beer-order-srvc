package pdtg.lsmscbeerordersrvc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pdtg.ls.brewery.model.BeerDto;
import pdtg.lsmscbeerordersrvc.domain.BeerOrder;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderLine;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderStatusEnum;
import pdtg.lsmscbeerordersrvc.domain.Customer;
import pdtg.lsmscbeerordersrvc.repositories.BeerOrderRepository;
import pdtg.lsmscbeerordersrvc.repositories.CustomerRepository;
import pdtg.lsmscbeerordersrvc.services.beer.BeerServiceRestTemplateImpl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class BeerOrderManagerImplIT {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    WireMockServer wireMockServer;
    Customer testCustomer;

    @Autowired
    ObjectMapper objectMapper;

    UUID beerId = UUID.randomUUID();

    @TestConfiguration
    static class RestTemplateBuilderProvider {
        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer(){
            WireMockServer server = with(wireMockConfig().port(8883));
            server.start();
            return server;
        }
    }
    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.save(Customer.builder()
                .customerName("Test Customer").build());
    }

    @Test
    void newToAllocatedTest()  {
        BeerDto beerDto = BeerDto.builder()
                .id(beerId)
                .beerName("Test name")
                .price(new BigDecimal("12.1"))
                .upc("12345")
                .build();

        BeerOrder beerOrder= createBeerOrder();

        beerOrder.getBeerOrderLines().forEach(beerOrderLine -> {
            try {
                wireMockServer.stubFor(get(BeerServiceRestTemplateImpl.BEER_SERVICE_UPC_PATH+beerOrderLine.getUpc())
                        .willReturn(okJson(objectMapper.writeValueAsString(beerDto))));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        BeerOrder savedBeerOrder = beerOrderManager.newBeerOrder(beerOrder);
        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(beerOrder.getId()).orElse(BeerOrder.builder().build());
            assertEquals(BeerOrderStatusEnum.ALLOCATION_PENDING,foundOrder.getOrderStatus());
        });

        assertNotNull(savedBeerOrder);
        assertEquals(BeerOrderStatusEnum.ALLOCATED, savedBeerOrder.getOrderStatus());
        System.out.println(savedBeerOrder.getOrderStatus());
    }

    private BeerOrder createBeerOrder(){
        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer).build();

        Set<BeerOrderLine> lines = new HashSet<>();
        lines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .upc("12345")
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());

        beerOrder.setBeerOrderLines(lines);
        return beerOrder;
    }
}