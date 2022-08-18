package pdtg.ls.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created by Diego T. 17-08-2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidateOrderResult {
    private UUID orderId;
    private boolean isValid;
}
