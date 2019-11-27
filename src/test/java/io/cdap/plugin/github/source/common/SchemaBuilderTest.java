package io.cdap.plugin.github.source.common;

import io.cdap.cdap.api.data.schema.Schema;
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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaBuilderTest {

  private Class<?> clazz;

  public SchemaBuilderTest(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {Branch.class},
      {Collaborator.class},
      {Comment.class},
      {Commit.class},
      {Content.class},
      {DeployKey.class},
      {Deployment.class},
      {Fork.class},
      {Invitation.class},
      {Page.class},
      {Release.class},
      {TrafficReferrer.class},
      {Webhook.class}
    });
  }

  @Test
  public void testBuildSchema() {
    //given
    int fieldsCount = clazz.getDeclaredFields().length;
    Class<?> superclass = clazz.getSuperclass();
    if (superclass != null) {
      fieldsCount += superclass.getDeclaredFields().length;
    }

    //when
    Schema schema = SchemaBuilder.buildSchema(clazz.getSimpleName(), clazz);

    //then
    Assert.assertNotNull(schema);
    Assert.assertNotNull(schema.getFields());
    Assert.assertEquals(schema.getFields().size(), fieldsCount);
  }
}
