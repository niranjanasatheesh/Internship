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
public class Radius {



  @JsonProperty("0")
  String three;
  @JsonProperty("1")
  String four;
  @JsonProperty("2")
  String five;
}
