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
public class Metrics {

  String title;



  String description;
  String metric;
  String type;
  Integer rank;
  String aggregationRule;
  Integer rule;



  Integer decimals;
}
