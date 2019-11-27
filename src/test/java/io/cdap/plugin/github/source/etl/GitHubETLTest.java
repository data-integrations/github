/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.cdap.plugin.github.source.etl;

import com.google.common.base.Strings;
import io.cdap.cdap.api.artifact.ArtifactSummary;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.dataset.table.Table;
import io.cdap.cdap.datapipeline.DataPipelineApp;
import io.cdap.cdap.datapipeline.SmartWorkflow;
import io.cdap.cdap.etl.api.batch.BatchSource;
import io.cdap.cdap.etl.mock.batch.MockSink;
import io.cdap.cdap.etl.mock.test.HydratorTestBase;
import io.cdap.cdap.etl.proto.v2.ETLBatchConfig;
import io.cdap.cdap.etl.proto.v2.ETLPlugin;
import io.cdap.cdap.etl.proto.v2.ETLStage;
import io.cdap.cdap.proto.ProgramRunStatus;
import io.cdap.cdap.proto.artifact.AppRequest;
import io.cdap.cdap.proto.id.ApplicationId;
import io.cdap.cdap.proto.id.ArtifactId;
import io.cdap.cdap.proto.id.NamespaceId;
import io.cdap.cdap.test.ApplicationManager;
import io.cdap.cdap.test.DataSetManager;
import io.cdap.cdap.test.WorkflowManager;
import io.cdap.plugin.github.source.batch.GithubBatchSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GitHubETLTest extends HydratorTestBase {

  private static final ArtifactSummary APP_ARTIFACT = new ArtifactSummary("data-pipeline", "3.2.0");

  private static String authorizationToken;
  private static String repoName;
  private static String repoOwner;
  private static String datasetName;

  @BeforeClass
  public static void setupTestClass() throws Exception {
    authorizationToken = System.getProperty("github.authorization.token");
    if (Strings.isNullOrEmpty(authorizationToken)) {
      throw new IllegalArgumentException("github.authorization.token system property must not be empty.");
    }
    repoName = System.getProperty("github.repo.name");
    if (Strings.isNullOrEmpty(repoName)) {
      throw new IllegalArgumentException("github.repo.name system property must not be empty.");
    }
    repoOwner = System.getProperty("github.repo.owner");
    if (Strings.isNullOrEmpty(repoOwner)) {
      throw new IllegalArgumentException("github.repo.owner system property must not be empty.");
    }
    datasetName = System.getProperty("github.repo.dataset");
    if (Strings.isNullOrEmpty(datasetName)) {
      throw new IllegalArgumentException("github.repo.dataset system property must not be empty.");
    }

    ArtifactId parentArtifact = NamespaceId.DEFAULT.artifact(APP_ARTIFACT.getName(), APP_ARTIFACT.getVersion());

    // add the artifact and mock plugins
    setupBatchArtifacts(parentArtifact, DataPipelineApp.class);

    // add our plugins artifact with the artifact as its parent.
    // this will make our plugins available.
    addPluginArtifact(NamespaceId.DEFAULT.artifact("example-plugins", "1.0.0"),
                      parentArtifact, GithubBatchSource.class);
  }

  @Test
  public void testGitHubBatchSource() throws Exception {

    ETLStage source = new ETLStage("GitHubETLTest", new ETLPlugin(GithubBatchSource.NAME, BatchSource.PLUGIN_TYPE,
                                                               getSourceMinimalDefaultConfigs(), null));

    String outputDatasetName = "output-batchsourcetest_github";
    ETLStage sink = new ETLStage("sink", MockSink.getPlugin(outputDatasetName));

    ETLBatchConfig etlConfig = ETLBatchConfig.builder()
      .addStage(source)
      .addStage(sink)
      .addConnection(source.getName(), sink.getName())
      .build();

    ApplicationId pipelineId = NamespaceId.DEFAULT.app("GitHubBatchTest");
    ApplicationManager appManager = deployApplication(pipelineId, new AppRequest<>(APP_ARTIFACT, etlConfig));

    WorkflowManager workflowManager = appManager.getWorkflowManager(SmartWorkflow.NAME);
    workflowManager.startAndWaitForRun(ProgramRunStatus.COMPLETED, 5, TimeUnit.MINUTES);

    DataSetManager<Table> dataset = getDataset(outputDatasetName);
    List<StructuredRecord> outputRecords = MockSink.readOutput(dataset);

    Assert.assertNotNull(outputRecords);
  }

  public Map<String, String> getSourceMinimalDefaultConfigs() {
    Map<String, String> sourceProps = new HashMap<>();
    sourceProps.put("referenceName", "ref");
    sourceProps.put("authorizationToken", authorizationToken);
    sourceProps.put("repoName", repoName);
    sourceProps.put("repoOwner", repoOwner);
    sourceProps.put("datasetName", datasetName);
    return sourceProps;
  }
}
