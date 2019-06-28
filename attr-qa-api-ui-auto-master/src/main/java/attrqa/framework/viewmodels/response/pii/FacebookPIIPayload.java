package attrqa.framework.viewmodels.response.pii;

import java.util.List;

/**
 * view model to map json response for GET BatchAPIResponse
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class FacebookPIIPayload {
  List<String> schema;
  List<List<String>> data;

  public List<String> getSchema() {
    return schema;
  }

  public List<List<String>> getData() {
    return data;
  }
}
