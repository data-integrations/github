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

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import io.cdap.plugin.github.source.common.GitHubRequestFactory;
import io.cdap.plugin.github.source.common.model.GitHubModel;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

import static io.cdap.plugin.github.source.common.GitHubRequestFactory.DEFAULT_PAGE_SIZE;

/**
 * RecordReader implementation, which reads {@link GitHubModel} instances from GitHub repository API
 */
public class GithubRecordReader extends RecordReader<NullWritable, GitHubModel> {

  private final GithubBatchSourceConfig config;
  private final String link;

  private Iterator<GitHubModel> currentPage;
  private GitHubModel currentRow;
  private Integer currentRowIndex = 0;

  public GithubRecordReader(GithubBatchSourceConfig config, String link) {
    this.config = config;
    this.link = link;
  }

  @Override
  public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException {
    HttpRequest httpRequest = GitHubRequestFactory.buildRequest(link, config.getAuthorizationToken());
    HttpResponse response = httpRequest.execute();
    Class<? extends GitHubModel[]> datasetClass = (Class<? extends GitHubModel[]>)
      Array.newInstance(config.getDatasetClass(), 0).getClass();
    currentPage = Arrays.stream(response.parseAs(datasetClass)).iterator();
  }

  @Override
  public boolean nextKeyValue() {
    if (!currentPage.hasNext()) {
      return false;
    }
    currentRowIndex++;
    currentRow = currentPage.next();
    return true;
  }

  @Override
  public NullWritable getCurrentKey() {
    return null;
  }

  @Override
  public GitHubModel getCurrentValue() {
    return currentRow;
  }

  @Override
  public float getProgress() {
    return currentRowIndex / (float) DEFAULT_PAGE_SIZE;
  }

  @Override
  public void close() {

  }
}
