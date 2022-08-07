package pdtg.lsmscbeerordersrvc.web.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderLine;
import pdtg.lsmscbeerordersrvc.services.beer.BeerService;
import pdtg.lsmscbeerordersrvc.services.beer.model.BeerDto;
import pdtg.lsmscbeerordersrvc.web.model.BeerOrderLineDto;

import java.util.Optional;

/**
 * Created by Diego T. 06-08-2022
 */
public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper{


    private BeerService beerService;
    private BeerOrderLineMapper beerOrderLineMapper;

    @Autowired
    public void setBeerService(BeerService beerService){
        this.beerService = beerService;
    }

    @Autowired
    public void setBeerOrderLineMapper(BeerOrderLineMapper beerOrderLineMapper){
        this.beerOrderLineMapper = beerOrderLineMapper;
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        BeerOrderLineDto orderLineDto = beerOrderLineMapper.beerOrderLineToDto(line);
        Optional<BeerDto> beerDtoOptional = beerService.getBeerByUpc(line.getUpc());

        beerDtoOptional.ifPresent(beerDto -> {
            orderLineDto.setBeerName(beerDto.getBeerName());
            orderLineDto.setBeerStyle(beerDto.getBeerName());
            orderLineDto.setPrice(beerDto.getPrice());
        });

        return orderLineDto;
    }

    @Override
    public BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto) {
        return beerOrderLineMapper.dtoToBeerOrderLine(dto);
    }
}
