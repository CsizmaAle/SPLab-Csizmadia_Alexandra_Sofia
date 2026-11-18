package com.example.demo.commands;

import java.util.Optional;

import com.example.demo.models.BaseElement;
import com.example.demo.models.Element;
import com.example.demo.models.Section;

// Cautari in arborele de elemente al unei carti (Composite)
final class ElementTree {
    private ElementTree() {
    }

    // cauta recursiv sectiunea cu id-ul dat, sub radacina
    static Optional<Section> findSection(Section root, Integer id) {
        for (Element child : root.getChildren()) {
            if (child instanceof Section section) {
                if (id.equals(section.getId())) {
                    return Optional.of(section);
                }
                Optional<Section> found = findSection(section, id);
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    // scoate elementul cu id-ul dat de oriunde din arbore; true daca l-a gasit
    static boolean remove(Section root, Integer id) {
        for (Element child : root.getChildren()) {
            if (child instanceof BaseElement base && id.equals(base.getId())) {
                root.remove(child);
                return true;
            }
            if (child instanceof Section section && remove(section, id)) {
                return true;
            }
        }
        return false;
    }
}
