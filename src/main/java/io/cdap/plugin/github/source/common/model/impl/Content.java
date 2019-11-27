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

/**
 * Content model
 */
public class Content implements GitHubModel {

  @Key
  private String type;
  @Key
  private String encoding;
  @Key
  private Long size;
  @Key
  private String name;
  @Key
  private String path;
  @Key
  private String content;
  @Key
  private String sha;
  @Key
  private String url;
  @Key("git_url")
  private String gitUrl;
  @Key("html_url")
  private String htmlUrl;
  @Key("download_url")
  private String downloadUrl;
  @Key("_links")
  private Link links;

  /**
   * Content.Link model
   */
  public static class Link {
    @Key
    private String git;
    @Key
    private String self;
    @Key
    private String html;
  }
}
