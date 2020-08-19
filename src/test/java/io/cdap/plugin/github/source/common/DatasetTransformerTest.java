package io.cdap.plugin.github.source.common;

import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.github.source.common.model.impl.Commit;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static io.github.benas.randombeans.EnhancedRandomBuilder.aNewEnhancedRandom;

@RunWith(Parameterized.class)
public class DatasetTransformerTest {

  private static final EnhancedRandom random = aNewEnhancedRandom();

  private Class<?> clazz;
  private Object model;

  public DatasetTransformerTest(Class<?> clazz) {
    this.clazz = clazz;
    this.model = random.nextObject(clazz);
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
//      {Branch.class},
//      {Collaborator.class},
//      {Comment.class},
      {Commit.class},
//      {Content.class},
//      {DeployKey.class},
//      {Deployment.class},
//      {Fork.class},
//      {Invitation.class},
//      {Page.class},
//      {Release.class},
//      {TrafficReferrer.class},
//      {Webhook.class}
    });
  }

  @Test
  public void testTransformModel() throws NoSuchFieldException, IllegalAccessException {
    //given
    Schema schema = SchemaBuilder.buildSchema(clazz.getSimpleName(), clazz);

    //when
    StructuredRecord output = DatasetTransformer.transform(model, schema);

    //then
    Assert.assertNotNull(schema.getFields());
    schema.getFields().forEach(field -> Assert.assertNotNull(output.get(field.getName())));
  }
}
