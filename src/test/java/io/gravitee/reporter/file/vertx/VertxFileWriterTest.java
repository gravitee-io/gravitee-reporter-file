/*
 * Copyright © 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.reporter.file.vertx;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.gravitee.reporter.api.Reportable;
import io.gravitee.reporter.common.MetricsType;
import io.gravitee.reporter.common.formatter.Formatter;
import io.gravitee.reporter.file.config.FileReporterConfiguration;
import io.vertx.core.Vertx;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class VertxFileWriterTest {

    private static final ZoneOffset ZONE = ZoneOffset.UTC;
    private static final LocalDateTime NOW = LocalDateTime.now(ZONE);

    private VertxFileWriter<Reportable> vertxFileWriter;

    @Mock
    private FileReporterConfiguration configuration;

    @Mock
    private File file;

    @BeforeEach
    void setup() {
        when(configuration.getFilename()).thenReturn("/metrics/%s-yyyy_mm_dd");
        vertxFileWriter = new VertxFileWriter<>(mock(Vertx.class), MetricsType.REQUEST, mock(Formatter.class), "filename", configuration);
    }

    @Test
    void shouldDeleteFile_should_return_true_if_retainDays_configuration_exceeds_file_lastModified_time() {
        when(configuration.getRetainDays()).thenReturn(10L);

        long currentTimeMs = NOW.toInstant(ZONE).toEpochMilli();
        when(file.lastModified()).thenReturn(NOW.toInstant(ZONE).minus(10, DAYS).minus(1, SECONDS).toEpochMilli());

        assertThat(vertxFileWriter.shouldDeleteFile(file, currentTimeMs)).isTrue();
    }

    @Test
    void shouldDeleteFile_should_return_false_if_retainDays_configuration_doesnt_exceed_file_lastModified_time() {
        when(configuration.getRetainDays()).thenReturn(10L);

        long currentTimeMs = NOW.toInstant(ZONE).toEpochMilli();
        when(file.lastModified()).thenReturn(NOW.toInstant(ZONE).minus(10, DAYS).plus(1, SECONDS).toEpochMilli());

        assertThat(vertxFileWriter.shouldDeleteFile(file, currentTimeMs)).isFalse();
    }

    @Test
    void shouldDeleteFile_should_return_false_if_retainDays_configuration_is_0() {
        when(configuration.getRetainDays()).thenReturn(0L);

        long currentTimeMs = NOW.toInstant(ZONE).toEpochMilli();

        assertThat(vertxFileWriter.shouldDeleteFile(file, currentTimeMs)).isFalse();
        verify(file, never()).lastModified();
    }
}
