package se.sundsvall.archive.api.domain.byggr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.stereotype.Component;

import us.codecraft.xsoup.Xsoup;

@Component
class MetadataValidator {

    private static final String WITH_SUFFIX_REGEX = ".*(\\.[a-z]{3,4})$";

    boolean isValidMetadata(final String metadataXml) {
        var doc = Jsoup.parse(metadataXml, Parser.xmlParser());

        for (Element element : Xsoup.compile("//Bilaga").evaluate(doc).getElements()) {
            if (!element.attr("Namn").toLowerCase().matches(WITH_SUFFIX_REGEX) ||
                    !element.attr("Lank").toLowerCase().matches(WITH_SUFFIX_REGEX)) {
                return false;
            }
        }

        return true;
    }
}
