package se.sundsvall.archive.api.domain.byggr;

import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import us.codecraft.xsoup.Xsoup;

@Component
class MetadataUtil {

    private static final String WITH_SUFFIX_REGEX = ".*(\\.[a-z]{3,4})$";

    boolean isValidMetadata(final String metadataXml) {
        var doc = Jsoup.parse(metadataXml, Parser.xmlParser());

        for (var element : evaluateXPath(doc, "//Bilaga")) {
            if (!element.attr("Namn").toLowerCase().matches(WITH_SUFFIX_REGEX) ||
                    !element.attr("Lank").toLowerCase().matches(WITH_SUFFIX_REGEX)) {
                return false;
            }
        }

        return true;
    }

    int getConfidentialityLevel(final String metadataXml) {
        var doc = Jsoup.parse(metadataXml, Parser.xmlParser());

        var matches = evaluateXPath(doc, "//ArkivobjektHandling/Handlingstyp");
        if (matches.size() != 1) {
            throw Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Invalid metadata")
                .withDetail("Unable to extract 'Handlingstyp' from metadata. Found " + matches.size() + " matching node(s)")
                .build();
        }

        return Optional.ofNullable(matches.first())
            .map(Element::text)
            .stream()
            .anyMatch("D"::equalsIgnoreCase) ? 1 : 0;
    }

    String replaceAttachmentNameAndLink(final String metadataXml, final String uuid, final String extension) {
        var doc = Jsoup.parse(metadataXml, Parser.xmlParser());

        var matches = evaluateXPath(doc, "//ArkivobjektHandling/Bilaga");
        if (matches.size() != 1) {
            throw Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Invalid metadata")
                .withDetail("Found " + matches.size() + " 'Bilaga' node(s), when 1 was expected")
                .build();
        }

        var element = matches.first();
        if (element != null) {
            element.attr("Namn", uuid + extension).attr("Lank", uuid + extension);
        }

        return doc.toString();
    }

    Elements evaluateXPath(final Element element, final String expression) {
        return Xsoup.compile(expression).evaluate(element).getElements();
    }
}
