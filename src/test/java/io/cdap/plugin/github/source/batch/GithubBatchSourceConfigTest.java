package io.cdap.plugin.github.source.batch;

import io.cdap.cdap.etl.api.validation.ValidationFailure;
import io.cdap.cdap.etl.mock.validation.MockFailureCollector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static io.cdap.plugin.github.source.batch.GithubBatchSourceConfig.AUTHORIZATION_TOKEN;
import static io.cdap.plugin.github.source.batch.GithubBatchSourceConfig.DATASET_NAME;
import static io.cdap.plugin.github.source.batch.GithubBatchSourceConfig.REPOSITORY_NAME;
import static io.cdap.plugin.github.source.batch.GithubBatchSourceConfig.REPOSITORY_OWNER;

public class GithubBatchSourceConfigTest {

  private MockFailureCollector failureCollector;

  @Before
  public void setUp() {
    failureCollector = new MockFailureCollector();
  }

  @Test
  public void testValidateFieldsCaseCorrectFields() {
    //given
    GithubBatchSourceConfig config = new GithubBatchSourceConfig("ref");
    config.authorizationToken = "token";
    config.repoOwner = "owner";
    config.repoName = "repo";
    config.datasetName = "dataset";

    //when
    config.validate(failureCollector);

    //then
    Assert.assertTrue(failureCollector.getValidationFailures().isEmpty());
  }

  @Test
  public void testValidateFieldsCaseEmptyFields() {
    //given
    GithubBatchSourceConfig config = new GithubBatchSourceConfig("ref");

    //when
    config.validate(failureCollector);

    //then
    Assert.assertEquals(4, failureCollector.getValidationFailures().size());
  }

  @Test
  public void testValidateConfigCaseAuthTokenNull() {
    //given
    GithubBatchSourceConfig config = new GithubBatchSourceConfig("ref");
    MockFailureCollector failureCollector = new MockFailureCollector();
    config.repoOwner = "owner";
    config.repoName = "repo";
    config.datasetName = "dataset";

    //when
    config.validate(failureCollector);

    boolean isStartDateFailure = failureCollector.getValidationFailures().stream()
      .map(ValidationFailure::getCauses)
      .flatMap(Collection::stream)
      .anyMatch(cause -> cause.getAttributes().containsValue(AUTHORIZATION_TOKEN));

    //then
    Assert.assertTrue(isStartDateFailure);
  }

  @Test
  public void testValidateConfigCaseRepoOwnerNull() {
    //given
    GithubBatchSourceConfig config = new GithubBatchSourceConfig("ref");
    MockFailureCollector failureCollector = new MockFailureCollector();
    config.authorizationToken = "token";
    config.repoName = "repo";
    config.datasetName = "dataset";

    //when
    config.validate(failureCollector);

    boolean isStartDateFailure = failureCollector.getValidationFailures().stream()
      .map(ValidationFailure::getCauses)
      .flatMap(Collection::stream)
      .anyMatch(cause -> cause.getAttributes().containsValue(REPOSITORY_OWNER));

    //then
    Assert.assertTrue(isStartDateFailure);
  }

  @Test
  public void testValidateConfigCaseRepoNameNull() {
    //given
    GithubBatchSourceConfig config = new GithubBatchSourceConfig("ref");
    MockFailureCollector failureCollector = new MockFailureCollector();
    config.authorizationToken = "token";
    config.repoOwner = "owner";
    config.datasetName = "dataset";

    //when
    config.validate(failureCollector);

    boolean isStartDateFailure = failureCollector.getValidationFailures().stream()
      .map(ValidationFailure::getCauses)
      .flatMap(Collection::stream)
      .anyMatch(cause -> cause.getAttributes().containsValue(REPOSITORY_NAME));

    //then
    Assert.assertTrue(isStartDateFailure);
  }

  @Test
  public void testValidateConfigCaseDatasetNameNull() {
    //given
    GithubBatchSourceConfig config = new GithubBatchSourceConfig("ref");
    MockFailureCollector failureCollector = new MockFailureCollector();
    config.authorizationToken = "token";
    config.repoOwner = "owner";
    config.repoName = "repo";

    //when
    config.validate(failureCollector);

    boolean isStartDateFailure = failureCollector.getValidationFailures().stream()
      .map(ValidationFailure::getCauses)
      .flatMap(Collection::stream)
      .anyMatch(cause -> cause.getAttributes().containsValue(DATASET_NAME));

    //then
    Assert.assertTrue(isStartDateFailure);
  }
}
