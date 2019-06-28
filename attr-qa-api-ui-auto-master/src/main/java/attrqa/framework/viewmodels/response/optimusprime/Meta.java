package attrqa.framework.viewmodels.response.optimusprime;

import lombok.Getter;

import java.util.List;
/**
 * view model to map json response for GET Bubble Chart API
 *
 *
 * @author  Johan Varghese
 * @version 1.0
 * @since   2019-05-27
 */
@Getter
public class Meta {

  String x;
  List<String> y;
  List<String> radius;
  List<String> gradient;
  Metrics[] metrics;
}
