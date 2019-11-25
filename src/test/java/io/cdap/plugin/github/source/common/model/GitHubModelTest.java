package io.cdap.plugin.github.source.common.model;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.google.common.io.ByteStreams;
import io.cdap.plugin.github.source.common.model.impl.Branch;
import io.cdap.plugin.github.source.common.model.impl.Collaborator;
import io.cdap.plugin.github.source.common.model.impl.Comment;
import io.cdap.plugin.github.source.common.model.impl.Commit;
import io.cdap.plugin.github.source.common.model.impl.Content;
import io.cdap.plugin.github.source.common.model.impl.DeployKey;
import io.cdap.plugin.github.source.common.model.impl.Deployment;
import io.cdap.plugin.github.source.common.model.impl.Fork;
import io.cdap.plugin.github.source.common.model.impl.Invitation;
import io.cdap.plugin.github.source.common.model.impl.Page;
import io.cdap.plugin.github.source.common.model.impl.Release;
import io.cdap.plugin.github.source.common.model.impl.TrafficReferrer;
import io.cdap.plugin.github.source.common.model.impl.Webhook;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GitHubModelTest {

  private Class<?> clazz;
  private String fileName;

  private HttpRequestFactory requestFactory;

  public GitHubModelTest(Class<?> clazz, String fileName) {
    this.clazz = clazz;
    this.fileName = fileName;
  }

  @Before
  public void setUp() {
    requestFactory = new MockHttpTransport() {
      @Override
      public LowLevelHttpRequest buildRequest(String method, String url) {
        return new MockLowLevelHttpRequest() {
          @Override
          public LowLevelHttpResponse execute() throws IOException {
            MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
            response.setContentType(Json.MEDIA_TYPE);
            response.setContent(getFile(fileName));
            return response;
          }
        };
      }
    }.createRequestFactory((HttpRequest request) ->
                             request.setParser(new JsonObjectParser(GsonFactory.getDefaultInstance())));
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {Branch[].class, "branches.json"},
      {Collaborator[].class, "collaborators.json"},
      {Comment[].class, "comments.json"},
      {Commit[].class, "commits.json"},
      {Content[].class, "contents.json"},
      {DeployKey[].class, "deploy_keys.json"},
      {Deployment[].class, "deployments.json"},
      {Fork[].class, "forks.json"},
      {Invitation[].class, "invitations.json"},
      {Page[].class, "pages.json"},
      {Release[].class, "releases.json"},
      {TrafficReferrer[].class, "traffic_referrers.json"},
      {Webhook[].class, "webhooks.json"}
    });
  }

  @Test
  public void validateModelFields() throws IOException {
    HttpRequest request = requestFactory.buildGetRequest(HttpTesting.SIMPLE_GENERIC_URL);
    HttpResponse response = request.execute();

    Object result = response.parseAs(clazz);

    AssertionsForClassTypes.assertThat(result).hasNoNullFieldsOrProperties();
  }

  byte[] getFile(String fileName) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream stream = classLoader.getResourceAsStream(fileName);
    return ByteStreams.toByteArray(stream);
  }
}
