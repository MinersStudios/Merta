package com.minersstudios.genero.lib.xml.tag;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@SuppressWarnings("unused")
public class TextTag extends XmlTagImpl {
    private String text;
    private Boolean isWhitespace;

    private TextTag(final @NonNull XmlPullParser parser) {
        super(parser);
    }

    /**
     * Returns the text content of the current element as a string
     *
     * @return The text content of the current element as a string
     * @throws IllegalStateException If the parser is in an invalid state
     */
    public @NonNull String getText() throws IllegalStateException {
        if (this.text == null) {
            this.text = this.getParser().getText();
        }

        return this.text;
    }

    /**
     * Returns the buffer that contains the text of the current element, as well
     * as the start offset and length relevant for the current element
     *
     * @param holderForStartAndLength Must hold an 2-element int array into
     *                                which the start offset and length values
     *                                will be written
     * @return Char buffer that contains the text of the current event
     * @throws IllegalStateException If the parser is in an invalid state
     */
    public @NonNull char[] getTextCharacters(final @NonNull int[] holderForStartAndLength) throws IllegalStateException {
        return this.getParser().getTextCharacters(holderForStartAndLength);
    }

    /**
     * Returns whether the current element contains only whitespace characters
     *
     * @return True if the current element contains only whitespace characters
     * @throws IllegalStateException If the parser is in an invalid state
     */
    public boolean isWhitespace() throws IllegalStateException {
        if (this.isWhitespace == null) {
            try {
                this.isWhitespace = this.getParser().isWhitespace();
            } catch (final XmlPullParserException e) {
                this.isWhitespace = false;

                Log.e(
                        this.getClass().getSimpleName(),
                        "Failed to determine if text is whitespace, defaulting to false",
                        e
                );
            }
        }

        return this.isWhitespace;
    }

    @Override
    public @NonNull String toString() throws IllegalStateException {
        return "XmlTag{" +
                "type=" + this.getType() +
                ", depth=" + this.getDepth() +
                ", lineNumber=" + this.getLineNumber() +
                ", columnNumber=" + this.getColumnNumber() +
                ", positionDescription='" + this.getPositionDescription() + '\'' +
                ", text='" + this.getText() + '\'' +
                ", isWhitespace=" + this.isWhitespace() +
                '}';
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
    public static @NonNull TextTag create(final @NonNull XmlPullParser parser) throws UnsupportedOperationException {
        try {
            parser.require(XmlPullParser.TEXT, null, null);
        } catch (final Throwable ignored) {
            throw new UnsupportedOperationException("Not a text tag");
        }

        return new TextTag(parser);
    }
}
