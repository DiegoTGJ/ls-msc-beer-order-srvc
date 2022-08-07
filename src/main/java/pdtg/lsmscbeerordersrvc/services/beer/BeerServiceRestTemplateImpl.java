package pdtg.lsmscbeerordersrvc.services.beer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pdtg.lsmscbeerordersrvc.services.beer.model.BeerDto;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Diego T. 06-08-2022
 */
@ConfigurationProperties(prefix = "pdtg.brewery", ignoreUnknownFields = false)
@Component
@Slf4j
public class BeerServiceRestTemplateImpl implements BeerService{
    private final RestTemplate restTemplate;

    private String beerServiceHost;
    private final String BEER_SERVICE_PATH = "/api/v1/beer/";
    private final String BEER_SERVICE_UPC_PATH = "/api/v1/beer/beerUpc/";


    public void setBeerServiceHost(String beerServiceHost){
        this.beerServiceHost = beerServiceHost;
    }
    public BeerServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {
        log.debug("Calling Beer Service");

        return Optional.ofNullable(restTemplate.getForObject(beerServiceHost+BEER_SERVICE_UPC_PATH+upc,BeerDto.class));
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID beerId) {
        return Optional.ofNullable(restTemplate.getForObject(beerServiceHost+BEER_SERVICE_PATH+beerId,BeerDto.class));
    }

}
