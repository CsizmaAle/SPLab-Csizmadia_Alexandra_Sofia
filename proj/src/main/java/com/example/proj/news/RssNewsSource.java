package com.example.proj.news;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.web.util.HtmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.IntStream;

// Adapter: item-urile RSS devin Article
public record RssNewsSource(String url) implements NewsSource {
    private static final String CONTENT = "http://purl.org/rss/1.0/modules/content/";

    @Override
    public List<Article> fetch() {
        try (InputStream in = new DefaultResourceLoader().getResource(url).getInputStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            // protectie XXE: fara DOCTYPE si fara DTD-uri sau entitati externe
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            NodeList items = factory.newDocumentBuilder().parse(in).getElementsByTagName("item");
            return IntStream.range(0, items.getLength()).mapToObj(i -> article((Element) items.item(i)))
                .filter(a -> a.title() != null && a.url() != null).toList();
        } catch (Exception e) {
            throw new IllegalStateException("feed invalid: " + url, e);
        }
    }

    private static Article article(Element item) {
        String description = text(item, null, "description"), content = text(item, CONTENT, "encoded");
        return new Article(text(item, null, "title"), summary(description), text(item, null, "link"),
            date(text(item, null, "pubDate")), content != null ? content : description);
    }

    private static String text(Element e, String ns, String tag) {
        NodeList l = ns == null ? e.getElementsByTagName(tag) : e.getElementsByTagNameNS(ns, tag);
        return l.getLength() == 0 ? null : l.item(0).getTextContent().strip();
    }

    // fara tag-uri HTML si entitati, maxim 1000 de caractere
    private static String summary(String html) {
        if (html == null) return "";
        String s = HtmlUtils.htmlUnescape(html.replaceAll("<[^>]*>", " ")).replaceAll("\\s+", " ").strip();
        return s.length() > 1000 ? s.substring(0, 1000) : s;
    }

    // data lipsa sau invalida = acum
    private static Instant date(String rfc1123) {
        try {
            return ZonedDateTime.parse(rfc1123, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
        } catch (NullPointerException | DateTimeParseException e) {
            return Instant.now();
        }
    }
}
