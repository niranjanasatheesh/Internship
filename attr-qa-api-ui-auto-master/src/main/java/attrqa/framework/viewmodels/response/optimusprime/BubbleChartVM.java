package attrqa.framework.viewmodels.response.optimusprime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

/**
 * view model to map json response for GET Bubble Chart API
 *
 *
 * @author  Johan Varghese
 * @version 1.0
 * @since   2019-05-27
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class BubbleChartVM {

  Meta meta;
  Data[] data;

}
