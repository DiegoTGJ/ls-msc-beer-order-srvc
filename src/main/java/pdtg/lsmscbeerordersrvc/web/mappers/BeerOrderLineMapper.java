package pdtg.lsmscbeerordersrvc.web.mappers;

import org.mapstruct.Mapper;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderLine;
import pdtg.lsmscbeerordersrvc.web.model.BeerOrderLineDto;

@Mapper(uses = {DateMapper.class})
public interface BeerOrderLineMapper {
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
}
