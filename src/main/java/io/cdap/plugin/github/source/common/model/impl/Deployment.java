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
package io.cdap.plugin.github.source.common.model.impl;

import com.google.api.client.util.Key;
import io.cdap.plugin.github.source.common.model.GitHubModel;
import io.cdap.plugin.github.source.common.model.impl.user.User;

/**
 * Deployment model
 */
public class Deployment implements GitHubModel {

  @Key
  private String url;
  @Key
  private Long id;
  @Key("node_id")
  private String nodeId;
  @Key
  private String sha;
  @Key
  private String ref;
  @Key
  private String task;
  @Key
  private Payload payload;
  @Key("original_environment")
  private String originalEnvironment;
  @Key
  private String environment;
  @Key
  private String description;
  @Key
  private User creator;
  @Key("created_at")
  private String createdAt;
  @Key("updated_at")
  private String updatedAt;
  @Key("statuses_url")
  private String statusesUrl;
  @Key("repository_url")
  private String repositoryUrl;
  @Key("transient_environment")
  private Boolean transientEnvironment;
  @Key("production_environment")
  private Boolean productionEnvironment;

  /**
   * Deployment.Payload model
   */
  public static class Payload {
    @Key
    private String deploy;
  }
}
