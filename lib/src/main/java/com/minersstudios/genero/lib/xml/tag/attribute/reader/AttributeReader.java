package com.minersstudios.genero.lib.xml.tag.attribute.reader;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.xml.tag.attribute.Attribute;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;

public interface AttributeReader extends Reader<Attribute> {

    /**
     * Creates a new instance of {@link AttributeReader} with the given parser
     *
     * @param parser The parser to read from
     * @return A new instance of {@code AttributeReader}
     */
    @Contract("_ -> new")
    static @NonNull AttributeReader create(final @NonNull XmlPullParser parser) {
        return new AttributeReaderImpl(parser);
    }
}
