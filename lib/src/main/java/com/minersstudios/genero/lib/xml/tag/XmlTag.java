package com.minersstudios.genero.lib.xml.tag;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;

public interface XmlTag {

    /**
     * Returns the type of the element
     * <p>
     * The type can be one of the following :
     * <ul>
     *     <li>{@link XmlPullParser#START_TAG}</li>
     *     <li>{@link XmlPullParser#END_TAG}</li>
     *     <li>{@link XmlPullParser#TEXT}</li>
     *     <li>{@link XmlPullParser#START_DOCUMENT}</li>
     *     <li>{@link XmlPullParser#END_DOCUMENT}</li>
     * </ul>
     *
     * @return The type of the element
     * @throws IllegalStateException If the parser is in an invalid state
     * @see XmlPullParser
     */
    int getType() throws IllegalStateException;

    /**
     * Returns the current depth of the element.
     * <p>
     * Outside the root element, the depth is {@code 0}. The depth is
     * incremented by {@code 1} when a start tag is reached. The depth is
     * decremented after the end tag event was observed.
     *
     * <pre>
     * &lt;!-- outside -->   0
     * &lt;root>             1
     *   sometext            1
     *     &lt;foobar>       2
     *     &lt;/foobar>      2
     * &lt;/root>            1
     * &lt;!-- outside -->   0
     * </pre>
     *
     * @return Returns the current depth of the element
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getDepth() throws IllegalStateException;

    /**
     * Returns the current line number, starting from {@code 1}
     * <p>
     * When the parser does not know the current line number or can not
     * determine it, {@code -1} is returned (e.g. for WBXML).
     *
     * @return The current line number or {@code -1}
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getLineNumber() throws IllegalStateException;

    /**
     * Returns the current column number, starting from {@code 0}
     * <p>
     * When the parser does not know the current column number or can not
     * determine it, {@code -1} is returned (e.g. for WBXML).
     *
     * @return The current column number or {@code -1}
     * @throws IllegalStateException If the parser is in an invalid state
     */
    int getColumnNumber() throws IllegalStateException;

    /**
     * Returns a short text describing the current parser state, including the
     * position, a description of the current event and the data source if known.
     * <p>
     * This method is especially useful to provide meaningful error messages and
     * for debugging purposes.
     *
     * @return A short text describing the current parser state
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @NonNull String getPositionDescription() throws IllegalStateException;

    /**
     * Returns a string representation of the current element
     *
     * @return A string representation of the current element
     * @throws IllegalStateException If the parser is in an invalid state
     */
    @Override
    @NonNull String toString() throws IllegalStateException;

    /**
     * Returns the current element as a {@link StartTag}
     *
     * @return The current element as a {@code StartTag}
     * @throws UnsupportedOperationException If the current element is not a
     *                                       start tag
     * @throws IllegalStateException         If the parser is in an invalid
     *                                       state
     */
    @NonNull StartTag toStartTag() throws UnsupportedOperationException, IllegalStateException;

    /**
     * Returns the current element as an {@link EndTag}
     *
     * @return The current element as an {@code EndTag}
     * @throws UnsupportedOperationException If the current element is not an
     *                                       end tag
     * @throws IllegalStateException         If the parser is in an invalid
     *                                       state
     */
    @NonNull EndTag toEndTag() throws UnsupportedOperationException, IllegalStateException;

    /**
     * Returns the current element as a {@link TextTag}
     *
     * @return The current element as a {@code TextTag}
     * @throws UnsupportedOperationException If the current element is not a
     *                                       text tag
     * @throws IllegalStateException         If the parser is in an invalid
     *                                       state
     */
    @NonNull TextTag toTextTag() throws UnsupportedOperationException, IllegalStateException;

    /**
     * Creates a new {@link XmlTag} instance
     *
     * @param parser The parser to create the tag from
     * @return A new {@code XmlTag} instance
     */
    @Contract("_ -> new")
    static @NonNull XmlTag create(final @NonNull XmlPullParser parser) {
        return new XmlTagImpl(parser) {};
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
    static @NonNull StartTag createStart(final @NonNull XmlPullParser parser) throws UnsupportedOperationException {
        return StartTag.create(parser);
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
    static @NonNull EndTag createEnd(final @NonNull XmlPullParser parser) throws UnsupportedOperationException {
        return EndTag.create(parser);
    }

    /**
     * Creates a new {@link TextTag} instance
     *
     * @param parser The parser to create the tag from
     * @return A new {@code TextTag} instance
     * @throws UnsupportedOperationException If the current element is not a
     *                                       text tag
     */
    @Contract("_ -> new")
    static @NonNull TextTag createText(final @NonNull XmlPullParser parser) throws UnsupportedOperationException {
        return TextTag.create(parser);
    }
}
