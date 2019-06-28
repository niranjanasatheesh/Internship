package attrqa.framework.viewmodels.response.optimusprime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
/**
 * view model to map json response for GET Bubble Chart API
 *
 *
 * @author  Johan Varghese
 * @version 1.0
 * @since   2019-05-27
 */
@Getter
public class Gradient {

  @JsonProperty("0")
  String six;
  @JsonProperty("1")
  String seven;
  @JsonProperty("2")
  String eight;


}
