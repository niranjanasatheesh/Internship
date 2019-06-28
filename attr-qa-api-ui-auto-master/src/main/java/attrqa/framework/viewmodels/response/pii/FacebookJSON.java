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
public class FacebookJSON {

  private List<String> schema = null;
  private List<List<String>> data = null;

  public List<String> getSchema() {
    return schema;
  }

  public List<List<String>> getData() {
    return data;
  }
}
