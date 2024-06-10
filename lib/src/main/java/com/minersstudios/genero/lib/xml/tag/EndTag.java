package com.minersstudios.genero.lib.xml.tag;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;

public class EndTag extends ElementTag {

    EndTag(final @NonNull XmlPullParser parser) {
        super(parser);
    }

    /**
     * Creates a new {@link StartTag} instance
     *
     * @param parser The parser to create the tag from
     * @return A new {@code StartTag} instance
     * @throws UnsupportedOperationException If the current element is not a
     *                                       start tag
     */
    @Contract("_ -> new")
    public static @NonNull EndTag create(final @NonNull XmlPullParser parser) throws UnsupportedOperationException {
        try {
            parser.require(XmlPullParser.END_TAG, null, null);
        } catch (final Throwable ignored) {
            throw new UnsupportedOperationException("Not an end tag");
        }

        return new EndTag(parser);
    }
}
