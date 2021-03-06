/*
 * Copyright © 2020 Cask Data, Inc.
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

/**
 * DeployKey model for github.
 */
public class DeployKey implements GitHubModel {

  @Key
  private Long id;
  @Key
  private String key;
  @Key
  private String url;
  @Key
  private String title;
  @Key
  private Boolean verified;
  @Key("created_at")
  private String createdAt;
  @Key("read_only")
  private Boolean readOnly;
}
