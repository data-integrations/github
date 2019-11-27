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

package io.cdap.plugin.github.source.batch;

import com.google.common.base.Strings;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.etl.api.FailureCollector;
import io.cdap.plugin.common.ReferencePluginConfig;
import io.cdap.plugin.github.source.common.SchemaBuilder;
import io.cdap.plugin.github.source.common.model.GitHubModel;
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

import javax.annotation.Nullable;

/**
 * Provides all required configuration for reading Github data from Batch Source.
 */
public class GithubBatchSourceConfig extends ReferencePluginConfig {

  public static final String AUTHORIZATION_TOKEN = "authorizationToken";
  public static final String AUTHORIZATION_TOKEN_DISPLAY_NAME = "Authorization token";
  public static final String REPOSITORY_OWNER = "repoOwner";
  public static final String REPOSITORY_OWNER_DISPLAY_NAME = "Repository owner name";
  public static final String REPOSITORY_NAME = "repoName";
  public static final String REPOSITORY_NAME_DISPLAY_NAME = "Repository name";
  public static final String DATASET_NAME = "datasetName";
  public static final String DATASET_NAME_DISPLAY_NAME = "Dataset name";
  public static final String HOSTNAME = "hostname";

  @Name(AUTHORIZATION_TOKEN)
  @Description("Authorization token to access GitHub API")
  @Macro
  protected String authorizationToken;

  @Name(REPOSITORY_OWNER)
  @Description("GitHub repository owner")
  @Macro
  protected String repoOwner;

  @Name(REPOSITORY_NAME)
  @Description("GitHub repository name")
  @Macro
  protected String repoName;

  @Name(DATASET_NAME)
  @Description("Dataset name that you would like to retrieve")
  @Macro
  protected String datasetName;

  @Name(HOSTNAME)
  @Description("GitHub API hostname")
  @Nullable
  @Macro
  protected String hostname;

  private transient Schema schema = null;

  public GithubBatchSourceConfig(String referenceName) {
    super(referenceName);
  }

  public Schema getSchema() {
    if (schema == null) {
      schema = SchemaBuilder.buildSchema(datasetName, getDatasetClass());
    }
    return schema;
  }

  public String getAuthorizationToken() {
    return authorizationToken;
  }

  public String getRepoOwner() {
    return repoOwner;
  }

  public String getRepoName() {
    return repoName;
  }

  public String getDatasetName() {
    return datasetName;
  }

  public Class<? extends GitHubModel> getDatasetClass() {
    switch (datasetName) {
      case "Branches": {
        return Branch.class;
      }
      case "Collaborators": {
        return Collaborator.class;
      }
      case "Comments": {
        return Comment.class;
      }
      case "Commits": {
        return Commit.class;
      }
      case "Contents": {
        return Content.class;
      }
      case "Deploy Keys": {
        return DeployKey.class;
      }
      case "Deployments": {
        return Deployment.class;
      }
      case "Forks": {
        return Fork.class;
      }
      case "Invitations": {
        return Invitation.class;
      }
      case "Pages": {
        return Page.class;
      }
      case "Releases": {
        return Release.class;
      }
      case "Traffic:Referrers": {
        return TrafficReferrer.class;
      }
      case "Webhooks": {
        return Webhook.class;
      }
      default: {
        throw new IllegalArgumentException("Unsupported dataset name!");
      }
    }
  }

  @Nullable
  public String getHostname() {
    return hostname;
  }

  public void validate(FailureCollector failureCollector) {
    if (!containsMacro(authorizationToken) && Strings.isNullOrEmpty(authorizationToken)) {
      failureCollector
        .addFailure(String.format("%s must be specified.", AUTHORIZATION_TOKEN_DISPLAY_NAME), null)
        .withConfigProperty(AUTHORIZATION_TOKEN_DISPLAY_NAME);
    }
    if (!containsMacro(repoOwner) && Strings.isNullOrEmpty(repoOwner)) {
      failureCollector
        .addFailure(String.format("%s must be specified.", REPOSITORY_OWNER_DISPLAY_NAME), null)
        .withConfigProperty(REPOSITORY_OWNER_DISPLAY_NAME);
    }
    if (!containsMacro(repoName) && Strings.isNullOrEmpty(repoName)) {
      failureCollector
        .addFailure(String.format("%s must be specified.", REPOSITORY_NAME_DISPLAY_NAME), null)
        .withConfigProperty(REPOSITORY_NAME_DISPLAY_NAME);
    }
    if (!containsMacro(datasetName) && Strings.isNullOrEmpty(datasetName)) {
      failureCollector
        .addFailure(String.format("%s must be specified.", DATASET_NAME_DISPLAY_NAME), null)
        .withConfigProperty(DATASET_NAME_DISPLAY_NAME);
    }
  }
}
