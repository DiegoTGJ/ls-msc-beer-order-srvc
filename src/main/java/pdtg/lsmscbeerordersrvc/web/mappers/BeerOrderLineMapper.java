package pdtg.lsmscbeerordersrvc.web.mappers;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import pdtg.lsmscbeerordersrvc.domain.BeerOrderLine;
import pdtg.ls.brewery.model.BeerOrderLineDto;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(BeerOrderLineMapperDecorator.class)
public interface BeerOrderLineMapper {
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
}
