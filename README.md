SolrSearchEngine
================

This project completes a search engine which can be used to search the content from pdf files.

The search engine will use Apache Tika to parse the content in the pdf file, calculate the frequency of n-gram phrases (one word, two words, three words), and use a geometry database to find a appropriate coordinates for this file.

The search engine is based on Apache Solr which will index the uploaded files and add the geometry information to the index. Solr will provide the API for use and return the results in JSON format.

The website is built upon the ajax-solr, which is based on JQuery and provide variable API for connecting Solr server and displaying the results.

The hotspot map is achieved by using Google map API, which loads all location information of the search results and create a hotspot map.  