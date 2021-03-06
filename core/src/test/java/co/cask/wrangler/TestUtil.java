/*
 * Copyright © 2017 Cask Data, Inc.
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

package co.cask.wrangler;

import co.cask.wrangler.api.DirectiveParseException;
import co.cask.wrangler.api.Directives;
import co.cask.wrangler.api.Pipeline;
import co.cask.wrangler.api.PipelineException;
import co.cask.wrangler.api.Record;
import co.cask.wrangler.executor.PipelineExecutor;
import co.cask.wrangler.executor.TextDirectives;

import java.util.List;

/**
 * Utilities for testing.
 */
public final class TestUtil {

  /**
   * Executes the directives on the record specified.
   *
   * @param directives to be executed.
   * @param records to be executed on directives.
   * @return transformed directives.
   */
  public static List<Record> run(String[] directives, List<Record> records)
    throws PipelineException, DirectiveParseException {
    Directives d = new TextDirectives(directives);
    Pipeline pipeline = new PipelineExecutor();
    pipeline.configure(d, null);
    return pipeline.execute(records);
  }

}
