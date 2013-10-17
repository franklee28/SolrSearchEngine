import java.io.*;
import java.util.*;
import java.util.regex.*;

import org.apache.tika.*;
import org.apache.tika.parser.*;
import org.apache.tika.parser.pdf.*;
import org.apache.tika.exception.*;
import org.apache.tika.sax.*;
import org.apache.tika.metadata.*;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.apache.log4j.*;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.ContentStreamBase.FileStream;
import org.apache.solr.common.util.ContentStreamBase;

public class SolrIndex {
	private GeoNameReader gnr;
	PrintWriter logfile;
	Date timestamp;
	int numFiles,numFilesWLoc;

	public static void main(String[] args) { 
		SolrIndex instance = new SolrIndex();
		instance.run(args[0]);
	}
	
	public SolrIndex() {
		gnr = new GeoNameReader();
		numFiles = 0;
		numFilesWLoc = 0;
		timestamp = new Date();
		//gnr.specificOutput("Kainamanu");
		try {
			logfile = new PrintWriter("log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void finalize() throws Throwable {
		try {
			logfile.close();
	    } finally {
	        super.finalize();
	    }
	}
	
	private void run(String dirName) {
		String openDir = "./" + dirName;
		File pdfdir = new File(openDir);
		File[] pdfs = pdfdir.listFiles(new pdfFilenameFilter());
		for (File pdf:pdfs) {
			numFiles++;
			String filename = pdf.getName();
			String content = parsePDF(pdf);
			try {
				Location location = gnr.getGeoName(content);
				if (location != null) {
					numFilesWLoc++;
					updateLog(filename, location);
					//index
					indexFilesToSolr(pdf, location);
				}
				else {
					updateLog(filename);
					//index
					indexFilesToSolr(pdf, location);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			logfile.println(timestamp + " -- No of files processed: " + numFiles);
			logfile.println(timestamp + " -- No of files with location: " + numFilesWLoc);
			logfile.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String parsePDF(File f) {
		InputStream in = null;
		String content = null;
		//boolean hasKeyword = false;
		
		try {
			Parser parser = new PDFParser(); //Use the pdf parser to parse the text
			in = new BufferedInputStream(new FileInputStream(f));
			ContentHandler handler = new BodyContentHandler(10000000); //Set a large internal string buffer to handle the parsed text
			Metadata meta = new Metadata();
			meta.set(Metadata.RESOURCE_NAME_KEY, f.getName());
			
			parser.parse(in, handler, meta, new ParseContext()); //Parse the file and store the content into the handler
			
			content = handler.toString(); //Get the content from the handler
			System.out.println(content);
		}
		catch(IOException e) {  
			e.printStackTrace();  
		}
		catch(SAXException e) {  
			e.printStackTrace();  
		}
		catch(TikaException e) {  
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return content;
	}
	
	private void indexFilesToSolr(File f, Location location) throws IOException, SolrServerException {
		String url = "http://192.168.1.102:8080/solr-example"; 
		HttpSolrServer server = new HttpSolrServer(url);
		//server.deleteByQuery("*:*");
		
		String loc = null;
		String solrId = f.getName();
		//System.out.println(solrId);
		if (location != null) {
			loc = location.getLatitude() + "," + location.getLongitude();
		}

		ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
		
		up.addFile(f, "application/pdf");
		up.setParam("stream.type", "application/pdf");
		up.setParam("literal.id", solrId);
		up.setParam("uprefix", "attr_");
		//up.setParam("fmap.attr_stream_content_type", "content_type");
		up.setParam("literal.loc_p", loc);
		//up.setParam("extractOnly", "true");
		
		up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
		
		server.request(up);
	}
	
	private void updateLog(String filename, Location location) {
		timestamp.setTime(System.currentTimeMillis());
		logfile.println(timestamp + " -- \"" + filename +"\" : " + location.getLatitude() + "," + location.getLongitude());
		logfile.flush();
	}
	
	private void updateLog(String filename) {
		timestamp.setTime(System.currentTimeMillis());
		logfile.println(timestamp + " -- \"" + filename +"\" : No location found");
		logfile.flush();
	}
	
	static class pdfFilenameFilter implements FilenameFilter {
		private Pattern p = Pattern.compile(".*\\.pdf",Pattern.CASE_INSENSITIVE);
		public boolean accept(File dir, String name) {
			Matcher m = p.matcher(name);
			return m.matches();
		}
	}
}