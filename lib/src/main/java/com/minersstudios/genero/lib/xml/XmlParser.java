package com.minersstudios.genero.lib.xml;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;

import android.content.res.XmlResourceParser;
import android.util.Log;

import androidx.annotation.NonNull;

import com.minersstudios.genero.lib.xml.tag.XmlTag;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class XmlParser {

    @Contract(" -> fail")
    private XmlParser() throws AssertionError {
        throw new AssertionError("Utility class");
    }

    /**
     * Parse an XML document using a {@link XmlResourceParser resource parser}.
     * <p>
     * <b>NOTE:</b> The parser closes automatically when the stream is closed,
     *              which is implemented via {@link Stream#onClose(Runnable)}.
     *              Therefore, when redefining it, take care to close the parser
     *              manually.
     *
     * @param parser The pull parser to use
     * @return A stream of XML tags
     * @see XmlResourceParser
     * @see #parsePull(XmlPullParser)
     */
    public static @NonNull Stream<XmlTag> parseResource(final @NonNull XmlResourceParser parser) {
        return parsePull(parser)
                .onClose(
                        () -> {
                            try {
                                parser.close();
                            } catch (final Throwable e) {
                                Log.e("XmlParser", "Failed to close parser", e);
                            }
                        }
                );
    }

    /**
     * Parse an XML document using a {@link XmlPullParser standard pull parser}
     * <p>
     * <b>NOTE:</b> The input stream is not closed automatically when the stream
     *              is closed. Therefore, it is the responsibility of the caller
     *              to close the input stream.
     *
     * @param parser The pull parser to use
     * @return A stream of XML tags
     * @see XmlPullParser
     */
    public static @NonNull Stream<XmlTag> parsePull(final @NonNull XmlPullParser parser) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new TagIterator(parser),
                        Spliterator.ORDERED | Spliterator.IMMUTABLE
                ),
                false
        );
    }

    private static class TagIterator implements Iterator<XmlTag> {
        private final XmlPullParser parser;

        TagIterator(final @NonNull XmlPullParser parser) {
            this.parser = parser;
        }

        @Override
        public boolean hasNext() {
            try {
                return this.parser.getEventType() != END_DOCUMENT;
            } catch (final XmlPullParserException ignored) {
                return false;
            }
        }

        @Override
        public @NonNull XmlTag next() throws NoSuchElementException {
            try {
                this.parser.next();
            } catch (final IOException | XmlPullParserException ignored) {
                throw new NoSuchElementException();
            }

            return XmlTag.create(this.parser);
        }
    }
}
