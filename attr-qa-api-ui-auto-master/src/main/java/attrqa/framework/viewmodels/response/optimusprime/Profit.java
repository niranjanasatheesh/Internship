package attrqa.framework.viewmodels.response.optimusprime;

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
public class Profit {



  String metric;




  Float optimized;
  Float current;
  Float diffValue;
  Float diffPercent;
}
