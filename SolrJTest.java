import java.io.*;
import java.util.*;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrServerException;

public class SolrJTest {
	public static void main(String[] args) throws IOException, SolrServerException {
		String url = "http://192.168.1.102:8080/solr-example";
		HttpSolrServer server = new HttpSolrServer(url);
		
		server.deleteByQuery("*:*");
		SolrInputDocument doc1 = new SolrInputDocument();
		doc1.addField( "id", "SOLR1000", 1.0f );
		doc1.addField( "name", "Solr, the Enterprise Search Server", 1.0f );
		doc1.addField( "manu", "Apache Software Foundation", 1.0f );
		doc1.addField( "cat", "Software", 1.0f );
		doc1.addField( "cat", "Search", 1.0f );
		doc1.addField( "features", "Advanced Full-Text Search Capabilities using Lucene", 1.0f );
		doc1.addField( "features", "Optimized for High Volume Web Traffic", 1.0f );
		doc1.addField( "features", "Standards Based Open Interfaces - XML and HTTP", 1.0f );
		doc1.addField( "features", "Comprehensive HTML Administration Interfaces", 1.0f );
		doc1.addField( "features", "Scalability - Efficient Replication to other Solr Search Servers", 1.0f );
		doc1.addField( "features", "Flexible and Adaptable with XML configuration and Schema", 1.0f );
		doc1.addField( "features", "Good unicode support: h&#xE9;llo (hello with an accent over the e)", 1.0f );
		doc1.addField( "price", 0 );
		doc1.addField( "popularity", 10 );
		doc1.addField( "inStock", true );

		SolrInputDocument doc2 = new SolrInputDocument();
		doc2.addField( "id", "3007WFP", 1.0f );
		doc2.addField( "name", "Dell Widescreen UltraSharp 3007WFP", 1.0f );
		doc2.addField( "manu", "Dell, Inc.", 1.0f );
		doc2.addField( "manu_id_s", "dell", 1.0f );
		doc2.addField( "cat", "electronics", 1.0f );
		doc2.addField( "cat", "monitor", 1.0f );
		doc2.addField( "features", "30\" TFT active matrix LCD, 2560 x 1600, .25mm dot pitch, 700:1 contrast", 1.0f );
		doc2.addField( "includes", "USB cable", 1.0f );
		doc2.addField( "weight", 401.6 );
		doc2.addField( "price", 2199 );
		doc2.addField( "popularity", 6 );
		doc2.addField( "inStock", true );
		doc2.addField( "store", 43.17614,-90.57341 );

		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		docs.add( doc1 );
		docs.add( doc2 );

		server.add( docs );

		server.commit();
	}
}