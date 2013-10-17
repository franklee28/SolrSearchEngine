import java.io.*;
import java.util.*;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.NamedList;

/**
 * @author EDaniel
 */
public class SolrJPDFTest {

  public static void main(String[] args) {
    try {
      //Solr cell can also index MS file (2003 version and 2007 version) types.
      String fileName = "./test/ufo9.pdf"; 
      //this will be unique Id used by Solr to index the file contents.
      String solrId = "Sample.pdf"; 
      
      indexFilesSolrCell(fileName, solrId);
      
    } catch (Exception ex) {
      System.out.println(ex.toString());
    }
  }
  
  /**
   * Method to index all types of files into Solr. 
   * @param fileName
   * @param solrId
   * @throws IOException
   * @throws SolrServerException
   */
  public static void indexFilesSolrCell(String fileName, String solrId) throws IOException, SolrServerException {
    
    String url = "http://192.168.1.102:8080/solr-example"; 
    HttpSolrServer server = new HttpSolrServer(url);
	server.deleteByQuery("*:*");

    ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
    
    up.addFile(new File(fileName), "application/pdf");
    
    up.setParam("literal.id", solrId);
    up.setParam("uprefix", "attr_");
	up.setParam("stream.type", "application/pdf");
    //up.setParam("fmap.content", "attr_content");
	up.setParam("literal.loc_p", "42.36, -71.07");
    //up.setParam("extractOnly", "true");

    up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
    
	server.request(up);

    //QueryResponse rsp = server.query(new SolrQuery("*:*"));
    
    //System.out.println(rsp);
  }
}
