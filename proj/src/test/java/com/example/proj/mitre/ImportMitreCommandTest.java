package com.example.proj.mitre;

import com.example.proj.ApiTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.io.UncheckedIOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImportMitreCommandTest extends ApiTest {
    @ParameterizedTest @ValueSource(strings = {"classpath:stix-mini.json", "file:src/test/resources/stix-mini.json"})
    void importsFromUrl(String url) {
        assertThat(new ImportMitreCommand(importer, url).execute()).isEqualTo(new ImportResult(8, 5));
    }

    @Test void missingFileFails() {
        assertThatThrownBy(() -> new ImportMitreCommand(importer, "classpath:nope.json").execute()).isInstanceOf(UncheckedIOException.class);
    }
}
