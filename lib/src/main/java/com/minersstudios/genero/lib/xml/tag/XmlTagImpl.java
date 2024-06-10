package com.minersstudios.genero.lib.xml.tag;

import android.util.Log;

import androidx.annotation.NonNull;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

abstract class XmlTagImpl implements XmlTag {
    private final Reference<XmlPullParser> parser;
    private int type;
    private int depth;
    private int lineNumber;
    private int columnNumber;
    private String positionDescription;

    static final int NOT_SET = -2;

    XmlTagImpl(final @NonNull XmlPullParser parser) {
        this.parser = new WeakReference<>(parser);
        this.type = NOT_SET;
        this.depth = NOT_SET;
        this.lineNumber = NOT_SET;
        this.columnNumber = NOT_SET;
    }

    @Override
    public int getType() throws IllegalStateException {
        if (this.type == NOT_SET) {
            try {
                this.type = this.getParser().getEventType();
            } catch (final XmlPullParserException e) {
                this.type = XmlPullParser.END_DOCUMENT;

                Log.e(
                        this.getClass().getSimpleName(),
                        "Failed to get event type, defaulting to XmlPullParser.END_DOCUMENT",
                        e
                );
            }
        }

        return this.type;
    }

    @Override
    public int getDepth() throws IllegalStateException {
        if (this.depth == NOT_SET) {
            this.depth = this.getParser().getDepth();
        }

        return this.depth;
    }

    @Override
    public int getLineNumber() throws IllegalStateException {
        if (this.lineNumber == NOT_SET) {
            this.lineNumber = this.getParser().getLineNumber();
        }

        return this.lineNumber;
    }

    @Override
    public int getColumnNumber() throws IllegalStateException {
        if (this.columnNumber == NOT_SET) {
            this.columnNumber = this.getParser().getColumnNumber();
        }

        return this.columnNumber;
    }

    @Override
    public @NonNull String getPositionDescription() throws IllegalStateException {
        if (this.positionDescription == null) {
            this.positionDescription = this.getParser().getPositionDescription();
        }

        return this.positionDescription;
    }

    @Override
    public @NonNull String toString() throws IllegalStateException {
        return "XmlTag{" +
                "type=" + this.getType() +
                ", depth=" + this.getDepth() +
                ", lineNumber=" + this.getLineNumber() +
                ", columnNumber=" + this.getColumnNumber() +
                ", positionDescription='" + this.getPositionDescription() + '\'' +
                "}";
    }

    @Override
    public @NonNull StartTag toStartTag() throws UnsupportedOperationException, IllegalStateException {
        return StartTag.create(this.getParser());
    }

    @Override
    public @NonNull EndTag toEndTag() throws UnsupportedOperationException, IllegalStateException {
        return EndTag.create(this.getParser());
    }

    @Override
    public @NonNull TextTag toTextTag() throws UnsupportedOperationException, IllegalStateException {
        return TextTag.create(this.getParser());
    }

    protected @NonNull XmlPullParser getParser() throws IllegalStateException {
        final XmlPullParser parser = this.parser.get();

        if (parser == null) {
            throw new IllegalStateException("Parser is in an invalid state");
        }

        return parser;
    }
}
