/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.reporter.file.config;

import io.gravitee.reporter.file.formatter.Type;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author David BRASSELY (david.brassely at graviteesource.com)
 * @author GraviteeSource Team
 */
public class FileReporterConfiguration {

    /**
     *  Reporter file name.
     */
    @Value("${reporters.file.fileName:#{systemProperties['gravitee.home']}/metrics/%s-yyyy_mm_dd}")
    private String filename;

    @Value("${reporters.file.output:json}")
    private String outputType;

    @Value("${reporters.file.flushInterval:1000}")
    private long flushInterval;

    public String getFilename() {
        return filename;
    }

    public Type getOutputType() {
        return outputType == null ? Type.JSON : Type.valueOf(outputType.toUpperCase());
    }

    public long getFlushInterval() {
        return flushInterval;
    }
}
