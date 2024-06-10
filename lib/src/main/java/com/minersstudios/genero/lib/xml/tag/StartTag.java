package com.minersstudios.genero.lib.xml.tag;

import android.content.res.XmlResourceParser;
import android.util.Log;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.xml.tag.attribute.Attribute;
import com.minersstudios.genero.lib.xml.tag.attribute.reader.AttributeReader;
import com.minersstudios.genero.lib.xml.tag.attribute.reader.Reader;
import com.minersstudios.genero.lib.xml.tag.attribute.reader.ResourceAttributeReader;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class StartTag extends ElementTag {
    private Reader<? extends Attribute> reader;
    private Boolean isEmptyElementTag;

    private StartTag(final @NonNull XmlPullParser parser) {
        super(parser);
    }

    /**
     * Returns an attribute reader for the current tag.
     * <p>
     * Available readers :
     * <ul>
     *     <li>{@link AttributeReader} - all {@link XmlPullParser xml parsers}</li>
     *     <li>{@link ResourceAttributeReader} - for {@link XmlResourceParser}</li>
     * </ul>
     *
     * @param type The class of the reader to create
     * @param <R>  The type of reader to create
     * @return An attribute reader
     * @throws ClassCastException If the specified class is not a valid reader
     *                            class for the current tag
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @SuppressWarnings("unchecked")
    public <R extends Reader<? extends Attribute>> @NonNull R getAttributeReader(final @NonNull Class<R> type) throws ClassCastException, IllegalStateException {
        if (this.reader == null) {
            final XmlPullParser parser = this.getParser();

            this.reader =
                    ResourceAttributeReader.class == type
                    && parser instanceof XmlResourceParser
                    ? ResourceAttributeReader.create((XmlResourceParser) parser)
                    : AttributeReader.create(parser);
        }

        return (R) this.reader;
    }

    /**
     * Returns true if the current element is degenerated (e.g. &lt;foobar/&gt;).
     *
     * @return True if the element is empty
     * @throws IllegalStateException If the parser is in an invalid state
     */
    public boolean isEmptyElementTag() throws IllegalStateException {
        if (this.isEmptyElementTag == null) {
            try {
                this.isEmptyElementTag = this.getParser().isEmptyElementTag();
            } catch (final XmlPullParserException e) {
                this.isEmptyElementTag = true;

                Log.e(
                        this.getClass().getSimpleName(),
                        "Failed to determine if the element is empty, defaulting to true. " +
                        "Seems like the parser is in an invalid state.",
                        e
                );
            }
        }

        return this.isEmptyElementTag;
    }

    /**
     * Creates a new {@link EndTag} instance
     *
     * @param parser The parser to create the tag from
     * @return A new {@code EndTag} instance
     * @throws UnsupportedOperationException If the current element is not an
     *                                       end tag
     */
    @Contract("_ -> new")
    public static @NonNull StartTag create(final @NonNull XmlPullParser parser) throws UnsupportedOperationException {
        try {
            parser.require(XmlPullParser.START_TAG, null, null);
        } catch (final Throwable ignored) {
            throw new UnsupportedOperationException("Not a start tag");
        }

        return new StartTag(parser);
    }
}
