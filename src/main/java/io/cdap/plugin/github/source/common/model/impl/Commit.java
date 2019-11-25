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
import io.cdap.plugin.github.source.common.model.impl.user.User;

import java.util.List;

/**
 * Commit model for github.
 */
public class Commit implements GitHubModel {

  @Key
  private String url;
  @Key
  private String sha;
  @Key("node_id")
  private String nodeId;
  @Key("html_url")
  private String htmlUrl;
  @Key("comments_url")
  private String commentsUrl;
  @Key
  private CommitData commit;
  @Key("author")
  private User mainAuthor;
  @Key("committer")
  private User mainCommitter;
  @Key
  private List<CommitData.Tree> parents;

  /**
   * Commit.CommitData model
   */
  public static class CommitData {
    @Key
    private String url;
    @Key
    private CommitUser author;
    @Key
    private CommitUser committer;
    @Key
    private String message;
    @Key
    private Tree tree;
    @Key("comment_count")
    private Integer commentCount;
    @Key
    private Verification verification;

    /**
     * Commit.CommitData.CommitUser model
     */
    public static class CommitUser {
      @Key
      private String name;
      @Key
      private String email;
      @Key
      private String date;
    }

    /**
     * Commit.CommitData.Tree model
     */
    public static class Tree {
      @Key
      private String url;
      @Key
      private String sha;
    }

    /**
     * Commit.CommitData.Verification model
     */
    public static class Verification {
      @Key
      private Boolean verified;
      @Key
      private String reason;
      @Key
      private String signature;
      @Key
      private String payload;
    }
  }
}
