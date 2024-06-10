package com.minersstudios.genero.lib.xml.tag.attribute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

class RawAttribute<P extends XmlPullParser> implements Attribute {
    private final Reference<P> parser;
    private final int index;
    private String namespace;
    private String name;
    private String prefix;
    private String type;
    private String value;
    private Boolean isDefault;

    RawAttribute(
            final @NonNull P parser,
            final int index
    ) throws IndexOutOfBoundsException {
        final int count = parser.getAttributeCount();

        if (
                index < 0
                || index >= count
        ) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for attribute count " + count);
        }

        this.parser = new WeakReference<>(parser);
        this.index = index;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public @NonNull String getNamespace() throws IllegalStateException {
        if (this.namespace == null) {
            try {
                this.namespace = this.getParser().getAttributeNamespace(this.index);
            } catch (final IndexOutOfBoundsException e) {
                throw new IllegalStateException("Parser is in an invalid state", e);
            }
        }

        return this.namespace;
    }

    @Override
    public @NonNull String getName() throws IllegalStateException {
        if (this.name == null) {
            try {
                this.name = this.getParser().getAttributeName(this.index);
            } catch (final IndexOutOfBoundsException e) {
                throw new IllegalStateException("Parser is in an invalid state", e);
            }
        }

        return this.name;
    }

    @Override
    public @Nullable String getPrefix() throws IllegalStateException {
        if (this.prefix == null) {
            try {
                this.prefix = this.getParser().getAttributePrefix(this.index);
            } catch (final IndexOutOfBoundsException e) {
                throw new IllegalStateException("Parser is in an invalid state", e);
            }
        }

        return this.prefix;
    }

    @Override
    public @NonNull String getType() throws IllegalStateException {
        if (this.type == null) {
            try {
                this.type = this.getParser().getAttributeType(this.index);
            } catch (final IndexOutOfBoundsException e) {
                throw new IllegalStateException("Parser is in an invalid state", e);
            }
        }

        return this.type;
    }

    @Override
    public @NonNull String getValue() throws IllegalStateException {
        if (this.value == null) {
            try {
                this.value = this.getParser().getAttributeValue(this.index);
            } catch (final IndexOutOfBoundsException e) {
                throw new IllegalStateException("Parser is in an invalid state", e);
            }
        }

        return this.value;
    }

    @Override
    public boolean isDefault() throws IllegalStateException {
        if (this.isDefault == null) {
            this.isDefault = this.getParser().isAttributeDefault(this.index);
        }

        return this.isDefault;
    }

    protected @NonNull P getParser() throws IllegalStateException {
        final P parser = this.parser.get();

        if (parser == null) {
            throw new IllegalStateException("Parser is in an invalid state");
        }

        return parser;
    }
}
