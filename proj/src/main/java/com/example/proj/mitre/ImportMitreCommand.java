package com.example.proj.mitre;

import com.example.proj.commands.Command;
import org.springframework.core.io.DefaultResourceLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public record ImportMitreCommand(MitreImportService service, String url) implements Command<ImportResult> {
    @Override
    public ImportResult execute() {
        // accepta classpath:, file: si https:
        try (InputStream in = new DefaultResourceLoader().getResource(url).getInputStream()) {
            return service.importFrom(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
