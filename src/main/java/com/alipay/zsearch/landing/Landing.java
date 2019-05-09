package com.alipay.zsearch.landing;

import com.google.common.collect.ImmutableMap;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;

public class Landing {

    private static Logger logger = LogManager.getLogger(Landing.class);

    final int port = 9999;
    final String server = "zsearch.cloud.alipay.com";
    final String username = "your_username";
    final String password = "your_password";
    final String index = "greeting";

    RestHighLevelClient client;

    RestHighLevelClient client() {
        if (client == null) {
            client = factoryClient();
        }
        return client;
    }

    RestHighLevelClient factoryClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(server, port))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    /**
     * Create Index .
     *
     * @throws IOException
     */
    void createIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );
        Map mapping = ImmutableMap.of("properties",
                ImmutableMap.of(
                        "name", ImmutableMap.of("type", "keyword"),
                        "age", ImmutableMap.of("type", "long")
                )
        );
        request.mapping("_doc", mapping);
        client().indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * Put Docuemnts .
     *
     * @throws IOException
     */
    void bulk() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest(index).type("_doc").source(XContentType.JSON, "name", "Pierre", "age", 18));
        request.add(new IndexRequest(index).type("_doc").source(XContentType.JSON, "name", "Monroe", "age", 23));
        request.add(new IndexRequest(index).type("_doc").source(XContentType.JSON, "name", "Martin", "age", 77));
        request.add(new IndexRequest(index).type("_doc").source(XContentType.JSON, "name", "Joanne", "age", 12));
        request.add(new IndexRequest(index).type("_doc").source(XContentType.JSON, "name", "Gerard", "age", 76));
        BulkResponse bulkResponse = client().bulk(request, RequestOptions.DEFAULT);
        logger.info("bulk indexed [{}] ", bulkResponse.getItems().length);
    }

    void search() throws IOException {
        client().indices().flush(new FlushRequest(index), RequestOptions.DEFAULT);
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.indices(index).source(searchSourceBuilder);
        SearchResponse searchResponse = client().search(searchRequest, RequestOptions.DEFAULT);
        logger.info("search response: {}", searchResponse);
    }

    void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        client().indices().delete(request, RequestOptions.DEFAULT);
    }

    void close() throws IOException {
        client().close();
    }

    public static void main(String[] args) throws IOException {
        Landing landing = new Landing();
        landing.createIndex();
        landing.bulk();
        landing.search();
        landing.deleteIndex();
        landing.close();
    }
}
